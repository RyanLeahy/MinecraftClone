/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.cubes.Vector3Int;
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
import com.jme3.math.Ray;
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
    
    //walking instance variables
    private Vector3f walkDirection = new Vector3f(); //three vectors are used for walking
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    private Vector3f playerSpawn;
   
    //Collision instance variables
    private boolean characterCollision; //this is true if the player is touching something, helps for limiting jumping
    private boolean fallCollision; 
    
    //key handling instance variables
    private boolean[] keyPress; //boolean array to store if keys are pressed or not
    private enum Keys {LEFT, RIGHT, UP, DOWN, CROUCH, BREAK, PLACE}; //enumeration for indexing the array
    
    private int debugTrack = 0; //used as a counter to show in debug prints that something is occurring 
    
    //private Spatial currentModel; not needed right now
    
    //block modification instance variables
    private Ray ray;
    private CollisionResults collisionResults;
    private Vector3Int collisionPoint;
    
    public Physics(Main mainClass)
    {
        myMain = mainClass;
        bulletAppState = new BulletAppState();
        physicsNode = new Node();
        keyPress = new boolean[Keys.values().length]; //creates a boolean array the same size as the amount of enumerations
        myMain.getStateManager().attach(bulletAppState);
        myMain.getRootNode().attachChild(physicsNode);
        bulletAppState.getPhysicsSpace().addCollisionListener(this); //implementation is collision method at the bottom
        setupPlayer();
        
        bulletAppState.setDebugEnabled(false);
    }
    
    /* DEPRECATED FOR NOW
     * Method takes in a spatial and adds all the necessary things to make it so you can't just walk through it
     * @param model
     * @param gravityValue
     *
    public void addCollision(Spatial model, float gravityValue)
    {
        CollisionShape modelShape;
        currentModel = model;
        
        //if the item is not suppose to move
        if (gravityValue == 0)
            modelShape = CollisionShapeFactory.createMeshShape(model); //creates mesh shape, better suited for still objects
        else
            modelShape = CollisionShapeFactory.createDynamicMeshShape(model); //otherwise create a dynamic mesh shape, better suited for movable objects
        
        rigidBodyControl = new RigidBodyControl(modelShape, gravityValue); //gives hitbox and amount of weight to this
        currentModel.addControl(rigidBodyControl); //add the hitbox and weight to the model
        
        //if the item is not suppose to move, have to check it again because the above if statement can't hold these in it because it relies on the subsequent lines, thats why were doing this agains
        if (gravityValue == 0)
            currentModel.getControl(RigidBodyControl.class).setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02); //sets group to collision group 2, the group who ignores collisions
        else
        {
            currentModel.getControl(RigidBodyControl.class).setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01); //sets group to collision group 1, the group who checks collision
            currentModel.getControl(RigidBodyControl.class).setKinematic(true);
        }
        
        rigidBodyControl.setGravity(new Vector3f(0,-30f,0));
        bulletAppState.getPhysicsSpace().add(model); //add it to the physics listener
    }*/
    
    public BulletAppState getBulletAppState()
    {
        return bulletAppState;
    }
    
    //method handles what happens when you fall below a certain threshold on the y axis
    private void below0()
    {
        setPlayerSpawn(playerSpawn);
    }
    
    /**
     * Sets the coordinate location of the object passed to the addCollision method
     * @param model
     * @param coordinates 
     */
    public void setLocation(Spatial model, Vector3f coordinates)
    {
        model.getControl(RigidBodyControl.class).setPhysicsLocation(coordinates);
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
        
        
        setPlayerSpawn(new Vector3f(0, 70, 0));

        //this helps makes collision events more efficient by only calculating the ones that involve the player
        playerSpatial.getControl(CharacterControl.class).setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01); //assign it to collision group 1
        playerSpatial.getControl(CharacterControl.class).addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02); //report event when it collides with group 2, group 2 will be everything that isnt the player
        
        bulletAppState.getPhysicsSpace().add(characterControl); //add it to the physics listener
        //myMain.getRootNode().attachChild(playerSpatial); //this line will render the spatial which isn't necessary right now
    }
    
    /**
     * method sets the location of where the player spawns in game
     * @param coordinates 
     */
    public void setPlayerSpawn(Vector3f coordinates)
    {
        characterControl.setPhysicsLocation(coordinates);
        playerSpawn = coordinates;
    }
    
    //This method handles what happens when a button is clicked, usually setting a boolean to true indicating a key was pressed
    public void onAction(String name, boolean isPressed, float tpf)
    {
        switch (name)
        {
            case "Left":
                if (isPressed) { keyPress[Keys.LEFT.ordinal()] = true; } else { keyPress[Keys.LEFT.ordinal()] = false; }
                break;
            case "Right":
                if (isPressed) { keyPress[Keys.RIGHT.ordinal()] = true; } else { keyPress[Keys.RIGHT.ordinal()] = false; }
                break;
            case "Up":
                if (isPressed) { keyPress[Keys.UP.ordinal()] = true; } else { keyPress[Keys.UP.ordinal()] = false; }
                break;
            case "Down":
                if (isPressed) { keyPress[Keys.DOWN.ordinal()] = true; } else { keyPress[Keys.DOWN.ordinal()] = false; }
                break;
            case "Crouch":
                if (isPressed) { keyPress[Keys.CROUCH.ordinal()] = true; } else { keyPress[Keys.CROUCH.ordinal()] = false; }
                break;
            case "Jump":
                if (characterCollision && isPressed) {characterControl.jump(new Vector3f(0, 10f, 0));} else { characterCollision = false; } //if key is pressed and the character is touching an object
                break;
            case "Break":
                if (isPressed) { keyPress[Keys.BREAK.ordinal()] = true; breakBlock(); } else { keyPress[Keys.BREAK.ordinal()] = false; }
                break;
            case "Place":
                if (isPressed) { keyPress[Keys.PLACE.ordinal()] = true; } else { keyPress[Keys.PLACE.ordinal()] = false; }
                break;
            default:
                break;
        }
    }
    
    //called frequently, performs game state updates
    public void simpleUpdate(float tpf) {
        if (keyPress[Keys.CROUCH.ordinal()]) //if the shift key is pressed
        {
            camDir.set(myMain.getMinecraftCam().getCam().getDirection()).multLocal(0.15f); //slow down movement
            camLeft.set(myMain.getMinecraftCam().getCam().getLeft()).multLocal(0.1f);
        }
        else //if its not pressed
        {
            camDir.set(myMain.getMinecraftCam().getCam().getDirection()).multLocal(0.6f); //keep movement the same
            camLeft.set(myMain.getMinecraftCam().getCam().getLeft()).multLocal(0.4f);
        }
        
        //this handles player movement
        walkDirection.set(0, 0, 0);
        if (keyPress[Keys.LEFT.ordinal()]) 
        {
            walkDirection.addLocal(camLeft);
        }
        if (keyPress[Keys.RIGHT.ordinal()]) 
        {
            walkDirection.addLocal(camLeft.negate());
        }
        if (keyPress[Keys.UP.ordinal()]) 
        {
            walkDirection.addLocal(camDir);
        }
        if (keyPress[Keys.DOWN.ordinal()]) 
        {
            walkDirection.addLocal(camDir.negate());
        }
        walkDirection.setY(0); //this makes it so pointing at the sky doesn't actually move the character into the sky, the only way that the player should raise in Y is by jumping
        characterControl.setWalkDirection(walkDirection);
        myMain.getMinecraftCam().getCam().setLocation(characterControl.getPhysicsLocation());
        
        //this handles what happens when the player falls out of the world and at what y value it occurs
        if(characterControl.getPhysicsLocation().getY() < -50)
            below0();
    }
    
    private void breakBlock()
    {
        collisionResults = new CollisionResults();
        ray = new Ray(myMain.getMinecraftCam().getCam().getLocation(), myMain.getMinecraftCam().getCam().getDirection());
        myMain.getWorldGenerator().getWorldNode().collideWith(ray, collisionResults);
        if(collisionResults.getClosestCollision() != null && collisionResults.getClosestCollision().getDistance() < 5) //add conditioning, first makes sure there is something to break, second makes sure its not too far
        {
            Vector3f tempTranslation = collisionResults.getClosestCollision().getContactPoint();
            collisionPoint = new Vector3Int((int)tempTranslation.getX(), (int)tempTranslation.getY(), (int)tempTranslation.getZ());
            myMain.getWorldGenerator().removeBlock(collisionPoint);
        }
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
