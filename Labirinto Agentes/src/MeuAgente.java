import java.awt.Color;
import java.awt.Graphics2D;


public class MeuAgente extends Agente {
	
	Color color;
	double vel = 80;
	double  ang  = 0;
	
	int estado = 0;
	
	double oldx = 0;
	double oldy = 0;
	
	int timeria = 0;
	
	boolean colidiu = false;
	
	String mensagemDeVisinho = "";
	
	public MeuAgente(int x,int y, Color color) {
		// TODO Auto-generated constructor stub
		X = x;
		Y = y;
		
		this.color = color;
	}
	
	@Override
	public void SimulaSe(int DiffTime) {
		// TODO Auto-generated method stub
		timeria+=DiffTime;
		
		oldx = X;
		oldy = Y;
		colidiu = false;
		
		X+=Math.cos(ang)*vel*DiffTime/1000.0;
		Y+=Math.sin(ang)*vel*DiffTime/1000.0;
		
		//System.out.println("vel "+vel+" X "+X+" Y "+Y+" "+oldx+" "+oldy );
		
		//Colisão entre agentes
//		for(int i = 0; i < GamePanel.listadeagentes.size();i++){
//		    Agente agente = GamePanel.listadeagentes.get(i);
//		    
//		    if(agente!=this){
//			    
//			    double dax = agente.X - X;
//			    double day = agente.Y - Y;
//			    
//			    double dista = dax*dax + day*day;
//			    
//			    if(dista<400){
//			    	X = oldx;
//			    	Y = oldy;
//			    	
//			    	colidiu = true;
//			    	
//			    	break;
//			    }
//		    }
//		}
		

		
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				int bx = (int)((X-1+j)/16);
				int by = (int)((Y-1+i)/16);
				if(bx < 0 || by < 0 || bx > GamePanel.mapa.Largura || by > GamePanel.mapa.Altura || GamePanel.mapa.mapa[by][bx]==1) {
					X = oldx;
					Y = oldy;
					colidiu = true;
					break;
				}
			}
		}
		
		
	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int XMundo, int YMundo) {
		// TODO Auto-generated method stub
		dbg.setColor(color);
		
		dbg.fillOval((int)(X-6)-XMundo, (int)(Y-6)-YMundo, 12, 12);
		
		dbg.setColor(Color.white);
		double linefx = X + 6*Math.cos(ang);
		double linefy = Y + 6*Math.sin(ang);dbg.drawLine((int)X-XMundo,(int)Y-YMundo, (int)linefx-XMundo, (int)linefy-YMundo);
	
	}

	public void calculaIA(int DiffTime,int avisao[][],int avisaoMapa2[][],MeuAgente visinhos[]){
		if(mensagemDeVisinho.equals("FOCK U")) {
			ang+=Math.PI;
			mensagemDeVisinho="";
		}
		
		if(colidiu) {
			if(avisao[5][6]==0) {
				ang = 0;
				System.out.println("Direita");
			}else if(avisao[4][5]==0){
				ang = -Math.PI/2;
				System.out.println("Cima");
			}else if(avisao[5][4]==0){
				ang = Math.PI;
				System.out.println("Esquerda");
				//System.out.println(" ang "+ang+" "+X+" "+Y);
			}else if(avisao[6][5]==0){
				ang = Math.PI/2;		
				System.out.println("Baixo");
				
			}else {
				System.out.println("NADA");
			}
		}
		for(int i = 0; i < visinhos.length;i++) {
			if(visinhos[i]!=null) {
				//System.out.println("FOCK YOU");
				visinhos[i].mensagemDeVisinho = "FOCK U";
			}
		}
	}
	
}
