package perceptron;

public class DummyFeature extends Feature {

	public DummyFeature(int height, int width) {
		super(height, width);
	}
	
	@Override
	public int value(boolean[][] image) {
		return 1;
	}

}
