public class Stuffing {
    private static String ZeroBitStuffing(String InfoString1) {
        StringBuilder newString = new StringBuilder();
        int one_cnt = 0, ist_cnt = 0;
        char[] infoChars = InfoString1.toCharArray();
        for (char ch : infoChars) {
            newString.append(ch);
            if (ch == '1') {
                one_cnt++;
                if (one_cnt == 5) {
                    newString.append('0');
                    one_cnt = 0;
                    ist_cnt++;
                }
            }
            else {
                one_cnt = 0;
            }
        }
        return "01111110" + newString.toString() + "01111110";
    }

    private static int[] ZeroBitGetFlagIndex(String receivedString, String flagString) {
        int[] index = new int[2];
        index[0] = receivedString.indexOf(flagString) + flagString.length();
        index[1] = receivedString.indexOf(flagString, index[0]);
        return index;
    }

    private static String ZeroBitGetOriginalString(String receivedString) {
        int[] Index = ZeroBitGetFlagIndex(receivedString, "01111110");
        String tmpString = receivedString.substring(Index[0], Index[1]);
        System.out.println(tmpString);
        StringBuilder newString = new StringBuilder(tmpString);
        int delIndex = newString.indexOf("11111");
        while(delIndex != -1) {
            newString.deleteCharAt(delIndex + 5);
            delIndex = newString.indexOf("11111", delIndex + 5);
        }
        return newString.toString();
    }
    
    public static void main(String[] args) {
        String InfoString = "000000111111111111111111000";
        System.out.println(InfoString);
        String SendString = ZeroBitStuffing(InfoString);
        System.out.println(SendString);
        String ReceiveString = ZeroBitGetOriginalString(SendString);
        System.out.println(ReceiveString);
    }
}