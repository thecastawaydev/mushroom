/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author christian
 */
public final class ObjectHelper {
    
    private static RigidBodyControl physicsControl;
    public static Spatial s_Model;
    public static float modelX;
    public static float modelZ;
    public static Material highlightMat;
    public static Material greenTrans, redTrans;
    
    public static void LoadObjectHelper(){
        highlightMat = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = Main.s_AssetManager.loadTexture("Models/tree.png");
        highlightMat.setTexture("ColorMap", texture);
        
        greenTrans = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greenTrans.setColor("Color", new ColorRGBA(0, 1, 0, 0.3f));
        greenTrans.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        redTrans = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redTrans.setColor("Color", new ColorRGBA(1, 0, 0, 0.3f));
        redTrans.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
    public static Spatial AddModel(Vector3f position, String name){
            s_Model = Main.s_AssetManager.loadModel("Models/tree.obj");
            
            s_Model.setLocalTranslation(position);
            s_Model.scale(0.8f);
            s_Model.setName(name);

            s_Model.setShadowMode(ShadowMode.CastAndReceive);
            
            CollisionShape collision = new CapsuleCollisionShape(0.5f, 1f);        
            physicsControl = new RigidBodyControl(collision, 0);
            physicsControl.setKinematic(true);
            s_Model.addControl(physicsControl);
            Main.bulletAppState.getPhysicsSpace().add(s_Model);
           // Main.s_TreeNode.attachChild(s_Model);
            
            return s_Model;
    }
    
    public static void SetModel(Vector3f position){
        s_Model.setLocalTranslation(position);
        s_Model.getControl(RigidBodyControl.class).setPhysicsLocation(position);
                        ObjectHelper.s_Model.setMaterial(ObjectHelper.greenTrans);
    }
    
    public static void RemoveModel(Spatial model)
    {
        Main.bulletAppState.getPhysicsSpace().remove(model.getControl(RigidBodyControl.class));
        model.removeFromParent();     
    }
    
    public static void HighlightModel(Camera cam, InputManager inputManager)
    {
        CollisionResults results = new CollisionResults();
        Vector2f mouseCoords = new Vector2f(inputManager.getCursorPosition());

        Ray mouseRay = new Ray(cam.getWorldCoordinates(mouseCoords, 0),

        cam.getWorldCoordinates(mouseCoords, 1).subtractLocal(

        cam.getWorldCoordinates(mouseCoords, 0)).normalizeLocal());
        Main.s_TreeNode.collideWith(mouseRay, results);
        
        if(results.size() > 0){
            Spatial target = results.getClosestCollision().getGeometry();
                AmbientLight light = new AmbientLight();
                light.setColor(ColorRGBA.Blue);
                Main.s_TreeNode.addLight(light);
                target.addLight(light);

        }

    }
    
    public static void MoveModel(Camera cam, InputManager inputManager, Spatial scene)
    {
        if(Statics.s_PlayerSettingModel == true){
            CollisionResults results = new CollisionResults();
            Ray ray = new Ray();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d).normalizeLocal();
            ray.setOrigin(click3d);
            ray.setDirection(dir);
             ray = new Ray(click3d, dir);
             scene.collideWith(ray, results);
            
             if(results.size() > 0){
                 CollisionResult point = results.getClosestCollision();
                Vector3f destination = point.getContactPoint();
                ObjectHelper.s_Model.setMaterial(ObjectHelper.greenTrans);
                SetModel(destination);
             }
        }
    }
}
