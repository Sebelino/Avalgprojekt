import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Factorize{
    public static void main(String[] args){
        List<BigInteger> input = weed();
        for(BigInteger n : input){
            List<BigInteger> factorization = stupid(n);
            printFactorization(factorization);
        }
    }

    private static List<BigInteger> read(){
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
        return input;
    }

    private static List<BigInteger> weed(){
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
        return input;
    }

//    private static List<BigInteger> eratosthenes(BigInteger input){
//        List<BigInteger> factors = new ArrayList<BigInteger>();
//        BigInteger number = new BigInteger(""+input);
//        List
//        return factors;
//    }

    /** Check if n is divisible with every number in [2..n]. */
    private static List<BigInteger> stupid(BigInteger input){
        if(input.compareTo(new BigInteger("1000000")) > 0){ return new ArrayList(); }
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
}
