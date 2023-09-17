package Day09;

import IntcodeDay09.Intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
			BufferedReader fileReader = new BufferedReader(new FileReader("Day9.txt"));
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
					continue;
				} if (returnCode == Intcode.AWAITING_INPUT) {
					computer.feedInput(Long.parseLong(scanner.nextLine()));
				} else if (returnCode == Intcode.HALT) {
					break;
				}
			}
	}
}
