package coursework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.scene.Parent;

import java.lang.*;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	

	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();
		
		
		//Record a copy of the best Individual in the population
		best = getBest();

		/**
		 * main EA processing loop
		 */		
		
		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */
		//ArrayList<Individual> newPopulation = new ArrayList<Individual>();
			
		
			// Select 2 Individuals from the current population. Tournament selection 
			Individual parent1; 
			Individual parent2;
			switch(Parameters.selection) {
			case "tournament":
				parent1 = selectionTournament(); 
				parent2 = selectionTournament();
				break;
			case "roulette":
				parent1 = selectionRoulette(); 
				parent2 = selectionRoulette();
				break;
			case "elitist":
				Collections.sort(population);
				parent1 = population.get(0);
				parent2 = population.get(1);
				break;
			default:
				parent1 = selectionRandom();
				parent2 = selectionRandom();
			}
			
			// Generates two children 
			ArrayList<Individual> children;
			switch(Parameters.reproduction) {
			case "onePoint":
				children = reproduceOnePoint(parent1, parent2);
				break;
			case "twoPoint":
				children = reproduceTwoPoint(parent1, parent2);
				break;
			case "arithmetic":
				children = reproduceArithmetic(parent1, parent2);
				break;
			default:
				children = reproduceUniform(parent1, parent2);
			}
						
			//mutate the offspring
			switch(Parameters.mutation) {
			case "inversion":
				mutationInversion(children);
				break;
			case "swap":
				mutationSwap(children);
				break;
			case "creep":
				mutationCreep(children);
				break;
			default: 
				mutate(children);
			}
			
			// Evaluate the children
				evaluateIndividuals(children);			
				
			// Replace children in population 
			switch(Parameters.replacement) {
			case "deterministic":
				replaceDeterministic(children, parent1, parent2);
				break;
			case "restrictedTS":
				replaceRestrictedTS(children);
				break;
			case "best":
				replaceBest(children, parent1, parent2);
				break;
			default:
				replaceWorst(children);
			}
			
			// check to see if the best has improved
			best = getBest();
			// Implemented in NN class. 
			//outputStats();			
			//Increment number of completed generations			
		}
		//save the trained network to disk
		saveNeuralNetwork();
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}
	
	//return the distance between individuals by adding absolute difference of chromosome values
	private double getDistance(Individual indi1, Individual indi2)
	{
		double distance = 0;
		for(int i = 0; i < indi1.chromosome.length; i++)
		{
			distance += Math.abs(indi1.chromosome[i] - indi2.chromosome[i]);
		}
		return distance;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 * 
	 * 
	 * 
	 */
	private Individual selectionRandom() {
		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		return parent.copy();
	}
	
	//tournament selection - 
	private Individual selectionTournament() {
	
		ArrayList<Individual> tournament = new ArrayList<Individual>();
		
		for(int i = 0; i < Parameters.tournamentSize; i++)
		{
			tournament.add(population.get(Parameters.random.nextInt(Parameters.popSize)));
		}
		
		Collections.sort(tournament);
		
		return tournament.get(0);
	}
	
	//roulette selection:
	private Individual selectionRoulette() {
		
		double fitnessAll = 0;
		for(Individual individual : population)
		{
			fitnessAll += individual.fitness;
		}
		
		double random = fitnessAll * Parameters.random.nextDouble();
		
		for(Individual individual : population) {
			random -= 1 - individual.fitness;
			if(random < 0)
				return individual;
		}
		return population.get(population.size()-1);
	}
	

	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 * 
	 * replaced by one point crossover
	 */
	private ArrayList<Individual> reproduceOnePoint(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		int cutPoint = Parameters.random.nextInt(parent1.chromosome.length);
	
		for(int i = 0; i < parent1.chromosome.length; i++)
		{
			if(i < cutPoint) {
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			}
			else {
				child1.chromosome[i] = parent2.chromosome[i];
				child2.chromosome[i] = parent1.chromosome[i];
			}
		}

		children.add(child1);
		children.add(child2);			
		return children;
	}
	
	//two point crossover
	private ArrayList<Individual> reproduceTwoPoint(Individual parent1, Individual parent2){
		ArrayList<Individual> children = new ArrayList<>();
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		int length = parent1.chromosome.length;
		int cutPoint1 = Parameters.random.nextInt(length);
		int cutPoint2 = Parameters.random.nextInt(length);
		
		for (int i = 0; i < length; i++){
			if(i < cutPoint1 || i >= cutPoint2){
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			} else {
		    child1.chromosome[i] = parent2.chromosome[i];
		    child2.chromosome[i] = parent1.chromosome[i];
			}
		}
		
		children.add(child1);
		children.add(child2);	
		return children;
	}
	
	//simple arithmetic crossover
	private ArrayList<Individual> reproduceArithmetic(Individual parent1, Individual parent2){
		ArrayList<Individual> children = new ArrayList<>();
		int cutPoint = Parameters.random.nextInt(parent1.chromosome.length);
		Individual child1 = new Individual();
		Individual child2 = new Individual();

			for (int i = cutPoint; i < parent1.chromosome.length; i++){ 
				child1.chromosome[i] = (parent1.chromosome[i] * Parameters.aritmetic) + (parent2.chromosome[i] * (1-Parameters.aritmetic));
			}
			children.add(child1);
			
			for (int i = cutPoint; i < parent1.chromosome.length; i++){
				child2.chromosome[i] = (parent1.chromosome[i] * (1-Parameters.aritmetic)) + (parent2.chromosome[i] * Parameters.aritmetic);
			}
			children.add(child1);
			children.add(child2);
		
		return children;
	}
	
	//uniform crossover
	private ArrayList<Individual> reproduceUniform(Individual parent1, Individual parent2){
		ArrayList<Individual> children = new ArrayList<>();
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		for (int i = 0; i < parent1.chromosome.length; i++){
			if(Parameters.random.nextBoolean()){
				child1.chromosome[i] = parent1.chromosome[i];
				child2.chromosome[i] = parent2.chromosome[i];
			} else {
				child1.chromosome[i] = parent2.chromosome[i];
				child2.chromosome[i] = parent1.chromosome[i];
			}
		}

		children.add(child1);
		children.add(child2);	
		return children;
	}
	
	/**
	 * Mutation
	 * 
	 * 
	 */
	private void mutate(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}
	//inversion mutation: selected random start and end points and inverse all genes in between
	private void mutationInversion(ArrayList<Individual> children) {				
		for(Individual child : children) {
			if(Parameters.random.nextDouble() < Parameters.mutateRate) {
				//random inversion start and end points
				int startPoint = Parameters.random.nextInt(child.chromosome.length);
				int endPoint = startPoint + Parameters.random.nextInt(child.chromosome.length - startPoint);
				
				double[] chromosomeTemp = new double[endPoint-startPoint+1];
				int l = 0;
				for(int i = startPoint; i < endPoint+1; i++) {
					chromosomeTemp[l] = child.chromosome[i];
					l++;
				}
				double[] reverseTemp = new double[endPoint-startPoint+1]; 
		        int j = endPoint-startPoint;
		        //to reverse the positions of gene values
		        for (int i = 0; i < j; i++) { 
		            reverseTemp[j] = chromosomeTemp[i]; 
		            j = j - 1; 
		        }
		        int k = 0;
		        for(int i = startPoint; i < endPoint; i++) {
					child.chromosome[i] = reverseTemp[k];
					k++;
		        }
			}
		}
	}
	
	//swap mutation: swap two random alleles
	private void mutationSwap(ArrayList<Individual> children) {
		for(Individual child : children) {
			if(Parameters.random.nextDouble() < Parameters.mutateRate) {
				int swap1 = Parameters.random.nextInt(child.chromosome.length);
				int swap2 = Parameters.random.nextInt(child.chromosome.length);
				double tempAllele = child.chromosome[swap1];
				child.chromosome[swap1] = child.chromosome[swap2];
				child.chromosome[swap2] = tempAllele;
			}
		}
	}
	
	//creep mutation: random gene is mutated to random value between gene min and max
	private void mutationCreep(ArrayList<Individual> children) {
		for(Individual child : children) {
			if(Parameters.random.nextDouble() < Parameters.mutateRate) {
				int alleleToMutate = Parameters.random.nextInt(child.chromosome.length);
				//new random value scaled to desired range by multiplying by 6 (3+|-3|) and shifted to get negative values
				double newValue = Parameters.random.nextDouble() * 6 - 3;
				child.chromosome[alleleToMutate] = newValue;
			}
		}
	}
	
	/**
	 * 
	 * Replaces the worst member of the population 
	 * (regardless of fitness)
	 * 
	 */
	private void replaceWorst(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}
	}
		
	// Tried to do deterministic crowding replacement: evaluates by similarity (distance from each other) and fitness
	private void replaceDeterministic(ArrayList<Individual> children, Individual parent1, Individual parent2) {
		Individual child1 = children.get(0); 
		Individual child2 = children.get(1);
		population.remove(parent1);
		population.remove(parent2);
		  
		//if first p1, c1 and p2,c2 are more similar, they are competing, otherwise p1,c2 and p2,c1 
		  if(getDistance(parent1,child1) + getDistance(parent2, child2) <= getDistance(parent1,child2) + getDistance(parent2, child1)) {
			  if(child1.compareTo(parent1) == -1)
				  population.add(child1);
			  else
				  population.add(parent1);
			  if(child2.compareTo(parent2) == -1) 
				  population.add(child2);
			  else
				  population.add(parent2);
		  }
			  
		  else {
			  if(child1.compareTo(parent2) == -1)
				  population.add(child1);
			  else
				population.add(parent2);
				
			  if(child2.compareTo(parent1) == -1)
				  population.add(child2);
			  else
				  population.add(parent1);
			  }
	}
	
	//restricted tournament selection replacement, same tournament size as for tournament selection
	private void replaceRestrictedTS(ArrayList<Individual> children) {

		for(Individual child : children) {
			//make a tournament of random individuals
			ArrayList<Individual> tournament = new ArrayList<Individual>();
			for(int i = 0; i < Parameters.tournamentSize; i++)
			{
				tournament.add(population.get(Parameters.random.nextInt(Parameters.popSize)));
			}
			
			//set first tournament members as most similar
			Individual mostSimilar = tournament.get(0);
			double minDist = getDistance(child, mostSimilar);
			
			//compare all tournament members to find the most similar one
			for(int i = 1; i < tournament.size(); i++)
			{
				if(getDistance(tournament.get(i), child) < minDist) {
					mostSimilar = tournament.get(i);
					minDist = getDistance(tournament.get(i), child);
				}
			}
			//if the child is fitter than most similar member, replace it
			if(child.fitness < mostSimilar.fitness)
				population.set(population.indexOf(mostSimilar), child);
		}
	}
	
	//Replaces parents only if children are fitter: 
	private void replaceBest(ArrayList<Individual> children, Individual parent1, Individual parent2) {
		children.add(parent2);
		children.add(parent1);
		population.remove(parent1);
		population.remove(parent2);
		
		Collections.sort(children);
		Individual best = children.get(0);
		Individual secondBest = children.get(1);
		
		population.add(best);
		population.add(secondBest);
	}	 

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
