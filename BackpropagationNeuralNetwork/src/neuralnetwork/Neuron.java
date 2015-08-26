/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Richard O'Brien
 */
public class Neuron {
	
	public double[] weights;
	
	public double[] weightDifference;
	
	private int inputCount;
	
	private Double output = null;
	
	private Double signalError = null;

	public Neuron(int inputCount) {
		
		//Include extra input for bias/threshold
		this.inputCount = inputCount + 1;
		
		//Initialise weight and weight difference arrays
		//Weight difference array records delta weights for calculations
		this.weights = new double[this.inputCount];
		this.weightDifference = new double[this.inputCount];
		
		//initialise weights randomly and set weight defferences to 0.0
		initialiseWeights();
		
	}
	
	private void initialiseWeights() {
		
		Random random = new Random();
		//Max = 1.0 / sqrt(inputCount), Min = -Max
		double max = 1.0 / Math.sqrt(this.inputCount);
		double min = -max;
		double range = max - min;
		
		//We initialise random weights with an additional weight for the bias
		for (int i = 0; i < this.inputCount; i++) {
			this.weights[i] = ((random.nextDouble() * range) + min);
			
			//Assign weight differences to 0.0 initially
			//so that momentum can work during 1st iteration
			this.weightDifference[i] = 0.0;
		}
	}
	
	public double getWeightInputSum (double[] inputs) throws Exception {

		if (inputs.length != this.inputCount - 1) {
			throw new Exception("Inputs to node do not match node input count!");
		}
		
		//Add bias
		double result = this.weights[inputs.length];
		
		//Multiply inputs by weights
		for (int i = 0; i < this.inputCount -1; i++) {
			result+= this.weights[i] * inputs[i];
		}

		return result;
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public Double getOutput() throws Exception {
		if (this.output == null) {
			throw new Exception("Neuron has not computed an output yet.");
		}
		
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public Double getSignalError() throws Exception {
		if (this.signalError == null) {
			throw new Exception("Neuron has not computed its signal error yet.");
		}

		return signalError;
	}

	public void setSignalError(Double signalError) {
		this.signalError = signalError;
	}
	
	public List<Double> getWeights() {
		
		List<Double> neuronWeights = new ArrayList<>();
		
		for (int i = 0; i < weights.length; i++) {
			neuronWeights.add(weights[i]);
		}
		
		return neuronWeights;
	}
}
