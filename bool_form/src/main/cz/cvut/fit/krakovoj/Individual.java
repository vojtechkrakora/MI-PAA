/**
 * 
 */
package cz.cvut.fit.krakovoj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author krakovoj@fit.cvut.cz
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public class Individual {
	private List<Integer> chromosome = new ArrayList<Integer>();
	
	public Individual(int length){
		if(length < 0){
			System.err.println("Warning: Individual size should be equal or more than 0." +
					"Required lenth '" + length + "' is changed to 0.");
			length = 0;
		}
		for(int i = 0; i < length;i++){
			chromosome.add(0);
		}
	}
	
	public Individual(Individual inputIndividual){
		for(int i = 0; i < inputIndividual.size(); i++){
			try {
				chromosome.add(inputIndividual.getChromosomeElem(i));
			} catch (Exception e) {
				System.err.println("Chromosome was fulfilled prematurely:");
				e.printStackTrace();
				return;
			}
		}
	}
	
	public int getChromosomeElem(int position) throws Exception{
		if(position >= chromosome.size()){
			throw new IndexOutOfBoundsException("Position is bigger than chromosome length.");
		}
		return chromosome.get(position);
	}
	
	public void setRandomChromosome(){
		Random rand = new Random();
		
		if(chromosome.size() < 1){
			System.err.println("Warning: Chromosome length is " + chromosome.size() +" so it" +
					" can not be randomised.");
			return;
		}
		
		for(int i = 0; i < chromosome.size(); i++){		
			chromosome.set(i,rand.nextInt(Integer.MAX_VALUE) % 2);	
		}
	}
	
	public void mutate(double mutationProbability){
		Random rand = new Random();
		
		for(int i = 0; i < chromosome.size();i++){
			if(java.lang.Double.compare(rand.nextDouble(), mutationProbability) <= 0){
				if(chromosome.get(i) == 1){
					chromosome.set(i,0);
				} else {
					chromosome.set(i,1);
				}
			}
		}
	}
	
	public int getChromosomeLength(){
		return chromosome.size();
	}
	
	public Individual clone(){
		return new Individual(this);
	}
}
