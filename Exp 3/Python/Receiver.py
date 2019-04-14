#!/usr/bin/python3
import math
import socket 
expected_frame = 0

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

def isCRC(InfoString2,GenXString):
	mod = GetRemainder(InfoString2, GenXString)
	if mod == 0:
		return True
	else:
		return False

def handle_msg(msg):
    global expected_frame
    GenXString = "10001000000100001"
    return_msg = ""
    origin_msg = ""
    msg = list(msg)
    for i in range(len(msg)-1):
        origin_msg = origin_msg + msg[i + 1]
    if isCRC(origin_msg, GenXString) == False:
        return_msg = return_msg + '2'
        print("CRC error")
    elif msg[0] ==str(expected_frame):
        print("接收成功")
        if expected_frame == 0:
            return_msg = return_msg + '1'
            expected_frame = 1
        else:
            return_msg = return_msg + '0';
            expected_frame = 0
    return_msg = return_msg + '\0'
    return  return_msg

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # 创建 socket 对象
host = socket.gethostname()  # 获取本地主机名
port = 12345  # 设置端口
addr = (host, port)
s.connect(addr)  # 绑定端口号
print("bind succes!")
while True:
    return_msg=[]
    recvData1 = s.recv(1024)
    #与sender连接已断开
    if recvData1 == b'':
        break
    recvData=recvData1.decode()
    print(recvData)
    #处理接收到的消息，发送确认消息给发送方
    temp_msg = ""
    temp_msg = handle_msg(recvData)
    temp_msg = list(temp_msg)
    for i in range(len(temp_msg)):
        return_msg.append(temp_msg[i])
    return_msg.append('\0')
    print("return_msg：", return_msg[0]);
    print("---------------------------------------------------")
    return_msg=''.join(return_msg)
    s.sendall(return_msg.encode())

s.close()  # 关闭连接
