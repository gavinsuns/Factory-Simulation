import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Employee here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Employee extends Worker
{
    public Employee(int s)
    {
        this.xp = 20;
        this.rank = 2;
        this.station = s;
        this.timer = 300;
        alive = true;
        this.atLocation = true;
        bar.update(xp, 0, 0); 
    }
    /**
     * Act - do whatever the Employee wants to do. This method is called whenever
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
           Peasant p = new Peasant(this.getX(), this.station);
           getWorld().addObject(p, this.getX(), this.getY());
           getWorld().removeObject(this);
        }  
        else if(xp > 100)
        {
           Manager m = new Manager(this.station);
           getWorld().addObject(m, this.getX(), this.getY());
           getWorld().removeObject(this);
        }
    }
}
