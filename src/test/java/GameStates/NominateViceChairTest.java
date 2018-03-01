package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NominateViceChairTest {

    private Game game;
    private NominateViceChair nominateState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        nominateState = new NominateViceChair(game);
    }

    @Test
    void testNoPreviousPositions() {
        nominateState.doAction();
        assertEquals(5, nominateState.choices.size());
    }

    @Test
    void testWithPreviousPositions() {
        game.getVariables().setPreviousSupremeChancellor(game.getPlayerManager().getPlayers().get(4));
        game.getVariables().setPreviousViceChair(game.getPlayerManager().getPlayers().get(3));
        nominateState.doAction();

        assertEquals(3, nominateState.choices.size());
    }

    @Test
    void testWithPreviousPositionsAndAssassinations() {
        game.getVariables().setPreviousSupremeChancellor(game.getPlayerManager().getPlayers().get(4));
        game.getVariables().setPreviousViceChair(game.getPlayerManager().getPlayers().get(3));
        game.getPlayerManager().getPlayers().remove(2);
        nominateState.doAction();

        assertEquals(3, nominateState.choices.size());
    }
}