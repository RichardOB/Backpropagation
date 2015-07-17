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
public class Layer {
	
	public Neuron neurons[];
	
	private int neuronCount;
	private int inputCount;

	public Layer(int neuronCount, int inputCount) {
		this.neuronCount = neuronCount;
		this.inputCount = inputCount;
		
		this.neurons = new Neuron[this.neuronCount];
		
		for (int i = 0; i < this.neuronCount; i++) {
			neurons[i] = new Neuron(this.inputCount);
		}
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
	
	
	
}
