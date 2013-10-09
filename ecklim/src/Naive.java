import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Naive extends Factorizer{
    public List<BigInteger> factorize(BigInteger input){
        BigInteger limit = new BigInteger("100000000000000000");
        if(input.compareTo(limit) > 0){ return new ArrayList<BigInteger>(); }
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");limit.compareTo(i) >= 0;i = i.add(new BigInteger("1"))){
            if(number.isProbablePrime(100)){
                factors.add(number);
                number = BigInteger.ONE;
                break;
            }
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
        return factors;
    }
}
