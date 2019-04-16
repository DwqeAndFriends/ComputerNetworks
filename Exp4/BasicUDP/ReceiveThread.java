package com.DwqeGroup.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ReceiveThread extends Thread {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    ReceiveThread(DatagramSocket dS, DatagramPacket dP) {
        datagramSocket = dS;
        datagramPacket = dP;
    }

    @Override
    public void run() {
        try{
            for(int i = 0; i < 10; i++) {
                datagramSocket.receive(datagramPacket);
                System.out.println(Arrays.toString(datagramPacket.getData()));
                System.out.println(i + "爸爸收到了");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
