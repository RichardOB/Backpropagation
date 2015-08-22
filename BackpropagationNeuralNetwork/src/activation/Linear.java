/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activation;

/**
 *
 * @author Richard O'Brien
 */
public class Linear implements ActivationFunction {

	@Override
	public double calculateActivation(double input) {
		return input;
	}
	
	
}
