package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.Player;
import SithImperative.Main;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GameMessageService {

    private final static Logger LOGGER = Logger.getLogger(GameMessageService.class.getName());

    private static final String LISTEN_GAME = "LISTEN_GAME";
    private static final String REGISTER_PLAYER = "REGISTER_PLAYER";
    private static final String QUERY_RESPONSE = "QUERY_RESPONSE";
    private static final String INFORM_VICE_CHAIR = "You are the Vice Chair!";
    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";

    private static final Integer REGISTER_ATTEMPTS = 10;

    private Game game;
    private GamePlayerMessageActions gamePlayerMessageActions;

    private GameScreenMessageActions gameScreenMessageActions;
    private Map<String, JSONObject> pendingAckMessages;

    public GameMessageService(Game game) {
        this.game = game;
        this.gamePlayerMessageActions = new GamePlayerMessageActions(this);
        this.gameScreenMessageActions = new GameScreenMessageActions(this);
        this.pendingAckMessages = new HashMap<>();
    }

    // MESSAGE RECEIVING GAMESCREEN

    public void receiveGameScreenMessage(Session user, Map<String, String> message) {
        if (message.get("type").equals(LISTEN_GAME)) {
            this.game.addGameListener(user);
        }
    }

    // MESSAGE RECEIVING PLAYER

    public void receivePlayerMessage(Session user, Map<String, String> message) {
        if (message.get("type").equals(REGISTER_PLAYER)) {
            registerPlayer(message, user, REGISTER_ATTEMPTS);
        } else if (message.get("type").equals(QUERY_RESPONSE)) {
            queryResponse(message, message.get("playerName"));
        }
    }

    private void registerPlayer(Map<String, String> message, Session user, Integer attempts) {
        String playerName = message.get("playerName");
        String gameName = message.get("gameName");
        Player player = game.getPlayerManager().getPlayerByName(playerName);

        if (checkConnectionValidity(playerName)) {
            if (player == null) {
                Player newPlayer = game.getPlayerManager().addNewPlayer(gameName, playerName, user);
                this.getGamePlayerMessageActions().initPlayer(newPlayer, newPlayer.getRole());
                game.receiveData(playerName, null);
            } else {
                game.getPlayerManager().reconnectPlayer(playerName, user);
                this.getGamePlayerMessageActions().initPlayer(player, player.getRole());

                if (game.getVariables().getViceChairName().equals(playerName)) {
                    this.getGamePlayerMessageActions().setSpecialRole(player, INFORM_VICE_CHAIR);
                }
                if (game.getVariables().getSupremeChancellorName().equals(playerName)) {
                    this.getGamePlayerMessageActions().setSpecialRole(player, INFORM_SUPREME_CHANCELLOR);
                }

                if (getPlayerPendingMessages(playerName) != null) {
                    LOGGER.info("RECONNECTED: " + player.getName() + " SENDING: " + getPlayerPendingMessages(playerName));
                    sendPlayerMessageRequiredResponse(player, getPlayerPendingMessages(playerName), attempts);
                }
            }
        } else {
            System.out.println("PLAYER ALREADY CONNECTED");
            user.close();
        }
    }

    private boolean checkConnectionValidity(String playerName) {
        Player p = this.game.getPlayerManager().getPlayerByName(playerName);
        if (p != null) {
            if (p.getSession() != null) {
                //TODO fix this
                return true;
                //return false;
            }
        }
        return true;
    }

    private void queryResponse(Map<String, String> message, String playerName) {
        ackMessage(playerName);
        String response = message.get("response");
        game.receiveData(playerName, response);
    }

    // MESSAGE SENDING GAMESCREEN

    public void sendGameScreenMessage(Session listener, JSONObject gameScreenMessage, Integer attempts) {
        if (attempts <= 0) {return;}
        try {
            listener.getRemote().sendString(gameScreenMessage.toString());
        } catch (Exception ex) {
            System.out.println("GameScreenMessage sending failed");
            delayedRetryMessageSending(() -> sendGameScreenMessage(listener, gameScreenMessage, attempts - 1));
        }
    }

    // MESSAGE SENDING PLAYER

    protected void sendPlayerMessageRequiredResponse(Player target, JSONObject gameMessage, Integer attempts) {
        if (attempts <= 0) {return;}
        try {
            System.out.println(target);
            LOGGER.info("TARGET: " + target.getName() + " SENT: " + gameMessage);

            target.getSession().getRemote().sendString(gameMessage.toString());
            addPlayerPendingMessage(target.getName(), gameMessage);
        } catch (Exception ex) {
            //delayedRetryMessageSending(target, gameMessage);
            System.out.println("playerMessageRR sending failed");
            ex.printStackTrace();
            delayedRetryMessageSending(() -> sendPlayerMessageRequiredResponse(target, gameMessage, attempts - 1));
        }
    }

    public void sendPlayerMessage(Player target, JSONObject gameMessage, Integer attempts) {
        if (attempts <= 0) {return;}
        try {
            target.getSession().getRemote().sendString(gameMessage.toString());
        } catch (Exception ex) {
            System.out.println("playerMessage sending failed");
            //delayedRetryMessageSending(target, gameMessage);
            delayedRetryMessageSending(() -> sendPlayerMessage(target, gameMessage, attempts - 1));
        }
    }

    public GamePlayerMessageActions getGamePlayerMessageActions() {
        return gamePlayerMessageActions;
    }

    public GameScreenMessageActions getGameScreenMessageActions() {
        return gameScreenMessageActions;
    }

    private JSONObject getPlayerPendingMessages(String playerName) {
        return this.pendingAckMessages.get(playerName);
    }

    private void ackMessage(String playerName) {
        this.pendingAckMessages.put(playerName, null);
    }

    private void addPlayerPendingMessage(String playerName, JSONObject gameMessage) {
        if (!gameMessage.equals(pendingAckMessages.get(playerName))) {
            LOGGER.severe("MESSAGE LOST EARLIER");
            System.out.println(pendingAckMessages.get(playerName));
            System.out.println(gameMessage);
        } else {
            this.pendingAckMessages.put(playerName, gameMessage);
            LOGGER.info("ADD PENDING MESSAGE: " + playerName + " MESSAGE: " + gameMessage);
        }
    }

    private void delayedRetryMessageSending(Runnable sendMessage) {

        LOGGER.warning("FAILED, trying again in 3s");
        try {
            TimeUnit.SECONDS.sleep(5);
            sendMessage.run();
        } catch (InterruptedException e) {
            LOGGER.severe("WAIT FAILED, MESSAGE LOST");
        }
    }
}
