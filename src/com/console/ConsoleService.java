package com.console;

import java.util.Scanner;

public class ConsoleService {

	private static final ThreadLocal<Scanner> input;

	static {
		input = ThreadLocal.withInitial(() -> new Scanner(System.in));
	}

	public static String getUserInput(String message) {
		System.out.println(message);
		Scanner scanner = input.get(); //getting the scanner instance.
		String userInput = scanner.nextLine();
		return userInput;
	}
}