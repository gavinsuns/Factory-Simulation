import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class NukeGif here.
 * 
 * @author Anson
 * @version 1.01
 */
public class NukeGif extends Actor
{
    GifImage image = new GifImage("nuke.gif");
    /**
     * Act - do whatever the NukeGif wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
       setImage(image.getCurrentImage());
       
    }    
}
