/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSet;

import utilities.ArrayUtilities;

/**
 *
 * @author Richard O'Brien
 */
public class GeneralisationSet {
	
	public double[][] input;
	
	public double[][] expectedOutput;
	
	int generalisationPatternCount;

	public GeneralisationSet(double[][] input, double[][] expectedOutput) {
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.generalisationPatternCount = input.length;
	}

	public double[][] getInput() {
		return input;
	}

	public void setInput(double[][] input) {
		this.input = input;
	}

	public double[][] getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(double[][] expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public int getGeneralisationPatternCount() {
		return generalisationPatternCount;
	}

	public void setGeneralisationPatternCount(int generalisationPatternCount) {
		this.generalisationPatternCount = generalisationPatternCount;
	}

	public void print() {
		System.out.println("Generalisation Set:\n");
		for (int i = 0; i < generalisationPatternCount; i++) {
			
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
