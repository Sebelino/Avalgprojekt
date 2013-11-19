/**
 * Abstract class for a algorithm solving the traveling salesman problem.
 * Every algorithm should be wrapped in a class and inherit this class.
 */
public abstract class Algorithm{
    /**
     * @return The tour, represented as a permutation of the set {1,...,n}.
     * @param instance a set of (x,y) points, where instance[i] is the ith point.
     */
    public abstract int[] algorithm(double[][] instance);
}
