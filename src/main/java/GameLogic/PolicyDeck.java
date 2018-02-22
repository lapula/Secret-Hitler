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
        System.out.println(policyDeck.toString());
        if (policyDeck.size() < 3) {
            policyDeck = initPolicyDeck();
        }
        List<Policy> topThree = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            topThree.add(policyDeck.remove(policyDeck.size() - 1));
        }
        return topThree;
    }
    
    public String nextThreeToString() {
        if (policyDeck.size() < 3) {
            policyDeck = initPolicyDeck();
        }
        String topThree = "";
        for (int i = 0; i < 3; i++) {
            topThree += policyDeck.get(policyDeck.size() - 1 - i);
            if (i < 2) {
                topThree += ", ";
            }
        }
        return topThree;
    }
    
    private List<Policy> initPolicyDeck() {
        
        List<Policy> deck = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            deck.add(Policy.LOYALIST_POLICY);
        }
        for (int i = 0; i < 11; i++) {
            deck.add(Policy.SEPARATIST_POLICY);
        }
        Collections.shuffle(deck);
        
        return deck;
    }

    public Integer getDeckCardsCount() {
        return policyDeck.size();
    }
}
