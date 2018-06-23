package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * The Main class initializes and offers methods to help other classes use and add to the game
 * None of the work is done in this class all of it is passed off to other appropriate classes
 * 
 * @author Ryan Leahy
 */
public class Main extends SimpleApplication implements ActionListener //for keyMapping to work
{
    private static Main myApp;
    private Physics gamePhysics;
    private MinecraftCamera gameCam;
    private KeyMapping gameKeyMap;
    private MinecraftSky gameSky;
    
    public static void main(String[] args)
    {
        myApp = new Main();
        myApp.start();
    }

    @Override
    public void simpleInitApp()
    {
        gameCam = new MinecraftCamera(myApp, cam);
        gameKeyMap = new KeyMapping(myApp);
        gameSky = new MinecraftSky(myApp);
        gamePhysics = new Physics(myApp);
        Spatial grassBlock = assetManager.loadModel("Models/GrassBlock/GrassBlock.j3o");
        gamePhysics.addCollision(grassBlock, 0);
        grassBlock.center();
        rootNode.attachChild(grassBlock);
        rootNode.attachChild(makeFloor());
        rootNode.addLight(new MinecraftLight().getWorldLight());
    }
    
    //passes instance of minecraftCamClass MAY IMPLEMENT DIFFERENTLY LATER
    public MinecraftCamera getMinecraftCam()
    {
        return gameCam;
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        gamePhysics.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
        gamePhysics.onAction(name, isPressed, tpf); //pass off the action to the physics class
    }
    protected Geometry makeFloor() {
    Box box = new Box(15, .2f, 15);
    Geometry floor = new Geometry("the Floor", box);
    floor.setLocalTranslation(0, -4, -5);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Gray);
    floor.setMaterial(mat1);
    return floor;
}
