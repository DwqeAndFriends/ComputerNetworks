#! python3
# coding: utf-8

from winpcapy import *
from scapy.all import *

#打印网络适配器信息#
i = 0
with WinPcapDevices() as devices:
    for device in devices:
        i = i + 1
        print("[",i,"]","name:",device.name.decode(),"description:",device.description.decode())

#选择捕获适配器编号#
i = 0
index = input("Enter the adapter number to query:")
with WinPcapDevices() as devices:
    for device in devices:
        i = i + 1
        if i == int(index):
            myDevice = device.name.decode()
print("please wait...")

num = 0
packets = sniff(iface=myDevice,count=10,timeout=1000)
for p in packets:
    num = num + 1
    #打印报文#
    print("--------------packet %d--------------\n"%(num))
    ls(p)
    print("\n")
    p.show()
    
