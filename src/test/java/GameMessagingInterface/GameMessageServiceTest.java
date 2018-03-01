package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.PolicyDeck;
import Helpers.GameInitializationHelper;
import org.easymock.EasyMock;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

//TODO finish this
class GameMessageServiceTest {

    private static final String LISTEN_GAME = "LISTEN_GAME";
    private static final String REGISTER_PLAYER = "REGISTER_PLAYER";
    private static final String QUERY_RESPONSE = "QUERY_RESPONSE";

    private Game game;

    @BeforeEach
    public void beforeEach() {
        game = new Game("test", 6);
    }

    @Test
    public void registerNewPlayer() {
        Session user = EasyMock.mock(Session.class);
        GamePlayerMessageActions gamePlayerMessageActions = EasyMock.mock(GamePlayerMessageActions.class);
        game.getGameMessageService().setGamePlayerMessageActions(gamePlayerMessageActions);
        Map<String, String> message = new HashMap<>();
        message.put("type", REGISTER_PLAYER);
        message.put("gameName", "test");
        message.put("playerName", "Obi-Wan");

        game.getGameMessageService().receivePlayerMessage(user, message);
        assertEquals(1, game.getPlayerManager().getPlayers().size());
    }

    @Test
    public void reconnectExistingPlayer() {
        Session user = EasyMock.mock(Session.class);
        GamePlayerMessageActions gamePlayerMessageActions = EasyMock.mock(GamePlayerMessageActions.class);
        game.getGameMessageService().setGamePlayerMessageActions(gamePlayerMessageActions);
        Map<String, String> message = new HashMap<>();
        message.put("type", REGISTER_PLAYER);
        message.put("gameName", "test");
        message.put("playerName", "Obi-Wan");
        game.getGameMessageService().receivePlayerMessage(user, message);
        game.getPlayerManager().getPlayers().get(0).setSession(null);

        game.getGameMessageService().receivePlayerMessage(user, message);

        assertEquals(1, game.getPlayerManager().getPlayers().size());
        assertTrue(game.getPlayerManager().getPlayers().get(0).getSession().isPresent());
    }

}