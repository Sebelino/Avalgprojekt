import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;


public class Opt {
	private static final boolean VISUALIZE = false;
	private static final long TIME_LIMIT = 1400000000l;
	
	private static float[][] coords;
	private static float[][] dists;
	private static Edge[] edges;
	private static cityDist[][] nearest;
	
	private static int[] tour;
	private static int[] tourInv;

	private static Random rand;
	private static Visualizer vis;
	
	private static long startTime;
	
	public static void main(String[] args) {
		startTime = System.nanoTime();
		rand = new Random();
		parseInput();
		computeDistances();
		if(coords.length <= 3) {
			printTrivial();
			System.exit(0);
		}
		computeEdgeOrder();
		if(VISUALIZE) initGraphics();
//		tour = generateRandomSolution();
//		tour = generateNNSolution();
		tour = generateGreedySolution();

		computeNearestUsingEdges();
//		computeNearest();
//		for(int i = 0;i<nearest.length;i++) {
//			for(int j = 0;j<nearest[0].length;j++) {
//				System.out.print(nearest[i][j].city + " ");
//			}
//			System.out.println();
//		}
		float best = Float.MAX_VALUE;
		int[] bestTour = new int[coords.length];
		for(int i = 0;i<tour.length;i++) {
			bestTour[i] = tour[i];
		}
		int iters = 0;
		
		while(System.nanoTime()-startTime < TIME_LIMIT) {
			iters++;
//			System .out.println("hej");
			computeInvTour(tour);
//			System.out.println(Arrays.toString(tour));
			int[] newTour = threeOpt(15);
			newTour = twoOpt(15, newTour);
//			System.out.println("blubi");
			float newVal = evalTour(newTour);
//			System.out.println("poop");
			if(newVal < best) {
				best = newVal;
				bestTour = newTour;
			}
		}
//		System.out.println("sup");
		tour = bestTour;
		
//		computeInvTour(tour);
//		tour = twoOpt(20);
		printSolution();
//		System.err.println("Distance: " + trueEvalTour(tour));
//		System.err.println("Iterations: " + iters);
//		System.out.println(trueEvalTour(tour));
//		printTrivial();
		if(VISUALIZE) updateVisualization(genVisMatrix());
	}
	
	private static void computeInvTour(int[] newTour) {
		tourInv = new int[coords.length];
		for(int i = 0;i<newTour.length;i++) {
			tourInv[newTour[i]] = i;
		}
	}

	private static int[] generateGreedySolution() {
		int[][] sol = new int[coords.length][2];
		for(int i = 0;i<sol.length;i++) {
			sol[i][0] = -1;
			sol[i][1] = -1;
		}
		
		int count = 0;
		int index = 0;
		while(count < coords.length) {
//			System.out.println(count);
			int startDeg = 0;
			int endDeg = 0;
			Edge e = edges[index];
			if(sol[e.start][0] != -1) {
				startDeg++;
				if(sol[e.start][1] != -1) {
					startDeg++;
				}
			}
			if(startDeg < 2) {
				if(sol[e.end][0] != -1) {
					endDeg++;
					if(sol[e.end][1] != -1) {
						endDeg++;
					}
				}
				if(startDeg == 1 && endDeg == 1) {
					if(!isCycle(sol, e.start, e.end) || count == coords.length-1) {
						sol[e.start][1] = e.end;
						sol[e.end][1] = e.start;
						count++;
					}
				}
				else if(endDeg < 2) {
					if(startDeg == 0) {
						sol[e.start][0] = e.end;
					}
					else {
						sol[e.start][1] = e.end;
					}
					if(endDeg == 0) {
						sol[e.end][0] = e.start;
					}
					else {
						sol[e.end][1] = e.start;
					}
					count++;
				}
			}
			index++;
		}
//		System.out.println(index);
		int[] t = new int[coords.length];
		
		t[0] = 0;
		t[1] = sol[0][0];
		int city = sol[0][0];
//		int prev = 0;
		for(int i = 2;i<coords.length;i++) {
			if(sol[t[i-1]][0] != t[i-2]) {
				t[i] = sol[t[i-1]][0];
			}
			else if(sol[t[i-1]][1] != t[i-2]) {
				t[i] = sol[t[i-1]][1];
			}
//			prev = t[i];
		}
		
		return t;
	}

