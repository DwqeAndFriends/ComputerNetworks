package com.DwqeGroup.UDP;

import com.DwqeGroup.CRC.CRC_Pro;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



public class Sender {
    private static final int myPort = 7777;
    private static final int hisPort = 8888;
    public static byte[] makeError(byte[] a){
        byte[] res=a;
        Random r=new Random();
        int index=r.nextInt(100)%res.length;
        if(res[index]=='0')
            res[index]='1';
        else res[index]='0';
    return res;
    }
    public static void main(String[] args) throws Exception {
        Queue<String> network_data = new LinkedList<>();
        Random ra=new Random();
        for(int i=0;i<10;i++){
            char tem[]= new char[12];
            for(int j=0;j<10;j++){
                
                tem[j]=(char) (ra.nextInt(2)); 
            }
            tem[10]='\0';
            String t=new String(tem);
            network_data.offer(t);
        }        
        DatagramSocket datagramSocket = new DatagramSocket(myPort);
        DatagramPacket sendPacket;
        InetAddress inetAddress = InetAddress.getLocalHost();


        byte next_frame_to_send;
        byte[] s = new byte[35];
        next_frame_to_send = 0;

        String buffer = network_data.poll();    

        while (true) {
            if (buffer != null) {
                char[] bufferChars = buffer.toCharArray();
                int remainder = CRC_Pro.CRC_Remainder(bufferChars);
                char[] tmp = CRC_Pro.CRC_SendString(bufferChars, remainder);
                byte[] CRC_data = CRC_Pro.CharsToBytes(tmp);
                System.arraycopy(CRC_data, 0, s, 1, CRC_data.length);
            }
            s[0] = next_frame_to_send;
            //System.out.println(Arrays.toString(s));
            System.out.println("Current frame:"+s[0]);
            boolean errflag=false;
            if(ra.nextDouble()<0.2){
                s=makeError(s);
                errflag=true;
            }
            if(ra.nextDouble()<0.8){
                
                sendPacket = new DatagramPacket(s, s.length, inetAddress, hisPort);   
                datagramSocket.send(sendPacket);
                if(errflag==false) {
                	System.out.println("Transmission correct");
                }
                else {
                	System.out.println("Transmission error");
                }
            }
            else{
                System.out.println("Frame lost");
            }
            byte[] ack = new byte[1];
            DatagramPacket ackPacket = new DatagramPacket(ack, ack.length);
            datagramSocket.setSoTimeout(2000);    
            try {
                datagramSocket.receive(ackPacket);
                System.out.println("Acknowledgement frame NO:"+ack[0]);
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
            System.out.println("next_frame_to_send:"+next_frame_to_send);
            System.out.println("");
        }

        System.out.println("Sending finished");
    }
}
