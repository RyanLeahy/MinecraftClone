/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.cubes.Block;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.CubesSettings;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;

/**
 *
 * @author rplea
 */
public class BlockDatabase
{
    private Main myMain;
    private CubesSettings settings;
    
    public BlockDatabase(Main mainClass)
    {
        myMain = mainClass;
        settings = new CubesSettings(myMain);
    }
    
    private void initiateSettings()
    {
        settings.setDefaultBlockMaterial("assets/Textures/textureatlas.png");
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
        //new Block(1, "Grass", myMain.getAssetManager().loadModel("Models/GrassBlock/GrassBlock.j3o"), myMain.getGamePhysics(), myMain, 0f, coordinates);
        BlockManager.register(new Block(new BlockSkin[]{
            new BlockSkin(new BlockSkin_TextureLocation(0,1), false),
            new BlockSkin(new BlockSkin_TextureLocation(0,2), false),
            new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
            new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
            new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
            new BlockSkin(new BlockSkin_TextureLocation(0,0), false)
        }));
    }
}

