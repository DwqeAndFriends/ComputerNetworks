public class CRC_Pro {
    public static void main(String[] args) {
        
        String InfoString = "计算机网络CRC-CCITT校验";
        System.out.println("待发送字符串: " + InfoString);
        char[] InfoChars = InfoString.toCharArray();
        System.out.println("校验发送字符串：");
        int remainder = CRC.CRC_Remainder(InfoChars);
        System.out.println("CRC-CCITT余数: " + "0x" + Integer.toHexString(remainder).toUpperCase());

        char[] SendString = CRC.CRC_SendString(InfoChars, remainder);

        System.out.print("\n已接收字符串: ");
        System.out.println(SendString);
        System.out.println("校验接收字符串: ");
        int check = CRC.CRC_Remainder(SendString);
        System.out.print("校验结果: ");
        System.out.println(check);
    }
}

class CRC {
    private static byte[] CharToTwoBytes(char ch) {
        byte[] b2 = new byte[2];
        b2[0] = (byte)((ch & 0xFF00) >> Byte.SIZE);
        b2[1] = (byte)(ch & 0x00FF);
        return b2;
    }

    private static byte[] CharsToBytes(char[] str) {
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

    static int CRC_Remainder(char[] str) {
        byte[] bytes = CRC.CharsToBytes(str);
        System.out.println("校验字符串十六进制表示: " + BytesToHexString(bytes));
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

    static char[] CRC_SendString(char[] str, int remainder) {
        int l = str.length;
        char[] new_str = new char[l + 1];
        System.arraycopy(str, 0, new_str, 0, l);
        new_str[l] = (char)remainder;
        return new_str;
    }

    /*
    private static char TwoBytesToChar(byte[] b2) {
        return (char)(((char)b2[0] << Byte.SIZE) | (0x00FF & (char)b2[1]));
    }
    static char[] BytesToChars(byte[] bs) {
        char[] str = new char[bs.length / 2];
        for(int i = 0; i < bs.length; i += 2) {
            byte[] b2 = new byte[2];
            b2[0] = bs[i];
            b2[1] = bs[i + 1];
            str[i / 2] = TwoBytesToChar(b2);
        }
        return str;
    }
    */
}