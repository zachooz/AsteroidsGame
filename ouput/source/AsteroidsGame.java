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

/* @pjs preload="ship.png, asteroid.png, shipBackward.png, shipForward.png, star1.png, bullet.png;*/

SpaceShip myShip;
SpaceField mySpaceField;
boolean accelerate, turnCounterClockwise, turnClockwise, decelerate;
PImage bullet;
int shootTimer;
int spawnTimer;
int s;
//your variable declarations here
public void setup(){
  //your code here
  size(700,700);
  myShip =  new SpaceShip();
  mySpaceField = new SpaceField();
  bullet=loadImage("bullet.png");
  shootTimer = 100;
  spawnTimer=0;
}
public void draw() {
	int m = millis();
	s = second();
	background(0);
	mySpaceField.showField();
	myShip.show(); 
	myShip.move(); 
	if (accelerate)
		myShip.accelerate(myShip.acceleration);

	if (decelerate)
		myShip.accelerate(myShip.acceleration*-1);

	if (turnCounterClockwise)
		myShip.rotateShip(-5);

	if (turnClockwise) 
		myShip.rotateShip(5);

	if(!accelerate && !decelerate)
		myShip.notAccelerating();
  if (mousePressed == true && m>=shootTimer){
    myShip.shoot();
	shootTimer=m + 100;
  }
  if(s>=spawnTimer){
	mySpaceField.spawnStroid();
	spawnTimer=s+1;
  }
  if(spawnTimer == 60 && s == 0)
	spawnTimer = 0;
}

class aBullet extends Floater{
  private String currentBullet;
  private double speed;
  private double dRadians;
  public aBullet(double myPointDirection, double myCenterX, double myCenterY){
    speed = 15;  
    this.myPointDirection = myPointDirection;
    this.myCenterX=myCenterX;
    this.myCenterY=myCenterY;   
    dRadians =myPointDirection*(Math.PI/180);
    this.myDirectionX =  ((speed) * Math.cos(dRadians)); 
    this.myDirectionY = ((speed) * Math.sin(dRadians));
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
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180); 
     pushMatrix();
        imageMode(CENTER);
        translate((float)myCenterX,(float)myCenterY);
        rotate((float)dRadians);
        tint(255, 255);
        image(bullet, 0, 0, 15, 15);
     popMatrix();
  } 
  public void move(){   //move the floater in the current direction of travel
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;       
  } 
}
class SpaceShip extends Floater{
  public float acceleration;
  private PImage ship;
  private String currentImage;
  private aBullet[] bulletHolder;
  private int bulletNum;
  public SpaceShip(){
    acceleration=.3f;   
    myCenterX=width/2;
    myCenterY=height/2;  
    myDirectionX=0;
    myDirectionY=0;
    myPointDirection=0;
	  ship=loadImage("ship.png");
    currentImage="ship.png";
    bulletHolder = new aBullet[50];
    bulletNum = 0;
  } 
  public aBullet[] getBullets(){
	return bulletHolder;
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
    //convert the current direction the floater is pointing to radians    
    double dRadians =myPointDirection*(Math.PI/180);     
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
  		currentImage="shipForward.png"; //change image due to acceleration
  	} else {
  		currentImage="shipBackward.png";
  	}
  } 
  
  public void notAccelerating(){
  	if(currentImage!="ship.png"){
  		currentImage="ship.png";
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
    for(aBullet oneBullet : bulletHolder){
      if(oneBullet!=null){
        oneBullet.move();
        oneBullet.show();
      }
    }
  }
  public void shoot(){
    double dRadians =myPointDirection*(Math.PI/180); 
    if(bulletNum>=bulletHolder.length)
      bulletNum=0;

    double theX1 = myCenterX + ((25) * Math.cos(dRadians+Math.PI/8));
    double theY1 = myCenterY + ((25) * Math.sin(dRadians+Math.PI/8));

    double theX2= myCenterX + ((25) * Math.cos(dRadians-Math.PI/8));
    double theY2 = myCenterY + ((25) * Math.sin(dRadians-Math.PI/8));
    bulletHolder[bulletNum]=new aBullet(myPointDirection, theX1, theY1);
    bulletNum++;
    bulletHolder[bulletNum]=new aBullet(myPointDirection, theX2, theY2);
    bulletNum++;
  }
  public void show(){  //Draws the floater at the current position      
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);  
	   pushMatrix();
		    imageMode(CENTER);
		    translate((float)myCenterX,(float)myCenterY);
		    rotate((float)dRadians);
		    tint(255, 255);
		    image(ship, 0, 0, 50, 47);
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
		starImage=loadImage("star1.png");
		
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

public class Asteroid{
	private int radius=50;
	private double x, y, directionX, directionY;
	private PImage asteroidImage=loadImage("asteroid.png");
	double rotation = 0;
	double rotationAdd = Math.random()/5;
	private double lifeTime = 15;
	private int life=5;
	public Asteroid(){
		setPos();
		makeDirection(x, y);
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
	private void makeDirection(double x, double y){
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
	public void run(){
		display();
		move();

	}
	
	private void move(){
		x+=directionX;
		y+=directionY;
	}
	
	private void display(){
		rotation += rotationAdd;
		imageMode(CENTER);
		tint(255, 255);
		image(asteroidImage, (float)x, (float)y, (int)radius, (int)radius);
	}
	
	public boolean timeOut(){
		if(s>=spawnTimer)
			lifeTime--;

		if(lifeTime<=0)
			return true;
		if(life<=0)
			return true;
		return false;
	}
	public boolean collide(){
		
		//ship rad is 20!
		if(dist((float)x,(float)y,myShip.getX(),myShip.getY())<20+radius/2){
			return true;
		}
		
		//bullet rad is 6.5
		for(int i = 0; i<myShip.getBullets().length; i++){
			if(myShip.getBullets()[i]!=null){
				if(dist((float)x,(float)y,myShip.getBullets()[i].getX(),myShip.getBullets()[i].getY())<6.5f+radius/2){
					myShip.getBullets()[i]=null;
					life--;
				}
			}
		}
		return false;
	}
}

//Class that holds arrays of objects
public class SpaceField{
	public Star[] starHolder;
	public Asteroid[] asteroidHolder;
	public SpaceField(){
		starHolder = new Star[30];
		asteroidHolder = new Asteroid[50];
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
	
	public void showField(){
		for(Star aStar : starHolder){
			aStar.display();
		}
		for(int i = 0; i<asteroidHolder.length; i++){
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
		}
	}
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
