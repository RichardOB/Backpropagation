/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import activation.HyperbolicTangent;
import activation.Linear;
import activation.Sigmoid;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import neuralnetwork.NetworkSettings;
import neuralnetwork.NeuralNetwork;
import utilities.DataPreparator;
import utilities.FileIO;

/**
 *
 * @author Richard O'Brien
 */
public class UserInterfaceController implements Initializable {
	
	//Neural Network
	@FXML 
	private TextField maxEpochCount;
	
	@FXML 
	private TextField learningRate;
	
	@FXML 
	private TextField momentum;
	
	@FXML 
	private TextField trailCount;
	
	@FXML 
	private TextField hiddenNeuronCount;
	
	@FXML 
	private TextField errorThreshold;
	
	//Data Set Settings
	
	@FXML 
	private TextField outputCount;
	
	//Files
	
	@FXML 
	private FileChooser fileChooser;
	
	@FXML
	private File file;
	
	@FXML 
	private TextField inputFilePath;
	
	@FXML 
	private TextField outputFilePath;
	
	//Controller fields
	NetworkSettings settings;
	
	NeuralNetwork neuralNetwork;
	
	DataPreparator dataPrep;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	public void openInputFile() {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Problem Set File");
		Stage stage = null;
		file = fileChooser.showOpenDialog(stage);
		inputFilePath.setText(file.getPath());
	}
	
	public void openOutputFile() {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Select Output File");
		Stage stage = null;
		file = fileChooser.showOpenDialog(stage);
		outputFilePath.setText(file.getPath());
	}
	
	public void train() throws IOException, Exception {
		
		dataPrep = new DataPreparator();
		dataPrep.readProblemSetFromFile(inputFilePath.getText());
		
		//preprocess data
		
		//separate data into non overlapping sets (Training vs Generalisation)
		int inputCount = dataPrep.preprocessData(Integer.parseInt(outputCount.getText()), new Sigmoid(), new Linear());
		
		settings = new NetworkSettings();
		
		settings.setMomentum(Double.parseDouble(momentum.getText()));
		settings.setLearningRate(Double.parseDouble(learningRate.getText()));
		settings.setEpochCount(Integer.parseInt(maxEpochCount.getText()));
		settings.setInputNeuronCount(inputCount);
		settings.setOutputNeuronCount(Integer.parseInt(outputCount.getText()));
		settings.setHiddenNeuronCount(Integer.parseInt(hiddenNeuronCount.getText()));
		settings.setFailureThreshold(Double.parseDouble(errorThreshold.getText()));
		
		
		//NetworkSettings settings, ActivationFunction activationFunction
		neuralNetwork = new NeuralNetwork(settings, new Sigmoid(), new Linear());
		
		neuralNetwork.setupTraining(dataPrep.getTrainingSet(), dataPrep.getGeneralisationSet());
		neuralNetwork.train();
		
		double[][] dataSet = neuralNetwork.useTrainedNetwork(dataPrep.getFullDataSet());
		String dataSetString = dataPrep.convertAndAlterDataSet(dataSet, 26.2144, -26.2144);
		
		FileIO.writeNetworkReports(neuralNetwork.weights, neuralNetwork.errorsOverEpoch, "Data Set " + file.getPath() + "\n\n" + settings.toString(), neuralNetwork.inputOutput, dataSetString);
		System.out.println("Done!");
	}
	
	
	
}
