/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.List;

/**
 *
 * @author Richard O'Brien
 */
public class ScreenIO {
	
	private static void printFile(List<Double[]> input) {
		
		input.stream().map((line) -> {
			for (int j = 0; j < line.length; j++) {
				
				System.out.print(line[j]);
				
				if (j != line.length - 1) {
					System.out.print(",");
				}
			}
			return line;
		}).forEach((_item) -> {
			System.out.println("");
		});
	}
	
	private static void printValueArray (double[][] arr) {
		
		for (double[] arr1 : arr) {
			for (int j = 0; j < arr1.length; j++) {
				System.out.print(arr1[j]);
				
				if (j != arr1.length - 1) {
					System.out.print(",");
				}
			}
			System.out.println("");
		}
	}
}
