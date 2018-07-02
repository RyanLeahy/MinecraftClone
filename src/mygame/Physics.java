/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author rplea
 */
public class Physics implements PhysicsCollisionListener
{
    //main class instance variables
    private BulletAppState bulletAppState; //gives access to physics libraries
    private RigidBodyControl rigidBodyControl; //gives model solidness
    private CharacterControl characterControl; //represents the player
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Node physicsNode; //used as localNode so not everything is being added to the rootNode
    
    private Vector3f playerSpawn;
   
    //Collision instance variables
    private boolean characterCollision; //this is true if the player is touching something, helps for limiting jumping
    
    private int debugTrack = 0; //used as a counter to show in debug prints that something is occurring 
    
    //private Spatial currentModel; not needed right now
    
    public Physics(Main mainClass)
    {
        myMain = mainClass;
        bulletAppState = new BulletAppState();
        physicsNode = new Node();
        myMain.getStateManager().attach(bulletAppState);
        myMain.getRootNode().attachChild(physicsNode);
        bulletAppState.getPhysicsSpace().addCollisionListener(this); //implementation is collision method at the bottom
        setupPlayer();
        
        bulletAppState.setDebugEnabled(false);
    }
    
    /**
     * Method returns the bulletAppState in the physics class
     * 
     * @return bulletAppState
     */
    public BulletAppState getBulletAppState()
    {
        return bulletAppState;
    }
    
    /**
     * Method returns the characterControl in the physics class
     * 
     * @return characterControl
     */
    public CharacterControl getCharacterControl()
    {
        return characterControl;
    }
    
    /**
     * Method returns the boolean that reports if the character is colliding with anything
     * 
     * @return characterCollision
     */
    public boolean getCharacterCollision()
    {
        return characterCollision;
    }
    
    /**
     * Method changes the boolean that reports if the character is colliding with anything
     * 
     * @param state 
     */
    public void setCharacterCollision(boolean state)
    {
        characterCollision = state;
    }
    
    /**
     * Method creates a new collisionresult object and returns it when called
     * 
     * @return CollisionResults
     */
    public CollisionResults getCollisionResults()
    {
        return new CollisionResults();
    }
    
    private void setupPlayer()
    {
        //Spatial playerSpatial = myMain.getAssetManager().loadModel("Models/Character/character.j3o");
        //CollisionShape playerShape = CollisionShapeFactory.createMeshShape(playerSpatial);
        Box box = new Box(1, 2, 1); 
        Geometry playerSpatial = new Geometry("player", box); //collision wouldn't detect just the collision mesh so I had to give it a spatial for it to detect
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(.6f, 1.5f, 1); //creates the invisible field, 2 wide, 2 tall, 1 means standing up
        characterControl = new CharacterControl(playerShape, .05f);
        
        characterControl.setJumpSpeed(20);
        characterControl.setFallSpeed(30);
        characterControl.setGravity(new Vector3f(0,-30f,0));
        characterControl.setSpatial(playerSpatial);
        playerSpatial.addControl(characterControl);

        //this helps makes collision events more efficient by only calculating the ones that involve the player
        playerSpatial.getControl(CharacterControl.class).setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01); //assign it to collision group 1
        playerSpatial.getControl(CharacterControl.class).addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02); //report event when it collides with group 2, group 2 will be everything that isnt the player
        
        bulletAppState.getPhysicsSpace().add(characterControl); //add it to the physics listener
        //myMain.getRootNode().attachChild(playerSpatial); //this line will render the spatial which isn't necessary right now
    }
    
    //called frequently, performs game state updates
    public void simpleUpdate(float tpf) 
    {
        
    }
    
    //method responds to a collision event and performs a specified action
    @Override
    public void collision(PhysicsCollisionEvent event)
    {
        //System.out.println(event.getNodeA() + " " + event.getNodeB() + " " + debugTrack++);
        
        //This chunk of code checks if the character is touching something to be used to prevent infinite jumping
        if (event.getNodeA().getName().equals("player"))
        {
            characterCollision = true;
        }
        else if (event.getNodeB().getName().equals("player"))
        {
            characterCollision = true;
        }
  }
}
