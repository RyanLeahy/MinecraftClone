package mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera; //default camera

public class MinecraftCamera
{
    private Camera gameCam; //camera instance variable
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Physics gamePhysics;
   
    //walking instance variables
    private Vector3f walkDirection = new Vector3f(); //three vectors are used for walking
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    private boolean[] keyPress;
    
    //constructor that takes in the default camera from main
    public MinecraftCamera (Main mainClass, Camera cam)
    {
        myMain = mainClass;
        gameCam = cam;
        gamePhysics = myMain.getGamePhysics();
        keyPress = myMain.getKeyMapping().getKeyPress();
    }

    /**
     * Method slows down the speed of the player
     */
    public void CrouchSpeed()
    {
        setCamDir(getCam().getDirection(), 0.15f);
        setCamLeft(getCam().getLeft(), 0.1f);
    }
    
    /**
     * Method normalizes the speed of the player
     */
    public void normalSpeed()
    {
        setCamDir(getCam().getDirection(), 0.6f);
        setCamLeft(getCam().getLeft(), 0.4f);
    }
    
    /**
     * Method returns the camera held inside the class
     * 
     * @return gameCam 
     */
    public Camera getCam()
    {
        return gameCam;
    }
    
    private void setCamDir(Vector3f coordinates, float speed)
    {
        camDir.set(coordinates).multLocal(speed);
    }
    
    private void setCamLeft(Vector3f coordinates, float speed)
    {
        camLeft.set(coordinates).multLocal(speed);
    }
    
    /**
     * Method returns the local walk direction to the caller
     * 
     * @return walkDirection
     */
    public Vector3f getWalkDirection()
    {
        return walkDirection;
    }
    
    /**
     * Method returns the local Cam direction to the caller
     * 
     * @return camDir
     */
    public Vector3f getCamDir()
    {
        return camDir;
    }
    
    /**
     * Method returns the local Cam Left to the caller
     * 
     * @return camLeft 
     */
    public Vector3f getCamLeft()
    {
        return camLeft;
    }
    
    public void simpleUpdate(float tpf)
    {
        if (keyPress[KeyMapping.Keys.CROUCH.ordinal()]) //if the shift key is pressed
        {
            CrouchSpeed();
        }
        else //if its not pressed
        {
            normalSpeed();
        }
        
        //this handles player movement
        getWalkDirection().set(0, 0, 0);
        if (keyPress[KeyMapping.Keys.LEFT.ordinal()]) 
        {
            getWalkDirection().addLocal(getCamLeft());
        }
        if (keyPress[KeyMapping.Keys.RIGHT.ordinal()]) 
        {
            getWalkDirection().addLocal(getCamLeft().negate());
        }
        if (keyPress[KeyMapping.Keys.UP.ordinal()]) 
        {
            getWalkDirection().addLocal(getCamDir());
        }
        if (keyPress[KeyMapping.Keys.DOWN.ordinal()]) 
        {
            getWalkDirection().addLocal(getCamDir().negate());
        }
        getWalkDirection().setY(0); //this makes it so pointing at the sky doesn't actually move the character into the sky, the only way that the player should raise in Y is by jumping
        gamePhysics.getCharacterControl().setWalkDirection(getWalkDirection());
        getCam().setLocation(gamePhysics.getCharacterControl().getPhysicsLocation());
    }
}
