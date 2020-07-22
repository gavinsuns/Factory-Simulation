import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author Anson
 * @version 1.01
 */
public class MyWorld extends greenfoot.World
{
    //Constants
    public static final int BELT_ONE_Y = 80;
    public static final int BELT_TWO_Y = 190;
    public static final int BELT_THREE_Y = 300;
    public static final int BELT_FOUR_Y = 410;
    public static final int BELT_FIVE_Y = 520;
    
    //Variables neccessary for updating the world.
    private int workerSpawnRate;
    private int numberOfWorkers;
    private int tanksMade;
    private int gunsMade;
    private int nukeMade;
    private double enemyProgress =100;
    private double enemyProducts;
    private double chanceOfVictory;
    private double productsMade = 0;
    private int timeElapsed;
    private int time;
    private int time2;
    private int totalSalary = 0;
    
    //Import timer and create scoreboard for stats.
    SimpleTimer timer = new SimpleTimer();
    private ScoreBoard score = new ScoreBoard();
    
    //Background Music: Toad's Factory from Mario Kart Wii.
    GreenfootSound bgmusic = new GreenfootSound("bgmusic.wav");
    
    //Create multiple conveyor belts objects and any neccessary variables
    //so they can be moved at different speeds.
    public ConveyorBelt belt1 = new ConveyorBelt();
    public ConveyorBelt belt2 = new ConveyorBelt();
    public ConveyorBelt belt3 = new ConveyorBelt();
    public ConveyorBelt belt4 = new ConveyorBelt();
    public ConveyorBelt belt5 = new ConveyorBelt();
    private int beltSpeed1 = 3;
    private int beltSpeed2 = 3;
    private int beltSpeed3 = 1;
    private int beltSpeed4 = 1;
    private int beltSpeed5 = 2;
    private int belt1Counter = 0;
    private int belt2Counter = 0;
    private int belt3Counter = 0;
    private int belt4Counter = 0;
    private int belt5Counter = 0;
    private double disasterChance = 0;
    private double multiplier = 10;
    
    //Spawnrate of the products.
    private int gunSpawnRate = 100;
    private int tankSpawnRate = 500;
    private int nukeSpawnRate = 1750;

    private boolean s0 = false;
    private boolean s1 = false;
    private boolean s2 = false;
    private boolean s3 = false;
    private boolean s4 = false;
    private boolean s5 = false;
    private boolean s6 = false;
    private boolean s7 = false;
    private boolean s8 = false;
    private boolean s9 = false;
    private boolean s10 = false;
    private boolean s11 = false;
    private boolean s12 = false;
    private boolean s13 = false;
    private boolean s14 = false;
    
    //PUBLIC METHODS
    /**
     * Constructor for objects of class MyWorld.
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 640, 1); 
        addObject(belt1,300,80);
        addObject(belt2,300,190);
        addObject(belt3,300,300);
        addObject(belt4,300,410);
        addObject(belt5,300,520);
        addObject(score,500,600);
        timer.mark();
        bgmusic.play();
    }
    /**
     * Actions to be called every act.
     */
    public void act()
    {
        time = timer.millisElapsed();
        updateInfoBoard();
        moveBelts();
        spawnItems();
        disasterChance();
        enemyProgress = (enemyProgress+enemyWarEffort());
        chanceOfVictory = (int)(((3*tanksMade+gunsMade+7*nukeMade)/enemyProgress)*100);        
        if(!s0 || !s1 || !s2 || !s3 || !s4 || !s5 || !s6 || !s7 || !s8 || !s9 || !s10 || !s11 || !s12 || !s13 || !s14)
        {
            spawnPeasant();
        }
    }
    /**
     * Increases the number of products made by 1.
     */
    public void increaseProductsMade()
    {
        productsMade++;
    }
    /**
     * Increases the number of guns made by 1.
     */
    public void increaseGunsMade()
    {
        gunsMade++;
    }
    /**
     * Increases the number of tanks made by 1.
     */
    public void increaseTanksMade()
    {
        tanksMade++;
    }
    /**
     * Increases the number of nukes made by 1.
     */
    public void increaseNukesMade()
    {
        nukeMade++;
    }
    /**
     * Calculates the chance of a disaster happening.
     */
    public void disasterChance(){
        disasterChance = numberOfWorkers+multiplier;
        if(time%30000 == 0){
            multiplier= Math.pow(multiplier,1.1);
        }
    }
    /**
     * Increases the number of workers by 1.
     */
    public void totalNumberWorkers(int input)
    {
        numberOfWorkers += input;
    }
    /**
     * Returns a random value to signfy the value of enemy products.
     */
    public double enemyWarEffort()
    {
        enemyProducts =  Greenfoot.getRandomNumber(6);
        return enemyProducts;
    }
    
    //PRIVATE METHODS
    /**
     *  Moves the belts at their desired speeds every act.
     */
    private void moveBelts()
    {
        belt1.setLocation(200+belt1Counter,80);
        belt2.setLocation(200+belt2Counter,190);
        belt3.setLocation(200+belt3Counter,300);
        belt4.setLocation(200+belt4Counter,410);
        belt5.setLocation(200+belt5Counter,520);
        if(belt1Counter>500){
            belt1Counter=0;
        }
        if(belt3Counter>500){
            belt3Counter=0;
        }
        if(belt2Counter>500){
            belt2Counter=0;
        }
        if(belt4Counter>500){
            belt4Counter=0;
        }
        if(belt5Counter>500){
            belt5Counter=0;
        }
        belt1Counter+=beltSpeed1;
        belt2Counter+=beltSpeed2;
        belt3Counter+=beltSpeed3;
        belt4Counter+=beltSpeed4;
        belt5Counter+=beltSpeed5;
    }
    /**
     * Updates the information board.
     */
    private void updateInfoBoard()
    {
        showText(numberOfWorkers+"",220,620);
        showText(productsMade+"",335,620);
        showText(chanceOfVictory+"%",450,620);
        showText(0+"%",550,620);
        showText(0+"%",680,620);
        showText((time/60000)+":",800,620);
        showText((time%60000)/1000+"",830,620);
    }
    
