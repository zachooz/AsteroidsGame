import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AsteroidsGame extends PApplet {

/* @pjs preload="Sprites/ship.png, Sprites/asteroid.png, Sprites/shipBackward.png, Sprites/shipForward.png, Sprites/star1.png, Sprites/bullet.png, Sprites/debree.png, Sprites/game.png, Sprites/over.png, Sprites/retry.png;*/
/* MINIM DOESNT WORK ONLINE
import ddf.minim.*;
Minim minim;
AudioPlayer player;
AudioInput input;
*/

private SpaceShip myShip;
private SpaceField mySpaceField;
private boolean accelerate, turnCounterClockwise, turnClockwise, decelerate, gameOver;
private PImage bullet;

private int shootTimer;
private int spawnTimer;
private int s;
private int score;
private int waitTime;

private int num;
//your variable declarations here
public void setup(){
  size(700,700);
  myShip =  new SpaceShip();
  mySpaceField = new SpaceField();
  bullet=loadImage("Sprites/bullet.png");
  shootTimer = 100;
  spawnTimer=0;
  gameOver=false;
  score = 0;
  waitTime = 100;
  num = 0;
  //music
  /*
  minim = new Minim(this);
  player = minim.loadFile("pew.mp3");
  input = minim.getLineIn();
  */
  cursor(CROSS);
}

public void draw() {

	int m = millis();
	s = second();
	background(0);
	mySpaceField.showField();
	myShip.show(); 
	myShip.move();
	textAlign(RIGHT);
	textSize(32);
	fill(255, 255, 255);
	text("Score: " + score, width-10, 50); 
 
	if (accelerate)
		myShip.accelerate(myShip.acceleration);

	if (decelerate)
		myShip.accelerate(myShip.acceleration*-1);
  /* USED TO CHANGE ROT WITH KEYS
	if (turnCounterClockwise)
		myShip.rotateShip(-5);
	if (turnClockwise) 
		myShip.rotateShip(5);
  */
	if(!accelerate && !decelerate)
		myShip.notAccelerating();
	if (mousePressed == true && m>=shootTimer){
		/*
		player.play();
		player = minim.loadFile("pew.mp3");	
		*/
		myShip.shoot();
		shootTimer=m + waitTime;
	}

  if(s>=spawnTimer){
	if(!gameOver){
		mySpaceField.spawnStroid();
	}
	spawnTimer=s+1;
  }
  if(spawnTimer == 60 && s == 0)
	spawnTimer = 0;

}


class ABullet extends Floater{
  private int mySize = 15;
  private float rad = mySize/2-1;
  private String currentBullet;
  private double speed;
  private double dRadians;
  public ABullet(double dRadians, double myCenterX, double myCenterY){
    speed = 15;  
    this.dRadians = dRadians;
    this.myCenterX=myCenterX;
    this.myCenterY=myCenterY;   
    this.myDirectionX =  ((speed) * Math.cos(dRadians)); 
    this.myDirectionY = ((speed) * Math.sin(dRadians));
  } 
  public float getRad(){
    return rad;
  } 
  public void setX(int x){
    myCenterX=x;
  } 
  public int getX(){
    return (int) (myCenterX);
  }  
  public void setY(int y){
    myCenterY=y;
  }  
  public int getY(){
    return (int) (myCenterY);
  }  
  public void setDirectionX(double x){
    myDirectionX = x;
  }  
  public double getDirectionX(){
    return myDirectionX;
  }  
  public void setDirectionY(double y){
    myDirectionY = y;
  }   
  public double getDirectionY(){
    return myDirectionY;
  }  
  public void setPointDirection(int degrees){
    myPointDirection=degrees;
  }
  public double getPointDirection(){
    return myPointDirection;
  }
  public void accelerate(double dAmount){     
    //convert the current direction the floater is pointing to radians    
    dRadians =myPointDirection*(Math.PI/180);   
    //change coordinates of direction of travel 
        myDirectionX += ((dAmount) * Math.cos(dRadians)); 
        myDirectionY += ((dAmount) * Math.sin(dRadians));
  } 
  public void show(){  //Draws the floater at the current position    
     pushMatrix();
        imageMode(CENTER);
        translate((float)myCenterX,(float)myCenterY);
        rotate((float)dRadians);
        tint(255, 255);
        image(bullet, 0, 0, mySize, mySize);
     popMatrix();
  } 
  public void move(){   //move the floater in the current direction of travel
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;       
  } 
}

public class ABomb {
	private double myRad = 0;
	private int opacity=255;
	private double y;
	private double x;
	private int theColor;
	public ABomb(double x, double y){
		if(num%3 == 0){
			theColor = 0;
		} else if(num%2 == 0) {
			theColor = 1;
		} else {
			theColor = 2;
		}
		num++;
		this.x=x;
		this.y=y;
	}

	private void show(){
		noFill();
		if(theColor == 0){
			stroke(255,255,255);
		} else if(theColor == 1){
			stroke(0,255,0);
		} else{
			stroke(255,0,0);
		}
		strokeWeight(10);  // Beastly
		ellipse((float)x,(float)y,(float)myRad,(float)myRad);
		myRad++;
	}
	public void run(){
		show();
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}

	public double getRad(){
		return myRad;
	}
	public boolean done(){
		if(myRad > 200)
			return true;
		return false;
	}
}
public class Beam{
	private int length = 1000;
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double dRadians;
	private int time = 100;
	public Beam(double x1, double y1, double dRadians){
		this.x1=x1;
		this.y1=y1;
		this.dRadians = dRadians;
		x2 = x1 + (length* Math.cos(dRadians));
		y2 = y1 + (length* Math.sin(dRadians));

	}
	public void show(){
		strokeWeight(1);
		line((float)x1,(float)y1,(float)x2,(float)y2);
	}
	public double getX1(){
		return x1;
	}
	public double getX2(){
		return x2;
	}
	public double getY1(){
		return y1;
	}
	public double getY2(){
		return y2;
	}
	public boolean alive(){
		time--;
		if(time <= 0)
			return false;
		return true;
	}

}
public class SpaceShip extends Floater{
  public float acceleration;
  private PImage ship;
  private String currentImage;
  private ABullet[] bulletHolder;
  private int bulletNum;
  private ABomb[] bombHolder;
  private int bombNum;
  private int mySize = 50;
  private int rad = mySize/2 - 5;
  private double dRadians;
  private String gun;
  public SpaceShip(){
    acceleration=.3f;   
    myCenterX=width/2;
    myCenterY=height/2;  
    myDirectionX=0;
    myDirectionY=0;
    myPointDirection=0;
	ship=loadImage("Sprites/ship.png");
    currentImage="Sprites/ship.png";
    bulletHolder = new ABullet[50];
    bulletNum = 0;
  	bombHolder = new ABomb[20];
    bombNum = 0;
    dRadians = Math.asin((mouseY-myCenterY)/(dist((float)myCenterX,(float)myCenterY,mouseX,mouseY))); 
    if((mouseX-myCenterX)<0){
      dRadians=Math.PI-dRadians;
    }
    choice();
  }

  public void choice(){
  	//boom may be added
  	String[] guns = {"spread","spread","rapid","spread","rapid","rapid","rapid","boom","boom","boom","boom"};
  	int rNum = (int) (Math.random()*guns.length);
  	gun = guns[rNum];
  }
  public String getGun(){
  	return gun;
  }
  public int getRad(){
    return (int) (rad);
  } 
  public ABullet[] getBullets(){
	return bulletHolder;
  }
  public ABomb[] getBombs(){
	return bombHolder;
  }
  public void setX(int x){
    myCenterX=x;
  } 
  public int getX(){
    return (int) (myCenterX);
  }  
  public void setY(int y){
    myCenterY=y;
  }  
  public int getY(){
    return (int) (myCenterY);
  }  
  public void setDirectionX(double x){
    myDirectionX = x;
  }  
  public double getDirectionX(){
    return myDirectionX;
  }  
  public void setDirectionY(double y){
    myDirectionY = y;
  }   
  public double getDirectionY(){
    return myDirectionY;
  }  
  public void setPointDirection(int degrees){
    myPointDirection=degrees;
  }
  public double getPointDirection(){
    return myPointDirection;
  }
  public void accelerate(double dAmount){
    int maxSpeed = 10;
    //change coordinates of direction of travel 
    myDirectionX += ((dAmount) * Math.cos(dRadians)); 
    myDirectionY += ((dAmount) * Math.sin(dRadians));
	
	if(myDirectionX > maxSpeed)
		myDirectionX = maxSpeed;
	if(myDirectionY > maxSpeed)
		myDirectionY = maxSpeed;
	if(myDirectionX < -1 * maxSpeed)
		myDirectionX = -1 * maxSpeed;
	if(myDirectionY < -1 * maxSpeed)
		myDirectionY = -1 * maxSpeed;
  	if(dAmount>0){
  		currentImage="Sprites/shipForward.png"; //change image due to acceleration
  	} else {
  		currentImage="Sprites/shipBackward.png";
  	}
  } 
  
  public void notAccelerating(){
  	if(currentImage!="Sprites/ship.png"){
  		currentImage="Sprites/ship.png";
  	}
  }
  public void move(){   //move the floater in the current direction of travel
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width){     
      myCenterX = 0;    
    }    
    else if (myCenterX<0){     
      myCenterX = width;    
    }    
    if(myCenterY >height){    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0){     
      myCenterY = height;    
    }
    for(ABullet oneBullet : bulletHolder){
      if(oneBullet!=null){
        oneBullet.move();
        oneBullet.show();
      }
    }
  }
  public void shoot(){
    myPointDirection=dRadians*(190/Math.PI);
    if(bulletNum>=bulletHolder.length)
      bulletNum=0;
	if(bombNum>=bombHolder.length)
	      bombNum=0;
  	if(gun == "rapid"){
  		waitTime=100;
	    double theX1 = myCenterX + ((25) * Math.cos(dRadians+Math.PI/8));
	    double theY1 = myCenterY + ((25) * Math.sin(dRadians+Math.PI/8));

	    double theX2= myCenterX + ((25) * Math.cos(dRadians-Math.PI/8));
	    double theY2 = myCenterY + ((25) * Math.sin(dRadians-Math.PI/8));
	    bulletHolder[bulletNum]=new ABullet(dRadians, theX1, theY1);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians, theX2, theY2);
	    bulletNum++;
	}
  	if(gun == "spread"){
  		waitTime=400;
	    double theX = myCenterX + ((25) * Math.cos(dRadians));
	    double theY = myCenterY + ((25) * Math.sin(dRadians));

	    double theX1 = myCenterX + ((25) * Math.cos(dRadians+.1f));
	    double theY1 = myCenterY + ((25) * Math.sin(dRadians+.1f));

	    double theX2= myCenterX + ((25) * Math.cos(dRadians-.1f));
	    double theY2 = myCenterY + ((25) * Math.sin(dRadians-.1f));

	    double theX3= myCenterX + ((25) * Math.cos(dRadians+.15f));
	    double theY3 = myCenterY + ((25) * Math.sin(dRadians+.15f));

	    double theX4= myCenterX + ((25) * Math.cos(dRadians-.15f));
	    double theY4 = myCenterY + ((25) * Math.sin(dRadians-.15f));

	    double theX5 = myCenterX + ((25) * Math.cos(dRadians+.05f));
	    double theY5 = myCenterY + ((25) * Math.sin(dRadians+.05f));

	    double theX6= myCenterX + ((25) * Math.cos(dRadians-.05f));
	    double theY6 = myCenterY + ((25) * Math.sin(dRadians-.05f));

	    bulletHolder[bulletNum]=new ABullet(dRadians, theX, theY);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians+.1f, theX1, theY1);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians-.1f, theX2, theY2);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians+.15f, theX3, theY3);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians-.15f, theX4, theY4);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians+.05f, theX5, theY5);
	    bulletNum++;
	    if(bulletNum>=bulletHolder.length)
	      bulletNum=0;
	    bulletHolder[bulletNum]=new ABullet(dRadians-.05f, theX6, theY6);
	    bulletNum++;
	}

	if(gun == "boom"){
		waitTime=600;
		bombHolder[bombNum] = new ABomb(this.getX(),this.getY());
		bombNum++;
	}

	if(gun == "beam"){

	}
  }
  public void show(){  //Draws the floater at the current position      
    //convert degrees to radians for sin and cos   
    if(dist((float)myCenterX,(float)myCenterY,mouseX,mouseY)!=0){
    	dRadians = Math.acos((mouseX-myCenterX)/(dist((float)myCenterX,(float)myCenterY,mouseX,mouseY))); 
    }
    if((mouseY-myCenterY)<0){
      dRadians*=-1;
    }
    for(int i = 0; i<bombHolder.length; i++){
      if(bombHolder[i]!=null){
         bombHolder[i].run();
         if(bombHolder[i].done()){
         	bombHolder[i] = null;
         }
      }
    }
	   pushMatrix();
		    imageMode(CENTER);
		    translate((float)myCenterX,(float)myCenterY);
		    rotate((float)dRadians);
		    tint(255, 255);
		    image(ship, 0, 0, mySize, mySize);
	   popMatrix();
	ship=loadImage(currentImage); //will be normal unless changed by acceleration later
  } 
}

