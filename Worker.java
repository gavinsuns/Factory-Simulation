import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Worker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Worker extends Actor
{
    protected int xp;
    protected int maxXp = 100;
    protected int rank;
    protected int offset;
    protected Product p;
    protected boolean atLocation;
    protected int walkSpeed;
    protected int walkAnimSpeed;
    protected int delayActsLeft;
    protected boolean active;
    protected boolean alive;
    protected int targetX;
    protected int station;
    protected int timer;
    protected XPBar bar;
    protected GreenfootImage[][] moveImages;
    protected GreenfootImage[] deathImages;
    // Not yet implemented
    protected GreenfootImage[][] shootImages;
    protected GreenfootImage[][] castImages;
    protected GreenfootImage[][] thrustImages;
    protected GreenfootImage[][] slashImages;

    // Current set of images, loaded from one of the above arrays
    private GreenfootImage[] currentImages; 

    protected Direction direction; // Direction is 0-Right, 1-Left, 2-Up, 3-Down
    // And is based on a public Enum below

    protected double framesPerSecond;   // animation speed
    protected double moveSpeed;         // how many pixels per SECOND (not per act)
    protected double secondsPerFrame;   // calculated fraction of second per frame

    protected double tolerance;         // how close to target to stop
    protected double maxFrameLength;    // Used to avoid "jumping" if lag or GF pause.
    // What fraction of a second is max dead time?

    protected boolean autoMove;  // determine if currently moving 

    // ======== Private variables ========
    private int frame;          // current frame counter
    private double xx, yy;      // internal, double representation of coordinates
    private int dirX, dirY;     // variables used to control direction
    private int prevX, prevY;   // previous rounded X and Y values
    private boolean idle;         // used to specify idle frame
    private boolean stopAtEnd;  // Stop after end of the current set of frames
    private boolean removeAfterAnimation; // After stopAtEnd animation completes, remove myself?

    private Dot dot;            // dot used for direction / turnTowards method
    private Dot targetDot;      // extra Dot used as target for the dot

    // for time-keeping to keep animation going at consistent speed
    private long lastFrame;         // Keep track of when the last animation was updated
    private long current;

    
    public void work()
    {
        offset = (this.getImage().getHeight() / 2) + 30;
        p = (Product)getOneObjectAtOffset(0, offset, Product.class);
        if(atLocation == true)
        {
            if(p != null)
            {
                if(rank == 1)
                {
                    int chance = Greenfoot.getRandomNumber(2);
                    if(chance < 1)
                    {
                        p.build(200);
                        xp += 8;
                        bar.update(true, 8, 0, 0);
                    }
                    else
                    {
                        xp -= 6;
                        bar.update(false, 6, 0, 0);
                    }
                }
                if(rank == 2)
                {
                    int chance = Greenfoot.getRandomNumber(3);
                    if(chance < 2)
                    {
                        p.build(400);
                        xp += 7;
                        bar.update(true, 7, 0, 0);
                    }
                    else
                    {
                        xp -= 6;
                        bar.update(false, 6, 0, 0);
                    }
                }
                if(rank == 3)
                {
                    int chance = Greenfoot.getRandomNumber(4);
                    if(chance < 3)
                    {
                        p.build(600);
                        xp += 6;
                        bar.update(true, 6, 0, 0);
                    }
                    else
                    {
                        xp -= 6;
                        bar.update(false, 6, 0, 0);
                    }
                }
            }
        }
    }
    public abstract void changeRank();
    public Worker (){
        // Set initial direction and speed. Override this in your constructor.
        direction = Direction.RIGHT;
        changeSpeed (15, 20);

        // Start values for private variables
        frame = 0;
        dirX = 0;
        dirY = 0;
        idle = false;
        autoMove = false;
        stopAtEnd = false;

        // How close, in pixels, should player have to get to it's intended
        // target in clickToMove mode before movement is automatically ended
        tolerance = 1.0;

        // In case program is too laggy, this determines the max time per frame.
        // In other words, if a frame takes longer than maxFrameLength to render,
        // or the program is paused for more time, the animation will place the
        // object at the point at the distance that would have been covered in
        // maxFrameLength time.
        maxFrameLength = 0.1667; // aboutn 1/6th of a second, 6 FPS  

        // Spawn my hidden friend dot
        dot = new Dot ();

        // Set the initial timestamp for animation timer
        lastFrame = System.nanoTime();
        
        bar = new XPBar(maxXp, this);
    }

    public void addedToWorld (World w){
        // When I get added to world, set my internal double variables 
        xx = getX();
        yy = getY();
        prevX = getX();
        prevY = getY();
        setLocation ((int)Math.round(xx), (int)Math.round(yy));
        w.addObject(dot, getX(), getY());
        //setCurrentImages (moveImages[direction.getDirection()]);
        
        w.addObject(bar, getX(), getY());
    }

    /**
     * Import .. needs work to work with anything outside of SpriteFoot sprites
     * 
     * TODO: remove nDirs - should always be 4 anyway.
     */
    protected GreenfootImage[][] importMoveSprites (String baseString, int nDirs, int nFrames){
        GreenfootImage[][] temp = new GreenfootImage[nDirs][nFrames];
        for (int dir = 0; dir < nDirs; dir++){
            for (int frm = 0; frm < nFrames; frm++){
                String directionString;
                switch (dir) {
                    case 0: directionString = "Right";  break;
                    case 1: directionString = "Left";   break;
                    case 2: directionString = "Up";     break;
                    case 3: directionString = "Down";   break; 
                    default: directionString = "Error"; break;
                }

                String tempFileName = baseString + directionString + frm +".png";
                temp[dir][frm] = new GreenfootImage (tempFileName);
            }
        }
        return temp;
    }

    protected GreenfootImage[] importDeathSprites (String baseString, int nFrames){
        GreenfootImage[] temp = new GreenfootImage [nFrames];
        for (int i = 0; i < nFrames; i++){
            String fileName = baseString + i + ".png";
            temp[i] = new GreenfootImage(fileName);
        }
        return temp;
    }

    /**
     * Set the movement speed (in pixels per second) and the animation rate
     * (in frames per second)
     */
    protected void changeSpeed (int moveSpeed, int framesPerSecond)
    {
        // Only run code if something has changed
        if (this.moveSpeed != moveSpeed || this.framesPerSecond != framesPerSecond){
            this.framesPerSecond = framesPerSecond;
            this.moveSpeed = moveSpeed;

            // Figure out how many seconds per frame
            secondsPerFrame = 1.0 / this.framesPerSecond;
            // Reset animation timer
            lastFrame = System.nanoTime();
        }
    }

    /**
     * Move towards a specified actor. 
     * 
     * @param Actor the Actor to move toward at the current speed and framerate
     */
    public void moveTowards (Actor a)
    {
        dot.setTarget(a, this);
        dot.setLocation (getX(), getY());

        // Reset animation timer
        lastFrame = System.nanoTime();
        autoMove = true;
        double dx = getX() -  a.getX();
        double dy = getY() -  a.getY();
        if (Math.abs (dx) > Math.abs (dy)){ // more horizontal
            if (dx > 0){
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        } else {// more vertical
            if (dy > 0){
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }
        setCurrentImages (moveImages[direction.getDirection()]);
    }

    public void moveTowards (int targetX, int targetY)
    {
        getWorld().removeObject(targetDot);
        targetDot = null;
 
        // create a temporary object at the target lotcation
        targetDot = new Dot();
        getWorld().addObject (targetDot, targetX, targetY);

        // Now that I've made an object, I can use the other clickToMove method
        // to set my Dot to follow it
        moveTowards (targetDot);
    }

    /**
     * <p>Make this AnimatedCharacter move in a specified direction.</p>
     * 
     * <p><b>Instructions:</b></p>
     * <ol>
     * <li>Subclasses should START and then STOP movement, not call this
     * method repeatedly. This is so that the animation can run smoothly.
     * Call the stopMoving() method to stop.</li>
     *
     * <li>Intended to receive a 1 or -1 for for ONE of the parameters, and a 
     * zero (0) for the other. This method does not allow diagonal movement.</li>
     * </ol>
     * @param dirX  The direction for x movement. Should be -1, 0 or 1.
     * @param dirY  The direction for y movement. Should be -1, 0 or 1. 
     */
    protected void moveInDirection (int dirX, int dirY)
    {
        autoMove = false;
        // If there has been a change in direction
        if (this.dirX != dirX || this.dirY != dirY){

            if (dirX == 0 && dirY == 0){
                idle = true; 
                lastFrame = System.nanoTime(); // reset animation timer to start fresh
                frame = 0; // 0 is the idle frame
                setImage (moveImages[direction.getDirection()][frame]);
            } else {
                idle = false; 
                //frame = 1; // set to first frame if dir has changed
                // set the facing direction if direction has changed
                if (dirX == 1){
                    direction = Direction.RIGHT;
                } else if (dirX == -1){
                    direction = Direction.LEFT;
                } else if (dirY == 1){
                    direction = Direction.DOWN;
                } else if (dirY == -1){
                    direction = Direction.UP;
                }
            }
            setCurrentImages (moveImages[direction.getDirection()]);
            // set these variables so that I can check for changes next time
            this.dirX = dirX;
            this.dirY = dirY;
        } 
    }

    /**
     * 
     */
    public void moveInDirection (Direction direction){
        dirX = 0;
        dirY = 0;
        idle = false;
        autoMove = false;
        this.direction = direction;
        if (direction == direction.RIGHT){
            dirX = 1;
        } else if (direction == direction.LEFT){
            dirX = -1;
        } else if (direction == direction.DOWN){
            dirY = 1;
        } else if (direction == direction. UP){
            dirY = -1;
        }
        setCurrentImages (moveImages[direction.getDirection()]);
        //        moveInDirection (dirX, dirY);
    }

    // This method if you want to set an idle facing direction
    public void stopMoving (Direction direction){
        this.direction = direction;
        idle=true;
        stopMoving();
    }

    // This method if you want to just stop and use current facing direction.
    public void stopMoving ()
    {
        autoMove = false;
        dirX = 0;
        dirY = 0;
        lastFrame = System.nanoTime();
    }

    // TODO: Diagonal Movement
    protected void moveBySetRotation (int rotation){

    }

    
    /**
     * Override the default setLocation method because the backing
     * variables xx and yy need to be updated, otherwise they will
     * immediately move the player back every time it tries to move.
     */
    @Override
    public void setLocation (int x, int y){
        xx = (int)x;
        yy = (int)y;   
        // once variable have been updated, call the normal method:
        super.setLocation (x, y);
    }

    // Load the death frames
    public void death (boolean removeAfterAnimation){
        this.removeAfterAnimation = removeAfterAnimation;
        stopAtEnd = true;
        idle = false;
        frame = 0;

        setCurrentImages (deathImages);
        stopMoving(Direction.DOWN); // face downward, as that is how the death animations are set up
    }

    /**
     * Called to avoid getting stuck on an object - when you detect that you
     * have hit an object, call this method to go back to the previous position,
     * so your Actor won't get stuck in a loop where it's touching the object so
     * it cannot move. See Player class for example.
     */
    public void stepBack (){
        xx = (double)prevX;
        yy = (double)prevY;
    }

    private void setCurrentImages (GreenfootImage[] images){
        currentImages = new GreenfootImage[images.length];
        for (int i = 0; i < images.length; i++){
            currentImages[i] = images[i];
        }
    }

    /**
     * Act - do whatever the Walker wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {

        long lastAct = current;
        // determine how much time has passed since the last act
        current = System.nanoTime();
        // Find elapsed time since last frame switch in milliseconds (ms) for animation
        long elapsed = (current - lastFrame) / 1000000; 
        // Find elapsed time since last act, for movement
        long deltaTime = (current - lastAct) / 1000000;

        boolean removeMe = false; // flag that can be set to true for object self-removal

        if ((dirX == 0 && dirY == 0) && !autoMove && !stopAtEnd){ // if not moving, and not playing a terminal animation, switch to idle
            idle = true;
            lastFrame = current;    // reset animation timer so it always starts fresh in next frame if
            // next frame is animated
            frame = 0;
            //System.out.println("Detected not moving - switching to idle");
            setImage (currentImages[frame]);
        }        
        else{
            // Troubleshooting code:
            // System.out.println("System Nano: " + System.nanoTime());
            // System.out.println("Last: " + lastFrame);
            // System.out.println("Elapsed: " + elapsed);

            // Check if ready to show next frame, and if so, advance frame
            if (elapsed > secondsPerFrame * 1000 || idle){
                // note - the use of the idle variable here is to avoid restarting the animation
                // timer after idle. This way, the first frame after idle starts instantly
                lastFrame = current;
                frame++;
                idle = false;
            }

            if (frame > currentImages.length - 1){
                if(stopAtEnd){
                    frame = currentImages.length - 1;
                    if(removeAfterAnimation){
                        removeMe = true;
                    }
                } else {
                    frame = 1; // 0th frame is idle frame only, so count 1..last, not 0..last
                }

            }
            // now that the calculations are done, set the correct image
            setImage (currentImages[frame]);
        }
        // calculate delta time - how many seconds have passed since the last act (I.e. 30 fps, dT = 0.0333)
        double dT = (current-lastAct) / 1000000000.0;

        prevX = (int)Math.round(xx);
        prevY = (int)Math.round(yy);

        if (dT > maxFrameLength){
            dT = maxFrameLength;
        }

        if (autoMove){// Click to move mode
            dot.turnTowards();
            //System.out.println("Move: " + (double) moveSpeed* ((current-lastAct) / 1000000000.0));
            dot.move((double) moveSpeed* dT);

            xx = (double)dot.getX();
            yy = (double)dot.getY();

            //Stop moving if I'm close to my target
            if (Math.sqrt(Math.pow(targetDot.getX() - getX(), 2) + Math.pow(targetDot.getY() - getY(), 2)) < tolerance){
                idle = true;
                getWorld().removeObject(targetDot);
                targetDot = null;
                stopMoving();
            }

        } else { // Regular move mode
            // calculate exact new location. Decimal values will be rounded, but stored accurately, for
            // smooth animation over time
            xx += ((double)(dirX) * moveSpeed) * dT;
            yy += ((double)(dirY) * moveSpeed) * dT;

        }

        // Normalize position - makes sure it can't go outside of world      
        if (xx > getWorld().getBackground().getWidth() || xx < 0){
            xx = (double)getX();
        }
        if (yy > getWorld().getBackground().getHeight() || yy < 0){
            yy = (double)getY();
        }

        // update my location
        setLocation ((int)Math.round(xx), (int)Math.round(yy));

        if (removeMe){
            getWorld().removeObject(this);
        }
    }    

    // ENUM to keep direction related code clean. The array is set up so that 
    // index of main array corresponds with direction (I.e. img[0][2] is the 2nd frame in the RIGHT array)
    // which could also be written img[direction.getDirection()][2] to check the direction variable, of type
    // Direction, and use the corresponding int to access the correct image in the 2d array.
    public enum Direction {
        RIGHT(0), 
        LEFT(1), 
        UP(2), 
        DOWN(3);

        private final int dirCode;
        private Direction (int dirCode){
            this.dirCode = dirCode;
        }

        public int getDirection (){
            return this.dirCode;
        }

        public static Direction fromInteger(int x) {
            switch(x) {
                case 0:
                return RIGHT;
                case 1:
                return LEFT;
                case 2:
                return UP;
                case 3: 
                return DOWN;
            }
            return null;
        }
        public final static int size = Direction.values().length;
    }    

}

/**
 * Dot is a simple helper class that has it's own built in
 * precise X and Y variables (like SmoothMover) and can be moved smoothly
 */
class Dot extends Actor
{
    private GreenfootImage myImage;
    private double xx, yy;
    private int targetX, targetY;
    private Actor target;

    public Dot ()
    {
        myImage = new GreenfootImage(1,1);
        // Troubleshooting Code
        //  myImage.setColor(Color.YELLOW);
        //  myImage.fill();
        setImage(myImage);
        target = null;
    }

    public double getPreciseX(){
        return xx;
    }

    public double getPreciseY(){
        return yy;
    }

    public void addedToWorld(World w){
        xx = getX();
        yy = getY();   
    }

    public void setTarget (Actor a, Actor owner){
        target = a;
        xx = owner.getX();
        yy = owner.getY();
    }


    /**
     * Move forward by the specified exact distance.
     */
    public void move(double distance)
    {
        double radians = Math.toRadians(getRotation());
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        // Use my precise location variables to handle the movement
        double tempx = xx;
        double tempy = yy;
        xx += dx;
        yy += dy;

        setLocation ((int)Math.round(xx), (int)Math.round(yy));
    }

    /**
     * Turn towards my current target. Use a built 
     */
    public void turnTowards (){
        if (target != null){
            setRotation ((int)Math.round(Math.toDegrees((Math.atan2(target.getY() - yy, target.getX() - xx)))));
        }
    }
}





