/**
 * The Christofides approximation algorithm. Should give an answer with approximation factor 1.5.
 * @author Sebastian Olsson
 */
public class Christofides extends Algorithm{
	Graph graph;
	
	public Christofides(){}

	private void initGraph(float[][] instance){
		graph = new Graph(instance);
		System.err.println(graph);
	}

	public int[] tour(float[][] instance){
		initGraph(instance);
		int[] tour = new int[instance.length];
		for(int i = 0;i < tour.length;i++){
			tour[i] = i;
		}
		return tour;
	}
}
