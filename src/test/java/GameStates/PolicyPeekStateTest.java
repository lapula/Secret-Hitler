package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicyPeekStateTest {

    private Game game;
    private PolicyPeekState nominateState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        nominateState = new PolicyPeekState(game);
    }

    @Test
    void testAction() {
        nominateState.doAction();
        assertEquals(1, nominateState.choices.size());
    }

    @Test
    void testReceiveData() {
        nominateState.doAction();
        nominateState.receiveData(game.getVariables().getSupremeChancellor().map(Player::getName).orElse(""),
                game.getPolicyDeck().nextThreeToString());


        assertEquals(1, nominateState.choices.size());
        assertEquals(State.ROUND_START, game.getGameStateType().orElse(null));
    }

}