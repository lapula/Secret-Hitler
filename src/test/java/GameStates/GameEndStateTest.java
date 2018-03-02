package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameEndStateTest {

    private Game game;
    private GameEndState gameEndState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        gameEndState = new GameEndState(game);
        game.stateStatusUpdate(State.GAME_END);
    }

    @Test
    void loyalistCardsWin() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addLoyalistPolicy());
        gameEndState.doAction();
        assertEquals(GameEndState.LOYALIST_WIN_CARDS, gameEndState.subheader);
    }

    @Test
    void separatistsCardsWin() {
        IntStream.range(0, 6).forEach(i -> game.getVariables().addSeparatistPolicy());
        gameEndState.doAction();
        assertEquals(GameEndState.SEPARATISTS_WIN_CARDS, gameEndState.subheader);
    }

    @Test
    void palpatineDeadWin() {
        Player palpatine = game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE)).findFirst().get();
        game.getPlayerManager().getPlayers().remove(palpatine);
        gameEndState.doAction();
        assertEquals(GameEndState.LOYALIST_WIN_PALPATINE, gameEndState.subheader);
    }

    @Test
    void palpatineChancellorWin() {
        IntStream.range(0, 4).forEach(i -> game.getVariables().addSeparatistPolicy());
        Player palpatine = game.getPlayerManager().getPlayers().stream().filter(p -> p.getRole().equals(Role.SHEEV_PALPATINE)).findFirst().get();
        game.getVariables().setViceChair(palpatine);
        game.stateStatusUpdate(State.LEGISTLATIVE_SESSION);
        gameEndState.doAction();
        assertEquals(GameEndState.SEPARATISTS_WIN_PALPATINE, gameEndState.subheader);
    }

}