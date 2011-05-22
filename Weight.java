
import java.util.Random ;
/**
 *
 * @author Berni
 */
public class Weight {
    Random rnd  ;
    float min ;
    float max ;
    /** Creates a new instance of Weight */
    public Weight() {
        rnd = new Random() ;
        min = Float.parseFloat("-1") ;
        max = Float.parseFloat("1"); ;
    }
    public void setMinMax(int entries_to_neuron, int neurons_in_lay){
        // according to Ngueyn-Widorw
        // set min and max
        max = Float.parseFloat(""+Math.pow(Math.pow(neurons_in_lay, -1), Math.pow(entries_to_neuron, -1))) ;
        min = (-1)*max ;//Float.parseFloat(""+max+"*(-1)") ;
    } 
    public void setMinMaxExitNeurons(){
        max = Float.parseFloat("0.5");
        min = Float.parseFloat("-0.5") ; 
    }
    public float getNextWeight(){
        return(float)( Matrix.normalizeValue(rnd.nextFloat(),0,1,min,max));
    }
    
}
