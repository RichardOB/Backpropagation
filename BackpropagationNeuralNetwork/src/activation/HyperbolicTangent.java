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
public class HyperbolicTangent implements ActivationFunction{
	
	@Override
	public double calculateActivation (double input) {
		
		double dividend = Math.exp(input) - Math.exp(-input);
		double divisor = Math.exp(input) + Math.exp(-input);
		
		return dividend/divisor;
	}

	@Override
	public double calculateDirivative(double nOutput) {
		return 1 - Math.pow(nOutput, 2);
	}
	
}
