import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * An undirected Euclidean (multi-)graph.
 * @author Sebastian Olsson
 */
public class Graph{
	public float[][] points; /* (points[i][0],points[i][1]) is the point for vertex vertices[i]. */
	public final int[] vertices; /* :: Index -> ID. Sorted. */
	public int[][] adjacencyMatrix; /* adjacencyMatrix[i][j] == distance(vertices[i],vertices[j]) */
	public final boolean complete;
	public final int order; /* Number of vertices. */
	public final int size; /* Number of edges. */
	public final int[][] edges; /* The number of edges between each pair of vertices. */

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
		edges = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				if(p1 != p2){
					adjacencyMatrix[p1][p2] = distance(points[vertices[p1]],points[vertices[p2]]);
					edges[p1][p2] = 1;
				}else{
					adjacencyMatrix[p1][p2] = 0;
					edges[p1][p2] = 0;
				}
			}
		}
	}
	public Graph(final float[][] points){
		this(points,Util.ring(points.length));
	}

	/* Constructs a spanning tree. */
	public Graph(float[][] points,int[][] arcs,int[][] weights,int[] vertices){
		order = vertices.length;
		size = arcs.length;
		complete = size == Math.max(0,order*(order-1)/2);
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[order][order];
		this.edges = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				adjacencyMatrix[p1][p2] = -1;
				edges[p1][p2] = 0;
			}
		}
		for(int[] arc : arcs){
			setDistance(arc,weights[arc[0]][arc[1]]);
			edges[arc[0]][arc[1]] = 1;
			edges[arc[1]][arc[0]] = 1;
		}
		this.vertices = vertices;
		this.points = points;
	}

	/**
	 * @param points A set of points in the form {(points[0][0],points[0][1]),...}
	 * @param arcs arcs[i][j] == The number of arcs from i to j.
	 * @param vertices A set of vertices.
	 * @param complete True iff the graph is complete.
	 */
	public Graph(float[][] points,int[][] arcs,int[] vertices){
		this.order = vertices.length;
		this.size = arcs.length;
		this.complete = size == Math.max(0,order*(order-1)/2);
		this.vertices = vertices;
		edges = new int[order][order];
		for(int[] arc : arcs){
			edges[arc[0]][arc[1]] += 1;
			edges[arc[1]][arc[0]] += 1;
		}
		this.points = points;
		makeAdjacencyMatrix();
	}

	/** Constructs a matching, where (vertices[i],vertices[i+1]) is an edge for each even i. */
	public Graph(float[][] points,int[] vertices,int totalDistance){
		this.vertices = vertices;
		this.points = points;
		order = vertices.length;
		size = order/2;
		complete = size == Math.max(order-1,0)*(order-1);
		/* Initialize adjacency matrix */
		adjacencyMatrix = new int[order][order];
		edges = new int[order][order];
		for(int p1 = 0;p1 < order;p1++){
			for(int p2 = 0;p2 < order;p2++){
				adjacencyMatrix[p1][p2] = -1;
				edges[p1][p2] = 0;
			}
		}
		for(int i = 0;i < order;i += 2){
			int v = i;
			int w = i+1;
			setDistance(v,w,distance(points[v],points[w]));
			edges[v][w] = 1;
			edges[w][v] = 1;
		}
	}

	/** Constructs the join of two graphs. g1 is assumed to be a supergraph to g1, vertex-ically speaking. */
	public Graph(Graph g1,Graph g2){
		/* Join point sets */
		points = g1.points;
		/* Join vertices */
		vertices = g1.vertices;
		/* Misc */
		order = vertices.length;
		size = Math.max(0,order*(order-1)/2);
		complete = false;
		/* Edge & adjacency matrix */
		edges = new int[order][order];
		adjacencyMatrix = new int[order][order];
		int[] map = new int[g2.order]; // :: Index_g2 -> Index_g1
		for(int i = 0;i < g2.order;i++){
			int v = -1;
			for(int j = 0;j < g1.order;j++){
				if(g2.vertices[i] == g1.vertices[j]){
					v = j;
					break;
				}
			}
			map[i] = v;
		}
		for(int i = 0;i < order;i++){
			for(int j = 0;j < order;j++){
				edges[i][j] = g1.edges[i][j];
				adjacencyMatrix[i][j] = g1.adjacencyMatrix[i][j];
			}
		}
		for(int i = 0;i < g2.order;i++){
			for(int j = 0;j < g2.order;j++){
				if(g2.edges[i][j] > 0){
					edges[map[i]][map[j]] += g2.edges[i][j];
					adjacencyMatrix[map[i]][map[j]] = g2.adjacencyMatrix[i][j];
				}
			}
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

	/**
	 * @return A perfect matching whose sum weight is minimal.
	 * @throws RuntimeException if no perfect matching exists.
	 */
	public Graph minimalPerfectMatching(){
		if(order % 2 != 0){
			throw new RuntimeException("Order is odd!");
		}
		List<Integer> candidateMatching = new ArrayList<Integer>(); /* (cm[0],cm[1]),... */
		int candidateDistance = Integer.MAX_VALUE;
		Permutations pi = new Permutations(vertices);
		Iterator<List<Integer>> it = pi.iterator();
		System.err.println("vertices");
		while(it.hasNext()){
			List<Integer> permutation = it.next();
			int totalDistance = 0;
			for(int i = 0;i < order;i += 2){
				final int v = permutation.get(i);
				final int w = permutation.get(i+1);
				final int distance = adjacencyMatrix[idToIndex(v)][idToIndex(w)];
				totalDistance += distance;
			}
			System.err.println("perm="+permutation+" total="+totalDistance+" cd="+candidateDistance);
			if(totalDistance < candidateDistance){
				candidateDistance = totalDistance;
				candidateMatching = new ArrayList<Integer>(permutation);
			}
		}
		System.err.println("done"+candidateMatching);
		int[] perfectMatchingArray = new int[order];
		for(int i = 0;i < order;i++){
			perfectMatchingArray[i] = candidateMatching.get(i);
		}
		Graph matching = new Graph(points,perfectMatchingArray,candidateDistance);
		return matching;
	}

	/** @return A corresponding Hamiltonian cycle if this graph is an Euclidean circuit. */
	public Graph hamiltonCycle(){
		int[][] edgeCounts = new int[order][order];
		for(int i = 0;i < order;i++){
			for(int j = 0;j < order;j++){
				edgeCounts[i][j] = edges[i][j];
			}
		}
		List<Integer> euclideanCircuit = new ArrayList<Integer>();
		euclideanCircuit.add(0);
		for(int w = 0;w < order;w++){
			int v = euclideanCircuit.get(euclideanCircuit.size()-1);
			if(edgeCounts[v][w] > 0){
				edgeCounts[v][w]--;edgeCounts[w][v]--;
				euclideanCircuit.add(w);
				w = 0;
			}
		}
		List<Integer> hamiltonCycle = new ArrayList<Integer>();
		for(int v : euclideanCircuit){
			if(!hamiltonCycle.contains(v)){
				hamiltonCycle.add(v);
			}
		}
		int[][] arcs = new int[hamiltonCycle.size()][2];
		for(int i = 0;i < hamiltonCycle.size()-1;i++){
			arcs[i][0] = hamiltonCycle.get(i);
			arcs[i][1] = hamiltonCycle.get(i+1);
		}
		arcs[arcs.length-1][0] = hamiltonCycle.get(hamiltonCycle.size()-1);
		arcs[arcs.length-1][1] = hamiltonCycle.get(0);
		List<Integer> verticesList = new ArrayList<Integer>(hamiltonCycle);
		Collections.sort(verticesList);
		int vertices[] = new int[order];
		for(int i = 0;i < order;i++){
			vertices[i] = verticesList.get(i);
		}
		return new Graph(this.points,arcs,vertices);
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

	private void makeAdjacencyMatrix(){
		adjacencyMatrix = new int[order][order];
		for(int v = 0;v < order;v++){
			for(int w = 0;w <= v;w++){
				if(edges[v][w] > 0){
					setDistance(v,w,distance(points[v],points[w]));
				}else{
					setDistance(v,w,-1);
				}
			}
		}
	}

	/* The inverse to the vertices array. */
	public int idToIndex(int id){
		return Arrays.binarySearch(vertices,id);
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