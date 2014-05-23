import java.util.*;
public class Algorithm {

	
	//problem
	public int m = 50;
	public int[] weights = {5, 7, 4, 3};
	public int[] revenues = {8, 11, 6, 4};
	public int maxWeight = 14;
	public int SIZE = weights.length;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Algorithm().run();

	}
	public void run(){
		long time = System.currentTimeMillis();
		if(m != 1){
			expandProblem(m);
		}
		
		//choose initial solution
		int[] x = new int [SIZE];
		int[] bestSoFar = x;
		
		//parameters
		double t = 100;
		double tmin = 10;
		int L = 1000;
		double alpha = 0.95;
		
		while(t > tmin){
			int count = 0;
			while(count < L){
				
				//choose random neighbour y to x
				int [] y = neighbour(x);
	
				
				int deltaF = revenue(x) - revenue(y);
				if(deltaF <= 0){
					x = y;
				}else if (metropolis(deltaF, t)){
					x = y;
				}
				if(revenue(x) > revenue(bestSoFar)){
					bestSoFar = x;
				}
				
				count ++;
			}
			t = t*alpha;
		}
		System.out.print("Result: ");
		print(bestSoFar);
		System.out.println("Total weight: " + weight(bestSoFar));
		System.out.println("Execution time: " + (System.currentTimeMillis() - time) + " ms");
	}
	
	/*
	 * Metropolis test
	 */
	private boolean metropolis(int deltaF, double t){
		double rand = Math.random();
		return rand < Math.exp(-1.0*deltaF/t);
	}

	/*
	 * Calculates the total revenue of a knapsack with 
	 * items according to the input vector
	 */
	private int revenue(int [] vector){
		
		int sum = 0;	
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 1){
				sum += revenues[i];
			}
		}
		return sum;
	}
	
	/*
	 * Calculates the total weight of a knapsack with 
	 * items according to the input vector
	 */
	private int weight(int [] vector){
		
		int sum = 0;	
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 1){
				sum += weights[i];
			}
		}
		return sum;
	}
	
	/*
	 * Return a random neighbour to the input vector
	 */
	private int [] neighbour(int [] vector){
		List<int []> neighbourList = new ArrayList<int []>();
		int [] cur = copy(vector);
		
		//add
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 0){
				cur[i] = 1;
				if(weight(cur) <= maxWeight){
					neighbourList.add(cur);
				}
				cur = copy(vector);
			}
		}
		
		//delete
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 1){
				cur[i] = 0;
				neighbourList.add(cur);
				cur = copy(vector);
			}
		}
		
		//replace
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 1){
				for(int j = 0; j < SIZE; j ++){
					if(vector[j] == 0){ // i != j implicit
						cur[i] = 0;
						cur[j] = 1;
						if(weight(cur) < maxWeight){
							neighbourList.add(cur);
							cur = copy(vector);
						}
					}
				}
			}
		}
		
		int random = (int) (neighbourList.size()*Math.random());
		return neighbourList.get(random);
	}
	
	/*
	 * Prints a vector of int values on the form {0, ..., ..., 1}
	 */
	private void print(int [] vector){
		
		System.out.print("{");
		for(int i = 0; i < SIZE-1; i++){
			System.out.print(vector[i] +", ");
		}
		System.out.println(vector[SIZE-1] + "}");
	}
	
	/*
	 * Returns a copy of an int vector
	 */
	private int [] copy(int [] vector) {
		int [] copy = new int[SIZE];
		for(int i = 0; i < SIZE; i++){
			copy[i] = vector[i];
		}
		return copy;
	}
	
	/*
	 * Creates m copies of the objects and 
	 * multiplies the maxWeight by m
	 */
	private void expandProblem(int m){
		SIZE *= m;
		maxWeight *= m;
		int [] tempWeights = new int[SIZE];
		int [] tempRevenues = new int[SIZE];
		
		for(int i = 0; i < SIZE; i += m){
			for(int j = 0; j < m; j++){
				tempWeights[i+j] = weights[i/m];
				tempRevenues[i+j] = revenues[i/m];
			}
		}
		
		weights = tempWeights;
		revenues = tempRevenues;
	}
}
