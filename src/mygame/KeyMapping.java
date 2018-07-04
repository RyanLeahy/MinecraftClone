/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;

/**
 *
 * @author Ryan Leahy
 */
public class KeyMapping
{
    private Main myMain;
    private InputManager inputManager;
    private MinecraftCamera gameCam;
    private Physics gamePhysics;
    private WorldGenerator gameWorldGen;
    
    //key handling instance variables
    private boolean[] keyPress; //boolean array to store if keys are pressed or not
    public enum Keys {LEFT, RIGHT, UP, DOWN, CROUCH, BREAK, PLACE, SELECT_BLOCK, CHANGE_PERSPECTIVE}; //enumeration for indexing the array
    
    public KeyMapping(Main mainClass)
    {
        myMain = mainClass;
        inputManager = myMain.getInputManager();
        gameCam = myMain.getMinecraftCam();
        gamePhysics = myMain.getGamePhysics();
        gameWorldGen = myMain.getWorldGenerator();
        keyPress = new boolean[Keys.values().length]; //creates a boolean array the same size as the amount of enumerations
        defaultMap();
    }
    
    //method maps all the default keys
    private void defaultMap()
    {
        addMap("Left", new KeyTrigger(KeyInput.KEY_A));
        addMap("Right", new KeyTrigger(KeyInput.KEY_D));
        addMap("Up", new KeyTrigger(KeyInput.KEY_W));
        addMap("Down", new KeyTrigger(KeyInput.KEY_S));
        addMap("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        addMap("Crouch", new KeyTrigger(KeyInput.KEY_LSHIFT));
        addMap("Break", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        addMap("Place", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        addMap("SelectBlock", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        addMap("ChangePerspective", new KeyTrigger(KeyInput.KEY_F5));
    }
    
    /**
     * This method is for adding mapping
     * 
     * @param inputName
     * @param inputKey
     */
    public void addMap(String inputName, Trigger inputKey)
    {
        inputManager.addMapping(inputName, inputKey);
        inputManager.addListener(myMain, inputName);
    }
    
    /**
     * This method is for reMapping already set keys
     * 
     * @param inputName
     * @param inputKey
     */
    public void reMap(String inputName, Trigger inputKey)
    {
        inputManager.deleteMapping(inputName);
        inputManager.addMapping(inputName, inputKey);
        inputManager.addListener(myMain, inputName);
    }
    
    //This method handles what happens when a button is clicked, usually setting a boolean to true indicating a key was pressed
    public void onAction(String name, boolean isPressed, float tpf)
    {
        switch (name)
        {
            case "Left":
                if (isPressed) { keyPress[Keys.LEFT.ordinal()] = true; } else { keyPress[Keys.LEFT.ordinal()] = false; }
                break;
            case "Right":
                if (isPressed) { keyPress[Keys.RIGHT.ordinal()] = true; } else { keyPress[Keys.RIGHT.ordinal()] = false; }
                break;
            case "Up":
                if (isPressed) { keyPress[Keys.UP.ordinal()] = true; } else { keyPress[Keys.UP.ordinal()] = false; }
                break;
            case "Down":
                if (isPressed) { keyPress[Keys.DOWN.ordinal()] = true; } else { keyPress[Keys.DOWN.ordinal()] = false; }
                break;
            case "Crouch":
                if (isPressed) { keyPress[Keys.CROUCH.ordinal()] = true; } else { keyPress[Keys.CROUCH.ordinal()] = false; }
                break;
            case "Jump":
                if (gamePhysics.getCharacterCollision() && isPressed) {gamePhysics.getCharacterControl().jump(new Vector3f(0, 10f, 0));} else { gamePhysics.setCharacterCollision(false);} //if key is pressed and the character is touching an object
                break;
            case "Break":
                if (isPressed) { keyPress[Keys.BREAK.ordinal()] = true; } else { keyPress[Keys.BREAK.ordinal()] = false; }
                break;
            case "Place":
                if (isPressed) { keyPress[Keys.PLACE.ordinal()] = true; } else { keyPress[Keys.PLACE.ordinal()] = false; }
                break;
            case "SelectBlock":    
                if (isPressed) { keyPress[Keys.SELECT_BLOCK.ordinal()] = true; } else { keyPress[Keys.SELECT_BLOCK.ordinal()] = false; }
                break;
            case "ChangePerspective":
                if (isPressed) { keyPress[Keys.CHANGE_PERSPECTIVE.ordinal()] = true;} else { keyPress[Keys.CHANGE_PERSPECTIVE.ordinal()] = false; }
                break;
            default:
                break;
        }
    }
    
    public boolean[] getKeyPress()
    {
        return keyPress;
    }
    
    //called frequently, performs game state updates
    public void simpleUpdate(float tpf) 
    {
        
    }
}
