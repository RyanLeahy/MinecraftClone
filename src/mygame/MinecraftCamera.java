package mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera; //default camera

public class MinecraftCamera
{
    private Camera gameCam; //camera instance variable
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Physics gamePhysics;
   
    //walking instance variables
    private Vector3f walkDirection;
    private Vector3f camDir;
    private Vector3f camLeft;
    
    private boolean[] keyPress;
    
    private int curPerspective;
    
    //constructor that takes in the default camera from main
    public MinecraftCamera (Main mainClass, Camera cam)
    {
        myMain = mainClass;
        gameCam = cam;
        gamePhysics = myMain.getGamePhysics();
        keyPress = myMain.getKeyMapping().getKeyPress();
        walkDirection = new Vector3f();
        camDir = new Vector3f();
        camLeft = new Vector3f();
        curPerspective = 0;
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
    
    private Vector3f setPerspective(Vector3f coords)
    {
        Vector3f ret = coords;
        Vector3f temp;
        
        switch (curPerspective)
        {
            case 1:
                
                return ret;
            case 2:
                return ret;
            default:
                return ret;
        }
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
        walkDirection.set(0, 0, 0);
        if (keyPress[KeyMapping.Keys.LEFT.ordinal()]) 
        {
            walkDirection.addLocal(camLeft);
        }
        if (keyPress[KeyMapping.Keys.RIGHT.ordinal()]) 
        {
            walkDirection.addLocal(camLeft.negate());
        }
        if (keyPress[KeyMapping.Keys.UP.ordinal()]) 
        {
            walkDirection.addLocal(camDir);
        }
        if (keyPress[KeyMapping.Keys.DOWN.ordinal()]) 
        {
            walkDirection.addLocal(camDir.negate());
        }
        walkDirection.setY(0); //this makes it so pointing at the sky doesn't actually move the character into the sky, the only way that the player should raise in Y is by jumping
        gamePhysics.getCharacterControl().setWalkDirection(walkDirection);
        
        //if the key to change the perspective is called increase the current perspective int
        if(keyPress[KeyMapping.Keys.CHANGE_PERSPECTIVE.ordinal()])
        {
            curPerspective++;
            if (curPerspective == 2) //if it reaches the max amount of perspectives reset it
                curPerspective = 0;
        }    
            getCam().setLocation(setPerspective(gamePhysics.getCharacterControl().getPhysicsLocation()));
    }
}
