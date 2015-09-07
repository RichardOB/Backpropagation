/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSet;

import java.util.Random;
import utilities.ArrayUtilities;

/**
 *
 * @author Richard O'Brien
 */
public class TrainingSet {
	
	public double[][] input;
	
	public double[][] expectedOutput;
	
	int trainingPatternCount;

	public TrainingSet(double[][] input, double[][] expectedOutput) {
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.trainingPatternCount = input.length;
	}

	public int getTrainingPatternCount() {
		return trainingPatternCount;
	}

	public void setTrainingPatternCount(int trainingPatternCount) {
		this.trainingPatternCount = trainingPatternCount;
	}
	
	public void print() {
		for (int i = 0; i < trainingPatternCount; i++) {
			
			for (int j = 0; j < input[i].length; j++) {
				System.out.print(input[i][j] + " ");
			}
			
			System.out.print(": ");
			
			for (int j = 0; j < expectedOutput[i].length; j++) {
				System.out.print(expectedOutput[i][j] + " ");
			}
			
			System.out.println("");
		}
	}
	
	public void shuffleArray() throws Exception
	{
		ArrayUtilities.shuffleDataSet(input, expectedOutput);
	}
}
