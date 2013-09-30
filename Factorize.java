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
            while(true){
                String line = br.readLine();
                BigInteger num;
                try{
                    num = new BigInteger(line);
                }catch(NumberFormatException e){
                    break;
                }
                input.add(num);
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        for(BigInteger n : input){
            List<BigInteger> factorization = stupid(n);
            printFactorization(factorization);
        }
    }

    private static List<BigInteger> stupid(BigInteger input){
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");input.compareTo(i) > 0;i = i.add(new BigInteger("1"))){
            if(number.mod(new BigInteger(""+i.toString())).equals(new BigInteger("0"))){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(new BigInteger("1"));
            }
        }
        return factors;
    }

    private static void printFactorization(List<BigInteger> factors){
        for(BigInteger f : factors){
            System.out.println(f);
        }
        System.out.println();
    }
}
