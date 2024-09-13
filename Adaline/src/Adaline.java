import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Adaline {
	static int biasindex = 2;
	
	public static void main(String[] args) {
		ArrayList<double[]> listainputs = new ArrayList<>();
		ArrayList<double[]> listaoutputs= new ArrayList<>();
		try {
			BufferedReader bfr1 = new BufferedReader(new FileReader("in_FN.txt"));
			String line = "";
			while((line=bfr1.readLine())!=null) {
				String dados[] = line.split(";");
				double d[] = new double[3];
				d[0] = Double.parseDouble(dados[0]);
				d[1] = Double.parseDouble(dados[1]);
				d[2] = Double.parseDouble(dados[2]);
				listainputs.add(d);
			}
			bfr1.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedReader bfr2 = new BufferedReader(new FileReader("out_FN.txt"));
			String line = "";
			while((line=bfr2.readLine())!=null) {
				String dados[] = line.split(";");
				double d[] = new double[3];
				d[0] = Double.parseDouble(dados[0]);
				d[1] = Double.parseDouble(dados[1]);
				d[2] = Double.parseDouble(dados[2]);
				listaoutputs.add(d);
			}
			bfr2.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		

		//double TreinamentoIn[][] = {{0,0,0},{1,0,0},{0,1,0},{0,0,1}};
		//double TreinamentoOut[][] = {{1,1,1},{0,0,0},{0,0,0},{0,0,0}};
		
		Neuronio neuronio[] = new Neuronio[3];
		for(int i = 0; i < 3;i++) {
			neuronio[i] = new Neuronio(3);
			System.out.println(""+neuronio[i].printaPesos());
		}


//		for(int i = 0; i < listainputs.size();i++) {
//			double d[] = listainputs.get(i);
//			double o[] = listaoutputs.get(i);
//			for(int n = 0; n < 3;n++) {
//				
//				double r = neuronio[n].executa(d);
//				System.out.println("In "+d[0]+","+d[1]+","+d[2]+" = "+r+" [E="+(o[n]-r+"]"));
//			}
//		}
		
		for(int epoca = 0; epoca < 1000000; epoca++ ) {
			for(int n = 0; n < 3;n++) {
				neuronio[n].zeraDW();
			}
			//System.out.println("--");
			double erroTotal = 0;
			//for(int j = 0; j < listainputs.size();j++) {
			for(int j = 0; j < 1000;j++) {
				double d[] = listainputs.get(j);
				double o[] = listaoutputs.get(j);
				for(int n = 0; n < 3;n++) {
					double r = neuronio[n].executa(d);
					
					double e = o[n] - r;
					erroTotal+=Math.abs(e);
					//System.out.println(""+e);
					neuronio[n].calculaDelta(e, d);
				}
			}
			System.out.println(""+epoca+" ErroTotal: "+erroTotal);
			for(int n = 0; n < 3;n++) {
				//System.out.println("dw "+n+" "+neuronio[n].dw[0]+" "+neuronio[n].dw[1]+" "+neuronio[n].dw[2]);
				neuronio[n].atribuiDW();
			}
			if(erroTotal==0) {
				break;
			}
		}
		
		System.out.println("----------------TREINOU-------------------");

		double erroTotal = 0;
		for(int i = 1000; i < listainputs.size();i++) {
			double d[] = listainputs.get(i);
			double o[] = listaoutputs.get(i);
			//System.out.println("-- i "+i);
			for(int n = 0; n < 3;n++) {
				double r = neuronio[n].executa(d);
				
				double e = o[n] - r;
				erroTotal+=Math.abs(e);
				//System.out.println("In "+d[0]+","+d[1]+","+d[2]+" = "+r+" [E="+e);
			}
		}
		System.out.println(" ErroTotal Conjunto De Treinamento: "+erroTotal+" - "+(1-(erroTotal/(300*3)))*100+"%");
		
		erroTotal = 0;
		Random rand = new Random();
		double d[] = new double[3];
		for(int i = 0; i < 1000000;i++) {
			d[0] = rand.nextDouble()*200-100;
			d[1] = rand.nextDouble()*200-100;
			d[2] = rand.nextDouble()*200-100;
			
			for(int n = 0; n < 3;n++) {
				double r = neuronio[n].executa(d);
				
				double e = 0;
				if(n==0) {
					e = (f1(d[0],d[1],d[2])?1.0:0.0) - r;
				}
				if(n==1) {
					e = (f2(d[0],d[1], d[2])?1.0:0.0) - r;
				}
				if(n==2) {
					e = (f3(d[0],d[1], d[2])?1.0:0.0) - r;
				}
				
				erroTotal+=Math.abs(e);
				//System.out.println("In "+d[0]+","+d[1]+","+d[2]+" = "+r+" [E="+e);
			}
			
		}
		System.out.println(" ErroTotal Conjunto De Testes: "+erroTotal+" - "+(1-(erroTotal/(1000000.0*3)))*100+"%");
		
		/*for(int epoca = 0; epoca < 100; epoca++ ) {
			for(int j = 0; j < TreinamentoIn.length;j++) {
				double r = neuronio(TreinamentoIn[j],w);
				double e = TreinamentoOut[j][0] - r;
				for(int i = 0; i < TreinamentoIn[j].length;i++) {
					dw[i] += TreinamentoIn[j][i]*e*0.1;
				}
				dw[biasindex] += 1*e*0.1;
			}
		}
		System.out.println("----------------TREINOU-------------------");
		System.out.println("W "+w[0]+" "+w[1]+" "+w[2]);
		for(int i = 0; i < TreinamentoIn.length;i++) {
			double r = neuronio(TreinamentoIn[i],w);
			System.out.println("In "+TreinamentoIn[i][0]+","+TreinamentoIn[i][1]+" = "+r+" [E="+(TreinamentoOut[i][0]-r+"]"));
		}*/
		
	}
	
	public static boolean f1(double x,double y, double z) {
		return (x*2-y*1.5+z*4-3) > 0;
	}
	public static boolean f2(double x,double y, double z) {
		return (x*3-y*2+z-9) > 0;
	}
	public static boolean f3(double x,double y, double z) {
		return (x+y*3-z*1-5) > 0;
	}

}
