/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author christian
 */
public class InventorySlot {
    public int slotNumber;
    public int quantity = 0;
    public String itemName;
    
    public InventorySlot(int slotNumber, int quantity, String itemName){
        this.slotNumber = slotNumber;
        this.quantity = quantity;
        this.itemName = itemName;
    }
}
