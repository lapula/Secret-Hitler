/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

/**
 *
 * @author pulli
 */
public enum Policy {
    LOYALIST_POLICY("Loyalist policy"),
    SEPARATIST_POLICY("Separatist policy");
    
    private String stringValue;
    Policy(String stringValue) {
        this.stringValue = stringValue;
    }
    
    public String toString() {
        return this.stringValue;
    }
}
