import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An undirected graph with non-negative weights.
 * @author Sebastian Olsson
 */
public class Graph{
	private float[][] points; /* (points[i][0],points[i][1]) is the point for vertex vertices[i]. */
	private final int[] vertices; /* :: Index -> ID. Sorted. */
	private final int[][] adjacencyMatrix; /* adjacencyMatrix[i][j] == distance(vertices[i],vertices[j]) */
	public final boolean complete;
	public final int order; /* Number of vertices. */
	public final int size; /* Number of edges. */

	// TODO: Futtig shallow copy-optimering.
	/** Constructs a complete graph from a set of Euclidean points. */
	public Graph(final float[][] points,int[] vertices){
		complete = true;
		order = vertices.length;
		size = Math.max(0,order*(order-1)/2);
		this.vertices = vertices;
		this.points = points;
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				adjacencyMatrix[p1][p2] = distance(points[p1],points[p2]);
			}
		}
	}
	public Graph(final float[][] points){
		this(points,Util.ring(points.length));
	}

	/** Constructs a spanning tree from the edges and matrix of weights. */
	public Graph(float[][] points,int[][] edges,int[][] weights,int[] vertices){
		order = weights.length;
		size = edges.length;
		complete = size == Math.max(order-1,0)*(order-1);
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				adjacencyMatrix[p1][p2] = -1;
			}
		}
		for(int[] edge : edges){
			setDistance(edge,weights[edge[0]][edge[1]]);
		}
		this.vertices = vertices;
		this.points = points;
	}

	/** @return The minimal spanning tree of this graph. */
	public Graph mst(){
		if(!complete){
			throw new RuntimeException();
		}
		return prim();
	}

	/** @return The minimal spanning tree of this graph using Prim's algorithm. */
	private Graph prim(){
		final int initialVertex = 0;
		int vertex = initialVertex;
		int[][] edges = new int[order-1][2];
		Set<Integer> vertices = new HashSet<Integer>();
		vertices.add(vertex);
		for(int edgeCtr = 0;edgeCtr < edges.length;edgeCtr++){
			int[] candidateVertex = new int[2];
			int candidateDistance = Integer.MAX_VALUE;
			for(int v : vertices){
				for(int w : neighbors(v)){
					// TODO
					if(!vertices.contains(w) && adjacencyMatrix[v][w] < candidateDistance){
						candidateVertex[0] = v;
						candidateVertex[1] = w;
						candidateDistance = adjacencyMatrix[v][w];
					}
				}
			}
			vertices.add(candidateVertex[1]);
			edges[edgeCtr][0] = candidateVertex[0];
			edges[edgeCtr][1] = candidateVertex[1];
		}
		// I hate primitive types in Java.
		int[] verticesArray = new int[vertices.size()];
		Integer[] verticesList = Arrays.copyOf(vertices.toArray(),vertices.size(),Integer[].class);
		for(int v = 0;v < vertices.size();v++){
			verticesArray[v] = verticesList[v];
		}
		System.err.println("creating mst");
		Graph mst = new Graph(points,edges,adjacencyMatrix,verticesArray);
		return mst;
	}

	/**
	 * @return The complete graph that is given when forming an edge between each pair
	 * of odd-degreed vertices.
	 */
	public Graph oddDegreeGraph(){
		Set<Integer> oddVertices = new HashSet<Integer>();
		for(int i = 0;i < order;i++){
			if(degree(i) % 2 == 1){
				oddVertices.add(vertices[i]);
			}
		}
		int[] verticesArray = new int[oddVertices.size()];
		Integer[] verticesList = Arrays.copyOf(oddVertices.toArray(),oddVertices.size(),Integer[].class);
		for(int v = 0;v < oddVertices.size();v++){
			verticesArray[v] = verticesList[v];
		}
		return new Graph(points,verticesArray);
	}

	public int degree(int v){
		int deg = 0;
		for(int d : adjacencyMatrix[v]){
			if(d >= 0){
				deg++;
			}
		}
		return deg;
	}

	/** @return The Euclidean distance between p1 and p2. */
	private int distance(float[] p1,float[] p2){
		return (int)(0.5+Math.sqrt((p2[0]-p1[0])*(p2[0]-p1[0])+(p2[1]-p1[1])*(p2[1]-p1[1])));
	}

	/** @return The direct successors of vertex v. */
	public Set<Integer> neighbors(int v){
		Set<Integer> neighbors = new HashSet<Integer>();
		for(int i = 0;i < order;i++){
			if(adjacencyMatrix[v][i] > 0){
				neighbors.add(i);
			}
		}
		return neighbors;
	}

	/** @return Sets the distance between vertices v and w. */
	private void setDistance(int v,int w,int distance){
		adjacencyMatrix[v][w] = distance;
		adjacencyMatrix[w][v] = distance;
	}
	private void setDistance(int[] edge,int distance){
		setDistance(edge[0],edge[1],distance);
	}

	// TODO: dynprog
	/** @return a set of lines that this graph consists of. */
	public int[][] edges(){
		System.err.println(order+"o,s"+size);
		int[][] edges = new int[size][2];
		int ctr = 0;
		for(int i = 0;i < order;i++){
			for(int j = 0;j <= i;j++){
				if(adjacencyMatrix[i][j] > 0){
					edges[ctr][0] = vertices[i];
					edges[ctr][1] = vertices[j];
					ctr++;
				}
			}
		}
		return edges;
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