import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class QuadSieve extends Factorizer {
	private static final double ln2 = Math.log(2);
	
	private int B;
	private BigInteger N;
	private BigInteger sqrtN;
	private int[] lotsOfPrimes;
	
	private static final BigInteger Biggest = new BigInteger("1000000000000000000000000000000");
	
//	public QuadSieve() {}
	
	public QuadSieve(int[] lotsOfPrimes) {
		this.lotsOfPrimes = lotsOfPrimes;
	}

	@Override
	public List<BigInteger> factorize(BigInteger input) {
//		long start = System.nanoTime();
		N = input;
		B = factorBaseLimit()*2;
		if(B < 3000) {
			B = 3000;
		}
//		B = 30;
//		System.out.println("B " + B);
		
		LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
		
		int[] primes = cutOffPrimes();
		
		int[] moduloPrimes = new int[primes.length];
		
		tryPrimesAndGetModulo(primes, factors, moduloPrimes);
		
		if(N.compareTo(BigInteger.ONE) == 0) {
			return factors;
		}
		else if(N.isProbablePrime(100)) {
			factors.add(N);
			return factors;
		}
		
		/*
		 * Kolla perfekt potens här!
		 */
		
		if(input.compareTo(Biggest) > 0) {
//			int a = 1/0;
//			System.out.println("OJ");
			return null;
		}
		
		
		
		sqrtN = Util.sqrt(N).add(BigInteger.ONE);
		
		int[] factorBase = factorBase(primes, moduloPrimes);
		
		int[] logPrimes = getLogPrimes(factorBase);
//		System.out.println(Arrays.toString(factorBase));
		
		int[][] modPSolutions = modPSolutions(factorBase);
		
//		printIntMatrix(modPSolutions);
		
		int limit = (int) Math.pow(factorBase.length, 2)*1;
		if(limit < 100000) {
//			limit = 100000;
		}
		else if(limit > Integer.MAX_VALUE-1) {
			limit = Integer.MAX_VALUE-1;
		}

		ArrayList<BigIntObj> smooths = new ArrayList<BigIntObj>();
		int startX = 0;
		while(smooths.size() <= factorBase.length+3) {
//			if(startX >= limit*2000) {
//				return null;
//			}
			int[] logQs = getLogQs(limit, startX);
			logSieve(factorBase, logPrimes, modPSolutions, logQs, startX);
			smooths = findSmoothsAndFactors(logQs, factorBase, logPrimes[logPrimes.length-1], smooths, startX);
			startX = startX + logQs.length;
//			System.out.println(smooths.size());
			
//			if(smooths.size() <= factorBase.length+3) {
//				return null;
//			}
		}
		
		
//		controlCheckSmooths(smooths, factorBase);
		
//		if(smooths.size() <= factorBase.length+3) { // Give up!
////			if(smooths.size() < 5) {
////				int a = 1/0;
////			}
////			System.out.println("give up");
////			System.out.println("number primes " + factorBase.length);
////			System.out.println(smooths.size());
//			return null; 
//		}
//		else {
////			if(smooths.size() < 5) {
////				int a = 1/0;
////			}
////			return null;
//		}
		
		boolean[][] A = produceBoolTransMatrix(smooths);
		
		UglyGauss gausser = new UglyGauss();
		gausser.gaussJordan(A);

//		System.out.println(factorBase.length);
//		System.out.println(limit);
		int iters = 0;
		while(iters < 10) { // Max iters just nu
			iters++;
			BigInteger[] pair = produceXYPair(gausser, factorBase, smooths);
			BigInteger sum = pair[0].add(pair[1]);
			BigInteger diff = BigInteger.ONE;
			if(pair[0].compareTo(pair[1]) > 0) {
				diff = pair[0].subtract(pair[1]);
			}
			else if(pair[1].compareTo(pair[0]) > 0) {
				diff = pair[1].subtract(pair[0]);
			}
			else {
				return null;
//				System.out.println("FAIL");
			}
			BigInteger sumGCD = sum.gcd(N);
			BigInteger diffGCD = diff.gcd(N);

//			System.out.println("sum: " + sum);
//			System.out.println("diff: " + diff);
//
//			System.out.println("sumgcd: " + sumGCD);
//			System.out.println("diffGCD: " + diffGCD);

			if(sumGCD.compareTo(N) < 0 && sumGCD.compareTo(BigInteger.ONE) > 0) {
				factors.add(sumGCD);
			}
			if(diffGCD.compareTo(N) < 0 && diffGCD.compareTo(BigInteger.ONE) > 0) {
				factors.add(diffGCD);
			}
			if(factors.size() > 0) {
//				System.out.println("TIME: " + (System.nanoTime()-start)/1000000);
				return factors;
			}
			
		}
		
//		System.out.println("Goddamn shit");
		return null;
	}
	
	private int[] cutOffPrimes() {
		int i = 0;
		while(lotsOfPrimes[i] < B) {
			i++;
		}
//		System.out.println(i);
		int[] primes = new int[i];
		
		for(int j = 0;j<primes.length;j++) {
			primes[j] = lotsOfPrimes[j];
		}
		
		return primes;
	}

	private void checkSolution(boolean[] solution, boolean[][] A) {
		boolean[] row = solution;
		
//		boolean[] tempr = A[1];
//		
//		int[] res = new int[row.length];
//		int summa = 0;
//		for(int i = 0;i<row.length;i++) {
//			boolean one = tempr[i] && row[i];
//			if(one) {
//				res[i] = 1;
//				summa++;
//			}
//			
//		}
//		
//		System.out.println("A: " + Arrays.toString(tempr));
//		System.out.println("sol: " + Arrays.toString(row));
//		System.out.println(Arrays.toString(res));
//		System.out.println("SUM " + summa);
		
		int[] result = new int[A.length];
		
		for(int i = 0;i<A.length;i++) {
			int sum = 0;
			for(int j = 0;j<A[0].length;j++) {
				boolean one = (A[i][j] && row[j]);
				if(one) {
					sum = (sum + 1) % 2;
				}
				else {
					sum+= 0;
				}
			}
			result[i] = sum;
		}
		
		System.out.println("IS SOLUTION??");
		System.out.println(Arrays.toString(result));
	}

	private BigInteger[] produceXYPair(UglyGauss gausser, int[] factorBase, ArrayList<BigIntObj> smooths) {
		
		boolean[] solution = gausser.generateSolution();
//		System.out.println(Arrays.toString(solution));
		
//		boolean[][] ACopy = new boolean[A.length][A[0].length];
//		for(int i = 0;i<A.length;i++) {
//			for(int j = 0;j<A[0].length;j++) {
//				ACopy[i][j] = A[i][j];
//			}
//		}
//		checkSolution(solution, ACopy);
		
//		int[] lengths = new int[solution.length];
		
		BigInteger X = BigInteger.ONE;
		BigInteger sqrtX = BigInteger.ONE;
		BigInteger Y = BigInteger.ONE;

		int[] expSums = new int[smooths.get(0).factorExps.length];

		for(int j = 0;j<solution.length;j++) {
			if(solution[j]) {
				for(int k = 0;k<expSums.length;k++) {
					expSums[k] += smooths.get(j).factorExps[k];
				}
				BigInteger xPlusSqrtN = sqrtN.add(BigInteger.valueOf(smooths.get(j).X));
				sqrtX = sqrtX.multiply(xPlusSqrtN);
//				X = X.multiply(xPlusSqrtN.pow(2));
//				Y = Y.multiply(smooths.get(j).Y);
			}
		}

//		for(int k = 0;k<expSums.length;k++) {
//			expSums[k] = expSums[k] % 2;
//		}
//
//		System.out.println("expsums " + Arrays.toString(expSums));
		BigInteger sqrtY = BigInteger.ONE;
		for(int i = 0;i<factorBase.length;i++) {
			if(expSums[i] > 0) {
				sqrtY = sqrtY.multiply(BigInteger.valueOf(factorBase[i]).pow(expSums[i]/2));
			}
		}

//		if(X.mod(N).compareTo(Y.mod(N)) == 0) {
//			System.out.println(X.bitLength());
//			System.out.println(Y.bitLength());
//			System.out.println("Waiting...");
//			BigInteger sqrtY = Util.sqrt(Y);
			//				System.out.println(Y);
			//				System.out.println(sqrtY);
//			if(sqrtX.pow(2).compareTo(X) == 0 && sqrtY.pow(2).compareTo(Y) == 0) {
//				System.out.println("X OK");
//				System.out.println("Y OK");
				BigInteger[] pair = new BigInteger[2];
				pair[0] = sqrtX;
				pair[1] = sqrtY;
				return pair;
//			}
//		}
//		else {
//			System.out.println("FUUUCK");
//		}

		
//		for(int i = 0;i<solution.length;i++) {
//			int sum = 0;
//			for(int j = 0;j<solution[0].length;j++) {
//				if(solution[i][j]) {
//					sum++;
//				}
//			}
//			lengths[i] = sum;
//		}
//		System.out.println(Arrays.toString(lengths));
		
//		return null;
	}

	private boolean[][] produceBoolTransMatrix(ArrayList<BigIntObj> smooths) {
		boolean[][] matrix = new boolean[smooths.get(0).factorExps.length][smooths.size()];
		
		int col = 0;
		for(BigIntObj o : smooths) {
			for(int i = 0;i<o.factorExps.length;i++) {
				matrix[i][col] = (o.factorExps[i] % 2 == 1);
			}
			col++;
		}
		
		return matrix;
	}
	
	private int[][] produceMatrix(LinkedList<BigIntObj> smooths) {
		int[][] matrix = new int[smooths.size()][smooths.getFirst().factorExps.length];
		
		int row = 0;
		for(BigIntObj o : smooths) {
			for(int i = 0;i<o.factorExps.length;i++) {
				matrix[row][i] = o.factorExps[i] % 2;
			}
			row++;
		}
		
		return matrix;
	}

	private void controlCheckSmooths(LinkedList<BigIntObj> smooths, int[] factorBase) {
		System.out.println("CHECK");
		System.out.println();
		int num = 0;
		
		for(BigIntObj o : smooths) {
			BigInteger check = BigInteger.ONE;
//			boolean write = true;
			for(int i = 0;i<o.factorExps.length;i++) {
				check = check.multiply(BigInteger.valueOf(factorBase[i]).pow(o.factorExps[i]));
			}
//			if(write) {
//				System.out.println(o.X);
//				System.out.println(o.Y);
//				for(int i = 0;i<o.factorExps.length;i++) {
//					if(o.factorExps[i] != 0) {
//						System.out.print(factorBase[i] + "^" + o.factorExps[i] + " * ");
//					}
//				}
//				System.out.println();
//			}
//			if(write) num++;
			if(check.compareTo(o.Y) != 0) {
				System.out.println("???????????");
				System.out.println(check);
				System.out.println(o.Y);
				num++;
			}
		}
		
		System.out.println("NUM WITH  " + num);
		
		
	}

	private ArrayList<BigIntObj> findSmoothsAndFactors(int[] qTable, int[] factorBase, int threshold, ArrayList<BigIntObj> smooths, int startX) {
//		ArrayList<BigIntObj> smooths = new ArrayList<BigIntObj>();
		
		int num = 0;
		
		for(int i = 0;i<qTable.length;i++) {
			if(qTable[i] <= threshold) {
				num++;
//				BigInteger X = BigInteger.valueOf(i);
				BigInteger Y = Q(i+startX);
				int[] factorExps = getFactorExps(factorBase, Y);
				
				if(factorExps[0] != -1) {
					smooths.add(new BigIntObj(i+startX, Y, factorExps));
					if(smooths.size() > factorBase.length+3) {
						return smooths;
					}
				}
			}
		}
//		System.out.println("total found " + num);
		return smooths;
		
	}

	public int[] getFactorExps(int[] factorBase, BigInteger y) {
		int[] factorExps = new int[factorBase.length];
		
		BigInteger kvot = y;
		
		int i = 0;
		while(kvot.compareTo(BigInteger.ONE) > 0 && i < factorBase.length) {
			BigInteger base = BigInteger.valueOf(factorBase[i]);
			BigInteger[] dar = null;
			while(dar == null || dar[1].compareTo(BigInteger.ZERO) == 0) {
				dar = kvot.divideAndRemainder(base);
				if(dar[1].compareTo(BigInteger.ZERO) == 0) {
					factorExps[i]++;
					kvot = dar[0];
				}
			}
			
			i++;
		}
		
		if(kvot.compareTo(BigInteger.ONE) != 0) {
			factorExps[0] = -1;
		}
		
		return factorExps;
	}

	private void logSieve(int[] factorBase, int[] logPrimes, int[][] modPSolutions, int[] qTable, int startX) {
//		LinkedList<Integer> smooths = new LinkedList<Integer>();
//		
//		
		
		for(int i = 0;i<modPSolutions[0].length;i++) {
			int logP = logPrimes[i];
			int firstSol = modPSolutions[0][i];
			int secondSol = modPSolutions[1][i];
			
			if(firstSol >= 0) {
				int rest = (startX-firstSol) % factorBase[i];
				if(rest < 0) {
					rest = factorBase[i] + rest;
				}
				int currX = factorBase[i]-rest;
//				if(firstSol > 0){
//					currX = firstSol - (startX % firstSol);
//				}
				
				while(currX < qTable.length) {
					qTable[currX] = qTable[currX] - logP;
					
					currX += factorBase[i];
				}
			}
			if(secondSol >= 0 && secondSol != firstSol) {
				int rest = (startX-secondSol) % factorBase[i];
				if(rest < 0) {
					rest = factorBase[i] + rest;
				}
				int currX = factorBase[i]-rest;
//				if(secondSol > 0) {
//					currX = secondSol - (startX % secondSol);
//				}
				
				while(currX < qTable.length) {
					qTable[currX] = qTable[currX] - logP;
					
					currX += factorBase[i];
				}
			}
		}
		
	}
	
	/*
	 * Divide each entry in qTable, several powers
	 */
	private LinkedList<Integer> basicSievePowers(int[] factorBase, int[][] modPSolutions, BigInteger[] qTable) {
		LinkedList<Integer> smooths = new LinkedList<Integer>();
		
		for(int i = 0;i<modPSolutions[0].length;i++) {
			BigInteger p = BigInteger.valueOf(factorBase[i]);
			int firstSol = modPSolutions[0][i];
			int secondSol = modPSolutions[1][i];
			
			if(firstSol >= 0) {
				int currX = firstSol;
				
				while(currX < qTable.length) {
					while(qTable[currX].mod(p).compareTo(BigInteger.ZERO) == 0) {
						qTable[currX] = qTable[currX].divide(p);
					}
					
					currX += factorBase[i];
				}
			}
			if(secondSol >= 0 && secondSol != firstSol) {
				int currX = secondSol;
				
				while(currX < qTable.length) {
					while(qTable[currX].mod(p).compareTo(BigInteger.ZERO) == 0) {
						qTable[currX] = qTable[currX].divide(p);
					}
					
					currX += factorBase[i];
				}
			}
		}
		
		for(int i = 0;i<qTable.length;i++) {
			if(qTable[i].compareTo(BigInteger.ONE) == 0) {
				smooths.add(i);
			}
		}
		
		return smooths;
	}
	
	/*
	 * Divide each entry in qTable, only first powers
	 */
	private LinkedList<Integer> basicSieve(int[] factorBase, int[][] modPSolutions, BigInteger[] qTable) {
		LinkedList<Integer> smooths = new LinkedList<Integer>();
		
		for(int i = 0;i<modPSolutions[0].length;i++) {
			BigInteger p = BigInteger.valueOf(factorBase[i]);
			int firstSol = modPSolutions[0][i];
			int secondSol = modPSolutions[1][i];
			
			if(firstSol >= 0) {
				int currX = firstSol;
				
				
				
				while(currX < qTable.length) {
					if(currX == 61) {
						System.out.println("prime " + p);
					}
					qTable[currX] = qTable[currX].divide(p);
					currX += factorBase[i];
				}
			}
			if(secondSol >= 0 && secondSol != firstSol) {
				int currX = secondSol;
				
				
				
				while(currX < qTable.length) {
					if(currX == 61) {
						System.out.println("prime " + p);
					}
					qTable[currX] = qTable[currX].divide(p);
					currX += factorBase[i];
				}
			}
		}
		
		for(int i = 0;i<qTable.length;i++) {
			if(qTable[i].compareTo(BigInteger.ONE) == 0) {
				smooths.add(i);
			}
		}
		
		return smooths;
	}
	
	private void tryPrimesAndGetModulo(int[] primes, LinkedList<BigInteger> factors, int[] moduloPrimes) {
		for(int i = 0;i<moduloPrimes.length;i++) {
			BigInteger I = BigInteger.valueOf(primes[i]);
			moduloPrimes[i] = N.mod(I).intValue();
			if(moduloPrimes[i] == 0) { // WTF, hittade faktor, kör igen med nytt N
				N = N.divide(I);
				factors.add(I);
				tryPrimesAndGetModulo(primes, factors, moduloPrimes);
			}
		}
		return;
	}
	
	private int[][] modPSolutions(int[] factorBase) {
		int[][] solutions = new int[2][factorBase.length];
		
		solutions[0][0] = 1-(sqrtN.mod(BigInteger.valueOf(2)).intValue());
		solutions[1][0] = -1;
		
		for(int i = 1;i<solutions[0].length;i++) {
			int firstSol = Util.shanksTonelli(factorBase[i], N);
			int secondSol = factorBase[i]-firstSol;
			int sqrtModP = sqrtN.mod(BigInteger.valueOf(factorBase[i])).intValue();
			firstSol = firstSol - sqrtModP;
			secondSol = secondSol - sqrtModP;
			if(firstSol < 0) {
				firstSol = factorBase[i] + firstSol;
			}
			if(secondSol < 0) {
				secondSol = factorBase[i] + secondSol;
			}
			solutions[0][i] = firstSol;
			solutions[1][i] = secondSol;
		}
		
		return solutions;
	}

	private int[] getLogPrimes(int[] primes) { // not done
		int[] logPrimes = new int[primes.length];
		for(int i = 0;i<logPrimes.length;i++) {
			logPrimes[i] = (int) Math.round(Math.log(primes[i])/ln2);
		}
		return logPrimes;
	}

	public int[] atkinPrimes() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		boolean[] sieve = new boolean[B+1];
		int limitSqrt = (int)Math.sqrt(B);
		sieve[2] = true;
		sieve[3] = true;
		
		for (int x = 1; x <= limitSqrt; x++) {
	        for (int y = 1; y <= limitSqrt; y++) {
	            // first quadratic using m = 12 and r in R1 = {r : 1, 5}
	            int n = (4 * x * x) + (y * y);
	            if (n <= B && (n % 12 == 1 || n % 12 == 5)) {
	                sieve[n] = !sieve[n];
	            }
	            // second quadratic using m = 12 and r in R2 = {r : 7}
	            n = (3 * x * x) + (y * y);
	            if (n <= B && (n % 12 == 7)) {
	                sieve[n] = !sieve[n];
	            }
	            // third quadratic using m = 12 and r in R3 = {r : 11}
	            n = (3 * x * x) - (y * y);
	            if (x > y && n <= B && (n % 12 == 11)) {
	                sieve[n] = !sieve[n];
	            } // end if
	            // note that R1 union R2 union R3 is the set R
	            // R = {r : 1, 5, 7, 11}
	            // which is all values 0 < r < 12 where r is 
	            // a relative prime of 12
	            // Thus all primes become candidates
	        } // end for
	    } // end for
	    // remove all perfect squares since the quadratic
	    // wheel factorization filter removes only some of them
	    for (int n = 5; n <= limitSqrt; n++) {
	        if (sieve[n]) {
	            int x = n * n;
	            for (int i = x; i <= B; i += x) {
	                sieve[i] = false;
	            } // end for
	        } // end if
	    } // end for
	    
	    for(int i = 2;i<sieve.length;i++) {
	    	if(sieve[i]) {
	    		list.add(i);
	    	}
	    }
	    
	    int[] primeArr = new int[list.size()];
		for(int i = 0;i<primeArr.length;i++) {
			primeArr[i] = list.get(i);
		}
		return primeArr;
	}
	
	public int[] eratoPrimes() { // Eratosthenes
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		boolean[] numbers = new boolean[B+1];
		int currPrime = 2;
		while(currPrime <= B) {
			if(numbers[currPrime]) {
				currPrime++;
			}
			else {
				list.add(currPrime);
				for(int i = currPrime;i<=B;i += currPrime) {
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
	
	public int[] primes() { // naive
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
		for(int i = 3;i<B;i += 2) {
			BigInteger I = new BigInteger("" + i);
			if(I.isProbablePrime(100)) {
					list.add(I.intValue());
			}
		}
		
		int[] primeArr = new int[list.size()];
		for(int i = 0;i<primeArr.length;i++) {
			primeArr[i] = list.get(i);
		}
		return primeArr;
	}
	
	public int[] factorBase(int[] primes, int[] moduloPrimes) {
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
//		long startTime = System.nanoTime();
//		System.out.println("oj");
		for(int i = 1;i<primes.length;i++) {			
			int jacobi = Util.jacobi(moduloPrimes[i], primes[i]);
			if(jacobi == 1) {
				list.add(primes[i]);
			}
			
		}
//		System.out.println("hej " + (System.nanoTime()-startTime)/1000000);
		int[] base = new int[list.size()];
		for(int i = 0;i<base.length;i++) {
			base[i] = list.get(i);
		}
		return base;
	}
	
	public int[] getLogQs(int limit, int startX) {
		int[] V = new int[limit];
		
		int cheatLogQ = sqrtN.add(BigInteger.valueOf(limit+startX)).pow(2).subtract(N).bitLength()-1;
		
		for(int i = 0;i<V.length;i++) {
			V[i] = cheatLogQ;
		}
		
		return V;
	}
	
	public BigInteger[] getQs(int limit) {
		BigInteger[] V = new BigInteger[limit];
		
		BigInteger C = sqrtN.pow(2).subtract(N);
		BigInteger D = sqrtN.multiply(BigInteger.valueOf(2));
		
		for(int i = 0;i<V.length;i++) {
			BigInteger I = BigInteger.valueOf(i);
//			V[i] = sqrtN.add(BigInteger.valueOf(i)).pow(2).subtract(N);
			V[i] = I.add(D).multiply(I).add(C);
		}
		
		return V;
	}

	public int factorBaseLimit() {
		return (int) (Math.exp(Math.sqrt(N.bitLength()*Math.log(2)*Math.log(N.bitLength()*Math.log(2)))/2));
	}
	
	public void printIntMatrix(int[][] mat) {
		for(int i = 0;i<mat.length;i++) {
			for(int j = 0;j<mat[0].length;j++) {
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	private class BigIntObj {
		public int X;
		public BigInteger Y;
		public int[] factorExps;
		
		public BigIntObj(int X, BigInteger Y, int[] factorExps) {
			this.X = X;
			this.Y = Y;
			this.factorExps = factorExps;
		}
		
		public String toString() {
			return "" + X;
		}
	}
	
	public BigInteger Q(int x) {
		return sqrtN.add(BigInteger.valueOf(x)).pow(2).subtract(N);
	}
	
	public void printBoolMatrix(boolean[][] mat) {
		for(int i = 0;i<mat.length;i++) {
			System.out.print("{");
			for(int j = 0;j<mat[0].length-1;j++) {
				System.out.print((mat[i][j] ? 1 : 0) + ",");
			}
			System.out.print((mat[i][mat[0].length-1] ? 1 : 0) + "}");
			System.out.println();
		}
	}
	
//	public int factorBaseLimit(BigInteger N) {
//		System.out.println(N.bitLength());
//		return (int) Math.exp(Math.sqrt(N.bitLength()*Math.log(2)*Math.log(N.bitLength()*Math.log(2)))/2);
//	}
}

