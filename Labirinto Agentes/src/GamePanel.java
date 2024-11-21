import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class GamePanel extends Canvas implements Runnable {
	private static final int PWIDTH = 960;
	private static final int PHEIGHT = 800;
	private Thread animator;
	private boolean running = false;
	private boolean gameOver = false;

	int FPS, SFPS;
	int fpscount;

	public static Random rnd = new Random();

//BufferedImage imagemcharsets;

	boolean LEFT, RIGHT, UP, DOWN;

	public static int mousex, mousey;

	public static ArrayList<Agente> listadeagentes = new ArrayList<Agente>();

	public static Mapa_Grid mapa;

	double posx, posy;

//MeuAgente meuHeroi = null;

//TODO ESSE È O RESULTADO
	int caminho[] = null;

	float zoom = 1;

	int ntileW = 60;
	int ntileH = 50;

	HashSet<Integer> nodosVisitados = new HashSet<>();

	public GamePanel() {

		setBackground(Color.white);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		// create game components
		setFocusable(true);

		requestFocus(); // JPanel now receives key events

		// Adiciona um Key Listner
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (keyCode == KeyEvent.VK_LEFT) {
					LEFT = true;
				}
				if (keyCode == KeyEvent.VK_RIGHT) {
					RIGHT = true;
				}
				if (keyCode == KeyEvent.VK_UP) {
					UP = true;
				}
				if (keyCode == KeyEvent.VK_DOWN) {
					DOWN = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (keyCode == KeyEvent.VK_LEFT) {
					LEFT = false;
				}
				if (keyCode == KeyEvent.VK_RIGHT) {
					RIGHT = false;
				}
				if (keyCode == KeyEvent.VK_UP) {
					UP = false;
				}
				if (keyCode == KeyEvent.VK_DOWN) {
					DOWN = false;
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				mousex = e.getX();
				mousey = e.getY();

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					int mousex = (int) ((e.getX() + mapa.MapX) / zoom);
					int mousey = (int) ((e.getY() + mapa.MapY) / zoom);

					int mx = mousex / 16;
					int my = mousey / 16;

					if (mx > mapa.Altura) {
						return;
					}
					if (my > mapa.Largura) {
						return;
					}

					mapa.mapa[my][mx] = 1;
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				// System.out.println(" "+arg0.getButton());
				int mousex = (int) ((arg0.getX() + mapa.MapX) / zoom);
				int mousey = (int) ((arg0.getY() + mapa.MapY) / zoom);

				// System.out.println(""+arg0.getX()+" "+mapa.MapX+" "+zoom);
				// System.out.println(""+mousex+" "+mousey);

				int mx = mousex / 16;
				int my = mousey / 16;

				if (mx > mapa.Altura) {
					return;
				}
				if (my > mapa.Largura) {
					return;
				}

				if (arg0.getButton() == 3) {
					if (mapa.mapa[my][mx] == 0) {
						mapa.mapa2[my][mx] += 200;
					}
				}
				if (arg0.getButton() == 1) {
					if (mapa.mapa[my][mx] == 0) {
						caminho = null;
						long timeini = System.currentTimeMillis();

						System.out.println("" + my + " " + mx);

						// buscaEmLArgura(mx,my);

						// mapa.mapa[my][mx]

						long timefin = System.currentTimeMillis() - timeini;
						System.out.println("Tempo Final: " + timefin);
					} else {
						System.out.println("Caminho Final Bloqueado");
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// System.out.println("w "+e.getWheelRotation());
				if (e.getWheelRotation() > 0) {
					zoom = zoom * 1.1f;
				} else if (e.getWheelRotation() < 0) {
					zoom = zoom * 0.90f;
				}

				ntileW = (int) ((960 / zoom) / 16) + 1;
				ntileH = (int) ((800 / zoom) / 16) + 1;

				if (ntileW >= 1000) {
					ntileW = 1000;
				}
				if (ntileH >= 1000) {
					ntileH = 1000;
				}
				mapa.NumeroTilesX = ntileW;
				mapa.NumeroTilesY = ntileH;
			}
		});

//	try {
//		imagemcharsets = ImageIO.read( getClass().getResource("Chara1.png") );
//	}
//	catch(IOException e) {
//		System.out.println("Load Image error:");
//	}

		// meuHeroi = new MeuAgente(10, 10, Color.blue);

		// listadeagentes.add(meuHeroi);

		mousex = mousey = 0;

		mapa = new Mapa_Grid(100, 100, ntileW, ntileH);
		mapa.loadmapfromimage("/imagemlabirinto1000.png");

		for (int i = 0; i < 40; i++) {
			Color cor = Color.black;

			switch (rnd.nextInt(4)) {
			case 0:
				cor = Color.red;
				break;
			case 1:
				cor = Color.BLUE;
				break;
			case 2:
				cor = Color.green;
				break;

			default:
				break;
			}

			int bx = 0;
			int by = 0;

			do {
				bx = rnd.nextInt(100);
				by = rnd.nextInt(100);
			} while (mapa.mapa[by][bx] == 1);

			listadeagentes.add(new MeuAgente((bx * 16) + 8, (by * 16) + 8, cor));
		}

	} // end of GamePanel()

	public void startGame()
// initialise and start the thread
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()

	public void stopGame()
// called by the user to stop execution
	{
		running = false;
	}

	public void run()
	/* Repeatedly update, render, sleep */
	{
		running = true;

		long DifTime, TempoAnterior;

		int segundo = 0;
		DifTime = 0;
		TempoAnterior = System.currentTimeMillis();

		this.createBufferStrategy(2);
		BufferStrategy strategy = this.getBufferStrategy();

		while (running) {

			gameUpdate(DifTime); // game state is updated
			Graphics g = strategy.getDrawGraphics();
			gameRender((Graphics2D) g); // render to a buffer
			strategy.show();

			try {
				Thread.sleep(0); // sleep a bit
			} catch (InterruptedException ex) {
			}

			DifTime = System.currentTimeMillis() - TempoAnterior;
			TempoAnterior = System.currentTimeMillis();

			if (segundo != ((int) (TempoAnterior / 1000))) {
				FPS = SFPS;
				SFPS = 1;
				segundo = ((int) (TempoAnterior / 1000));
			} else {
				SFPS++;
			}

		}
		System.exit(0); // so enclosing JFrame/JApplet exits
	} // end of run()

	int timerfps = 0;

	private void gameUpdate(long DiffTime) {

		if (LEFT) {
			posx -= 1000 * DiffTime / 1000.0;
		}
		if (RIGHT) {
			posx += 1000 * DiffTime / 1000.0;
		}
		if (UP) {
			posy -= 1000 * DiffTime / 1000.0;
		}
		if (DOWN) {
			posy += 1000 * DiffTime / 1000.0;
		}

		if (posx > mapa.Largura * 16) {
			posx = mapa.Largura * 16;
		}
		if (posy > mapa.Altura * 16) {
			posy = mapa.Altura * 16;
		}
		if (posx < 0) {
			posx = 0;
		}
		if (posy < 0) {
			posy = 0;
		}

		mapa.Posiciona((int) posx, (int) posy);

		for (int i = 0; i < listadeagentes.size(); i++) {
			MeuAgente oAgente = (MeuAgente) listadeagentes.get(i);
			int avisao[][] = calculaVisao(oAgente);
			MeuAgente visinhos[] = new MeuAgente[10];
			int avisaoMapa2[][] = calculaVisaoMapa2(oAgente,avisao,visinhos);
			oAgente.calculaIA((int) DiffTime,avisao,avisaoMapa2,visinhos);
			oAgente.SimulaSe((int) DiffTime);
		}
	}

	int visao[][] = new int[11][11];
	int visaoMapa2[][] = new int[11][11];

	public int[][] calculaVisao(MeuAgente oAgente) {
		int bx = (int) (oAgente.X / 16);
		int by = (int) (oAgente.Y / 16);

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				int bbx = bx - 5 + j;
				int bby = by - 5 + i;
				if (bbx < 0 || bby < 0 || bbx > mapa.Largura || bby > mapa.Altura) {
					visao[i][j] = -1;
				} else {
					visao[i][j] = mapa.mapa[bby][bbx];
				}
			}
		}
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				if (visao[i][j] == 0) {
					int dx = j - 5;
					int dy = i - 5;
					if (dx == 0 && dy == 0) {
						continue;
					}
					int npassos = (((int) Math.sqrt(dx*dx + dy*dy))*2) + 8;
					double passox = (dx * 16) / (double) npassos;
					double passoy = (dy * 16) / (double) npassos;
					double xinicial = j*16+8;
					double yinicial = i*16+8;
					for (int p = 0; p < npassos; p++) {
						xinicial -= passox;
						yinicial -= passoy;
						int bpx = (int) (xinicial / 16);
						int bpy = (int) (yinicial / 16);
						//System.out.println("bpx "+bpx+" bpy "+bpy+" passox "+passox+" passoy "+passoy+" npassos "+npassos+" dx "+dx+" dy "+dy);
						if (visao[bpy][bpx] == 1) {
							visao[i][j] = -1;
							break;
						}
						if (bpx == 0 && bpy == 0) {
							break;
						}
					}
				}
			}
		}
		return visao;
	}
	public int[][] calculaVisaoMapa2(MeuAgente oAgente,int avisao[][], MeuAgente visinhos[]) {
		int bx = (int) (oAgente.X / 16);
		int by = (int) (oAgente.Y / 16);

		int vcount = 0;
		
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				int bbx = bx - 5 + j;
				int bby = by - 5 + i;
				if (avisao[i][j]==0)
				{
					visaoMapa2[i][j] = mapa.mapa2[bby][bbx];
					
					for(int ai = 0; ai < listadeagentes.size();ai++) {
						MeuAgente possivelVisinho = (MeuAgente)listadeagentes.get(ai);
						if(vcount>=10) {
							break;
						}
						if(possivelVisinho==oAgente) {
							continue;
						}
						int bvx = (int) (possivelVisinho.X / 16);
						int bvy = (int) (possivelVisinho.Y / 16);
						if(bvx==bbx&&bvy==bby) {
							visinhos[vcount] = possivelVisinho;
							vcount++;
						}
					}
					
				}else {
					visaoMapa2[i][j] = -1;
				}
			}
		}
		return visaoMapa2;
	}

	Color corPath = new Color(70, 70, 255);
	Color corVisao = new Color(0.0f,0.9f, 0.2f, 0.2f);
	Color corVisaoBlock = new Color(0.9f,0.0f, 0.2f, 0.4f);
	Color corPlayer= new Color(0.1f,0.0f, 0.9f, 0.2f);

	private void gameRender(Graphics2D dbg)
