import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class CRC {

    private static long  GetRemainder(String newString, String GenXString) {
        int r = GenXString.length() - 1;
        String subString = newString.substring(0, r);
        long mod = Long.parseLong(subString, 2);
        long divisor = Long.parseLong(GenXString, 2);
        for(int i = r; i < newString.length(); i++) {
            mod = mod * 2  + newString.charAt(i) - '0';
            if(mod >= (1 << r)) {
                mod ^= divisor;
            }
            else {
                mod ^= 0;
            }
        }
        return mod;
    }

    private static String GetSendString(String InfoString1, String GenXString) {
        int rr = GenXString.length() - 1;
        StringBuilder sbu = new StringBuilder(rr);
        for(int i = 0; i < rr; i++) sbu.append('0');
        String newString = InfoString1 + sbu.toString();
        long mod = GetRemainder(newString, GenXString);
        long tmp = Long.parseLong(InfoString1, 2) << rr;
        String SendStr = Long.toBinaryString(tmp ^ mod);
        System.out.print("生成循环冗余校验码CRC-Code: ");
        System.out.println(SendStr.substring(32));
        return SendStr;
    }

    public static void main(String[] argv) throws IOException {
        Ini ini = new Ini(new File("CRC.ini"));
        String GenXString = ini.get("Strings","GenXString");
        String InfoString1 = ini.get("Strings", "InfoString1");
        String InfoString2 = ini.get("Strings", "InfoString2");

        System.out.print("CRC-CCITT二进制比特串: ");
        System.out.println(GenXString);

        System.out.println();
        System.out.print("待发送的数据信息二进制比特串: ");
        System.out.println(InfoString1);
        String SendString = GetSendString(InfoString1, GenXString);
        System.out.print("带校验和的发送帧: ");
        System.out.println(SendString);

        System.out.println();
        System.out.print("接收数据信息二进制比特串: ");
        System.out.println(InfoString2);
        long cmp = GetRemainder(InfoString2, GenXString);
        System.out.print("余数: ");
        System.out.println(Long.toBinaryString(cmp));
        if (cmp == 0) {
            System.out.println("校验无错");
        }
        else {
            System.out.println("校验有错");
        }
    }
}
