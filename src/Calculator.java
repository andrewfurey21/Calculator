
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Calculator {

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		log("Type your calculation :");
		String input = scan.next();
		log("" + calculate(input));
		scan.close();
	}

	static void log(String s) {
		System.out.println(s);
	}

	static double calculate(String input) {
		final int DECIMAL_PLACES = 10;
		char operators[] = { '^', '*', '/', '%', '-', '+' };
		char digits[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.' };
		ArrayList<Character> ops = new ArrayList<Character>();
		ArrayList<Double> numbers = new ArrayList<Double>();
		String currentNumber = "";
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '(') {
				String bracketSequence = "";
				boolean found = false;
				int k = i + 1;
				int bracketCount = 0;
				while (k < input.length()) {
					if (input.charAt(k) == '(') {
						bracketCount++;
					} else if (input.charAt(k) == ')') {
						bracketCount--;
					}
					if (bracketCount < 0) {
						found = true;
						break;
					}
					bracketSequence += input.charAt(k);
					k++;
				}
				if (!found) {
					System.err.println("ERROR: Missing ) in " + input);
					return 0;
				}
				double ans = calculate(bracketSequence);
				try {
					String replacement = '(' + bracketSequence + ')';
					input = input.replace(replacement, "" + ans);
					continue;
				} catch (Exception e) {
					System.err.println("ERROR: unknown answer: " + ans);
				}
			}
		}
		for (int i = 0; i < input.length(); i++) {
			boolean correct = false;

			for (int j = 0; j < operators.length; j++) {
				if (operators[j] == input.charAt(i)) {
					correct = true;
					ops.add(operators[j]);
					if (!currentNumber.equals("")) {
						try {
							numbers.add(Double.parseDouble(currentNumber));
						} catch (Exception e) {
							System.err.println("ERROR: unknown double: " + currentNumber);
						}
					}
					currentNumber = "";
				}
			}
			for (int j = 0; j < digits.length; j++) {
				if (digits[j] == input.charAt(i)) {
					correct = true;
					currentNumber += digits[j];
				}
			}
			if (!correct) {
				System.err.println("ERROR: Unknown symbol");
			}
		}
		try {
			numbers.add(Double.parseDouble(currentNumber));
		} catch (Exception e) {
			System.err.println("ERROR: unknown double: " + currentNumber);
		}
		currentNumber = "";
		Collections.reverse(numbers);
		Collections.reverse(ops);
		
		if (ops.size() == 0) {
			return roundOff(numbers.get(0), DECIMAL_PLACES);
		} else {
			if (ops.get(ops.size()-1) == '-' && ops.size() == numbers.size()) {
				numbers.add(0.0D);
			}
			for (int i = 0; i < operators.length; i++) {
				for (int j = ops.size() - 1; j >= 0; j--) {
					double ans = 0;
					if (ops.get(j).equals(operators[i])) {

						if (ops.get(j) == '^') {
							ans = Math.pow(numbers.get(j + 1), numbers.get(j));
						} else if (ops.get(j) == '*') {
							ans = numbers.get(j + 1) * numbers.get(j);
						} else if (ops.get(j) == '/') {
							if (numbers.get(j) == 0) {
								System.err.println("ERROR: Cannot divide by 0");
								return 0;
							}
							ans = numbers.get(j + 1) / numbers.get(j);
						} else if (ops.get(j) == '%') {
							if (numbers.get(j) == 0) {
								System.err.println("ERROR: Cannot divide by 0");
								return 0;
							}
							ans = numbers.get(j + 1) % numbers.get(j);
						} else if (ops.get(j) == '+') {
							ans = numbers.get(j + 1) + numbers.get(j);
						} else if (ops.get(j) == '-') {
							ans = numbers.get(j + 1) - numbers.get(j);
						}
						ops.remove(j);
						numbers.set(j, ans);
						numbers.remove(j + 1);
					}
				}
			}
		}
		
		return roundOff(numbers.get(0), DECIMAL_PLACES);
	}
	
	static double roundOff(double a, int places) {
		return Math.round(a*(double)Math.pow(10.0, places))/(double)Math.pow(10.0, places);
	}
}