abstract class Floater {//Do NOT modify the Floater class! Make changes in the SpaceShip class {   
  protected int corners;  //the number of corners, a triangular floater has 3   
  protected int[] xCorners;   
  protected int[] yCorners;   
  protected int myColor;   
  protected double myCenterX, myCenterY; //holds center coordinates   
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
  protected double myPointDirection; //holds current direction the ship is pointing in degrees    
  abstract public void setX(int x);  
  abstract public int getX();   
  abstract public void setY(int y);   
  abstract public int getY();   
  abstract public void setDirectionX(double x);   
  abstract public double getDirectionX();   
  abstract public void setDirectionY(double y);   
  abstract public double getDirectionY();   
  abstract public void setPointDirection(int degrees);   
  abstract public double getPointDirection(); 

  //Accelerates the floater in the direction it is pointing (myPointDirection)   
  public void accelerate(double dAmount){          
    //convert the current direction the floater is pointing to radians    
    double dRadians =myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));       
  }   
  public void rotateShip(int nDegreesOfRotation){     
    //rotates the floater by a given number of degrees    
    myPointDirection+=nDegreesOfRotation;   
  }   
  public void move(){   //move the floater in the current direction of travel
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width){     
      myCenterX = 0;    
    }    
    else if (myCenterX<0){     
      myCenterX = width;    
    }    
    if(myCenterY >height){    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0){     
      myCenterY = height;    
    }   
  }   
  public void show(){  //Draws the floater at the current position              
    fill(myColor);   
    stroke(myColor);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);  
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++){     
      //rotate and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
  }   
} 

