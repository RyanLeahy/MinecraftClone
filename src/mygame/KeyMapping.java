/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Ryan Leahy
 */
public class KeyMapping
{
    private Main myMain;
    private InputManager inputManager;
    
    public KeyMapping(Main mainClass)
    {
        myMain = mainClass;
        inputManager = myMain.getInputManager();
        defaultMap();
    }
    
    /*
        method maps all the default keys
    */
    private void defaultMap()
    {
        addMap("Left", new KeyTrigger(KeyInput.KEY_A));
        addMap("Right", new KeyTrigger(KeyInput.KEY_D));
        addMap("Up", new KeyTrigger(KeyInput.KEY_W));
        addMap("Down", new KeyTrigger(KeyInput.KEY_S));
        addMap("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        addMap("Crouch", new KeyTrigger(KeyInput.KEY_LSHIFT));
    }
    
    /**
     * This method is for adding mapping
     * 
     * @param String inputName, KeyTrigger inputKey
     */
    public void addMap(String inputName, KeyTrigger inputKey)
    {
        inputManager.addMapping(inputName, inputKey);
        inputManager.addListener(myMain, inputName);
    }
    
    /**
     * This method is for reMapping already set keys
     */
    public void reMap(String inputName, KeyTrigger inputKey)
    {
        inputManager.deleteMapping(inputName);
        inputManager.addMapping(inputName, inputKey);
        inputManager.addListener(myMain, inputName);
    }
}
