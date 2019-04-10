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
    tmp = int(newString, 2)
    return bin(mod ^ tmp).replace("0b","")


InfoString1 = "1101011111"
GenXString = "10011"
SendString = GetSendString(InfoString1, GenXString)
print("SendString: ", SendString)
cmp = GetRemainder(SendString, GenXString)
print("cmp: ", cmp)
input("Please enter...")
