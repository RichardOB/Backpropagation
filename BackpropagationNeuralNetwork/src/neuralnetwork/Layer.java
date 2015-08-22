/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import activation.ActivationFunction;

/**
 *
 * @author Richard O'Brien
 */
public class Layer {
	
	public Neuron neurons[];
	
	private int neuronCount;
	private int inputCount;
	
	ActivationFunction activationFunction;

	public Layer(int neuronCount, int inputCount, ActivationFunction activationFunction) {
		this.neuronCount = neuronCount;
		this.inputCount = inputCount;
		this.activationFunction = activationFunction;
		
		//create array of neurons according to neuron count of layer
		this.neurons = new Neuron[this.neuronCount];
		
		//initialise each neuron with the relevant input count (i.e. without bias)
		for (int i = 0; i < this.neuronCount; i++) {
			neurons[i] = new Neuron(this.inputCount);
		}
	}
	
	public void feedForward (double[] inputs) throws Exception {
		for(Neuron n: neurons) {
			
			//Calculate weighted input sum of neuron, including the bias weight
			double weightInputSum = n.getWeightInputSum(inputs);
			
			//Set output of neuron to the result of the activation function
			n.setOutput(this.activationFunction.calculateActivation(weightInputSum));
		}
	}
	
	public double[] getOutputs() throws Exception {
		
		double[] result = new double[this.neuronCount];
		
		for (int i = 0; i < this.neuronCount; i++) {
			result[i] = this.neurons[i].getOutput();
		}
		
		return result;
	}

	public int getNeuronCount() {
		return neuronCount;
	}

	public void setNeuronCount(int neuronCount) {
		this.neuronCount = neuronCount;
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}
	
	public double calculateSignalError(double target, double nOutput) {
		return -(target - nOutput) * activationFunction.calculateDirivative(nOutput);
	}
	
	public double calculateSignalError(double n2SignalError, double n1Output, double n2Weight) {
		return n2SignalError * n2Weight * activationFunction.calculateDirivative(n1Output);
	}
}
