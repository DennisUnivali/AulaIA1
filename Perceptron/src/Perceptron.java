import java.util.Random;

public class Perceptron {
	static int biasindex = 2;
	
	public static void main(String[] args) {
		Random rnd = new Random();
		double TreinamentoIn[][] = {{0,0},{0,1},{1,0},{1,1}};
		double TreinamentoOut[][] = {{1},{0},{0},{1}};
		
		//double TreinamentoIn[][] = {{0,0},{0,1},{1,0},{1,1}};
		//double TreinamentoOut[][] = {{1},{0},{0},{1}};
		
		double w[] = new double[3];
		w[0] = (rnd.nextDouble()*2)-1; 
		w[1] = (rnd.nextDouble()*2)-1;
		w[2] = (rnd.nextDouble()*2)-1;
		
		
		System.out.println("W "+w[0]+" "+w[1]+" "+w[2]);
		
		for(int i = 0; i < TreinamentoIn.length;i++) {
			double r = neuronio(TreinamentoIn[i],w);
			System.out.println("In "+TreinamentoIn[i][0]+","+TreinamentoIn[i][1]+" = "+r+" [E="+(TreinamentoOut[i][0]-r+"]"));
		}
		
		double passotreimanto = 0.01;
		for(int epoca = 0; epoca < 1000; epoca++ ) {
			double dw[] = new double[3];
			double wold[] = new double[3];
			for(int i = 0; i < w.length;i++) {
				wold[i] = w[i];
			}
			for(int j = 0; j < TreinamentoIn.length;j++) {
				double r = neuronio(TreinamentoIn[j],w);
				double e = TreinamentoOut[j][0] - r;
				for(int i = 0; i < TreinamentoIn[j].length;i++) {
					dw[i] += TreinamentoIn[j][i]*e*passotreimanto;
				}
				dw[biasindex] += 1*e*0.1;
				for(int i = 0; i < TreinamentoIn[0].length;i++) {
					w[i] += dw[i];
				}
				//w[biasindex] += dw[biasindex];
				//System.out.println("Epoca "+epoca+" W "+w[0]+" "+w[1]+" "+w[2]);
			}
			boolean naomudou = true;
			for(int i = 0; i < w.length;i++) {
				if(wold[i]!=w[i]) {
					naomudou = false;
					break;
				}
			}
			if(naomudou) {
				break;
			}
		}
		
		
		
		
		System.out.println("----------------TREINOU-------------------");
		System.out.println("W "+w[0]+" "+w[1]+" "+w[2]);
		for(int i = 0; i < TreinamentoIn.length;i++) {
			double r = neuronio(TreinamentoIn[i],w);
			System.out.println("In "+TreinamentoIn[i][0]+","+TreinamentoIn[i][1]+" = "+r+" [E="+(TreinamentoOut[i][0]-r+"]"));
		}
		
	}
	
	public static double neuronio(double in[],double w[]) {
		double soma = 0;
		for(int i = 0; i < in.length;i++) {
			soma += in[i]*w[i];
		}
		soma += 1*w[biasindex];
		if(soma>0) {
			return 1;
		}else {
			return 0;
		}
	}
}
