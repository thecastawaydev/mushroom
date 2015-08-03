package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {

    public static Node s_TreeNode;
    public static AssetManager s_AssetManager;
    public static AppStateManager s_StateManager;
    public static InputManager s_InputManager;
    
    private PlayerNode player;
    public static BulletAppState bulletAppState;
    
    private Scene mainScene;
    private Sun sun;
    
    BitmapText hudText;
    Inventory inventory;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        s_TreeNode = new Node();
        s_AssetManager = assetManager;
        s_StateManager = stateManager;
        s_InputManager = inputManager;
        inventory = new Inventory(this, guiNode, rootNode, cam);
        ObjectHelper.LoadObjectHelper();
                      
        mouseInput.setCursorVisible(true);
        flyCam.setEnabled(false);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        mainScene = new Scene();
        mainScene.initScene(rootNode);
        sun = new Sun();
        sun.initSun(rootNode, viewPort, cam);
        
        Spatial mushroom = assetManager.loadModel("Models/mushroom.obj");
        player = new PlayerNode(mushroom, inputManager, cam, mainScene.sceneModel);
        player.getCharacterControl().setPhysicsLocation(new Vector3f(0, 2f, 0));
        rootNode.attachChild(player);
        bulletAppState.getPhysicsSpace().add(player);
        
        Spatial model =  ObjectHelper.AddModel(new Vector3f(5, 0, 5), "tree");
        s_TreeNode.attachChild(model);
        
                          
        rootNode.attachChild(s_TreeNode);

        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.White);
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0);
        guiNode.attachChild(hudText);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        ObjectHelper.HighlightModel(cam, inputManager);
        ObjectHelper.MoveModel(cam, inputManager, mainScene.sceneModel);
        sun.updateSun(tpf);
        hudText.setText(sun.timeOfDay.getHour() + ":" + sun.timeOfDay.getSecond());
        inventory.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
