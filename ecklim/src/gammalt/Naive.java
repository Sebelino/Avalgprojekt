import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Naive extends Factorizer{
    public List<BigInteger> factorize(BigInteger input){
//        long startTime = System.nanoTime();
        BigInteger limit = new BigInteger("10000000000000000");
      //BigInteger limit = new BigInteger("100000000000000000"); // Time limit exceeded här.
        if(input.compareTo(limit) > 0){ return new ArrayList<BigInteger>(); }
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");Util.sqrt(input).compareTo(i) >= 0;i = i.add(new BigInteger("1"))){
            System.err.println("i="+i+"number="+number.toString());
            if(number.mod(new BigInteger(""+i.toString())).equals(new BigInteger("0"))){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(new BigInteger("1"));
            }
        }
        BigInteger product = Util.product(factors);
        if(!product.equals(input)){
            factors.add(number);
        }
//        long endTime = System.nanoTime();System.err.println("Time="+(endTime-startTime));
        return factors;
    }
}