/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameMessagingInterface.GameMessageService;
import GameMessagingInterface.GamePlayerMessageActions;
import GameMessagingInterface.GameScreenMessageActions;
import GameStates.GameState;
import GameStates.State;
import GameStates.StateFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Game {
    
    private StateFactory stateFactory;
    private PlayerManager playerManager;
    private GameState gameState;
    private GameMessageService gameMessageService;
    private State gameStateType;
    private GameVariables gameVariables;
    private PolicyDeck policyDeck;
    private List<Session> gameListeners;

    @NotNull
    private String gameName;
    private boolean gameStarted;
    private int lastSentMessageTargetCount;
    private int currentReceivedMessages;

    public Game(String gameName, int numberOfPlayers) {
        
        this.gameListeners = new LinkedList<>();
        this.stateFactory = new StateFactory();
        this.gameVariables = new GameVariables();
        this.policyDeck = new PolicyDeck();
        this.gameMessageService = new GameMessageService(this);
        this.gameStarted = false;
        this.lastSentMessageTargetCount = this.gameVariables.getGamePlayers();
        this.currentReceivedMessages = 0;
        this.gameVariables.setGamePlayers(numberOfPlayers);
        this.playerManager = new PlayerManager(this);
        this.gameName = gameName;
        this.gameState = this.stateFactory.getGameState(this, State.GAME_START);
        this.gameStateType = State.GAME_START;
        this.stateStatusUpdate(null);
    }
    
    public void stateStatusUpdate(State nextState) {
        this.gameStateType = nextState;
   }

   public void stateStatusAction() {
       if (gameStateType != null) {
           this.gameState.sendEndMessages();
           if (checkGameEndConditions()) {
               this.gameState = this.stateFactory.getGameState(this, State.GAME_END);
           } else {
               this.gameState =  this.stateFactory.getGameState(this, gameStateType);
           }
       }
       this.gameState.doAction();
       this.currentReceivedMessages = 0;
       this.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(this.gameListeners, this.toJSON());
       this.lastSentMessageTargetCount = this.gameState.sendData();
   }
    
    public void receiveData(String player, String data) {
        this.currentReceivedMessages++;
        this.gameState.receiveData(player, data);
        if (this.lastSentMessageTargetCount <= this.currentReceivedMessages) {
            stateStatusAction();
        }
        this.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(this.gameListeners, this.toJSON());
    }

    public boolean checkGameEndConditions() {
        if (!gameStarted) {
            return false;
        }
        if (this.gameVariables.getSeparatistPolicyCount() >= 6) {
            return true;
        }
        if (this.gameVariables.getViceChair().map(Player::getRole).orElse(Role.LOYALIST).equals(Role.SHEEV_PALPATINE)
                && this.gameVariables.getSeparatistPolicyCount() > 3
                && this.gameStateType.equals(State.LEGISTLATIVE_SESSION)) {
            return true;
        }
        if (this.gameVariables.getLoyalistPolicyCount() >= 5) {
            return true;
        }
        List<Player> palpatineInGame = this.playerManager.getPlayers().stream()
                .filter(player -> player.getRole().equals(Role.SHEEV_PALPATINE))
                .collect(Collectors.toList());

        return palpatineInGame.size() == 0;
    }
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public GameVariables getVariables() {
        return this.gameVariables;
    }
    
    public PolicyDeck getPolicyDeck() {
        return this.policyDeck;
    }
    
    public void addGameListener(Session session) {
        this.gameListeners.add(session);
    }

    public List<Session> getGameListeners() {
        return this.gameListeners;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted() {
        this.gameStarted = true;
    }

    public Optional<State> getGameStateType() {
        return Optional.ofNullable(gameStateType);
    }

    public GamePlayerMessageActions getGamePlayerMessageActions() {
        return this.gameMessageService.getGamePlayerMessageActions();
    }

    public GameScreenMessageActions getGameScreenMessageActions() {
        return this.gameMessageService.getGameScreenMessageActions();
    }

    public GameMessageService getGameMessageService() {
        return gameMessageService;
    }

    public String getGameName() {
        return gameName;
    }

    public JSONObject toJSON() {

        JSONObject electionResults = new JSONObject(gameVariables.getElectionResults());
        JSONObject json = new JSONObject();
        json.put("supremeChancellor", gameVariables.getSupremeChancellor().map(Player::getName).orElse(""));
        json.put("viceChair", gameVariables.getViceChair().map(Player::getName).orElse(""));
        json.put("gamePlayers", gameVariables.getGamePlayers());
        json.put("governmentVotesThisRound", gameVariables.getSenateVotesThisRound());
        json.put("loyalistPoliciesPassed", gameVariables.getLoyalistPolicyCount());
        json.put("separatistPoliciesPassed", gameVariables.getSeparatistPolicyCount());
        json.put("players", playerManager.getPlayerNames());
        json.put("cardsInDeck", this.getPolicyDeck().getDeck().size());
        json.put("state", this.getGameStateType().map(State::toString).orElse(""));

        if (this.getPlayerManager().getPlayers().size() != this.getVariables().getElectionResults().size()) {
            json.put("electionResults", "");
        } else {
            json.put("electionResults", electionResults);
        }
        
        return json;
    }
}
