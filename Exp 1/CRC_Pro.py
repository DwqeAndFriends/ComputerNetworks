#!/usr/bin/python3
import math

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
    print("CRC-Code = ",bin(mod).replace("0b",""))
    tmp = int(newString, 2)
    return bin(mod ^ tmp).replace("0b","")


print("Sender:")
InfoString1 = "11011010110010010100010110"
GenXString = "10001000000100001"    #17位
print("InfoString1 = ",InfoString1)
print("GenXString = ",GenXString)
SendString = GetSendString(InfoString1, GenXString)
print("SendString = ", SendString)
print("\n")
print("Receiver:")
InfoString2 = SendString
#InfoString2 = SendString[:-1] + str(int(SendString[-1]) ^ 1)  #最后一位取反
cmp = GetRemainder(InfoString2, GenXString)
print("InfoString2 = ",InfoString2)
print("CRC-Code = ", bin(cmp).replace("0b",""))
if cmp == 0:
    print("Correct result.")
else:
    print("Wrong result.")
input("Please enter...")
