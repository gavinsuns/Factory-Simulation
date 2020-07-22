import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A gun that can be spawned, built, and have it's image changed.
 * Image taken from: https://crossfirefps.fandom.com/wiki/Mosin_Nagant
 * Sound taken from: https://www.youtube.com/watch?v=FfmGFAQmMNk
 * 
 * @author Carl Lam
 * @version October 2019
 */
public class Gun extends Product
{    
    //Load assets for this product. 
    GreenfootImage gun1 = new GreenfootImage("rifle1.png");
    GreenfootImage gun2 = new GreenfootImage("rifle2.png");
    GreenfootImage gun3 = new GreenfootImage("rifle3.png");
    GreenfootImage gun4 = new GreenfootImage("rifle4.png");
    GreenfootSound build = new GreenfootSound("hammer.wav");
    
    //CONSTRUCTOR
    /**
     * Initializes image and X limit for gun on initial spawn.
     */
    public Gun()
    {
        super();
        moveSpeed = 3;
        curSpeed = moveSpeed;
        totalBuildPoints = GUN_BUILD_POINTS;
        curBuildPoints = 0;
        //Update initial values for stage, the XLimit to start, and the starting image.
        updateStage();
        updateImage();
        updateXLimit();
        //Initialize the buildPointLimit.
        buildPointLimit = totalBuildPoints / 4 + 10;
    }
    
    //PROTECTED METHODS
    /**
     * Updates the image of gun based on its current stage.
     */
    protected void updateImage()
    {
        if(curStage == 1)
        {
            setImage(gun1);
        }
        else if(curStage == 2)
        {
            setImage(gun2);
        }
        else if(curStage == 3)
        {
            setImage(gun3);
        }
        else
        {
            setImage(gun4);
        }
    }
    /**
     * Checks if the object has reached the edge of the map, and deletes if it is.
     */
    protected void edgeCheck()
    {
        if (this.getX() > getWorld().getWidth() - 50)
        {
            if(!isTrash)
            {
                //Products only make it to the edge if they are completed, so they will be added to the "products made" total.
                ((MyWorld)getWorld()).increaseProductsMade();
                //Increasing the variable for stats.
                ((MyWorld)getWorld()).increaseGunsMade();
            }
            getWorld().removeObject(this);
        }
    }
    protected void buildSound()
    {
        build.play();
    }
}