import java.io.BufferedReader;import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Util{

    public static List<BigInteger> read(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<BigInteger> input = new ArrayList<BigInteger>();
        try{
            while(br.ready()){
                String line = br.readLine();
                if(line.length() > 0){
                    input.add(new BigInteger(line));
                }
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return input;
    }

    public static BigInteger product(List<BigInteger> factors){
        BigInteger product = new BigInteger("1");
        for(BigInteger f : factors){
            product = product.multiply(f);
        }
        return product;
    }

    public static void printFactorization(List<BigInteger> factors){
        if(factors == null || factors.isEmpty()){
            System.out.println("fail");
        }else{
            for(BigInteger f : factors){
                System.out.println(f);
            }
        }
        System.out.println();
    }

    public static int log2(BigInteger n) { // gör bättre genom att använda bigdecimal och avrunda
    	BigInteger two = BigInteger.valueOf(2);
        int a = 0; // lower
        int b = n.bitLength()+2; // upper
//        long startTime = System.nanoTime();
        while(b >= a) {
            int mid = (a+b)/2; // (a+b)/2
            if(two.pow(mid).compareTo(n) > 0){ 
            	b = mid - 1;
            }
            else{
            	a = mid + 1;
            }
        }
//        System.out.println((System.nanoTime() - startTime)/(double) 1000000000);
        return a - 1;
    }
    
    public static BigInteger kSqrt(BigInteger n, int k) {
        BigInteger a = BigInteger.ONE; // lower
        BigInteger b = n.add(BigInteger.ZERO); // upper
//        long startTime = System.nanoTime();
        while(b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1); // (a+b)/2
            if(mid.pow(k).compareTo(n) > 0){ 
            	b = mid.subtract(BigInteger.ONE);
            }
            else{
            	a = mid.add(BigInteger.ONE);
            }
        }
//        System.out.println((System.nanoTime() - startTime)/(double) 1000000000);
        return a.subtract(BigInteger.ONE);
    }
    
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = n.shiftRight(n.bitLength()/2+2); // lower
        BigInteger b = n.shiftRight(n.bitLength()/2-2); // upper
//        long startTime = System.nanoTime();
        while(b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1); // (a+b)/2
            if(mid.multiply(mid).compareTo(n) > 0){ 
            	b = mid.subtract(BigInteger.ONE);
            }
            else{
            	a = mid.add(BigInteger.ONE);
            }
        }
//        System.out.println((System.nanoTime() - startTime)/(double) 1000000000);
        return a.subtract(BigInteger.ONE);
    }
    
    /*
     * Only works for odd primes
     * n legendre p must be 1
     * Second solution given by p - return
     */
    public static int shanksTonelli(int p, BigInteger n) { // see wikipedia for algorithm
    	BigInteger P = BigInteger.valueOf(p);
    	
    	// step 1
    	int Q = p-1;
    	int S = 0;
    	while(Q % 2 == 0) {
    		Q = Q/2;
    		S++;
    	}
    	if(S == 1) {
    		return n.modPow(BigInteger.valueOf((p+1)/4), P).intValue();
    	}
    	
    	//step 2
    	int z = 2;
    	while(Util.jacobi(z, p) != -1) {
    		z += 2;
    	}
    	BigInteger c = BigInteger.valueOf(z).modPow(BigInteger.valueOf(Q), P);
    	
    	//step 3
    	long R = n.modPow(BigInteger.valueOf((Q+1)/2), P).longValue();
    	long t = n.modPow(BigInteger.valueOf(Q), P).longValue();
    	int M = S;
//    	System.out.println("c " + c);
//    	System.out.println("startT " + t);
    	
    	//step 4
    	while(true) {
    		
    		// step 4.1
    		if(t == 1) {
    			return (int) R;
    		}
    		
    		// step 4.2
    		int i = 1;
    		long tempT = t;
    		while(i < M && tempT != 1) {
    			tempT = (tempT*tempT) % p;
//    			System.out.println("tempT " + tempT);
    			if(tempT != 1) {
    				i++;
    			}
    		}
    		
    		
//    		System.out.println("i " + i);
//    		System.out.println("M " + M);
    		
    		// step 4.3
    		int exp = (int) Math.pow(2, M-i-1);
    		long b = c.modPow(BigInteger.valueOf(exp), P).longValue();
    		R = (R*b) % p;
    		t = (t*b*b) % p;
    		c = BigInteger.valueOf(b*b).mod(P);
//    		System.out.println("b " + b);
//    		System.out.println("t " + t);
//    		System.out.println("c " + c);
    		
    		if(i == M) return -1;
    		M = i;
    		
    		
    	}
    }
    
    public static int jacobi(int a, int n) { // see wikipedia jacobi symbol
    	int reduced = a % n; // step 1
    	
    	int numTwos = 0; // step 2
    	while(reduced % 2 == 0) {
        	numTwos++;
        	reduced = reduced/2;
    	}
    	
    	if(BigInteger.valueOf(reduced).gcd(BigInteger.valueOf(n)).compareTo(BigInteger.ONE) != 0) { // step 3
    		return 0;
    	}
    	
    	int jacobiFromTwos = 1; // evaluation of step 2
    	if(numTwos % 2 == 1) {
    		int modEight = n % 8;
        	if(modEight == 3 || modEight == 5) {
        		jacobiFromTwos = -1;
        	}
    	}
    	
    	if(reduced == 1) { // also step 3
    		return jacobiFromTwos;
    	}
    	
    	if(n % 4 == 1 ||reduced % 4 == 1) { // step 4
    		return jacobiFromTwos*jacobi(n, reduced);
    	}
    	else {
    		return jacobiFromTwos*(-jacobi(n, reduced));
    	}
    }
    
    public static int jacobi(BigInteger a, BigInteger n) { // see wikipedia jacobi symbol
    	BigInteger reduced = a.mod(n); // step 1
    	
    	BigInteger[] dar = new BigInteger[2]; // step 2
    	int numTwos = 0;
//    	System.out.println("ey");
//    	System.out.println(reduced);
//    	System.out.println(reduced.testBit(reduced.bitCount()-1));
//    	System.out.println(reduced.bitCount());
//    	while(!reduced.testBit(reduced.bitLength()-1)) {
    	while(dar[1] == null || dar[1].compareTo(BigInteger.ZERO) == 0) {
    		dar = reduced.divideAndRemainder(new BigInteger("2"));
        	if(dar[1].compareTo(BigInteger.ZERO) == 0) { // divisible by 2
        		numTwos++;
        		reduced = dar[0];
        	}
    	}
    	
    	if(reduced.gcd(n).compareTo(BigInteger.ONE) != 0) { // step 3
    		return 0;
    	}
    	
    	int jacobiFromTwos = 1; // evaluation of step 2
    	if(numTwos % 2 == 1) {
    		int modEight = n.mod(new BigInteger("8")).intValue();
        	if(modEight == 3 || modEight == 5) {
        		jacobiFromTwos = -1;
        	}
    	}
    	
    	if(reduced.compareTo(BigInteger.ONE) == 0) { // also step 3
    		return jacobiFromTwos;
    	}
    	
    	int mModFour = n.mod(new BigInteger("4")).intValue(); // step 4
    	int nModFour = reduced.mod(new BigInteger("4")).intValue();
    	if(nModFour == 1 || mModFour == 1) {
    		return jacobiFromTwos*jacobi(n, reduced);
    	}
    	else {
    		return jacobiFromTwos*(-jacobi(n, reduced));
    	}
    }

    @SuppressWarnings("rawtypes")
    public static Factorizer getFactorizer(String[] args){
        if(args.length == 1){
            String token = args[0];
            Class c = null;
            try{
                c = Class.forName(token);
            }catch(ClassNotFoundException e){
                System.err.println("Class "+token+" not found. Running default algorithm.");
                return null;
            }
            try{
                return (Factorizer)c.newInstance();
            }catch(InstantiationException e){
                System.err.println("The class could not be instantiated.");
            }catch(IllegalAccessException e){
                System.err.println("Illegal access.");
            }
        }
        return null;
    }
}

        BigInteger product = new BigInteger("1");
        for(BigInteger f : factors){
            product = product.multiply(f);
        }
        return product;
    }

    public static void printFactorization(List<BigInteger> factors){
        if(factors == null || factors.isEmpty()){
            System.out.println("fail");
        }else{
            for(BigInteger f : factors){
                System.out.println(f);
            }
        }
        System.out.println();
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if(mid.multiply(mid).compareTo(n) > 0){ b = mid.subtract(BigInteger.ONE); }
            else{ a = mid.add(BigInteger.ONE); }
        }
        return a.subtract(BigInteger.ONE);
    }

    @SuppressWarnings("rawtypes")
    public static Factorizer getFactorizer(String[] args){
        if(args.length == 1){
            String token = args[0];
            Class c = null;
            try{
                c = Class.forName(token);
            }catch(ClassNotFoundException e){
                System.err.println("Class "+token+" not found. Running default algorithm.");
                return null;
            }
            try{
                return (Factorizer)c.newInstance();
            }catch(InstantiationException e){
                System.err.println("The class could not be instantiated.");
            }catch(IllegalAccessException e){
                System.err.println("Illegal access.");
            }
        }
        return null;
    }
}
