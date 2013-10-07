import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Util{

    public static List<BigInteger> read(){
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

    public static BigInteger product(List<BigInteger> factors){
        BigInteger product = new BigInteger("1");
        for(BigInteger f : factors){
            product = product.multiply(f);
        }
        return product;
    }

    public static void printFactorization(List<BigInteger> factors){
        if(factors.isEmpty()){
            System.out.println("fail");
        }else{
            for(BigInteger f : factors){
                System.out.println(f);
            }
        }
        System.out.println();
    }

    public static BigInteger sqrt(BigInteger n) {
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
