import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample shows how to create, train, save and load simple Multi Layer
 * Perceptron
 */
public class MainTrabBanco {
	
	public static void main(String[] args) {
		boolean treinamento = true;
		
		DataSet trainingSet = new DataSet(48, 1);
		
		try {
			FileReader csvDataSet = new FileReader("Bank_TrainNew_in.txt");
			BufferedReader bfr = new BufferedReader(csvDataSet);
			
			String line = "";
			while((line=bfr.readLine())!=null) {
				String split[] = line.split("\t");
				//System.out.println(""+split.length);
				
				double input[] = new double[48];
				for(int i = 0; i < 48;i++) {
					input[i] = Double.parseDouble(split[i]);
				}
				
				double out = Double.parseDouble(split[48]);
				trainingSet.addRow(new DataSetRow(input, new double[] { out }));			
			}
			csvDataSet.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(treinamento) {
			// create multi layer perceptron
			MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 48, 48,24,12,1);
			myMlPerceptron.randomizeWeights();
			
			BackPropagation learningrules = new BackPropagation();
			learningrules.setMaxIterations(100); 
			learningrules.setMaxError(0.00001);
			learningrules.addListener(new LearningEventListener() {	
				@Override
				public void handleLearningEvent(LearningEvent arg0) {
					if(arg0.getEventType()==LearningEvent.Type.EPOCH_ENDED) {
						BackPropagation bp = (BackPropagation)arg0.getSource();
						System.out.println("EPOCA "+bp.getCurrentIteration()+" "+bp.getTotalNetworkError());
					}
				}
			});
			myMlPerceptron.learn(trainingSet, learningrules);
			
			//testNeuralNetwork(myMlPerceptron, trainingSet);
			// save trained neural network
			myMlPerceptron.save("myMlPerceptron.nnet");
		}


		
		
		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		// test loaded neural network
		
		
		// test perceptron
		System.out.println("TRAINING SET");
		testNeuralNetworkScore(loadedMlPerceptron, trainingSet);
		
		
		DataSet testSet = new DataSet(48, 1);
		
		try {
			FileReader csvDataSet = new FileReader("Bank_Test.txt");
			BufferedReader bfr = new BufferedReader(csvDataSet);
			
			String line = "";
			while((line=bfr.readLine())!=null) {
				String split[] = line.split("\t");
				//System.out.println(""+split.length);
				
				double input[] = new double[48];
				for(int i = 0; i < 48;i++) {
					input[i] = Double.parseDouble(split[i]);
				}
				
				double out = Double.parseDouble(split[48]);
				testSet.addRow(new DataSetRow(input, new double[] { out }));			
			}
			csvDataSet.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// test perceptron
		System.out.println("TEST SET");
		testNeuralNetworkScore(loadedMlPerceptron, testSet);
		
	}

	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
		}
	}
	
	public static void testNeuralNetworkScore(NeuralNetwork nnet, DataSet testSet) {
		int i = 0;
		double somaerro = 0;
		
		int mConfusao[][] = new int[2][2];
		
		int totalPositivos = 0;
		
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			
			
			double outReal = dataRow.getDesiredOutput()[0];
			double oputRede = networkOutput[0];
			somaerro += Math.abs(networkOutput[0]-outReal);
			i++;
			
			int realI = outReal>0.5?1:0;
			int redeI = oputRede>0.5?1:0;
			
			totalPositivos+=realI;
			
			mConfusao[realI][redeI]++;
			
		}
		System.out.println("Erro Total "+somaerro);
		
		System.out.println(" Matriz Confus�o");
		System.out.println("\t pV \t pF");
		System.out.println("rV\t"+mConfusao[1][1]+"\t"+ mConfusao[1][0]);
		System.out.println("rF\t"+mConfusao[0][1]+"\t"+ mConfusao[0][0]);
		
		double acerto = (1-(mConfusao[0][1]/(double)(mConfusao[1][1]+mConfusao[0][1])))*100.0;
		
		System.out.println("Acerto Positivo: "+acerto+"%");
		
		double precisao = mConfusao[1][1]/(double)(mConfusao[1][1]+mConfusao[1][0]);
		double revocacao = mConfusao[1][1]/(double)(mConfusao[1][1]+mConfusao[0][1]);
		double acuracia = (mConfusao[1][1]+mConfusao[0][0])/(double)(mConfusao[1][1]+mConfusao[1][0]+mConfusao[0][1]+mConfusao[0][0]);
		double f1 = 2*((precisao*revocacao)/(double)(precisao+revocacao));
		
		System.out.println("precisao: "+precisao);
		System.out.println("revocacao: "+revocacao);
		System.out.println("acuracia: "+acuracia);
		System.out.println("f1: "+f1);
		
		// 10 - 100
		double faturamentoBurro = totalPositivos*100;
		double custoBurro = testSet.size()*10;
		double lucroBurro = faturamentoBurro - custoBurro;
		System.out.println("BURRO F: "+faturamentoBurro+" C:"+custoBurro+" L:"+lucroBurro);
		
		double faturamentoIA = mConfusao[1][1]*100;
		double custoIA = (mConfusao[1][1]+mConfusao[0][1])*10;
		double lucroIA = faturamentoIA - custoIA;
		System.out.println("IA F: "+faturamentoIA+" C:"+custoIA+" L:"+lucroIA);
	}	

}