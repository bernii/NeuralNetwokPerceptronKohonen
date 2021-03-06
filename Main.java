/**
 *
 * @author Berni
 */
public class Main {    
    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
//    	double[][] arrx = {{0, 1, 1}, {1, 1, 1}, {1, 0, 1}, {0, 0, 0}};
    	double[][] arrx = {{0, 0.05, 0.05}, {0,0, 0.25}, {0.5, 0, 0.5}, {0.6, 0.4, 0}};
        Matrix x = new Matrix(arrx);
        System.out.println("X = \n" + x.print());
         System.out.println("Firtst column from X:");
         Matrix firX = x.getColumn(0);
         System.out.println("firX = \n" + firX.print());
         
//         double[][] arry = {{0, 31}, {1, 1}, {1, 0}, {0, 0}};
         double[][] arry = {{0.1}, {0.25}, {1}, {1}};
         Matrix y = new Matrix(arry);
         System.out.println("Y = \n" + y.print());
         System.out.println("Firtst column from Y:");
         Matrix firY = y.getColumn(0);
         System.out.println("firY = \n" + firY.print());
         PerceptronBuilder net = new PerceptronBuilder();
         
         net.makeNetwok(x.getHeigth() + "-6-" + y.getHeigth());
         System.out.println(net.printLay(0));
//         System.out.println("After multiplication - y(0):\n");
//         Matrix y0 = Matrix.multiplication(net.getLay(0), fir,true) ;
//         System.out.println(y0.print());
//         System.out.println(net.printLay(1)) ;
//
//         System.out.println("Before learnin - first lay:\n");
//         System.out.println(net.printLay(0)) ;
//         System.out.println("Computed results:\n");
//         net.computeResults(firX) ;
//         System.out.println(net.getComputedResults(1).print());
//         
//         System.out.println("SUMS :\n"+net.getComputedSums(0).print());
//         System.out.println(net.getComputedSums(1).print());
//         
//         System.out.println("After learnin - first lay:\n");
//         System.out.println(net.printLay(0)) ;
         
         double max = y.findMaxValue();
         double min = y.findMinValue();
         net.setOutputMinMax(min, max);
         net.setEpchos(6000);
         net.start_learning(x, y);
         
         System.out.print("Target outputs:\n" + y.print());
         Matrix out = net.getOutput(x, min, max);
         System.out.println("Computed outputs:\n" + out.print());
         
         /**
          * Kohonen test
          */
//         
//         //Matrix test = Matrix.createByCols("1.5 2.2; 1.2 3.2");
//          Matrix test = Matrix.createByCols("1 0 0 ; 0 1 0");
//         out = net.getOutput(test, min, max) ;
//         System.out.println("Computed test:\n"+out.print());
//         Matrix Koh = Matrix.createByCols("12 3 3 ; 13 4 3 ; 1 2 11 ; 2 2 12 ; 2 1 11");//" ; 150 33 ; 151 30 ; 156 34 ; 11 2"); 
//         Koh = Koh.transpose() ;
//         System.out.println("Koh transposed:\n"+Koh.print()) ;
//          Koh = Koh.transpose() ;
//         System.out.println("Koh transposed once again:\n"+Koh.print()) ;
//         float sumRoot = Koh.normalizeKohonen() ;
//         System.out.println("Koh normalized:\n"+Koh.print()) ;
//         
//         kohonenBuilder kohNet = new kohonenBuilder() ;
//         //kohNet.makeNetwork(2,4,4) ;
//         kohNet.makeNetwork(3,2) ;
//        // System.out.println("Fatigue mx:\n"+kohNet.fatigue.print());
//        // System.out.println("Weights matrix:"+kohNet.neurons.print());
//         Matrix neurons = kohNet.neurons ;
//         kohNet.start_learning(Koh) ;
//         
//         Koh.denormalizeKohonen(sumRoot);
//         System.out.println("Koh denormalized:\n"+Koh.print()) ;
//       //  Matrix neurons = kohNet.neurons ;
//         neurons.denormalizeKohonen(sumRoot) ;
//         //neurons.transpose();
//         System.out.println("Kohonen neurons:\n"+neurons.transpose().print());
//         System.out.println("Wins per neuron:\n"+kohNet.tempWins.transpose().print());
//         System.out.println("Clusters:");
//         Matrix clusters = Matrix.create(Koh.getWidth(), 1, -1);
//         for(int i=0;i<clusters.getWidth();i++){
//             float minDist = Float.MAX_VALUE ;
//             for(int a=0;a<neurons.getHeigth();a++){
//                 if(kohonenElements.distance(neurons.getRow(a),Koh.getColumn(i))<minDist){     
//                     //System.out.println("Winner i="+i+" a="+a);
//                     clusters.setValue(i, 0, a) ;
//                     minDist = kohonenElements.distance(neurons.getRow(a),Koh.getColumn(i)) ;
//                 }
//             }
//         }
//         System.out.println(clusters.print());
//         for(int i =0 ; i< neurons.getHeigth();i++){
//             System.out.println(" neuron["+i+"]   "+neurons.transpose().getColumn(i).print(" "));
//             for(int a=0 ; a<clusters.getWidth();a++){
//                 if(i==clusters.getValue(a,0))
//                     System.out.println("       -  pkt["+a+"] = "+Koh.transpose().getRow(a).print(" ")) ;
//             }
//         }
//         System.out.println("Erros in iterations:\n"+kohNet.error.print());
//         System.out.println("Wypisanie dla Lukasza:\n");
//         for(int i =0 ; i< neurons.getHeigth();i++){
//             if(i!=0) System.out.print("; ");
//             System.out.print("neuron["+i+"] "+neurons.getRow(i).print(""));
//             for(int a=0 ; a<clusters.getWidth();a++){
//                 if(i==clusters.getValue(a,0))
//                     System.out.print("| pkt["+a+"] "+Koh.transpose().getRow(a).print("")) ;
//             }
//         }
    }
    
}
