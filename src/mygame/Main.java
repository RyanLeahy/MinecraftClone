package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.system.AppSettings;

/**
 * The Main class initializes and offers methods to help other classes use and add to the game
 * None of the work is done in this class all of it is passed off to other appropriate classes
 * 
 * @author Ryan Leahy
 */
public class Main extends SimpleApplication implements ActionListener, Application //for keyMapping to work
{
    private static Main myApp;
    private Physics gamePhysics;
    private MinecraftCamera gameCam;
    private KeyMapping gameKeyMap;
    private MinecraftSky gameSky;
    private GUI gameGui;
    private BlockDatabase gameDatabase;
    private WorldGenerator gameWorldGen;
    private Application application;
    
    public static void main(String[] args)
    {
        myApp = new Main();
        myApp.start();
    }

    @Override
    public void simpleInitApp()
    {
        gamePhysics = new Physics(myApp);
        gameKeyMap = new KeyMapping(myApp);
        gameCam = new MinecraftCamera(myApp, cam);
        gameSky = new MinecraftSky(myApp);
        gameGui = new GUI(myApp);
        gameDatabase = new BlockDatabase(myApp);
        gameWorldGen = new WorldGenerator(myApp, gameDatabase);
        
        rootNode.addLight(new MinecraftLight().getWorldLight());
    }
    
    /**
     * Method returns the AppSettings of the game
     * 
     * @return settings 
     */
    public AppSettings getSettings()
    {
        return settings;
    }
    
    //passes instance of minecraftCamClass MAY IMPLEMENT DIFFERENTLY LATER
    public MinecraftCamera getMinecraftCam()
    {
        return gameCam;
    }
    
    /**
     * Method returns the physics engine of the game
     * 
     * @return gamePhysics
     */
    public Physics getGamePhysics()
    {
        return gamePhysics;
    }
    
    /**
     * Method returns the world generator of the game
     * 
     * @return gameWorldGen 
     */
    public WorldGenerator getWorldGenerator()
    {
        return gameWorldGen;
    }
    
    public KeyMapping getKeyMapping()
    {
        return gameKeyMap;
    }
    
    public Application getApplication()
    {
        return application;
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        //gameKeyMap.simpleUpdate(tpf); nothing currently in simpleUpdate so unnecessary
        gameCam.simpleUpdate(tpf);
        //gamePhysics.simpleUpdate(tpf); nothing currently in simpleUpdate so unnecessary
        gameWorldGen.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
        gameKeyMap.onAction(name, isPressed, tpf); //pass off the action to the key mapping class
    }
}
    
