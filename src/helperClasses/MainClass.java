package helperClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

public class MainClass {
	Scanner s = new Scanner(System.in);

	public Scanner loadFiles(String prompt) {
		String input = readLine(prompt);
		try {
			URL url = getClass().getClassLoader().getResource(input);
			String path = url.getPath();
			path = path.replaceAll("%20", " ");
			System.out.println("Loading " + path);
			return new Scanner(new File(path));

		} catch (FileNotFoundException e) {
			print("Could not find the file at " + input);
			return null;
		}
	}

	public boolean askBoolean() {
		String input = "";
		while (true) {
			input = readLine();
			if (input.equals("Y") || input.equals("y")) {
				return true;
			} else if (input.equals("N") || input.equals("n")) {
				return false;
			}
		}
	}

	public PrintWriter createFile() {
		String fileName = "";
		boolean valid = false;
		while (!valid) {
			fileName = readLine();
			if (fileName.equals("")) {
				print("File name must not be empty");
			}
			if (fileName.contains(" ")) {
				print("File name cannot contain spaces");
			}
			valid = true;
		}
		try {
			if (fileName.endsWith(".txt") == false) {
				fileName = fileName.concat(".txt");
			}
			return new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Prints the string s concanted with "\n" to the console.
	 * 
	 * @param s
	 */
	public void print(String s) {
		System.out.println(s);
	}

	public String readLine(String prompt) {
		System.out.println(prompt);
		return s.nextLine();
	}

	public String readLine() {
		return s.nextLine();
	}
}
