/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author rplea
 */
public class Physics implements PhysicsCollisionListener
{
    private BulletAppState bulletAppState; //gives access to physics libraries
    private RigidBodyControl rigidBodyControl; //gives model solidness
    private CharacterControl characterControl; //represents the player
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean characterCollision;
    private boolean[] keyPress;
    private enum Keys {LEFT, RIGHT, UP, DOWN, CROUCH};
    private int debugTrack = 0;
    
    public Physics(Main mainClass)
    {
        myMain = mainClass;
        bulletAppState = new BulletAppState();
        keyPress = new boolean[Keys.values().length]; //creates a boolean array the same size as the amount of enumerations
        myMain.getStateManager().attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this); //wrote implementation of interface below
        setupPlayer();
        bulletAppState.setDebugEnabled(true);
    }
    
    /**
     * Method takes in a spatial and adds all the necessary things to make it so you can't just walk through it
     * @param Spatial model
     * @param float gravityValue 
     */
    public void addCollision(Spatial model, float gravityValue)
    {
        CollisionShape modelShape = CollisionShapeFactory.createMeshShape(model); //creates hitbox
        rigidBodyControl = new RigidBodyControl(modelShape, gravityValue); //gives hitbox and amount of weight to this
        model.addControl(rigidBodyControl); //add the hitbox and weight to the model
        rigidBodyControl.setGravity(new Vector3f(0,-30f,0));
        bulletAppState.getPhysicsSpace().add(model); //add it to the physics listener
    }
    
    private void setupPlayer()
    {
        Box box = new Box(2, 2, 2); 
        Geometry playerSpatial = new Geometry("player", box); //collision wouldn't detect just the collision mesh so I had to give it a spatial for it to detect
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(2f, 6f, 1); //creates the invisible field, 2 wide, 6 tall, 1 means standing up
        characterControl = new CharacterControl(playerShape, .05f);
        characterControl.setJumpSpeed(20);
        characterControl.setFallSpeed(30);
        characterControl.setGravity(new Vector3f(0,-30f,0));
        characterControl.setSpatial(playerSpatial);
        setPlayerSpawn(0, 10, 0);
        bulletAppState.getPhysicsSpace().add(characterControl); //add it to the physics listener
    }
    
    public void setPlayerSpawn(float x, float y, float z)
    {
        characterControl.setPhysicsLocation(new Vector3f(x,y,z));
    }
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
        if (name.equals("Left")) {
        if (isPressed) { keyPress[Keys.LEFT.ordinal()] = true; } else { keyPress[Keys.LEFT.ordinal()] = false; }
      } else if (name.equals("Right")) {
        if (isPressed) { keyPress[Keys.RIGHT.ordinal()] = true; } else { keyPress[Keys.RIGHT.ordinal()] = false; }
      } else if (name.equals("Up")) {
        if (isPressed) { keyPress[Keys.UP.ordinal()] = true; } else { keyPress[Keys.UP.ordinal()] = false; }
      } else if (name.equals("Down")) {
        if (isPressed) { keyPress[Keys.DOWN.ordinal()] = true; } else { keyPress[Keys.DOWN.ordinal()] = false; }
      } else if (name.equals("Crouch")) {
        if (isPressed) { keyPress[Keys.CROUCH.ordinal()] = true; } else { keyPress[Keys.CROUCH.ordinal()] = false; }      
      } else if (name.equals("Jump")) {
        //Some methods used for setting gravity related variables were deprecated in
        //the 3.2 version of the engine. Choose the method that matches your version
        //of the engine.
        // < jME3.2
        //if (isPressed) { player.jump();}

        // >= jME3.2
        if (characterCollision && isPressed) {characterControl.jump(new Vector3f(0, 20f, 0));} else { characterCollision = false; } //if key is pressed and the character is touching an object
      }
    }
    
    public void simpleUpdate(float tpf) {
        if (keyPress[Keys.CROUCH.ordinal()])
        {
            camDir.set(myMain.getMinecraftCam().getCam().getDirection()).multLocal(0.15f);
            camLeft.set(myMain.getMinecraftCam().getCam().getLeft()).multLocal(0.1f);
        }
        else
        {
            camDir.set(myMain.getMinecraftCam().getCam().getDirection()).multLocal(0.6f);
            camLeft.set(myMain.getMinecraftCam().getCam().getLeft()).multLocal(0.4f);
        }
        walkDirection.set(0, 0, 0);
        if (keyPress[Keys.LEFT.ordinal()]) {
            walkDirection.addLocal(camLeft);
        }
        if (keyPress[Keys.RIGHT.ordinal()]) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (keyPress[Keys.UP.ordinal()]) {
            walkDirection.addLocal(camDir);
        }
        if (keyPress[Keys.DOWN.ordinal()]) {
            walkDirection.addLocal(camDir.negate());
        }
        characterControl.setWalkDirection(walkDirection);
        myMain.getMinecraftCam().getCam().setLocation(characterControl.getPhysicsLocation());
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event)
    {
        System.out.println(event.getNodeA() + " " + event.getNodeB() + " " + debugTrack++);
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
