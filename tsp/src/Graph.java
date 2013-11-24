import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
/**
 * An undirected graph with non-negative weights.
 * @author Sebastian Olsson
 */
public class Graph{
	private float[][] points; /* (points[i][0],points[i][1]) is the ith point. */
	private final int[][] adjacencyMatrix; /* -1 if no edge. */
	public final boolean complete;
	public final int order; /* Number of vertices. */
	public final int size; /* Number of edges. */

	// TODO: Futtig shallow copy-optimering.
	/** Constructs a complete graph from a set of Euclidean points. */
	public Graph(final float[][] points){
		complete = true;
		order = points.length;
		size = Math.max(order-1,0)*(order-1);
		this.points = new float[order][2];
		for(int i = 0;i < order;i++){
			this.points[i][0] = points[i][0];
			this.points[i][1] = points[i][1];
		}
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				adjacencyMatrix[p1][p2] = distance(points[p1],points[p2]);
			}
		}
	}

	/** Constructs a spanning tree from the edges and matrix of weights. */
	public Graph(int[][] edges,int[][] weights){
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
		int edgeCtr = 0;
		while(vertices.size() != order){
			int[] candidateVertex = new int[2];
			int candidateDistance = Integer.MAX_VALUE;
			for(int v : vertices){
				for(int w : neighbors(v)){
					if(!vertices.contains(w) && adjacencyMatrix[v][w] < candidateDistance){
						candidateVertex[0] = v;
						candidateVertex[1] = w;
						candidateDistance = adjacencyMatrix[v][w];
					}
				}
			}
			vertices.add(candidateVertex[1]);
			System.err.println("erst"+vertices+"cand="+candidateVertex[1]);
			edges[edgeCtr][0] = candidateVertex[0];
			edges[edgeCtr][1] = candidateVertex[1];
			edgeCtr++;
		}
		Graph mst = new Graph(edges,adjacencyMatrix);
		return mst;
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