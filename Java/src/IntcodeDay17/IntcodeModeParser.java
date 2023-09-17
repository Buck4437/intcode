package IntcodeDay17;

public class IntcodeModeParser {

    public static final int POSITION = 0;
    public static final int IMMEDIATE = 1;
    public static final int RELATIVE = 2;

    // Order: Opcode, Param1, Param2, Param3
    public static IntcodeOpcodeObject parse(long value) {
        int opcode = (int) value % 100;
        int param1 = (int) (value / 100) % 10;
        int param2 = (int) (value / 1000) % 10;
        int param3 = (int) (value / 10000) % 10;
        return new IntcodeOpcodeObject(opcode, param1, param2, param3);
    }

}

