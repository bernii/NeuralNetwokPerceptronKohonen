


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
public class SwtGUI {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Composite composite = null;
	private Canvas canvas = null;
	private Text textNumOfEpchos = null;
	private Label labelNumOfEpchos = null;
	private Button buttonNextStep = null;

	public SwtGUI() {
		super();
		// TODO Auto-generated constructor stub
	}
	boolean start = true ;
	Thread update ;
	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		composite = new Composite(sShell, SWT.NONE);
		createCanvas();
		composite.setBounds(new org.eclipse.swt.graphics.Rectangle(2,6,495,329));
		textNumOfEpchos = new Text(composite, SWT.BORDER);
		textNumOfEpchos.setText("100");
		textNumOfEpchos.setSize(new org.eclipse.swt.graphics.Point(44,20));
		textNumOfEpchos.setLocation(new org.eclipse.swt.graphics.Point(399,15));
		labelNumOfEpchos = new Label(composite, SWT.NONE);
		labelNumOfEpchos.setText("Epochs number");
		labelNumOfEpchos.setSize(new org.eclipse.swt.graphics.Point(68,20));
		labelNumOfEpchos.setLocation(new org.eclipse.swt.graphics.Point(330,15));
		buttonNextStep = new Button(composite, SWT.NONE);
		buttonNextStep.setBounds(new org.eclipse.swt.graphics.Rectangle(449,297,38,19));
		buttonNextStep.setText("start");
		labelLambda = new Label(composite, SWT.NONE);
		labelLambda.setText("lambda:");
		labelLambda.setSize(new org.eclipse.swt.graphics.Point(70,20));
		labelLambda.setLocation(new org.eclipse.swt.graphics.Point(329,55));
		textLambda = new Text(composite, SWT.BORDER);
		textLambda.setBounds(new org.eclipse.swt.graphics.Rectangle(399,55,61,20));
		textLambda.setText("1");
		buttonStop = new Button(composite, SWT.NONE);
		buttonStop.setText("stop");
		buttonStop.setLocation(new org.eclipse.swt.graphics.Point(396,297));
		buttonStop.setSize(new org.eclipse.swt.graphics.Point(38,19));
		createGroupNeuronsNum();
		buttonRestart = new Button(composite, SWT.NONE);
		buttonRestart.setLocation(new org.eclipse.swt.graphics.Point(326,297));
		buttonRestart.setText("restart");
		buttonRestart.setBackground(new Color(Display.getCurrent(), 255, 0, 0));
		buttonRestart.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		buttonRestart.setFont(new Font(Display.getDefault(), "Verdana", 7, SWT.BOLD));
		buttonRestart.setSize(new org.eclipse.swt.graphics.Point(48,19));
		createGroupCanvas();
		labelCreateFirst = new Label(composite, SWT.NONE);
		labelCreateFirst.setBounds(new org.eclipse.swt.graphics.Rectangle(346,247,126,12));
		labelCreateFirst.setVisible(false);
		labelCreateFirst.setText("Create a point!");
		buttonRestart
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					update.wait();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				display.syncExec(new Runnable(){
					public void run()
					{
						start = false ;
					}
				});

