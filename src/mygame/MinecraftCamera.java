package mygame;

import com.jme3.renderer.Camera; //default camera

public class MinecraftCamera
{
    private Camera gameCam; //camera instance variable
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    
    //constructor that takes in the default camera from main
    public MinecraftCamera (Main mainClass, Camera cam)
    {
        myMain = mainClass;
        gameCam = cam;
        
        moveSpeed(50f);
        rotationSpeed(1f);
    }

    //allows the speed of movement to be changed
    public void moveSpeed(float speed)
    {
        //gameCam.setMoveSpeed(speed); 
    }

    //alows the speed of rotation to be changed
    public void rotationSpeed(float speed)
    {
        //gameCam.setRotationSpeed(speed);
    }
    
    public Camera getCam()
    {
        return gameCam;
    }
}
