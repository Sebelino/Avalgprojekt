import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class Factorize{
    public static void main(String[] args){
        List<BigInteger> input = read();
        for(BigInteger n : input){
            List<BigInteger> factorization = naive(n,1000000);
            printFactorization(factorization);
        }
    }

    /** Not working correctly on Sebastian's computer but Kattis accepts it. */
    private static List<BigInteger> read(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<BigInteger> input = new ArrayList<BigInteger>();
        try{
            while(br.ready()){
                String line = br.readLine();
                if(line.length() > 0){
                    input.add(new BigInteger(line));
                }
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return input;
    }

    /** Check if n is divisible with every integer in [2..n]. */
    private static List<BigInteger> naive(BigInteger input,int limit){
        if(input.compareTo(new BigInteger(""+limit)) > 0){ return new ArrayList<BigInteger>(); }
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");sqrt(input).compareTo(i) >= 0;i = i.add(new BigInteger("1"))){
            if(number.mod(new BigInteger(""+i.toString())).equals(new BigInteger("0"))){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(new BigInteger("1"));
            }
        }
        BigInteger product = product(factors);
        if(!product.equals(input)){
            factors.add(number);
        }
        return factors;
    }

    private static BigInteger product(List<BigInteger> factors){
        BigInteger product = new BigInteger("1");
        for(BigInteger f : factors){
            product = product.multiply(f);
        }
        return product;
    }

    @Test
    public void testAdd(){
        assertEquals("Junit is working","Junit is working");
    }

    /** Check if n is divisible with every integer in [2..n]. */
    private static List<BigInteger> stupid(BigInteger input,int limit){
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

    private static void printFactorization(List<BigInteger> factors){
        if(factors.isEmpty()){
            System.out.println("fail");
        }else{
            for(BigInteger f : factors){
                System.out.println(f);
            }
        }
        System.out.println();
    }

    private static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if(mid.multiply(mid).compareTo(n) > 0){ b = mid.subtract(BigInteger.ONE); }
            else{ a = mid.add(BigInteger.ONE); }
        }
        return a.subtract(BigInteger.ONE);
    }
}
