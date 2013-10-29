import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class NaiveEratost2 extends Factorizer{
    public List<BigInteger> factorize(BigInteger input){
        final String limitStr = "7300";
        final int certainty = 100;

        BigInteger limit = new BigInteger(limitStr);
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");!number.equals(BigInteger.ONE);i = i.add(new BigInteger("2"))){
            //System.out.println("i="+i);
            if(number.isProbablePrime(certainty)){
                factors.add(number);
                number = BigInteger.ONE;
            }
            if(i.compareTo(limit) >= 0){
                return new ArrayList<BigInteger>();
            }
            if(number.mod(new BigInteger(""+i)).equals(BigInteger.ZERO)){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(new BigInteger("2"));
            }
            if(i.equals(new BigInteger("2"))){
                i = i.subtract(BigInteger.ONE);
            }
        }
        return factors;
    }
}