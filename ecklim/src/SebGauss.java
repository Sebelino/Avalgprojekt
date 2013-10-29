import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class SebGauss {

    /** Gauss-Jordan elimination of a boolean homogeneous linear system.
     *  Note: Modifies matrix a.
     *  @return A basis for the null space of matrix a. That is, the
     *  vectors v and w in the general solution x = tv+sw, for example
     *  (where t and s are parameters, i.e. members of the set {0,1}).
     *  Returns an empty vector if there is only one (trivial) solution.
     **/
    public static boolean[][] gaussJordan(boolean[][] a) {
        int height = a.length;
        if(height == 0){
            return new boolean[][]{};
        }
        int width = a[0].length;
        int nextIndex = 0;
        // Gaussian elimination
        for(int j = 0;j < width;j++){
            int pivotRow = -1;
            for(int i = nextIndex;i < height;i++){
                if(a[i][j]){
                    pivotRow = i;
                    break;
                }
            }
            if(pivotRow == -1){
                continue; // All elements in this column were zeroes.
            }else{
                // Swap rows.
                boolean[] temp = new boolean[width];
                for(int i = 0;i < width;i++){
                    temp[i] = a[nextIndex][i];
                }
                for(int i = 0;i < width;i++){
                    a[nextIndex][i] = a[pivotRow][i];
                }
                for(int i = 0;i < width;i++){
                    a[pivotRow][i] = temp[i];
                }
                nextIndex++;
            }
            // Add the pivot row to appropriate rows.
            for(int i = pivotRow+1;i < height;i++){
                int addingRowIndex = nextIndex-1;
                if(a[i][j]){
                    for(int k = 0;k < width;k++){
                        a[i][k] ^= a[addingRowIndex][k];
                    }
                }
            }
        }
        //System.out.println("Done with Gauss."); printMatrix(a);
        final int rank = nextIndex;
        final int nullity = width-rank;
        if(nullity == 0){
            // If nullity(a) = 0, there can be nothing but a trivial solution.
            return new boolean[][]{};
        }
        Set<Integer> pivotCols = new HashSet<Integer>();
        // Jordanian elimination
        for(int i = Math.min(width,height)-1;i >= 0;i--){
            for(int j = 0;j < width;j++){
                // Is this a pivot element?
                if(a[i][j]){
                    pivotCols.add(j);
                    // Then add the pivot row (row i) to appropriate rows.
                    for(int k = i-1;k >= 0;k--){
                        if(a[k][j]){
                            for(int m = j;m < width;m++){
                                a[k][m] ^= a[i][m];
                            }
                        }
                    }
                    break;
                }
            }
        }
        //System.out.println("Done with Jordan."); printMatrix(a);
        List<Integer> nullCols = new ArrayList<Integer>();
        for(int j = 0;j < width;j++){
            if(!pivotCols.contains(j)){
                nullCols.add(j);
            }
        }
        boolean[][] basis = new boolean[nullity][width];
        for(int nc = 0;nc < nullCols.size();nc++){
            basis[nc][nullCols.get(nc)] = true;
            for(int i = 0;i < height;i++){
                if(a[i][nullCols.get(nc)]){
                    for(int j = 0;j < width;j++){
                        if(a[i][j]){
                            basis[nc][j] = true;
                        }
                    }
                }
            }
        }
        return basis;
    }

    private static void printMatrix(boolean[][] matrix){
        for(int i = 0;i < matrix.length;i++){
            for(int j = 0;j < matrix[i].length;j++){
                System.out.print((matrix[i][j] ? 1 : 0)+" ");
            }
            System.out.println();
        }
    }

    private static boolean[][] numbersToBools(byte[][] matrix){
        int height = matrix.length;
        if(matrix.length == 0){
            return new boolean[][]{};
        }
        int width = matrix[0].length;
        boolean[][] boolMatrix = new boolean[height][width];
        for(int i = 0;i < height;i++){
            for(int j = 0;j < width;j++){
                boolMatrix[i][j] = matrix[i][j] == 1;
            }
        }
        return boolMatrix;
    }

    public static void main(String[] args) {
        boolean[][] a = numbersToBools(new byte[][]
    // Taget från exemplet här: http://www.nada.kth.se/~joel/qs_lecture.pdf
                       { {0,1,1}
                        ,{1,0,1}
                        ,{0,0,0}
                        ,{1,1,0}
                        ,{0,0,0}
                        ,{0,1,1}

    // System med trivial lösning
//                       { {1,1,0}
//                        ,{0,1,1}
//                        ,{1,1,0}
//                        ,{0,0,0}
//                        ,{0,0,0}
//                        ,{1,1,0}
//                        ,{0,0,0}
//                        ,{0,0,0}
//                        ,{0,1,0}
//                        ,{0,0,0}
//                        ,{1,0,1}

    // System med icketrivial lösning
//                       { {1,1,0,0,0,0}
//                        ,{0,0,0,1,0,1}
//                        ,{1,0,1,0,0,0}
//                        ,{0,0,0,1,1,0}
                       });
        System.out.println("Input matrix:");
        printMatrix(a);
        boolean[][] x = gaussJordan(a);
        System.out.println("Gaussed-Jordan'd matrix:");
        printMatrix(a);
        System.out.print("The solution space is ");
        if(x.length == 0){
            System.out.println("trivial (a zero vector).");
        }else{
            System.out.println("nontrivial. Basis for the solution space:");
            printMatrix(x);
        }
    }
}
