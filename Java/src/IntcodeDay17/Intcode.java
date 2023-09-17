package IntcodeDay17;

import java.util.ArrayList;
import java.util.HashMap;

public class Intcode {

	public static final int AWAITING_INPUT = 3;
	public static final int CREATED_OUTPUT = 4;
	public static final int HALT = 99;
	
	private long instruction_pointer = 0;
	private long relative_pointer = 0;

	private ArrayList<Long> inputQueue = new ArrayList<>();
	private final long[] memoryOriginalState;
	private HashMap<Long, Long> memory;

	private long lastOutput = -1;

	public Intcode(long[] memory) {
		this.memoryOriginalState = memory.clone();
		this.memory = new HashMap<>();

		for (int i = 0; i < memory.length; i++) {
			this.memory.put((long) i, memory[i]);
		}
	}
	
	public long getFirstLocation() {
		return readMemory(0);
	}

	public long getLastOutput() {
		return lastOutput;
	}
	
	public void reset() {
		instruction_pointer = 0;
		relative_pointer = 0;
		lastOutput = -1;

		inputQueue = new ArrayList<>();

		memory = new HashMap<>();
		for (int i = 0; i < memoryOriginalState.length; i++) {
			this.memory.put((long) i, memoryOriginalState[i]);
		}
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

	private long readMemory(long address) {
		if (memory.containsKey(address)) {
			return memory.get(address);
		}
		return 0;
	}

	public void writeToMemory(long address, long value) {
		memory.put(address, value);
	}

	public long getAddressAt(long pointer, int mode) throws IntcodeException {
		long address = readMemory(pointer);

		if (mode == IntcodeModeParser.POSITION) {
			return address;
		} else if (mode == IntcodeModeParser.RELATIVE) {
			return address + relative_pointer;
		} else if (mode == IntcodeModeParser.IMMEDIATE) {
			throw new IntcodeException("Cannot write in immediate mode");
		}
		throw new IntcodeException("Unknown mode: " + mode);

	}

	public long getParameterAt(long pointer, int mode) throws IntcodeException {
		long value = readMemory(pointer);

		if (mode == IntcodeModeParser.POSITION) {
			return readMemory(value);
		} else if (mode == IntcodeModeParser.IMMEDIATE) {
			return value;
		} else if (mode == IntcodeModeParser.RELATIVE) {
			return readMemory(value + relative_pointer);
		}
		throw new IntcodeException("Unknown mode: " + mode);
	}
	
	public int run() throws IntcodeException {
		while (true) {
			long opcodeRaw = readMemory(instruction_pointer++);
			IntcodeOpcodeObject parsedOpcode = IntcodeModeParser.parse(opcodeRaw);
			int opcode = parsedOpcode.opcode;

			switch (opcode) {
				case 1 -> {
					long in1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long in2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long write = getAddressAt(instruction_pointer++, parsedOpcode.param3);

					long result = in1 + in2;
					writeToMemory(write, result);
				}
				case 2 -> {
					long in1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long in2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long write = getAddressAt(instruction_pointer++, parsedOpcode.param3);
					;

					long result = in1 * in2;
					writeToMemory(write, result);
				}
				case 3 -> {
					if (hasInput()) {
						long input = readInput();
						long write = getAddressAt(instruction_pointer++, parsedOpcode.param1);
						writeToMemory(write, input);
					} else {
						// Reset pointer position
						instruction_pointer--;
						return Intcode.AWAITING_INPUT;
					}

				}
				case 4 -> {
					lastOutput = getParameterAt(instruction_pointer++, parsedOpcode.param1);

					return Intcode.CREATED_OUTPUT;
				}
				case 5 -> {
					long jmp = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long move = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (jmp != 0) {
						instruction_pointer = (int) move;
					}
				}
				case 6 -> {
					long jmp = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long move = getParameterAt(instruction_pointer++, parsedOpcode.param2);

					if (jmp == 0) {
						instruction_pointer = (int) move;
					}
				}
				case 7 -> {
					long a1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long a2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long write = getAddressAt(instruction_pointer++, parsedOpcode.param3);

					if (a1 < a2) {
						writeToMemory(write, 1);
					} else {
						writeToMemory(write, 0);
					}
				}
				case 8 -> {
					long a1 = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					long a2 = getParameterAt(instruction_pointer++, parsedOpcode.param2);
					long write = getAddressAt(instruction_pointer++, parsedOpcode.param3);

					if (a1 == a2) {
						writeToMemory(write, 1);
					} else {
						writeToMemory(write, 0);
					}
				}
				case 9 -> {
					long val = getParameterAt(instruction_pointer++, parsedOpcode.param1);
					relative_pointer += val;
				}
				case 99 -> {
					return Intcode.HALT;
				}
				default -> throw new IntcodeException("Unrecognized opcode: " + opcode);
			}
		}
	}
}
