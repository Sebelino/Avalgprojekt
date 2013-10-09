import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Tester{
    public static void main(String[] args){
		Factorizer factorizer = Util.getFactorizer(args);
        if(factorizer == null){
            factorizer = new NaiveEratost2();
        }

        assertEquals("2",list(2),factorizer);
        assertEquals("3",list(3),factorizer);
        assertEquals("4",list(2,2),factorizer);
        assertEquals("5",list(5),factorizer);
        assertEquals("81",list(3,3,3,3),factorizer);
        assertEquals("85219",list(31,2749),factorizer);
        assertEquals("44051",list(29,7,31,7),factorizer);
        assertEquals("9809423",list(13,31,101,241),factorizer);
        assertEquals("6342995164",list(2,2,11,144158981),factorizer);
        assertEquals("291282330479",list(7,31,929,1444903),factorizer);
        assertEquals("184068044163235",list(5,19,137,32191,439339),factorizer);
        System.out.println("Testing finished.");
    }

    private static void assertEquals(final String number,final List<BigInteger> factors,final Factorizer factorizer){
        final List<BigInteger> output = factorizer.factorize(new BigInteger(""+number));
        final List<BigInteger> returnedFactors = new ArrayList<BigInteger>();
        for(BigInteger n : output){
            returnedFactors.add(n);
        }
        final int outputSize = output.size();
        if(outputSize != factors.size()){
            error("Factor list sizes do not match.",output,factors);
        }
        Iterator<BigInteger> it = factors.iterator();
        while(it.hasNext()){
            BigInteger factor = it.next();
            if(!returnedFactors.contains(factor)){
                error("Factor "+factor+" not present in output.",output,factors);
            }
            returnedFactors.remove(returnedFactors.indexOf(factor));
        }
        if(returnedFactors.size() > 0){
            error("Output returned more factors than expected!",output,factors);
        }
    }

    private static void error(String msg,List<BigInteger> returned,List<BigInteger> expected){
        String appendix = "Returned: "+returned+"\nExpected: "+expected;
        throw new IllegalArgumentException(msg+"\n"+appendix);
    }

    private static List<BigInteger> list(long... numbers){
        List<BigInteger> list = new ArrayList<BigInteger>();
        for(long n : numbers){
            list.add(new BigInteger(""+n));
        }
        return list;
    }
}
