/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author Ryan Leahy
 */
public class GUI 
{
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Node guiNode;
    
    public GUI(Main mainClass)
    {
        myMain = mainClass;
        guiNode = myMain.getGuiNode();
        initCrossHair();
    }
    
    /*
    Makes a crosshair for the screen and places it
    */
    private void initCrossHair()
    {
        Picture crossHair = new Picture("CrossHair");
        crossHair.setImage(myMain.getAssetManager(), "Interface/GUIElements/crosshair.png", true);
        crossHair.setWidth(myMain.getSettings().getWidth() / 2);
        crossHair.setHeight(myMain.getSettings().getHeight() / 2);
        crossHair.setPosition(myMain.getSettings().getWidth()/4, myMain.getSettings().getHeight()/4);
        guiNode.attachChild(crossHair);
    }
}

