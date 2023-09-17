package Day25;

import IntcodeDay17.ASCIITranslator;
import IntcodeDay17.Intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
			BufferedReader fileReader = new BufferedReader(new FileReader("Day25.txt"));
			String rawIntcodeProgram = String.join("", fileReader.readLine());
			long[] intcodeProgram = Arrays.stream(rawIntcodeProgram.split(","))
										 .mapToLong(Long::parseLong)
										 .toArray();

			Scanner scanner = new Scanner(System.in);
			Intcode computer = new Intcode(intcodeProgram);

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
