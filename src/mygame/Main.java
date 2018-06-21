package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.app.state.AppState; //for interface for the add and remove helper methods

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication
{
    private static Main myApp;
    
    public static void main(String[] args)
    {
        myApp = new Main();
        
        myApp.start();
    }

    @Override
    public void simpleInitApp()
    {
        Physics gamePhysics = new Physics(myApp);
        MinecraftCamera gameCam = new MinecraftCamera(myApp, flyCam);
        Spatial grassBlock = assetManager.loadModel("Models/GrassBlock/GrassBlock.j3o");
        rootNode.attachChild(grassBlock);
        rootNode.addLight(new MinecraftLight().getWorldLight());
    }
    
    /**
     * adds a node to rootNode 
     * @param childNode
     */
    public void addNode(Node childNode)
    {
        rootNode.attachChild(childNode);
    }

    /**
     * removes a node from rootNode
     * @param childNode 
     */
    public void removeNode(Node childNode)
    {
        rootNode.detachChild(childNode);
    }
    
    /**
     * adds a state to stateManager
     * @param child 
     */
    public void addState(AppState child)
    {
        stateManager.attach(child);
    }
    
    /**
     * removes a state from stateManager
     * @param child 
     */
    public void removeState(AppState child)
    {
        stateManager.detach(child);
    }
    
    
    @Override
    public void simpleUpdate(float tpf)
    {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        //TODO: add render code
    }
}
