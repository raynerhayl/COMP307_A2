package kNearestNeighbor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Main {

	private Scanner s = new Scanner(System.in);

	public Main() {
		print("Files are loaded from the bin folder");

		Scanner s = loadFiles("Enter file name for training set\n");
		while (s == null) {
			s = loadFiles("Enter file name for training set\n");
		}
		if (s != null) {
			List<Instance> trainingSet = new ArrayList<Instance>();
			parseSet(trainingSet, s);
			
			s = loadFiles("Enter file name for test set\n");
			while (s == null) {
				s = loadFiles("Enter file name for test set\n");
			}
			if (s != null) {
				List<Instance> testSet = new ArrayList<Instance>();
				parseSet(testSet, s);

				print("Run multiple tests [Y/N] ?");

				if (askBoolean()) {

					print("Please enter the minimum value for k (inclusive)");
					int minK = getK();
					print("Please enter the maximum value for k");
					int maxK = getK();
					while (maxK > trainingSet.size()) {
						print("K must be smaller than the number of instances in the training set.");
						maxK = getK();
					}
					print("Save to file [Y/N] ?");

					PrintWriter pW = null;

					if (askBoolean()) {
						pW = createFile();
					}

					runClassifier(trainingSet, testSet, minK, maxK, pW);
				} else {
					print("Please enter a value for k");
					int k = getK();

					runClassifier(trainingSet, testSet, k, k, null);

				}

			}
		}
	}

	/**
	 * Prompts the user for a file name, does some basic error handling and
	 * returns a printWriter for that file.
	 * 
	 * @return
	 */
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

	/**
	 * Returns a valid integer for k between which is greater than 0
	 * 
	 * @return
	 */
	public int getK() {
		int k = -1;
		while (k < 0) {
			String input = readLine();
			if (input.matches("^-?\\d+$")) {
				k = Integer.valueOf(input);
				if (k < 0) {
					print("Input must be greater than 0");
				}
			} else {
				print("Input must consist only of digits");
			}
		}
		return k;
	}

	/**
	 * Run the classifier a certain number of times, incrementing k each run
	 * 
	 * @param trainingSet
	 * @param testSet
	 * @param maxK
	 *            the largest k to go up to
	 * @param minK
	 *            the starting value for k
	 */
	public void runClassifier(List<Instance> trainingSet, List<Instance> testSet, int minK, int maxK, PrintWriter pW) {
		if (pW != null) {
			pW.println("Accuracy");
		}
		print("Print out progress? [Y/N]");
		boolean output = askBoolean();
		for (int i = minK; i <= maxK; i++) {
			Classifier.classify(trainingSet, testSet, i);

			int correct = 0;
			for (int j = 0; j < testSet.size(); j++) {
				Instance inst = testSet.get(j);

				if (output) {
					System.out.println("Instance " + j + ": classified as: " + inst.classifiedName + " actual class: "
							+ inst.className + " correct: " + inst.checkClassification());
				}

				if (inst.checkClassification()) {
					correct++;
				}
			}
			double correctness = (double) correct / (double) testSet.size();
			if (pW != null) {
				pW.println(String.valueOf(correctness));
				pW.flush();
			}
			print("Classifier accuracy for k = " + i + " : " + String.valueOf(correctness));

		}
	}

	public static void main(String[] args) {
		new Main();
	}

	/**
	 * Parses a text file representing a set of instances. Adds the instances to
	 * a List.
	 * 
	 * NOTE: Does not clear the list
	 * 
	 * @param output
	 *            The list of instances to add to
	 * @param input
	 *            The Scanner attached to the text file
	 */
	public void parseSet(List<Instance> output, Scanner input) {
		while (input.hasNextLine()) {
			Scanner l = new Scanner(input.nextLine());
			Instance nI = new Instance();
			while (l.hasNextDouble()) {
				nI.addFeature(l.nextDouble());
			}
			if (l.hasNext()) {
				nI.setClassName(l.next());
			}
			// not sure why theres an 'extra' instance being created, shouldn't
			// get in the while loop?
			if (!nI.className.equals("")) {
				output.add(nI);
			}
		}
	}

	public Scanner loadFiles(String prompt) {
		String input = readLine(prompt);
		if (!input.contains("/")) {
			if (!input.contains(".txt")) {
				input = input.concat(".txt");
			}
		}
		try {
			URL url = getClass().getClassLoader().getResource("res/part1/" + input);
			String path = url.getPath();
			path = path.replaceAll("%20", " ");
			System.out.println(path);
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
