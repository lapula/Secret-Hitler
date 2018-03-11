package GameLogic;

import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PolicyDeckTest {

    private Game game;
    private PolicyDeck deck;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        game.setGameStarted();
        deck = game.getPolicyDeck();
    }

    @Test
    public void deckCreatedCorrectly() {
        assertTrue(deck.getDeck().size() == 17);
        assertTrue(deck.getDeck().stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count() == 6);
        assertTrue(deck.getDeck().stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count() == 11);
    }

    @Test
    public void deckCreatedCorrectlyWithExistingPolicies() {
        game.getVariables().addSeparatistPolicy();
        game.getVariables().addSeparatistPolicy();
        game.getVariables().addSeparatistPolicy();
        game.getVariables().addLoyalistPolicy();
        deck = new PolicyDeck(game);

        assertEquals(13, deck.getDeck().size());
        assertTrue(deck.getDeck().stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count() == 5);
        assertTrue(deck.getDeck().stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count() == 8);
    }

    @Test
    public void getsTopCard() {
        int fullSize = deck.getDeck().size();
        deck.drawNext();
        assertEquals(fullSize - 1, deck.getDeck().size());

        IntStream.range(0, fullSize).forEach(i -> deck.drawNext());
        assertEquals(fullSize - 1, deck.getDeck().size());
    }

    @RepeatedTest(50)
    public void getsTopThreeCards() {
        int size = deck.getDeck().size();

        String topCardsString = deck.nextThreeToString();
        assertEquals(size, deck.getDeck().size());
        List<Policy> lastCards = Arrays.asList(deck.getDeck().get(size - 1), deck.getDeck().get(size - 2), deck.getDeck().get(size - 3));
        List<Policy> topCards = deck.drawNextThree();

        assertEquals(lastCards, topCards);
        assertEquals(lastCards.get(2) + ", " + lastCards.get(1) + ", " + lastCards.get(0), topCardsString);
    }

    @Test
    public void getsTopThreeCardsWithTwoLeft() {
        assertTrue(deck.getDeck().size() == 17);

        IntStream.range(0, 5).forEach(i -> deck.drawNextThree());
        deck.nextThreeToString();
        assertEquals(17, deck.getDeck().size());
    }

    @Test
    public void shufflesEmptyDeckCorrectly() {
        assertTrue(deck.getDeck().size() == 17);
        IntStream.range(0, 6).forEach(i -> deck.drawNextThree());
        assertEquals(14, deck.getDeck().size());

        game.getVariables().addSeparatistPolicy();
        game.getVariables().addSeparatistPolicy();
        game.getVariables().addSeparatistPolicy();
        game.getVariables().addLoyalistPolicy();

        IntStream.range(0, 5).forEach(i -> deck.drawNextThree());

        assertEquals(10, deck.getDeck().size());
    }
}