package main_d2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import intcodeD2.*;

public class Main {
	public static void main(String[] args) {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader("src/day2.txt"));
			String rawIntcodeProgram = String.join("", fileReader.readLine());
			long[] intcodeProgram = Arrays.stream(rawIntcodeProgram.split(","))
										 .mapToLong(Integer::parseInt)
										 .toArray();

			// Part 1
			Intcode computer = new Intcode(intcodeProgram);
			computer.run();
			System.out.println(computer.getFirstLocation());
			
			// Part 2
			for (int noun = 0; noun < 100; noun++) {
				for (int verb = 0; verb < 100; verb++) {
					computer.reset();
					computer.setLocation(1, noun);
					computer.setLocation(2, verb);
					computer.run();
					if (computer.getFirstLocation() == 19690720) {
						System.out.println(100 * noun + verb);
						return;
					}
				}
			}
		} catch (Exception e){
			System.out.println(e);
		}		
	}
}
