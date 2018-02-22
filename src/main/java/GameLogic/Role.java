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
public enum Role {
    SHEEV_PALPATINE("Sheev Palpatine"),
    SEPARATIST("Separatist"),
    LOYALIST("Loyalist");

    private String stringValue;

    Role(String stringValue) {
        this.stringValue = stringValue;
    }
    
    public String toString() {
        return this.stringValue;
    }
}
