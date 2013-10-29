public class Gauss {

    /** Gauss elimination of a boolean homogeneous linear system.
     *  Note: Modifies matrix a.
     *  @return True iff rank(a) = width(a). In that case, there is only
     *  one (trivial) solution, namely the zero vector.
     **/
    public static boolean gauss(boolean[][] a) {
        int height = a.length;
        if(height == 0){
            return true;
        }
        int width = a[0].length;
        if(height < width){
            return false; // Because then rank(a) <= height(a) < width(a).
        }
        int nextIndex = 0;
        // Gaussian elimination
        for(int j = 0;j < width;j++){
            int pivotIndex = -1;
            for(int i = nextIndex;i < height;i++){
                if(a[i][j]){
                    pivotIndex = i;
                    break;
                }
            }
            if(pivotIndex == -1){
                // All elements in this column were zeroes.
                continue;
            }else{
                // Swap rows.
                boolean[] temp = new boolean[height];
                for(int i = 0;i < width;i++){
                    temp[i] = a[nextIndex][i];
                }
                for(int i = 0;i < width;i++){
                    a[nextIndex][i] = a[pivotIndex][i];
                }
                for(int i = 0;i < width;i++){
                    a[pivotIndex][i] = temp[i];
                }
                nextIndex++;
            }
            // Add the pivot row to appropriate rows.
            for(int i = pivotIndex+1;i < height;i++){
                int addingRowIndex = nextIndex-1;
                if(a[i][j]){
                    for(int k = 0;k < width;k++){
                        a[i][k] = a[i][k] ^ a[addingRowIndex][k];
                    }
                }
            }
        }
        // Check to see if a[width-1] is a zero row.
        for(int i = 0;i < width;i++){
            if(a[width-1][i]){
                return true; // Apparently not.
            }
        }
        return false; // a[width(a)-1] is a zero row => rank(a) = width(a).
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
//                       { {0,1,1}
//                        ,{1,0,1}
//                        ,{0,0,0}
//                        ,{1,1,0}
//                        ,{0,0,0}
//                        ,{0,1,1}
                       { {1,1,0}
                        ,{0,1,1}
                        ,{1,1,0}
                        ,{0,0,0}
                        ,{0,0,0}
                        ,{1,1,0}
                        ,{0,0,0}
                        ,{0,0,0}
                        ,{0,1,0}
                        ,{0,0,0}
                        ,{1,0,1}
                       });
        System.out.println("Input matrix:");
        printMatrix(a);
        boolean uniqueSolution = gauss(a);
        System.out.print("The solution is ");
        if(uniqueSolution){
            System.out.println("trivial.");
        }else{
            System.out.println("nontrivial");
        }
        System.out.println("Gaussed matrix:");
        printMatrix(a);
    }
}
