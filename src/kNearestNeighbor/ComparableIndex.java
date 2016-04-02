package kNearestNeighbor;

/**
 * An Object which stores to variables and is comparable according to 
 * its second variable.
 * @author Haylem Rayner
 *
 */
public class ComparableIndex implements Comparable{

	public int arg1;
	public double arg2;
	
	public ComparableIndex(int arg1, double arg2){
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	@Override
	public int compareTo(Object o) {
		ComparableIndex other = (ComparableIndex)o;
		return Double.compare(this.arg2, other.arg2);
	}
	
	
	
}
