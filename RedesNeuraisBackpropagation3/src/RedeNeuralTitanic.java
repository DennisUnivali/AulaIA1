import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class RedeNeuralTitanic {
	Random rnd = new Random();
	
	
	public static void main(String[] args) {
		DecimalFormat df2 = new DecimalFormat( "0.00000000" );
		DataSet trainingSet = new DataSet(10, 1);
		DataSet testSet = new DataSet(10, 1);
		ArrayList<String[]> listaDadosBrutos = new ArrayList<>();
		
		
		float faremax = 0;
		float faremin = Float.MAX_VALUE;
		
		try {
			FileReader fr = new FileReader("treinamento_titanic.csv"); 
			BufferedReader bfr = new BufferedReader(fr);
			String line = bfr.readLine();
			String spl[] = line.split(";");
			for(int i = 0; i < spl.length;i++) {
				System.out.println("["+i+"] "+spl[i]);
			}

			while((line=bfr.readLine())!=null) {
				spl = line.split(";");
				String pclass = spl[0];
				String sex = spl[1];
				String age = spl[2];
				String sibsp = spl[3];
				String parch = spl[4];
				float fare = 0;
				try {
					fare = Float.parseFloat(spl[5]);
				}catch (Exception e) {
				}
				
				if(fare>faremax) {
					faremax = fare;
				}
				if(fare<faremin) {
					faremin = fare;
				}
				
				String embarked = spl[6];
				String live = spl[7];
				
				listaDadosBrutos.add(spl);
			
				//System.out.println(""+line);
			}
			
			System.out.println("FaraMin "+faremin+" FareMax "+faremax);
			
			for(int i = 0;i < listaDadosBrutos.size();i++) {
				double data[] = new double[10]; 
				spl = listaDadosBrutos.get(i);
				float pclass = (Integer.parseInt(spl[0]))/3.0f; //1
				data[0] = (double)pclass;
				
				String sex = spl[1]; //2
				if(sex.equals("male")) {
					data[1] = 1;
					data[2] = 0;
				}else {
					data[1] = 0;
					data[2] = 1;
				}
				
				double age = 30;
				try {
					Double.parseDouble(spl[2]);//1
				}catch (Exception e) {

				}
				data[3] = age/100.0;
				float sibsp = (Integer.parseInt(spl[3]))/8.0f;
				data[4] = sibsp;
				float parch = (Integer.parseInt(spl[4]))/9.0f;
				data[5] = parch;
				float fare = 0;
				try {
					fare = Float.parseFloat(spl[5]);
				}catch (Exception e) {
				}
				data[6] = fare/faremax;
				
				String embarked = spl[6];
				data[7] = embarked.equals("C")?1:0;
				data[8] = embarked.equals("Q")?1:0;
				data[9] = embarked.equals("S")?1:0;
				
				double out[] = new double[1];
				int live = Integer.parseInt(spl[7]);
				out[0] = live;
				
				trainingSet.addRow(new DataSetRow(data, out));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			FileReader fr = new FileReader("teste_titanic.csv"); 
			BufferedReader bfr = new BufferedReader(fr);
			String line = bfr.readLine();
			String spl[] = line.split(";");
			for(int i = 0; i < spl.length;i++) {
				System.out.println("["+i+"] "+spl[i]);
			}

			while((line=bfr.readLine())!=null) {
				spl = line.split(";");
				double data[] = new double[10]; 
				float pclass = (Integer.parseInt(spl[0]))/3.0f; //1
				data[0] = (double)pclass;
				
				String sex = spl[1]; //2
				if(sex.equals("male")) {
					data[1] = 1;
					data[2] = 0;
				}else {
					data[1] = 0;
					data[2] = 1;
				}
				
				double age = 30;
				try {
					Double.parseDouble(spl[2]);//1
				}catch (Exception e) {

				}
				data[3] = age/100.0;
				float sibsp = (Integer.parseInt(spl[3]))/8.0f;
				data[4] = sibsp;
				float parch = (Integer.parseInt(spl[4]))/9.0f;
				data[5] = parch;
				float fare = 0;
				try {
					fare = Float.parseFloat(spl[5]);
				}catch (Exception e) {
				}
				data[6] = fare/faremax;
				
				String embarked = spl[6];
				data[7] = embarked.equals("C")?1:0;
				data[8] = embarked.equals("Q")?1:0;
				data[9] = embarked.equals("S")?1:0;
				
				double out[] = new double[1];
				int live = Integer.parseInt(spl[7]);
				out[0] = live;
				
				testSet.addRow(new DataSetRow(data, out));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Vai Trainar");

		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 10, 20, 1);
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxIterations(10000); 
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
		testNeuralNetwork(myMlPerceptron, trainingSet,"Matriz Confusao Treinamento");
		// save trained neural network
		System.out.println("Save Perceptron");
		myMlPerceptron.save("myMlPerceptron.nnet");
		// load saved neural network
		
		System.out.println("Load Perceptron");
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		// test loaded neural network
		
		testNeuralNetwork(myMlPerceptron, testSet,"Matriz Confusao Teste");
		

	}

	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet,String info) {
		int matrizDeConfusao[][] = new int[2][2];
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			int saidaDaRede = networkOutput[0]>=0.5?1:0;
			int saidaDesejada = dataRow.getDesiredOutput()[0]>=0.5?1:0;
			matrizDeConfusao[saidaDesejada][saidaDaRede]++;
		}
		System.out.println(info);
		System.out.println(""+matrizDeConfusao[1][1]+"\t"+matrizDeConfusao[1][0]);
		System.out.println(""+matrizDeConfusao[0][1]+"\t"+matrizDeConfusao[0][0]);
		
		double precisao = matrizDeConfusao[1][1]/(double)(matrizDeConfusao[1][1]+matrizDeConfusao[1][0]);
		double revocacao = matrizDeConfusao[1][1]/(double)(matrizDeConfusao[1][1]+matrizDeConfusao[0][1]);
		double acuracia = (matrizDeConfusao[1][1]+matrizDeConfusao[0][0])/(double)(matrizDeConfusao[1][1]+matrizDeConfusao[1][0]+matrizDeConfusao[0][1]+matrizDeConfusao[0][0]);
		double f1 = 2*((precisao*revocacao)/(double)(precisao+revocacao));
		System.out.println("precisao "+precisao);
		System.out.println("revocacao "+revocacao);
		System.out.println("acuracia "+acuracia);
		System.out.println("f1 "+f1);
		
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
			
			if(angulo%18==0) {
				dbg.setColor(Color.BLUE);
				dbg.drawLine((int)(olposx1+2), (int)(250+sin*200), (int)(olposx2+2), (int)(250+networkOutput[0]*200));
			}
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