package com.DwqeGroup.UDP;

import com.DwqeGroup.CRC.CRC_Pro;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Receiver {
    private static final int myPort = 8888;
    private static final int hisPort = 7777;

    public static void main(String[] args) throws Exception {
        // write your code here
        DatagramSocket datagramSocket = new DatagramSocket(myPort);
        DatagramPacket receivePacket;
        InetAddress inetAddress = InetAddress.getLocalHost();

        byte frame_excepted;
        byte[] r = new byte[35];
        frame_excepted = 0;

        System.out.println("Start");
        do {
            receivePacket = new DatagramPacket(r, r.length);
            datagramSocket.receive(receivePacket);
            byte[] rack = new byte[1];
            DatagramPacket ackP = new DatagramPacket(rack, rack.length, inetAddress, hisPort);
            if (r[0] == frame_excepted) {
                System.out.println(Arrays.toString(r));
                byte[] receive_data = new byte[34];
                System.arraycopy(r, 1, receive_data, 0, 34);
                int mod = CRC_Pro.CRC_Remainder(receive_data);
                if (mod == 0) {
                    rack[0] = 1;
                    frame_excepted += 1;
                    byte[] tmp = new byte[32];
                    System.arraycopy(receive_data, 0, tmp, 0, 32);
                    System.out.print(CRC_Pro.BytesToChars(tmp));
                    System.out.println(" received");
                }
            }
            datagramSocket.send(ackP);
            if (frame_excepted == 5) {
                System.out.println("waiting");
                Thread.sleep(5000);
            }
        } while (frame_excepted != 10);
        System.out.println("Receiving finished");
    }
}
