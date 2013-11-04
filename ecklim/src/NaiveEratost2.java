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
        while(number.mod(new BigInteger("2")).equals(BigInteger.ZERO)){
            number = number.divide(new BigInteger("2"));
            factors.add(new BigInteger("2"));
        }
        for(BigInteger i = new BigInteger("3");!number.equals(BigInteger.ONE);i = i.add(new BigInteger("2"))){
            //System.out.println("i="+i);
            if(number.isProbablePrime(certainty)){
                factors.add(number);
                number = BigInteger.ONE;
            }
            if(i.compareTo(limit) >= 0){
                return new ArrayList<BigInteger>();
            }
            while(number.mod(new BigInteger(""+i)).equals(BigInteger.ZERO)){
                number = number.divide(i);
                factors.add(i);
            }
        }
        return factors;
    }
}
