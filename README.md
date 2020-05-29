Edinburgh Napier University
SET10107 Computational Intelligence coursework

Task is to evolve weights for a  MLP  (Mutli-Layer  Perceptron)  artificial  neural network  that  will be used to control a fleet of identical spacecraft. The objective is to evolve a single controller that is able to land the fleet safely regardless of their different starting positions and starting velocities.  

The objective is to implement an evolutionary algorithm (EA) to evolve the weights of the supplied neural network controller. Part of the code suplied:

ExampleEvolutionaryAlgorithm: partially implemented EA. Methods for Initialisation and Mutation are provided. You are expected to add selection, crossover and replacement operators.

ExampleHillClimber: A simple hill climber that works on a single solution.

Parameters: You can adjust the number of Hidden nodes in the Neural Network.

StartGui opens a graphical interface that allows you to train and test your implementation. 

StartNoGui includes an example of how the code can be executed without the user interface.

TODO 
You are supplied with a basic framework in Java that implements some of the basic functionality required to set up a neural network and implements a skeleton EA. At minimum you must: 
 
• Add missing evolutionary operators (selection, crossover, replacement  etc.) 
• Evaluate the performance of your algorithm(s) on the training and test scenarios given 
• Report the fitness on both the training and test sets averaged over at least 10 runs of your algorithm. 

 In addition, you may wish to: 
 • Conduct a parameter exploration to tune the algorithm you have designed 
 • Investigate the number of nodes that should be in the hidden layer 
 • Investigate the role of different operators 
 
Report added as well
