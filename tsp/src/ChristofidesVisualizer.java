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
	private Set<Line2D> matching;
	private Set<Line2D> multigraph;
	private Set<Line2D> hamilton;
	
	public ChristofidesVisualizer(float[][] coords,int frameWidth,int frameHeight){
		super(coords,frameWidth,frameHeight);
		mst = new HashSet<Line2D>();
		oddEdges = new HashSet<Line2D>();
		matching = new HashSet<Line2D>();
		multigraph = new HashSet<Line2D>();
		hamilton = new HashSet<Line2D>();
	}

	@Override
	public void drawSolution(Graphics g) {
		super.drawSolution(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setColor(Color.BLUE);

		Iterator<Line2D> it = mst.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.ORANGE);
		it = oddEdges.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setStroke(new BasicStroke(7,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.RED);
		it = matching.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.decode("#339900"));
		it = multigraph.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.setColor(Color.BLACK);
		it = hamilton.iterator();
		while(it.hasNext()) {
			Line2D line = it.next();
			g2d.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
		}
		//g2d.setColor(Color.BLACK);
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

	public void updateMatchGraph(Graph matchingGraph){
		matching = new HashSet<Line2D>();
		if(matchingGraph == null){
			return;
		}
		for(int[] e : matchingGraph.edges()){
			System.err.println("matchedges="+e[0]+","+e[1]);
		}
		for(int[] edge : matchingGraph.edges()){
			int v = edge[0];
			int w = edge[1];
			Point2D p1 = coords[v];
			Point2D p2 = coords[w];
			matching.add(new Line2D.Float(p1,p2));
		}
	}

	public void updateMultiGraph(Graph mGraph){
		multigraph = new HashSet<Line2D>();
		if(mGraph == null){
			return;
		}
		for(int[] e : mGraph.edges()){
			System.err.println("multiedges="+e[0]+","+e[1]);
		}
		for(int[] edge : mGraph.edges()){
			int v = edge[0];
			int w = edge[1];
			Point2D p1 = coords[v];
			Point2D p2 = coords[w];
			multigraph.add(new Line2D.Float(p1,p2));
		}
	}

	public void updateHamiltonGraph(Graph hamiltonGraph){
		hamilton = new HashSet<Line2D>();
		if(hamiltonGraph == null){
			return;
		}
		for(int[] e : hamiltonGraph.edges()){
			System.err.println("hamiltonedges="+e[0]+","+e[1]);
		}
		for(int[] edge : hamiltonGraph.edges()){
			int v = edge[0];
			int w = edge[1];
			Point2D p1 = coords[v];
			Point2D p2 = coords[w];
			hamilton.add(new Line2D.Float(p1,p2));
		}
	}
}
