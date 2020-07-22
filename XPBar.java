import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * XPBar (renamed XPBar for this simulation) is a resource bar widget. 
 * Resources will be displayed on bar images and can include health, mana, and
 * energy. NOTE: Despite some methods asking for multiple parameters for multiple 
 * resources, they can still be used for an object with just an hp bar, or an
 * hp and mana bar. Just input the value into the desired parameters, and input 0 for
 * parameters for unused resources.
 * 
 * @author Edmund Chow
 * @version 0.1
 */
public class XPBar extends Actor
{
    //Useful ResourceBar variables
    private int maxHealth;
    private int maxMana;
    private int maxEnergy;

    private int currHealth;
    private int currMana;
    private int currEnergy;
        
    private double healthPerc;
    private double manaPerc;
    private double energyPerc;
    
    private int greenBarSize;
    private int redBarSize;
    private int blueBarSize;
    private int yellowBarSize;
    private int greyBarSize;
    private int whiteBarSize;
    
    private Actor worker;
   
    // Declare Instance Images
    private GreenfootImage bar;
    
    // Size of resource bars
    private int BAR_WIDTH = 60;
    private int HP_BAR_HEIGHT = 8;
    private int MANA_BAR_HEIGHT = 5;
    private int OFFSET = 25;
    
    // Colors
    private Color green = new Color (0, 255, 0);
    private Color red = new Color (255, 0, 0);
    private Color blue = new Color (0, 0, 235);
    private Color grey = new Color (128, 128, 128);
    private Color yellow = new Color (255, 255, 0);
    private Color white = new Color (255, 255, 255);
        
    /**
     * Creates a XPBar resource bar object with only HP
     * 
     * @param hp Sets maximum health and current health
     */
    public XPBar(int hp, Actor worker)
    {
        bar = new GreenfootImage(BAR_WIDTH, HP_BAR_HEIGHT);
        bar.setColor(yellow);
        bar.fill();
        setImage(bar);
        if(hp > 0)
        {
            maxHealth = hp;
            currHealth = maxHealth;
        }
        this.worker = worker;
    }
        
    /**
     * Creates a XPBar resource bar object with HP and mana
     * 
     * @param hp Sets maximum and current health
     * @param m Sets maximum and current mana
     */
    public XPBar(int hp, int m)
    {
        bar = new GreenfootImage(BAR_WIDTH, HP_BAR_HEIGHT + MANA_BAR_HEIGHT);
        bar.setColor(green);
        bar.fillRect(0, 0, BAR_WIDTH, HP_BAR_HEIGHT);
        bar.setColor(blue);
        bar.fillRect(0, HP_BAR_HEIGHT, BAR_WIDTH, MANA_BAR_HEIGHT);
        setImage(bar);
        if(hp > 0 && m > 0)
        {
            maxHealth = hp;
            currHealth = maxHealth;
            maxMana = m;
            currMana = maxMana;
        }
    }
    
    /**
     * Creates a XPBar resource bar object with HP, mana and energy
     * 
     * @param hp Sets maximum and current health
     * @param m Sets maximum and current mana
     * @param e Sets maximum and current energy
     */
    public XPBar(int hp, int m, int e)
    {
        bar = new GreenfootImage(BAR_WIDTH + 30, HP_BAR_HEIGHT + MANA_BAR_HEIGHT + 20);
        bar.setColor(yellow);
        bar.fillRect(0, 0, BAR_WIDTH, HP_BAR_HEIGHT);
        bar.setColor(blue);
        bar.fillRect(0, HP_BAR_HEIGHT, BAR_WIDTH, MANA_BAR_HEIGHT);
        bar.setColor(yellow);
        bar.fillRect(BAR_WIDTH, 0, HP_BAR_HEIGHT, HP_BAR_HEIGHT + MANA_BAR_HEIGHT);
        setImage(bar);
        if(hp > 0 && m > 0 && e > 0)
        {
            maxHealth = hp;
            currHealth = maxHealth;
            maxMana = m;
            currMana = maxMana;
            maxEnergy = e;
            currEnergy = maxEnergy;
        }
    }
    
