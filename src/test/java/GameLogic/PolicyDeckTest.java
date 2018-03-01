package GameLogic;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PolicyDeckTest {

    @Test
    public void deckCreatedCorrectly() {
        PolicyDeck d = new PolicyDeck();
        assertTrue(d.getDeck().size() == 17);
        assertTrue(d.getDeck().stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count() == 6);
        assertTrue(d.getDeck().stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count() == 11);
    }

    @Test
    public void getsTopCard() {
        PolicyDeck d = new PolicyDeck();
        int fullSize = d.getDeck().size();
        d.drawNext();
        assertEquals(fullSize - 1, d.getDeck().size());

        IntStream.range(0, fullSize).forEach(i -> d.drawNext());
        assertEquals(fullSize - 1, d.getDeck().size());
    }

    @RepeatedTest(50)
    public void getsTopThreeCards() {
        PolicyDeck d = new PolicyDeck();
        int size = d.getDeck().size();

        String topCardsString = d.nextThreeToString();
        assertEquals(size, d.getDeck().size());
        List<Policy> lastCards = Arrays.asList(d.getDeck().get(size - 1), d.getDeck().get(size - 2), d.getDeck().get(size - 3));
        List<Policy> topCards = d.drawNextThree();

        assertEquals(lastCards, topCards);
        assertEquals(lastCards.get(2) + ", " + lastCards.get(1) + ", " + lastCards.get(0), topCardsString);
    }

    @RepeatedTest(100)
    public void shufflesEmptyDeckCorrectly() {
        PolicyDeck d = new PolicyDeck();
        assertTrue(d.getDeck().size() == 17);

        IntStream.range(0, 5).forEach(i -> d.drawNextThree());
        List<Policy> bottom = new ArrayList<>(d.getDeck());
        List<Policy> nextThree = d.drawNextThree();

        int bottomLoyalistCards = ((int) bottom.stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count());
        int bottomSeparatistCards = ((int) bottom.stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count());
        int nextThreeLoyalistCards = ((int) nextThree.stream().filter(p -> p.equals(Policy.LOYALIST_POLICY)).count());
        int nextThreeSeparatistCards = ((int) nextThree.stream().filter(p -> p.equals(Policy.SEPARATIST_POLICY)).count());

        assertTrue(bottomLoyalistCards <= nextThreeLoyalistCards);
        assertTrue(bottomSeparatistCards <= nextThreeSeparatistCards);
    }
}