/**
 * The Christofides approximation algorithm. Should give an answer with approximation factor 1.5.
 * @author Sebastian Olsson
 */
public class Christofides extends Algorithm{
	Graph graph;
	Graph mst;
	Graph oddGraph;
	Graph match;
	
	public Christofides(){}

	private void initGraph(float[][] instance){
		graph = new Graph(instance);
		System.err.println(graph);
	}

	public int[] tour(float[][] instance){
		initGraphics(instance,"christofides");
		initGraph(instance);
		int[] tour = new int[instance.length];
		for(int i = 0;i < tour.length;i++){
			tour[i] = i;
		}
		mst = graph.mst();
		System.err.println("mst=\n"+mst);
		oddGraph = mst.oddDegreeGraph();
		System.err.println("oddgraph=\n"+oddGraph);
		match = oddGraph.minimalPerfectMatching();
		System.err.println("match=\n"+match);
		updateVisualization(tourToCycle(tour));
		return tour;
	}
	
	/**
	 * @param A tour, specified in cycle notation, e.g. (0 2 1 3 4)
	 * @return The tour, specified in one-row notation, e.g. [2,3,1,4,0], i.e. {0->2,1->3,2->1,3->4,4->0}.
	 */
	private int[] tourToCycle(int[] tour){
		int[] cycleTour = new int[tour.length];
		int v = 0;
		for(int w = 1;w < tour.length;w++){
			cycleTour[v] = tour[w];
			v = w;
		}
		cycleTour[tour.length-1] = tour[0];
		return cycleTour;
	}

	@Override
    protected void updateVisualization(int[] tour){
    	if(visualize){
//    		repaint(1500);
//    		visualizer.updateSol(tour);
    		repaint(1500);
    		((ChristofidesVisualizer)visualizer).updateMST(mst);
    		repaint(1500);
    		((ChristofidesVisualizer)visualizer).updateOddGraph(oddGraph);
    		repaint(1500);
    		((ChristofidesVisualizer)visualizer).updateMatchGraph(match);
    		repaint(1500);
    	}
    }
	
	private void repaint(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		visualizer.repaint();
	}
}
