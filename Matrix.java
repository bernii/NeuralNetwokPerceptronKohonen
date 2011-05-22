/**
 * Matrix and its operations.
 * @author Berni
 */
public class Matrix {
	static int Sum_TYPE = 3;
	static int ExitNeurons = 5;
	static int PerceptronNeurons = 6;
	static int Custom = 7;

	double[][] array;

	/** Creates a new instance of Matrix */
	public Matrix(final double[][] array) {
		this.array = array;
	}

	//    public static Matrix createByCols(String data){
	//        //ex. "2 3 4 1 3 ; 2 2 1 1 2" // 2 - dimensional matrix
	//        // creates a Matrix from a string   
	//        //    System.out.println("String input=\n"+data) ;
	//           String []cols = data.split(";") ;
	//           String []width = cols[0].split(" ");
	//           int w = cols.length ;
	//           int h = width.length ;    
	//           int dims[] = new int[]{w, h};
	//           double[][] arr = new double[w][h];
	//           int wtmp = w ;
	//           int htmp;
	//           while(wtmp-->0){
	////                Object arrobj = Array.get(arr, wtmp);
	//                if(cols[wtmp].startsWith(" "))
	//                    cols[wtmp] = cols[wtmp].replaceFirst(" ", "");
	//                width = cols[wtmp].split(" ");
	//                htmp = width.length ;
	//                while(htmp-->0)//{
	//       //             System.out.println("htmp="+htmp+" width[htmp]="+width[htmp]);
	//                	arr[wtmp][htmp] = Float.parseFloat(width[htmp]) ;
	////                    Array.setFloat(arrobj, htmp, Float.parseFloat(width[htmp]));
	//      //          }
	//           }
	//           Matrix mx = new Matrix(arr) ;
	//           return mx ;
	//        }

	public int getWidth(){
		return array.length;
	}
	public int getHeigth(){
		return array[0].length;
	}
	public double getValue(int x, int y){
		return array[x][y];
	}
	public void setValue(int x, int y, double value){
		array[x][y] = value;
	} 

	public double findMaxValue(){
		double biggestVal = this.getValue(0, 0);
		for (int i = 0; i < this.getWidth(); i++) {
			for (int a = 0; a < this.getHeigth(); a++) {
				if (this.getValue(i, a) > biggestVal) {
					biggestVal = this.getValue(i, a);
				}
			}
		}
		return biggestVal;
	}
	public double findMinValue(){
		double smallestVal = this.getValue(0, 0);
		for (int i = 0; i < this.getWidth(); i++) {
			for (int a = 0; a < this.getHeigth(); a++) {
				if (this.getValue(i, a) < smallestVal) {
					smallestVal = this.getValue(i, a);
				}
			}
		}
		return smallestVal;
	}
	public float normalizeKohonen(){
		// sum is computed for whole matrix !
		float sumRoot = 0;
		for (int row = 0; row < this.getHeigth(); row++) {
			for (int i = 0; i < this.getWidth(); i++) {
				sumRoot += Math.pow(this.getValue(i, row), 2);
			}
		}

		for (int row = 0; row < this.getHeigth(); row++) {
			for (int i = 0; i < this.getWidth(); i++) {
				this.setValue(i, row, 
						(float) (this.getValue(i, row) / Math.sqrt(sumRoot)));
			}
		}
		return sumRoot;
	}
	public void denormalizeKohonen(float sumRoot){
		for (int row = 0; row < this.getHeigth(); row++) {
			for (int i = 0; i < this.getWidth(); i++) {
				this.setValue(i, row, 
						(float) (this.getValue(i, row) * Math.sqrt(sumRoot)));
			}
		}
	}
	/**
	 * Normalize matrix to given range.
	 * @param oldMin
	 * @param oldMax
	 * @param newMin
	 * @param newMax
	 */
	public void normalize(double oldMin, double oldMax, double newMin, double newMax){  
		double min = oldMin;
		double max = oldMax;
		for (int i = 0; i < this.getWidth(); i++) {
			for (int a = 0; a < this.getHeigth(); a++) {
				this.setValue(i, a,  
						normalizeValue(this.getValue(i, a), 
								min, max, newMin, newMax));
			}
		}
	}