//star class
public class Star{
	private PImage starImage;
	private int x, y;
	private float opacity, fadeAmount;
	private String fadeMode;
	public Star(){
		//sets the star image
		starImage=loadImage("Sprites/star1.png");
		
		//sets a random position on the screen for the stars
		x = (int) (Math.random()*width);
		y = (int) (Math.random()*height);
		
		//holds star opacity
		opacity=255;
		
		//holds wether to fade in or out
		fadeMode = "out";
		
		//holds how much the star will fade per frame
		fadeAmount = (float) (Math.random()*5 + 1);
	}
	//displays the star
	public void display(){
		//makes star twinkle
		if(opacity <=50){
			fadeMode = "in";
		}
		
		if(opacity >= 255){
			fadeMode = "out";
		}
		
		if(fadeMode == "out"){
			opacity-= fadeAmount;
		} else {
			opacity += fadeAmount;
		}
		tint(255, opacity);
		image(starImage, x, y, 5, 5);
	}
	
}
public class MinAsteroid extends Asteroid{
	public MinAsteroid(double x, double y){
		radius=35;
		type = "MinAsteroid";
		this.x=x;
		this.y=y;
		makeDirection(x, y);

	}
	protected void makeDirection(double x, double y){
		int speedConstant=4;
		
		directionX = (Math.random()*(speedConstant+1)-speedConstant/2);
		directionY = (Math.random()*(speedConstant+1)-speedConstant/2);
		
		if(directionX == 0 && directionY == 0){
			makeDirection(x,y);
		}
	}
	protected void display(){
		imageMode(CENTER);
		tint(255, 255);
		image(asteroidImage, (float)x, (float)y, (int)this.radius, (int)this.radius);
	}
}

