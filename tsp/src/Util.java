import java.util.ArrayList;
import java.util.List;

public class Util {
	/** @return {0,...,n-1} */
	public static int[] ring(int n){
		int[] ring = new int[n];
		for(int i = 0;i < n;i++){
			ring[i] = i;
		}
		return ring;
	}
	
	/** Prints the array. */
	public static void printArray(int[] array){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i < array.length;i++){
			list.add(array[i]);
		}
		System.err.println(list);
	}

	/** Prints the matrix */
	public static void printMatrix(int[][] matrix){
		String str = "";
		for(int y = 0;y < matrix.length;y++){
			for(int x = 0;x < matrix[y].length;x++){
				str += matrix[y][x]+"\t";
			}
			str += "\n";
		}
		System.err.println(str);
	}
}
