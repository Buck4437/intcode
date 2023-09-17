package IntcodeDay05;

import java.util.HashMap;

public class IntcodeModeParser {

    public static final int POSITION = 0;
    public static final int IMMEDIATE = 1;

    // Order: Opcode, Param1, Param2, Param3
    public static IntcodeOpcodeObject parse(int value) {
        int opcode = value % 100;
        int param1 = (value / 100) % 10;
        int param2 = (value / 1000) % 10;
        int param3 = (value / 10000) % 10;
        return new IntcodeOpcodeObject(opcode, param1, param2, param3);
    }

}

