import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Run{
	private Run(){}

    @SuppressWarnings("rawtypes")
    public Algorithm getAlgorithm(String[] args) throws ClassNotFoundException{
        if(args.length == 1){
            String token = args[0];
            Class c = null;
            c = Class.forName(token);
            try{
                return (Algorithm)c.newInstance();
            }catch(InstantiationException e){
                System.err.println("The class could not be instantiated: "+token);
            }catch(IllegalAccessException e){
                System.err.println("Illegal access.");
            }
        }else{
            throw new ClassNotFoundException("One argument, please.");
        }
        return null;
    }

    public float[][] read(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        float[][] input = null;
        try{
            if(br.ready()){
                String line = br.readLine();
                if(line.length() > 0){
                    int numberOfPoints = Integer.parseInt(line);
			        input = new float[numberOfPoints][2];
                }else{
                	throw new RuntimeException("The first line in the input was empty!");
                }
            }
            int pointCtr = 0;
            while(pointCtr < input.length && br.ready()){
                String line = br.readLine();
                if(line.length() > 0){
                	String[] coordStrings = line.split("\\s+");
                    input[pointCtr][0] = Float.parseFloat(coordStrings[0]); // x
                    input[pointCtr][1] = Float.parseFloat(coordStrings[1]); // y
                    pointCtr++;
                }
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return input;
    }

    private float[][] randomInput(int size){
    	float[][] input = new float[size][2];
		Random rand = new Random();
		for(int i = 0;i < size;i++) {
			input[i][0] = rand.nextFloat()*2000000-1000000;
			input[i][1] = rand.nextFloat()*2000000-1000000;
		}
		return input;
    }

    public void printTour(int[] tour){
    	for(int index : tour){
    		System.out.println(index);
    	}
    }

	public static void main(String[] args){
		Run run = new Run();
		float[][] input;
		if(args.length == 2 && args[0].equalsIgnoreCase("random")){
			input = run.randomInput(Integer.parseInt(args[1]));
		}else{
			input = run.read();
		}
		for(int i = 0;i < input.length;i++){
			for(int j = 0;j < input[i].length;j++){
				System.out.print(input[i][j]+"\t");
			}
			System.out.println();
		}
        Algorithm algorithm = null;
        try{
            algorithm = run.getAlgorithm(args);
        }catch(ClassNotFoundException e){
            algorithm = new Christofides();
        }
        System.err.println("Using "+algorithm.getClass().getName());
		int[] tour = algorithm.tour(input);
		run.printTour(tour);
	}
}
