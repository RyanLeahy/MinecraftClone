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
        backView = myMain.getRenderManager().createMainView("backPerspective", backPerspective);
        backView.setClearFlags(true, true, true);
        backView.attachScene(myMain.getRootNode());
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
        Vector3f manipulateCoords;
        Quaternion manipulateRotate = rotation.clone();
        
        switch (curPerspective)
        {
            case 1:
                x = coords.getX() + (5 * gameCam.getDirection().getX()); //multiply the amount of blocks you want it to go out by with the direction number, the direction is a float between 0-1 and it helps make the position not fixed but able to rotate with the cam
                y = coords.getY() + (1 * gameCam.getDirection().getY());
                z = coords.getZ() + (5 * gameCam.getDirection().getZ());
                manipulateCoords = new Vector3f(x,y,z);
                manipulateRotate.lookAt(gameCam.getDirection().negate(), gameCam.getUp()); //look at the opposite direction as the main cam, use it's up axis
                frontPerspective.setLocation(manipulateCoords);
                frontPerspective.setRotation(manipulateRotate);
                backPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort( 0.5f , 1.0f  ,  0.0f , 0.5f);
                getCam().setLocation(coords);
                frontPerspective.setViewPort(0,1,0,1);
                
                break;
            case 2:
                x = coords.getX() + (-5 * gameCam.getDirection().getX());
                y = coords.getY() + (-1 * gameCam.getDirection().getY());
                z = coords.getZ() + (-5 * gameCam.getDirection().getZ());
                manipulateCoords = new Vector3f(x,y,z);
                backPerspective.setLocation(manipulateCoords);
                backPerspective.setRotation(manipulateRotate);
                frontPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort( 0.5f , 1.0f  ,  0.0f , 0.5f);
                getCam().setLocation(coords);
                backPerspective.setViewPort(0,1,0,1);
                break;
            default:
                getCam().setLocation(coords);
                frontPerspective.setViewPort(0,0,0,0);
                backPerspective.setViewPort(0,0,0,0);
                getCam().setViewPort(0,1,0,1);
                break;
        }
        
        if(curPerspective == 3)
            curPerspective = 0;
    }
    
    //prevents camera from doing a 360 by locking it if the direction becomes 1 or 0
    private void lockCam()
    {
        Quaternion manipulateRotation = getCam().getRotation().clone();
        Vector3f manipulateDirection = getCam().getDirection().clone();
        if(getCam().getDirection().getY() < -0.989)
        {
            manipulateDirection.setY(-0.987f);
            manipulateRotation.lookAt(manipulateDirection, getCam().getUp());
            getCam().setRotation(manipulateRotation);
        }
        else if(getCam().getDirection().getY() > 0.95)
        {    
            manipulateDirection.setY(0.94f);
            manipulateRotation.lookAt(manipulateDirection, getCam().getUp());
            getCam().setRotation(manipulateRotation);
        }
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
        
        setPerspective(gamePhysics.getCharacterControl().getPhysicsLocation(), getCam().getRotation()); //this handles which camera mode is being used when f5 is pressed
        
        //lockCam(); //this handles making sure the camera doesn't do a sweet kick flip (can't rotate upside down)
    }
}
