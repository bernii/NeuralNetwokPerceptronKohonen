/**
 *
 * @author Berni
 */
public class KohonenElements {
    
    public static double distance(Matrix x1, Matrix x2){
    // x1 and x2 are two column vectors
    	double result = 0;
        for (int i = 0; i < x1.getHeigth(); i++) {
            result += Math.pow(x1.getValue(0, i) - x2.getValue(0, i), 2);
        }
        result = Math.sqrt(result); // square root
        return result;
    }
    public static double neighborhood(double distance, double lambda){
    	if (distance == 0) { return 1; }
        return Math.exp((-1 * Math.pow(distance, 2) 
        		* Math.pow(2 * Math.pow(lambda, 2), -1)));
    }
    public static double fatigue(double actualFatigue, boolean isWinner,int neurons_num){
    	double fatigue = 1;
        if (isWinner) {
            fatigue = actualFatigue - KohonenBuilder.fatigueMinLevel;
        } else {
        	fatigue = actualFatigue + Math.pow(neurons_num, -1);
        }
        if (fatigue < 0) {
            System.out.println("ERROR!!! fatigue is " + fatigue);
        }
        if (fatigue > 1) {
        	fatigue = 1;
        }
        return fatigue;
    }
    private static double weight(double oldWeight, double learningFactor, 
    		double neighborhood, double input){
        return oldWeight + learningFactor * neighborhood * (input - oldWeight);
    }
    public static void weight(Matrix inputColumn, Matrix weights, 
    		int winnerNumber,double learningFactor,double neighborhood){
    	for (int i = 0; i < inputColumn.getHeigth(); i++) {
    		weights.setValue(i, winnerNumber, 
    				weight(weights.getValue(i, winnerNumber), learningFactor, 
    						neighborhood, inputColumn.getValue(0, i)));
    	}
    }
}
