import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

public class Tester{
    public Tester(String[] args){ }

    public void runTests(){
        List<List<Result>> resultLists = benchmark();
        for(List<Result> results : resultLists){
            System.out.println("Results");
            for(Result result : results){
                System.out.println(result.toStringNumberTime());
            }
        }
    }

    private List<List<Result>> benchmark(){
        String[] factorizerNames = new String[]{"Naive","EratoFactorizer","Pollard","Combiner"};
        List<Factorizer> factorizers = new ArrayList<Factorizer>();
        for(String name : factorizerNames){
            try{
                factorizers.add(Util.getFactorizer(new String[]{name}));
            }catch(ClassNotFoundException e){
                System.err.println("Class not found: "+name);
            }
        }
        /* Get numbers to test on */
        List<BigInteger> numbers = nFactorProducts(3,60);
        /* Get the raw results */
        List<List<Result>> resultLists = new ArrayList<List<Result>>();
        for(Factorizer factorizer : factorizers){
            List<Result> results = factorizationResults(numbers,factorizer);
            resultLists.add(results);
        }
        /* Process the results */
        List<List<Result>> processedResultLists = new ArrayList<List<Result>>();
        for(List<Result> results : resultLists){
            List<Result> processedResults = new ArrayList<Result>();
            final Iterator<Result> it = results.iterator();
            while(it.hasNext()){
                Result r = it.next();
                if(r.succeeded){
                    processedResults.add(r);
                }
            }
            processedResultLists.add(processedResults);
        }
        return processedResultLists;
    }

    private List<Result> factorizationResults(List<BigInteger> numbers,Factorizer factorizer){
        List<Result> results = new ArrayList<Result>();
        for(BigInteger number : numbers){
            Result result = compute(number.toString(),factorizer);
            if(result.succeeded){
                System.err.println("Succeeded:\t"+result.number);
            }else{
                System.err.println("Failed:\t\t"+result.number);
            }
            results.add(result);
        }
        return results;
    }

    private List<BigInteger> nFactorProducts(int lowerExponent,int upperExponent){
        List<BigInteger> numbers = new ArrayList<BigInteger>();
        for(int i = lowerExponent;i <= upperExponent;i++){
            BigInteger composite = nFactorProduct(i,2);
            numbers.add(composite);
        }
        return numbers;
    }

    /**
     * @return The product of n random primes on the interval [2^e,2^(e+1)-1].
     */
    private BigInteger nFactorProduct(final int e,final int n){
        BigInteger upper = new BigInteger("2").pow(e+1).subtract(BigInteger.ONE);
        List<BigInteger> primes = new ArrayList<BigInteger>();
        BigInteger randomPosition = upper;
        while(primes.size() < n){
            randomPosition = randomNumberOnInterval(e);
            for(BigInteger i = randomPosition;i.compareTo(upper) <= 0;i = i.add(BigInteger.ONE)){ //TODO:<=?
                if(i.isProbablePrime(100)){
                    primes.add(i);
                    if(primes.size() == n){
                        break;
                    }
                }
            }
        }
        if(primes.size() > n){
            throw new RuntimeException("Accumulated more primes than expected, for some reason.");
        }
        BigInteger product = BigInteger.ONE;
        for(BigInteger prime : primes){
            product = product.multiply(prime);
        }
        return product;
    }

    private List<Result> randomResults(int lowerExponent,int upperExponent,int iterations,Factorizer factorizer){
        List<Result> results = new ArrayList<Result>();
        for(int i = lowerExponent;i <= upperExponent;i++){
            for(int j = 1;j <= iterations;j++){
                results.add(randomResult(i,factorizer));
            }
        }
        Collections.sort(results,new NumberComparator());
        return results;
    }

    /**
     * @return The result of computing a random number in [2^n,2^(n+1)-1].
     */
    private Result randomResult(final int n,final Factorizer factorizer){
        if(n <= 0){
            return new Result(new BigInteger("0"),new ArrayList<BigInteger>(),0,factorizer);
        }
        BigInteger randomNo = randomNumberOnInterval(n);
        return compute(randomNo.toString(),factorizer);
    }

    /**
     * @return A random number on [2^n,2^(n+1)-1].
     */
    private BigInteger randomNumberOnInterval(final int n){
        if(n <= 0){
            return BigInteger.ZERO;
        }
        Random rng = new Random();
        BigInteger randomNumber;
        do{
            randomNumber = new BigInteger(n+1,rng);
        }while(randomNumber.shiftRight(n).equals(BigInteger.ZERO));
        return randomNumber;
    }

