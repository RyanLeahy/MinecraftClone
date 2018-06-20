package mygame;

import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA; //allows to set color of light

public class MinecraftLight
{
    private AmbientLight worldLight;

    public MinecraftLight()
    {
        worldLight = new AmbientLight(); //ambient light source on the world
        worldLight.setColor(ColorRGBA.White.mult(1.3f));
    }

    public AmbientLight getWorldLight()
    {
        return worldLight;
    }
}
