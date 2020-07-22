import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Products that can be interacted by workers to be built and contribute to the war effort.
 * 
 * @author Carl Lam
 * @version October 2019
 */
public abstract class Product extends Actor
{
    //Constants for the various workstations.
    protected static int WORKSTATION_ONE_XLOC = 240;
    protected static int WORKSTATION_TWO_XLOC = 480;
    protected static int WORKSTATION_THREE_XLOC = 720;
    protected static int FACTORY_EDGE_XLOC = 960;
    
    //Constants for individual items.
    protected static int GUN_BUILD_POINTS = 2000;
    protected static int TANK_BUILD_POINTS = 5000;
    protected static int NUKE_BUILD_POINTS = 10000;
    
    //Initialize variables for individual products.
    protected int worldWidth;
    protected int totalBuildPoints;
    protected int curBuildPoints;
    protected int curStage;
    protected int moveSpeed;
    protected int curSpeed;
    protected int buildPointLimit;
    protected int XLimit;
    protected int trashPoints;
    protected boolean isTrash;
    protected boolean movedOnce;    
    private Product prod;
    
    //Trash image.
    GreenfootImage trash = new GreenfootImage("trash.png");
    
    //Abstract methods to be put in each Product (each product has different assets to be displayed.
    protected abstract void updateImage();
    protected abstract void edgeCheck();
    protected abstract void buildSound();
    
    //PUBLIC METHODS
    /**
     * Actions to be called every act.
     */
    public void act() 
    {
        moveChecker();
        checkObjectAhead();
        edgeCheck();
    }
    /**
     *  Adds points to the current build points, and checks to see if the image of the 
     *  item needs an update based on the new "balance" of points, only when the object is not moving.
     *  
     *  @param inputBuildPoints Number of points to add to the object.
     */
    public boolean build(int inputBuildPoints)
    {
        boolean built = false;
        if(inputBuildPoints > 0 && curBuildPoints < totalBuildPoints && !isTrash)
        {
            //Only allows for building in range of the station, and only up to the build points to the current build stage.
            if(isAtBuildStation() && curBuildPoints < buildPointLimit)
            {
                curBuildPoints += inputBuildPoints;
                buildSound();
                built = true;
            }
        }
        //Prevents the image from being fixed if it is turned into trash.
        if(!isTrash)
        {
            updateImage();
            updateXLimit();
            updateStage();
            updateBuildPointLimit();
        }
        //Returns true if the object is built, and false if the object has not had any build points successfully inputted.
        return built;
    }
    /**
     * Changes move speed of product. Will not allow for values under 0.
     * 
     * @param speed The int value to change the speed to.
     */
    public void changeMoveSpeed(int speed)
    {
        if(speed >= 0)
        {
            this.curSpeed = speed;
        }
    }
    /**
     * Returns the current build points that the product has.
     * 
     * @return The current build points. 
     */
    public int getBuildPoints()
    {
        return curBuildPoints;
    }
    
    //PROTECTED METHODS
    /**
     * Updates the maximum X position that the product can move to until it finishes it's build stage.
     */
    protected void updateXLimit()
    {
        if(curStage == 1)
        {
            XLimit = WORKSTATION_ONE_XLOC;
        }
        else if(curStage == 2)
        {
            XLimit = WORKSTATION_TWO_XLOC;
        }
        else if(curStage == 3)
        {
            XLimit = WORKSTATION_THREE_XLOC;
        }
        else
        {
            XLimit = FACTORY_EDGE_XLOC;
        }
    }
    /**
     * Returns the current stage of production that the product is currently in.
     */
    protected void updateStage()
    {
        if(curBuildPoints < totalBuildPoints/4)
        {
            curStage = 1;
        }
        else if(curBuildPoints < (totalBuildPoints/4) * 2)
        {
            curStage = 2;
        }
        else if(curBuildPoints < (totalBuildPoints/4) * 3)
        {
            curStage = 3;
        }
        else
        {
            curStage = 4;
        }
    }
    /**
     * Updates the maximum allowable value that the worker can build to, depending on the position of the product.
     * (eg. cannot build up to stage 4 when only at station 2).
     */
    protected void updateBuildPointLimit()
    {
        if(this.getX() <= WORKSTATION_ONE_XLOC + 10)
        {
            buildPointLimit = totalBuildPoints/4 + 10;
        }
        else if(this.getX() >= WORKSTATION_ONE_XLOC + 10 && this.getX() <= WORKSTATION_TWO_XLOC + 10)
        {
            buildPointLimit = (totalBuildPoints/4) * 2 + 10;
        }
        else if(this.getX() >= WORKSTATION_TWO_XLOC + 10 && this.getX() <= WORKSTATION_THREE_XLOC + 10)
        {
            buildPointLimit = (totalBuildPoints/4) * 3 + 10;
        }
        else if(this.getX() <= FACTORY_EDGE_XLOC)
        {
            buildPointLimit = totalBuildPoints;
        }
    }
    /**
     * Checks to see if object has reached its 'limit' for the current stage on the conveyor belt.
     * If it is at the limit, it will not move.
     */
    protected void moveChecker()
    {
        if(this.getX() < XLimit)
        {
            move(curSpeed);
        }
        if(!movedOnce)
        {
            movedOnce = true;
        }
    }
    /**
     * If touching an object on spawn, delete to prevent stacking object.
     */
    protected void addedToWorld(World MyWorld)
    {
        if(isTouching(Product.class) && !movedOnce)
        {
            getWorld().removeObject(this);
        }
    }
    
    //PRIVATE METHODS
    /**
     * Returns true if the object is within range of a build station.
     */
    private boolean isAtBuildStation()
    {
        if(this.getX() <= WORKSTATION_ONE_XLOC + 5 && this.getX() >= WORKSTATION_ONE_XLOC - 5)
        {
            return true;
        }
        else if(this.getX() <= WORKSTATION_TWO_XLOC + 5 && this.getX() >= WORKSTATION_TWO_XLOC - 5)
        {
            return true;
        }
        else if(this.getX() <= WORKSTATION_THREE_XLOC + 5 && this.getX() >= WORKSTATION_THREE_XLOC - 5)
        {
            return true;
        }
        return false;
    }
    /**
     * Turns the object into a bag of trash that goes to the factory edge.
     */
    private void turnToTrash()
    {
        isTrash = true;
        setImage(trash);
        XLimit = FACTORY_EDGE_XLOC;
    }
    /**
     * Checks 5 pixels ahead of the object for another object, so that it stops and
     * doesn't overlap with another one.
     */
    private void checkObjectAhead()
    {
        Product prod = (Product)getOneObjectAtOffset(getObjOffset() + 5, 0, Product.class);
        if(prod != null)
        {
            this.changeMoveSpeed(0);
        }
        else
        {
            this.changeMoveSpeed(moveSpeed);
        }
    }
    /**
     * Returns half of the width of the image.
     */
    private int getObjOffset()
    {
        return this.getImage().getWidth()/2;
    }
}