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

/**
 *
 * @author rplea
 */
public class BlockDatabase
{
    private Main myMain;
    private CubesSettings settings;
    private static Block[] registeredBlocks;
    private final int MAX_BLOCKS = 453;
    
    public BlockDatabase(Main mainClass)
    {
        myMain = mainClass;
        settings = new CubesSettings(myMain);
        registeredBlocks = new Block[MAX_BLOCKS];
        initiateSettings();
        initiateBlocks();
    }
    
    private void initiateSettings()
    {
        settings.setDefaultBlockMaterial("Textures/textureatlas.png");
        settings.setBlockSize(1);
    }
    
    private void initiateBlocks()
    {
        this.createGrass();
        this.createDirt();
        this.createStone();
        this.createCobblestone();
        this.createDiamondOre();
        this.createIronOre();
        this.createGoldOre();
        this.createRedstoneOre();
        this.createCoalOre();
        this.createOakWood();
        this.createBedrock();
        this.createOakLeaves();
        this.createOakWoodPlank();
        this.createGlass();
        this.createCraftingTable();
        this.createFurnace();
    }
    
    /**
     * Method passes back the instance of CubeSettings created in the block class
     * @return settings
     */
    public CubesSettings getSettings()
    {
        return settings;
    }
    
    /**
     * Method creates a block at a passed coordinate
     * for ID's look at https://minecraft-ids.grahamedgecombe.com/
     * 
     * @param id
     * @param coordinates 
     */
    public Block createBlock(int id)
    {
       return registeredBlocks[id];
    }
    
    public int searchDatabase(Block search)
    {
        int id = -1;
        
        for(int i = 0; i < MAX_BLOCKS; i++)
        {
            if (registeredBlocks[i] != null && registeredBlocks[i].equals(search))
            {
                id = i;
                i = MAX_BLOCKS;
            }
        }
        
        return id;
    }
    
    private Block createGrass()
    {
        if(registeredBlocks[2] == null)
        {
            registeredBlocks[2] = new Block(new BlockSkin[]{
                new BlockSkin(new BlockSkin_TextureLocation(1,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(2,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(0,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(0,0), false)
            });
            BlockManager.register(registeredBlocks[2]);
        }
        
        return registeredBlocks[2];
    }
    
    private Block createDirt()
    {
        if(registeredBlocks[3] == null)
        {
            registeredBlocks[3] = new Block(new BlockSkin(new BlockSkin_TextureLocation(2,0), false));
            BlockManager.register(registeredBlocks[3]);
        }
        
        return registeredBlocks[3];
    }
    
    private Block createStone()
    {
        if(registeredBlocks[1] == null)
        {
            registeredBlocks[1] = new Block(new BlockSkin(new BlockSkin_TextureLocation(3,0), false));
            BlockManager.register(registeredBlocks[1]);
        }
        
        return registeredBlocks[1];
    }
    
    private Block createCobblestone()
    {
        if(registeredBlocks[4] == null)
        {
            registeredBlocks[4] = new Block(new BlockSkin(new BlockSkin_TextureLocation(4,0), false));
            BlockManager.register(registeredBlocks[4]);
        }
        
        return registeredBlocks[4];
    }
    
    private Block createDiamondOre()
    {
        if(registeredBlocks[56] == null)
        {
            registeredBlocks[56] = new Block(new BlockSkin(new BlockSkin_TextureLocation(5,0), false));
            BlockManager.register(registeredBlocks[56]);
        }
        
        return registeredBlocks[56];
    }
    
    private Block createIronOre()
    {
        if(registeredBlocks[15] == null)
        {
            registeredBlocks[15] = new Block(new BlockSkin(new BlockSkin_TextureLocation(6,0), false));
            BlockManager.register(registeredBlocks[15]);
        }
        
        return registeredBlocks[15];
    }
    
    private Block createGoldOre()
    {
        if(registeredBlocks[14] == null)
        {
            registeredBlocks[14] = new Block(new BlockSkin(new BlockSkin_TextureLocation(7,0), false));
            BlockManager.register(registeredBlocks[14]);
        }
        
        return registeredBlocks[14];
    }
    
    private Block createRedstoneOre()
    {
        if(registeredBlocks[73] == null)
        {
            registeredBlocks[73] = new Block(new BlockSkin(new BlockSkin_TextureLocation(8,0), false));
            BlockManager.register(registeredBlocks[73]);
        }
        
        return registeredBlocks[73];
    }
    
    private Block createCoalOre()
    {
        if(registeredBlocks[16] == null)
        {
            registeredBlocks[16] = new Block(new BlockSkin(new BlockSkin_TextureLocation(9,0), false));
            BlockManager.register(registeredBlocks[16]);
        }
        
        return registeredBlocks[16];
    }
    
    private Block createOakWood()
    {
        if(registeredBlocks[17] == null)
        {
            registeredBlocks[17] = new Block(new BlockSkin[]{
                new BlockSkin(new BlockSkin_TextureLocation(11,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(11,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(10,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(10,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(10,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(10,0), false)
            });
            BlockManager.register(registeredBlocks[17]);
        }
        
        return registeredBlocks[17];
    }
    
    private Block createBedrock()
    {
        if(registeredBlocks[7] == null)
        {
            registeredBlocks[7] = new Block(new BlockSkin(new BlockSkin_TextureLocation(12,0), false));
            BlockManager.register(registeredBlocks[7]);
        }
        
        return registeredBlocks[7];
    }
    
    private Block createOakLeaves()
    {
        if(registeredBlocks[18] == null)
        {
            registeredBlocks[18] = new Block(new BlockSkin(new BlockSkin_TextureLocation(13,0), false));
            BlockManager.register(registeredBlocks[18]);
        }
        
        return registeredBlocks[18];
    }
    
    private Block createOakWoodPlank()
    {
        if(registeredBlocks[5] == null)
        {
            registeredBlocks[5] = new Block(new BlockSkin(new BlockSkin_TextureLocation(14,0), false));
            BlockManager.register(registeredBlocks[5]);
        }
        
        return registeredBlocks[5];
    }
    
    private Block createGlass()
    {
        if(registeredBlocks[20] == null)
        {
            registeredBlocks[20] = new Block(new BlockSkin(new BlockSkin_TextureLocation(15,0), true));
            BlockManager.register(registeredBlocks[20]);
        }
        
        return registeredBlocks[20];
    }
    
    private Block createCraftingTable()
    {
        if(registeredBlocks[58] == null)
        {
            registeredBlocks[58] = new Block(new BlockSkin[]{
                new BlockSkin(new BlockSkin_TextureLocation(2,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(14,0), false),
                new BlockSkin(new BlockSkin_TextureLocation(1,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(1,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(0,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(1,1), false)
            });
            BlockManager.register(registeredBlocks[58]);
        }
        
        return registeredBlocks[58];
    }
    
    private Block createFurnace()
    {
        if(registeredBlocks[61] == null)
        {
            registeredBlocks[61] = new Block(new BlockSkin[]{
                new BlockSkin(new BlockSkin_TextureLocation(5,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(5,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(4,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(4,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(3,1), false),
                new BlockSkin(new BlockSkin_TextureLocation(4,1), false)
            });
            BlockManager.register(registeredBlocks[61]);
        }
        
        return registeredBlocks[61];
    }
}

