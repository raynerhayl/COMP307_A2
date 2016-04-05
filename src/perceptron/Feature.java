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
	
	public Feature(int height, int width){ // TODO make it so that height must equal width
		this.height = height;
		this.width = width;
		
		assignInt(row, height);
		assignInt(col, width);
		assignBool(sgn);
	}
	
	public static void main(String[] args) {
		new Feature(10,10);
	}
	
	public void assignInt(int[] array, int max){
		for(int i = 0; i < array.length ; i++){
			array[i] = rnd.nextInt(max);
		}
	}
	
	public void assignBool(boolean[] array){
		for(int i = 0; i < array.length; i ++){
			array[i] = rnd.nextBoolean();
		}
	}
	
	public int value(boolean[][] image){
		int sum = 0;
		for(int i = 0; i < row.length; i++){
			if(image[row[i]][col[i]] == sgn[i]){
				sum++;
			}
		}
		return (sum>=3)?1:0;
	}
	
}
