package coursework;

import java.util.Arrays;
import model.StringIO;

import model.Fitness;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Example of how to to run the {@link ExampleEvolutionaryAlgorithm} without the need for the GUI
 * This allows you to conduct multiple runs programmatically 
 * The code runs faster when not required to update a user interface
 *
 */
public class StartNoGui {

	public static void main(String[] args) {
		/**
		 * Train the Neural Network using our Evolutionary Algorithm 
		 * 
		 */

		//Set the parameters here or directly in the Parameters Class
		Parameters.maxEvaluations = 20000; // Used to terminate the EA after this many generations
		Parameters.popSize = 40; // Population Size

		//number of hidden nodes in the neural network
		Parameters.setHidden(5);
		
		//Set the data set for training 
		//Parameters.setDataSet(DataSet.Training);
		
		
		
		//Create a new Neural Network Trainer Using the above parameters 
		//NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
		
		//train the neural net (Go and have a coffee)
		//to test will run permistation of all operators: 4 selection, 4 replacement, 4 mutation and 4 reproduction(crossover)
		//will run each 10 times and print average fitness
		//will run on Training and Test data
		String[] selection = {"tournament", "random", "roulette", "elitist"};
		String[] crossover = {"onePoint", "twoPoint", "uniform", "arithmetic"}; 
		String[] mutation = {"inversion", "swap", "creep", "standart"};
		String[] replacement = {"random", "restrictedTS", "best", "deterministic"};

		double sumFitnessTraining = 0, sumFitnessTest = 0;
		double[] fitnesesT = new double[10];
		double[] fitnesesTest = new double[10];
		for(int i = 0; i < 10; i++) {
			Parameters.setDataSet(DataSet.Training);
			NeuralNetwork nn = new ExampleEvolutionaryAlgorithm(); 
			nn.run();
			sumFitnessTraining += nn.best.fitness;
			fitnesesT[i] += nn.best.fitness;
			Parameters.setDataSet(DataSet.Test); 
			double fitness = Fitness.evaluate(nn);
			sumFitnessTest += fitness;
			fitnesesTest[i] += fitness;
			System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness);
		}
		double meanTraining = sumFitnessTraining / 5, meanTest = sumFitnessTest/5;
		System.out.println("Mean training: " + meanTraining + ", mean test: " + meanTest);
		for(int i =0; i < 10; i++) {
			System.out.println("Best traing: " + fitnesesT[i] + "\n");
			System.out.println("Best test: " + fitnesesT[i]+ "\n");
		}
		
		
	/*	for(String sel : selection) { 
			Parameters.selection = sel; 
			for(String cross : crossover) { 
				Parameters.reproduction = cross; 
				for(String mut : mutation) {
				Parameters.mutation = mut; 
				for(String repl : replacement) {
					Parameters.replacement = repl; 
					str = "Test: selection - " + sel +
							", reproduction - " + cross + ", mutation -  " + mut + ", replacement -  " + repl + " "; 
					System.out.println(str); 
					StringIO.writeStringToFile(filePrefix +".txt", str, true); 
					NeuralNetwork nn = new ExampleEvolutionaryAlgorithm(); 
					nn.run();
					str = "Best: " + nn.best.toString();
					StringIO.writeStringToFile(filePrefix + ".txt", str, true);
					}
				}
			}
		}
 */


		 
		
		
		
		
		/**
		 * The last File Saved to the Output Directory will contain the best weights /
		 * Parameters and Fitness on the Training Set 
		 * 
		 * We can used the trained NN to Test on the test Set
		 */
		/*
		 * Parameters.setDataSet(DataSet.Test); double fitness = Fitness.evaluate(nn);
		 * System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness);
		 */
		
		/**
		 * Or We can reload the NN from the file generated during training and test it on a data set 
		 * We can supply a filename or null to open a file dialog 
		 * Note that files must be in the project root and must be named *-n.txt
		 * where "n" is the number of hidden nodes
		 * ie  1518461386696-5.txt was saved at timestamp 1518461386696 and has 5 hidden nodes
		 * Files are saved automatically at the end of training
		 *  
		 */

		
	}
}
