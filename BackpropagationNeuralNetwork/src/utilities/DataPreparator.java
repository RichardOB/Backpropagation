/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import activation.ActivationFunction;
import activation.Sigmoid;
import dataSet.GeneralisationSet;
import dataSet.TrainingSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Richard O'Brien
 */
public class DataPreparator {
	
	private List<Double[]> unprocessedProblemSet;
	
	private TrainingSet trainingSet;
	
	private GeneralisationSet generalisationSet;
	
	private TrainingSet fullDataSet;
	
	private double [][] input;
	
	private double [][] output;
	
	private int inputCount;
	
	private double [][] inputMinMax;
	private double [][] outputMinMax;
	
	public void readProblemSetFromFile(String path) throws FileNotFoundException, IOException, Exception {
		unprocessedProblemSet = FileIO.readProblemSetFromFile(path);
		
		verifyDataCompleteness();
	}

	public int preprocessData(int outputCount, ActivationFunction activationFunctionHidden, ActivationFunction activationFunctionOutput) throws Exception {
		
		inputCount = unprocessedProblemSet.get(0).length - outputCount;
		
		input = new double[unprocessedProblemSet.size()][inputCount];
		output = new double[unprocessedProblemSet.size()][outputCount];
		
		//Shuffle Data
		Collections.shuffle(unprocessedProblemSet, new Random());
		
		//Split Data into input and output sets
		for (int i = 0; i < unprocessedProblemSet.size(); i++) {
			
			Double[] problemInstance = unprocessedProblemSet.get(i);
			
			if (outputCount >= problemInstance.length) {
				throw new Exception("output count is too large for data set");
			}
			
			int count = 0;
			
			for (int j = 0; j < problemInstance.length; j++) {
				if (j < inputCount) {
					input[i][j] = problemInstance[j];
				}
				else if (j >= inputCount) {
					output[i][count++] = problemInstance[j];
				}
				else {
					throw new Exception("input and output count have disointability issues");
				}
			}
		}

		//Alter range of inputs
		inputMinMax = findMinMax(input);
		input = alterRange(input, -Math.sqrt(3.0), Math.sqrt(3.0), inputMinMax);

		//Alter range of outputs	
		//TODO: Confirm output for linear should still be between 0 and 1
		//TODO: Experiment with ranges: E.g. 0.1 -. 0.9 (Outer bounds unreachable)
		if (activationFunctionOutput instanceof Sigmoid) {
			outputMinMax = findMinMax(output);
			output = alterRange(output, 0.1, 0.9, outputMinMax);
		}
		else {
			//output = output;
			outputMinMax = findMinMax(output);
			//output = alterRange(output, 0.1, 0.9, outputMinMax);
			output = alterRange(output, -0.9, 0.9, outputMinMax);
		}

		splitDataSet(outputCount);
		return inputCount;
	}
	
	public String convertAndAlterDataSet(double[][] dataSet, int outputCount) throws Exception {

		printminMax(inputMinMax);
		printminMax(outputMinMax);
		
		double [][] convertedInput = new double[dataSet.length][inputCount];
		double [][] convertedOutput = new double[dataSet.length][outputCount];
		
		//Split Data into input and output sets
		for (int i = 0; i < dataSet.length; i++) {
			
			double[] problemInstance = dataSet[i];
			
			if (outputCount >= problemInstance.length) {
				throw new Exception("output count is too large for data set");
			}
			
			int count = 0;
			
			for (int j = 0; j < problemInstance.length; j++) {
				if (j < inputCount) {
					convertedInput[i][j] = problemInstance[j];
				}
				else if (j >= inputCount) {
					convertedOutput[i][count++] = problemInstance[j];
				}
				else {
					throw new Exception("input and output count have disointability issues");
				}
			}
		}
		
		convertedInput = alterRange(convertedInput, inputMinMax);
		convertedOutput = alterRange(convertedOutput, outputMinMax);
		

		String resultString = "";
		
		for (int i = 0; i < convertedInput.length; i++) {
			
			for (int j = 0; j < convertedInput[i].length; j++) {
				resultString += convertedInput[i][j];
				resultString += " ";
			}
			
			for (int j = 0; j < convertedOutput[i].length; j++) {
				resultString += convertedOutput[i][j];
				
				if (j != convertedOutput[i].length - 1) {
					resultString += " ";
				}
			}
			
			resultString += "\n";
		}

		return resultString;
	}
	
	private double[][] findMinMax(double[][] unsortedData) {
		
		double[][] sortedData = ArrayUtilities.deepCopy(unsortedData);
		
		double [][] minMax = new double [2][sortedData[0].length];
			
		//For all inputs
		for (int col = 0; col < sortedData[0].length; col ++) {
			
			//bubble sort according to input
			for (int i = 0; i < sortedData.length; i++) {
				for (int j = i+1; j < sortedData.length; j++) {
					if (sortedData[i][col]>sortedData[j][col]) {
						double temp = sortedData[i][col];
						sortedData[i][col] = sortedData[j][col];
						sortedData[j][col] = temp;
					}
				}
			}
			
			//range of unscaled values
			minMax[0][col] = sortedData[0][col];
			minMax[1][col] = sortedData[sortedData.length - 1][col];	
		}
		
		return minMax;
	}
	
