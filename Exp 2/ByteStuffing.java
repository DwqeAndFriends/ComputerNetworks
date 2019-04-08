public class ByteStuffing {
    
    private static String PPPByteStuffing(String InfoString1, String flagString, String ESC) {
        StringBuilder newString = new StringBuilder();
        char[] infoChars = InfoString1.toCharArray(), flagChars = flagString.toCharArray(), escChars = ESC.toCharArray();
        for (int i = 0; i < infoChars.length; i++) {
            if(infoChars[i] == flagChars[0] && infoChars[i + 1] == flagChars[1]) {
                newString.append(escChars);
                newString.append(flagChars);
                ++i;
            }
            else if(infoChars[i] == escChars[0] && infoChars[i + 1] == escChars[1]) {
                newString.append(escChars);
                newString.append(escChars);
                ++i;
            }
            else {
                newString.append(infoChars[i]);
            }
        }
        return flagString + newString.toString() + flagString;
    }

    public static void main(String[] args) {
        String InfoString1 = "347D7E807E4//0AA7D";
        String FlagString = "7E";
        String ESC = "//";
        String SendString = PPPByteStuffing(InfoString1, FlagString, ESC);
        System.out.println(SendString);
    }
}