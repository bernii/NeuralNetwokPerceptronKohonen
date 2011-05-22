/**
 * Implementation of Back Propagation algorithm.
 * @author berni
 *
 */
public class BackPropagation {
    static int Activation_TYPE = 1 ;
    static int Derivative_TYPE = 2 ;
            
    static double beta = 1 ;
  // static float learningFactor = Float.parseFloat("0.07") ;
    static double learningFactor = Float.parseFloat("0.03") ;
    
    /**
     * Select appropriate activation function
     * @param x
     * @return
     */
    public static double activationFunction(double x){
        return unipolarActivationFunction(x);
    }
    
    /**
     * Select appropriate derivative function
     * @param x
     * @return
     */
    public static double derivativeFunction(double x){
        return unipolarDerivative(x);
    }
    
    
    public static double unipolarActivationFunction(double x){
    	double denominator = (float)( 1 + Math.exp(-1*beta*x) ) ;
        return 1/denominator ;
    }
    public static double bipolarActivationFunction(double x){
        return Math.tanh(beta*x) ;
    }
    public static double unipolarDerivative(double x){
    	return beta*unipolarActivationFunction(x)*
                (1-unipolarActivationFunction(x)); 
    }    
    public static double bipolarDerivative(double x){
        return beta*(1-(float)Math.pow(bipolarActivationFunction(x),2)) ;
    }
    
    public static double targetFunction(){
        return 0 ;
    }
    
    public static double newWeightHiddenLay(double oldWeight,double prevLayComputedVal, double thisLayDerivativeVal, 
            Matrix thisLaySum4DerivativeVals,Matrix nextLayWeights,Matrix computedOutputVal, Matrix targetOutputVal ){
    	double newWeight =0;
    	double grad = 0  ; 
       for(int i=0 ; i<targetOutputVal.getHeigth();i++){
            grad += (computedOutputVal.getValue(0, i)-targetOutputVal.getValue(0, i))*
                    derivativeFunction(thisLaySum4DerivativeVals.getValue(0,i))*nextLayWeights.getValue(i+1,0)*  //+1 bo pierwsza do jedynki 
                    thisLayDerivativeVal*prevLayComputedVal ;
        }
       newWeight = oldWeight - learningFactor*grad ;
       return newWeight ;
    }
    
    public static void computeNewHiddenLayWeights(Matrix thisLayWeights, Matrix outputComputedVals,Matrix outputDestinationVals,
            Matrix nextLayNeuronSums, Matrix thisLayNeuronSums, Matrix prevLayComputedVal,
            Matrix nextLayWeights){
    	double newval ;
        for(int i=0;i<thisLayWeights.getHeigth();i++){ // neurons
            for(int a=0;a<thisLayWeights.getWidth();a++){ // weights
                newval = newWeightHiddenLay(thisLayWeights.getValue(a, i), prevLayComputedVal.getValue(0, a), 
                        derivativeFunction(thisLayNeuronSums.getValue(0,i)), nextLayNeuronSums, nextLayWeights, 
                        outputComputedVals, outputDestinationVals) ;
                thisLayWeights.setValue(a,i, newval) ;
            }
        }
    }
    
    public static double newWeightOutput(double oldWeight,double targetOutputVal, 
    		double computedOutputVal, double sumVal,double prevLayComputedVal){
    	double newWeight = oldWeight - learningFactor*
                (computedOutputVal-targetOutputVal)*
                derivativeFunction(sumVal)*prevLayComputedVal ;
        return newWeight ;
    }
    
    public static void computeNewOutputWeights(Matrix outputWeights,
            Matrix outputVals,Matrix neuronResults,Matrix neuronSums, Matrix nuronPrevResults){
        // TODO: [ok] POWINIEN LECIEC PO RZEDZIE ! i pobierac odpowednie wartosci (szczegolnie dla dalszych wartsw ;)
    	double newval ;
        double neuronPrevRes = 0 ;
        // [ok] troche zle robi bo wektor ma postac dwoch wierszy a on se jedzie ciota po kolumnach ;d
//        System.out.println("output vals:\n"+outputVals.print());
//        System.out.println("nuronPrevResults=\n"+nuronPrevResults.print());
        for(int i=0;i<outputWeights.getHeigth();i++){
            
            for(int a=0;a<outputWeights.getWidth();a++){
 //               System.out.println("jade a="+a+" i="+i);
                if(a==0) neuronPrevRes = 1 ;
                else 
                    neuronPrevRes = nuronPrevResults.getValue(0,a-1) ;
                newval = newWeightOutput(outputWeights.getValue(a,i), 
                        outputVals.getValue(0,i), neuronResults.getValue(0, i), 
                        neuronSums.getValue(0,i), neuronPrevRes );
//                System.out.println("i="+i+" a="+a+" newval="+newval);
                outputWeights.setValue(a,i, newval) ;
            }
        }
            
    }
    
}
