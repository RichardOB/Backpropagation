/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSet;

/**
 *
 * @author Richard O'Brien
 */
public class TrainingSet {
	
	public double[][] input;
	
	public double[][] expectedOutput;
	
	int trainingPatternCount;
	
	double trainingAccuracyTotal = 0.0;

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

	public double getTrainingAccuracyTotal() {
		return trainingAccuracyTotal;
	}

	public void setTrainingAccuracyTotal(double trainingAccuracyTotal) {
		this.trainingAccuracyTotal = trainingAccuracyTotal;
	}
	
	public double getTrainingAccuracyAverage(int epochs) {
		return this.trainingAccuracyTotal / epochs;
	}
	
	public void addToTotal(double increment){
		this.trainingAccuracyTotal += increment;
	}
}
