/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author Ryan Leahy
 */
public class WorldGenerator
{
    private Main myMain;
    private BlockDatabase myDatabase;
    
    public WorldGenerator(Main mainClass, BlockDatabase databaseClass)
    {
        myMain = mainClass;
        myDatabase = databaseClass;
        initiateWorld();
    }
    
    //Will write basic implementation for the moment but later this will become more advanced and will handle procedural generation
    private void initiateWorld()
    {
        for(int x = 0; x < 202; x += 2)
            for(int z = 0; z < 202; z += 2)
                myDatabase.createBlock(1, new Vector3f(x, -7, z));
    }
    
    //this will be used to render and derender as someone moves through chunks
    private void expandWorld()
    {
        
    }
}
