package GameMessagingInterface;

import GameLogic.Game;
import SithImperative.Main;
import org.easymock.EasyMock;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameScreenWebSocketInterfaceTest {

    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String LISTEN_GAME = "LISTEN_GAME";


    private Game game;
    private GameScreenWebSocketInterface gameScreenInterface;

    @BeforeEach
    public void beforeEach() {
        game = new Game("test", 6);
        Main.games = new ConcurrentHashMap<>();
        gameScreenInterface = new GameScreenWebSocketInterface();
    }

    @Test
    public void canCreateNewGame() {
        Session session = EasyMock.mock(Session.class);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", CREATE_GAME);
        jsonMessage.put("gamePlayers", 6);
        gameScreenInterface.onMessage(session, jsonMessage.toString());
        gameScreenInterface.onMessage(session, jsonMessage.toString());

        assertEquals(2, Main.games.size());
    }

    @Test
    public void canListenGame() {
        Session session = EasyMock.mock(Session.class);
        JSONObject messageCreate = new JSONObject();
        messageCreate.put("type", CREATE_GAME);
        messageCreate.put("gamePlayers", 6);
        gameScreenInterface.onMessage(session, messageCreate.toString());

        String gameName = Main.games.keySet().iterator().next();
        JSONObject messageListen = new JSONObject();
        messageListen.put("type", LISTEN_GAME);
        messageListen.put("gameName", gameName);
        gameScreenInterface.onMessage(session, messageListen.toString());

        assertEquals(2, Main.games.get(gameName).getGameListeners().size());
    }


}