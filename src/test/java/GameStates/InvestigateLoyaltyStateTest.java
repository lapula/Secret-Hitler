package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvestigateLoyaltyStateTest {

    private Game game;
    private InvestigateLoyaltyState investigateLoyaltyState;
    private int gamePlayers;

    @BeforeEach
    public void beforeEach() {
        gamePlayers = 6;
        game = GameInitializationHelper.getInitializedGame(gamePlayers, 0, 1);
        investigateLoyaltyState = new InvestigateLoyaltyState(game);
        game.stateStatusUpdate(State.INVESTIGATE_LOYALTY);
    }

    @Test
    void doAction() {
        investigateLoyaltyState.doAction();
        assertEquals(gamePlayers - 1, investigateLoyaltyState.choices.size());
    }

    @Test
    void receiveData() {
        investigateLoyaltyState.doAction();
        Player palpatinePlayer = game.getPlayerManager().getPlayers()
                .stream()
                .filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE))
                .findFirst()
                .get();
        investigateLoyaltyState.receiveData(game.getVariables().getSupremeChancellor().map(Player::getName).orElse(""),
                palpatinePlayer.getName());

        assertEquals(String.format(InvestigateLoyaltyState.INVESTIGATION_INFO, palpatinePlayer.getName(),Role.SEPARATIST.toString()),
                investigateLoyaltyState.investigationInfo);
        assertEquals(State.ROUND_START, game.getGameStateType().orElse(null));
    }
}