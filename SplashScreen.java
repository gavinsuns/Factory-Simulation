import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * SplashScreen.
 * 
 * @author 
 * @version
 */
public class SplashScreen extends World
{

    /**
     * Constructor for objects of class SplashScreen.
     */
    public SplashScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 640, 1); 
    }
    
    public void act()
    {
        if(Greenfoot.mouseClicked(this))
        {
            World factory = new MyWorld();
            Greenfoot.setWorld(factory);
        }
    }
}
