/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;

/**
 *
 * @author rplea
 */
public class Physics
{
    private BulletAppState bulletAppState; //gives access to physics libraries
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    
    public Physics(Main mainClass)
    {
        bulletAppState = new BulletAppState();
        myMain.addState(bulletAppState);
        
    }
}
