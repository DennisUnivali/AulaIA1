import java.text.DecimalFormat;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class XorMultiLayerPerceptronSample {
	
	public static void main(String[] args) {
		// create training set (logical XOR function)
		DataSet trainingSet = new DataSet(2, 1);
		trainingSet.addRow(new DataSetRow(new double[] { 0, 0 }, new double[] { 0 }));
		trainingSet.addRow(new DataSetRow(new double[] { 0, 1 }, new double[] { 1 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 0 }, new double[] { 1 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 1 }, new double[] { 0 }));
		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 4, 1);
		// learn the training set
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxError(0.000001);
		myMlPerceptron.learn(trainingSet, learningrules);
		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myMlPerceptron, trainingSet);
		// save trained neural network
		myMlPerceptron.save("myMlPerceptron.nnet");
		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		// test loaded neural network
		System.out.println("Testing loaded neural network");
		testNeuralNetwork(loadedMlPerceptron, trainingSet);
	}

	public static DecimalFormat df = new DecimalFormat("0.000000");

	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + "["+df.format(networkOutput[0])+"]");//Arrays.toString(networkOutput));
		}
	}

}