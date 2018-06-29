package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.cubes.*;

/**
 * Class holds identifying information for blocks that will be placed in the game, an id, a name, and the model
 */
public class BlockDEPRECATED
{
    private int blockId;
    private String blockName;
    private Spatial blockModel;
    private Physics gamePhysics;
    private Main myMain;
    private float blockGravity;
    private Vector3f blockCoordinate;

    /**
     * Block constructor handles creating the blocks in the game
     * 
     * @param id
     * @param name 
     * @param model
     * @param physicsClass
     * @param mainClass
     * @param gravityValue
     * @param coordinates
     * make sure to use asset manager to pass the model
     */
    public BlockDEPRECATED (int id, String name, Spatial model, Physics physicsClass, Main mainClass, float gravityValue, Vector3f coordinates)
    {
        blockId = id;
        blockName = name;
        blockModel = model;
        gamePhysics = physicsClass;
        myMain = mainClass;
        blockGravity = gravityValue;
        blockCoordinate = coordinates;
        registerBlock();
    }

    /**
     * passes back blockID
     * @return int
     */
    public int getBlockId()
    {
        return blockId;
    }

    /**
     * passes back blockName
     * @return string
     */
    public String getBlockName()
    {
        return blockName;
    }

    /**
     * passes back blockModel
     * @return Spatial
     */
    public Spatial getBlockModel()
    {
        return blockModel;
    }
    
    /**
     * passes back blockCoordinate
     * @return Vector3f
     */
    public Vector3f getBlockCoordinate()
    {
        return blockCoordinate;
    }
    
    private void registerBlock()
    {
        gamePhysics.addCollision(blockModel, blockGravity);
        gamePhysics.setLocation(blockModel, blockCoordinate);
        myMain.getRootNode().attachChild(blockModel);
    }
}