	private double[][] alterRange(double[][] unsortedData, double [][] tsMinMax) {

		double[][] sortedData = ArrayUtilities.deepCopy(unsortedData);
			
		//For all inputs
		for (int col = 0; col < sortedData[0].length; col ++) {
			
			//bubble sort according to input
			for (int i = 0; i < sortedData.length; i++) {
				for (int j = i+1; j < sortedData.length; j++) {
					if (sortedData[i][col]>sortedData[j][col]) {
						double temp = sortedData[i][col];
						sortedData[i][col] = sortedData[j][col];
						sortedData[j][col] = temp;
					}
				}
			}
			
			
			//range of unscaled values
			double tuMin = sortedData[0][col];
			double tuMax = sortedData[sortedData.length - 1][col];
			
			
			//calculate new scaled values of unsorted data
			for (double[] unsortedInput : unsortedData) {
				double tu = unsortedInput[col];
				double ts = ((tu - tuMin)/(tuMax - tuMin)) * (tsMinMax[1][col] - tsMinMax[0][col]) + tsMinMax[0][col];
				unsortedInput[col] = ts;
			}
        }
		return unsortedData;
	}
	
	private double[][] alterRange(double[][] unsortedData, double tsMin, double tsMax, double[][] oldMinMax) {

		double[][] sortedData = ArrayUtilities.deepCopy(unsortedData);
			
		//For all inputs
		for (int col = 0; col < sortedData[0].length; col ++) {
			
			
			//range of unscaled values
			double tuMin = oldMinMax[0][col];
			double tuMax = oldMinMax[1][col];
			
			//calculate new scaled values of unsorted data
			for (double[] unsortedInput : unsortedData) {
				double tu = unsortedInput[col];
				double ts = ((tu - tuMin)/(tuMax - tuMin)) * (tsMax - tsMin) + tsMin;
				unsortedInput[col] = ts;
			}
        }
		return unsortedData;
	}
	
	public int splitDataSet(int outputCount) throws Exception {

		int trainingDataCount = (int) Math.ceil(unprocessedProblemSet.size() * 0.8);
		int generalisationDataCount = (int) Math.floor(unprocessedProblemSet.size() * 0.2);
		
		double[][] trainingDataInput = new double[trainingDataCount][inputCount];
		double[][] trainingDataOutput = new double[trainingDataCount][outputCount];

		//Copy first 80% of input data to trainingDataInput
		System.arraycopy(input, 0, trainingDataInput, 0, trainingDataCount);
		//Copy first 80% of output data to trainingDataOutput
		System.arraycopy(output, 0, trainingDataOutput, 0, trainingDataCount);
		
		//Add data to training set
		this.trainingSet = new TrainingSet(trainingDataInput, trainingDataOutput);
		
		double[][] generalisationDataInput = new double[generalisationDataCount][inputCount];
		double[][] generalisationDataOutput = new double[generalisationDataCount][outputCount];
		
		//Copy last 20% of input data to generalisationDataInput
		System.arraycopy(input, trainingDataCount, generalisationDataInput, 0, output.length - trainingDataCount);
		//Copy last 20% of output data to generalisationDataOutput
		System.arraycopy(output, trainingDataCount, generalisationDataOutput, 0, output.length - trainingDataCount);
		
		//Add data to generalisation set
		this.generalisationSet = new GeneralisationSet(generalisationDataInput, generalisationDataOutput);
		
		//Add data to full set
		this.fullDataSet = new TrainingSet(input, output);
		
		return inputCount;
	}
	
	private void verifyDataCompleteness() throws Exception {
		
		int expectedSize = unprocessedProblemSet.get(0).length;
		
		for (Double[] unprocessedProblemSet1 : unprocessedProblemSet) {
			if (unprocessedProblemSet1.length != expectedSize) {
				throw new Exception("Problem set is incomplete!");
			}
		}
	}

	public TrainingSet getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(TrainingSet trainingSet) {
		this.trainingSet = trainingSet;
	}

	public GeneralisationSet getGeneralisationSet() {
		return generalisationSet;
	}

	public void setGeneralisationSet(GeneralisationSet generalisationSet) {
		this.generalisationSet = generalisationSet;
	}

	public TrainingSet getFullDataSet() {
		return fullDataSet;
	}

	public void setFullDataSet(TrainingSet fullDataSet) {
		this.fullDataSet = fullDataSet;
	}
	
	public void	printminMax(double[][] minMax) {
		
		for (double[] minMax1 : minMax) {
			System.out.print(minMax1[0] + " ");
		}
	}
	
}
