import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class Main {
	private static Random random;
	private static final int POLLARD_LIMIT = 10000;
	private static final boolean DEBUG = false;
	
	public static void main(String[] arg) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		LinkedList<BigInteger> list = new LinkedList<BigInteger>();
		
		random = new Random();
		
		while(br.ready()) {
			list.add(new BigInteger(br.readLine()));
		}
		
		br.close();
		
		for(BigInteger number : list) {
			LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
			pollard(factors, number);
			if(factors.isEmpty()) {
				System.out.println("fail");
			}
			else {
				for(BigInteger factor : factors) {
					System.out.println(factor);
				}
			}
			System.out.println();
		}
	}

	private static void pollard(LinkedList<BigInteger> factors, BigInteger number) {
//		System.out.println("original number is " + number);
		
		while(!number.isProbablePrime(100) && number.subtract(BigInteger.ONE).longValue() > 1) {
			BigInteger factor = pollardIter(number);
			if(factor == null) {
				factors.clear();
				return;
			}
			if(!factor.isProbablePrime(100)) {
				pollard(factors, factor);
			}
			else {
				factors.add(factor);
			}
			number = number.divide(factor);
			while(number.remainder(factor).longValue() == 0) {
				factors.add(factor);
				number = number.divide(factor);
			}
		}
		
		if(number.subtract(BigInteger.ONE).longValue() > 1) factors.add(number);
		return;
	}
	
	private static BigInteger pollardIter(BigInteger number) {
		debug("number is " + number);
		ArrayList<BigInteger> sequence = new ArrayList<BigInteger>();
		long longValue = number.longValue();
		long lastVal = (long) (random.nextDouble()*longValue);
		sequence.add(new BigInteger(Long.toString(lastVal)));
		debug("random is " + lastVal);
		
		for(int i = 1;i < POLLARD_LIMIT;i++) {
			while(sequence.size() <= 2*i) {
				debug("" + ((long) Math.pow(lastVal, 2)+1));
				debug("" + ((long) (Math.pow(lastVal, 2)+1) % longValue));
				lastVal =  ((long)(Math.pow(lastVal, 2)+1) % longValue);
				debug("lastVal is " + lastVal);
				
				sequence.add(new BigInteger(Long.toString(lastVal)));
				
			}
			debug("sequence value i " + i + " : " + sequence.get(i));
			debug("sequence value 2i " + 2*i + " : " + sequence.get(2*i));
			BigInteger factor = number.gcd(sequence.get(2*i).subtract(sequence.get(i)));
			debug("" + factor);
			if(factor.subtract(BigInteger.ONE).longValue() > 1 && factor.subtract(number).longValue() != 0) {
				debug("Found factor! " + factor);
				return factor;
			}
		}
		debug("Found no factor?");
		return null;
	}
	
	private static void debug(String s) {
		if(DEBUG) System.out.println(s);
	}
}
