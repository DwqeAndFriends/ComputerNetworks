public class CRC {

    private static long  GetRemainder(String newString, String GenXString) {
        int r = GenXString.length() - 1;
        String subString = newString.substring(0, r);
        long mod = Long.parseLong(subString, 2);
        long divisor = Long.parseLong(GenXString, 2);
        for(int i = r; i < newString.length(); i++) {
            mod = mod * 2  + newString.charAt(i) - '0';
            if(mod >= Math.pow(2, r)) {
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
        return Long.toBinaryString(tmp ^ mod);
    }

    public static void main(String[] argv) {
        String InfoString1 = "1101011111";
        String GenXString = "10011";
        String SendString = GetSendString(InfoString1, GenXString);
        System.out.println(SendString);
        long cmp = GetRemainder(SendString, GenXString);
        System.out.println(cmp);
    }
}
