package helperClasses;

import java.io.File;
import java.io.FileNotFoundException;
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
			System.out.println("Loading "+path);
			return new Scanner(new File(path));

		} catch (FileNotFoundException e) {
			print("Could not find the file at " + input);
			return null;
		}
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
