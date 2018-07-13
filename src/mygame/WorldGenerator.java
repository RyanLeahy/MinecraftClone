/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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
    private Physics gamePhysics;
    private Camera gameCam;
    private int currentID;
    private Vector3f playerSpawn;
    private boolean[] keyPress;
    private float blockDelay = 0f;
    
    
    public WorldGenerator(Main mainClass, BlockDatabase databaseClass)
    {
        myMain = mainClass;
        myDatabase = databaseClass;
        terrainNode = new Node();
        gamePhysics = myMain.getGamePhysics();
        gameCam = myMain.getMinecraftCam().getCam();
        currentID = -1;
        keyPress = myMain.getKeyMapping().getKeyPress();
        setPlayerSpawn(new Vector3f(0, 70, 0));
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
        
        int x = 0;
        for(int i = 0; i < 453; i++)
            if(myDatabase.createBlock(i) != null)
            {
                world.setBlock(new Vector3Int(x, 61, 32), myDatabase.createBlock(i));
                x++;
            }
        
        
        terrainNode.addControl(world);
        terrainNode.addControl(new RigidBodyControl(0));
        gamePhysics.getBulletAppState().getPhysicsSpace().addAll(terrainNode);
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
    
    private void generateChunk(Vector3Int start, Vector3Int stop)
    {
        
    }
    
    //this will be used to render and derender as someone moves through chunks
    private void expandWorld()
    {
        
    }
    
    /**
     * method sets the location of where the player spawns in game
     * @param coordinates 
     */
    public void setPlayerSpawn(Vector3f coordinates)
    {
        gamePhysics.getCharacterControl().setPhysicsLocation(coordinates);
        playerSpawn = coordinates;
    }
    
    /**
     * Method handles what happens when you fall below a certain threshold
     */
    public void below0()
    {
        setPlayerSpawn(playerSpawn);
    }
    
    /**
     * Method returns the current location of the player
     * 
     * @return characterLocation
     */
    public Vector3f getLocation()
    {
        return gamePhysics.getCharacterControl().getPhysicsLocation();
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
        Ray ray = new Ray(gameCam.getLocation(), gameCam.getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
            world.removeBlock(BlockNavigator.getPointedBlockLocation(world, collisionResults.getClosestCollision().getContactPoint(), false)); //false value returns the coordinates of the block, true returns the coordinates of where the neighbor would be
    }
    
    public void addBlock(CollisionResults collisionResults)
    {
        Ray ray = new Ray(gameCam.getLocation(), gameCam.getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            if (currentID != -1) //if the search didn't fail use that block
                world.setBlock(BlockNavigator.getPointedBlockLocation(world, collisionResults.getClosestCollision().getContactPoint(), true), myDatabase.createBlock(currentID));
            else //otherwise use stone
                world.setBlock(BlockNavigator.getPointedBlockLocation(world, collisionResults.getClosestCollision().getContactPoint(), true), myDatabase.createBlock(1)); 
        }
    }
    
    /**
     * Method finds the id of the block and returns it 
     * 
     * @param collisionResults
     */
    public void selectBlock(CollisionResults collisionResults)
    {
        Ray ray = new Ray(gameCam.getLocation(), gameCam.getDirection());
        terrainNode.collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null/* && collisionResults.getClosestCollision().getDistance() < 5*/) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            currentID = myDatabase.searchDatabase(world.getBlock(BlockNavigator.getPointedBlockLocation(world, collisionResults.getClosestCollision().getContactPoint(), false)));
            world.removeChunk(new Vector3Int(collisionResults.getClosestCollision().getContactPoint()), world);
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
    
    /*private boolean delay(float tpf) //deprecated for now
    {
        boolean back;
        if(blockDelay >= tpf * 5 || blockDelay == 0)
        {
            blockDelay = 0;
            back = true;
        }
        else
            back = false;
        
        blockDelay += tpf;
        
        return back;
    }*/
    
    @Override
    public void onSpatialUpdated(BlockChunkControl blockChunk){
        Geometry optimizedGeometry;
        MeshCollisionShape optimizedShape;
        
        Object[] results;
        onSpatialUpdate optimized = new onSpatialUpdate();
        Thread t = new Thread(optimized);
        t.start();
        results = optimized.getOptimized(blockChunk);
        
        optimizedGeometry = (Geometry)results[0];
        optimizedShape = (MeshCollisionShape)results[1];
        
        RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
        if(rigidBodyControl == null){
            rigidBodyControl = new RigidBodyControl(0);
            optimizedGeometry.addControl(rigidBodyControl);
            gamePhysics.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
        }
        rigidBodyControl.setCollisionShape(optimizedShape);
    }
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
        switch (name)
        {
            case "Break":
                if (isPressed) { removeBlock(gamePhysics.getCollisionResults()); }
                break;
            case "Place":
                if (isPressed) { addBlock(gamePhysics.getCollisionResults()); }
                break;
            case "SelectBlock":    
                if (isPressed) { selectBlock(gamePhysics.getCollisionResults()); }
                break;
            default:
                break;
        }
    }
    
    public void simpleUpdate(float tpf)
    {
        //this handles what happens when the player falls out of the world and at what y value it occurs
        if(gamePhysics.getCharacterControl().getPhysicsLocation().getY() < -50)
            below0();
        
    }
    
    private static class onSpatialUpdate implements Runnable
    {
        private Object[] optimized;
        
        @Override
        public void run()
        {
            
        }
        
        public Object[] getOptimized(BlockChunkControl blockChunk)
        {
            optimized = new Object[2];
            optimized[0] = blockChunk.getOptimizedGeometry();
            optimized[1] = new MeshCollisionShape(((Geometry)optimized[0]).getMesh());
            
            return optimized;
        }
    }
}
