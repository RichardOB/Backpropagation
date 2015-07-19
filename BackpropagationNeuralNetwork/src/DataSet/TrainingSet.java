/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSet;

/**
 *
 * @author Richard O'Brien
 */
public class TrainingSet {
	
	double[][] input;
	
	double[][] expectedOutput;
	
	int trainingPatternCount;

	public TrainingSet(double[][] input, double[][] expectedOutput) {
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.trainingPatternCount = input.length;
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

	public int getTrainingPatternCount() {
		return trainingPatternCount;
	}

	public void setTrainingPatternCount(int trainingPatternCount) {
		this.trainingPatternCount = trainingPatternCount;
	}
	
	
}
