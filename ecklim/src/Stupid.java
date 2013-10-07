import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Stupid extends Factorizer{
    public static void main(String[] args){
        Factorizer fer = new Naive();
        List<BigInteger> input = Util.read();
        for(BigInteger n : input){
            List<BigInteger> factorization = fer.factorize(n);
            Util.printFactorization(factorization);
        }
    }

    public List<BigInteger> factorize(BigInteger input){
        int limit = 1000;
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
