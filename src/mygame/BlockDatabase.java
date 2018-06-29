/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author rplea
 */
public class BlockDatabase
{
    private Main myMain;
    
    public BlockDatabase(Main mainClass)
    {
        myMain = mainClass;
    }
    
    /**
     * Method creates a block at a passed coordinate
     * for ID's look at https://minecraft-ids.grahamedgecombe.com/
     * 
     * @param id
     * @param coordinates 
     */
    public void createBlock(int id, Vector3f coordinates)
    {
        //this tree will get uglier as it grows
        if(id == 1)
            createGrass(coordinates);
    }
    
    private void createGrass(Vector3f coordinates)
    {
        new Block(1, "Grass", myMain.getAssetManager().loadModel("Models/GrassBlock/GrassBlock.j3o"), myMain.getGamePhysics(), myMain, 0f, coordinates);
    }
}

