/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author christian
 */
public final class ObjectHelper {
    
    private static RigidBodyControl physicsControl;
    public static Spatial s_Model;
    public static float modelX;
    public static float modelZ;
    
    public static Spatial AddModel(Vector3f position){
            s_Model = Main.s_AssetManager.loadModel("Models/tree.obj");
            
            s_Model.setLocalTranslation(position);
            s_Model.scale(0.8f);
            s_Model.setName("tree");
            
            modelX = s_Model.getLocalTranslation().x;
            modelZ = s_Model.getLocalTranslation().z;
            
            CollisionShape collision = new CapsuleCollisionShape(0.5f, 1f);        
            physicsControl = new RigidBodyControl(collision, 0);
            s_Model.addControl(physicsControl);
            Main.bulletAppState.getPhysicsSpace().add(s_Model);
           // Main.s_TreeNode.attachChild(s_Model);
            
            return s_Model;
    }
    
    public static void SetModel(Vector3f position){
        s_Model.setLocalTranslation(position);
        s_Model.getControl(RigidBodyControl.class).setPhysicsLocation(position);
        
    }
    
    public static void RemoveModel(Spatial model)
    {
        Main.bulletAppState.getPhysicsSpace().remove(model.getControl(RigidBodyControl.class));
        model.removeFromParent();     
    }
}