public class Asteroid{
	protected int radius=70;
	protected double x, y, directionX, directionY;
	protected PImage asteroidImage=loadImage("Sprites/asteroid.png");
	protected double lifeTime = 20;
	protected double life=5;
	protected String type = "Asteroid";
	public Asteroid(){
		setPos();
		makeDirection(x, y);
	}
	public void setX(double x){
		this.x=x;
	}
	public void setY(double y){
		this.y=y;
	}
	
	private void setPos(){
		int ran = (int)(Math.random()*4);
		if(ran==0){
			//bottom
			y=height+radius;
			x=(Math.random()*(width-radius*2))+radius;
		} else if(ran==1){
			//top
			y=-1*radius;
			x=(Math.random()*(width-radius*2))+radius;
		} else if(ran==2){
			//right
			x=width+radius;
			y=(Math.random()*(height-radius*2))+radius;
		} else if(ran==3){
			//left
			x=-1*radius;
			y=(Math.random()*(height-radius*2))+radius;
		}
	}
	protected void makeDirection(double x, double y){
		double speedConstant = 1;
		if(x>width){
			directionX = (Math.random()*speedConstant+speedConstant)*-1;
		} else if(x<0) {
			directionX = (Math.random()*speedConstant+speedConstant);
		} else {
			directionX = (Math.random()*speedConstant*2-speedConstant);
		}
		if(y>height){
			directionY = (Math.random()*speedConstant+speedConstant)*-1;
		} else if(y<0) {
			directionY = (Math.random()*speedConstant+speedConstant);
		} else {
			directionY = (Math.random()*speedConstant*2-speedConstant);
		}
		
		if(directionX == 0 && directionY == 0){
			makeDirection(x,y);
		}
	}
	protected void run(){
		display();
		move();

	}
	
