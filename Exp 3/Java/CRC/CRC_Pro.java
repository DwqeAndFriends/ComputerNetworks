package com.DwqeGroup.CRC;

public class CRC_Pro {
    public static void main(String[] args) {
        String InfoString = "计算机网络CRC-CCITT校验";
        System.out.println("待发送字符串: " + InfoString);
        char[] InfoChars = InfoString.toCharArray();
        System.out.println("校验发送字符串：");
        int remainder = CRC_Pro.CRC_Remainder(InfoChars);
        System.out.println("CRC-CCITT余数: " + "0x" + Integer.toHexString(remainder).toUpperCase());

        char[] SendString = CRC_Pro.CRC_SendString(InfoChars, remainder);

        System.out.print("\n已接收字符串: ");
        System.out.println(SendString);
        System.out.println("校验接收字符串: ");
        int check = CRC_Pro.CRC_Remainder(SendString);
        System.out.print("校验结果: ");
        System.out.println(check);
    }

    private static byte[] CharToTwoBytes(char ch) {
        byte[] b2 = new byte[2];
        b2[0] = (byte)((ch & 0xFF00) >> Byte.SIZE);
        b2[1] = (byte)(ch & 0x00FF);
        return b2;
    }

    public static byte[] CharsToBytes(char[] str) {
        byte[] bs = new byte[str.length *2];
        for(int i = 0; i < str.length; i++) {
            byte[] b2 = CharToTwoBytes(str[i]);
            bs[i * 2] = b2[0];
            bs[i * 2 + 1] = b2[1];
        }
        return bs;
    }

    private static String BytesToHexString(byte[] bs) {
        StringBuilder hexString = new StringBuilder();
        hexString.append("0x");
        for(byte b : bs) {
            hexString.append(Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase());
        }
        return hexString.toString();
    }

    public static int CRC_Remainder(char[] str) {
        byte[] bytes = CRC_Pro.CharsToBytes(str);
        int crc = CRC_Remainder(bytes);
        return crc;
    }

    public static int CRC_Remainder(byte[] bytes) {
        // System.out.println("校验字符串十六进制表示: " + BytesToHexString(bytes));
        int crc = 0xFFFF;
        int polynomial = 0x1021; //0001 0000 0010 0001

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = (b >> (7 - i) & 1) == 1;
                boolean c15 = (crc >> 15 & 1) == 1;
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xFFFF;
        return crc;
    }

    public static char[] CRC_SendString(char[] str, int remainder) {
        int l = str.length;
        char[] new_str = new char[l + 1];
        System.arraycopy(str, 0, new_str, 0, l);
        new_str[l] = (char)remainder;
        return new_str;
    }

    public static byte[] CRC_SendBytes(byte[] bytes, int remainder) {
        int l = bytes.length;
        byte[] sendBytes = new byte[l + 2];
        System.arraycopy(bytes, 0, sendBytes, 0, l);
        sendBytes[l] = (byte)((remainder & 0xFF00) >> 8);
        sendBytes[l + 1] = (byte)(remainder & 0x00FF);
        return sendBytes;
    }

    private static char TwoBytesToChar(byte[] b2) {
        return (char)(((char)b2[0] << Byte.SIZE) | (0x00FF & (char)b2[1]));
    }

    public static char[] BytesToChars(byte[] bs) {
        char[] str = new char[bs.length / 2];
        for(int i = 0; i < bs.length; i += 2) {
            byte[] b2 = new byte[2];
            b2[0] = bs[i];
            b2[1] = bs[i + 1];
            str[i / 2] = TwoBytesToChar(b2);
        }
        return str;
    }
}
