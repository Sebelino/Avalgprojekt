import javax.swing.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class Visualizer extends JPanel{
	protected final int w; /* Window width */
	protected final int h; /* Window height */
	protected final Point[] coords; // :: VertexName -> Point
	protected final List<Set<Line2D>> solutions;

	// Create a constructor method
	public Visualizer(float[][] coords, int frameWidth, int frameHeight){
		super();
		w = frameWidth;
		h = frameHeight;
		this.coords = graphCoords(coords);
		solutions = new ArrayList<Set<Line2D>>();
	}

	protected Point[] graphCoords(float[][] coords) {
		Point[] gCoords = new Point[coords.length];

		float minX = Float.MAX_VALUE;
		float maxX = -Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MIN_VALUE;

		for(int i = 0;i<coords.length;i++) {
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
		
		float xInterval = maxX - minX;
		float yInterval = maxY - minY;
		
		for(int i = 0;i<coords.length;i++) {
			gCoords[i] = new Point(
				(int)(100+(coords[i][0]-minX)/xInterval*(w-200)),
		        (int)(100+(coords[i][1]-minY)/yInterval*(h-200))
			);
		}
		
		return gCoords;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//		g.drawLine(10,10,150,150); // Draw a line from (10,10) to (150,150)
		drawSolution(g);
	}

	public void drawSolution(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		if(coords != null) {
			for(int i = 0;i<coords.length;i++) {
				g2d.drawString(""+i,(int) coords[i].x+5, (int) coords[i].y+5);
			}
		}
		g2d.setColor(Color.GREEN);
		if(!solutions.isEmpty()) {
			Set<Line2D> currentSolution = solutions.get(solutions.size()-1);
			Iterator<Line2D> it = currentSolution.iterator();
			System.err.println("sol");
			while(it.hasNext()) {
				Line2D line = it.next();
				g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
				System.err.println((int)line.getX1()+","+(int)line.getY1()+","+(int)line.getX2()+","+(int)line.getY2());
			}
		}
		g2d.setColor(Color.BLACK);
	}

	public void updateSol(int[] currSolution) {
		Set<Line2D> newSolution = new HashSet<Line2D>();
		for(int v = 0;v < currSolution.length;v++){
			int w = currSolution[v];
			Point p1 = coords[v];
			Point p2 = coords[w];
			newSolution.add(new Line2D.Float(p1,p2));
		}
		solutions.add(newSolution);
	}

	//	public static void main(String arg[]){
	//		JFrame frame = new JFrame("BasicPanel");
	//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//		frame.setSize(200,200);
	//
	//		Visualizer panel = new Visualizer();
	//		frame.setContentPane(panel);          
	//		frame.setVisible(true);                   
	//	}
}
