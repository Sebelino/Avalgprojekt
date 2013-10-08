import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

/**
 * Abstract class for a factorization algorithm.
 * Every factorization algorithm should be wrapped in a class and inherit this class.
 */
public abstract class Factorizer{
    /** @return A list of prime number factors of the number input. */
    public abstract List<BigInteger> factorize(BigInteger input);
}
