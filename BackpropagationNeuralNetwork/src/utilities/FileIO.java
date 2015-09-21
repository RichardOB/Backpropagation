/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard O'Brien
 */
public class FileIO {
	
	public static List<Double[]> readProblemSetFromFile(String path) throws FileNotFoundException, IOException, Exception {
		File file = new File(path);
		
		List<Double[]> unprocessedProblemSet = new ArrayList<>();
		
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
		
		return unprocessedProblemSet;
	}
	
	public static void writeNetworkReports(String weights, String errorsOverEpoch, String networkSettings, String resultVector, String finalData, String errorPlot) {
		
		java.util.Date date= new java.util.Date();
		String timestamp = "" + date;
		writeToFile(weights, "reports/" + timestamp + "/weights.txt");
		writeToFile(errorsOverEpoch, "reports/" + timestamp + "/errorsOverEpoch.txt");
		writeToFile(networkSettings, "reports/" + timestamp + "/networkSettings.txt");
		writeToFile(resultVector, "reports/" + timestamp + "/resultVector.txt");
		writeToFile(finalData, "reports/" + timestamp + "/finalData.txt");
		writeToFile(errorPlot, "reports/" + timestamp + "/errorplot.csv");
	}

	private static void writeToFile(String content, String path) {
		try {
			
			File file = new File(path);
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Written Report to file: " + file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
