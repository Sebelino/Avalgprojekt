import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

public class Tester{
    private final Factorizer factorizer;

    public Tester(String[] args){
        Factorizer f = null;
        try{
            f = Util.getFactorizer(args);
        }catch(ClassNotFoundException e){
            f = new Naive();
        }finally{
            factorizer = f;
            System.err.println("Using "+factorizer.getClass().getName()+".");
        }
    }

    public void runTests(){
        //assertEquals("6342995164",list(2,2,11,144158981),factorizer);
        //compute("291282330479",factorizer);
        //testInterval("2","1000000",factorizer);
        List<Result> results = benchmark(factorizer);
        for(Result result : results){
            System.out.println(result.toStringNumberTime());
        }
    }

    private List<Result> benchmark(Factorizer factorizer){
        List<Result> results;
        /* Get raw results */
        //int samples = 1000;
        //results = testInterval("2","10000",3,factorizer);
        results = dualFactorResults(3,60,factorizer);
        /* Process the results */
        List<Result> processedResults = new ArrayList<Result>();
        final Iterator<Result> it = results.iterator();
        while(it.hasNext()){
            Result r = it.next();
            if(r.succeeded/* && !r.factorizer.getClass().getName().equals("Naive")*/){
                processedResults.add(r);
            }
        }
//        Iterator<Result> it = results.iterator();
//        while(it.hasNext()){
//            long averageTime = 0;
//            Result element = null;
//            int failures = 0;
//            for(int i = 0;i < samples;i++){
//                element = it.next();
//                if(!element.succeeded){
//                    failures++;
//                }
//                averageTime += element.nanoseconds;
//            }
//            if((double)failures/samples > 0.01){ // Fault tolerance.
//                System.err.println("Failed around "+element.number+".");
//                break;
//            }else{
//                processedResults.add(new Result(element.number,element.factors,averageTime/samples,element.factorizer));
//            }
//        }
        return processedResults;
    }

    private List<Result> dualFactorResults(int lowerExponent,int upperExponent,Factorizer factorizer){
        List<Result> results = new ArrayList<Result>();
        for(int i = lowerExponent;i <= upperExponent;i++){
            BigInteger composite = dualFactorProduct(i,factorizer);
            Result result = compute(composite.toString(),factorizer);
            if(result.succeeded){
                System.err.println("Succeeded:\t"+result.number);
            }else{
                System.err.println("Failed:\t\t"+result.number);
            }
            results.add(result);
        }
        return results;
    }

    /**
     * @return The product of two primes on the interval [2^e,2^(e+1)-1].
     */
    private BigInteger dualFactorProduct(final int e,final Factorizer factorizer){
        BigInteger upper = new BigInteger("2").pow(e+1).subtract(BigInteger.ONE);
        List<BigInteger> primes = new ArrayList<BigInteger>();
        for(BigInteger i = new BigInteger("2").pow(e);i.compareTo(upper) < 0;i = i.add(BigInteger.ONE)){
            if(i.isProbablePrime(100)){
                primes.add(i);
                if(primes.size() == 2){
                    break;
                }
            }
        }
        if(primes.size() != 2){
            throw new RuntimeException("Did not find 2 primes.");
        }
        BigInteger product = primes.get(0).multiply(primes.get(1));
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
        Random rng = new Random();
        BigInteger randomNumber;
        do{
            randomNumber = new BigInteger(n+1,rng);
        }while(randomNumber.shiftRight(n).equals(BigInteger.ZERO));
        return compute(randomNumber.toString(),factorizer);
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
