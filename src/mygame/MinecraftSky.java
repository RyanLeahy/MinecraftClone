/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author Ryan Leahy
 */
public class MinecraftSky
{
    private Main myMain;
    
    public MinecraftSky(Main mainClass)
    {
        myMain = mainClass;
        setSky();
    }
    
    //CHANGE IMPLEMENTATION
    public void setSky()
    {
        myMain.getViewPort().setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
    }
    
}
