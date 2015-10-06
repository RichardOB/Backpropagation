/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import dataSet.GeneralisationSet;
import dataSet.TrainingSet;
import activation.ActivationFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	//Reports
	public String errorsOverEpoch = "";
	public String trainGenErrorPlot = "";
	public String weights = "";
	public String inputOutput = "";

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
		
		trainGenErrorPlot += "training_error,generalisation_error";
		
		//TODO: Or end when average generalisation acccuracy within threshold
		while (epochNumber++ < this.settings.getEpochCount()) {
			
			//Record Current Epoch Number 
			errorsOverEpoch += "\nepoch: " + epochNumber;
			weights += "\nepoch: " + epochNumber;
			inputOutput += "\nepoch: " + epochNumber;
			System.out.println("Epoch: " + epochNumber);
			trainGenErrorPlot += "\n";

			//1. Train
			//Start Training Phase for next epoch
			double trainingAccuracy = startTrainingPhase();
			
			trainGenErrorPlot += trainingAccuracy + ",";
			
			//Record if epoch is better than previous
			boolean better = prevTrainingErr >= trainingAccuracy;
			errorsOverEpoch += "\nTraining Error Improvement: " + better;
			//Record weights after current epoch
			//weights += getWeightListAsString();
			
			//Capture training error for next epoch
			prevTrainingErr = trainingAccuracy;
			
			//2. Test
			//Start Testing Phase for next epoch
			double generalisationAccuracy = startTestingPhase();
			
			trainGenErrorPlot += generalisationAccuracy;
			
			//Record if epoch is better than previous
			better = prevGeneralisationErr >= generalisationAccuracy;
			errorsOverEpoch += "\nGeneralisation Error Improvement: " + better;
			
			//Capture generalisation error for next epoch
			prevGeneralisationErr = generalisationAccuracy;
		
		}
	}
	
	private double startTrainingPhase() throws Exception {
		
		double errorAccumulator = 0;

		inputOutput += "\n\tTraining: ";
		
		//Loop through entire training set
		for (int i = 0; i < this.trainingSet.getTrainingPatternCount(); i++) {
			
			//1. Feed inputs through network 
			feedForward(this.trainingSet.input[i]);
			//Capture result vector string
			inputOutput += "\n\t\t" + getResultVectorString(this.trainingSet.input[i]);
			
			
			//2. Get previously calculated outputs from network
			double[] actual = outputLayer.getOutputs();
			
			//3. Calculate neural network error
			double error = calculatePatternError(this.trainingSet.expectedOutput[i], actual);
			errorAccumulator += error;
					
			//5. Calculate Signal Errors
			calculateSignalErrors(this.trainingSet.expectedOutput[i]);
			
			//6. Backpropagation to adjust weights
			backPropagate(this.trainingSet.input[i]);
		}
		
		errorAccumulator = errorAccumulator/this.trainingSet.getTrainingPatternCount();
		errorsOverEpoch += "\nTraining Average Error: " + errorAccumulator;

		//TODO: Shuffle dataset
		this.trainingSet.shuffleArray();
		
		//Calculate training accuracy (MSE) of epoch
		return errorAccumulator;
	}
	
	private double startTestingPhase() throws Exception {
		
		double errorAccumulator = 0;

		inputOutput += "\n\tTesting: ";
		
		//Loop through entire generalisation set
		for (int i = 0; i < this.generalisationSet.getGeneralisationPatternCount(); i++) {
			
			//Get previously calculated outputs from network
			double[] actual = getOutput(this.generalisationSet.input[i]);
			//Capture result vector string
			inputOutput += "\n\t\t" + getResultVectorString(this.generalisationSet.input[i]);
			
			//3. Calculate neural network error
			double error = calculatePatternError(this.generalisationSet.expectedOutput[i], actual);
			errorAccumulator += error;
		}
		
		errorAccumulator = errorAccumulator/this.generalisationSet.getGeneralisationPatternCount();
		
		errorsOverEpoch += "\nGeneralisation Average Error: " + errorAccumulator;
		
		//TODO: Shuffle dataset
		this.generalisationSet.shuffleArray();
		
		//Calculate generalisation accuracy of epoch
		return errorAccumulator;
	}
	
	public double[][] useTrainedNetwork(TrainingSet fullDataSet) throws Exception {
		
		List<double[]> data = new ArrayList<>();
		
		//Loop through entire dataset
		for (int i = 0; i < fullDataSet.getTrainingPatternCount(); i++) {
			
			//Get input/outputs from Neural Network and add to result
			getOutput(fullDataSet.input[i]);
			
			double[] temp = getResultVectorDoubleArray(fullDataSet.input[i]);
			
			for (int j = 0; j < temp.length; j++) {
				System.out.print(temp[j] + " ");
			}
			System.out.println("");
			
			data.add(temp);
		}
		
		//Convert array list to double[][]
		double[][] result = new double[data.size()][data.get(1).length];
		
		for (int i = 0; i < result.length; i++) {
			double[] vect = data.get(i);
			System.arraycopy(vect, 0, result[i], 0, result[i].length);
		}
		
		System.out.println("After");
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println("");
		}
		
		return result;
	}
	
	private void calculateSignalErrors(double[] targetOutput) throws Exception {
		
		//Calculate signal error for each output
		for (int i = 0; i < this.settings.getOutputNeuronCount(); i++) {
			
			Neuron n = this.outputLayer.neurons[i];
			
			double nOutput = n.getOutput();
			
			//TODO: Experiment with negative sign (Not sure why it was there)
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
	
	private void backPropagate(double[] input) throws Exception {
		
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
	
	private double[] getOutput(double[] inputs) throws Exception {

		this.hiddenLayer.feedForward(inputs);

		this.outputLayer.feedForward(this.hiddenLayer.getOutputs());

		return this.outputLayer.getOutputs();
	}
	
	private void feedForward(double[] inputs) throws Exception {

		this.hiddenLayer.feedForward(inputs);

		this.outputLayer.feedForward(this.hiddenLayer.getOutputs());
	}
	
	private double calculatePatternError(double[] expectedOutput, double[] actualOutput) throws Exception {
		
		if (expectedOutput.length != actualOutput.length) {
			throw new Exception("Output length (" + actualOutput.length + ") does not match expected output length (" + expectedOutput.length + ")");
		}
		
		double error = 0.0;

		for (int i = 0; i < actualOutput.length; i++) {
			error += Math.pow((expectedOutput[i] - actualOutput[i]),2);
		}
		
		return error;
	}
	
	public List getWeights() {
		
		List result = new ArrayList();
		
		result.add("Hidden Layer: " + this.hiddenLayer.getWeights());
		result.add("Output Layer: " + this.outputLayer.getWeights());
		
		return result;
	}
	
	public String getWeightListAsString() {
		List networkWeights = getWeights();
		String result = "";
		
		for (Object weight : networkWeights) {
			result += "\n" + weight.toString();
		}
		
		return result + "\n";
	}
	
	public String getResultVectorString(double[] input) throws Exception {
		
		String result = "";
		
		result += Arrays.toString(input) + " ";
		
		Neuron[] output = outputLayer.neurons;
		for (Neuron output1 : output) {
			result += output1.getOutput() + " ";
		}
		
		return result;
	}
	
	public double[] getResultVectorDoubleArray(double[] input) throws Exception {
		
		Neuron[] output = outputLayer.neurons;
		
		double[] outputArray = new double[output.length + input.length];
		
		for (int i = 0; i < input.length; i++) {
			outputArray[i] = input[i];
		}
		
		int count = input.length;
		for (Neuron output1 : output) {
			outputArray[count] = output1.getOutput();
			count++;
		}
		
		return outputArray;
	}
	
}
