import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Run{
	public static void main(String[] args){

		Factorizer factorizer = new Pollard();
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
					int a = 1/0;  // Kattis debug, will give run time error if we get here
					Util.printFactorization(null);
				}
			}
		}
	}
}
