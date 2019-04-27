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

    private static String ZeroBitStuffing(String InfoString1) {
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
        StringBuilder newString = new StringBuilder(tmpString);
        int delIndex = newString.indexOf("11111", 0);
        while(delIndex != -1) {
            newString.deleteCharAt(delIndex + 5);
            delIndex = newString.indexOf("11111", delIndex + 5);
        }
        return newString.toString();
    }

    public static void main(String[] args) {
        String InfoString1 = "347D7E807E4//0AA7D";
        String FlagString = "7E";
        String ESC = "//";
        System.out.println(InfoString1);
        String SendString1 = "2312//7E676AD" + PPPByteStuffing(InfoString1, FlagString, ESC);
        System.out.println(SendString1);
        String OriginalString = ByteGetOriginalString(SendString, FlagString, ESC);
        System.out.println(OriginalString);

        String InfoString2 = "000000111111111111111111000";
        System.out.println(InfoString2);
        String SendString2 = ZeroBitStuffing(InfoString2);
        System.out.println("01100" + SendString2);
        String ReceiveString = ZeroBitGetOriginalString("01100" + SendString);
        System.out.println(ReceiveString);
    }
}