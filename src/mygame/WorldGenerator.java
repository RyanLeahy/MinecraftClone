/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author Ryan Leahy
 */
public class WorldGenerator implements BlockChunkListener
{
    private Main myMain;
    private BlockDatabase myDatabase;
    private BlockTerrainControl world;
    private Node terrainNode;
    private int currentID;
    
    public WorldGenerator(Main mainClass, BlockDatabase databaseClass)
    {
        myMain = mainClass;
        myDatabase = databaseClass;
        terrainNode = new Node();
        initiateWorld();
    }
    
    //Will write basic implementation for the moment but later this will become more advanced and will handle procedural generation
    private void initiateWorld()
    {
        //this line creates a single chunk
        world = new BlockTerrainControl(myDatabase.getSettings(), new Vector3Int(4, 1, 4));
        
        //basic world for testing purposes
        world.setBlockArea(new Vector3Int(0, 60, 0), new Vector3Int(64, 1, 64), myDatabase.createBlock(2)); //grass
        world.setBlockArea(new Vector3Int(0, 50, 0), new Vector3Int(64, 10, 64), myDatabase.createBlock(3)); //dirt
        world.setBlockArea(new Vector3Int(0, 1, 0), new Vector3Int(64, 50, 64), myDatabase.createBlock(1)); //stone
        world.setBlockArea(new Vector3Int(0, 0, 0), new Vector3Int(64, 1, 64), myDatabase.createBlock(7)); //bedrock
        generateTree(new Vector3Int(8, 61, 8), world);
        generateTree(new Vector3Int(16, 61, 16), world);
        
        
        for(int i = 0; i < 65; i++)
            if(myDatabase.createBlock(i) != null)
                world.setBlock(new Vector3Int(i, 61, 32), myDatabase.createBlock(i));
        
        
        terrainNode.addControl(world);
        terrainNode.addControl(new RigidBodyControl(0));
        myMain.getGamePhysics().getBulletAppState().getPhysicsSpace().addAll(terrainNode);
        myMain.getRootNode().attachChild(terrainNode);
        world.addChunkListener(this);
    }
    
    //pass int to where to generate the base of the tree
    private void generateTree(Vector3Int coordinates, BlockTerrainControl currentChunk)
    {
        int x, y, z;
        x = coordinates.getX();
        y = coordinates.getY();
        z = coordinates.getZ();
        
        currentChunk.setBlockArea(coordinates, new Vector3Int(1, 6, 1), myDatabase.createBlock(17));
        currentChunk.setBlockArea(new Vector3Int(x - 2, y + 3, z + 1), new Vector3Int(5, 2, 2), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x - 2, y + 3, z - 2), new Vector3Int(5, 2, 2), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x + 1, y + 3, z), new Vector3Int(2, 2, 1), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x - 2, y + 3, z), new Vector3Int(2, 2, 1), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x - 1, y + 5, z), new Vector3Int(1, 2, 1), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x + 1, y + 5, z), new Vector3Int(1, 2, 1), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x, y + 5, z - 1), new Vector3Int(1, 2, 1), myDatabase.createBlock(18));
        currentChunk.setBlockArea(new Vector3Int(x, y + 5, z + 1), new Vector3Int(1, 2, 1), myDatabase.createBlock(18));
    }
    
    //this will be used to render and derender as someone moves through chunks
    private void expandWorld()
    {
        
    }
    
    /**
     * Method handles returning the node which all the blocks are stored
     * 
     * @return terrainNode
     */
    public Node getWorldNode()
    {
        return terrainNode;
    }
    
    /**
     * Method removes a block at a given coordinate
     * 
     * @param collisionResults
     */
    public void removeBlock(CollisionResults collisionResults)
    {
        Ray ray = new Ray(myMain.getMinecraftCam().getCam().getLocation(), myMain.getMinecraftCam().getCam().getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            Vector3f tempTranslation = collisionResults.getClosestCollision().getContactPoint();
            Vector3Int collisionPoint = new Vector3Int((int)tempTranslation.getX(), (int)tempTranslation.getY(), (int)tempTranslation.getZ());
            world.removeBlock(collisionPoint);
        }
    }
    
    public void addBlock(CollisionResults collisionResults)
    {
        Ray ray = new Ray(myMain.getMinecraftCam().getCam().getLocation(), myMain.getMinecraftCam().getCam().getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            Vector3f tempTranslation = collisionResults.getClosestCollision().getContactPoint();
            Vector3Int collisionPoint = new Vector3Int((int)tempTranslation.getX(), (int)tempTranslation.getY(), (int)tempTranslation.getZ());
            if (currentID != -1) //if the search didn't fail use that block
                world.setBlock(collisionPoint, myDatabase.createBlock(currentID));
            else //otherwise use stone
                world.setBlock(collisionPoint, myDatabase.createBlock(1)); 
        }
    }
    
    /**
     * Method finds the id of the block and returns it 
     * 
     * @param collisionResults
     */
    public void selectBlock(CollisionResults collisionResults)
    {
        Ray ray = new Ray(myMain.getMinecraftCam().getCam().getLocation(), myMain.getMinecraftCam().getCam().getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            Vector3f tempTranslation = collisionResults.getClosestCollision().getContactPoint();
            Vector3Int collisionPoint = new Vector3Int((int)tempTranslation.getX(), (int)tempTranslation.getY(), (int)tempTranslation.getZ());
            currentID = myDatabase.searchDatabase(world.getBlock(collisionPoint));
        }
    }
    
    /**
     * Method adds a block at a given coordinate
     * 
     * @param coordinates
     * @param id
     */
    public void addBlock(Vector3Int coordinates, int id)
    {
        world.setBlock(coordinates, myDatabase.createBlock(id));
    }
    
    @Override
    public void onSpatialUpdated(BlockChunkControl blockChunk){
        Geometry optimizedGeometry = blockChunk.getOptimizedGeometry_Opaque();
        RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
        if(rigidBodyControl == null){
            rigidBodyControl = new RigidBodyControl(0);
            optimizedGeometry.addControl(rigidBodyControl);
            myMain.getGamePhysics().getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
        }
        rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
    }
}
