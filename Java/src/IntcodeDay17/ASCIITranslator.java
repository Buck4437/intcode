package IntcodeDay17;

import java.util.ArrayList;

public class ASCIITranslator {

    public static String toString(long value) {
        if (0 <= value && value <= 255) {
            return Character.toString((char) value);
        }
        return Long.toString(value);
    }

    public static ArrayList<Long> toLongs(String text) {
        ArrayList<Long> result = new ArrayList<>();
        for (char c : text.toCharArray()) {
            result.add((long) c);
        }
        return result;
    }

}
