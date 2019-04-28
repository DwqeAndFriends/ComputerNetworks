from io import StringIO
import numpy as np
import configparser

def ZeroBitStuffing(InfoString1,flagString):   
    newString=""
    one_cnt = 0; 
    infoChars = list(InfoString1)
    for ch in infoChars:
        newString=newString+ch
        if ch == '1':
            one_cnt=one_cnt+1
            if one_cnt == 5:
                newString=newString+'0'
                one_cnt = 0
        else:
            one_cnt = 0
    return flagString + newString + flagString

def ZeroBitGetFlagIndex(receivedString,flagString):
    index=np.array([0,0])
    index[0] = receivedString.find(flagString) + len(flagString)
    index[1] = receivedString.find(flagString, index[0])
    return index;

def ZeroBitGetOriginalString(receivedString,flagString):
    Index = ZeroBitGetFlagIndex(receivedString, flagString)
    tmpString = receivedString[Index[0]:Index[1]]
    newString = tmpString
    delIndex = newString.find("11111", 0)
    while delIndex != -1:
        newString=newString[:delIndex + 5]+newString[delIndex +6:]
        delIndex = newString.find("11111", delIndex + 5)
    return newString

def PPPByteStuffing(InfoString1,flagString,ESC):
    newString=""
    infoChars = list(InfoString1)
    i=0
    while i<len(infoChars):   
        if infoChars[i] == flagString[0] and infoChars[i + 1] == flagString[1]:
            newString=newString+ESC
            newString=newString+flagString
            i=i+2
        elif infoChars[i] == ESC[0] and infoChars[i + 1] == ESC[1]:
            newString=newString+ESC
            newString=newString+ESC
            i=i+2
        else:
            newString=newString+infoChars[i]
            i=i+1
    return flagString + newString + flagString    

def ByteGetOriginalString(receivedString,flagString,ESC):
    receivedChars = list(receivedString)
    originalString = ""
    startFlag = False
    i=0
    while i< len(receivedChars): 
        if receivedChars[i] == ESC[0] and receivedChars[i + 1] == ESC[1]:
            if startFlag==True:
                originalString=originalString+receivedChars[i + 2]
                originalString=originalString+receivedChars[i + 3]
            i=i+4
        elif receivedChars[i] == flagString[0] and receivedChars[i + 1] == flagString[1]:
            if startFlag==False:
                startFlag = True
                i=i+2
            else:
                break;
        elif startFlag==True:
            originalString=originalString+receivedChars[i]
            i=i+1
        else:
            i=i+1
    return originalString

config = configparser.ConfigParser()
config.readfp(open('Stuffing.ini'))
ByteInfoString1 = config.get("ByteStuffing","InfoString1")
ByteFlagString = config.get("ByteStuffing","FlagString")
ESC = config.get("ByteStuffing","ESC")
print("字节填充: ")
print("帧起始结束标志: ",ByteFlagString)
print("转义字符ESC: ",ESC)
print("待填充数据帧: ",ByteInfoString1)
ByteSendString = PPPByteStuffing(ByteInfoString1, ByteFlagString, ESC)
print("字节填充后发送帧: ",ByteSendString)
ByteReceiveString = "2312FF7E676AD" + ByteSendString
print("\n")
print("包含其他字符的接收帧: ",ByteReceiveString)
ByteOriginalString = ByteGetOriginalString(ByteReceiveString, ByteFlagString, ESC)
print("接收帧字节删除后数据帧: ",ByteOriginalString)
print("\n")
print("-----------------------------------------------------------")
print("\n")
print("零比特填充: ")
ZeroBitInfoString1 = config.get("ZeroBitStuffing","InfoString1")
ZeroBitFlagString=config.get("ZeroBitStuffing","FlagString")
print("帧起始结束标志: ",ZeroBitFlagString)
print("待填充数据帧: ",ZeroBitInfoString1)
ZeroBitSendString = ZeroBitStuffing(ZeroBitInfoString1, ZeroBitFlagString)
print("零比特填充后数据帧: ",ZeroBitSendString)
print("\n")
ZeroBitReceiveString = "01100" + ZeroBitSendString
print("包含其他比特的接收帧: ",ZeroBitReceiveString)
ZeroBitOriginalString = ZeroBitGetOriginalString(ZeroBitReceiveString, ZeroBitFlagString)
print("接收帧零比特删除后数据帧: ",ZeroBitOriginalString)
input("Please Enter...")
