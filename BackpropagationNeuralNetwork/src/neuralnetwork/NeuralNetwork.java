/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import dataSet.GeneralisationSet;
import dataSet.TrainingSet;
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

	public NeuralNetwork(NetworkSettings settings, ActivationFunction activationFunctionHidden, ActivationFunction activationFunctionOutput) {
		this.settings = settings;
		
		//neuronCount, inputCount, activationFunction
		this.hiddenLayer = new Layer(this.settings.getHiddenNeuronCount(), this.settings.getInputNeuronCount(), activationFunctionHidden);
		this.outputLayer = new Layer(this.settings.getOutputNeuronCount(), this.settings.getHiddenNeuronCount(), activationFunctionOutput);
	}
	
	public void setupTraining(TrainingSet trainingSet, GeneralisationSet generalisationSet) {
		this.trainingSet = trainingSet;
		this.generalisationSet = generalisationSet;	
	}
	
	public void train() throws Exception {
		
		int epochNumber = 0;
		
		double prevTrainingErr = 0.0;
		double prevGeneralisationErr = 0.0;
		
		//TODO: Or end when average generalisation acccuracy within threshold
		while (epochNumber++ < this.settings.getEpochCount()) {
			
			System.out.println("epoch: " + epochNumber);
			
			//1. Train
			double trainingAccuracy = startTrainingPhase();
			boolean better = prevTrainingErr >= trainingAccuracy;
			System.out.println("Training Error Difference: " + better);
			
			prevTrainingErr = trainingAccuracy;
			
			//2. Test
			double generalisationAccuracy = startTestingPhase();
			better = prevGeneralisationErr >= generalisationAccuracy;
			System.out.println("Generalisation Error Better: " + better);
			
			prevGeneralisationErr = generalisationAccuracy;
			
			//3. Add to training and generalisation totals
			this.trainingSet.addToTotal(trainingAccuracy);
			this.generalisationSet.addToTotal(generalisationAccuracy);
			
			//4. add training/generalisation results to training result file
			//TODO
		}
		
		//System.out.println("Training Accuracy: " + trainingSet.getTrainingAccuracyAverage(epochNumber));
		//System.out.println("Generalisation Accuracy: " + generalisationSet.getGeneralisationAccuracyAverage(epochNumber));
	}
	
	private double startTrainingPhase() throws Exception {
		
		double accuracy = 0;
		double errorAccumulator = 0;
		//System.out.println("\nTRAINING:");
		
		for (int i = 0; i < this.trainingSet.getTrainingPatternCount(); i++) {
			
			//1. Feed inputs through network 
			feedForward(this.trainingSet.input[i]);
			
			//2. Get previously calculated outputs from network
			double[] actual = outputLayer.getOutputs();
			
			//3. Calculate neural network error
			double error = calculatePatternError(this.trainingSet.expectedOutput[i], actual);
			errorAccumulator += error;
			
			//System.out.println(error);
			
			//4. Determine if prediction was correct
			accuracy += determinePredictionCorrectness(error);
			
			//5. Calculate Signal Errors
			calculateSignalErrors(this.trainingSet.expectedOutput[i]);
			
			//6. Backpropagation to adjust weights
			backPropagate(this.trainingSet.input[i]);
		}
		
		errorAccumulator = errorAccumulator/this.trainingSet.getTrainingPatternCount();
		//System.out.println("Training Accuracy: " + accuracy/this.trainingSet.getTrainingPatternCount() * 100.0);
		System.out.println("Training Average Error: " + errorAccumulator);
		
		//Calculate training accuracy (MSE) of epoch
		return errorAccumulator;
	}
	
	private double startTestingPhase() throws Exception {
		
		double accuracy = 0;
		double errorAccumulator = 0;
		//System.out.println("\nTESTING:");
		
		for (int i = 0; i < this.generalisationSet.getGeneralisationPatternCount(); i++) {
			
			double[] actual = getOutput(this.generalisationSet.input[i]);
			
			double error = calculatePatternError(this.generalisationSet.expectedOutput[i], actual);
			errorAccumulator += error;
			
			//System.out.println(error);
			
			//Determine if prediction was correct
			accuracy += determinePredictionCorrectness(error);
		}
		
		errorAccumulator = errorAccumulator/this.generalisationSet.getGeneralisationPatternCount();
		//System.out.println("Generalisation Accuracy: " + accuracy/this.generalisationSet.getGeneralisationPatternCount() * 100.0);
		System.out.println("Generalisation Average Error: " + errorAccumulator);
		
		//Calculate generalisation accuracy of epoch
		return errorAccumulator;
	}
	
	public void calculateSignalErrors(double[] targetOutput) throws Exception {
		
		//Calculate signal error for each output
		for (int i = 0; i < this.settings.getOutputNeuronCount(); i++) {
			
			Neuron n = this.outputLayer.neurons[i];
			
			double nOutput = n.getOutput();
			
			//TODO: Experiment with negative sign (Not sure why it was there)
			//n.setSignalError(-(targetOutput[i] - nOutput) * (1 - nOutput) * nOutput);
			//n.setSignalError(-(targetOutput[i] - nOutput) * (1 - nOutput) * nOutput);
			n.setSignalError(outputLayer.calculateSignalError(targetOutput[i], nOutput));
		}
		
		//Calculate the signal error for each hidden unit
		for (int i = 0; i < this.settings.getHiddenNeuronCount(); i++) {
			
			Neuron n1 = this.hiddenLayer.neurons[i];
			
			double sum = 0.0;
			
			//Calculate the sum of (the output error signal) * (weight vector) * (1 - output of hidden layer) * (output of hidden layer)
			for (int j = 0; j < settings.getOutputNeuronCount(); j++) {
				Neuron n2 = this.outputLayer.neurons[j];
				
				//Backpropagate signal error from a single output to determine 
				//the current hidden unit neuron's error contribution to the output node. 
				//sum += n2.getSignalError() * n2.weights[i] * (1 - n1.getOutput()) * n1.getOutput();
				sum += hiddenLayer.calculateSignalError(n2.getSignalError(), n1.getOutput(), n2.weights[i]);
			}
			
			//Set current hidden unit neuron's total error contribution
			n1.setSignalError(sum);
		}
	}
	
	public void backPropagate(double[] input) throws Exception {
		
		//Calculate the new weight values for the hidden-to-output weights
		for (int i = 0; i < this.settings.getOutputNeuronCount(); i++) {
			
			Neuron n = this.outputLayer.neurons[i];
			
			//Update all weights for neuron (except bias)
			for (int j = 0; j < this.settings.getHiddenNeuronCount(); j++) {
				
				//Calculate weight difference between output neuron i and hidden neuron j
				n.weightDifference[j] = -this.settings.getLearningRate() * 
										n.getSignalError() * 
										this.hiddenLayer.neurons[j].getOutput() +
										this.settings.getMomentum() * 
										n.weightDifference[j];
				
				//Update weight between output neuron i and hidden neuron j
				n.weights[j] += n.weightDifference[j];
			}
			
			//calculate the bias/threshold weight difference
			n.weightDifference[this.settings.getOutputNeuronCount()] = 
										-this.settings.getLearningRate() * 
										n.getSignalError() + 
										this.settings.getMomentum() * 
										n.weightDifference[this.settings.getOutputNeuronCount()];
			
			//Update biaa/threshold weight
			n.weights[this.settings.getOutputNeuronCount()] += n.weightDifference[this.settings.getOutputNeuronCount()];			
		}
		
		//Calculate the new weight values for the input-to-hidden weights
		for (int i = 0; i < this.settings.getHiddenNeuronCount(); i++) {
			
			Neuron n = this.hiddenLayer.neurons[i];
			
			for (int j = 0; j < this.settings.getInputNeuronCount(); j++) {
				
				//Calculate weight difference between hidden neuron i and input j
				n.weightDifference[j] = -this.settings.getLearningRate() *
										n.getSignalError() * 
										input[j] + 
										this.settings.getMomentum() * 
										n.weightDifference[j];
				
				//Update weight between hidden neuron i and input j
				n.weights[j] += n.weightDifference[j];							
			}

			//calculate the bias/threshold weight difference
			n.weightDifference[this.settings.getInputNeuronCount()] = 
										-this.settings.getLearningRate() * 
										n.getSignalError() + 
										this.settings.getMomentum() * 
										n.weightDifference[this.settings.getInputNeuronCount()];
			
			//Update biaa/threshold weight
			n.weights[this.settings.getInputNeuronCount()] += n.weightDifference[this.settings.getInputNeuronCount()];
		}
	}
	
	public double[] getOutput(double[] inputs) throws Exception {

		this.hiddenLayer.feedForward(inputs);

		this.outputLayer.feedForward(this.hiddenLayer.getOutputs());

		return this.outputLayer.getOutputs();
	}
	
	private void feedForward(double[] inputs) throws Exception {

		this.hiddenLayer.feedForward(inputs);

		this.outputLayer.feedForward(this.hiddenLayer.getOutputs());
	}
	
	private double calculatePatternError(double[] expectedOutput, double[] actualOutput) throws Exception {
		//TODO: Calculate signal error
		
		if (expectedOutput.length != actualOutput.length) {
			throw new Exception("Output length (" + actualOutput.length + ") does not match expected output length (" + expectedOutput.length + ")");
		}
		
		double error = 0.0;
		
		//System.out.println("expected : actual : error");
		for (int i = 0; i < actualOutput.length; i++) {
			error += Math.pow((expectedOutput[i] - actualOutput[i]),2);
			//System.out.println(expectedOutput[i] + " : " + actualOutput[i] + " : " + Math.abs( Math.abs(expectedOutput[i]) - Math.abs(actualOutput[i])));
		}
		
		//WHY ARE WE DOING THIS????
		//error = error/actualOutput.length;
		
		return error;
	}
	
	private int determinePredictionCorrectness(double error) {
		
		if (error <= settings.getFailureThreshold()) {
			return 1;
		}
		
		return 0;
	}
	
}
