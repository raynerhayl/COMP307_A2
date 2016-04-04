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

	public static void main(String[] args) {
		new Main();
	}

	GUI g;

	public Main() {
		g = new GUI(this);
	}

	public Set<Instance> loadFile(File file) {
		if (!file.getName().contains(".dat")) {
			g.println("File must be of .dat format");
		}
		g.println("Loading " + file.getName());
		try {
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
			g.println("Loading Complete");

			fileScanner.close();
			return set;
		} catch (IOException e) {
			g.println("Error reading file");
		}
		return null;
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().equals(JButton.class)) {
			JButton source = (JButton) e.getSource();
			String tag = source.getText();
			if (tag.equals("Classify")) {
				g.print("Classify pressed");
				if (classifier.testSet != null && classifier.trainingSet != null) {
					g.print("Classifying");
					g.root = classifier.buildTree();
					layoutTree(g.root);
					classifier.classify(g.root);
					g.print(String.valueOf(g.root.size()));
					g.repaint();
				}
			}
			if (tag.equals("Load Test Set") || tag.equals("Load Training Set")) {
				JFileChooser fc = new JFileChooser();
				int returnval = fc.showOpenDialog(g);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					if (tag.equals("Load Test Set")) {
						g.print("Loading test set: ");

						classifier.testSet = loadFile(fc.getSelectedFile());
					}
					if (tag.equals("Load Training Set")) {
						g.print("Loading training set: ");

						classifier.trainingSet = loadFile(fc.getSelectedFile());
					}

				}
			}
		}
	}

}
