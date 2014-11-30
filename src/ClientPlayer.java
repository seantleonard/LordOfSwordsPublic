import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientPlayer extends JPanel implements Runnable{
	private static final long serialVersionUID = -3929233179764541207L;
	BufferedImage imgPlayerSkeletonF = null;
	BufferedImage imgPlayerColorF = null;
	BufferedImage imgPlayerArmorF = null;
	BufferedImage imgPlayerArmF = null;
	BufferedImage imgPlayerSkeletonL = null;
	BufferedImage imgPlayerColorL = null;
	BufferedImage imgPlayerArmorL = null;
	BufferedImage imgPlayerArmL = null;
	BufferedImage imgPlayerSkeletonR = null;
	BufferedImage imgPlayerColorR = null;
	BufferedImage imgPlayerArmorR = null;
	BufferedImage imgPlayerArmR = null;
	BufferedImage imgPlayerSkeletonD = null;
	BufferedImage imgPlayerColorD = null;
	BufferedImage imgPlayerArmorD = null;
	BufferedImage imgPlayerArmD = null;
	
	BufferedImage forward[] = new BufferedImage[4];
	BufferedImage left[]=new BufferedImage[4];
	BufferedImage right[]=new BufferedImage[4];
	BufferedImage down[]=new BufferedImage[4];
	private ClientApplication myApp;
	private ClientDrawingPanel drawPanel;
	private int xSquare;
	private int ySquare;
	private int xPixel;
	private int yPixel;
	//private int quadrant=9;
	private int direction=2;
	private int currentQuadrant=9;
	private String teamColor;
	
	public ClientPlayer(ClientApplication myApp, String teamColor, ClientDrawingPanel drawPanel){
		this.myApp = myApp;
		this.drawPanel = drawPanel;
		this.teamColor= teamColor;
		DrawKeyListener ls = new DrawKeyListener();
		addKeyListener(ls);
		setVisible(true);
		setLayout(null);
		
		xPixel=0;
		yPixel=0;
		xSquare =0;
		ySquare =0;
		try {
			forward[0] = ImageIO.read(new File("images/playerSkeleton/facing_forward.png"));
			forward[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_forward.png"));
			forward[2]  = ImageIO.read(new File("images/armor/basic/facing_forward.png"));
			forward[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_forward.png"));
			left[0] = ImageIO.read(new File("images/playerSkeleton/facing_left.png"));
			left[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_left.png"));
			left[2] = ImageIO.read(new File("images/armor/basic/facing_left.png"));
			left[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_left.png"));
			right[0] = ImageIO.read(new File("images/playerSkeleton/facing_right.png"));
			right[1]= ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_right.png"));
			right[2] = ImageIO.read(new File("images/armor/basic/facing_right.png"));
			right[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_right.png"));
			down[0] = ImageIO.read(new File("images/playerSkeleton/facing_backwards.png"));
			down[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_backwards.png"));
			down[2] = ImageIO.read(new File("images/armor/basic/facing_backwards.png"));
			down[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_backwards.png"));
		} catch (IOException e) {}
	}
	
	public void run(){
		while(true){
			
			//constantly checking the x and y coordinates to be able to update the position of the character on the 
			//grid map model. FOr instance, if a player is in the bounds x 
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	public void setPlayerChange(String armorType, String weaponType ){
		try{
			/*forward[2]  = ImageIO.read(new File("images/armor/"+armorType+"/facing_forward.png"));
			forward[3] = ImageIO.read(new File("images/arms/"+weaponType+"/still/facing_forward.png"));
			
			left[2] = ImageIO.read(new File("images/armor/"+armorType+"/facing_left.png"));
			left[3] = ImageIO.read(new File("images/arms/"+weaponType+"/still/facing_left.png"));
			
			right[2] = ImageIO.read(new File("images/armor/"+armorType+"/facing_right.png"));
			right[3] = ImageIO.read(new File("images/arms/"+weaponType+"/still/facing_right.png"));
			
			
			down[2] = ImageIO.read(new File("images/armor/"+armorType+"/facing_backwards.png"));
			down[3] = ImageIO.read(new File("images/arms/"+weaponType+"/still/facing_backwards.png"));*/
			
			forward[2]  = ImageIO.read(new File(armorType));
			forward[3] = ImageIO.read(new File(weaponType));
			
			left[2] = ImageIO.read(new File(armorType));
			left[3] = ImageIO.read(new File(weaponType));
			
			right[2] = ImageIO.read(new File(armorType));
			right[3] = ImageIO.read(new File(weaponType));
			
			
			down[2] = ImageIO.read(new File(armorType));
			down[3] = ImageIO.read(new File(weaponType));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void setX(int x){
		this.xPixel=x;
	}
	
	public void setY(int y){
		this.yPixel=y;
	}
	
	public void setXSq(int x){
		this.xSquare=x;
	}
	
	public void setYSq(int y){
		this.ySquare=y;
	}
	
	public void setQuadrant(int q){
		this.currentQuadrant=q;
	}
	
	public void setDirection(int d){
		this.direction=d;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(direction==0)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(down[i], xPixel, yPixel,null);
			}
		}
		else if(direction==1)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(right[i], xPixel, yPixel,null);
			}
		}
		else if(direction==2)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(forward[i], xPixel, yPixel,null);
			}
		}
		else if(direction==3)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(left[i], xPixel, yPixel,null);
			}
		}
	}
	
	

	class DrawKeyListener extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			
			//Check the potential new position. If it is in a different square
			//
			
			//if the new potential position is within 5 of a border, then check. Set quad change boolean to true; 
			
			if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
				if( (((450-xPixel) < 6) && currentQuadrant == 3) || (((450-xPixel) < 6) && currentQuadrant == 1)){
					//DO NOTHING, too close to the right border.
				}else if( (((225-xPixel) < 6 ) && currentQuadrant == 0) || (((225-xPixel) < 6 ) && currentQuadrant == 2)){
					myApp.sendServerMessage("R:5");
					drawPanel.drawQuadChange = true;
				}
		
				else{
					  //Right arrow key code
					//SEND THE SERVER THE MESSAGE R	
					if( ((xPixel+22.5+5)/45) != xSquare){
						myApp.sendServerMessage("R:5");
			            //Up arrow key code
						direction = 1;
						xPixel+=5;					
					}else{
						direction = 1;
						xPixel+=5;	
					}
				}
	          
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
				if( ((xPixel < 6) && currentQuadrant == 0) || ((xPixel < 6) && currentQuadrant == 2) ){
					//DO NOTHING, too close to the right border.
				}else if( (( (xPixel-225) < 6) && currentQuadrant==1) || (((xPixel-225) < 6) && currentQuadrant==3)  ){
					myApp.sendServerMessage("L:5");
				}
				
				else{
					if( ((xPixel+22.5+5)/45) != xSquare){
						myApp.sendServerMessage("L:5");
			            //Up arrow key code
						direction = 3;
						xPixel-=5;					
					}else{
						direction = 3;
						xPixel-=5;	
					}
				}
				
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				
				if( ((yPixel < 6) && currentQuadrant == 0) || ((yPixel < 6) && currentQuadrant == 1) ){
					//do nothing 
				}else if( (( (yPixel-225) < 6) && currentQuadrant==2) || (((yPixel-225) < 6) && currentQuadrant==3)  ){
					myApp.sendServerMessage("U:5");
				}
				
				else{
					if( ((yPixel+22.5+5)/45) != ySquare){
						myApp.sendServerMessage("U:5");
			            //Up arrow key code
						direction = 0;
						yPixel-=5;					
					}else{
						direction = 0;
						yPixel-=5;	
					}
				}
				
				
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN ) {
				
				if( (((450-yPixel) < 6) && currentQuadrant == 2) || (((450-yPixel) < 6) && currentQuadrant == 3)){
					//DO NOTHING, too close to the right border.
				}else if( (( (225-yPixel) < 6) && currentQuadrant==0) || (((225-yPixel) < 6) && currentQuadrant==1)  ){
					myApp.sendServerMessage("D:5");
				}
				
				else{
					if( ((yPixel+22.5+5)/45) != ySquare){
						myApp.sendServerMessage("D:5");
			            //Up arrow key code
						direction = 2;
						yPixel+=5;	
						//ySquare = (yPixel+22.5)/45;
					}else{
						direction = 2;
						yPixel+=5;	
					}
				}
				
			} else if (e.getKeyCode() == KeyEvent.VK_A ) {
				//ATTACK
				myApp.sendServerMessage("A:10");

			}
		}
		
	}
}