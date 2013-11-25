import javax.swing.*;
import java.awt.*;


public class Visualizer extends JPanel{
	int w;
	int h;

	int[] currSol;
	float[][] coords;

	// Create a constructor method
	public Visualizer(float[][] coords, int frameWidth, int frameHeight){
		super();
		w = frameWidth;
		h = frameHeight;
		this.coords = graphCoords(coords);
	}

	public float[][] graphCoords(float[][] coords) {
		float[][] gCoords = new float[coords.length][2];

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
			gCoords[i][0] = 100 + (coords[i][0]-minX)/xInterval*(w-200);
			gCoords[i][1] = 100 + (coords[i][1]-minY)/yInterval*(h-200);
		}
		
		return gCoords;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//		g.drawLine(10,10,150,150); // Draw a line from (10,10) to (150,150)
		drawSolution(g);
	}

	public void drawSolution(Graphics g) {
		if(coords != null) {
			for(int i = 0;i<coords.length;i++) {
				//g.drawRect((int) coords[i][0], (int) coords[i][1], 4, 4);
				g.drawString(""+i,(int) coords[i][0], (int) coords[i][1]);
			}
		}
		if(currSol != null) {
			for(int i = 0;i<currSol.length;i++) {
				g.drawLine((int)coords[i][0],(int)coords[i][1],(int)coords[currSol[i]][0],(int)coords[currSol[i]][1]);
			}
		}
	}

	public void updateSol(int[] currSolution) {
		currSol = currSolution;
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