	protected void move(){
		x+=directionX;
		y+=directionY;
	}
	
	protected void display(){
		imageMode(CENTER);
		tint(255, 255);
		image(asteroidImage, (float)x, (float)y, (int)this.radius, (int)this.radius);
	}
	
	protected boolean timeOut(){
		if(s>=spawnTimer)
			lifeTime--;
		if(lifeTime<=0)
			return true;
		return false;
	}
	protected boolean collide(){
		if((dist((float)x,(float)y,myShip.getX(),myShip.getY())<myShip.getRad()+radius/2) || gameOver){
			for(int i=0; i<10; i++){
				mySpaceField.createDebree(x,y,radius);
			}
			endGame();
			return true;
		}
		
		if(myShip.getGun() == "spread" || myShip.getGun() == "rapid"){
			for(int i = 0; i<myShip.getBullets().length; i++){
				if(myShip.getBullets()[i]!=null){
					if(dist((float)x,(float)y,myShip.getBullets()[i].getX(),myShip.getBullets()[i].getY())<myShip.getBullets()[i].getRad()+radius/2){
						myShip.getBullets()[i]=null;
						for(int a=0; a<3; a++){
							mySpaceField.createDebree(x,y,radius);
						}
						life--;
					}
				}
		
			}
		}
		if(myShip.getGun() == "boom"){
			for(int i = 0; i<myShip.getBombs().length; i++){
				if(myShip.getBombs()[i] != null){
					if(dist((float)x,(float)y,(float)myShip.getBombs()[i].getX(),(float)myShip.getBombs()[i].getY())<myShip.getBombs()[i].getRad()/2+50 && dist((float)x,(float)y,(float)myShip.getBombs()[i].getX(),(float)myShip.getBombs()[i].getY())>myShip.getBombs()[i].getRad()/2-50){
						mySpaceField.createDebree(x,y,radius);
						life-=.05f;
					}
				}
			}
	    }

		if(life<=0){
			if(type == "Asteroid"){
				mySpaceField.spawnMinStroid(x,y);
				mySpaceField.spawnMinStroid(x,y);
				mySpaceField.spawnMinStroid(x,y);
			}
			score+=2;
			return true;
		}
		return false;
	}
}

