#!/usr/bin/python3
import math
import configparser

def GetRemainder(newString, GenXString):
    r = len(GenXString) - 1
    SubString = newString[0:r]
    mod = int(SubString, 2)
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
    print("生成循环冗余校验码CRC-Code：",bin(mod).replace("0b",""))
    tmp = int(newString, 2)
    return bin(mod ^ tmp).replace("0b","")


config = configparser.ConfigParser()
config.readfp(open('CRC.ini'))

#Sender#
InfoString1 = config.get("Strings","InfoString1")
GenXString = config.get("Strings","GenXString")
print("CRC-CCITT二进制比特串：",GenXString)
print("\n")
print("待发送的数据信息二进制比特串：",InfoString1)
SendString = GetSendString(InfoString1, GenXString)
print("带校验和的发送帧：", SendString)
print("\n")

#Receiver#
InfoString2 = config.get("Strings","InfoString2")
print("接收数据信息二进制比特串：",InfoString2)
cmp = GetRemainder(InfoString2, GenXString)
print("余数：",cmp)
if cmp == 0:
    print("校验无错")
else:
    print("校验有错")
input("Please enter...")
