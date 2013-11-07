import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Pollard extends Factorizer {
	private Random random;
	private static final long POLLARD_LIMIT = 20000L;
	private static final int CERTAINTY = 1000;
	private static final BigInteger BIGGEST = new BigInteger("100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	private static final boolean DEBUG = false;

	private long time;
	private long totalTime;
	private int wrong;

	public Pollard() {
		random = new Random();
		wrong = 0;
	}

	@Override
	public List<BigInteger> factorize(BigInteger number) {
		LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
		number = trySmallPrimes(factors, number);
		if(pollard(factors, number)) return factors;
		return null;
	}

	private boolean pollard(LinkedList<BigInteger> factors, BigInteger number) { // returns true if success
		debug("original number is " + number);


		while(!number.isProbablePrime(CERTAINTY) && number.compareTo(BigInteger.ONE) > 0) {

			BigInteger factor = pollardIter(number);

			if(factor == null) {
				factors.clear();
				return false;
			}

			if(!factor.isProbablePrime(CERTAINTY)) {
				if(!pollard(factors, factor)) {
					return false;
				}
			}
			else {
				factors.add(factor);
			}

			number = number.divide(factor);
			while(number.remainder(factor).compareTo(BigInteger.ZERO) == 0) {
				factors.add(factor);
				number = number.divide(factor);
			}
		}

		if(number.compareTo(BigInteger.ONE) > 0) factors.add(number);
		return true;
	}

	private BigInteger pollardIter(BigInteger number) {
		debug("number is " + number);
		ArrayList<BigInteger> sequence = new ArrayList<BigInteger>();
		BigInteger lastVal = new BigInteger(number.bitLength()-1, random);
		sequence.add(lastVal);

		//for(int i = 1;i < POLLARD_LIMIT;i++) {
		for(int i = 1;true;i++) {
			while(sequence.size() <= 2*i) {
				BigInteger newVal = lastVal.pow(2).add(BigInteger.ONE).remainder(number);
				lastVal = newVal;

				sequence.add(newVal);

			}
			BigInteger factor = number.gcd(sequence.get(2*i).subtract(sequence.get(i)));
			if(factor.compareTo(BigInteger.ONE) > 0 && factor.compareTo(number) < 0) {
				debug("Found factor! " + factor);
				return factor;
			}
		}
		//debug("Found no factor?");
		//return null;
	}

	private BigInteger trySmallPrimes(LinkedList<BigInteger> factors, BigInteger number) {
		long[] primes = {2, 3, 5, 7, 11, 13, 17, 19};
		for(int i = 0;i<primes.length;i++) {

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

	private void debug(String s) {
		if(DEBUG) System.out.println(s);
	}


}