	private static float trueEvalTour(int[] newTour) {
		float dist = 0;
		
		for(int i = 0;i<newTour.length-1;i++) {
			dist += Math.sqrt(dists[newTour[i]][newTour[i+1]]);
		}
		dist += Math.sqrt(dists[newTour[newTour.length-1]][newTour[0]]);
		
		return dist; 
	}
	
	private static float evalTour(int[] newTour) {
		float dist = 0;
		
		for(int i = 0;i<newTour.length-1;i++) {
			dist += dists[newTour[i]][newTour[i+1]];
		}
		dist += dists[newTour[newTour.length-1]][newTour[0]];
		
		return dist; 
	}
	
	private static boolean isCycle(int[][] sol, int start, int end) {
		int curr = sol[start][0];
		int prev = start;
//		if(curr == -1) {
//			curr = sol[start][1];
//		}
		while(true) {
//			System.out.println("curr " + curr);
//			System.out.println("curr[0] " + sol[curr][0]);
//			System.out.println("curr[1] " + sol[curr][1]);
//			System.out.println("prev " + prev);
//			System.out.println("start " + start);
//			System.out.println("end " + end);
			if(curr == end) {
				return true;
			}
			if(sol[curr][1] == -1) {
				return false;
			}
			if(sol[curr][0] != prev) {
				prev = curr;
				curr = sol[curr][0];
			}
			else if(sol[curr][1] != prev) {
				prev = curr;
				curr = sol[curr][1];
			}
		}
	}

	private static void computeEdgeOrder() {
		edges = new Edge[coords.length*(coords.length-1)/2];
		int curr = 0;
		for(int i = 0;i<dists.length;i++) {
			for(int j = i+1;j < dists.length;j++) {
				edges[(j-(i+1))+curr] = new Edge(i, j, dists[i][j]);
//				System.out.println((j-(i+1))+curr);
			}
			curr += dists.length-(i+1);
		}
		Arrays.sort(edges);
	}

	private static void updateVisualization(int[][] currSol) {
		if(VISUALIZE) {
			vis.updateSol(currSol);
			vis.repaint();
		}
	}
	
