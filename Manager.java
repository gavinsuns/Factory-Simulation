
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Manager here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Manager extends Worker
{
    public Manager(int s)
    {  
        this.xp = 20;
        this.rank = 3;
        this.station = s;
        this.timer = 300;
        alive = true;
        this.atLocation = true;
        bar.update(xp, 0, 0); 
    }
    
    /**
     * Act - do whatever the Manager wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        timer -= 10;
        if(timer < 0)
        {
            work();
            this.timer = 300;
        }
        changeRank();
    }  
    public void changeRank()
    {
        if(xp<0)
        {
           Employee e = new Employee(this.station);
           getWorld().addObject(p, this.getX(), this.getY());
           getWorld().removeObject(this);
        }  
    }
}