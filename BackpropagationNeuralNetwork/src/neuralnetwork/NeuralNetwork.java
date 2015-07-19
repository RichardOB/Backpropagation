/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import DataSet.GeneralisationSet;
import DataSet.TrainingSet;
import activation.ActivationFunction;

/**
 *
 * @author Richard O'Brien
 */
public class NeuralNetwork {
	
	TrainingSet trainingSet;
	
	GeneralisationSet generalisationSet;
	
	NetworkSettings settings;
	
	ActivationFunction activationFunction;
	
	//TODO: Turn into array of hidden layers
	Layer hiddenLayer;
	
	Layer outputLayer;

	public NeuralNetwork(NetworkSettings settings, ActivationFunction activationFunction) {
		this.settings = settings;
		this.activationFunction = activationFunction;
		
		this.hiddenLayer = new Layer(this.settings.getHiddenNeuronCount(), this.settings.getInputNeuronCount(), this.activationFunction);
		this.outputLayer = new Layer(this.settings.getHiddenNeuronCount(), this.settings.getInputNeuronCount(), this.activationFunction);
	}
	
	public void setupTraining(TrainingSet trainingSet, GeneralisationSet generalisationSet) {
		this.trainingSet = trainingSet;
		this.generalisationSet = generalisationSet;		
	}
}
