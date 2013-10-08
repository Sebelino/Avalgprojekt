import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Stupid extends Factorizer{
    public List<BigInteger> factorize(BigInteger input){
        int limit = 10000;
        if(input.compareTo(new BigInteger(""+limit)) > 0){ return new ArrayList<BigInteger>(); }
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");input.compareTo(i) >= 0;i = i.add(new BigInteger("1"))){
            if(number.mod(new BigInteger(""+i.toString())).equals(new BigInteger("0"))){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(new BigInteger("1"));
            }
        }
        return factors;
    }
}
