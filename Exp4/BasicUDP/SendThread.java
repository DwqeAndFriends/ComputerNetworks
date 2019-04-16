package com.DwqeGroup.UDP;

import java.io.IOException;
import java.net.*;

public class SendThread extends Thread  {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    SendThread(DatagramSocket dS, DatagramPacket dP) {
        datagramSocket = dS;
        datagramPacket = dP;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                datagramSocket.send(datagramPacket);
                Thread.sleep(100);
            }
        }
        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
