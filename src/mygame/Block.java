package mygame;

import com.jme3.scene.Spatial;

/**
 * Class holds identifying information for blocks that will be placed in the game, an id, a name, and the model
 */
public class Block
{
    private int blockId;
    private String blockName;
    private Spatial blockModel;

    /**
     *
     * @parameter int id, string name, Spatial model
     * make sure to use asset manager to pass the model
     */
    public Block (int id, String name, Spatial model)
    {
        blockId = id;
        blockName = name;
        blockModel = model;
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
     * Sets the blockId contained in the object
     * @param int
     */
    public void setBlockId(int blockId)
    {
        this.blockId = blockId;
    }

    /**
     * sets the blockName contained in the object
     * @param String
     */
    public void setBlockName(String blockName)
    {
        this.blockName = blockName;
    }

    /**
     * sets the blockModel contained in the object
     * @param Spatial
     */
    public void setBlockModel(Spatial blockModel)
    {
        this.blockModel = blockModel;
    }
}
