
public class VariavelFuzzy {
	String nome;
	float b1,t1,t2,b2;
	
	public VariavelFuzzy() {
	}
	
	public VariavelFuzzy(String nome, float b1, float t1, float t2, float b2) {
		super();
		this.nome = nome;
		this.b1 = b1;
		this.t1 = t1;
		this.t2 = t2;
		this.b2 = b2;
	}
	
	public float fuzzifica(float v) {
		if(v<b1) {
			return 0;
		}
		if(v>b2) {
			return 0;
		}
		
		if(v>=t1&&v<=t2) {
			return 1;
		}
		
		if(v>b1&&v<t1) {
			float p = (v-b1)/(t1-b1);
			return p;
		}
		
		if(v>t2&&v<b2) {
			float p = 1.0f-((v-t2)/(b2-t2));
			return p;
		}
		
		return 0;
	}
	
}
