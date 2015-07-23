/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Richard O'Brien
 */
public class UserInterfaceController implements Initializable {
	
	//Settings
	@FXML 
	private TextField maxEpochCount;
	
	@FXML 
	private TextField learningRate;
	
	@FXML 
	private TextField momentum;
	
	@FXML 
	private TextField trailCount;
	
	//Files
	
	@FXML 
	private FileChooser fileChooser;
	
	@FXML
	private File file;
	
	@FXML 
	private TextField inputFilePath;
	
	@FXML 
	private TextField outputFilePath;

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
	
}
