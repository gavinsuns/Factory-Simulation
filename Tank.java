import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A tank that can be spawned, built, and have it's image changed.
 * Image taken from: http://clipart-library.com/clipart/266142.htm
 * Sound taken from: https://freesound.org/people/thefilmbakery/sounds/137836/
 * 
 * @author Carl Lam
 * @version October 2019
 */
public class Tank extends Product
{
    //Load assets for this product.
    GreenfootImage tank1 = new GreenfootImage("tank1.png");
    GreenfootImage tank2 = new GreenfootImage("tank2.png");
    GreenfootImage tank3 = new GreenfootImage("tank3.png");
    GreenfootImage tank4 = new GreenfootImage("tank4.png");
    GreenfootSound metal = new GreenfootSound("metal.wav");
    
    //CONSTRUCTOR
    /**
     * Initializes image and X limit for tank on initial spawn.
     */
    public Tank()
    {
        moveSpeed = 1;
        curSpeed = moveSpeed;
        totalBuildPoints = TANK_BUILD_POINTS;
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
     * Updates the image of tank based on its current stage.
     */
    protected void updateImage()
    {
        if(curStage == 1)
        {
            setImage(tank1);
        }
        else if(curStage == 2)
        {
            setImage(tank2);
        }
        else if(curStage == 3)
        {
            setImage(tank3);
        }
        else
        {
            setImage(tank4);
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
                ((MyWorld)getWorld()).increaseTanksMade();
            }
            getWorld().removeObject(this);
        }
    }
    protected void buildSound()
    {
        metal.play();
    }
}