public class Debree{
	private PImage debreeImage=loadImage("Sprites/debree.png");
	private int radius = 10;
	private int opacity = 255;
	private double xDir = Math.random()*3-1;
	private double yDir = Math.random()*3-1;
	private double x, y;
	public Debree(double x, double y, int radius){
		this.x=x + Math.random()*radius-radius/2;
		this.y=y + Math.random()*radius-radius/2;
	}
	public void drawDebree(){
		tint(255, opacity);
		image(debreeImage, (float)x, (float)y, (int)radius, (int)radius);
		x+=xDir;
		y+=yDir;
		opacity-=10;
	}
	
	public boolean timeOut(){
		if(opacity <=1){
			return true;
		}
		return false;
	}
}

public class EndStroid{
	private double life=20;
	private double x, y;
	private int radius;
	private PImage asteroidImage;
	private String imageName;
	EndStroid(double x, double y, int radius, String imageName){
		this.x=x;
		this.y=y;
		this.radius=radius;
		this.imageName = imageName;
		asteroidImage=loadImage(imageName);
	}
	protected void run(){
		display();
	}
	protected void display(){
		imageMode(CENTER);
		tint(255, 255);
		image(asteroidImage, (float)x, (float)y, (int)this.radius, (int)this.radius);
	}
	protected boolean collide(){
		//ship rad is 20!
		if((dist((float)x,(float)y,myShip.getX(),myShip.getY())<myShip.getRad()+radius/2) || !gameOver){
			for(int i=0; i<30; i++){
				mySpaceField.createDebree(x,y,radius);
			}
			for(int i=0;i<2;i++){
				mySpaceField.spawnMinStroid(x,y);
				mySpaceField.posSpawnStroid(x,y);
			}
			if(imageName=="Sprites/retry.png"){
				gameOver=false;
				score=0;
			}
			return true;
		}
		
		//bullet rad is 6.5
		for(int i = 0; i<myShip.getBullets().length; i++){
			if(myShip.getBullets()[i]!=null){
				if(dist((float)x,(float)y,myShip.getBullets()[i].getX(),myShip.getBullets()[i].getY())<myShip.getBullets()[i].getRad()+radius/2){
					myShip.getBullets()[i]=null;
					for(int a=0; a<3; a++){
						mySpaceField.createDebree(x,y,radius);
					}
					if(imageName=="Sprites/retry.png"){
						life--;
					}
				}
			}
		}
		for(int i = 0; i<myShip.getBombs().length; i++){
			if(myShip.getBombs()[i] != null){
				if(dist((float)x,(float)y,(float)myShip.getBombs()[i].getX(),(float)myShip.getBombs()[i].getY())<myShip.getBombs()[i].getRad()/2+radius/2 && dist((float)x,(float)y,(float)myShip.getBombs()[i].getX(),(float)myShip.getBombs()[i].getY())>myShip.getBombs()[i].getRad()/2-20){
					mySpaceField.createDebree(x,y,radius);
					life-=.1f;
				}
			}
		}
		if(life<=0){
			for(int i=0;i<2;i++){
				mySpaceField.spawnMinStroid(x,y);
				mySpaceField.posSpawnStroid(x,y);
			}
			if(imageName=="Sprites/retry.png"){
				gameOver=false;
				score=0;
			}
			return true;
		}
		return false;
	}
}

//Class that holds arrays of objects
public class SpaceField{
	private Star[] starHolder;
	private Asteroid[] asteroidHolder;
	private Debree[] debreeHolder;
	private MinAsteroid[] minHolder;
	private EndStroid[] endHolder;
	public static final int ASTEROID_NUM = 100;
	public SpaceField(){
		endHolder = new EndStroid[3];
		minHolder = new MinAsteroid[ASTEROID_NUM];
		debreeHolder = new Debree[ASTEROID_NUM];
		starHolder = new Star[30];
		asteroidHolder = new Asteroid[ASTEROID_NUM];
		for(int i=0; i<starHolder.length; i++){
			starHolder[i] = new Star();
		}
	}
	
