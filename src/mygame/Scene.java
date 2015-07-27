/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import static mygame.Main.bulletAppState;

/**
 *
 * @author christian
 */
public class Scene {
    Node sceneNode;
    public Spatial sceneModel;
    
    public void initScene(Node rootNode)
    {
        sceneNode = new Node();
        sceneModel = Main.s_AssetManager.loadModel("Scenes/newScene.j3o");
        sceneModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
    }
}
