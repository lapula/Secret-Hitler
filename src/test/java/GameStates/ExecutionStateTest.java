package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionStateTest {

    private Game game;
    private ExecutionState executionState;
    private int gamePlayers;

    @BeforeEach
    public void beforeEach() {
        gamePlayers = 6;
        game = GameInitializationHelper.getInitializedGame(gamePlayers, 0, 1);
        executionState = new ExecutionState(game);
        game.stateStatusUpdate(State.EXECUTION);
    }

    @Test
    void doAction() {
        executionState.doAction();
        assertEquals(gamePlayers - 1, executionState.choices.size());
    }

    @Test
    void receiveData() {
        Player toBeExecuted = game.getPlayerManager().getPlayers().get(3);
        executionState.doAction();
        executionState.receiveData(toBeExecuted.getName(), toBeExecuted.getName());

        assertEquals(0L, game.getPlayerManager().getPlayers().stream()
                .filter(p -> p.getName().equals(toBeExecuted.getName())).count());
        assertEquals(1L, game.getPlayerManager().getPlayersIncludingExecuted().stream()
                .filter(p -> p.getName().equals(toBeExecuted.getName())).count());
        assertEquals(State.ROUND_START, game.getGameStateType().orElse(null));
    }

}