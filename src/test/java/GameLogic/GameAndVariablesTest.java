package GameLogic;

import GameStates.State;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameAndVariablesTest {

    private Game game;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        game.setGameStarted();
    }

    @Test
    void getsNextPlayerCorrectly() {
        int index = 2;
        Player current = game.getPlayerManager().getPlayers().get(index);
        Player next = game.getPlayerManager().getPlayers().get(index + 1);

        assertEquals(next, game.getPlayerManager().getNextPlayer(current));
    }

    @Test
    void getsNextPlayerCorrectlyIfLastIndex() {
        int index = game.getVariables().getGamePlayers() - 1;
        Player current = game.getPlayerManager().getPlayers().get(index);
        Player next = game.getPlayerManager().getPlayers().get(0);

        assertEquals(next, game.getPlayerManager().getNextPlayer(current));
    }

    @Test
    void getsNextPlayerCorrectlyIfCurrentDeleted() {
        int index = 2;
        Player current = game.getPlayerManager().getPlayers().get(index);
        Player next = game.getPlayerManager().getPlayers().get(index + 1);
        game.getPlayerManager().getPlayers().remove(current);

        assertEquals(next, game.getPlayerManager().getNextPlayer(current));
    }

    @Test
    void getsNextPlayerCorrectlyIfNextTwoDeleted() {
        int index = 0;
        Player current = game.getPlayerManager().getPlayers().get(index);
        Player next = game.getPlayerManager().getPlayers().get(index + 1);
        Player afterNext = game.getPlayerManager().getPlayers().get(index + 2);
        Player target = game.getPlayerManager().getPlayers().get(index + 3);
        game.getPlayerManager().getPlayers().remove(next);
        game.getPlayerManager().getPlayers().remove(afterNext);

        assertEquals(target, game.getPlayerManager().getNextPlayer(current));
    }

    @Test
    void getsNextPlayerCorrectlyIfLastDeleted() {
        int index = game.getVariables().getGamePlayers() - 2;
        Player current = game.getPlayerManager().getPlayers().get(index);
        Player next = game.getPlayerManager().getPlayers().get(index + 1);
        Player target = game.getPlayerManager().getPlayers().get(0);
        game.getPlayerManager().getPlayers().remove(next);

        assertEquals(target, game.getPlayerManager().getNextPlayer(current));
    }

    @RepeatedTest(50)
    void correctRoles() {
        int gamePlayers = new Random().nextInt(5) + 5;
        game = GameInitializationHelper.getInitializedGame(gamePlayers, 0, 1);
        int separatists = ((int) game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SEPARATIST)).count());
        int loyalists = ((int) game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.LOYALIST)).count());
        int palpatines = ((int) game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE)).count());
        int targetLoyalists;
        switch(gamePlayers) {
            case 5 : targetLoyalists = 3; break;
            case 6 : targetLoyalists = 4; break;
            case 7 : targetLoyalists = 4; break;
            case 8 : targetLoyalists = 5; break;
            case 9 : targetLoyalists = 5; break;
            case 10 : targetLoyalists = 6; break;
            default : targetLoyalists = 1;
        }

        assertEquals(1, palpatines);
        assertEquals(loyalists, targetLoyalists);
        assertEquals(gamePlayers - targetLoyalists - 1, separatists);
    }

    @Test
    void noEndCondition() {
        assertFalse(game.checkGameEndConditions());
    }

    @Test
    void loyalistCardsWin() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addLoyalistPolicy());
        assertTrue(game.checkGameEndConditions());
    }

    @Test
    void separatistsCardsWin() {
        IntStream.range(0, 6).forEach(i -> game.getVariables().addSeparatistPolicy());
        assertTrue(game.checkGameEndConditions());
    }

    @Test
    void palpatineDeadWin() {
        Player palpatine = game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE)).findFirst().get();
        game.getPlayerManager().getPlayers().remove(palpatine);
        assertTrue(game.checkGameEndConditions());
    }

    @Test
    void palpatineChancellorAfterThreeSeparatistCards() {
        IntStream.range(0, 4).forEach(i -> game.getVariables().addSeparatistPolicy());
        Player palpatine = game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE)).findFirst().get();
        game.getVariables().setViceChair(palpatine);
        game.stateStatusUpdate(State.LEGISTLATIVE_SESSION);
        assertTrue(game.checkGameEndConditions());
    }
}