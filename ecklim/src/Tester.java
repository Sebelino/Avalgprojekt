import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Tester{
    public static void main(String[] args){
        Factorizer naive = new Naive();
        assertEquals(81,list(3,3,3,3),naive);
        System.out.println("Testing finished.");
    }

    private static void assertEquals(final int number,final List<BigInteger> factors,final Factorizer factorizer){
        final List<BigInteger> output = factorizer.factorize(new BigInteger(""+number));
        final List<BigInteger> returnedFactors = new ArrayList<BigInteger>();
        for(BigInteger n : output){
            returnedFactors.add(n);
        }
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

    private static void error(String msg,List<BigInteger> returned,List<BigInteger> expected){
        String appendix = "Returned: "+returned+"\nExpected: "+expected;
        throw new IllegalArgumentException(msg+"\n"+appendix);
    }

    private static List<BigInteger> list(int... numbers){
        List<BigInteger> list = new ArrayList<BigInteger>();
        for(int n : numbers){
            list.add(new BigInteger(""+n));
        }
        return list;
    }

    private static boolean isPrime(BigInteger n){
        for(BigInteger i = new BigInteger("2");n.compareTo(i) > 0;i = i.add(new BigInteger("1"))){
            if(n.mod(new BigInteger(""+i.toString())).equals(new BigInteger("0"))){
                return false;
            }
        }
        return true;
    }
    private static boolean isPrime(int n){
        return isPrime(new BigInteger(""+n));
    }
}
