/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard O'Brien
 */
public class DataPreparator {
	
	
	public List<Double[]> readProblemSetFromFile(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		
		List<Double[]> result = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				
				String[] split = line.split("\\s+");
				
				Double[] convertedLine = new Double[split.length];
				
				for (int i = 0; i < split.length; i++) {
					convertedLine[i] = Double.parseDouble(split[i]);
				}
				
				result.add(convertedLine);
			}
		}
		
		printFile(result);
		return result;
	}
	
	private void printFile(List<Double[]> input) {
		
		for (int i = 0; i < input.size(); i++) {
			
			Double[] line = input.get(i);
			
			for (int j = 0; j < line.length; j++) {
				
				System.out.print(line[j]);
				
				if (j != line.length - 1) {
					System.out.print(",");
				}
			}
			System.out.println("");
		}
	}
}
