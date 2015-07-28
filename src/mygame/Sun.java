/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import jme3utilities.Misc;
import jme3utilities.TimeOfDay;
import jme3utilities.sky.SkyControl;

/**
 *
 * @author christian
 */
public class Sun {
    
    private Vector3f sunDirection;
    private SkyControl sc;
    public TimeOfDay timeOfDay;
    private FogFilter fogFilter;
    private float fogDensity = 0f;
    public void initSun(Node rootNode, ViewPort viewPort, Camera cam)
    {
        sunDirection = new Vector3f(-0.5f, -0.5f, -0.5f);
        DirectionalLight sun = new DirectionalLight();
        //sun.setDirection(sunDirection);
        //sun.setColor(ColorRGBA.White);
        sc = new SkyControl(Main.s_AssetManager, cam, 0.9f, true, true);
        rootNode.addControl(sc);
        sc.getUpdater().setMainLight(sun);
        sc.getSunAndStars().setObserverLatitude(0f);
        
        sc.getSunAndStars().setHour(6);
        sc.setEnabled(true);
        
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBlurScale(2.5f);
        bloom.setExposurePower(1);
        Misc.getFpp(viewPort, Main.s_AssetManager).addFilter(bloom);
        sc.getUpdater().addBloomFilter(bloom);
        
        rootNode.addLight(sun);
        
        timeOfDay = new TimeOfDay(sc.getSunAndStars().getHour());
        Main.s_StateManager.attach(timeOfDay);
        timeOfDay.setRate(1000f);
        
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(Main.s_AssetManager, 1024, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(Main.s_AssetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);     
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(Main.s_AssetManager, 1024, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
        
  
       
    }
    
    public void updateSun(float tpf)
    {
        float hour = timeOfDay.getHour();
        sc.getSunAndStars().setHour(hour);
        System.out.println(timeOfDay.getSecond());
        
    }
}
