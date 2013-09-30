import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Factorize{
    public static void main(String[] args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<BigInteger> input = new ArrayList<BigInteger>();
        try{
            while(br.ready()){
                input.add(new BigInteger(br.readLine()));
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        for(BigInteger n : input){
            List<BigInteger> factorization = naive(n);
            printFactorization(factorization);
        }
    }

    private static List<BigInteger> naive(BigInteger number){
        return null;
    }

    private static void printFactorization(List<BigInteger> factors){
        for(BigInteger f : factors){
            System.out.println(f);
        }
        System.out.println();
    }
}
