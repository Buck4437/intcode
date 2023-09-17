package IntcodeDay05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Intcode {

	public static final int AWAITING_INPUT = 3;
	public static final int HALT = 99;
	
	private int instruction_pointer = 0;

	private final ArrayList<Long> inputQueue = new ArrayList<>();
	private final long[] memoryOriginalState;
	private long[] memory;
	
	public Intcode(long[] memory) {
		this.memoryOriginalState = memory.clone();
		this.memory = memory.clone();
	}
	
	public long getFirstLocation() {
		return memory[0];
	}
	
	public void reset() {
		memory = memoryOriginalState.clone();
		instruction_pointer = 0;
	}

	public boolean hasInput() {
		return inputQueue.size() > 0;
	}

	public long readInput() throws IntcodeException {
		if (hasInput()) {
			return inputQueue.remove(0);
		}
		throw new IntcodeException("Input queue is empty");
	}

	public void feedInput(long value) {
		inputQueue.add(value);
	}

	public void feedInputs(ArrayList<Long> inputs) {
		inputQueue.addAll(inputs);
	}

	public long getParameterAt(int pointer, int mode) throws IntcodeException {
		long value = memory[pointer];

		if (mode == IntcodeModeParser.POSITION) {
			return memory[(int) value];
		} else if (mode == IntcodeModeParser.IMMEDIATE) {
			return value;
		}
		throw new IntcodeException("Unknown mode: " + mode);
	}
	
	public int run() throws IntcodeException {
		while (true) {
			if (instruction_pointer >= memory.length) {
				throw new IntcodeException("Memory out of bound");
			}

			int opcodeRaw = (int) memory[instruction_pointer++];
			IntcodeOpcodeObject parsedOpcode = IntcodeModeParser.parse(opcodeRaw);
			int opcode = parsedOpcode.opcode;

			switch (opcode) {
				case 1: {
					if (instruction_pointer + 2 >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long in1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long in2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long out = memory[instruction_pointer++];

					long result = in1 + in2;
					memory[(int) out] = result;
					break;
				}
				case 2: {
					if (instruction_pointer + 2 >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long in1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long in2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long out = memory[instruction_pointer++];

					long result = in1 * in2;
					memory[(int) out] = result;
					break;
				}
				case 3: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					if (hasInput()) {
						long input = readInput();
						long in1 = memory[instruction_pointer++];
						memory[(int) in1] = input;
					} else {
						// Reset pointer position
						instruction_pointer--;
						return Intcode.AWAITING_INPUT;
					}

					break;
				}
				case 4: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long out1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					System.out.println(out1);

					break;
				}
				case 5: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long jmp = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long move = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (jmp != 0) {
						instruction_pointer = (int) move;
					}
					break;
				}
				case 6: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long jmp = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long move = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (jmp == 0) {
						instruction_pointer = (int) move;
					}
					break;
				}
				case 7: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long a1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long a2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (a1 < a2) {
						memory[(int) memory[instruction_pointer++]] = 1;
					} else {
						memory[(int) memory[instruction_pointer++]] = 0;
					}
					break;
				}
				case 8: {
					if (instruction_pointer >= memory.length) {
						throw new IntcodeException("Memory out of bound");
					}

					long a1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long a2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (a1 == a2) {
						memory[(int) memory[instruction_pointer++]] = 1;
					} else {
						memory[(int) memory[instruction_pointer++]] = 0;
					}
					break;
				}
				case 99: {
					return Intcode.HALT;
				}
				default:
					throw new IntcodeException("Unrecognized opcode: " + opcode);
			}
		}
	}
}
