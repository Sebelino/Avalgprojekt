import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class NaiveInt extends Factorizer{
    public List<BigInteger> factorize(BigInteger inputBigInt){
        long input = inputBigInt.longValue();
//        long startTime = System.nanoTime();
        long limit = 1000;
      //BigInteger limit = new BigInteger("100000000000000000"); // Time limit exceeded här.
        List<Long> factors = new ArrayList<Long>();
        long number = input;
        for(long i = 2;i <= Math.sqrt(input);i++){
            if(i > limit){return new ArrayList<BigInteger>();}
            if(number % i == 0){
                factors.add(i);
                number /= i;
                i--;
            }
        }
        List<BigInteger> biFactors = new ArrayList<BigInteger>();
        for(long n : factors){
            biFactors.add(new BigInteger(""+n));
        }
        BigInteger product = Util.product(biFactors);
        if(!product.equals(inputBigInt)){
            biFactors.add(new BigInteger(""+number));
        }
//        long endTime = System.nanoTime();System.err.println("Time="+(endTime-startTime));
        return biFactors;
    }
}
