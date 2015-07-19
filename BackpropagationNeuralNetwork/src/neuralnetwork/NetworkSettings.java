/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

/**
 *
 * @author Richard O'Brien
 */
public class NetworkSettings {
	
	private double momentum;
	
	private double learningRate;
	
	private double epochCount;
	
	private int inputNeuronCount;
	
	private int outputNeuronCount;
	
	//TODO: Change this to an array where size = hidden layer count,
	//representing number of neurons at each hidden layer
	private int hiddenNeuronCount;
	
	private double failureThreshold;

	public NetworkSettings(double momentum, double learningRate, double epochCount, int inputNeuronCount, int outputNeuronCount, int hiddenNeuronCount, double failureThreshold) {
		this.momentum = momentum;
		this.learningRate = learningRate;
		this.epochCount = epochCount;
		this.inputNeuronCount = inputNeuronCount;
		this.outputNeuronCount = outputNeuronCount;
		this.hiddenNeuronCount = hiddenNeuronCount;
		this.failureThreshold = failureThreshold;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getEpochCount() {
		return epochCount;
	}

	public void setEpochCount(double epochCount) {
		this.epochCount = epochCount;
	}

	public int getInputNeuronCount() {
		return inputNeuronCount;
	}

	public void setInputNeuronCount(int inputNeuronCount) {
		this.inputNeuronCount = inputNeuronCount;
	}

	public int getOutputNeuronCount() {
		return outputNeuronCount;
	}

	public void setOutputNeuronCount(int outputNeuronCount) {
		this.outputNeuronCount = outputNeuronCount;
	}

	public int getHiddenNeuronCount() {
		return hiddenNeuronCount;
	}

	public void setHiddenNeuronCount(int hiddenNeuronCount) {
		this.hiddenNeuronCount = hiddenNeuronCount;
	}

	public double getFailureThreshold() {
		return failureThreshold;
	}

	public void setFailureThreshold(double failureThreshold) {
		this.failureThreshold = failureThreshold;
	}
}
