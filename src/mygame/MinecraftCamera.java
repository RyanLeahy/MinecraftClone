package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera; //default camera
import com.jme3.renderer.ViewPort;

public class MinecraftCamera 
{
    private Camera gameCam; //camera instance variable
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Physics gamePhysics;
   
    //walking instance variables
    private Vector3f walkDirection;
    private Vector3f camDir;
    private Vector3f camLeft;
    
    private Camera frontPerspective;
    private Camera backPerspective;
    ViewPort frontView, backView;
    
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
        frontPerspective = gameCam.clone();
        backPerspective = gameCam.clone();
        curPerspective = 0;
        frontView = myMain.getRenderManager().createMainView("frontPerspective", frontPerspective);
                frontView.setClearFlags(true, true, true);
                frontView.attachScene(myMain.getRootNode());
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
    
    private void setPerspective(Vector3f coords, Quaternion rotation)
    {
        float x,y,z;
        Vector3f manipulate;
        
        
        switch (curPerspective)
        {
            case 1:
                x = coords.getX() + (10 * getCam().getDirection().getX());
                y = coords.getY() + (10 * getCam().getDirection().getY());
                z = coords.getZ() + (10 * getCam().getDirection().getZ());
                manipulate = new Vector3f(x,y,z);
                frontPerspective.setLocation(manipulate);
                frontPerspective.setRotation(rotation);
                backPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort( 0.5f , 1.0f  ,  0.0f , 0.5f);
                getCam().setLocation(coords);
                frontPerspective.setViewPort(0,1,0,1);
                
                break;
            case 2:
                getCam().setLocation(coords);
                frontPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort(0,1,0,1);
                break;
            default:
                getCam().setLocation(coords);
                frontPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort(0,1,0,1);
                System.out.println(getCam().getLocation() + " " + getCam().getDirection());
                break;
        }
        
        if(curPerspective == 2)
            curPerspective = 0;
    }
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
        switch(name)
        {
            case "ChangePerspective":
                if(isPressed) { curPerspective++;}
                break;
        }
    }
    
    public void onAnalog(String name, float value, float tpf)
    {
        
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
        setPerspective(gamePhysics.getCharacterControl().getPhysicsLocation(), getCam().getRotation()); 
    }
}
