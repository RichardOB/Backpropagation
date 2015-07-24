/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataSet.GeneralisationSet;
import dataSet.TrainingSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard O'Brien
 */
public class DataPreparator {
	
	private List<Double[]> unprocessedProblemSet;
	
	private TrainingSet trainingSet;
	
	private GeneralisationSet generalisationSet;
	
	public void readProblemSetFromFile(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		
		unprocessedProblemSet = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				
				String[] split = line.split("\\s+");
				
				Double[] convertedLine = new Double[split.length];
				
				for (int i = 0; i < split.length; i++) {
					convertedLine[i] = Double.parseDouble(split[i]);
				}
				
				unprocessedProblemSet.add(convertedLine);
			}
		}
		
		printFile(unprocessedProblemSet);
	}
	
	private void printFile(List<Double[]> input) {
		
		input.stream().map((line) -> {
			for (int j = 0; j < line.length; j++) {
				
				System.out.print(line[j]);
				
				if (j != line.length - 1) {
					System.out.print(",");
				}
			}
			return line;
		}).forEach((_item) -> {
			System.out.println("");
		});
	}
	
	public void preprocessData() {
		//Alter range of data
	}
	
	public int splitDataSet(int outputCount) throws Exception {
		
		int inputCount = unprocessedProblemSet.get(0).length - outputCount;
		
		double [][] input = new double[unprocessedProblemSet.size()][inputCount];
		double [][] output = new double[unprocessedProblemSet.size()][outputCount];
		
		//Shuffle data
		for (int i = 0; i < unprocessedProblemSet.size(); i++) {
			
			Double[] problemInstance = unprocessedProblemSet.get(i);
			
			if (outputCount >= problemInstance.length) {
				throw new Exception("output count is too large for data set");
			}
			
			int count = 0;
			
			for (int j = 0; j < problemInstance.length; j++) {
				
				if (j < outputCount) {
					input[i][j] = problemInstance[j];
				}
				else if (j >= outputCount) {
					output[i][count++] = problemInstance[j];
				}
				else {
					throw new Exception("input and output count have disointability issues");
				}
			}
	
		}
		
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
		System.arraycopy(input, trainingDataCount, generalisationDataInput, trainingDataCount, output.length - trainingDataCount);
		//Copy last 20% of output data to generalisationDataOutput
		System.arraycopy(output, trainingDataCount, generalisationDataOutput, trainingDataCount, output.length - trainingDataCount);
		
		//Add data to generalisation set
		this.generalisationSet = new GeneralisationSet(generalisationDataInput, generalisationDataOutput);
		
		return inputCount;
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
}
