package perceptron;

import java.util.Scanner;

public class Instance {

	String className;
	public String classifiedName;

	boolean[][] image;

	public Instance(String className, Scanner s) {
		int width = s.nextInt();
		int height = s.nextInt();

		this.className = className;
		image = new boolean[height][width];

		String bits = s.next();
		while (s.hasNext() && !s.hasNext("P1")&&bits.length()!=width*height) {
			bits = bits.concat(s.next());
		}
		

		int index = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				String bit = String.valueOf(bits.charAt(index));
				if (bit.equals("1")) {
					image[row][col] = true;
				} else {
					image[row][col] = false;
				}
				index++;
			}
		}
	}

	@Override
	public String toString() {
		String s = className +"\n";
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[0].length; col++) {
				s = s.concat(String.valueOf(image[row][col])+"  ");
			}
			s = s.concat("\n");
		}
		return s;
	}

}
