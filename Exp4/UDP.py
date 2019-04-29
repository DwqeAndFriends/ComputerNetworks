
#!/usr/bin/python
import threading
import math
import time
import numpy as np
import socket
import random
import queue
import configparser

BUFSIZE = 1024
GenXString = "10001000000100001"
datasend = ["100000","1010010","1000000","1010101010","00001","10101010","101010","101010101010"]
buffered = 0
next_frame_send = 0
frame_expected = 0
ack_expected = 0
max_seq = 5
time_out = False
timelimit = 2
timer_queue = []

class Timer:
    frame_num = -1
    start_time = -1

def GetRemainder(newString, GenXString):
    r = len(GenXString) - 1
    SubString = newString[0:r]
    mod = int(SubString,2)
    divisor = int(GenXString, 2)
    for i in range(r,len(newString)):
        mod = mod * 2 + int(newString[i], 2)
        if mod >= math.pow(2, r):
            mod ^= divisor
        else:
            mod ^=0
    return mod

def GetSendString(InfoString1, GenXString):
    rr = len(GenXString) - 1
    newString = InfoString1
    for i in range(0, rr):
        newString = newString + "0"
    mod = GetRemainder(newString, GenXString)
    #print("CRC-Code = ",bin(mod).replace("0b",""))
    tmp = int(newString, 2)
    return bin(mod ^ tmp).replace("0b","")

def make_error(msg):
	new_string = list(msg)
	index = random.randint(0,len(msg) - 1) 
	if new_string[index] == '0':
		new_string[index] = '1'
	elif new_string[index] == '1':
		new_string[index] = '0'
	return ''.join(new_string)

def stop_timer(frame):
    global timer_queue
    number = 0
    if timer_queue:
        number = timer_queue[0].frame_num
    if number == frame:
        timer_queue.pop(0)
    
def start_timer(frame):
    global timer_queue
    newtimer = Timer()
    newtimer.frame_num = frame
    newtimer.start_time = time.time()
    timer_queue.append(newtimer)

def SendThread(threadName):
    global BUFSIZE,GenXString
    global datasend
    global buffered
    global next_frame_send,frame_expected,ack_expected
    global max_seq
    global time_out
    global timelimit
    global hisAddr,myAddr

    print("sending")
    time.sleep(1)
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # 创建 socket 对象
    s.bind(myAddr)
    
    while True:
        if time_out:
            print("time_out!")
            tmpbuf = buffered
            tmpack = ack_expected
            for i in range(0,tmpbuf):
                #CRC处理#
                errflag = False
                if (tmpack+i) > 7:
                    break
                data = GetSendString(datasend[tmpack+i],GenXString)
                if random.randint(0,99)< 10:
                    data = make_error(data)
                    errflag = True
                sendData = data+"/"+str(tmpack+i)+"/"+str(frame_expected-1)
                
                if random.randint(0,99)< 90:
                    s.sendto(sendData.encode(), hisAddr)
                    if errflag == False:
                        print("Filter: Correct transmission.")
                    else:
                        print("Filter: Transmission error.")
                else:
                    print("Filter: Frame lost.")

                print("resend: ",data)
                stop_timer(tmpack+i)
                start_timer(tmpack+i)
            
            time_out = False

        else:
            if buffered < max_seq:
                if next_frame_send > 7:
                    next_frame_send = 7
                if frame_expected == 8 and ack_expected == 8:
                    break
                errflag = False
                data = GetSendString(datasend[next_frame_send],GenXString)
                if random.randint(0,99)< 10:
                    data = make_error(data)
                    errflag = True
                sendData = data+"/"+str(next_frame_send)+"/"+str(frame_expected-1)                
                
                if random.randint(0,99)< 90:
                    s.sendto(sendData.encode(), hisAddr)
                    if errflag == False:
                        print("Filter: Correct transmission.")
                    else:
                        print("Filter: Transmission error.")
                else:
                    print("Filter: Frame lost.")

                print("send: ",data)
                start_timer(next_frame_send)
                buffered = buffered + 1
                next_frame_send = next_frame_send + 1
                time.sleep(1)
            else:
                time.sleep(2)
                continue
        print("")
    print("sending finish")
    s.close()

def ReceiveThread(threadName):
    global BUFSIZE,GenXString
    global datasend
    global buffered
    global next_frame_send,frame_expected,ack_expected
    global max_seq
    global time_out
    global timelimit
    global hisAddr,myAddr

    print("receiving")
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # 创建 socket 对象
    s.bind(hisAddr)

    while True:
        dataPacketReceive = s.recv(BUFSIZE)
        print("dataPacketReceive: ",dataPacketReceive)
        data = dataPacketReceive.decode()
        print("receive: ",data)
        info = data.split("/")
        receivedata = info[0]
        print("info[0]: ",info[0])
        print("receivedata: ",receivedata)
        receiveframe = int(info[1])
        if info[2] == "-1":
            receiveack = -1
        else:
            receiveack = int(info[2])

        #CRC验证#
        mod = GetRemainder(receivedata,GenXString)
        if mod == 0:
            print("Received correct frame: ",receiveframe)
            if receiveframe == frame_expected:
                crcflag = True
                if(crcflag):
                    frame_expected = frame_expected + 1
            if receiveack >= ack_expected:
                for i in range(ack_expected,receiveack):
                    stop_timer(i)
                    buffered = buffered - 1
                ack_expected = receiveack + 1
            if frame_expected == 8 and ack_expected == 8:
                break;
        print("")
    print("receiving finish")
    s.close()

def TimerThread(threadName):
    global time_out,timer_queue,a
    global frame_expected,ack_expected
    while True:
        if time_out == False:
            if len(timer_queue) == 0:
                time.sleep(0.2)
            else:
                nowtime = time.time()
                toptime = 0
                toptime = timer_queue[0].start_time
                dif = nowtime - toptime
                if dif > timelimit:
                    time_out = True
                time.sleep(0.2)
            if frame_expected == 8 and ack_expected == 8:
                break


config = configparser.ConfigParser()
config.readfp(open('GBN.ini'))
myport = int(config.get("Port", "host1Port"))
hisport = int(config.get("Port", "host2Port"))
FilterError = int(config.get("Filter", "FilterError"));
FilterLost = int(config.get("Filter", "FilterLost"))

host = socket.gethostname()  # 获取本地主机名
myAddr = (host, myport)  # 设置地址tuple
hisAddr = (host, hisport)

t1 = threading.Thread(target=SendThread,args=('sender',))
t2 = threading.Thread(target=ReceiveThread,args=('receiver',))
t3 = threading.Thread(target=TimerThread,args=('timer',))
t1.start()
t2.start()
t3.start()

threads = []
threads.append(t1)
threads.append(t2)
threads.append(t3)
for t in threads:
    t.join()
input("Please enter...")
