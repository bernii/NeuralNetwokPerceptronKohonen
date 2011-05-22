import java.util.Random;
/**
 *
 * @author Berni
 */
public class Weight {
    Random rnd;
    double min;
    double max;
    /** Creates a new instance of Weight */
    public Weight() {
        rnd = new Random();
        min = -1;
        max = 1;
    }
    public void setMinMax(int entries_to_neuron, int neurons_in_lay){
        // according to Ngueyn-Widorw
        // set min and max
        max = Math.pow(Math.pow(neurons_in_lay, -1),
        		Math.pow(entries_to_neuron, -1));
        min = (-1) * max;
    } 
    public void setMinMaxExitNeurons(){
        max = 0.5;
        min = -0.5;
    }
    public double getNextWeight(){
        return Matrix.normalizeValue(rnd.nextFloat(), 0, 1, min, max);
    }
    
}
