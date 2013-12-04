import java.util.ArrayList;
import java.util.List;

/**
 * The Christofides approximation algorithm. Should give an answer with approximation factor 1.5.
 * @author Sebastian Olsson
 */
public class Christofides extends Algorithm{
	Graph graph;
	Graph mst;
	Graph oddGraph;
	Graph match;
	Graph multigraph; /* mst + match */
	Graph hamiltoncycle;
	
	public Christofides(){}

	private void initGraph(float[][] instance){
		graph = new Graph(instance);
		System.err.println(graph);
	}

	public int[] tour(float[][] instance){
		initGraphics(instance,"christofides");
		initGraph(instance);
		int[] tour = new int[instance.length];
		mst = graph.mst();
		System.err.println("mst=\n"+mst);
		oddGraph = mst.oddDegreeGraph();
		System.err.println("oddgraph=\n"+oddGraph);
		match = oddGraph.minimalPerfectMatching();
		System.err.println("match=\n"+match);
		multigraph = new Graph(mst,match);
		System.err.println("multigraph=\n"+multigraph);
		hamiltoncycle = multigraph.hamiltonCycle();
		System.err.println("hamiltongraph=\n"+hamiltoncycle);
		updateVisualization(tourToCycle(tour));

		int[][] edgeCounts = new int[hamiltoncycle.order][hamiltoncycle.order];
		for(int i = 0;i < edgeCounts.length;i++){
			for(int j = 0;j < edgeCounts[i].length;j++){
				edgeCounts[i][j] = hamiltoncycle.edges[i][j];
			}
		}
		List<Integer> tourList = new ArrayList<Integer>();
		tourList.add(0);
		for(int w = 0;w < hamiltoncycle.order;w++){
			int v = tourList.get(tourList.size()-1);
			if(edgeCounts[v][w] > 0){
				edgeCounts[v][w]--;edgeCounts[w][v]--;
				tourList.add(w);
				w = 0;
			}
		}
		for(int i = 0;i < tourList.size();i++){
			tour[i] = tourList.get(i);
		}
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
//    		repaint(1000);
//    		visualizer.updateSol(tour);
    		repaint(1000);
    		((ChristofidesVisualizer)visualizer).updateMST(mst);
    		repaint(1000);
    		((ChristofidesVisualizer)visualizer).updateOddGraph(oddGraph);
    		repaint(1000);
    		((ChristofidesVisualizer)visualizer).updateMatchGraph(match);
    		repaint(1000);
    		((ChristofidesVisualizer)visualizer).updateMultiGraph(multigraph);
    		repaint(1000);
    		((ChristofidesVisualizer)visualizer).updateHamiltonGraph(hamiltoncycle);
    		repaint(1000);
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