				int neurWidth = Integer.parseInt(""+textWidth.getText()) ;
				int neurHeigth = Integer.parseInt(""+textHeigth.getText()) ;
				kohNet.makeNetwork(2,neurWidth,neurHeigth) ;
				canvas.redraw() ;


			}
		});
		buttonStop.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					update.wait();
					display.sleep() ;
					start = false ;

					display.wake() ;
					update.destroy() ;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				display.syncExec(new Runnable(){
					public void run()
					{
						start = false ;
					}
				});

			}
		});
		buttonNextStep
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(isOneNeuron){
					int steps = Integer.parseInt(""+textNumOfEpchos.getText()) ;
					start = true ;
					update = new Thread(){
						public void run()
						{
							kohNet.start_learning(Koh) ;
							canvas.redraw() ;
							textLambda.setText(""+kohNet.lambda);
						}
					} ;

					for(int i =0 ; i< steps &&start==true ; i++){
						display.asyncExec(update);
					}
				}
				else
					labelCreateFirst.setVisible(true);

			}
		});
	}

	private static int[] polygonCoord(double xx,double yy){
		int x = (int) (500*xx) ;
		int y = (int) (500*yy) ;
		return new int[] { x-5,y-5,x,y,x+5,y-5 } ;
	}
	/**
	 * This method initializes canvas	
	 *
	 */
	public int liczba_neuronow = 5 ;
	public static boolean isOneNeuron = false ;
	private void createCanvas() {
		canvas = new Canvas(composite, SWT.NONE);
		canvas.setBounds(new org.eclipse.swt.graphics.Rectangle(5,23,300,300));
		canvas.addPaintListener(new org.eclipse.swt.events.PaintListener() {
			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
				if(isOneNeuron==false)
					return ;
				liczba_neuronow = kohNet.neurons.getHeigth() ;
				for(int i=0;i<Koh.getWidth();i++){
					int x = (int)(500*Koh.getValue(i,0)) ;
					int y = (int)(500*Koh.getValue(i,1)) ;
					e.gc.drawOval(x,y,3,3);
				}

				for(int i=0 ; i<liczba_neuronow;i++){
					e.gc.drawPolygon(polygonCoord(kohNet.neurons.getValue(0,i),kohNet.neurons.getValue(1,i)));
				}
			}
		});
		canvas.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				labelCreateFirst.setVisible(false);
				if(!isOneNeuron){
					int neurWidth = Integer.parseInt(""+textWidth.getText()) ;
					int neurHeigth = Integer.parseInt(""+textHeigth.getText()) ;
					kohNet.makeNetwork(2,neurWidth,neurHeigth) ;
				}
				double arr[][] = null ;
				int i =0;
				if(Koh==null) {
					arr = new double[1][];
				} else {
					arr = new double[Koh.array.length+1][2];
					for(i =0 ; i < Koh.array.length ; i++){
						arr[i] = Koh.array[i];
					}
				}
				float x = (float) (e.x*Math.pow(500,-1));
				float y = (float) (e.y*Math.pow(500,-1));
				double[] a = {x,y};
				arr[i] = a ;
				Koh = new Matrix(arr);
				System.out.println("Koh network:\n"+Koh.print());
				canvas.redraw() ;
				isOneNeuron = true ;
				System.out.println("mouseDown() x="+x+" y="+y); 
			}
		});
	}

	/**
	 * @param args
	 */
	static Matrix Koh ;
	static KohonenBuilder kohNet ;
	static float sumRoot ;
	private Label labelLambda = null;
	private Text textLambda = null;
	static Display display ;
	private Button buttonStop = null;
	private Group groupNeuronsNum = null;
	private Label labelWidth = null;
	private Label labelHeigth = null;
	private Text textWidth = null;
	private Text textHeigth = null;
	private Button buttonRestart = null;
	private Group groupCanvas = null;
	private Label labelCreateFirst = null;
	/**
	 * This method initializes groupNeuronsNum	
	 *
	 */
	private void createGroupNeuronsNum() {
		groupNeuronsNum = new Group(composite, SWT.NONE);
		groupNeuronsNum.setText("Neurons network");
		groupNeuronsNum.setBounds(new org.eclipse.swt.graphics.Rectangle(328,120,145,76));
		labelWidth = new Label(groupNeuronsNum, SWT.NONE);
		labelWidth.setBounds(new org.eclipse.swt.graphics.Rectangle(9,26,62,12));
		labelWidth.setText("Width:");
		labelHeigth = new Label(groupNeuronsNum, SWT.NONE);
		labelHeigth.setBounds(new org.eclipse.swt.graphics.Rectangle(8,44,59,12));
		labelHeigth.setText("Height:");
		textWidth = new Text(groupNeuronsNum, SWT.BORDER);
		textWidth.setLocation(new org.eclipse.swt.graphics.Point(78,25));
		textWidth.setText("8");
		textWidth.setSize(new org.eclipse.swt.graphics.Point(35,14));
		textHeigth = new Text(groupNeuronsNum, SWT.BORDER);
		textHeigth.setLocation(new org.eclipse.swt.graphics.Point(78,41));
		textHeigth.setText("8");
		textHeigth.setSize(new org.eclipse.swt.graphics.Point(35,14));
	}

	/**
	 * This method initializes groupCanvas	
	 *
	 */
	private void createGroupCanvas() {
		groupCanvas = new Group(composite, SWT.NONE);
		groupCanvas.setText("Visualization");
		groupCanvas.setBounds(new org.eclipse.swt.graphics.Rectangle(0,6,312,321));
	}

	public static void main(String[] args) {
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		display = new Display();

		//		Koh = Matrix.createByCols("22 22"); 
		//        Koh = Koh.transpose() ;
		//        System.out.println("Koh transposed:\n"+Koh.print()) ;
		//        Koh = Koh.transpose() ;
		//        //Koh = Matrix.createByCols(kohTemp);
		//        System.out.println("Koh after:\n"+Koh.print()) ;
		//        sumRoot = Koh.normalizeKohonen() ;
		//        System.out.println("Koh normalized:\n"+Koh.print()) ;
		float min=(float) 0.013882493 ; // quite good neurons spread
		float max=(float) 0.6293397 ;
		kohNet = new KohonenBuilder() ;
		//     System.out.println("Min="+Koh.findMinValue()+" max="+Koh.findMaxValue());
		kohNet.setWeightsMinMax(min,max);
		kohNet.setEpchosNumber(Integer.parseInt("1")) ; 

		SwtGUI thisClass = new SwtGUI();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Kohonen Network _ swt");
		createComposite();
		sShell.setSize(new org.eclipse.swt.graphics.Point(509,365));
	}

}
