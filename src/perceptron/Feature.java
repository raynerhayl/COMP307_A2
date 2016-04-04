package perceptron;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class Feature {

	int[] row = new int[4];
	int[] col = new int[4];
	boolean[] sgn = new boolean[4];
	
	Random rnd = new Random();
	
	int height;
	int width;
	
	public Feature(int height, int width){
		this.height = height;
		this.width = width;
		Scanner s = new Scanner(System.in);
		s.nextInt();
		int i = s.nextInt();
		System.out.println(i);
		
		assignInt(row, height);
		assignInt(col, width);
	}
	
	public static void main(String[] args) {
		new Feature(10,10);
	}
	
	public void assignInt(int[] array, int max){
		for(int i = 0; i < array.length ; i++){
			array[i] = rnd.nextInt(max+1);
		}
	}
	
	public void assignBool(boolean[] array){
		for(int i = 0; i < array.length; i ++){
			array[i] = rnd.nextBoolean();
		}
	}
	
}
