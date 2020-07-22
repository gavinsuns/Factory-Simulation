import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Peasant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Peasant extends Worker 
{
    public Peasant(int s, int x) 
    {
        super();
        this.xp = 20; 
        this.rank = 1;
        this.targetX = x;
        this.station = s;
        this.timer = 300;
        moveImages = importMoveSprites ("PWalk", 1, 9);
        deathImages = importDeathSprites ("PDie", 6);
        maxFrameLength = 0.10;
        alive = true;
        direction = Direction.fromInteger(0);
        this.walkSpeed = 60;
        this.walkAnimSpeed = 20;
     
       
        changeSpeed(walkSpeed, walkAnimSpeed);
        setImage (moveImages[direction.getDirection()][0]);
        moveInDirection(direction);
        
        bar.update(xp, 0, 0); 
    }
    public void act() 
    {
        timer -= 10;
        if(xp<0 && alive)
        {
           MyWorld world = (MyWorld) getWorld();
           world.emptyStation(this.station);
           Greenfoot.playSound("oof.wav");
           world.addObject(new Dead(), getX(), getY());
           changeSpeed(walkSpeed, 5);
           alive = false;
           death(true);
        }
        else if(alive)
        {
            if(timer == 0)
            {
                work();
                this.timer = 300;
            }
            if(getX()>this.targetX)
            {
                atLocation = true;
                moveImages = importMoveSprites("PWork",1,1);
                setImage (moveImages[direction.getDirection()][0]);
                moveInDirection(direction);
                stopMoving();
            }
        }
        super.act();
        changeRank();
    } 
    public void changeRank()
    {
        if(xp>100)
        {
           Employee e = new Employee(this.station);
           getWorld().addObject(e,this.getX(), this.getY());
           getWorld().removeObject(this);
        }    
    }
}





