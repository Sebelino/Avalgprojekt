import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Naive extends Factorizer{
    public List<BigInteger> factorize(BigInteger input){
        final String limitStr = "100000000000000000";
        final int certainty = 100;

        BigInteger limit = new BigInteger(limitStr);
        if(input.compareTo(limit) > 0){ return new ArrayList<BigInteger>(); }
        List<BigInteger> factors = new ArrayList<BigInteger>();
        BigInteger number = new BigInteger(""+input);
        for(BigInteger i = new BigInteger("2");!number.equals(BigInteger.ONE);i = i.add(BigInteger.ONE)){
            if(number.isProbablePrime(certainty)){
                factors.add(number);
                number = BigInteger.ONE;
            }
            if(number.mod(new BigInteger(""+i)).equals(BigInteger.ZERO)){
                factors.add(i);
                number = number.divide(i);
                i = i.subtract(BigInteger.ONE);
            }
        }
        return factors;
    }
}
