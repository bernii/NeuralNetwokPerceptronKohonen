import java.util.ArrayList ;

public class PerceptronBuilder {
	ArrayList<Matrix> weigthMatrixes ;
	ArrayList<Matrix> neuronsResults ;
	ArrayList<Matrix> neuronsSums ;
	Matrix errorsInPeriods ;
	int polaryzation = 1 ;
	static int POLARYZATION_NEURON = 1 ;
	private int max_epchos = 15000 ;
	double maxY = 0 ;
	double minY = 0 ;
	double maxX = 0 ;
	double minX = 0 ;
	private int errorsToCompute = 100 ;
	private float minError = Float.MAX_VALUE ;

	public void setErrorsToCompute(int errNum){
		errorsToCompute = errNum;
	}
	public void setWeigthMatrixes(ArrayList<Matrix> weigthNew){
		weigthMatrixes = weigthNew;
	}
	public void setOutputMinMax(double min, double max){
		maxY = max;
		minY = min;
	}
	public void setInputMinMax(double min, double max){
		maxX = max;
		minX = min;
	}
	public void start_learning(Matrix X,Matrix Y){
		errorsInPeriods = Matrix.create(errorsToCompute, 1, 0);
		int period = 0;
		float errorPeriod = (float) (max_epchos 
				* Math.pow(errorsToCompute, -1));
		float errorPeriodMod = errorPeriod;
		System.out.println("max_epchos / errorsToCompute = " + max_epchos 
				+ "/" + errorsToCompute + " Error displayed every " 
				+ errorPeriod + " iterations");
		if (maxY == 0) {
			maxY = Y.findMaxValue();
		}
		if (minY == 0) {
			minY = Y.findMinValue();
		}
		if (maxX == 0) {
			maxX = X.findMaxValue();
		}
		if (minX == 0) {
			minX = X.findMinValue();
		}
		Y.normalize(0, 1);

		for (int i = 0; i < max_epchos; i++) {
			for (int a = 0; a < X.getWidth(); a++) { 
				this.computeResults(X.getColumn(a), minX, maxX);
				this.learn(X.getColumn(a), 
						Y.getColumn(a), minY, maxY, minX, maxX);
			}    
			if (i == 0 || i > errorPeriodMod) {
				errorPeriodMod += errorPeriod;
				float delta = 0;
				int a; 
				for (a = 0; a < X.getWidth(); a++) {
					this.computeResults(X.getColumn(a), minX, maxX);
					Matrix tempmx = 
						this.getComputedResults(this.neuronsResults.size() - 1);
					tempmx.normalize(-1, 1, minY, maxY);
					delta += tempmx.getValue(0, 0) 
					- Y.getColumn(a).getValue(0, 0); // only 1 result
				}     

				delta = Math.abs(delta);

				System.out.println("delta[" + i + "] = " + (delta / a) + " " + delta);
				errorsInPeriods.setValue(period++, 0, delta);             
				if (delta < minError) {
					minError = delta;
				} 
			}
		}
		Y.normalize(0, 1, minY, maxY);
	}

	/**
	 * Creates a network with weightMatrix - without randomizing them.
	 * @param architecture
	 * @param weightMatrix
	 */
	public void makeNetwok(String architecture, Matrix weightMatrix){
		String []args = architecture.split("-");
		int hidden_lays = args.length - 1;
		weigthMatrixes = new ArrayList<Matrix>();
		Matrix rnd;
		for (int i = 0; i < hidden_lays; i++){
			// actual lay
			if (i == hidden_lays - 1) {
				rnd = weightMatrix;
			} else {
				rnd = weightMatrix;
			}
			weigthMatrixes.add(rnd);
		}
	}

	/**
	 * Creates a network described by string.
	 * ex. architecture = "2-6-2-2"
	 * means 2 entries, two hidden lays (3 neurons and 4 neurons), 1 output
	 * @param architecture
	 */
	public void makeNetwok(String architecture){               
		String []args = architecture.split("-");
//		int entries = Integer.parseInt(args[0]) ;
		int hidden_lays = args.length - 1 ;
//		int outputs = hidden_lays+2 ;
		weigthMatrixes = new ArrayList<Matrix>();
		Matrix rnd;
		for (int i = 0; i < hidden_lays; i++) {
			// create matrix for each layer with random weights
			int weights_at_lay = Integer.parseInt(args[i]) ; 
			// first layer is entry
			int neurons_at_lay ;
			if (i == hidden_lays) {
				neurons_at_lay = Integer.parseInt(args[i]);
			} else {
				neurons_at_lay = Integer.parseInt(args[i + 1]);
			}
			// actual lay
			if (i == hidden_lays - 1) {
				rnd = Matrix.createRand(weights_at_lay 
						+ POLARYZATION_NEURON, 
						neurons_at_lay, Matrix.ExitNeurons);
			} else {
				rnd = Matrix.createRand(weights_at_lay 
						+ POLARYZATION_NEURON, 
						neurons_at_lay, Matrix.PerceptronNeurons);
			}
			weigthMatrixes.add(rnd);
		}
	}

