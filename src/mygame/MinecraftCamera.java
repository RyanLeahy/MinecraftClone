package mygame;

import com.jme3.input.FlyByCamera; //camera that allows WASD and mouse control
import com.jme3.renderer.Camera; //default camera

public class MinecraftCamera
{
    private FlyByCamera gameCam; //camera instance variable

    //constructor that takes in the default camera from main
    public MinecraftCamera (Camera defaultCam)
    {
        gameCam = new FlyByCamera(defaultCam); //turns default cam into fly cam
        gameCam.setEnabled(true);
        moveSpeed(10000);
        rotationSpeed(10);
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
