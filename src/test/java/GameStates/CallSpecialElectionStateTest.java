package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallSpecialElectionStateTest {

    private Game game;
    private CallSpecialElectionState callSpecialElectionState;
    private int gamePlayers;

    @BeforeEach
    public void beforeEach() {
        gamePlayers = 6;
        game = GameInitializationHelper.getInitializedGame(gamePlayers, 0, 1);
        callSpecialElectionState = new CallSpecialElectionState(game);
        game.stateStatusUpdate(State.CALL_SPECIAL_ELECTION);
    }

    @Test
    void doAction() {
        callSpecialElectionState.doAction();
        assertEquals(gamePlayers - 1, callSpecialElectionState.choices.size());
    }

    @Test
    void receiveData() {
        Player somePlayer = game.getPlayerManager().getPlayers().get(3);
        callSpecialElectionState.doAction();
        callSpecialElectionState.receiveData(somePlayer.getName(), somePlayer.getName());

        assertEquals(game.getVariables().getSupremeChancellor().map(Player::getName).orElse(""),
                game.getVariables().getSupremeChancellorBeforeSpecialElection().map(Player::getName).orElse(""));
        assertEquals(game.getVariables().getSpecialElectionSupremeChancellor().map(Player::getName).orElse(""),
                callSpecialElectionState.specialSupremeChancellor.getName());
        assertEquals(1, game.getVariables().getSpecialElectionPhase());
        assertEquals(State.ROUND_START, game.getGameStateType().orElse(null));
    }
}