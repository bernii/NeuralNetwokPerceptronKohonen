/**
 * Build Kohonen network
 * @author berni
 *
 */
public class KohonenBuilder {
	public Matrix neurons ;
	Matrix fatigue ;
	Matrix tempWins ; 
	Matrix error ;
	private Matrix distancesFromWinner ;
	private int max_epchos = 100 ;
	double maxVal = 1 ;
	double minVal = 0 ;
	double lambda = 0.02 ;//Float.valueOf("0.003") ;
	double minimalLambda = 0.00000001 ;
	double startLearningFactor = 0.02  ;
	static double fatigueMaxLevel = 1 ;
	static double fatigueMinLevel = 0.25/*(float)0.75*/ ;

	public void setEpchosNumber(int epchos) {
		max_epchos = epchos;
	}
	public int getEpchosNumber() {
		return max_epchos;
	}
	/**
	 * Initialization of matrix filled with random numbers.
	 * @param entries_num
	 * @param neurons_num
	 */
	public void makeNetwork(int entries_num, int neurons_num) {
		neurons = Matrix.createRand(entries_num, neurons_num, Matrix.Custom, -1, 1);
		fatigue = Matrix.create(1, neurons_num, fatigueMaxLevel);
		tempWins = Matrix.create(1, neurons_num, fatigueMaxLevel);   
		distancesFromWinner = Matrix.create(1, neurons_num, fatigueMaxLevel); 
	}
	public void setWeightsMinMax(float min, float max) {
		maxVal = max;
		minVal = min;
	}

	/**
	 * Making the matrix with weights are in equal spaces (2D).
	 * @param entries_num
	 * @param netHeight
	 * @param netWidth
	 */
	public void makeNetwork(int entries_num, int netHeight, int netWidth){
		double vertiacalSpace = ((Math.abs(maxVal) + Math.abs(minVal)) 
				* Math.pow(netWidth, -1));
		double horizontalSpace = ((Math.abs(maxVal) + Math.abs(minVal)) 
				* Math.pow(netHeight, -1));
		System.out.println("IN vert=" + vertiacalSpace + " hor=" + horizontalSpace);
		double[][] arr = new double[1][entries_num];
		for (int i = 0; i < entries_num; i++) {
			arr[0][i] = minVal;
		}
		double xpos = minVal;
		double ypos = minVal;
		System.out.println("Minimal val:" + minVal);

		arr = new double[netHeight * netWidth][2];
		for (int i = 0; i < netHeight * netWidth; i++) {
			arr[i][0] = xpos;
			arr[i][1] = ypos;
			xpos += horizontalSpace;
			if (xpos >= maxVal) {
				xpos = minVal;
				ypos += vertiacalSpace;
			}
		}       
		neurons = new Matrix(arr).transpose(); 
		System.out.println("Neurons:\n" + neurons.print());
		fatigue = Matrix.create(1, netHeight * netWidth, fatigueMaxLevel);
		tempWins = Matrix.create(1, netHeight * netWidth, fatigueMaxLevel);   
		distancesFromWinner = Matrix.create(1, netHeight * netWidth, fatigueMaxLevel);
	}

	public void start_learning(Matrix X){
		// creating matrix for errors in each iteration 
		error = Matrix.create(max_epchos, 1, -1);
		double errSum;
		for (int i = 0; i < max_epchos; i++) {
			errSum = 0;
			if (lambda > minimalLambda) {
				lambda = this.lambda - 0.001 * (i + 1) / 5; //max_epchos ; 
			} else {
				lambda = minimalLambda;
			}
			for (int colNum = 0; colNum < X.getWidth(); colNum++) {
				Matrix Xcol = X.getColumn(colNum);
				Matrix neuronWeights;
				double distance = Float.POSITIVE_INFINITY;
				int winnerNumber = -1;
				// searching for winner 
				for (int a = 0; a < neurons.getHeigth(); a++) {
					if (fatigue.getValue(0, a) < fatigueMinLevel) {
						continue;
					}
					neuronWeights = neurons.getRow(a).transpose();
					if (KohonenElements.distance(Xcol, neuronWeights) < distance) {
						distance = KohonenElements.distance(Xcol, neuronWeights);
						winnerNumber = a;
					}
				}
				// fatiguing neurons and computing distance from winner
				for (int a = 0; a < neurons.getHeigth(); a++) {
					// fatiguing the winner
					if (a == winnerNumber) {
						fatigue.setValue(0, winnerNumber, 
								KohonenElements.fatigue(fatigue.getValue(0, 
										winnerNumber), 
										true, neurons.getHeigth()));
					} else { // fatiguing the rest
						fatigue.setValue(0, a, 
								KohonenElements.fatigue(fatigue.getValue(0, a), 
										false, neurons.getHeigth()));
					}
					distancesFromWinner.setValue(0, a, 
							KohonenElements.distance(neurons.getRow(winnerNumber).transpose(), 
									neurons.getRow(a).transpose()));
				}               
				errSum = errSum + Math.pow(KohonenElements.distance(Xcol, 
						neurons.getRow(winnerNumber).transpose()), 2);
				tempWins.setValue(0, winnerNumber, 
						tempWins.getValue(0, winnerNumber) + 1);
				for (int actNeuron = 0; actNeuron < neurons.getHeigth(); actNeuron++) {
					double distanceFromWinner = distancesFromWinner.getValue(0, actNeuron); //kohonenElements.distance(neurons.getRow(winnerNumber).transpose(), neurons.getRow(actNeuron).transpose());
					// computing distance between winner and actual neuron
					double learningFactor = (1 - distanceFromWinner) / 12;// 42 OK
					double neighborhood = KohonenElements.neighborhood(distanceFromWinner, 
							lambda);
					// modifying weights
					KohonenElements.weight(Xcol, neurons, 
							actNeuron, learningFactor, neighborhood);
				}
			}
			error.setValue(i, 0, errSum);
		}
	}
}
