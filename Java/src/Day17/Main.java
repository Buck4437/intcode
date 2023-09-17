package Day17;

import IntcodeDay17.ASCIITranslator;
import IntcodeDay17.Intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
			BufferedReader fileReader = new BufferedReader(new FileReader("Day17.txt"));
			String rawIntcodeProgram = String.join("", fileReader.readLine());
			long[] intcodeProgram = Arrays.stream(rawIntcodeProgram.split(","))
										 .mapToLong(Long::parseLong)
										 .toArray();

			Scanner scanner = new Scanner(System.in);
			// Part 1
			Intcode computer = new Intcode(intcodeProgram);
			while (true) {
				int returnCode = computer.run();

				if (returnCode == Intcode.CREATED_OUTPUT) {
					String text = ASCIITranslator.toString(computer.getLastOutput());
					System.out.print(text);
				} if (returnCode == Intcode.AWAITING_INPUT) {
					ArrayList<Long> input = ASCIITranslator.toLongs(scanner.nextLine());
					computer.feedInputs(input);
				} else if (returnCode == Intcode.HALT) {
					break;
				}
			}

			// Part 2
			computer.reset();
			computer.writeToMemory(0, 2);

			String solution = """
					A,A,B,C,B,C,B,C,B,A
					R,10,L,12,R,6
					R,6,R,10,R,12,R,6
					R,10,L,12,L,12
					""";
			computer.feedInputs(ASCIITranslator.toLongs(solution));
			while (true) {
				int returnCode = computer.run();

				if (returnCode == Intcode.CREATED_OUTPUT) {
					String text = ASCIITranslator.toString(computer.getLastOutput());
					System.out.print(text);
				} if (returnCode == Intcode.AWAITING_INPUT) {
					ArrayList<Long> input = ASCIITranslator.toLongs(scanner.nextLine() + "\n");
					computer.feedInputs(input);
				} else if (returnCode == Intcode.HALT) {
					break;
				}
			}
	}
}
