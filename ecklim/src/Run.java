import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Run{
    public static void main(String[] args){
        Factorizer factorizer = new Naive();
        List<BigInteger> input = Util.read();
        for(BigInteger n : input){
            List<BigInteger> factorization = factorizer.factorize(n);
            Util.printFactorization(factorization);
        }
    }
}
