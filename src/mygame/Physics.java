/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author rplea
 */
public class Physics
{
    private BulletAppState bulletAppState; //gives access to physics libraries
    private RigidBodyControl rigidBodyControl; //gives model solidness
    private CharacterControl characterControl; //represents the player
    private Main myMain; //holds on to the main object so that it can call its methods if necessary
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    
    public Physics(Main mainClass)
    {
        myMain = mainClass;
        bulletAppState = new BulletAppState();
        myMain.getStateManager().attach(bulletAppState);
        setupPlayer();
    }
    
    /**
     * Method takes in a spatial and adds all the necessary things to make it so you can't just walk through it
     * @param Spatial model
     * @param float gravityValue 
     */
    public void addCollision(Spatial model, float gravityValue)
    {
        CollisionShape modelShape = CollisionShapeFactory.createMeshShape(model);
        rigidBodyControl = new RigidBodyControl(modelShape, gravityValue);
        model.addControl(rigidBodyControl);
        bulletAppState.getPhysicsSpace().add(model); //add it to the physics listener
    }
    
    private void setupPlayer()
    {
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(2f, 6f, 1); //creates the invisible field, 2 wide, 6 tall, 1 means standing up
        characterControl = new CharacterControl(playerShape, .05f);
        characterControl.setJumpSpeed(20);
        characterControl.setFallSpeed(30);
        characterControl.setGravity(new Vector3f(0,-30f,0));
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
        if (isPressed) { left = true; } else { left = false; }
      } else if (name.equals("Right")) {
        if (isPressed) { right = true; } else { right = false; }
      } else if (name.equals("Up")) {
        if (isPressed) { up = true; } else { up = false; }
      } else if (name.equals("Down")) {
        if (isPressed) { down = true; } else { down = false; }
      } else if (name.equals("Jump")) {
        //Some methods used for setting gravity related variables were deprecated in
        //the 3.2 version of the engine. Choose the method that matches your version
        //of the engine.
        // < jME3.2
        //if (isPressed) { player.jump();}

        // >= jME3.2
        if (isPressed) { characterControl.jump(new Vector3f(0,20f,0));}
      }
    }
    
    public void simpleUpdate(float tpf) {
        camDir.set(myMain.getMinecraftCam().getCam().getDirection()).multLocal(0.6f);
        camLeft.set(myMain.getMinecraftCam().getCam().getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        characterControl.setWalkDirection(walkDirection);
        myMain.getMinecraftCam().getCam().setLocation(characterControl.getPhysicsLocation());
    }
  }
}
