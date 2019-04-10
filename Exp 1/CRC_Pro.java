class CRC {
    /*
    private static char TwoBytesToChar(byte[] b2) {
        return (char)(((char)b2[0] << Byte.SIZE) | (0x00FF & (char)b2[1]));
    }
    */

    private static byte[] CharToTwoBytes(char ch) {
        byte[] b_2 = new byte[2];
        b_2[0] = (byte)((ch & 0xFF00) >> Byte.SIZE);
        b_2[1] = (byte)(ch & 0x00FF);
        return b_2;
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

    /*
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

    static int CRC_Remainder(String str) {
        byte[] bytes = CRC.CharsToBytes(str.toCharArray());
        int crc = 0xFFFF;
        int polynomial = 0x1021;

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xFFFF;
        return crc;
    }

    static String CRC_SendString(String str, int remainder) {
        return str + (char)remainder;
    }
}

/* test
public class CRC_Pro {
    public static void main(String[] args) {
        String InfoString = "Hello world!";
        System.out.println(InfoString);
        int remainder = CRC.CRC_Remainder(InfoString);
        System.out.println(Integer.toHexString(remainder));
        String nString = CRC.CRC_SendString(InfoString, remainder);
        System.out.println(nString);
        int check = CRC.CRC_Remainder(nString);
        System.out.println(check);
    }
}
*/