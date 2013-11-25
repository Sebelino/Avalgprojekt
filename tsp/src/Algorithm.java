import javax.swing.JFrame;
/**
 * Abstract class for a algorithm solving the traveling salesman problem.
 * Every algorithm should be wrapped in a class and inherit this class.
 */
public abstract class Algorithm{
    /**
     * @return The tour, represented as a permutation of the set {1,...,n}.
     * @param instance A set of (x,y) points, where instance[i] is the ith point.
     */
    public abstract int[] tour(float[][] instance);
    protected Visualizer visualizer;
    protected boolean visualize;

    /** Construct an algorithm with visualization turned off. */
    public Algorithm(){
    	visualize = false;
    	visualizer = null;
    }

    /** Turn visualization on. Should be called before
     * computing the tour and before calling updateVisualization. */
    protected void initGraphics(float[][] coordinates){
		JFrame frame = new JFrame("BasicPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);
        visualizer = new Visualizer(coordinates, 600, 600);
        visualize = true;
        frame.setContentPane(visualizer);
        frame.setVisible(true); 
    }
    
    /** Redraw the canvas. */
    protected void updateVisualization(int[] currSol){
    	if(visualize){
    		visualizer.updateSol(currSol);
    		visualizer.repaint();
    	}
    }
}