	public Matrix computeResults(Matrix inputColumn){
		double minInput = inputColumn.findMinValue();
		double maxInput = inputColumn.findMaxValue();
		this.computeResults(inputColumn, minInput, maxInput);
		return null;
	}
	public void computeResults(Matrix inputColumn, double minInput, double maxInput){
		neuronsResults = new ArrayList<Matrix>();
		neuronsSums = new ArrayList<Matrix>();
		inputColumn = inputColumn.addTop(POLARYZATION_NEURON) ; // adding polarization neuron
		neuronsResults.add(Matrix.multiplication(this.getLay(0), inputColumn,true,BackPropagation.Activation_TYPE)) ;
		neuronsSums.add(Matrix.multiplication(this.getLay(0), inputColumn,true,Matrix.Sum_TYPE)) ;
		for (int i = 1; i < weigthMatrixes.size(); i++) {
			Matrix prev = this.getComputedResults(i - 1);
			prev = prev.addTop(POLARYZATION_NEURON); // adding polarization neuron
			neuronsResults.add(Matrix.multiplication(this.getLay(i),prev,true,BackPropagation.Activation_TYPE)) ;
			neuronsSums.add(Matrix.multiplication(this.getLay(i),prev,true,Matrix.Sum_TYPE)) ;
		}   
	}
	public void learn(Matrix inputColumn, Matrix outputColumn,double minOutput, double maxOutput, 
			double minInput, double maxInput){
		BackPropagation.computeNewOutputWeights(
				(Matrix) weigthMatrixes.get(weigthMatrixes.size() - 1), 
				outputColumn, 
				(Matrix) neuronsResults.get(neuronsResults.size() - 1), 
				(Matrix) neuronsSums.get(neuronsSums.size() - 1), 
				(Matrix) neuronsResults.get(neuronsResults.size() - 2));
		// computing new output weights - last neuron layer
		for (int i = weigthMatrixes.size() - 2; i >= 0; i--) {
			Matrix prevLayComputedVal;
			if (i == 0) { // if there are no previous columns
				prevLayComputedVal = inputColumn; 
			} else {
				prevLayComputedVal = this.getComputedResults(i - 1);
			}
			// adding POLARYZATION_NEURON to the top of the layer
			prevLayComputedVal = prevLayComputedVal.addTop(POLARYZATION_NEURON) ; 
			// computing backpropagation algorythm for each layer
			BackPropagation.computeNewHiddenLayWeights(
					(Matrix) weigthMatrixes.get(i), 
					(Matrix) neuronsResults.get(i + 1), 
					outputColumn, 
					(Matrix) neuronsSums.get(i + 1), 
					(Matrix) neuronsSums.get(i), prevLayComputedVal, 
					(Matrix) weigthMatrixes.get(i + 1));
			
		}
	}

	/**
	 * Creates an matrix of values computed from input.
	 * @param input
	 * @param outputMin
	 * @param outputMax
	 * @return
	 */
	public Matrix getOutput(Matrix input, double outputMin, double outputMax){
		double[][] arr = new double[input.getWidth()][];
		for (int a = 0; a < input.getWidth(); a++) {
			this.computeResults(input.getColumn(a));
			Matrix tempmx = this.getComputedResults(this.neuronsResults.size() - 1); // 1
			tempmx.normalize(0, 1, outputMin, outputMax);
			arr[a] = new double[tempmx.getHeigth()];
			for (int b = 0; b < tempmx.getHeigth(); b++) {
				arr[a][b] = tempmx.getValue(0, b);       
			}
		}
		return new Matrix(arr);
	}

	public void setEpchos(int num){
		max_epchos = num;
	}
	public Matrix getComputedOutputs(){
		return getComputedResults(neuronsResults.size() - 1);
	}
	public Matrix getComputedSums(int index){
		return (Matrix) neuronsSums.get(index);
	}
	public Matrix getComputedResults(int index){
		return (Matrix) neuronsResults.get(index);
	}
	public Matrix getLay(int index){
		return (Matrix) weigthMatrixes.get(index);
	}
	public String printLay(int index){
		StringBuffer sb = new StringBuffer("");
		Matrix mx = (Matrix) weigthMatrixes.get(index);
		sb.append(mx.print());
		return sb.toString();
	}
}
