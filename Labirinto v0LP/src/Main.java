import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class Main extends JFrame
{

   private GamePanel gp;
   
   int mousecontext = 0;

   public static Main instance;
   
   public static void main(String[] args) {
	   Main m = new Main();
	   m.init();
   }
   
   public void init() {
	     instance = this;
	     
	     setFocusable(true);
	     
	     Container c = getContentPane();
	     c.setLayout( new BorderLayout() );   

	     
	     
	     gp = new GamePanel();
	     c.add(gp, "Center");
	     
	     
	     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	     this.pack();
	     this.setResizable(false);  
	     this.setVisible(true);	     
	     //resize(805, 505);
	          
	     gp.startGame();
   }

} // end of WormChaseApplet class

