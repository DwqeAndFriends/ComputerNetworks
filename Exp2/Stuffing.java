package com.company;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class Stuffing {
    private static String PPPByteStuffing(String InfoString1, String flagString, String ESC) {
        StringBuilder newString = new StringBuilder();
        char[] infoChars = InfoString1.toCharArray();
        for (int i = 0; i < infoChars.length; i++) {
            if(infoChars[i] == flagString.charAt(0) && infoChars[i + 1] == flagString.charAt(1)) {
                newString.append(ESC);
                newString.append(flagString);
                ++i;
            }
            else if(infoChars[i] == ESC.charAt(0) && infoChars[i + 1] == ESC.charAt(1)) {
                newString.append(ESC);
                newString.append(ESC);
                ++i;
            }
            else {
                newString.append(infoChars[i]);
            }
        }
        return flagString + newString.toString() + flagString;
    }

    private static String ByteGetOriginalString(String receivedString, String flagString, String ESC) {
        char[] receivedChars = receivedString.toCharArray();
        StringBuilder originalString = new StringBuilder();
        boolean startFlag = false;
        for(int i = 0; i < receivedChars.length; i++) {
            if(receivedChars[i] == ESC.charAt(0) && receivedChars[i + 1] == ESC.charAt(1)) {
                if(startFlag) {
                    originalString.append(receivedChars[i + 2]);
                    originalString.append(receivedChars[i + 3]);
                }
                i += 3;
            }
            else if(receivedChars[i] == flagString.charAt(0) && receivedChars[i + 1] == flagString.charAt(1)) {
                if(!startFlag) {
                    startFlag = true;
                    ++i;
                }
                else {
                    break;
                }
            }
            else {
                if(startFlag) {
                    originalString.append(receivedChars[i]);
                }
            }
        }
        return originalString.toString();
    }

    private static String ZeroBitStuffing(String InfoString1, String flagString) {
        StringBuilder newString = new StringBuilder();
        int one_cnt = 0; //int ist_cnt = 0;
        char[] infoChars = InfoString1.toCharArray();
        for (char ch : infoChars) {
            newString.append(ch);
            if (ch == '1') {
                one_cnt++;
                if (one_cnt == 5) {
                    newString.append('0');
                    one_cnt = 0;
                    //ist_cnt++;
                }
            }
            else {
                one_cnt = 0;
            }
        }
        return flagString + newString.toString() + flagString;
    }

    private static int[] ZeroBitGetFlagIndex(String receivedString, String flagString) {
        int[] index = new int[2];
        index[0] = receivedString.indexOf(flagString) + flagString.length();
        index[1] = receivedString.indexOf(flagString, index[0]);
        return index;
    }

    private static String ZeroBitGetOriginalString(String receivedString, String flagString) {
        int[] Index = ZeroBitGetFlagIndex(receivedString, flagString);
        String tmpString = receivedString.substring(Index[0], Index[1]);
        StringBuilder newString = new StringBuilder(tmpString);
        int delIndex = newString.indexOf("11111", 0);
        while(delIndex != -1) {
            newString.deleteCharAt(delIndex + 5);
            delIndex = newString.indexOf("11111", delIndex + 5);
        }
        return newString.toString();
    }

    public static void main(String[] args) throws IOException {
        Ini ini = new Ini(new File("./Stuffing.ini"));
        System.out.println("字节填充: ");
        String ByteInfoString1 = ini.get("ByteStuffing", "InfoString1");
        String ByteFlagString = ini.get("ByteStuffing", "FlagString");
        String ESC = ini.get("ByteStuffing", "ESC");
        System.out.print("帧起始结束标志: ");
        System.out.println(ByteFlagString);
        System.out.print("转义字符ESC: ");
        System.out.println(ESC);
        System.out.print("待填充数据帧: ");
        System.out.println(ByteInfoString1);
        String ByteSendString = PPPByteStuffing(ByteInfoString1, ByteFlagString, ESC);
        System.out.print("字节填充后发送帧: ");
        System.out.println(ByteSendString);
        String ByteReceiveString = "2312FF7E676AD" + ByteSendString;

        System.out.print("\n包含其他字符的接收帧: ");
        System.out.println(ByteReceiveString);
        String ByteOriginalString = ByteGetOriginalString(ByteReceiveString, ByteFlagString, ESC);
        System.out.print("接收帧字节删除后数据帧: ");
        System.out.println(ByteOriginalString);

        System.out.println("\n-----------------------------------------------------------\n");
        System.out.println("零比特填充: ");
        String ZeroBitInfoString1 = ini.get("ZeroBitStuffing", "InfoString1");
        String ZeroBitFlagString = ini.get("ZeroBitStuffing", "FlagString");
        System.out.print("帧起始结束标志: ");
        System.out.println(ZeroBitFlagString);
        System.out.print("待填充数据帧: ");
        System.out.println(ZeroBitInfoString1);
        String ZeroBitSendString = ZeroBitStuffing(ZeroBitInfoString1, ZeroBitFlagString);
        System.out.print("零比特填充后数据帧: ");
        System.out.println(ZeroBitSendString);


        System.out.print("\n包含其他比特的接收帧: ");
        String ZeroBitReceiveString = "01100" + ZeroBitSendString;
        System.out.println(ZeroBitReceiveString);
        String ZeroBitOriginalString = ZeroBitGetOriginalString(ZeroBitReceiveString, ZeroBitFlagString);
        System.out.print("接收帧零比特删除后数据帧: ");
        System.out.println(ZeroBitOriginalString);
    }
}