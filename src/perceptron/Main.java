package perceptron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import helperClasses.FileDialog;
import helperClasses.MainClass;
import helperClasses.MakeImage;

public class Main extends MainClass {

	List<Instance> instances = new ArrayList<Instance>();
	Feature[] features = new Feature[50];
	double[] weights = new double[50];
	int GEN_LIMIT = 1000;

	String print = "";

	public Main() {
		Scanner s = loadFiles("Please input the name of the image file located in bin");
		loadImages(s);

		features[0] = new DummyFeature(10, 10);

		weights[0] = -5000;
		
		for (int i = 1; i < 50; i++) {
			features[i] = new Feature(10, 10);
			weights[i] = Math.random() - .5;
		}

		List<Instance> wrong = new ArrayList<Instance>();
		for (int i = 0; i < GEN_LIMIT; i++) {
			wrong = new ArrayList<Instance>();
			for (Instance inst : instances) {

				Instance wrongInstance = trainPerceptron(inst);
				if (wrongInstance != null) {
					wrong.add(wrongInstance);
				}
			}
			if (wrong.size() == 0) {
				report(i, wrong.size());
			}
		}

		System.out.println(print);

		int wrongCount = 0;
		for (Instance inst : instances) {
			if (inst.classifiedName.equalsIgnoreCase(inst.className)) {
				wrongCount++;
			}
		}

		report(GEN_LIMIT, wrongCount);
	}

	public void report(int gen, int wrong) {
		System.out.println("Training complete after " + gen + " generations.");
		System.out.println(wrong + " Instance classified wrongly");
	}

	public Instance trainPerceptron(Instance inst) {
		double totalValue = 0;
		for (int i = 0; i < features.length; i++) {
			if (i == 0) {
				//System.out.println(weights[i]);
				totalValue += weights[i];
			} else {
				totalValue += features[i].value(inst.image) * weights[i];
				// dummy variable is not always returning 1
			}
		}
		String result = "";

		if (totalValue > 0) {
			result = "Yes";
		} else {
			result = "Other";
		}
		
		inst.classifiedName = result;

		if (!inst.className.equalsIgnoreCase(result)) {
			
			System.out.println("["+ inst.className+"] ");

			if (inst.className.equalsIgnoreCase("yes")) {
				for (int i = 0; i < features.length; i++) {
					if (features[i].value(inst.image) == 1) {
						// System.out.print("SUBTRACt\n");
						weights[i] -= features[i].value(inst.image);
					}
				}
				// add to weight
			} else {
				for (int i = 0; i < features.length; i++) {
					if (features[i].value(inst.image) == 1) {
						// System.out.print("ADD\n");
						weights[i] += features[i].value(inst.image);
					}
				}
				// subtract from weight
			}
			return inst;
		} else {
			return null;
		}
	}

	public void loadImages(Scanner s) {
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
