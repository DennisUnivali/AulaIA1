import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample shows how to create, train, save and load simple Multi Layer
 * Perceptron
 */
public class SinCosPerceptronSample {
	
	public static void main(String[] args) {
		DecimalFormat df2 = new DecimalFormat( "0.00000000" );

		DataSet trainingSet = new DataSet(2, 1);
		
		try {
			FileWriter csvDataSet = new FileWriter("SinTREINAMENTO.csv");

			Random rnd = new Random();
			
			for(int i = 0; i < 20;i++) {
				double angulo = rnd.nextInt(360);
				double in = angulo/360.0;
				double out = Math.sin(Math.toRadians(angulo));
				trainingSet.addRow(new DataSetRow(new double[] { in,1 }, new double[] { out }));
				csvDataSet.write(""+in+";"+out+"\n");
			}
			csvDataSet.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Vai Trainar");

		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 10,10, 1);
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxIterations(1000000); 
		learningrules.setLearningRate(0.002);
		learningrules.setMaxError(0.000000001);
		learningrules.addListener(new LearningEventListener() {
			@Override
			public void handleLearningEvent(LearningEvent arg0) {
				//System.out.println(""+arg0.getEventType());
				//if(arg0.getEventType()) {
				if(learningrules.getCurrentIteration()%1000==0) {
					System.out.println(" "+learningrules.getCurrentIteration()+" "+df2.format(learningrules.getTotalNetworkError()));
				}
			}
		});
		myMlPerceptron.learn(trainingSet, learningrules);
		
		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myMlPerceptron, trainingSet);
		// save trained neural network
		System.out.println("Save Perceptron");
		myMlPerceptron.save("myMlPerceptron.nnet");
		// load saved neural network
		
		System.out.println("Load Perceptron");
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		// test loaded neural network
		
		DataSet testgSet = new DataSet(2, 1);
		
		try {
			FileWriter csvTESTE = new FileWriter("SinTESTE.csv");

			for(int i = 0; i < 360;i++) {
				double angulo = (1*i); 
				double in = angulo/360.0;
				double out = Math.sin(Math.toRadians(angulo));
				testgSet.addRow(new DataSetRow(new double[] { in,1 }, new double[] { out }));
				csvTESTE.write(""+in+";"+out+"\n");
			}
			csvTESTE.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Testing loaded neural network");
		testNeuralNetworkScore(loadedMlPerceptron, testgSet,trainingSet);
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
	
	public static void testNeuralNetworkScore(NeuralNetwork nnet, DataSet testSet, DataSet trainingSet) {
		int i = 0;
		double somaerro = 0;
		
		BufferedImage img = new BufferedImage(800,500,BufferedImage.TYPE_INT_ARGB);
		Graphics2D dbg = (Graphics2D)img.getGraphics();
		
		dbg.setColor(Color.white);
		dbg.fillRect(0,0,800,500);
		
		float olposx1 = 10;
		float olposy1 = 250;
		
		float olposx2 = 10;
		float olposy2 = 250;
		
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			//System.out.print("Input: " + Arrays.toString(dataRow.getInput())+" ANG "+i*5);
//			System.out.println(""+dataRow.getInput()[0]+";"+dataRow.getInput()[1]+";"+);
			int angulo = i*1;
			double sin = Math.sin(Math.toRadians(angulo));
			//System.out.println(" Output: " + Arrays.toString(networkOutput)+" SIN "+sin+" ERRO "+(networkOutput[0]-sin));
			
			System.out.println(""+dataRow.getInput()[0]+";"+dataRow.getInput()[1]+";"+(i*5)+";"+networkOutput[0]+";"+sin);
			
			somaerro += Math.abs(networkOutput[0]-sin);
			i++;
			
			dbg.setColor(Color.BLACK);
			dbg.drawLine((int)olposx1, (int)olposy1, (int)(olposx1+2), (int)(250+sin*200));
			dbg.setColor(Color.RED);
			dbg.drawLine((int)olposx2, (int)olposy2, (int)(olposx2+2), (int)(250+networkOutput[0]*200));
			olposx1 = olposx1+2;
			olposy1 = (float)(250+sin*200);
			
			olposx2 = olposx2+2;
			olposy2 = (float)(250+networkOutput[0]*200);
			
//			if(angulo%18==0) {
//				dbg.setColor(Color.BLUE);
//				dbg.drawLine((int)(olposx1+2), (int)(250+sin*200), (int)(olposx2+2), (int)(250+networkOutput[0]*200));
//			}
		}
		for (DataSetRow dataRow : trainingSet.getRows()) {
			Double valorSaida = dataRow.getDesiredOutput()[0];
			Double valorEntrada = dataRow.getInput()[0];
			double angulo = valorEntrada*360;
			dbg.setColor(new Color(0,128,0));
			dbg.fillRect((int)(10+angulo*2)-1, (int)(250+valorSaida*200)-1,3,3);
		}
		
		
		System.out.println("Erro Total "+somaerro);
		try {
			ImageIO.write(img,"PNG",new File("SaidaSeno.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}	

}