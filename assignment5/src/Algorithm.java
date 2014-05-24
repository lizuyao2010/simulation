import java.util.*;
public class Algorithm {

	
	//problem
	public int m = 10;
	public int[] weights = {5, 7, 4, 3};
	public int[] revenues = {8, 11, 6, 4};
	public int maxWeight = 14;
	public int SIZE = weights.length;
	
	public static void main(String[] args) {
		new Algorithm().run();
	}
	public void run(){
		long time = System.currentTimeMillis();
		
		//expanding the problem if required
		if(m != 1){
			expandProblem(m);
		}

		//choose initial solution and currently best solution to only zeros
		int[] x = new int [SIZE];
		int[] bestSoFar = new int[SIZE];
		
		//parameters
		double t = 1094;
		double tmin = 1.59;
		int L = 1000;
		double alpha = 0.95;
		
		while(t > tmin){
			int count = 0;
			while(count < L){
				
				//choose random neighbor y to x		
				int[] y = neighbor(x);
				//int [] y = slowNeighbor(x);
				
				int deltaF = revenue(x) - revenue(y);
				if(deltaF <= 0){
					x = y;
				}else if (metropolis(deltaF, t)){
					x = y;
				}
				if(revenue(x) > revenue(bestSoFar)){
					bestSoFar = copy(x);
				}
				
				count ++;
			}
			t = t*alpha;
		}
		System.out.print("Result: ");
		print(bestSoFar);
		System.out.println("Total weight: " + weight(bestSoFar));
        System.out.println("Total revenue: " + revenue(bestSoFar));
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
	 * Return a random neighbor to the input vector, fast version
	 * which neighbor to pick is calculated and no other neighbors are saved
	 */
	private int [] neighbor(int [] vector){
		int [] cur = null;
		
		//calculate nbr of ones and zeros in vector
		int nbrZeros = 0;
		for(int i: vector){
			if(i == 0){
				nbrZeros++;
			}
		}
		int nbrOnes = SIZE - nbrZeros;
		
		//nbr of ways to add, delete and replace
		int nbrCombos = nbrZeros + nbrOnes + nbrZeros*nbrOnes; 
		
		while(cur == null){
			int rand = (int) (nbrCombos*Math.random());
		
			if(rand < nbrZeros){ //add
				int index = -1;
				int nbrZerosFound = 0;
				
				//finding index for the zero that should be turned into a one
				while(nbrZerosFound <= rand){
					index++;
					if(vector[index] == 0){
						nbrZerosFound++;
					}
				}
				cur = copy(vector);
				cur[index] = 1;
				if(weight(cur) > maxWeight){ //too heavy after add
					cur = null;
				}
				
			} else if(rand < nbrZeros+ nbrOnes){	//delete
				int index = -1;
				int nbrOnesFound = 0;
				
				//finding index for the one that should be turned into a zero
				while(nbrOnesFound <= rand-nbrZeros){
					index++;
					if(vector[index] == 1){
						nbrOnesFound++;
					}
				}
				cur = copy(vector);
				cur[index] = 0;
				
			}else{	//replace
				int nbrZerosFound = 0;
				int nbrOnesFound = 0;
				int zeroIndex = -1;
				int oneIndex = -1;
				
				//finding index for the zero that should be turned into a one
				int zeroToReplace = (rand-nbrZeros-nbrOnes)/nbrOnes;
				while(nbrZerosFound <= zeroToReplace){
					zeroIndex++;
					if(vector[zeroIndex] == 0){
						nbrZerosFound++;				
					}
				}
				
				//finding index for the one that should be turned into a zero
				int oneToReplace = (rand-nbrZeros-nbrOnes)%nbrOnes;
				while(nbrOnesFound <= oneToReplace){
					oneIndex++;
					if(vector[oneIndex] == 1){
						nbrOnesFound++;
					}
				}
				cur = copy(vector);
				cur[zeroIndex] = 1;
				cur[oneIndex] = 0;
				if(weight(cur) > maxWeight){//to heavy after replacement
					cur = null;
				}
			}
		}
		return cur;
	}	
	/*
	 * Return a random neighbor to the input vector, slow version
	 * where all possible neighbors are calculated and placed in a list
	 */
	private int [] slowNeighbor(int [] vector){
		List<int []> neighborList = new ArrayList<int []>();
		int [] cur = copy(vector);
		
		//add
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 0){
				cur[i] = 1;
				if(weight(cur) <= maxWeight){
					neighborList.add(cur);
				}
				cur = copy(vector);
			}
		}
		
		//delete
		for(int i = 0; i < SIZE; i++){
			if(vector[i] == 1){
				cur[i] = 0;
				neighborList.add(cur);
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
							neighborList.add(cur);
							cur = copy(vector);
						}
					}
				}
			}
		}
		
		int random = (int) (neighborList.size()*Math.random());
		return neighborList.get(random);
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
		System.arraycopy(vector, 0, copy, 0, SIZE);
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
