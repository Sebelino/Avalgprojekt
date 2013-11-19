/**
 * An undirected graph with non-negative weights.
 * @author Sebastian Olsson
 */
public class Graph{
	private float[][] points; /** (weights[i][0],weights[i][1]) is the ith point. */
	private int[][] adjacencyMatrix;

	// TODO: Futtig shallow copy-optimering.
	public Graph(final float[][] points){
		this.points = new float[points.length][2];
		for(int i = 0;i < points.length;i++){
			this.points[i][0] = points[i][0];
			this.points[i][1] = points[i][1];
		}
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[points.length][];
		for(int p1 = 0;p1 < points.length;p1++){
			adjacencyMatrix[p1] = new int[p1+1];
			for(int p2 = 0;p2 <= p1;p2++){
				float xDiff = points[p2][0]-points[p1][0];
				float yDiff = points[p2][1]-points[p1][1];
				int distance = (int)(0.5+Math.sqrt(xDiff*xDiff+yDiff*yDiff));
				setEdge(p1,p2,distance);
			}
		}
	}

	public Graph mst(){
		return null;
	}

	private void setEdge(int x,int y,int distance){
		if(x > y){
			setEdge(y,x,distance);
		}else{
			adjacencyMatrix[y][x] = distance;
		}
	}
	
	@Override
	public String toString(){
		String str = "";
		for(int y = 0;y < adjacencyMatrix.length;y++){
			for(int x = 0;x < adjacencyMatrix[y].length;x++){
				str += adjacencyMatrix[y][x]+"\t";
			}
			str += "\n";
		}
		return str;
	}
}