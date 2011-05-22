/**
 * Implementation of Back Propagation algorithm.
 * @author berni
 *
 */
public class BackPropagation {
	static int Activation_TYPE = 1;
	static int Derivative_TYPE = 2;

	static double beta = 1;
	static double learningFactor = 0.03; // 0.07

	/**
	 * Select appropriate activation function.
	 * @param x input
	 * @return function value
	 */
	public static double activationFunction(final double x) {
		return unipolarActivationFunction(x);
	}

	/**
	 * Select appropriate derivative function.
	 * @param x input
	 * @return function value
	 */
	public static double derivativeFunction(final double x) {
		return unipolarDerivative(x);
	}

	public static double unipolarActivationFunction(final double x) {
		double denominator = 1 + Math.exp(-1 * beta * x);
		return 1 / denominator;
	}
	public static double bipolarActivationFunction(final double x){
		return Math.tanh(beta * x);
	}
	public static double unipolarDerivative(final double x){
		return beta * unipolarActivationFunction(x) 
		* (1 - unipolarActivationFunction(x)); 
	}    
	public static double bipolarDerivative(final double x) {
		return beta * (1 - Math.pow(bipolarActivationFunction(x), 2));
	}

	public static double targetFunction(){
		return 0;
	}

	public static double newWeightHiddenLay(final double oldWeight, 
			final double prevLayComputedVal, final double thisLayDerivativeVal, 
			final Matrix thisLaySum4DerivativeVals, final Matrix nextLayWeights, 
			final Matrix computedOutputVal, final Matrix targetOutputVal) {
		double newWeight = 0;
		double grad = 0; 
		for (int i = 0; i < targetOutputVal.getHeigth(); i++) {
			grad += (computedOutputVal.getValue(0, i) - targetOutputVal.getValue(0, i)) 
			* derivativeFunction(thisLaySum4DerivativeVals.getValue(0, i)) 
			* nextLayWeights.getValue(i + 1, 0) //+1 first is number one 
			* thisLayDerivativeVal * prevLayComputedVal;
		}
		newWeight = oldWeight - learningFactor * grad;
		return newWeight;
	}

	public static void computeNewHiddenLayWeights(final Matrix thisLayWeights, 
			final Matrix outputComputedVals, final Matrix outputDestinationVals,
			final Matrix nextLayNeuronSums, final Matrix thisLayNeuronSums, 
			final Matrix prevLayComputedVal, final Matrix nextLayWeights) {
		double newval;
		for (int i = 0; i < thisLayWeights.getHeigth(); i++) { // neurons
			for (int a = 0; a < thisLayWeights.getWidth(); a++) { // weights
				newval = newWeightHiddenLay(thisLayWeights.getValue(a, i), 
						prevLayComputedVal.getValue(0, a), 
						derivativeFunction(thisLayNeuronSums.getValue(0, i)), 
						nextLayNeuronSums, nextLayWeights, 
						outputComputedVals, outputDestinationVals);
				thisLayWeights.setValue(a, i, newval);
			}
		}
	}

	public static double newWeightOutput(final double oldWeight,final double targetOutputVal, 
			final double computedOutputVal, final double sumVal,final double prevLayComputedVal) {
		double newWeight = oldWeight - learningFactor 
		* (computedOutputVal - targetOutputVal) 
		* derivativeFunction(sumVal) * prevLayComputedVal;
		return newWeight;
	}

	public static void computeNewOutputWeights(Matrix outputWeights,
			Matrix outputVals,Matrix neuronResults,Matrix neuronSums, Matrix nuronPrevResults){
		double newval ;
		double neuronPrevRes = 0 ;
		for (int i = 0; i < outputWeights.getHeigth(); i++) {            
			for (int a = 0; a < outputWeights.getWidth(); a++) {
				if (a == 0) { neuronPrevRes = 1; } else {
					neuronPrevRes = nuronPrevResults.getValue(0, a - 1);
				}
				newval = newWeightOutput(outputWeights.getValue(a, i), 
						outputVals.getValue(0, i), neuronResults.getValue(0, i), 
						neuronSums.getValue(0, i), neuronPrevRes);
				outputWeights.setValue(a, i, newval);
			}
		}

	}

}