	private static void initGraphics() {
		JFrame frame = new JFrame("BasicPanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,600);

		vis = new Visualizer(coords, 600, 600);
		frame.setContentPane(vis);          
		frame.setVisible(true); 
	}
	
	private static int[][] genVisMatrix() {
		int[][] sol = new int[coords.length][2];
		
		for(int i = 0;i<coords.length;i++) {
			if(i == coords.length-1) {
				sol[tour[i]][0] = tour[0];
				sol[tour[0]][1] = tour[i];
			}
			else {
				sol[tour[i]][0] = tour[i+1];
				sol[tour[i+1]][1] = tour[i];
			}
			 
		}

		return sol;
	}
	
	private static void printSolution() {
		for(int i = 0;i<tour.length;i++) {
			System.out.println(tour[i]);
		}
		
		
		// CONTROL CHECK
//		boolean[] yes = new boolean[tour.length];
//		for(int i = 0;i<tour.length;i++) {
//			yes[tour[i]] = true;
//		}
//		for(int i = 0;i<tour.length;i++) {
//			if(!yes[i]) {
//				int a = 1/0;
//				break;
//			}
//		}
	}

	private static int[] twoOpt(int k, int[] otherTour) {
		int[] newTour = new int[coords.length];
		for(int i = 0;i<tour.length;i++) {
			newTour[i] = otherTour[i];
		}
		
		if(k >= coords.length) {
			k = coords.length-1; 
		}
//		return;
		boolean change = true;
		int iters =0;
		while(change) {
			change = false;
			
			LinkedList<Integer> cities = new LinkedList<Integer>();
			for(int i = 0;i<coords.length;i++) {
				cities.add(i);
			}
			Collections.shuffle(cities);
//			System.out.println(cities);
			
			for(int i : cities) {
//			for(int i = 0;i<tour.length;i++) {
//				if(change) break;
				for(int j = 0;j<k;j++) { // Self is not there if nearestusingedges is used
//					if(change) break;
					int t1 = newTour[i];
					int tmp = i+1; if(tmp == newTour.length)tmp=0;
					int t2 = newTour[tmp];
					int t3 = nearest[t2][j].city;
					if(t3 == t1) {
						break;
					}
//					if(t2 == t3) {
//						int a = 1/0;
//					}
					else if(t2 != t3){
						tmp = (tourInv[t3]-1); if(tmp == -1)tmp=newTour.length-1;
						int t4 = newTour[tmp];
						if((dists[t2][t3] + dists[t1][t4]) < (dists[t1][t2] + dists[t4][t3])) {
//							System.out.println(Arrays.toString(tour));
//							System.out.println(Arrays.toString(tourInv));
//							System.out.println("t1: " + t1 + " t2: " + t2 + " t3: " + t3 + " t4 " + t4);
							twoSwitch(t1, t2, t3, t4, newTour);
							change = true;
//							iters++;
//							if(iters > 10) System.out.println("hej");
						}
					}
				}
			}
//			iters++;
		}
		return newTour;
	}
	
	private static int[] twoOpt(int k) {
		int[] newTour = new int[coords.length];
		for(int i = 0;i<tour.length;i++) {
			newTour[i] = tour[i];
		}
		
		if(k >= coords.length) {
			k = coords.length-1; 
		}
//		return;
		boolean change = true;
		int iters =0;
		while(change) {
			change = false;
			
			LinkedList<Integer> cities = new LinkedList<Integer>();
			for(int i = 0;i<coords.length;i++) {
				cities.add(i);
			}
			Collections.shuffle(cities);
//			System.out.println(cities);
			
			for(int i : cities) {
//			for(int i = 0;i<tour.length;i++) {
//				if(change) break;
				for(int j = 0;j<k;j++) { // Self is not there if nearestusingedges is used
//					if(change) break;
					int t1 = newTour[i];
					int tmp = i+1; if(tmp == newTour.length)tmp=0;
					int t2 = newTour[tmp];
					int t3 = nearest[t2][j].city;
					if(t3 == t1) {
						break;
					}
//					if(t2 == t3) {
//						int a = 1/0;
//					}
					else if(t2 != t3){
						tmp = (tourInv[t3]-1); if(tmp == -1)tmp=newTour.length-1;
						int t4 = newTour[tmp];
						if((dists[t2][t3] + dists[t1][t4]) < (dists[t1][t2] + dists[t4][t3])) {
//							System.out.println(Arrays.toString(tour));
//							System.out.println(Arrays.toString(tourInv));
//							System.out.println("t1: " + t1 + " t2: " + t2 + " t3: " + t3 + " t4 " + t4);
							twoSwitch(t1, t2, t3, t4, newTour);
							change = true;
//							iters++;
//							if(iters > 10) System.out.println("hej");
						}
					}
				}
			}
//			iters++;
		}
		return newTour;
	}

	private static int[] threeOpt(int k) {
		int[] newTour = new int[coords.length];
		for(int i = 0;i<tour.length;i++) {
			newTour[i] = tour[i];
		}
		
		if(k >= coords.length) {
			k = coords.length-1; 
		}
//		return;
		boolean change = true;
		int iters =0;
		while(change) {
			change = false;
			
			LinkedList<Integer> cities = new LinkedList<Integer>();
			for(int i = 0;i<coords.length;i++) {
				cities.add(i);
			}
			Collections.shuffle(cities);
//			System.out.println(cities);
			
			for(int i : cities) {
				if(System.nanoTime() - startTime > TIME_LIMIT) {
					return newTour;
				}
				int t1 = newTour[i];
				int tmp = i+1; if(tmp == newTour.length)tmp=0;
				int t2 = newTour[tmp];
				for(int j = 0;j<k && !change;j++) { // Self is not there if nearestusingedges is used
					int t3 = nearest[t2][j].city;
					if(t3 == t1) {
						break;
					}
//					if(t2 == t3) {
//						int a = 1/0;
//					}
					else if(t2 != t3){
						tmp = (tourInv[t3]-1); if(tmp == -1)tmp=newTour.length-1;
						int t4 = newTour[tmp];
						if(t4 != t2) {
							float partDist = dists[t1][t2] + dists[t3][t4] - dists[t2][t3];
							for(int l = 0;l<k && !change;l++) {
								int t5 = nearest[t4][l].city;
								if(dists[t4][t5] >= partDist) {
									break;
								}
								boolean good = false;
								if(tourInv[t5] > tourInv[t1]) {
									if(tourInv[t3] > tourInv[t1] && tourInv[t5] > tourInv[t3]) {
										good = true;
									}
								}
								else {
									if(tourInv[t5] > tourInv[t3] || tourInv[t3] > tourInv[t1]) {
										good = true;
									}
								}
								if(good && t5 != t4 && t5 != t3 && t5 != t2 && t5 != t1) {
									tmp = (tourInv[t5]-1); if(tmp == -1)tmp=newTour.length-1;
									int t6 = newTour[tmp];
									if(t6 != t4 && t6 != t3 && t6 != t2 && t6 != t1) {
										if((dists[t1][t2] + dists[t3][t4] + dists[t5][t6]) > (dists[t1][t6] + dists[t2][t3] + dists[t4][t5])) {
											threeSwitch(t1, t2, t3, t4, t5, t6, newTour);
											change = true;
										}
									}
								}
							}
						}
						
					}
				}
			}
//			iters++;
		}
		return newTour;
	}
	
	private static void threeSwitch(int t1, int t2, int t3, int t4, int t5,
			int t6, int[] newTour) {
		int[] copyTour = new int[newTour.length];
		for(int i = 0;i<newTour.length;i++) {
			copyTour[i] = newTour[i];
		}
//		System.out.println(t1 + " " + t2 + " " + t3 + " " + t4 + " " + t5 + " " + t6);
//		System.out.println(Arrays.toString(newTour));
		newTour[0] = t1;
//		newTour[tourInv[t2]] = t6;
		int curr = tourInv[t6];
		int index = 1;
		while(copyTour[curr] != t3) {
			newTour[index] = copyTour[curr];
			index++;
			curr--;
			if(curr < 0) curr = newTour.length-1;
		}
		newTour[index] = t3;
		index++;
		curr = tourInv[t2];
		while(copyTour[curr] != t4) {
//			System.out.println(newTour[index]);
			newTour[index] = copyTour[curr];
			index++;
			curr++;
			if(curr >= newTour.length) curr = 0;
		}
		newTour[index] = t4;
		index++;
		curr = tourInv[t5];
		while(copyTour[curr] != t1) {
			newTour[index] = copyTour[curr];
			index++;
			curr++;
			if(curr >= newTour.length) curr = 0;
		}
//		System.out.println(Arrays.toString(newTour));
		computeInvTour(newTour);
	}

	private static void twoSwitch(int t1, int t2, int t3, int t4, int[] newTour) {
		newTour[tourInv[t2]] = t4;
		newTour[tourInv[t4]] = t2;
		
		int i = tourInv[t2]+1; if(i == newTour.length) i=0; 
		int j = tourInv[t4]-1; if(j == -1)j=newTour.length-1;
		boolean[] switched = new boolean[newTour.length];
		switched[tourInv[t2]] = true;
		switched[tourInv[t4]] = true;
		while(i != j && !switched[i] && !switched[j]) {
			int tmp = newTour[i];
			newTour[i] = newTour[j];
			newTour[j] = tmp;
			switched[i] = true;
			switched[j] = true;
			i++; if(i == newTour.length) i=0; 
			j--; if(j == -1)j=newTour.length-1;
		}
		
		int t = tourInv[t2];
		while(newTour[t] != t3) {
			tourInv[newTour[t]] = t;
			t++; if(t == newTour.length) t=0; 
		}
		
	}

	private static int[] generateNNSolution() {
		int[] t = new int[coords.length];
		boolean[] used = new boolean[coords.length];

		t[0] = 0;
		used[0] = true;
		for(int i = 1;i<coords.length;i++) {
			int best = -1;
			for(int j = 0;j<coords.length;j++) {
				if(!used[j] && (best == -1 || dists[t[i-1]][j] < dists[t[i-1]][best]))
					best = j;

			}
			t[i] = best;
			used[best] = true;
		}
		
		tourInv = new int[coords.length];
		for(int i = 0;i<t.length;i++) {
			tourInv[t[i]] = i;
		}

		return t;
	}
	
	private static int[] generateRandomSolution() {
		int[] t = new int[coords.length];

		ArrayList<Integer> list = new ArrayList<Integer>(coords.length);
		for(int i = 0;i<coords.length;i++) {
			list.add(i);
		}

		int index = 0;
		while(!list.isEmpty()) {
			int next = list.remove(rand.nextInt(list.size()));
			t[index] = next;
			index++;
		}
		
		tourInv = new int[coords.length];
		for(int i = 0;i<t.length;i++) {
			tourInv[t[i]] = i;
		}

		return t;
	}
	
	private static void parseInput() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MIN_VALUE;
		try {
			int nodes = Integer.parseInt(in.readLine());
			coords = new float[nodes][2];
			for(int i = 0;i<nodes;i++) {
				String[] coord = in.readLine().split(" ");
				coords[i][0] = Float.parseFloat(coord[0]);
				coords[i][1] = Float.parseFloat(coord[1]);
				if(coords[i][0] < minX) {
					minX = coords[i][0];
				}
				if(coords[i][0] > maxX) {
					maxX = coords[i][0];
				}
				if(coords[i][1] < minY) {
					minY = coords[i][1];
				}
				if(coords[i][1] > maxY) {
					maxY = coords[i][1];
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void computeDistances() {
		dists = new float[coords.length][coords.length];
		for(int i = 0;i<dists.length;i++) {
			for(int j = 0;j<dists[0].length;j++) {
				float val = (float) (Math.pow(coords[i][0] - coords[j][0], 2) + Math.pow(coords[i][1] - coords[j][1], 2));
				dists[i][j] = val;
				dists[j][i] = val;
			}
		}
	}
	
	private static void computeNearest() {
		nearest = new cityDist[coords.length][coords.length];
		
		for(int i = 0;i<nearest.length;i++) {
			for(int j = 0;j<nearest[0].length;j++) {
				nearest[i][j] = new cityDist(j, dists[i][j]);
			}
		}
		
		for(int i = 0;i<nearest.length;i++) {
			Arrays.sort(nearest[i]);
		}
	}
	
	private static void computeNearestUsingEdges() {
		nearest = new cityDist[coords.length][coords.length-1];
		
		int[] indexes = new int[coords.length];
		
		for(int i = 0;i<edges.length;i++) {
			Edge e = edges[i];
			nearest[e.start][indexes[e.start]] = new cityDist(e.end, e.dist);
			nearest[e.end][indexes[e.end]] = new cityDist(e.start, e.dist);
			
			indexes[e.start]++;
			indexes[e.end]++;
		}
	}
	
	private static void printTrivial() {
		for(int i = 0;i<coords.length;i++) {
			System.out.println(i);
		}
	}
	
	private static class Edge implements Comparable {
		public int start;
		public int end;
		public float dist;
		
		public Edge(int start, int end, float dist) {
			this.start = start;
			this.end = end;
			this.dist = dist;
		}

		@Override
		public int compareTo(Object arg0) {
			return (int) (this.dist - ((Edge) arg0).dist);
		}
		
		
	}
	
	private static class cityDist implements Comparable {
		public int city;
		public float dist;
		
		public cityDist(int city, float dist) {
			this.city = city;
			this.dist = dist;
		}

		@Override
		public int compareTo(Object arg0) {
			return (int) (this.dist - ((cityDist) arg0).dist);
		}
		
		
	}
}
