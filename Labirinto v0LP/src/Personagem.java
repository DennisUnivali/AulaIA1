import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Personagem extends Agente{

	BufferedImage AnimeSet;
	
	int frame;
	int timeranimacao;
	int animacao;
	int tempoentreframes;
	
	int velocidade = 0;
	int velx,vely;
	
	double ang = 0;
	
	boolean segueobjetivo = false;
	double objetivoX = 0;
	double objetivoY = 0;
	
	int sizeX = 24;
	int sizeY = 32;
	
	public Personagem(BufferedImage _AnimeSet) {
		// TODO Auto-generated constructor stub
		AnimeSet = _AnimeSet;
		frame = 0;
		animacao = 0;
		timeranimacao = 0;
		
		velx = 0;
		vely = 0;	
		
		tempoentreframes = 200;
	}
	
	@Override
	public void DesenhaSe(Graphics2D dbg,int XMundo,int YMundo) {
		// TODO Auto-generated method stub
		dbg.drawImage(AnimeSet,(int)X-XMundo,(int)Y-YMundo, (int)(X+sizeX)-XMundo, (int)(Y+sizeY)-YMundo, sizeX*frame, sizeY*animacao, (sizeX*frame)+sizeX, (sizeY*animacao)+sizeY, null);
	}

	@Override
	public void SimulaSe(int DiffTime) {
		// TODO Auto-generated method stub	
	}

}
