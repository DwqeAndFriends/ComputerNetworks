package com.DwqeGroup.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Host {
    private static int myPort = 7777;
    private static int hisPort = 8888;
    private static byte[] dataReceive = new byte[2048];
    private static byte[] dataSend = new byte[2048];
    public static void main(String[] args) throws Exception {
	// write your code here
        dataSend[0] = 123; dataSend[1] = 127; dataSend[2] = -128; dataSend[3] = 34; dataSend[4] = 77;

        DatagramSocket datagramSocket = new DatagramSocket(myPort);
        InetAddress inetAddress = InetAddress.getLocalHost();

        DatagramPacket datagramPacketSend = new DatagramPacket(dataSend, dataSend.length, inetAddress, hisPort);
        SendThread sendThread = new SendThread(datagramSocket, datagramPacketSend);
        sendThread.start();

        DatagramPacket datagramPacketReceive = new DatagramPacket(dataReceive, dataReceive.length);
        ReceiveThread receiveThread = new ReceiveThread(datagramSocket, datagramPacketReceive);
        receiveThread.start();
        System.out.println("Host is running");
    }
}
