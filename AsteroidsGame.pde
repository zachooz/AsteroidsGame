SpaceShip myShip;
StarField myStarField;
boolean accelerate, turnCounterClockwise, turnClockwise, decelerate;
//your variable declarations here
public void setup(){
  //your code here
  size(500,500);
  myShip =  new SpaceShip();
  myStarField = new StarField();
}
public void draw() {
	background(0);
	myStarField.showField();
	myShip.show(); 
	myShip.move(); 
	if (accelerate) {
		myShip.accelerate(myShip.acceleration);
		
	} 

	if (decelerate) {
		myShip.accelerate(myShip.acceleration*-1);
		
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
  private PImage ship;
  public SpaceShip(){
    acceleration=.1;
    myColor=color(255,165,0);   
    myCenterX=width/2;
    myCenterY=height/2;  
    myDirectionX=0;
    myDirectionY=0;
    myPointDirection=0;
	ship=loadImage("ship.png");
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
    double dRadians =myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));
	if(dAmount>0){
		ship=loadImage("shipForward.png"); //change image due to acceleration
	} else {
		ship=loadImage("shipBackward.png");
	}
  }  
  public void show(){  //Draws the floater at the current position   
    fill(myColor);   
    stroke(myColor);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);  
	pushMatrix();
		imageMode(CENTER);
		translate((float)myCenterX,(float)myCenterY);
		rotate((float)dRadians);
		tint(255, 255);
		image(ship, 0, 0, 50, 47);
	popMatrix();
	ship=loadImage("ship.png"); //will be normal unless changed by acceleration later
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

//Class that holds an array of Star objects and displays all the stars in the array
public class StarField{
	public Star[] starHolder;
	public StarField(){
		starHolder = new Star[20];
		for(int i=0; i<starHolder.length; i++){
			starHolder[i] = new Star();
		}
	}
	
	public void showField(){
		for(Star aStar : starHolder){
			aStar.display();
		}
	}
}


//controls rotation and acceleration key inputs!
void keyPressed(){
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
void keyReleased() {
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
