public class ByteStuffing {
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

    public static void main(String[] args) {
        String InfoString1 = "347D7E807E4//0AA7D";
        String FlagString = "7E";
        String ESC = "//";
        System.out.println(InfoString1);
        String SendString = "2312//7E676AD" + PPPByteStuffing(InfoString1, FlagString, ESC);
        System.out.println(SendString);
        String OriginalString = ByteGetOriginalString(SendString, FlagString, ESC);
        System.out.println(OriginalString);
    }
}