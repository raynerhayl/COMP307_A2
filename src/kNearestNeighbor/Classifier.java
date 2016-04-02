package kNearestNeighbor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Classifier {

	/**
	 * Classifies the testSet according to K-NearestNeighbours. Updates the
	 * classified field in each of the test set with the result.
	 * 
	 * @param trainingSet
	 * @param testSet
	 */
	public static void classify(List<Instance> trainingSet, List<Instance> testSet, int k) {
		double[] featureRange = new double[trainingSet.get(0).features.size()];
		findRanges(trainingSet, featureRange);
		for (Instance testInstance : testSet) {
			PriorityQueue<ComparableIndex> distanceQueue = new PriorityQueue<ComparableIndex>();
			int index = 0;
			for (Instance trainInstance : trainingSet) {
				distanceQueue
						.add(new ComparableIndex(index, Instance.distance(testInstance, trainInstance, featureRange)));
				index++;
			}
			testInstance.setClassifiedName(findMajority(trainingSet, distanceQueue, k));
		}
	}

	/**
	 * Calculates the range for each feature in the set and writes to the output
	 * array
	 * 
	 * @param set
	 * @param output
	 */
	public static void findRanges(List<Instance> set, double[] output) {
		Map<Integer, Double> minf = new HashMap<Integer, Double>();
		Map<Integer, Double> maxf = new HashMap<Integer, Double>();

		for (Instance instance : set) {
			List<Double> f = instance.features;
			for (int i = 0; i < f.size(); i++) {
				if (!minf.containsKey(i) || f.get(i) < minf.get(i)) {
					minf.put(i, f.get(i));
				}

				if (!maxf.containsKey(i) || f.get(i) > maxf.get(i)) {
					maxf.put(i, f.get(i));
				}
			}
		}

		for (int i = 0; i < output.length; i++) {
			output[i] = maxf.get(i) - minf.get(i);
		}
	}

	/**
	 * A Helper method which caculates the majority class name for the k-nearest
	 * neighbors
	 * 
	 * @param trainingSet
	 * @param distanceQueue
	 * @return
	 */
	public static String findMajority(List<Instance> set, PriorityQueue<ComparableIndex> distanceQueue, int k) {
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		for (int i = 0; i < k; i++) {
			String className = set.get(distanceQueue.poll().arg1).className;
			if (classMap.containsKey(className)) {
				classMap.put(className, classMap.get(className) + 1);
			} else {
				classMap.put(className, 1);
			}
		}
		String classified = "";
		for (String className : classMap.keySet()) {
			if (classified.equals("")) {
				classified = className;
			} else if (classMap.get(className) > classMap.get(classified)) {
				classified = className;
			}
		}
		return classified;
	}

}
