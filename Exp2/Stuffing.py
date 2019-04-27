from io import StringIO
import numpy as np

def ZeroBitStuffing(InfoString1):   
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
    return "01111110" + newString + "01111110"

def ZeroBitGetFlagIndex(receivedString,flagString):
    index=np.array([0,0])
    index[0] = receivedString.find(flagString) + len(flagString)
    index[1] = receivedString.find(flagString, index[0])
    return index;

def ZeroBitGetOriginalString(receivedString):
    Index = ZeroBitGetFlagIndex(receivedString, "01111110")
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


InfoString1 = "347D7E807E4//0AA7D"
FlagString = "7E"
ESC = "//"
print(InfoString1)
SendString1 = "2312//7E676AD" + PPPByteStuffing(InfoString1, FlagString, ESC)
print(SendString1)
OriginalString = ByteGetOriginalString(SendString1, FlagString, ESC)
print(OriginalString)
InfoString2 = "000000111111111111111111000"
print(InfoString2)
SendString2 = ZeroBitStuffing(InfoString2)
print("01100" + SendString2)
ReceiveString = ZeroBitGetOriginalString("01100" + SendString2)
print(ReceiveString)
input("Please Enter...")

