/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pulli
 */
public class PolicyDeck {
    private List<Policy> policyDeck;
    
    public PolicyDeck() {
        this.policyDeck = initPolicyDeck();
    }
    
    public List<Policy> drawNextThree() {
        if (policyDeck.size() < 3) {
            policyDeck = initPolicyDeck();
        }
        List<Policy> topThree = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            topThree.add(policyDeck.remove(policyDeck.size() - 1 - i));
        }
        return topThree;
    }
    
    private List<Policy> initPolicyDeck() {
        
        List<Policy> deck = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            deck.add(Policy.LIBERAL);
        }
        for (int i = 0; i < 11; i++) {
            deck.add(Policy.FASCIST);
        }
        Collections.shuffle(deck);
        
        return deck;
    }
}
