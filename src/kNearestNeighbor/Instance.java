package kNearestNeighbor;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent some instance. Classified as having a className which
 * the instance belongs to and a list of features which describe the instance.
 * 
 * @author Haylem Rayner
 *
 */

public class Instance {

	String className = "";
	String classifiedName = "";
	List<Double> features;

	public Instance(String className, List<Double> features) {
		this.className = className;
		this.features = features;
	}

	public Instance(String className) {
		new Instance();
		setClassName(className);
	}

	public Instance() {
		features = new ArrayList<Double>();
	}

	public void addFeature(Double f) {
		features.add(f);
	}

	public void setClassName(String cN) {
		className = cN;
	}

	public void setClassifiedName(String cfN) {
		classifiedName = cfN;
	}

	/**
	 * A method for applying some distance measure between two instances. The
	 * method uses a Normalized Euclidian distance measure comparing the feature
	 * set of each instance and returning the Normalized Euclidian distance as a
	 * result.
	 * 
	 * Assumes that both instances have the same number of features
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static double distance(Instance i1, Instance i2, double[] ranges) {
		double distance = 0;
		for (int i = 0; i < i1.features.size(); i++) {
			double n = Math.pow(i1.features.get(i)-i2.features.get(i), 2);
			double d = Math.pow(ranges[i], 2);
			distance += n/d;
		}
		return Math.sqrt(distance);
	}

	/**
	 * Checks to see if this instance has been classified correctly
	 * 
	 * @return
	 */
	public boolean checkClassification() {
		return classifiedName.equals(className);
	}

	@Override
	public String toString() {
		String s = "[";
		for (double f : features) {
			s = s.concat(f + ", ");
		}
		return s.concat(className + "]");
	}

}
