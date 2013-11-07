import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.BitSet;


public class EratoFactorizer extends Factorizer {
	int[] primes;
	//public static final int PRIME_LIMIT =   5000000;
	public static final int PRIME_LIMIT = 300000000;
	//public static final int PRIME_LIMIT = 1000000000;
	public static int NAIVE_TESTING_LIMIT = PRIME_LIMIT;
	
	public EratoFactorizer () {
		primes = eratoPrimes(PRIME_LIMIT);
	}

	@Override
	public List<BigInteger> factorize(BigInteger input) {
		//if(input.isProbablePrime(100)) { // Avkommentera för bättre performance för primtal.
		//	List<BigInteger> list = new ArrayList<BigInteger>();
        //    list.add(input);
        //    return list;
		//}
		LinkedList<BigInteger> factors = new LinkedList<BigInteger>();

		BigInteger number = trySmallPrimes(factors, input); // Naive testing
		if(number.compareTo(BigInteger.ONE) == 0) {
			return factors;
		}
		else if(number.isProbablePrime(100)) {
			factors.add(number);
			return factors;
		}
		return null;
	}

	public int[] eratoPrimes(int limit) { // Eratosthenes
		ArrayList<Integer> list = new ArrayList<Integer>();

		BitSet numbers = new BitSet(limit+1);
		int currPrime = 2;
		while(currPrime <= limit) {
			if(numbers.get(currPrime)) {
				currPrime++;
			}
			else {
				list.add(currPrime);
				for(int i = currPrime;i<=limit;i += currPrime) {
					numbers.set(i,true);
				}
			}
		}

		int[] primeArr = new int[list.size()];
		for(int i = 0;i<primeArr.length;i++) {
			primeArr[i] = list.get(i);
		}
		return primeArr;
	}
	
	private BigInteger trySmallPrimes(LinkedList<BigInteger> factors, BigInteger number) {
		for(int i = 0;i<NAIVE_TESTING_LIMIT;i++) {
			if(i>=primes.length || number.compareTo(BigInteger.ONE) == 0) {
				break;
			}
			while(true) {
				BigInteger[] dAr = number.divideAndRemainder(BigInteger.valueOf(primes[i]));
				if(dAr[1].compareTo(BigInteger.ZERO) != 0) {
					break;
				}
				number = dAr[0];
				factors.add(BigInteger.valueOf(primes[i]));
			}
		}

		return number;
	}
	
}
