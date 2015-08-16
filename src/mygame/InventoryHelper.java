/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Spatial;

/**
 *
 * @author christian
 */
public class InventoryHelper {
    
    public static void AddToInventory(Spatial itemToAdd){
        for(int i = 0; i < 40; i++){
            if(Main.inventory.inventorySlots[i].itemName == "empty"){
                Main.inventory.inventorySlots[i].itemName = itemToAdd.getName();
            } 
        }
    }
}
