import java.awt.Color;
import java.awt.Graphics2D;


public class MeuAgente extends Agente {
	
	Color color;
	double vel = 40;
	double  ang  = 0;
	
	int estado = 0;
	
	double oldx = 0;
	double oldy = 0;
	
	int timeria = 0;
	
	boolean colidiu = false;
	
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
		
		if(timeria>100){
			calculaIA(DiffTime);
			timeria = 0;
		}
		
		X+=Math.cos(ang)*vel*DiffTime/1000.0;
		Y+=Math.sin(ang)*vel*DiffTime/1000.0;
		
		for(int i = 0; i < GamePanel.listadeagentes.size();i++){
		    Agente agente = GamePanel.listadeagentes.get(i);
		    
		    if(agente!=this){
			    
			    double dax = agente.X - X;
			    double day = agente.Y - Y;
			    
			    double dista = dax*dax + day*day;
			    
			    if(dista<400){
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
		
		dbg.drawOval((int)(X-10)-XMundo, (int)(Y-10)-YMundo, 20, 20);
		
		double linefx = X + 10*Math.cos(ang);
		double linefy = Y + 10*Math.sin(ang);dbg.drawLine((int)X-XMundo,(int)Y-YMundo, (int)linefx-XMundo, (int)linefy-YMundo);
	
	}

	public void calculaIA(int DiffTime){
		
//		if(colidiu==true){
//			if(GamePanel.rnd.nextInt(2)==0){
//				ang = ang + (Math.PI/2);
//			}else{
//				ang = ang - (Math.PI/2);
//			}
//			colidiu = false;
//			return;
//		}
//		
//		double dx = GamePanel.mousex - X;
//		double dy = GamePanel.mousey - Y;
//		
//		double dist = dx*dx + dy*dy;
//		
//		if(dist<169){
//			estado = 1;
//			ang = Math.atan2(dy, dx)+Math.PI-(Math.PI/4)+((Math.PI/2)*GamePanel.rnd.nextDouble());
//		}
//		if(dist > 40000){
//			estado = 0;
//		}
//		
//		if(estado == 0){
//			ang = Math.atan2(dy, dx);
//		}
		
		vel = 0;
		
	}
	
}