    public void act()
    {
        if(worker.getWorld() != null)
        {
            setLocation (worker.getX(), worker.getY() - OFFSET);
        }
        else
        {
            getWorld().removeObject(this);
        }
    }
    /**
     * Adds or subtracts value of current resource amounts(incremental)
     * 
     * @param b Determines whether resources are added or subtracted (true
     * adds to current resource amount, false subtracts from current resource amount) 
     * @param h Value that will be added to or subtracted from current health(must be positive value)
     * @param m Value that will be added to or subtracted from current mana(must be postive value)
     * @param e Value that will be added to or subtracted from current energy(must be postive value)
     */
    public void update(boolean b, int h, int m, int e)
    {
        if(b == true && h >= 0 && m >= 0 && e >= 0)
        {
            currHealth += h;
            if(currHealth >= maxHealth)
                {
                    currHealth = maxHealth;
                }
            currMana += m;
            if(currMana >= maxMana)
            {
                currMana = maxMana;
            }
            currEnergy += e;
            if(currEnergy >= maxEnergy)
            {
                currEnergy = maxEnergy;
            }
        }
        if(b == false && h >= 0 && m >= 0 && e >= 0)
        {
            currHealth -= h;
            if(currHealth <= 0)
            {
                currHealth = 0;
            }      
            currMana -= m;
            if(currMana <= 0)
            {
                currMana = 0;
            }
            currEnergy -= e;
            if(currEnergy <= 0)
            {
                currEnergy = 0;
            }
        }
        healthPerc = (double) currHealth / maxHealth;
        manaPerc = (double) currMana / maxMana;
        energyPerc = (double) currEnergy / maxEnergy;
        updateImage();
    }
    
    /**
     * Changes the value of current resource amounts (instantaneous) -
     * Will set current health, mana and/or energy equal to parameter values
     * (values less than 0 or greater that max are corrected to 0 and max values)
     * 
     * @param h Sets current health 
     * @param m Sets current mana
     * @param e Sets current energy
     */
    public void update(int h, int m, int e)
    {
        currHealth = h;
        if(currHealth >= maxHealth)
        {
            currHealth = maxHealth;
        }
        if(currHealth <= 0)
        {
            currHealth = 0;
        }      
        healthPerc = (double) currHealth / maxHealth;
        currMana = m;
        if(currMana >= maxMana)
        {
            currMana = maxMana;
        }
        if(currMana <= 0)
        {
            currMana = 0;
        }
        manaPerc = (double) currMana / maxMana;
        currEnergy = e;
        if(currEnergy >= maxEnergy)
        {
            currMana = maxMana;
        }
        if(currEnergy <= 0)
        {
            currEnergy = 0;
        }
        energyPerc = (double) currEnergy / maxEnergy;
        updateImage();
    }
    
    /**
     * returns current health value
     */
    public int getCurrHealth()
    {
        return currHealth;
    }
    
    /**
     * returns current mana value
     */
    public int getCurrMana()
    {
        return currMana;
    }
    
    /**
     * returns current energy value
     */
    public int getCurrEnergy()
    {
        return currEnergy;
    }
    
    private void updateImage()
    {
        greenBarSize = (int) (healthPerc * BAR_WIDTH);
        redBarSize = BAR_WIDTH - greenBarSize;
        blueBarSize = (int) (manaPerc * BAR_WIDTH);
        greyBarSize = BAR_WIDTH - blueBarSize;
        yellowBarSize = (int) (energyPerc * (HP_BAR_HEIGHT + MANA_BAR_HEIGHT));
        whiteBarSize =  (HP_BAR_HEIGHT + MANA_BAR_HEIGHT) - yellowBarSize;
        bar.setColor(yellow);
        bar.fillRect(0, 0, greenBarSize, HP_BAR_HEIGHT);
        bar.setColor(grey);
        bar.fillRect(greenBarSize, 0, redBarSize, HP_BAR_HEIGHT);
        bar.setColor(blue);
        bar.fillRect(0, HP_BAR_HEIGHT, blueBarSize, MANA_BAR_HEIGHT);
        bar.setColor(grey);
        bar.fillRect(blueBarSize, HP_BAR_HEIGHT, greyBarSize, MANA_BAR_HEIGHT);
        bar.setColor(yellow);
        bar.fillRect(greenBarSize+redBarSize, whiteBarSize, HP_BAR_HEIGHT, yellowBarSize);
        bar.setColor(white);
        bar.fillRect(greenBarSize+redBarSize, 0, HP_BAR_HEIGHT, whiteBarSize );
        this.setImage(bar);
    }
}