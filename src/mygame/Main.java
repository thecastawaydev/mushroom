package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

public class Main extends SimpleApplication {

    public static Node s_TreeNode;
    public static AssetManager s_AssetManager;
    
    Node sceneNode;
    private PlayerNode player;
    public static BulletAppState bulletAppState;
    
    private RigidBodyControl scene;
    private Spatial sceneModel;
    
    public WireBox wireBox;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        wireBox = new WireBox();
        
        s_TreeNode = new Node();
        s_AssetManager = assetManager;
        ObjectHelper.LoadObjectHelper();
                
        sceneNode = new Node();
        mouseInput.setCursorVisible(true);
        flyCam.setEnabled(false);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        sceneModel = assetManager.loadModel("Scenes/newScene.j3o");
        Spatial mushroom = assetManager.loadModel("Models/mushroom.obj");
        player = new PlayerNode(mushroom, inputManager, cam, sceneModel);
        player.getCharacterControl().setPhysicsLocation(new Vector3f(0, 2f, 0));
        rootNode.attachChild(player);
        bulletAppState.getPhysicsSpace().add(player);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
        
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 1024, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);

        Spatial model =  ObjectHelper.AddModel(new Vector3f(5, 0, 5));
        s_TreeNode.attachChild(model);
        
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
                          
        rootNode.attachChild(s_TreeNode);

        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        System.out.println(Statics.s_PlayerSettingModel);
        ObjectHelper.HighlightModel(cam, inputManager);
        ObjectHelper.MoveModel(cam, inputManager, sceneModel);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
