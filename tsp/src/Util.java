public class Util {
	/** @return {0,...,n-1} */
	public static int[] ring(int n){
		int[] ring = new int[n];
		for(int i = 0;i < n;i++){
			ring[i] = i;
		}
		return ring;
	}
}