    private List<Result> testInterval(final String startpoint,final String endpoint,final int iterations,final Factorizer factorizer){
        List<Result> results = new ArrayList<Result>();
        BigInteger a = new BigInteger(startpoint);
        BigInteger b = new BigInteger(endpoint);
        for(BigInteger i = a;i.compareTo(b) <= 0;i = i.add(new BigInteger("1"))){
            Result result = null;
            for(int j = 0;j < iterations;j++){
                Result r = compute(i.toString(),factorizer);
                if(result == null){
                    result = r;
                }else if(r.nanoseconds < result.nanoseconds){
                    result = r;
                }
            }
            results.add(result);
        }
        return results;
    }

    private Result compute(final String number,final Factorizer factorizer){
        BigInteger n = new BigInteger(number);
        if(n.compareTo(BigInteger.ZERO) <= 0){
            return new Result(n,new ArrayList<BigInteger>(),0,factorizer);
        }
        final long currentTime = System.nanoTime();
        List<BigInteger> output = factorizer.factorize(new BigInteger(""+number));
        final long diffTime = System.nanoTime()-currentTime;
        if(output == null){
            output = new ArrayList<BigInteger>();
        }
        return new Result(n,output,diffTime,factorizer);
    }

    private void assertEquals(final String number,final List<BigInteger> factors,final Factorizer factorizer){
        final List<BigInteger> output = factorizer.factorize(new BigInteger(""+number));
        final List<BigInteger> returnedFactors = new ArrayList<BigInteger>(output);
        final int outputSize = output.size();
        if(outputSize != factors.size()){
            error("Factor list sizes do not match.",output,factors);
        }
        Iterator<BigInteger> it = factors.iterator();
        while(it.hasNext()){
            BigInteger factor = it.next();
            if(!returnedFactors.contains(factor)){
                error("Factor "+factor+" not present in output.",output,factors);
            }
            returnedFactors.remove(returnedFactors.indexOf(factor));
        }
        if(returnedFactors.size() > 0){
            error("Output returned more factors than expected!",output,factors);
        }
    }

    private void error(String msg,List<BigInteger> returned,List<BigInteger> expected){
        String appendix = "Returned: "+returned+"\nExpected: "+expected;
        throw new IllegalArgumentException(msg+"\n"+appendix);
    }

    private List<BigInteger> list(long... numbers){
        List<BigInteger> list = new ArrayList<BigInteger>();
        for(long n : numbers){
            list.add(new BigInteger(""+n));
        }
        return list;
    }

    public static void main(String[] args){
        Tester t = new Tester(args);
        System.err.println("Running tests...");
        t.runTests();
        System.err.println("Testing finished.");
    }

    /**
     * The results of factorizing a number.
     */
    public class Result{
        public final BigInteger number; /* What was factorized. */
        public final boolean correct; /* True iff the factorization is correct. */
        public final boolean succeeded; /* False iff the algorithm failed to factorize the number. */
        public final List<BigInteger> factors; /* The factors that the algorithm found. */
        public final long nanoseconds; /* The time to produce a collection of prime factors from a number. */
        public final Factorizer factorizer; /* The factorization algorithm used. */

        public Result(BigInteger number,List<BigInteger> factors,long nanoseconds,Factorizer factorizer){
            this.number = number;
            correct = correct(number,factors);
            this.succeeded = factors.size() != 0;
            this.nanoseconds = nanoseconds;
            this.factorizer = factorizer;
            this.factors = new ArrayList<BigInteger>(factors);
        }

        private boolean correct(final BigInteger number,final List<BigInteger> factors){
            BigInteger product = new BigInteger("1");
            Iterator<BigInteger> it = factors.iterator();
            while(it.hasNext()){
                BigInteger factor = it.next();
                product = product.multiply(factor);
            }
            return product.equals(number);
        }

        public String toStringSingleLine(){
            return number+" "+(factors+"").replace(" ","")+" "+factorizer.getClass().getName()+" "
                +succeeded+" "+correct+" "+nanoseconds;
        }

        public String toStringNumberTime(){
            final int padding = Math.max(0,90-number.toString().length());
            final StringBuilder sb = new StringBuilder();
            for(int i = 0;i < padding;i++){
                sb.append(' ');
            }
            return number+sb.toString()+nanoseconds;
        }

        public String toString(){
            return	"Number:\t\t"+number
                +"\nFactors:\t"+factors
                +"\nAlgorithm:\t"+factorizer.getClass().getName()
                +"\nSucceeded:\t"+succeeded
                +"\nCorrect:\t"+correct
                +"\nTime:\t\t"+(nanoseconds/1000)+" μs";
        }
    }

    public class NumberComparator implements Comparator<Result>{
        public int compare(Result r1,Result r2){
            return r1.number.compareTo(r2.number);
        }
    }
}
