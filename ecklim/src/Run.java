import java.math.BigInteger;
import java.util.List;

public class Run{
	public static void main(String[] args){
        Factorizer factorizer = null;
        try{
            factorizer = Util.getFactorizer(args);
        }catch(ClassNotFoundException e){
            //factorizer = new Naive();
            factorizer = new Pollard();
        }
		List<BigInteger> input = Util.read();
		for(BigInteger n : input){
			List<BigInteger> factorization = factorizer.factorize(n);
			BigInteger check = BigInteger.ONE;
			if(factorization == null || factorization.isEmpty()) {
				Util.printFactorization(null);
			}
			else {
				for(BigInteger factor : factorization) {
					check = check.multiply(factor);
				}
				if(check.compareTo(n) == 0) {
					Util.printFactorization(factorization);
				}
				else {
					throw new ArithmeticException();  // Kattis debug; will give run time error if we get here.
				}
			}
		}
	}
}
