#!/usr/bin/python3
import math
import socket   
import numpy as np
import random
import time

#N=1000
#LISTEN_PORT=8888
msg_queue=[]
next_frame_to_send = 1 #下一个发送的帧编号
status_flag = 0  #发送状态标志位
msg_count = 0  #标记送到第几帧
seq = 0  #当前帧的编号
recv_flag = 0  #0表示未接受，1表示成功接收返回信息
timeout = 3  #超时时间
send_times = 10  #一共进行十次传输 

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

def handle_returnMsg(msg):
    global next_frame_to_send,seq,recv_flag,msg_count
    if int(msg) == next_frame_to_send:
        print("receive success!")
        if next_frame_to_send == 0:
            seq = 0
            next_frame_to_send = 1
        elif next_frame_to_send == 1:
            seq = 1
            next_frame_to_send = 0
        msg_count = msg_count + 1
        recv_flag = 0
    elif msg == '2':
        print("error occur during the send!")

def make_error(msg):
	new_string = list(msg)
	index = random.randint(0,len(msg) - 1) 
	if new_string[index] == '0':
		new_string[index] = '1'
	elif new_string[index] == '1':
		new_string[index] = '0'
	return ''.join(new_string)

#创建连接
socket.setdefaulttimeout(5)  #超时等待设置
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # 创建 socket 对象
host = socket.gethostname()  # 获取本地主机名
port = 12345  # 设置端口
addr = (host, port)  # 设置地址tuple
s.bind(addr)  # 绑定端口

GenXString = "10001000000100001"
for i in range(send_times):
    msg_queue.append("")
    for j in range(32):
        msg_queue[i] += str(random.randint(0,1))
    print("msg", i, ": ", msg_queue[i])

while True:
    try:
        data,addr = s.recvfrom(1024);
        print(data.decode()+"\n")
        break
    except socket.timeout as e:
        print("connect",e)

while True:
    if msg_count >= send_times or msg_queue[msg_count] == "":
        break
    else:
        origin_msg = msg_queue[msg_count]
    CRC_processed_msg = GetSendString(origin_msg, GenXString)
    
    if random.randint(0,99)< 20:
        CRC_processed_msg = make_error(CRC_processed_msg)
        status_flag = 1
        print("message", msg_count, " is supposed to be error!")
    prepared_msg = str(seq) + CRC_processed_msg
    print(prepared_msg)
    if random.randint(0,99)< 20 and status_flag != 1:
        status_flag = 2
        print("msg", msg_count, " is suppoesd to be lost.")
    else:
        if s.sendto(prepared_msg.encode(),addr):
            print("message", msg_count, " : send successfully!")
    try:
        recvDatab,addr = s.recvfrom(1024)  #接收
        recvData = recvDatab.decode()
        print("从接收端返回：",recvData[0])
        handle_returnMsg(recvData[0])
    except socket.timeout as e:
        print("Package lost, resend temp package!")
    print("--------------------------------------------------------------\n")
    time.sleep(2)

s.sendto("exit".encode(),addr)    
s.close()  # 关闭连接
