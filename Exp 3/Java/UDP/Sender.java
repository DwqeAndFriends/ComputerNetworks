package com.DwqeGroup.UDP;

import com.DwqeGroup.CRC.CRC_Pro;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Sender {
    private static final int myPort = 7777;
    private static final int hisPort = 8888;

    public static void main(String[] args) throws Exception {
        // write your code here
        Queue<String> network_data = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            network_data.offer("Message" + Integer.toString(i) + ": Hello!");
        }
                
        DatagramSocket datagramSocket = new DatagramSocket(myPort);
        DatagramPacket sendPacket;
        InetAddress inetAddress = InetAddress.getLocalHost();


        byte next_frame_to_send;
        byte[] s = new byte[35];
        next_frame_to_send = 0;

        String buffer = network_data.poll();    //从网络层队列获取数据

        while (true) {
            if (buffer != null) {
                char[] bufferChars = buffer.toCharArray();
                int remainder = CRC_Pro.CRC_Remainder(bufferChars);
                char[] tmp = CRC_Pro.CRC_SendString(bufferChars, remainder);
                byte[] CRC_data = CRC_Pro.CharsToBytes(tmp);
                System.arraycopy(CRC_data, 0, s, 1, CRC_data.length);
            }
            s[0] = next_frame_to_send;
            System.out.println(Arrays.toString(s));

            sendPacket = new DatagramPacket(s, s.length, inetAddress, hisPort);    //发送数据
            datagramSocket.send(sendPacket);

            byte[] ack = new byte[1];
            DatagramPacket ackPacket = new DatagramPacket(ack, ack.length);
            datagramSocket.setSoTimeout(2000);    //设置等待时间
            try {
                datagramSocket.receive(ackPacket);
                if (ack[0] == 1) {
                    if (network_data.size() == 0) {
                        break;
                    }
                    else {
                        buffer = network_data.poll();
                        next_frame_to_send += 1;
                    }
                }
            }
            catch (SocketTimeoutException e) {
                System.out.println("Time out");
            }
            finally {
                Thread.sleep(1998);
            }
        }

        System.out.println("Sending finished");
    }
}
