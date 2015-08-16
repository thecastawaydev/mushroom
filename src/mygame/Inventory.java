package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import tonegod.gui.controls.extras.DragElement;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;

public class Inventory extends SimpleApplication implements RawInputListener, ActionListener {
    
private Screen screen;
private float iconSize = 40;
Vector2f dim = new Vector2f();
Vector4f windowPadding = new Vector4f();
float dragBarHeight;
private Node rootNode;

Vector2f click2d = new Vector2f(), tempV2 = new Vector2f();
Vector3f click3d = new Vector3f(), pickDir = new Vector3f(), tempV3 = new Vector3f();
Ray pickRay = new Ray();
CollisionResults rayResults = new CollisionResults();
CollisionResult closest;
Camera cam;

boolean showInventory = false;
public Element inventory;
Window win;
InventorySlot[] inventorySlots;

public Inventory(Main main, Node guiNode, Node rootNode, Camera cam){
    initInventory(main, guiNode, rootNode, cam);
}
public void initInventory(Main main, Node guiNode, Node rootNode, Camera cam) {

     inventorySlots = new InventorySlot[40];
	createGUIScreen(main, guiNode);
	layoutGUI();

        this.rootNode = rootNode;
        this.cam = cam;
        
                
            Main.s_InputManager.addMapping("ShowInventory", new KeyTrigger(KeyInput.KEY_I));
    Main.s_InputManager.addListener(this, "ShowInventory");
    
   
}


private void createGUIScreen(Main main, Node guiNode) {
	screen = new Screen(main);
	screen.setUseUIAudio(true);
	screen.setUIAudioVolume(1f);
        screen.setUseToolTips(true);
	guiNode.addControl(screen);

}

private void layoutGUI() {
	windowPadding.set(screen.getStyle("Window#Dragbar").getVector4f("indents"));
	dragBarHeight = screen.getStyle("Window#Dragbar").getFloat("defaultControlSize");

	 inventory = new Element(
		screen,
		UIDUtil.getUID(),
		new Vector2f(
			windowPadding.x,
			(windowPadding.y*2)+dragBarHeight
		),
		Vector2f.ZERO,
		Vector4f.ZERO,
		null
	);
        
        Element equipSlot = new Element(
                screen,
                UIDUtil.getUID(),
                new Vector2f(windowPadding.x, (windowPadding.y*2) + dragBarHeight),
                Vector2f.ZERO,
                Vector4f.ZERO,
                null
                );
        
	inventory.setAsContainerOnly();
        equipSlot.setAsContainerOnly();
        
	for (int i = 0; i < 40; i++) {
            
		float x = i *iconSize;
                float y = 0;
		x += 2.5f;
                
                if(i >= 10 && i <= 19){
                    y = 1 * iconSize;
                    x = (i - 10) * iconSize + 2.5f;
                }
                else if(i >= 20 && i <= 29){
                    y = 2 * iconSize;
                    x = (i - 20) * iconSize + 2.5f;
                 }
                else if(i >= 30 && i <= 39){
                    y = 3 * iconSize;
                    x = (i - 30) * iconSize + 2.5f;
                }
                Element e = createInventorySlot(i, x, y);
                
                inventorySlots[i] = new InventorySlot(i, 0, "empty");
                e.setToolTipText(inventorySlots[i].itemName + " : " + inventorySlots[i].slotNumber + " : " + inventorySlots[i].quantity);
		inventory.addChild(e);
	}
        
        for(int i = 0; i < 10; i++){
            
        }
	inventory.sizeToContent();

	dim.set(
		inventory.getWidth()+(windowPadding.x*2),
		inventory.getHeight()+(windowPadding.y*3)+dragBarHeight
	);
	 win = new Window(screen, Vector2f.ZERO, dim);
	win.addChild(inventory);
	win.setlockToParentBounds(true);
	win.setIsResizable(false);
	screen.addElement(win);
}

private Element createInventorySlot(int index, float x, float y) {
	Element slot = new Element(
		screen,
		"InvSlot" + index,
		new Vector2f(x,y),
		new Vector2f(iconSize,iconSize),
		screen.getStyle("CheckBox").getVector4f("resizeBorders"),
		screen.getStyle("CheckBox").getString("defaultImg")
	);
	slot.setIsDragDropDropElement(true);
	return slot;
}

public DragElement createNewDragElement() {
	DragElement e = new DragElement(
		screen,
		new Vector2f(
			screen.getMouseXY().x-(iconSize/2),
			screen.getHeight()-(screen.getMouseXY().y+(iconSize/2))

		),
		new Vector2f(iconSize,iconSize),
		Vector4f.ZERO,
		"tonegod/gui/style/def/Common/Particles/spark.png"
	) {
		@Override
		public void onDragStart(MouseButtonEvent evt) {

		}
		@Override
		public boolean onDragEnd(MouseButtonEvent evt, Element dropElement) {
			if (dropElement != null) {
				setlockToParentBounds(false);
				return true;
			} else {
				Node n = getUserData("worldObject");
				rootNode.attachChild(n);
				screen.removeElement(this);
				return false;
			}
		}
	};
	e.setlockToParentBounds(true);
	e.setUseLockToDropElementCenter(true);
	e.setUseSpringBack(true);
	screen.addElement(e);
	return e;
}



public void beginInput() {  }
public void endInput() {  }
public void onJoyAxisEvent(JoyAxisEvent evt) {  }
public void onJoyButtonEvent(JoyButtonEvent evt) {  }
public void onMouseMotionEvent(MouseMotionEvent evt) {  }
public void onMouseButtonEvent(MouseButtonEvent evt) {
	if (evt.getButtonIndex() == 0 && evt.isPressed()) {
		click2d.set(Main.s_InputManager.getCursorPosition());
		tempV2.set(click2d);
		click3d.set(cam.getWorldCoordinates(tempV2, 0f));
		pickDir.set(cam.getWorldCoordinates(tempV2, 1f).subtractLocal(click3d).normalizeLocal());
		pickRay.setOrigin(click3d);
		pickRay.setDirection(pickDir);
		rayResults.clear();

		// Check for targeting collision
		rootNode.collideWith(pickRay, rayResults);
		closest = rayResults.getClosestCollision();

		if (closest != null) {
			Geometry geom = closest.getGeometry();
			Node parent = geom.getParent();
			DragElement de = createNewDragElement();
			de.setUserData("worldObject", parent);
			parent.removeFromParent();
		//	inputManager.setCursorVisible(true);
		}
	}
}
public void onKeyEvent(KeyInputEvent evt) {  }
public void onTouchEvent(TouchEvent evt) {  }

    public void onAction(String name, boolean value, float tpf) {
               if(name.equals("ShowInventory")){
            if(!value){
                if(showInventory == true){
                    showInventory = false;
                }else{
                    showInventory = true;
                }
            }
        }
    }

    @Override
    public void simpleInitApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void simpleUpdate(float tpf) {
    System.out.println(showInventory);
    if(showInventory == true)
       win.show();
    else
      win.hide();
}
}
