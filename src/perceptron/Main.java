package perceptron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import helperClasses.FileDialog;
import helperClasses.MainClass;
import helperClasses.MakeImage;

public class Main extends MainClass {

	List<Instance> instances = new ArrayList<Instance>();
	List<Instance> testInstances = new ArrayList<Instance>();
	Feature[] features = new Feature[51];
	double[] weights = new double[51];
	int GEN_LIMIT = 1000;

	String print = "";

	double LEARNING_RATE = .9;

	Random rnd = new Random(1);

	public Main() {
		Scanner s = loadFiles("Please input the name of the image file located in bin");
		loadImages(s, instances);

		System.out.println("Train for multiple learning rates [Y/N]");
		if (askBoolean() == true) {
			double minRate = Double.valueOf(readLine(
					"Enter value for minimum learning rate(eg 1 .must be decimal with or without negation sign, theres no parsing so be carfeul!)"));
			double maxRate = Double.valueOf(readLine("eg. 0 .Enter value for maximum learning rate(again be careful)"));
			double stepRate = Double.valueOf(readLine(
					"Enter increment value for each test(eg. 0.1 .it doesn't matter if it gives a value higher than the maxRate"));
			System.out.println("File name to save to , in bin");
			System.out.println("MinRate: " + minRate + " MaxRate: " + maxRate + " Increment: " + stepRate);
			PrintWriter pW = createFile();
			LEARNING_RATE = minRate;
			while (LEARNING_RATE < maxRate) {
				pW.println(trainPerceptron());
				pW.flush();
				LEARNING_RATE += stepRate;
			}

		} else {

			trainPerceptron();

			// For displaying the ones that are wrong

			while (true) {
				System.out.println("Load a new image file to test [Y/N]");
				if (askBoolean() == false) {
					break;
				}
				Scanner testScanner = loadFiles("Please input the name of the image file to test");
				loadImages(testScanner, testInstances);
				makeImage(testInstances);
				List<Instance> toDraw = new ArrayList<Instance>();
				int incorrect = 0;
				for (int i = 0; i < testInstances.size(); i++) {
					classify(testInstances.get(i));
					if (!testInstances.get(i).classifiedName.equalsIgnoreCase(testInstances.get(i).className)) {
						// incorrect++;
						toDraw.add(testInstances.get(i));
						incorrect++;
					}
				}
				System.out.println("Accuracy on test set:"
						+ (double) (testInstances.size() - incorrect) / (double) testInstances.size());
				makeImage(toDraw);
			}
		}
	}

	public int trainPerceptron() {
		features[0] = new DummyFeature(10, 10, rnd);

		weights[0] = rnd.nextDouble() - .5;

		for (int i = 1; i < 51; i++) {
			features[i] = new Feature(10, 10, rnd);
			weights[i] = rnd.nextDouble() - .5;
		}

		List<Integer> wrong = new ArrayList<Integer>();
		for (int i = 0; i < GEN_LIMIT; i++) {
			wrong = new ArrayList<Integer>();
			for (int j = 0; j < instances.size(); j++) {
				boolean correct = trainPerceptron(instances.get(j));
				if (correct == false) {
					wrong.add(j);
				}
			}
			if (wrong.size() == 0) {
				report(i, wrong);
				return i;
			}
		}
		wrong = new ArrayList<Integer>();
		for (int i = 0; i < instances.size(); i++) {
			if (instances.get(i).classifiedName.equalsIgnoreCase(instances.get(i).className) == false) {
				wrong.add(i);
			}
		}
		report(GEN_LIMIT, wrong);
		return GEN_LIMIT;
	}

	public void makeImage(List<Instance> instances) {
		MakeImage makeImage = new MakeImage("Cross");

		makeImage.image = new boolean[50][50];
		int startRow = 0;
		int startCol = 0;
		makeImage.cols = 50;
		makeImage.rows = 50;
		if (instances.size() > 0) {
			for (int i = 0; i < 25 && i < instances.size(); i++) {
				concatArray(makeImage.image, instances.get(i).image, startRow, startCol);
				if (startCol != 40) {
					startCol += 10;
				} else {
					startCol = 0;
					startRow += 10;
				}
			}
		}
		makeImage.redraw();
	}

	public void concatArray(boolean[][] image, boolean[][] fragment, int startRow, int startCol) {
		for (int row = startRow; row < startRow + fragment.length; row++) {
			for (int col = startCol; col < startCol + fragment[0].length; col++) {
				image[row][col] = fragment[row - startRow][col - startCol];
			}
		}
	}

	public void report(int gen, List<Integer> wrong) {
		System.out.println("Training complete after " + gen + " generations with learning rate of: " + LEARNING_RATE);
		System.out.println(wrong.size() + " Instance classified wrongly");
		for (Integer index : wrong) {
			System.out.println("Image :" + index + " classified wrong");
		}
	}

	public String classify(Instance inst) {
		double totalValue = 0;
		for (int i = 0; i < features.length; i++) {
			totalValue += features[i].value(inst.image) * weights[i];
		}
		String result = "";

		if (totalValue > 0) {
			result = "Yes";
		} else {
			result = "Other";
		}

		inst.classifiedName = result;

		return result;
	}

	public boolean trainPerceptron(Instance inst) {

		String result = classify(inst);

		if (!inst.className.equalsIgnoreCase(result)) {
			if (inst.className.equalsIgnoreCase("yes")) {
				for (int i = 0; i < features.length; i++) {
					weights[i] += features[i].value(inst.image) * LEARNING_RATE;
				}
				// add to weight
			} else {
				for (int i = 0; i < features.length; i++) {
					weights[i] -= features[i].value(inst.image) * LEARNING_RATE;
				}
				// subtract from weight
			}
			return false;
		} else {
			return true;
		}
	}

	public void loadImages(Scanner s, List<Instance> instances) {
		while (s.hasNext()) {
			s.next();
			String className = s.next();
			className = className.substring(1, className.length());
			instances.add(new Instance(className, s));
		}
		System.out.println("Done");
	}

	public static void main(String[] args) {
		new Main();
	}

}
