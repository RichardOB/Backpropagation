/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Random;

/**
 *
 * @author Richard O'Brien
 */
public class ArrayUtilities {
	
	public static <T> T[][] deepCopy(T[][] matrix) {
		return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
	}
	
	public static double[][] deepCopy(double[][] matrix) {
		return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
	}
	
	public static void shuffleDataSet(double[][] input, double[][] output) throws Exception
	{
		
		if (input.length != output.length) {
			throw new Exception("Input and output set lengths do not match");
		}
		
		Random rnd = new Random();
		for (int i = input.length - 1; i > 0; i--)
		{
		  int index = rnd.nextInt(i + 1);
		  
		  // Simple swap for Input
		  double[] inputElement = input[index];
		  input[index] = input[i];
		  input[i] = inputElement;
		  
		  // Simple swap for output
		  double[] outputElement = output[index];
		  output[index] = output[i];
		  output[i] = outputElement;
		}
	}
}
