package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.Player;
import GameStates.GameState;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GameMessageService {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(GameMessageService.class);

    private static final String REGISTER_PLAYER = "REGISTER_PLAYER";
    private static final String QUERY_RESPONSE = "QUERY_RESPONSE";
    private static final String INFORM_VICE_CHAIR = "You are the Vice Chair!";
    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";

    private static final Integer REGISTER_ATTEMPTS = 10;

    private Game game;
    private GamePlayerMessageActions gamePlayerMessageActions;
    private GameScreenMessageActions gameScreenMessageActions;
    private Map<String, JSONObject> pendingAckMessages;
    private ConcurrentHashSet<Thread> threads;


    public GameMessageService(Game game) {
        this.game = game;
        this.gamePlayerMessageActions = new GamePlayerMessageActions(this);
        this.gameScreenMessageActions = new GameScreenMessageActions(this);
        this.pendingAckMessages = new HashMap<>();
        this.threads = new ConcurrentHashSet<>();
    }

    // MESSAGE RECEIVING GAMESCREEN

    // Done in interface due to game not possibly existing yet

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
        Optional<Player> optionalPlayer = game.getPlayerManager().getPlayerByName(playerName);

        if (checkConnectionValidity(playerName)) {
            if (!optionalPlayer.isPresent()) {
                Player newPlayer = game.getPlayerManager().addNewPlayer(playerName, user);
                this.getGamePlayerMessageActions().initPlayer(newPlayer, newPlayer.getRole());
                game.receiveData(playerName, null);
            } else {
                Player player = optionalPlayer.get();
                game.getPlayerManager().reconnectPlayer(playerName, user);

                this.getGamePlayerMessageActions().initPlayer(player, player.getRole());

                if (game.getVariables().getViceChair().map(Player::getName).orElse("").equals(playerName)) {
                    this.getGamePlayerMessageActions().setSpecialRole(player, INFORM_VICE_CHAIR);
                }
                if (game.getVariables().getSupremeChancellor().map(Player::getName).orElse("").equals(playerName)) {
                    this.getGamePlayerMessageActions().setSpecialRole(player, INFORM_SUPREME_CHANCELLOR);
                }

                if (getPlayerPendingMessages(playerName) != null) {
                    logger.info("RECONNECTED: " + player.getName() + " SENDING: " + getPlayerPendingMessages(playerName));
                    JSONObject msg = getPlayerPendingMessages(playerName);
                    clearPendingMessage(player.getName());
                    sendPlayerMessageRequiredResponse(player, msg, attempts);

                }
            }
        } else {
            logger.error("PLAYER ALREADY CONNECTED");
            user.close();
        }
    }

    private boolean checkConnectionValidity(String playerName) {
        Optional<Player> p = this.game.getPlayerManager().getPlayerByName(playerName);
        if (p.isPresent()) {
            logger.info("Connection valid? " + !p.get().getSession().isPresent());
            if (p.get().getSession().isPresent()) {
                return false;
            }
        }
        return true;
    }

    private void queryResponse(Map<String, String> message, String playerName) {
        clearPendingMessage(playerName);
        String response = message.get("response");
        game.receiveData(playerName, response);
    }

    // MESSAGE SENDING GAMESCREEN

    public void sendGameScreenMessage(Session listener, JSONObject gameScreenMessage, Integer attempts) {
        Runnable runnable = () -> {
            if (attempts <= 0) {return;}
            try {
                listener.getRemote().sendString(gameScreenMessage.toString());
            } catch (Exception ex) {
                logger.info("GameScreenMessage sending failed");
                delayedRetryMessageSending(() -> sendGameScreenMessage(listener, gameScreenMessage, attempts - 1));
            }
        };
        threadRunner(runnable, listener.toString());
    }

    // MESSAGE SENDING PLAYER

    protected void sendPlayerMessageRequiredResponse(Player target, JSONObject gameMessage, Integer attempts) {
        Runnable runnable = () -> {
            final Thread currentThread = Thread.currentThread();
            currentThread.setName(target.getName());
            if (attempts <= 0) {return;}
            try {
                logger.info("THREAD: " + Thread.currentThread().getName() + " TARGET: " + target.getName() + " SENT: " + gameMessage);

                target.getSession().get().getRemote().sendString(gameMessage.toString());
                addPlayerPendingMessage(target.getName(), gameMessage);
            } catch (Exception ex) {
                delayedRetryMessageSending(() -> sendPlayerMessageRequiredResponse(target, gameMessage, attempts - 1));
            }
        };
        this.threadRunner(runnable, target.getName());
    }

    public void sendPlayerMessage(Player target, JSONObject gameMessage, Integer attempts) {
        Runnable runnable = () -> {
            if (attempts <= 0) {return;}
            try {
                target.getSession().get().getRemote().sendString(gameMessage.toString());
            } catch (Exception ex) {
                delayedRetryMessageSending(() -> sendPlayerMessage(target, gameMessage, attempts - 1));
            }
        };
        this.threadRunner(runnable, target.getName());
    }

    private void addPlayerPendingMessage(String playerName, JSONObject gameMessage) {
        if (!gameMessage.equals(pendingAckMessages.get(playerName)) && pendingAckMessages.get(playerName) != null) {
            logger.error("MESSAGE LOST EARLIER");
        } else {
            this.pendingAckMessages.put(playerName, gameMessage);
            logger.info("ADD PENDING MESSAGE: " + playerName + " MESSAGE: " + gameMessage);
        }
    }

    private void delayedRetryMessageSending(Runnable sendMessage) {

        logger.info("THREAD: " + Thread.currentThread().getName() + " FAILED, trying again in 4s");
        try {
            TimeUnit.SECONDS.sleep(4);
            sendMessage.run();
        } catch (InterruptedException e) {
            logger.error("WAIT FAILED, MESSAGE LOST");
        }
    }

    private void threadRunner(Runnable runnable, String runnableId) {
        Optional<Thread> old = threads.stream().filter(t -> t.getName().equals(runnableId)).findFirst();
        logger.info("Old Thread present? " + old.isPresent());
        old.ifPresent(Thread::interrupt);

        Thread thread = new Thread(runnable);
        thread.setName(runnableId);

        logger.info("Starting thread with name : " + runnableId);
        thread.start();
    }

    // GETTERS + SETTERS

    public GamePlayerMessageActions getGamePlayerMessageActions() {
        return gamePlayerMessageActions;
    }

    public GameScreenMessageActions getGameScreenMessageActions() {
        return gameScreenMessageActions;
    }

    protected JSONObject getPlayerPendingMessages(String playerName) {
        return this.pendingAckMessages.get(playerName);
    }

    private void clearPendingMessage(String playerName) {
        this.pendingAckMessages.put(playerName, null);
    }

    public void setGamePlayerMessageActions(GamePlayerMessageActions gamePlayerMessageActions) {
        this.gamePlayerMessageActions = gamePlayerMessageActions;
    }

    public void setGameScreenMessageActions(GameScreenMessageActions gameScreenMessageActions) {
        this.gameScreenMessageActions = gameScreenMessageActions;
    }

    public Map<String, JSONObject> getPendingAckMessages() {
        return pendingAckMessages;
    }

}
