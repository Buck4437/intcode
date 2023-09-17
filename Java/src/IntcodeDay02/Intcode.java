package IntcodeDay02;

public class Intcode {
	
	public static final int HALT = 99;
	
	private int instruction_pointer = 0;
	
	private long[] memoryOriginalState;
	private long[] memory;
	
	public Intcode(long[] memory) {
		this.memoryOriginalState = memory.clone();
		this.memory = memory.clone();
	}
	
	public long getFirstLocation() {
		return memory[0];
	}
	
	public void setLocation(int address, long value) {
		memory[address] = value;
	}
	
	public void reset() {
		memory = memoryOriginalState.clone();
		instruction_pointer = 0;
	}
	
	public int run() throws IntcodeException {
		while (true) {
			if (instruction_pointer >= memory.length) {
				throw new IntcodeException("Memory out of bound");
			}
			
			int opcode = (int) memory[instruction_pointer++];
			
			switch (opcode) {
				case 1: {
					if (instruction_pointer + 2 >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}
					
					long in1 = memory[instruction_pointer++], in2 = memory[instruction_pointer++], out = memory[instruction_pointer++];
					long result = memory[(int) in1] + memory[(int) in2];
					memory[(int) out] = result;
					break;
				}
				
				case 2: {
					if (instruction_pointer + 2 >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}
					
					long in1 = memory[instruction_pointer++], in2 = memory[instruction_pointer++], out = memory[instruction_pointer++];
					long result = memory[(int) in1] * memory[(int) in2];
					memory[(int) out] = result;
					break;
				}
				
				case 99:
					return Intcode.HALT;
					
				default:
					throw new IntcodeException("Unrecognized opcode: " + Integer.toString(opcode));
			}
		}
	}
	
}
