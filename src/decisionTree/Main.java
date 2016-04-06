package decisionTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class Main implements ActionListener {

	Classifier classifier = new Classifier();

	String trainingSet = "";
	String testSet = ""; // file name of the two sets

	public static void main(String[] args) {
		new Main();
	}

	GUI g;

	public Main() {
		g = new GUI(this);
		g.root = new Node("Test Root Node");
		g.root.lChild = new Node("True", 2, 2);
		g.root.rChild = new Node("False", 2, 2);
		layoutTree(g.root);

		g.println("Please resize the window, and the text ouput screen so that that the root node is visible");
		g.println("Then select a training set and test set. You can reload either set at any time");
		g.println("There must be a test set and a training set currently loaded to classify");
		g.println("");
	}

	public Set<Instance> loadFile(File file) {
		if (!file.getName().contains(".dat")) {
			g.println("File must be of .dat format");
		}
		g.println("Loading " + file.getName());
		try {
			classifier.attributes.clear();
			classifier.classA="";
			classifier.classB=""; // Clear all stored attributes and classes
			Scanner fileScanner = new Scanner(file);
			int line = 0;
			Set<Instance> set = new HashSet<Instance>();
			while (fileScanner.hasNextLine()) {
				Scanner lineScanner = new Scanner(fileScanner.nextLine());
				Instance instance = null;
				while (lineScanner.hasNext()) {
					switch (line) {
					case 0:
						if (classifier.classA.equals("")) {
							classifier.classA = lineScanner.next();
						} else {
							classifier.classB = lineScanner.next();
						}
						break;
					case 1:
						classifier.attributes.add(lineScanner.next());
						break;
					default:
						instance = new Instance(lineScanner.next(), lineScanner, classifier.attributes);
						break;
					}
				}
				if (instance != null) {
					set.add(instance);
				}
				line++;
			}
			g.println("Loading Complete \n");

			fileScanner.close();
			return set;
		} catch (IOException e) {
			g.println("Error reading file");
		}
		return null;
	}

	/**
	 * Sets the positions of all the nodes relative to the
	 * maximum required width of the tree (for the widest level)
	 * @param root
	 */
	public void layoutTree(Node root) {
		Map<Integer, List<Node>> depthMap = new HashMap<Integer, List<Node>>();
		g.root.maxDepth(depthMap);
		double maxWidth = 0; // largest level of tree
		for (int key : depthMap.keySet()) {
			if (depthMap.get(key).size() > maxWidth) {
				maxWidth = depthMap.get(key).size();
			}
		}
		double maxPWidth = maxWidth * 200; // width in pixels of largest level
		int start_x = 600;
		int start_y = 100;

		int y_margin = 70;

		for (int key : depthMap.keySet()) {
			int y = start_y + key * y_margin;
			int x_step = (int) ((maxPWidth) / (2.0 * (double) depthMap.get(key).size()));
			for (int i = 0; i < depthMap.get(key).size(); i++) {
				Node node = depthMap.get(key).get(i);
				node.pos[0] = start_x + x_step * (i + 1);
				node.pos[1] = y;
			}
		}
	}

	public void classify() {
		g.println("Learning tree according to " + trainingSet);
		g.println("Classifying " + testSet + "according to learned tree \n");
		g.root = classifier.buildTree();
		layoutTree(g.root);
		double[] accuracies = classifier.classify(g.root);

		g.println("Accuracy of classified instances " + String.valueOf(accuracies[0]));
		g.println("Accuracy of baseline classifier  " + String.valueOf(accuracies[1]));

		g.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().equals(JButton.class)) {
			JButton source = (JButton) e.getSource();
			String tag = source.getText();
			if (tag.equals("Classify")) {
				if (classifier.testSet != null && classifier.trainingSet != null) {
					classify();
				}
			}
			if (tag.equals("Load Test Set") || tag.equals("Load Training Set")) {
				JFileChooser fc = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fc.setCurrentDirectory(workingDirectory);
				int returnval = fc.showOpenDialog(g);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					if (tag.equals("Load Test Set")) {
						g.print("Loading test set: ");
						testSet = fc.getSelectedFile().getName();
						classifier.testSet = loadFile(fc.getSelectedFile());

					}
					if (tag.equals("Load Training Set")) {
						g.print("Loading training set: ");
						trainingSet = fc.getSelectedFile().getName();
						classifier.trainingSet = loadFile(fc.getSelectedFile());

					}

				}
			}

		}
	}

}
