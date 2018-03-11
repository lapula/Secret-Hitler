/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author pulli
 */
public class PolicyDeck {
    private List<Policy> policyDeck;

    private static final int LOYALIST_CARDS_INITIAL = 6;
    private static final int SEPARATIST_CARDS_INITIAL = 11;

    private Game game;
    public PolicyDeck(Game game) {
        this.game = game;
        this.policyDeck = initPolicyDeck();
    }

    public Policy drawNext() {

        if (policyDeck.size() == 0) {
            policyDeck = initPolicyDeck();
        }
        Policy next = policyDeck.remove(policyDeck.size() - 1);
        return next;
    }
    
    public List<Policy> drawNextThree() {

        if (policyDeck.size() < 3) {
            policyDeck = initPolicyDeck();
        }
        List<Policy> topThree = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> topThree.add(policyDeck.remove(policyDeck.size() - 1)));

        return topThree;
    }
    
    public String nextThreeToString() {
        if (policyDeck.size() < 3) {
            policyDeck = initPolicyDeck();
        }
        List<String> topThree = new ArrayList<>();
        IntStream.range(policyDeck.size() - 3, policyDeck.size()).forEach(i -> topThree.add(policyDeck.get(i).toString()));

        return String.join(", ", topThree);
    }
    
    private List<Policy> initPolicyDeck() {

        List<Policy> deck = new ArrayList<>();
        IntStream.range(0, LOYALIST_CARDS_INITIAL - game.getVariables().getLoyalistPolicyCount()).forEach(i -> deck.add(Policy.LOYALIST_POLICY));
        IntStream.range(0, SEPARATIST_CARDS_INITIAL - game.getVariables().getSeparatistPolicyCount()).forEach(i -> deck.add(Policy.SEPARATIST_POLICY));
        Collections.shuffle(deck);

        return deck;
    }

    public List<Policy> getDeck() {
        return policyDeck;
    }
}
