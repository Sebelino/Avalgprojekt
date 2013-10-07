import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Tester{
    public static void main(String[] args){
        naiveAssertEquals(12,1000,list(3,2,2));
        naiveAssertEquals(12,1000,list(3,4,2));
        System.out.println("Testing finished.");
    }

    private static void naiveAssertEquals(int number,int limit,List<BigInteger> factors){
        BigInteger n = new BigInteger(""+number);
        for(BigInteger f : factors){
            if(!isPrime(f)){
                throw new IllegalArgumentException(f.toString()+" was found to not be a prime!");
            }
        }
        if(!Util.product(factors).equals(n)){
            throw new IllegalArgumentException("The product did not equal the input!");
        }
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
