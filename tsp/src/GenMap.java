import java.util.Random;

public class GenMap {
	public static void main(String[] args) {
		int numCities = Integer.parseInt(args[0]);
		Random rand = new Random();
		System.err.println(numCities);
		for(int i = 0;i<numCities;i++) {
			double randX = rand.nextDouble()*2000000-1000000;
			double randY = rand.nextDouble()*2000000-1000000;
			System.err.printf("%.4f %.4f", randX, randY);
			System.err.println();
		}
	}
}
