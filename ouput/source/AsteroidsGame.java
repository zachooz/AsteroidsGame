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

SpaceShip myShip;
boolean accelerate, turnCounterClockwise, turnClockwise, decelerate;
//your variable declarations here
PImage ship;
public void setup(){
  //your code here
  size(500,500);
  ship=loadImage("ship.png");
  myShip =  new SpaceShip();
}
public void draw() {
	background(0);
	myShip.show(); 
	myShip.move(); 
	ship=loadImage("ship.png");
	if (accelerate) {
		myShip.accelerate(myShip.acceleration);
		ship=loadImage("shipForward.png");
	} 

	if (decelerate) {
		myShip.accelerate(myShip.acceleration*-1);
		ship=loadImage("shipBackward.png");
	} 

	if (turnCounterClockwise) {
		myShip.rotateShip(-5);
	} 

	if (turnClockwise) {
		myShip.rotateShip(5);
	} 
}
class SpaceShip extends Floater{
  public float acceleration;
  public SpaceShip(){
    acceleration=.1f;
	/* FOR POLYGON SHIP
    corners=3;
    xCorners=new int[3];
    yCorners=new int[3];
    xCorners[0]=-5;
    xCorners[1]=5;
    xCorners[2]=-5;
    yCorners[0]=-5;
    yCorners[1]=0;
    yCorners[2]=5;
	*/
    myColor=color(255,165,0);   
    myCenterX=width/2;
    myCenterY=height/2;  
    myDirectionX=0;
    myDirectionY=0;
    myPointDirection=0;
  } 
  public void setX(int x){
    myCenterX=x;
  };  
  public int getX(){
    return (int) (myCenterX);
  };   
  public void setY(int y){
    myCenterY=y;
  };   
  public int getY(){
    return (int) (myCenterY);
  };   
  public void setDirectionX(double x){
    myDirectionX = x;
  };   
  public double getDirectionX(){
    return myDirectionX;
  };   
  public void setDirectionY(double y){
    myDirectionY = y;
  };   
  public double getDirectionY(){
    return myDirectionY;
  };   
  public void setPointDirection(int degrees){
    myPointDirection=degrees;
  };   
  public double getPointDirection(){
    return myPointDirection;
  }; 
  public void show(){  //Draws the floater at the current position              
    fill(myColor);   
    stroke(myColor);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);  
	pushMatrix();
		imageMode(CENTER);
		translate((float)myCenterX,(float)myCenterY);
		rotate((float)dRadians);
		image(ship, 0, 0, 50, 47);
	popMatrix();
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
