package mygame;

import com.jme3.input.FlyByCamera; //camera that allows WASD and mouse control
import com.jme3.renderer.Camera; //default camera

public class MinecraftCamera
{
    private FlyByCamera gameCam; //camera instance variable
    private Camera defaultCam;
    //constructor that takes in the default camera from main
    public MinecraftCamera (Camera cam)
    {
        defaultCam = cam;
        gameCam = new FlyByCamera(defaultCam); //turns default cam into fly cam
        gameCam.setEnabled(true);
        moveSpeed(10000f);
        rotationSpeed(10000f);
    }

    //allows the speed of movement to be changed
    public void moveSpeed(float speed)
    {
        gameCam.setMoveSpeed(speed);
    }

    //alows the speed of rotation to be changed
    public void rotationSpeed(float speed)
    {
        gameCam.setRotationSpeed(speed);
    }
}
