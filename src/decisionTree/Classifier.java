package decisionTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains the logic for creating a decision tree Classifier and also using it
 * to actually classify an instance.
 * 
 * @author Haylem
 *
 */
public class Classifier {
	public List<String> attributes = new ArrayList<String>();

	public String classA = "";
	public String classB = "";

	Set<Instance> trainingSet;
	Set<Instance> testSet;

	private String majorityClass = "";
	private double majorityProbability = 0;

	/**
	 * Calculates the impurity of a given set of instances by taking the product
	 * of the frequency of each class.
	 * 
	 * @param instances
	 *            The set to measure impurity over
	 * @return A double representing the impurity of the set.
	 */
	private double impurity(Set<Instance> instances) {
		double[] classFrequency = classFrequency(instances);
		return (classFrequency[0] * classFrequency[1]) / (Math.pow(instances.size(), 2));

	}

	/**
	 * Calculates the frequency for each class and returns it as an array, with
	 * classA being in the first element and classB in the second element.
	 * 
	 * @param instances
	 *            the set of instances to find the frequency over
	 * @return an array with classA frequency in i = 0 and classB frequency in i
	 *         = 1
	 */
	private double[] classFrequency(Set<Instance> instances) {
		double fA = 0;
		double fB = 0;
		for (Instance instance : instances) {
			if (instance.className.equals(this.classA)) {
				fA++;
			} else {
				fB++;
			}
		}
		return new double[] { fA, fB };
	}

	/**
	 * Finds the most common class among some set of instances. If there is no
	 * majority then one is chosen randomly (Using Math.random())
	 * 
	 * @param instances
	 *            The set of instances to check for majority class in
	 * @return The name of the class which is the majority
	 */
	private String majorityClass(Set<Instance> instances) {
		double[] cF = classFrequency(instances);
		if (cF[0] == cF[1]) {
			return (Math.random() > 0.5) ? classA : classB;
		} else {
			return (cF[0] > cF[1]) ? classA : classB;
		}
	}

	/**
	 * Returns the set of instances that meet the criteria for the given
	 * attribute.
	 * 
	 * @param the
	 *            set of instances to check against criteria
	 * @param attributeID
	 *            The attribute to check criteria against
	 * @param critera
	 *            the criteria, true/false
	 * @return
	 */
	private Set<Instance> sortInstances(String attribute, boolean critera, Set<Instance> instances) {
		Set<Instance> nI = new HashSet<Instance>();
		for (Instance instance : instances) {
			if (instance.get(attribute) == critera) {
				nI.add(instance);
			}
		}
		return nI;
	}

	/**
	 * Recursively builds a decision tree according to the current training set
	 * of instances. The algorithm follows what was taught in class (hopefully)
	 * 
	 * @param instances
	 *            the set of instances left to train the classifier
	 * @param attributes
	 *            the set of attributes left to create the classifier
	 * @param depth
	 *            the current depth in the tree (to help with laying it out
	 *            later)
	 * @return
	 */
	private Node buildTree(Set<Instance> instances, Set<String> attributes, int depth) {
		if (instances.isEmpty()) {
			return new Node(majorityClass, majorityProbability, depth);
		} else if (impurity(instances) == 0) {
			return new Node(majorityClass(instances), 1, depth);
		} else if (attributes.size() == 0) {
			String majorityClass = majorityClass(instances);
			if (majorityClass(instances).equals(this.classA)) {
				return new Node(majorityClass, classFrequency(instances)[0] / (double) instances.size(), depth);
			} else {
				return new Node(majorityClass, classFrequency(instances)[1] / (double) instances.size(), depth);
			}
		} else {
			double bestImpurity = Double.MAX_VALUE;
			String bestAttribute = "";
			Set<Instance> bestTrue = new HashSet<Instance>();
			Set<Instance> bestFalse = new HashSet<Instance>();
			for (String attribute : attributes) {
				Set<Instance> trueInstances = sortInstances(attribute, true, instances);
				Set<Instance> falseInstances = sortInstances(attribute, false, instances);

				double trueWI = 0;
				double falseWI = 0;

				if (trueInstances.size() > 0) {
					trueWI = impurity(trueInstances) * ((double) trueInstances.size() / (double) instances.size());
				}

				if (falseInstances.size() > 0) {
					falseWI = impurity(falseInstances) * ((double) falseInstances.size() / (double) instances.size());
				}

				double avgImpurity = trueWI + falseWI;

				if (avgImpurity < bestImpurity) {
					bestAttribute = attribute;
					bestTrue = trueInstances;
					bestFalse = falseInstances;
					bestImpurity = avgImpurity;
				}
			}
			attributes.remove(bestAttribute);
			depth++;
			Node left = buildTree(bestTrue, attributes, depth);
			Node right = buildTree(bestFalse, attributes, depth);

			return new Node(bestAttribute, left, right, depth - 1);
		}
	}

	/**
	 * Returns the root node of the learned decision tree according to the
	 * current training set. Sets up the state for training, i.e. finding the
	 * majority class and its probability.
	 * 
	 * @return
	 */
	public Node buildTree() {
		double[] classFrequency = classFrequency(trainingSet);

		majorityClass = majorityClass(trainingSet);

		if (majorityClass.equals(classA)) {
			majorityProbability = classFrequency[0] / (double) trainingSet.size();
		} else {
			majorityProbability = classFrequency[1] / (double) trainingSet.size();

		}

		Set<Instance> instances = new HashSet<Instance>();
		instances.addAll(trainingSet);
		Set<String> attributes = new HashSet<String>();
		attributes.addAll(this.attributes);

		return buildTree(instances, attributes, 0);
	}

	/**
	 * Classifies each of the test instances according to the decision tree
	 * attached to the given root node.
	 * 
	 * @param root
	 *            the root of the learned decision tree to classify against.
	 * @return
	 */
	public double[] classify(Node root) {
		double correct = 0;
		double majorityCheck = 0;
		for (Instance instance : testSet) {
			Node currentNode = root;
			while (currentNode.lChild != null && currentNode.rChild != null) {
				if (instance.get(currentNode.name)) {
					currentNode = currentNode.lChild;
				} else {
					currentNode = currentNode.rChild;
				}
			}
			instance.classifiedName = currentNode.name;
			if (instance.classifiedName.equals(instance.className)) {
				correct++;
			}
			if (majorityClass.equals(instance.className)) {
				majorityCheck++;
			}
		}
		double[] results = new double[] { correct / testSet.size(), majorityCheck / testSet.size() };
		return (results);
	}
}