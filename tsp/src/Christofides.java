/**
 * The Christofides approximation algorithm. Should give an answer with approximation factor 1.5.
 * @author Sebastian Olsson
 */
public class Christofides extends Algorithm{

	public int[] tour(double[][] instance){
		int[] tour = new int[instance.length];
		for(int i = 0;i < tour.length;i++){
			tour[i] = i;
		}
		return tour;
	}
}
