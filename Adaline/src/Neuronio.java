
public class Neuronio {
	int nentradas = 1;
	double w[];
	double dw[];
	double fatorDeTreinamento = 0.001;
	
	public Neuronio(int nentradas) {
		this.nentradas = nentradas;
		w = new double[nentradas+1];
		dw = new double[nentradas+1];
		for(int i = 0; i < nentradas+1;i++) {
			w[i] = (Math.random()*20)-10;
		}
	}
	
	public double executa(double in[]) {
		double soma = 0;
		for(int i = 0; i < in.length;i++) {
			soma += in[i]*w[i];
		}
		soma += 1*w[nentradas];
		if(soma>0) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public void zeraDW() {
		for(int i = 0; i < nentradas+1;i++) {
			dw[i] = 0;
		}
	}
	public void calculaDelta(double e,double entradas[]) {
		for(int i = 0; i < entradas.length;i++) {
			dw[i] += entradas[i]*e*fatorDeTreinamento;
		}
		dw[nentradas] += 1*e*fatorDeTreinamento;
	}
	
	public void atribuiDW() {
		for(int i = 0; i < nentradas+1;i++) {
			//System.out.println(""+dw[i]);
			w[i]+=dw[i];
		}
	}
	
	public String printaPesos() {
		String s = "";
		for(int i = 0; i < w.length;i++) {
			s+= ""+w[i]+" ";
		}
		return s;
	}
}
