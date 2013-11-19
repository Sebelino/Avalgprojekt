import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;


public class SA {
	private static final int START_T = 50;
	private static final int INNER_ITERS = 50;
	private static final boolean VISUALIZE = false;
	
	private static double constant; // determined in parseInput
	
	private static float[][] coords;
	private static float[][] dists;
	
	private static Random rand;
	private static Visualizer vis;
	
	public static void main(String[] args) {
		
		rand = new Random();
		
		parseInput();
		computeDistances();
		if(coords.length < 5) {
			System.out.println("FUCK");
			System.exit(0);
		}
		if(VISUALIZE) initGraphics();
		
		int[][] currSol = generateRandomSolution();
//		printIntMatrix(currSol);
//		System.out.println("SOLUTION:");
//		printSol(currSol);
		int t = START_T;
		float currVal = evalStartSol(currSol);
//		System.out.println("STARTVAL " + currVal);
//		constant = 1.0/10000;
		while(t > 0) {
			for(int i = 0;i<INNER_ITERS;i++) {
				Action action = generateNeighbor(currSol);
//				System.err.println("Solution: ");
//				printSol(currSol);
				float newVal = evalSol(currSol, action, currVal);
				if(acceptSol(currVal, newVal, t)) {
					currSol = applyAction(currSol, action);
					currVal = newVal;
				}
			}
//			if(t % 20 == 0) {
//				updateVisualization(currSol);
//			}
			t--;
		}
//		System.err.println("Final solution:");
		updateVisualization(currSol);
//		printIntMatrix(currSol);
		printSol(currSol);
		
//		System.err.println("EndVal: " + currVal);
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

	private static void printAction(Action a) {
		
	}
	
	private static void printSol(int[][] sol) {
		System.out.println(0);
		int curr = sol[0][0];
		while(curr != 0) {
//			System.err.println("OJ");
			System.out.println(curr);
			curr = sol[curr][0];
		}
		
	}

	private static float evalStartSol(int[][] sol) {
		float sum = 0;
		int curr = sol[0][0];
		while(curr != 0) {
			sum += dists[curr][sol[curr][0]];
			curr = sol[curr][0];
		}
		return sum;
	}

	private static int[][] applyAction(int[][] currSol, Action action) {
		for(Edge e : action.removals) { // Ta bort alla gamla kanter
			if(currSol[e.start][0] == e.end) {
				currSol[e.start][0] = -1;
				currSol[e.end][1] = -1;
			}
			else {
				currSol[e.start][1] = -1;
				currSol[e.end][0] = -1;
			}
		}
		
		for(Edge e : action.additions) { // Lägg till alla nya kanter
			if(currSol[e.start][0] == -1) {
				currSol[e.start][0] = e.end;
				currSol[e.end][1] = e.start;
			}
			else {
				currSol[e.start][1] = e.end;
				currSol[e.end][0] = e.start;
			}
		}
		
		return currSol;
	}

	private static boolean acceptSol(float currVal, float newVal, int t) {
		
		if(newVal < currVal) {
//			System.err.println("LUCKY");
			return true;
		}
		else {
			double prob = Math.exp(constant*(currVal - newVal)/((double) t/START_T));
//			System.err.println("delta: " + (currVal - newVal) + ", t: " + t + " t/start_T: " + (float) t/START_T+ " prob: " + (int) (prob*1000));
			if(rand.nextDouble() < prob) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	private static float evalSol(int[][] currSol, Action action, float currVal) {
		float remSum = 0;
		float addSum = 0;
		
		for(Edge e : action.removals) {
			remSum += dists[e.start][e.end];
		}
		
		for(Edge e : action.additions) {
			addSum += dists[e.start][e.end];
		}
		
		return currVal - remSum + addSum;
	}

	private static Action generateNeighbor(int[][] currSol) {
		LinkedList<Edge> removals = new LinkedList<Edge>();
		LinkedList<Edge> additions = new LinkedList<Edge>();
		
		int randStart = rand.nextInt(coords.length);
		int randEnd = rand.nextInt(coords.length);
		while(randEnd == randStart || randEnd == currSol[randStart][0] || randEnd == currSol[randStart][1]) { // Bara fortsätt randomiza, liten chans att få samma
			randEnd = rand.nextInt(coords.length);
		}
//		System.err.println("start: " + randStart);
//		System.err.println("end: " + randEnd);
		if(rand.nextDouble() < 0.5) { // currSol[randStart][0] will be changed
//			System.out.println("start[0]: " + currSol[randStart][0]);
			removals.add(new Edge(randStart, currSol[randStart][0]));
			removals.add(new Edge(randEnd, currSol[randEnd][0]));
			removals.add(new Edge(randEnd, currSol[randEnd][1]));
			
			additions.add(new Edge(randStart, randEnd));
			additions.add(new Edge(randEnd, currSol[randStart][0]));
			additions.add(new Edge(currSol[randEnd][0], currSol[randEnd][1]));
		}
		else { // currSol[randStart][1] will be changed
//			System.out.println("start[1]: " + currSol[randStart][1]);
			removals.add(new Edge(randStart, currSol[randStart][1]));
			removals.add(new Edge(randEnd, currSol[randEnd][0]));
			removals.add(new Edge(randEnd, currSol[randEnd][1]));
			
			additions.add(new Edge(randStart, randEnd));
			additions.add(new Edge(randEnd, currSol[randStart][1]));
			additions.add(new Edge(currSol[randEnd][0], currSol[randEnd][1]));
		}
				
		return new Action(removals, additions);
	}

	private static int[][] generateRandomSolution() {
		int[][] sol = new int[coords.length][2];
		
		ArrayList<Integer> list = new ArrayList<Integer>(coords.length);
		for(int i = 0;i<coords.length;i++) {
			list.add(i);
		}
		
		int prev = list.remove(rand.nextInt(list.size()));
		int start = prev;
		while(!list.isEmpty()) {
			int next = list.remove(rand.nextInt(list.size()));
			sol[prev][0] = next;
			sol[next][1] = prev;
			prev = next;
		}
		sol[prev][0] = start;
		sol[start][1] = prev;
		
		return sol;
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
		double delta = Math.max(maxX-minX, maxY-minY);
		constant = 1.0/Math.pow(delta, 2);
//		System.err.println("Constant: " + constant);
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

	private static void printTrivial() {
		for(int i = 0;i<coords.length;i++) {
			System.out.println(i);
		}
	}
	
	private static void printIntMatrix(int[][] mat) {
		for(int i = 0;i<mat.length;i++) {
			for(int j= 0;j<mat[0].length;j++) {
				System.err.print(mat[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	private static class Action {
		public LinkedList<Edge> removals;
		public LinkedList<Edge> additions;
		
		public Action(LinkedList<Edge> removals, LinkedList<Edge> additions) {
			this.removals = removals;
			this.additions = additions;
		}
	}
	
	public static class Edge{
		public int start;
		public int end;
		
		public Edge(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
}
