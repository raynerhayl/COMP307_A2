package perceptron;

import java.util.Random;

public class DummyFeature extends Feature {

	public DummyFeature(int height, int width, Random rnd) {
		super(height, width, rnd);
	}
	
	@Override
	public int value(boolean[][] image) {
		return 1;
	}

}
