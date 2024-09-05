import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Mapa_Grid extends Mapa{

	public Mapa_Grid(int Largura,int Altura,int tilestelaX, int tilestelaY) {
		super(null, tilestelaX, tilestelaY);
		this.Altura = Altura;
		this.Largura = Largura;
		
		mapa = new int[Altura][Largura];
	}
	
	public void loadmapfromimage(String filename){
		BufferedImage imagem = null;
		try {
			BufferedImage imgtmp = ImageIO.read(this.getClass().getResourceAsStream(filename));
			imagem = new BufferedImage(imgtmp.getWidth(), imgtmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
			imagem.getGraphics().drawImage(imgtmp, 0, 0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.Altura = imagem.getWidth();
		this.Largura = imagem.getHeight();
		
		mapa = new int[Altura][Largura];
		
		WritableRaster rast = imagem.getRaster();
		DataBuffer buf = rast.getDataBuffer();
		
		int cor = buf.getElem(0);
		for(int j = 0; j < imagem.getHeight();j++){
			for(int i = 0; i < imagem.getWidth();i++){
				int cor1 = buf.getElem(imagem.getWidth()*j+i);
				if(cor==cor1){
					mapa[j][i] = 0;
				}else{
					mapa[j][i] = 1;
				}
			}
		}
	}

	@Override
	public void DesenhaSe(Graphics2D dbg) {
		// TODO Auto-generated method stub
		
    	int offx = MapX&0x0f; 
    	int offy = MapY&0x0f;
    	int somax,somay;
    	if(offx>0){
    		somax = 1;
    	}else{
    		somax = 0;
    	}
    	if(offy>0){
    		somay = 1;
    	}else{
    		somay = 0;
    	}
    	
    	dbg.setColor(Color.LIGHT_GRAY);
    	
        for(int j = 0; j < NumeroTilesY + somay; j++){            
        	dbg.drawLine(0,(j<<4)-offy,NumeroTilesX*16,(j<<4)-offy);
        }
        for(int i = 0; i < NumeroTilesX + somax; i++){
        	dbg.drawLine((i<<4)-offx,0,(i<<4)-offx,NumeroTilesY*16);
        }       
        
        dbg.setColor(Color.black);
        for(int j = 0; j < NumeroTilesY + somay; j++){            
            for(int i = 0; i < NumeroTilesX + somax; i++){
                if(mapa[j+(MapY>>4)][i+(MapX>>4)]>0){
                	dbg.fillRect((i<<4)-offx,(j<<4)-offy,16,16);
                }
            }
        }
	}
}
