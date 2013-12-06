import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Permutations implements Iterable<List<Integer>>{
	private List<Integer> elements;
	private final int limit = 1000;
	private int counter;

	public Permutations(int[] array){
		elements = new ArrayList<Integer>();
		for(int e : array){
			elements.add(e);
		}
		Collections.sort(elements);
		counter = 0;
	}

	@Override
	public Iterator<List<Integer>> iterator(){
		Iterator<List<Integer>> it = new Iterator<List<Integer>>(){
			private List<Integer> current = null;
			@Override
			public boolean hasNext() {
				if(current == null){
					return true;
				}
				//for(int i = 0;i < current.size()-1;i++){
				//	if(current.get(i) < current.get(i+1)){
				//		return true;
				//	}
				//}
				if(counter < limit){
					return true;
				}
				return false;
			}
			@Override
			public List<Integer> next() {
				//permutate();
				randomize();
				counter++;
				return current;
			}
			@Override
			public void remove() {
				throw new RuntimeException("Not implemented.");
			}
			private void randomize(){
				if(current == null){
					current = elements;
				}
				long seed = System.nanoTime();
				Collections.shuffle(current,new Random(seed));
			}

			@SuppressWarnings("unused")
			private void permutate(){
				if(current == null){
					current = elements;
					return;
				}
				permutate(current.size()-1);
			}
			private void permutate(final int index){
				if(index < 0){
					throw new RuntimeException("Ran out of elements.");
				}
				final int pivot = current.get(index);
				int smallestGreaterIndex = -1;
				int smallestGreater = Integer.MAX_VALUE;
				for(int i = index+1;i < current.size();i++){
					if(pivot < current.get(i)
							&& current.get(i) < smallestGreater){
						smallestGreaterIndex = i;
						smallestGreater = current.get(i);
					}
				}
				if(smallestGreaterIndex == -1){
					permutate(index-1);
					return;
				}
				Collections.swap(current,index,smallestGreaterIndex);
				sort(index+1,current.size());
			}
			private void sort(final int from,final int to){
				List<Integer> postfix = current.subList(from,to);
				current = current.subList(0,from);
				Collections.sort(postfix);
				current.addAll(postfix);
			}

			@SuppressWarnings("unused")
			private BigInteger factorial(int n) {
				BigInteger fac = BigInteger.ONE;
				while(n > 0){
					fac = fac.multiply(new BigInteger(""+n));
					n--;
				}
				return fac;
			}
		};
		return it;
	}
}
