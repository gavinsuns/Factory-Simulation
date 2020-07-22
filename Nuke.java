import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A nuke that can be spawned, built, and have it's image changed.
 * Image taken from: https://www.pngtube.com/viewm/bTxmhh_nuclear-bomb-the-atomic-bomb-bomb-png-image/
 * Sound taken from: https://www.youtube.com/watch?v=8ysc2quRx1M
 * 
 * @author Carl Lam
 * @version October 2019
 */
public class Nuke extends Product
{
    //Load assets for this product.
    GreenfootImage nuke1 = new GreenfootImage("nuke1.png");
    GreenfootImage nuke2 = new GreenfootImage("nuke2.png");
    GreenfootImage nuke3 = new GreenfootImage("nuke3.png");
    GreenfootImage nuke4 = new GreenfootImage("nuke4.png");
    GreenfootSound impactgun = new GreenfootSound("impactgun.wav");
    
    //CONSTRUCTOR
    /**
     * Initilalizes image and X limit for nuke on initial spawn.
     */
    public Nuke()
    {
        moveSpeed = 2;
        curSpeed = moveSpeed;
        totalBuildPoints = NUKE_BUILD_POINTS;
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
     * Updates the image of nuke based on its current stage.
     */
    protected void updateImage()
    {
        if(curStage == 1)
        {
            setImage(nuke1);
        }
        else if(curStage == 2)
        {
            setImage(nuke2);
        }
        else if(curStage == 3)
        {
            setImage(nuke3);
        }
        else
        {
            setImage(nuke4);
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
                ((MyWorld)getWorld()).increaseNukesMade();
            }
            getWorld().removeObject(this);
        }
    }
    public void buildSound()
    {
        impactgun.play();
    }
}