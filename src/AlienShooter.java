import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.glu.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class AlienShooter extends JFrame implements GLEventListener {
	GLCanvas canvas;
	Animator an;
	
	
	///////////////////////// array list to store each bullet, polygon and stars /////////////////////////
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Polygon> polygons = new ArrayList<>();
	ArrayList<stars> star = new ArrayList<>();
	  
	
	private GLUT glut =  new GLUT();
	
	
	
	///////////////////////// used for spaceship movement /////////////////////////
	boolean moveRight = false;
	boolean moveLeft = false;
	
	
	
	///////////////////////// level counter /////////////////////////
	int level = 1;
	
	
	
	///////////////////////// a counter for cheking how many aliens have ben killed /////////////////////////
	int alien_counter = 0;
	
	
	
	
	///////////////////////// used to change the alien scales the level goes up 0.3 and 0.6 are default///////////////////////
	private double scaleX = 0.3;
	private double scaleY = 0.6;
	
	
	
	
	
	
	class maps {
		ArrayList<Polygon> map1 = new ArrayList<>();

		public maps(int level) {
			if (level == 1) {
				scaleX = 0.3;
				scaleY = 0.6;
				map1.removeAll(map1);
				map1.add(new Polygon(450, 400));
				map1.add(new Polygon(300, 400));
				map1.add(new Polygon(150, 400));
				map1.add(new Polygon(0, 400));
				map1.add(new Polygon(-450, 400));
				map1.add(new Polygon(-300, 400));
				map1.add(new Polygon(-150, 400));  
				
				
				map1.add(new Polygon(375, 270));
				map1.add(new Polygon(225, 270));
				map1.add(new Polygon(75, 270));
				map1.add(new Polygon(-75, 270));
				map1.add(new Polygon(-225, 270));
				map1.add(new Polygon(-375, 270));
				
				
				map1.add(new Polygon(300, 120));
				map1.add(new Polygon(150, 120));
				map1.add(new Polygon(0, 120));
				map1.add(new Polygon(-300, 120));
				map1.add(new Polygon(-150, 120));
				
				map1.add(new Polygon(75, 0));
				map1.add(new Polygon(-75, 0));
			
			map1.add(new Polygon(0, -120));
				
				polygons.addAll(map1);
			} else if (level == 2) {
				polygons.removeAll(map1);
				scaleX = 0.3;
				scaleY = 0.5 ;
				map1.add(new Polygon(0, 0));
				map1.add(new Polygon(0, 280));
				map1.add(new Polygon(0, 130));
				map1.add(new Polygon(0, 400));
				
				map1.add(new Polygon(130, 190));
				map1.add(new Polygon(250, 190));
				map1.add(new Polygon(370, 190));
				
				map1.add(new Polygon(-130, 190));
				map1.add(new Polygon(-250, 190));
				map1.add(new Polygon(-370, 190));
				polygons.addAll(map1);
			} else  if(level == 3) {
				polygons.removeAll(map1);
				scaleX = 0.2;
				scaleY = 0.4 ;
				map1.add(new Polygon(0, 0));
				map1.add(new Polygon(100, 100));
				map1.add(new Polygon(-100, 100));
				map1.add(new Polygon(200, 200));
				map1.add(new Polygon(-200, 200));
				map1.add(new Polygon(300, 300));
				map1.add(new Polygon(-300, 300));
				map1.add(new Polygon(400, 400));
				map1.add(new Polygon(-400, 400));
				
				map1.add(new Polygon(0, 100));
				map1.add(new Polygon(100, 200));
				map1.add(new Polygon(-100, 200));
				map1.add(new Polygon(0, 200));
				map1.add(new Polygon(0, 300));
				map1.add(new Polygon(0, 400));
				polygons.addAll(map1);
			}else if(level == 4) {
				polygons.removeAll(map1);
				scaleX = 0.4;
				scaleY = 0.7 ;
				map1.add(new Polygon(0, 0));
				map1.add(new Polygon(100, 100));
				map1.add(new Polygon(-100, 100));
				map1.add(new Polygon(200, 200));
				map1.add(new Polygon(-200, 200));
				map1.add(new Polygon(300, 300));
				map1.add(new Polygon(-300, 300));
				map1.add(new Polygon(400, 400));
				map1.add(new Polygon(-400, 400));
				
				polygons.addAll(map1);
			}else{
				AlienShooter.this.level = 1;
				new maps(1);
				
			}
		}

	}

	
	
	
	
	public AlienShooter() {
		super("Space Ship");
		canvas = new GLCanvas();
		add(canvas);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				handleKeyRelease(e);
			}
		});
		an = new Animator(canvas);
		canvas.requestFocus();
		an.start();
		setSize(1080, 1000);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		///////////////////////// Create Player Polygon/////////////////////////
		polygons.add(new Polygon(-10,-480,10,-470));
		new maps(this.level);//Generate Default Level is 1
		genarateStars();//Genarate random 4 region stars

	}
	
	
	
	
	
	/////////////////////////Key Events/////////////////////////

	void handleKeyPress(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == ' ') {
			bulletSound();
			fireBullet();
		} else if (key == 'a' || key == 'A') {
			moveLeft = true;
		} else if (key == 'd' || key == 'D') {
			moveRight = true;
		}
	}

	void handleKeyRelease(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == 'a' || key == 'A') {
			moveLeft = false;
		} else if (key == 'd' || key == 'D') {
			moveRight = false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public void init(GLAutoDrawable arg0) {
	    GL2 gl = arg0.getGL().getGL2();
	    GLU glu = new GLU();
	    gl.glClearColor(0.5f, 0.0f, 0.78f, 0.0f);
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    glu.gluOrtho2D(-540, 540, -500, 500);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glEnable(GL2.GL_BLEND);
	    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	
	
	
	
	
	
	
	public void display(GLAutoDrawable arg0) {
		
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glBegin(GL2.GL_QUADS);

		// Bottom color (start of gradient)
		gl.glColor3f(0.1f, 0.0f, 0.8f);
		gl.glVertex2f(-540, -500);

		// Top color (end of gradient)
		gl.glColor3f(0.2f, 0.11f, 0.5f);
		gl.glVertex2f(-540, 500);

		// Top color (end of gradient)
		gl.glColor3f(0.0f, 0.0f, 0.4f); // Yellow
		gl.glVertex2f(540, 500);

		// Bottom color (start of gradient)
		gl.glColor3f(0.0f, 0.0f, 0.0f); // Black
		gl.glVertex2f(540, -500);

		// Bottom color (start of gradient)
		gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
		gl.glVertex2f(-540, -500);

		
		gl.glEnd();

		//draw stars
		startsDisplay(gl);

		//Display Score And Level
		String scoreText = "Score: " + (int) alien_counter;
        gl.glColor3f(1f, 1f, 1f);  
        gl.glRasterPos2i(450, -480);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, scoreText);
        gl.glColor3f(1, 1, 1);
        
        String levelText = "Level : " + (int) this.level;
        gl.glColor3f(1f, 1f, 1f);  
        gl.glRasterPos2i(-520, -480);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, levelText);
        gl.glColor3f(1, 1, 1);
        
        
		/////////////////////////dwarwig polygons/////////////////////////
		for (Polygon polygon : polygons) {

			if (polygon.isVisible) {

				if (polygons.get(0) != polygon) {
					gl.glColor4f(0	, 0, 0, 0f);
					gl.glBegin(GL2.GL_POLYGON);
					
					gl.glVertex2f(polygon.x1, polygon.y1);
					
					gl.glVertex2f(polygon.x2, polygon.y1);
					
					gl.glVertex2f(polygon.x2, polygon.y2);
					
					gl.glVertex2f(polygon.x1, polygon.y2);
					gl.glEnd();
					gl.glColor3f(0, 0, 0);
					gl.glColor4f(0.0f, 0.0f, 0f, 1f);
					alien(gl, scaleX, scaleY, ((polygon.x1 + polygon.x2) / 2),
							((polygon.y1 + polygon.y2) / 2));
					
				} else {
					gl.glColor4f(0.5f, 0.0f, 0.78f, 0f);
					gl.glBegin(GL2.GL_POLYGON);
					gl.glVertex2f(polygon.x1, polygon.y1);
					gl.glVertex2f(polygon.x2, polygon.y1);
					gl.glVertex2f(polygon.x2, polygon.y2);
					gl.glVertex2f(polygon.x1, polygon.y2);
					gl.glEnd();
					gl.glColor3f(0, 0, 0);
					gl.glColor4f(1f, 1f, 1f, 1f);
					playerModel(gl,((polygon.x1 + polygon.x2) / 2),
							((polygon.y1 + polygon.y2) / 2) );
					gl.glColor3f(0, 0, 0);
				}

			}

		}
		
		/////////////////////////cheking if all the targets are killed/////////////////////////
		if(alien_counter == polygons.size()-1){
			ChangeLevel(++this.level);
		}

		
		
		

		///////////////////////Move Player///////////////////////
		if (moveRight) {
			movePolygon(0, 0.5f, 0);
		} else if (moveLeft) {
			movePolygon(0, -0.5f, 0);
		}
		
		
		
		
		

		///////////////// Handle bullet firing and collisions///////////////////
		for (Bullet bullet : new ArrayList<>(bullets)) {
			gl.glColor3f(1, 1, 1);
			if (bullets.contains(bullet)) {
				gl.glRectf(bullet.x - 5, bullet.y - 5, bullet.x + 5,
						bullet.y + 5);
				bullet.update();
				if (bullet.y > this.size().height/2) {
					bullets.remove(bullet);
				}
				// Check for collisions and take appropriate action
				for (Polygon polygon : polygons) {
					if (isColliding(bullet, polygon)) {
						ColindingSound();
						polygon.isVisible = false;
                        alien_counter++;
						bullets.remove(bullet);
					}
				}
			}
			gl.glColor3f(0, 0, 0);
		}
	}
	
	
	
	
	
	

	
	/////////////////////changing level///////////////////////
	void ChangeLevel(int level2)  {
		levelUpSound();
		new maps(level2);
	}

	
	
	
	
	
	///////////////////////// spaceship movement/////////////////////////
	void movePolygon(int polygonIndex, float deltaX, float deltaY) {
		Polygon polygon = polygons.get(polygonIndex);

		// Update the polygon's position
		polygon.x1 += deltaX;
		polygon.x2 += deltaX;
		polygon.y1 += deltaY;
		polygon.y2 += deltaY;

		/////////// Bounds checking to prevent the polygon from going out of the screen///////////////////////
		if (polygon.x1 < -520 || polygon.x2 > 520 || polygon.y1 < -500
				|| polygon.y2 > 300) {
			// Reset the position if out of bounds
			polygon.x1 -= deltaX;
			polygon.x2 -= deltaX;
			polygon.y1 -= deltaY;
			polygon.y2 -= deltaY;
		}
	}
	
	
	
	
	
	
		///////////////////////// cheking if a bullet is hiting an alien /////////////////////////

	boolean isColliding(Bullet bullet, Polygon polygon) {
		if (!polygon.isVisible) {
			return false; // Skip collision check for invisible polygons
		}
  
		float bulletX = bullet.x;
		float bulletY = bullet.y;
		float polygonX1 = polygon.x1;
		float polygonY1 = polygon.y1;
		float polygonX2 = polygon.x2;
		float polygonY2 = polygon.y2;

		return (bulletX > polygonX1 && bulletX < polygonX2
				&& bulletY > polygonY1 && bulletY < polygonY2)
				|| (bulletX > polygonX1 && bulletX < polygonX2
						&& bulletY > polygonY2 && bulletY < polygonY1);
	}

	
	///////////////////////// playing a sound when an alien is hit with a bullet///////////////////////// 
	public void ColindingSound(){
		try { 
			AudioInputStream audio = AudioSystem.getAudioInputStream
					(new File("C:\\Users\\DotNet\\eclipse-workspace\\Alien_Shooter\\sounds\\coliding.wav"));
		   Clip	clip1 = AudioSystem.getClip();
			clip1.open(audio);
			clip1.start();
		}
		catch(UnsupportedAudioFileException e1) 
		{System.out.println(e1.getStackTrace());
		
		}catch(IOException e2)
		{System.out.println(e2.getStackTrace());}
		
		catch(LineUnavailableException e3) {
			System.out.println(e3.getStackTrace());
			
		}
	}
	
	///////////////////////// playing a sound when a bullet is fired/////////////////////////
	public void bulletSound(){
		try { 
			AudioInputStream audio = AudioSystem.getAudioInputStream
					(new File("C:\\Users\\DotNet\\eclipse-workspace\\Alien_Shooter\\sounds\\shoot.wav"));
		   Clip	clip1 = AudioSystem.getClip();
			clip1.open(audio);
			clip1.start();
		}
		catch(UnsupportedAudioFileException e1) 
		{System.out.println(e1.getStackTrace());
		
		}catch(IOException e2)
		{System.out.println(e2.getStackTrace());}
		
		catch(LineUnavailableException e3) {
			System.out.println(e3.getStackTrace());
			
		}
	}
	
	///////////////////////// playing a sound when the level changes/////////////////////////
	public void levelUpSound(){
		try { 
			AudioInputStream audio = AudioSystem.getAudioInputStream
					(new File("C:\\Users\\DotNet\\eclipse-workspace\\Alien_Shooter\\sounds\\win.wav"));
		   Clip	clip1 = AudioSystem.getClip();
			clip1.open(audio);
			clip1.start();
		}
		catch(UnsupportedAudioFileException e1) 
		{System.out.println(e1.getStackTrace());
		
		}catch(IOException e2)
		{System.out.println(e2.getStackTrace());}
		
		catch(LineUnavailableException e3) {
			System.out.println(e3.getStackTrace());
			
		}
	}
	
	
	
	
	
	public void dispose(GLAutoDrawable arg0) {
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	}
	
	
	
	
	

	public static void main(String[] arg) {
		new AlienShooter();
	}

	
	
	
	
	
	///////////////////////// Bullet class /////////////////////////
	class Bullet {
		float x, y;
		float speed = 0.7f;

		void update() {
			y += speed;
		}
	}

	
	
	///////////////////////// star class /////////////////////////
	class stars{
	    int x;
	    int y;
	    
	    public stars(int x,int y){
	      this.x=x;
	      this.y=y;
	    }
	  }
	
	
	
	
	
	///////////////////////// Polygon class /////////////////////////
	class Polygon {
		float x1, y1, x2, y2;
		boolean isVisible = true;
		Polygon(float x1, float y1, float x2, float y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		Polygon(float x, float y) {// center Point
			this(x - 30, y - 20, x + 30, y + 20);
		}
	}
	
	
	
	

	///////////////////////// Fire bullet method /////////////////////////
	void fireBullet() {
		Bullet bullet = new Bullet();
		Polygon bottomPolygon = polygons.get(0);// 0 index is for the first Player
		bullet.x = (bottomPolygon.x1-5 + bottomPolygon.x2-5) / 2; //X Center of Player 			
		bullet.y = bottomPolygon.y2+100; // Top of bottom polygon
		bullets.add(bullet);
	}

	
	
	
	
///////////////////////// method for generating stars /////////////////////////
	void genarateStars(){
	    for(int i = 0 ; i <35 ;i++){
	      int x = (int)(Math.random()*520);
	      int y = (int)(Math.random()*520);
	      star.add(new stars(x,y));
	    }
	      
	      for(int i = 0 ; i <35 ;i++){
	      int x = (int)(Math.random()*-520);
	      int y = (int)(Math.random()*-520);
	      star.add(new stars(x,y));
	    }
	      for(int i = 0 ; i <35 ;i++){
	      int x = (int)(Math.random()*-520);
	      int y = (int)(Math.random()*520);
	      star.add(new stars(x,y));
	    }
	      for(int i = 0 ; i <35 ;i++){
	      int x = (int)(Math.random()*520);
	      int y = (int)(Math.random()*-520);
	      star.add(new stars(x,y));
	    }
	  }
	 
	
	
	
	
	  void startsDisplay(GL2 gl){
	    for(stars starss:star){
	    gl.glColor4f(1, 1, 1, 0.3f);
	    gl.glPointSize(2f);
	    gl.glBegin(GL2.GL_POINTS);
	    gl.glVertex2f(starss.x, starss.y);
	    gl.glEnd();
	    gl.glColor3f(0, 0, 0);
	    }
	  }
	
	
	///////////////////////// Create alien Shape Used Inside Each Polygon /////////////////////////
	public void alien(GL2 gl, double scaleX, double scaleY, float x, float y) {
		gl.glPushMatrix();
		gl.glTranslated(x, y, 0);
		gl.glScaled(scaleX, scaleY, 1);
		gl.glRecti(-60, 75, 38, 100);
		gl.glRecti(-125, 60, 100, 75);
		gl.glRecti(-150, -10, 125, 60);
		gl.glRecti(-90, -30, 70, -10);
		// qachi lay rast
		gl.glRecti(38, -55, 85, -30);
		gl.glRecti(85, -80, 125, -50);
		// klk
		gl.glRecti(-39, -55, 17, -10);
		// qachi chap
		gl.glRecti(-110, -55, -60, -30);
		gl.glRecti(-150, -80, -110, -50);
		// dam
		gl.glColor3f(1, 1, 1);
		gl.glRecti(-39, -30, 17, -5);
		// chawi rast
		gl.glRecti(17, 15, 70, 40);
		// chawi lay chap
		gl.glRecti(-39, 15, -90, 40);
		gl.glColor3f(0, 0, 0);
		gl.glPopMatrix();

	}
	
	
	///////////////////////// space ship model created using rectangles /////////////////////////
	public void playerModel(GL2 gl, float x, float y) {
		gl.glColor3f(1, 1, 1);
		gl.glPushMatrix();
		gl.glScaled(0.9, 0.8, 0);
		gl.glTranslated(x, y, 0);
	    //
	    gl.glRecti(-30, -30, 20, 90);
	    
	    // 
	    gl.glRecti(-15, 90, 5, 110);
	    
	    //center
	    gl.glRecti( -70,-15 , 35, 17);
	    
	    //lakehshay lay chap
	    
	    gl.glRecti(-75, -30,-45,17);
	    
	    //Left Weapon
	    gl.glRecti(-60, 17, -45, 33);
	    
	    //
	    gl.glRecti(35, -30, 65, 17);
	    
	    //Rigth Weapon
	    gl.glRecti(35, 17, 50, 33);
	    
	    gl.glRecti(-15, -50, 5, -30);
	    
	    gl.glColor3f(0, 1, 0);
	    gl.glRecti(-15, 35, 5, 80);
	    gl.glColor3f(0, 0, 0);
	    gl.glPopMatrix();
	    
	}
	
	
	
	
	
}
