package cz.cvut.fit.krakovoj;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class knapsackProblem {
	private static List<String> files = fill_files();
	public static void main(String[] args) {
		try {
			for(String st : files){
				//knapsackHeuristic();
				//knapsackProblemBruteForce(st);
				knapsackDynamic(st);
				//fptasKnapsack("./data/knap_10.inst.dat",0.01);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> fill_files(){
		List<String> files = new ArrayList<String>();
		files.add("./data/knap_4.inst.dat");
		files.add("./data/knap_10.inst.dat");
		files.add("./data/knap_15.inst.dat");
		files.add("./data/knap_20.inst.dat");
		files.add("./data/knap_22.inst.dat");
		files.add("./data/knap_25.inst.dat");
		files.add("./data/knap_27.inst.dat");
		files.add("./data/knap_30.inst.dat");
		files.add("./data/knap_32.inst.dat");
		files.add("./data/knap_35.inst.dat");
		files.add("./data/knap_37.inst.dat");
		files.add("./data/knap_40.inst.dat");
		
		return files;
	}

	public static void knapsackHeuristic() throws IOException {
		String line;
		InputStream fis = new FileInputStream("./data/knap_10.inst.dat");
		InputStreamReader isr = new InputStreamReader(fis,
				Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		Knapsack knapsack = new Knapsack();
		long startTime, estimatedTime = 0, totalTime = 0;
		
		while ((line = br.readLine()) != null) {
			knapsack.fillKnapsack(line.split(" "));
			for (int i = 0; i < 100000; i++) {
				startTime = System.currentTimeMillis();
				knapsack.bubbleSort();
				knapsack.fillCapacity();
				estimatedTime += System.currentTimeMillis() - startTime;
			}
			estimatedTime /=100000;
			//System.out.println(knapsack.getId() +"," + estimatedTime +"," + knapsack.getSolutionCost());
			totalTime += estimatedTime;
			estimatedTime = 0;
			knapsack.clear();
		}
		System.out.println("Total avarage time is " + (totalTime/50));
		br.close();
	}

	public static void knapsackProblemBruteForce(String file) throws IOException {
		String line;
		InputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,
				Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		Knapsack knapsack = new Knapsack();
		KnapsackItem solution = new KnapsackItem();
		long startTime, estimatedTime = 0, totalTime = 0;

		while ((line = br.readLine()) != null) {
			knapsack.fillKnapsack(line.split(" "));
			for (int i = 0; i <5; i++) {
				startTime = System.nanoTime();
				knapsackProblemBruteForceRec(knapsack, 0, solution, knapsack.getTotalItemsCost());
				estimatedTime += System.nanoTime() - startTime;
			}
			estimatedTime /=5;
			//System.out.println(knapsack.getId() +"," + estimatedTime +"," + knapsack.getSolutionCost());
			totalTime += estimatedTime;
			estimatedTime = 0;
			knapsack.clear();
			solution.clear();
		}
		System.out.println(file + " " + (totalTime/50));
		br.close();
	}

	public static void knapsackProblemBruteForceRec(Knapsack knapsack,
			int item, KnapsackItem solution, int remainingCost) {
		KnapsackItem tempSolution = new KnapsackItem(solution.getCost(),
				solution.getWeight());
		if (item >= knapsack.getSize()) {
			if (solution.getCost() > knapsack.getSolutionCost()
					&& solution.getWeight() <= knapsack.getCapacity()) {
				knapsack.setSolutionCost(solution.getCost());
				knapsack.setSolutionWeight(solution.getWeight());
			}
			return;
		}
		if((tempSolution.getCost() + (remainingCost - knapsack.getItemCost(item))) > knapsack.getSolutionCost())
			knapsackProblemBruteForceRec(knapsack, item + 1, tempSolution, remainingCost - knapsack.getItemCost(item));
		solution.increaseCost(knapsack.getItemCost(item));
		solution.increaseWeight(knapsack.getItemWeight(item));
		if((solution.getCost() + (remainingCost - knapsack.getItemCost(item))) > knapsack.getSolutionCost())
			knapsackProblemBruteForceRec(knapsack, item + 1, solution, remainingCost - knapsack.getItemCost(item));
	}
	
	public static void knapsackDynamic(String inputFile) throws IOException{
		String line;
		InputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		Knapsack knapsack = new Knapsack();
		long startTime, estimatedTime = 0, totalTime = 0;
				
		while ((line = br.readLine()) != null) {			
			knapsack.fillKnapsack(line.split(" "));
			for (int i = 0; i <500; i++) {
				startTime = System.nanoTime();
				dynamicProgramming(knapsack);
				estimatedTime += System.nanoTime() - startTime;
			}
			estimatedTime /=500;
			totalTime += estimatedTime;
			knapsack.clear();
		}
		
		System.out.println(inputFile + " " + ((totalTime/50)));
		br.close();
	}
	
	/**
	 * filled array
	 * @param x_size number of columns
	 * @param y_size number of rows
	 * @return array[x_size, y_size] where all values are filled with 0
	 */
	public static int[][] filledArr(int x_size, int y_size){
		if(x_size < 0 || y_size < 0)
			return null;
		int[][] ret = new int[x_size][y_size];
		for(int i = 0; i < x_size; i++){
			for(int j = 0; j < y_size; j++){
				ret[i][j] = 0;
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param knapsack
	 */
	public static void dynamicProgramming(Knapsack knapsack){
		int[][] results = filledArr(knapsack.getSize()+1, knapsack.getCapacity()+1);
		
		for(int i = 1; i < knapsack.getSize()+1; i++){
			for(int j = 0; j < knapsack.getCapacity()+1; j++){
				if(knapsack.getItemWeight(i-1) <= j)
					results[i][j] = Math.max(results[i-1][j], results[i-1][j-knapsack.getItemWeight(i-1)] + knapsack.getItemCost(i-1));
				else
					results[i][j] = results[i-1][j];
			}	
		}
		
		knapsack.setSolutionCost(results[knapsack.getSize()][knapsack.getCapacity()]);		
	}
	
	public static void fptasKnapsack(String inputFile, double eps) throws Exception{
		String line;
		InputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		Knapsack knapsack = new Knapsack();
		
		while ((line = br.readLine()) != null) {
			knapsack.fillKnapsack(line.split(" "));
			fptasDynamic(knapsack,eps);
			System.out.println(knapsack.getSolutionCost());
			knapsack.clear();
		}
		
		br.close();
	}
	
	public static void fptasDynamic(Knapsack knapsack, double eps) throws Exception{
		int[][] results = filledArr(knapsack.getSize()+1, Knapsack.MAX_COST);
		int total_cost = knapsack.getTotalItemsCost();
		int b = knapsack.getShift(eps);
		
		if(total_cost >= Knapsack.MAX_COST)
			throw new Exception("Array is going to be out of bound.");
		
		for(int i = 1; i < results[0].length; i++)
			results[0][i] = Integer.MAX_VALUE/2;
		
		for(int i = 1; i <= knapsack.getSize(); i++){
			for(int j = 0; j <= total_cost; j++){
				if(knapsack.getItemCost(i-1) <= j){
					results[i][j] = Math.min(knapsack.getItemWeight(i-1)+results[i-1][j-knapsack.getItemCost(i-1)], results[i-1][j]);
				} else {
					results[i][j] = results[i-1][j];
				}
			}
		}
		
		for(int i = total_cost; i > 0; i-- ){
			if(results[knapsack.getSize()][i] <= knapsack.getCapacity()){
				knapsack.setSolutionCost(i);
				knapsack.setSolutionCost(knapsack.getSolutionCost() << b);
				break;
			}
		}
		
		knapsack.shiftBackItemsCost(b);		
	}

}
