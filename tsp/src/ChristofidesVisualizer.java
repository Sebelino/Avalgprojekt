import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("serial")
public class ChristofidesVisualizer extends Visualizer{
	private Set<Line2D> mst;
	private Set<Line2D> oddEdges;
	
	public ChristofidesVisualizer(float[][] coords,int frameWidth,int frameHeight){
		super(coords,frameWidth,frameHeight);
		mst = new HashSet<Line2D>();
		oddEdges = new HashSet<Line2D>();
	}

	@Override
	public void drawSolution(Graphics g) {
		super.drawSolution(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setColor(Color.BLUE);

		Iterator<Line2D> it = mst.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.RED);
		it = oddEdges.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setColor(Color.BLACK);
	}

	public void updateMST(Graph mstGraph){
		mst = new HashSet<Line2D>();
		if(mstGraph == null){
			return;
		}
		for(int[] edge : mstGraph.edges()){
			int v = edge[0];
			int w = edge[1];
			Point2D p1 = coords[v];
			Point2D p2 = coords[w];
			mst.add(new Line2D.Float(p1,p2));
		}
	}

	public void updateOddGraph(Graph oddGraph){
		oddEdges = new HashSet<Line2D>();
		if(oddGraph == null){
			return;
		}
		for(int[] e : oddGraph.edges()){
			System.err.println("oddedges="+e[0]+","+e[1]);
		}
		for(int[] edge : oddGraph.edges()){
			int v = edge[0];
			int w = edge[1];
			Point2D p1 = coords[v];
			Point2D p2 = coords[w];
			oddEdges.add(new Line2D.Float(p1,p2));
		}
	}
}
