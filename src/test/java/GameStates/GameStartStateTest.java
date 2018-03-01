package GameStates;

import GameLogic.Game;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStartStateTest {

    private Game game;
    private GameStartState gameStartState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        gameStartState = new GameStartState(game);
        game.stateStatusUpdate(State.ROUND_START);
    }

    @Test
    void receiveData() {
        gameStartState.receiveData(null, null);
        assertEquals(State.NOMINATE_VICE_CHAIR, game.getGameStateType().orElse(null));
    }


}