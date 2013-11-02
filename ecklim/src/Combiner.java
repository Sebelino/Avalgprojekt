import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Combiner extends Factorizer {
	public static final int PRIME_LIMIT = 30000;
	public static int NAIVE_TESTING_LIMIT = 1000;
	public static BigInteger POLLARD_LIMIT = new BigInteger("1000000000000000");
	public static BigInteger BIGGEST = new BigInteger("1000000000000000000000000000000");
	public static final int POTENS_LIMIT = 11;

	private int[] primes;

	public Combiner() {
		primes = eratoPrimes(PRIME_LIMIT);
//		System.out.println(primes[primes.length-1]);
//		System.out.println(primes.length);
	}

	@Override
	public List<BigInteger> factorize(BigInteger input) {
		
		LinkedList<BigInteger> factors = new LinkedList<BigInteger>();

		BigInteger number = trySmallPrimes(factors, input); // Naive testing

		LinkedList<BigInteger> queue = new LinkedList<BigInteger>();
		queue.add(number);
		
		while(!queue.isEmpty()) {
			BigInteger curr = queue.removeLast();
			
			//prime?
			if(curr.isProbablePrime(100)) {
				factors.add(curr);
			}
			else {
				
				// Pollard
				if(curr.compareTo(POLLARD_LIMIT) < 0) { 
					Pollard pollard = new Pollard();
					List<BigInteger> pollardFactors = pollard.factorize(curr);

					for(BigInteger factor : pollardFactors) {
						factors.add(factor);
					}
				}
				else {
					// Perfekt potens?
					BigInteger krot = tryPerfectPotens(curr, POTENS_LIMIT, factors);
					if(krot != null) {
						int exp = 0;
						while(curr.mod(krot).compareTo(BigInteger.ZERO) == 0) {
							curr = curr.divide(krot);
							exp++;
						}
						if(krot.isProbablePrime(100)) {
							for(int i = 0;i<exp;i++) {
								factors.add(krot);
							}
						}
						else {
							for(int i = 0;i<exp;i++) {
								queue.add(krot);
							}
						}
						if(curr.isProbablePrime(100)) {
							factors.add(curr);
						}
						else {
							queue.add(curr);
						}
					}
					else {
						if(curr.compareTo(BIGGEST) > 0) {
//							System.out.println(curr);
							return null;
						}
						
						// Quadratic Sieve Time!!!!
						QuadSieve qs = new QuadSieve(primes);
//						long start = System.nanoTime();
						List<BigInteger> qsFactors = qs.factorize(curr);
//						System.out.println("TIME: " + (System.nanoTime()-start)/1000000);
						if(qsFactors == null) {
							return null;
						}
						for(BigInteger factor : qsFactors) {
							if(factor.isProbablePrime(100)) {
								factors.add(factor);
							}
							else {
								queue.add(factor);
							}
						}
						
					}

				}
			}


		}

		return factors;
	}

	private BigInteger tryPerfectPotens(BigInteger curr, int potensLimit, LinkedList<BigInteger> factors) {
		for(int i = 2;i<=POTENS_LIMIT;i++) {
			BigInteger krot = Util.kSqrt(curr, i);
			if(krot.pow(i).compareTo(curr) == 0) { // Den hade en k-rot!!
				return krot;
			}
		}
		return null;
	}

	public int[] eratoPrimes(int limit) { // Eratosthenes
		ArrayList<Integer> list = new ArrayList<Integer>();

		boolean[] numbers = new boolean[limit+1];
		int currPrime = 2;
		while(currPrime <= limit) {
			if(numbers[currPrime]) {
				currPrime++;
			}
			else {
				list.add(currPrime);
				for(int i = currPrime;i<=limit;i += currPrime) {
					numbers[i] = true;
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