	/**
	 * Normalize matrix to given range.
	 * @param newMin
	 * @param newMax
	 */
	public void normalize(double newMin, double newMax){ 
		double min = this.findMinValue();
		double max = this.findMaxValue();
		for (int i = 0; i < this.getWidth(); i++) {
			for (int a = 0; a < this.getHeigth(); a++) {
				this.setValue(i, a, 
						normalizeValue(this.getValue(i, a), 
								min, max, newMin, newMax));
			}
		}
	}
	/**
	 * Convert one range to another (linearly).
	 * @param value
	 * @param min
	 * @param max
	 * @param newMin
	 * @param newMax
	 * @return
	 */
	public static double normalizeValue(double value, double min, double max,
			double newMin, double newMax){   
		double a = (newMax - newMin) / (max - min);
		double b = newMin - a * min;
		return (a * value + b);
	}

	public Matrix getColumn(int index){
		double[][] out = new double[1][];
		out[0] = array[index];
		return new Matrix(out);
	}
	public Matrix transpose(){
		double[][] arr = new double[array[0].length][array.length];
		for (int i = 0; i < arr.length; i++) {
			for (int a = 0; a < arr[0].length; a++) {
				arr[i][a] = array[a][i];
			}
		}
		return new Matrix(arr);
	}
	public Matrix getRow(int index){
		double[][] out = new double[array.length][1];
		for (int i = 0; i < out.length; i++) {
			out[i][0] = array[i][index];
		}
		return new Matrix(out);
	}
	public static Matrix multiplication(Matrix m1,Matrix m2){
		return multiplication(m1, m2, false, Sum_TYPE);
	}
	
	public static Matrix multiplication(Matrix m1,Matrix m2,boolean secondIsCol,
			int computeFunction){
		double sum = 0;
		double[][] out = new double[m2.getWidth()][m1.getHeigth()];
		for (int i = 0; i < m2.getWidth(); i++) { // col
			for (int index = 0; index < m1.getHeigth(); index++) { // row
				sum = 0;
				for (int a = 0; a < m1.getWidth(); a++) {
					sum += m1.getValue(a, index) * m2.getValue(i, a);
				}
				
				if (computeFunction == BackPropagation.Activation_TYPE){
					sum = BackPropagation.activationFunction(sum);
				} else if (computeFunction == BackPropagation.Derivative_TYPE) {
					sum = BackPropagation.derivativeFunction(sum) ;
				}
				out[i][index] = sum;
			}
		}
		return new Matrix(out);
	}
	
	/**
	 * Adds a 'val' to the top of the Matrix.
	 * @param val
	 * @return
	 */
	public Matrix addTop(float val){
		double[][] out = new double[array.length][array[0].length + 1];
		for (int i = 0; i < array.length; i++) {
			out[i][0] = val;
			for (int a = 0; a < array.length; a++) {
				out[i][a + 1] = array[i][a];
			}
		}
		return new Matrix(out);
	}
	public static Matrix createRand(int w, int h, int type){
		return Matrix.createRand(w, h, type, 0, 0);
	}
	
	/**
	 * Creates Matrix filled with value val.
	 * @param w
	 * @param h
	 * @param val
	 * @return
	 */
	public static Matrix create(int w, int h, double val){
		double[][] arr = new double[w][h];
		int wtmp = w;
		int htmp;
		while (wtmp-- > 0) {
			htmp = h;
			while (htmp-- > 0) {
				arr[wtmp][htmp] = val;
			}
		}
		Matrix mx = new Matrix(arr);
		return mx;
	}
	
	/**
	 * Creates Matrix with random weights.
	 * @param w
	 * @param h
	 * @param type
	 * @param min
	 * @param max
	 * @return
	 */
	public static Matrix createRand(int w, int h,int type, int min, int max){
		Weight wg = new Weight();
		double[][] arr = new double[w][h];
		int wtmp = w;
		int htmp;
		if (type == ExitNeurons) {
			wg.setMinMaxExitNeurons();
		} else if (type == PerceptronNeurons) {
			wg.setMinMax(h, w);     
		} else if (type == Custom) {
			wg.setMinMax(min, max);    
		}
		while (wtmp-- > 0) {
			htmp = h;
			while (htmp-- > 0) {
				arr[wtmp][htmp] = wg.getNextWeight();
			}
		}
		Matrix mx = new Matrix(arr);
		return mx;
	}
	public String print(){
		return print("\n");
	}
	public String print(String customCollonString){
		StringBuffer sb = new StringBuffer("") ;
		for (int i = 0; i < this.getHeigth(); i++) {
			for (int z = 0; z < this.getWidth(); z++) {
				if (i != 0) {
					sb.append("" + this.getValue(z, i) + " ");
				} else {
					sb.append(this.getValue(z, i) + " ");
				}
			}
			sb.append(customCollonString);  
		}
		return sb.toString();
	}
}