// draw the current frame to an image buffer
	{
		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

		AffineTransform trans = dbg.getTransform();
		dbg.scale(zoom, zoom);

		try {
			mapa.DesenhaSe(dbg);
		} catch (Exception e) {
			System.out.println("Erro ao desenhar mapa");
		}
		synchronized (nodosVisitados) {
			if (nodosVisitados.size() > 0) {

				for (Iterator iterator = nodosVisitados.iterator(); iterator.hasNext();) {
					Integer nodoint = (Integer) iterator.next();

					int nx = nodoint % mapa.Largura;
					int ny = nodoint / mapa.Largura;

					dbg.setColor(Color.red);
					dbg.fillRect(nx * 16 - mapa.MapX, ny * 16 - mapa.MapY, 16, 16);
				}
			}
		}

		if (caminho != null) {

			try {
				if (caminho != null) {
					for (int i = 0; i < caminho.length / 2; i++) {
						int nx = caminho[i * 2];
						int ny = caminho[i * 2 + 1];

						dbg.setColor(corPath);
						dbg.fillRect(nx * 16 - mapa.MapX, ny * 16 - mapa.MapY, 16, 16);
					}
				}
			} catch (Exception e) {
			}
		}

		for (int i = 0; i < listadeagentes.size(); i++) {
			MeuAgente oAgente = (MeuAgente) listadeagentes.get(i);
			oAgente.DesenhaSe(dbg, mapa.MapX, mapa.MapY);
			int avisao[][] = calculaVisao(oAgente);
			int bx = (int) (oAgente.X / 16);
			int by = (int) (oAgente.Y / 16);
			for(int iy = 0; iy < 11;iy++) {
				for(int ix = 0; ix < 11;ix++) {
					if(avisao[iy][ix]!=-1) {
						int nx = bx-5+ix;
						int ny = by-5+iy;
						
						if(avisao[iy][ix]==1) {
							dbg.setColor(corVisaoBlock);
						}else {
							dbg.setColor(corVisao);
						}
						if(iy==5&&ix==5) {
							dbg.setColor(corPlayer);
						}
						dbg.fillRect(nx*16-mapa.MapX, ny*16-mapa.MapY, 16, 16);
					}
				}
			}
		}

		dbg.setTransform(trans);

		dbg.setColor(Color.BLUE);
		dbg.drawString("FPS: " + FPS, 10, 10);

		// System.out.println("left "+LEFT);

	}

}
