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

    public PolicyDeck() {
        this.policyDeck = initPolicyDeck(new ArrayList<>(), false);
    }

    public Policy drawNext() {

        if (policyDeck.size() == 0) {
            policyDeck = initPolicyDeck(new ArrayList<>(), false);
        }
        Policy next = policyDeck.remove(policyDeck.size() - 1);
        return next;
    }
    
    public List<Policy> drawNextThree() {

        if (policyDeck.size() < 3) {
            List<Policy> bottomCards = new ArrayList<>();
            bottomCards.addAll(policyDeck);
            policyDeck = initPolicyDeck(bottomCards, false);
        }
        List<Policy> topThree = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> topThree.add(policyDeck.remove(policyDeck.size() - 1)));

        return topThree;
    }
    
    public String nextThreeToString() {
        if (policyDeck.size() < 3) {
            List<Policy> bottomCards = new ArrayList<>();
            bottomCards.addAll(policyDeck);
            policyDeck = initPolicyDeck(bottomCards, true);
        }
        List<String> topThree = new ArrayList<>();
        IntStream.range(policyDeck.size() - 3, policyDeck.size()).forEach(i -> topThree.add(policyDeck.get(i).toString()));

        return String.join(", ", topThree);
    }
    
    private List<Policy> initPolicyDeck(List<Policy> bottomCards, boolean onlyPeek) {
        int bottomLoyalistCards = 0;
        int bottomSeparatistCards = 0;

        if (onlyPeek) {
            bottomLoyalistCards = ((int) bottomCards.stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count());
            bottomSeparatistCards = ((int) bottomCards.stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count());
        }

        List<Policy> deck = new ArrayList<>();
        IntStream.range(0, LOYALIST_CARDS_INITIAL - bottomLoyalistCards).forEach(i -> deck.add(Policy.LOYALIST_POLICY));
        IntStream.range(0, SEPARATIST_CARDS_INITIAL - bottomSeparatistCards).forEach(i -> deck.add(Policy.SEPARATIST_POLICY));
        Collections.shuffle(deck);
        deck.addAll(bottomCards);

        return deck;
    }

    public List<Policy> getDeck() {
        return policyDeck;
    }
}