	public void spawnStroid(){
		outer: for(int i = 0; i<asteroidHolder.length; i++){
			if(asteroidHolder[i] == null){
				asteroidHolder[i] = new Asteroid();
				break outer;
			}
		}
	}
	public void posSpawnStroid(double x, double y){
		outer: for(int i = 0; i<asteroidHolder.length; i++){
			if(asteroidHolder[i] == null){
				asteroidHolder[i] = new Asteroid();
				asteroidHolder[i].setX(x);
				asteroidHolder[i].setY(y);
				break outer;
			}
		}
	}
	public void spawnMinStroid(double x, double y){
		outer: for(int i = 0; i<minHolder.length; i++){
			if(minHolder[i] == null){
				minHolder[i] = new MinAsteroid(x, y);
				break outer;
			}
		}
	}
	
	public void showField(){
		for(Star aStar : starHolder){
			aStar.display();
		}
		for(int i = 0; i<endHolder.length; i++){
			if(endHolder[i]!=null){
				endHolder[i].run();
				if(endHolder[i].collide()){
					endHolder[i] = null;
				}
			}
		}
		for(int i = 0; i<ASTEROID_NUM; i++){
			//controls large asteroid
			if(asteroidHolder[i]!=null){
				asteroidHolder[i].run();
				if(asteroidHolder[i].timeOut()){
					asteroidHolder[i] = null;
				}
				if(asteroidHolder[i]!=null){
					if(asteroidHolder[i].collide()){
						asteroidHolder[i] = null;
					}
				}
			}
			
			//controls small asteroids
			if(minHolder[i]!=null){
				minHolder[i].run();
				if(minHolder[i].timeOut()){
					minHolder[i] = null;
				}
				if(minHolder[i]!=null){
					if(minHolder[i].collide()){
						minHolder[i] = null;
					}
				}
			}
			
			//controls debree
			if(debreeHolder[i]!=null){
				debreeHolder[i].drawDebree();
				if(debreeHolder[i].timeOut()){
					debreeHolder[i] = null;
				}
			}
		}
	}
	
	public void createDebree(double x, double y, int radius){
		outer: for(int i = 0; i<debreeHolder.length; i++){
			if(debreeHolder[i] == null){
				debreeHolder[i] = new Debree(x, y, radius);
				break outer;
			}
		}
	}
	public void endGame(){
		//EndStroid(double x, double y, int radius, String imageName
		endHolder[0]=new EndStroid(width*2/5, height*1/5, 100, "Sprites/game.png");
		endHolder[1]=new EndStroid(width*3/5, height*1/5, 100, "Sprites/over.png");
		endHolder[2]=new EndStroid(width/2, height*3/5, 200, "Sprites/retry.png");
	}
}

public void endGame(){
	gameOver=true;
	myShip.setX(width/2);
	myShip.setY(height-80);
	myShip.setPointDirection(270);
	myShip.setDirectionX(0);
	myShip.setDirectionY(0);
	myShip.choice();
	mySpaceField.endGame();

	for(int i = 0; i<myShip.getBombs().length; i++)
		myShip.getBombs()[i] = null;
}

//controls rotation and acceleration key inputs!
public void keyPressed(){
	if (keyCode == UP || key == 'w') {
		accelerate=true;
	} 

	if (keyCode == DOWN || key == 's') {
		decelerate=true;
	} 

	if (keyCode == LEFT || key == 'a') {
		turnCounterClockwise=true;
	} 

	if (keyCode == RIGHT || key == 'd') {
		turnClockwise=true;
	} 

	if (key == 32){
		accelerate=true;
	}
}
public void keyReleased() {
	if (keyCode == UP || key == 'w') {
		accelerate=false;
	} 

	if (keyCode == DOWN || key == 's') {
		decelerate=false;
	} 

	if (keyCode == LEFT || key == 'a') {
		turnCounterClockwise=false;
	} 

	if (keyCode == RIGHT || key == 'd') {
		turnClockwise=false;
	} 

	if (key == 32){
		accelerate=false;
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