    //Product Class
    /**
     * Responsible for randomly spawning items on their appropriate belts.
     */
    private void spawnItems()
    {
        //Get random numbers based off the spawnrate to determine which product(s) to spawn with each call.
        int gunSpawnNum = Greenfoot.getRandomNumber(gunSpawnRate);
        int tankSpawnNum = Greenfoot.getRandomNumber(tankSpawnRate);
        int nukeSpawnNum = Greenfoot.getRandomNumber(nukeSpawnRate);
        if(gunSpawnNum == 1)
        {
            int belt = Greenfoot.getRandomNumber(2);
            //Choose which belt to spawn each item on.
            if(belt == 0)
            {
                addObject(new Gun(), 5, BELT_ONE_Y);
            }
            else if(belt == 1)
            {
                addObject(new Gun(), 5, BELT_TWO_Y);
            }
        }
        if(tankSpawnNum == 1)
        {
            int belt = Greenfoot.getRandomNumber(2);
            //Choose which belt to spawn each item on.
            if(belt == 0)
            {
                addObject(new Tank(), 5, BELT_THREE_Y);
            }
            else if(belt == 1)
            {
                addObject(new Tank(), 5, BELT_FOUR_Y);
            }
        }
        if(nukeSpawnNum == 1)
        {
            addObject(new Nuke(), 10, belt5.getY());
        }
    }
    
    //Worker Class
    public void spawnPeasant()
    {
        int x = Greenfoot.getRandomNumber(15);
        if(x == 0 && !s0)
        {
            Peasant p = new Peasant(0,240); 
            addObject(p,0,BELT_ONE_Y-52);
            s0 = true;
        }    
        else if(x == 1 && !s1)
        {
            Peasant p = new Peasant(1,480);
            addObject(p,0,BELT_ONE_Y-52);
            s1 = true;
        }
        else if(x == 2 && !s2)
        {
            Peasant p = new Peasant(2,720);
            addObject(p,0,BELT_ONE_Y-52);
            s2 = true;
        }
        else if(x == 3 && !s3)
        {
            Peasant p = new Peasant(3,240);
            addObject(p,0,BELT_TWO_Y-52);
            s3 = true;
        }
        else if(x == 4 && !s4)
        {
            Peasant p = new Peasant(4,480);
            addObject(p,0,BELT_TWO_Y-52);
            s4 = true;
        }
        else if(x == 5 && !s5)
        {
            Peasant p = new Peasant(5,720);
            addObject(p,0,BELT_TWO_Y-52);
            s5 = true;
        }
        else if(x == 6 && !s6)
        {
            Peasant p = new Peasant(6,240);
            addObject(p,0,BELT_THREE_Y-52);
            s6 = true;
        }
        else if(x == 7 && !s7)
        {
            Peasant p = new Peasant(7,480);
            addObject(p,0,BELT_THREE_Y-52);
            s7 = true;
        }
        else if(x == 8 && !s8)
        {
            Peasant p = new Peasant(8,720);
            addObject(p,0,BELT_THREE_Y-52);
            s8 = true;
        }
        else if(x == 9 && !s9)
        {
            Peasant p = new Peasant(9,240);
            addObject(p,0,BELT_FOUR_Y-52);
            s9 = true;
        }
        else if(x == 10 && !s10)
        {
            Peasant p = new Peasant(10,480);
            addObject(p,0,BELT_FOUR_Y-52);
            s10 = true;
        }
        else if(x == 11 && !s11)
        {
            Peasant p = new Peasant(11,720);
            addObject(p,0,BELT_FOUR_Y-52);
            s11 = true;
        }
        else if(x == 12 && !s12)
        {
            Peasant p = new Peasant(12,240);
            addObject(p,0,BELT_FIVE_Y-52);
            s12 = true;
        }
        else if(x == 13 && !s13)
        {
            Peasant p = new Peasant(13,480);
            addObject(p,0,BELT_FIVE_Y-52);
            s13 = true;
        }
        else if(x == 14 && !s14)
        {
            Peasant p = new Peasant(14,720);
            addObject(p,0,BELT_FIVE_Y-52);
            s14 = true;
        }
    }
    public void emptyStation(int s)
    {
        if(s == 0) { s0 = false; }
        if(s == 1) { s1 = false; }
        if(s == 2) { s2 = false; }
        if(s == 3) { s3 = false; }
        if(s == 4) { s4 = false; }
        if(s == 5) { s5 = false; }
        if(s == 6) { s6 = false; }
        if(s == 7) { s7 = false; }
        if(s == 8) { s8 = false; }
        if(s == 9) { s9 = false; }
        if(s == 10) { s10 = false; }
        if(s == 11) { s11 = false; } 
        if(s == 12) { s12 = false; }
        if(s == 13) { s13 = false; }
        if(s == 14) { s14 = false; }
    }
}









