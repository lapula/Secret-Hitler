/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameMessagingInterface.GameMessageService;
import GameMessagingInterface.GamePlayerMessageActions;
import GameMessagingInterface.GameScreenMessageActions;
import GameMessagingInterface.GameScreenWebSocketInterface;
import GameStates.State;
import GameStates.GameState;
import GameStates.StateFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


public class Game {
    
    private StateFactory stateFactory;
    private PlayerManager playerManager;
    private GameState gameState;
    private GameMessageService gameMessageService;
    private State gameStateType;
    private GameVariables gameVariables;
    private PolicyDeck policyDeck;
    private List<Session> gameListeners;
    private boolean gameStarted;

    public Game(Integer numberOfPlayers) {
        
        this.gameListeners = new LinkedList<>();
        this.stateFactory = new StateFactory();
        this.gameVariables = new GameVariables();
        this.policyDeck = new PolicyDeck();
        this.gameMessageService = new GameMessageService(this);
        this.gameStarted = false;
        this.gameVariables.setGamePlayers(numberOfPlayers);
        this.playerManager = new PlayerManager(this);
        this.changeState(State.GAME_START);
    }
    
    public void changeState(State state) {
        this.gameStateType = state;
        if (checkGameEndConditions()) {
            this.gameState = this.stateFactory.getGameState(this, State.GAME_END);
        } else {
            this.gameState =  this.stateFactory.getGameState(this, state);
        }
        this.gameState.doAction();
        this.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(this.gameListeners, this.toJSON());

   }
    
    public void receiveData(String player, String data) {
        this.gameState.receiveData(player, data);
        this.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(this.gameListeners, this.toJSON());
    }

    private boolean checkGameEndConditions() {
        if (!gameStarted) {
            return false;
        }
        if (this.gameVariables.getSeparatistPolicyCount() >= 6) {
            return true;
        }
        if (this.gameVariables.getViceChair().map(Player::getRole).orElse(Role.LOYALIST).equals(Role.SHEEV_PALPATINE)
                && this.gameVariables.getSeparatistPolicyCount() >= 4
                && this.gameState.equals(State.LEGISTLATIVE_SESSION)) {
            return true;
        }
        if (this.gameVariables.getLoyalistPolicyCount() >= 5) {
            return true;
        }
        List<Player> palpatineInGame = this.playerManager.getPlayers().stream()
                .filter(player -> player.getRole().equals(Role.SHEEV_PALPATINE))
                .collect(Collectors.toList());
        if (palpatineInGame.size() == 0) {
            return true;
        }
        return false;
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

    public State getGameStateType() {
        return gameStateType;
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

    public JSONObject toJSON() {

        System.out.println(gameVariables.getSupremeChancellor());

        JSONObject electionResults = new JSONObject(gameVariables.getElectionResults());
        JSONObject json = new JSONObject();
        json.put("supremeChancellor", gameVariables.getSupremeChancellor().map(Player::getName).orElse(""));
        json.put("viceChair", gameVariables.getViceChair().map(Player::getName).orElse(""));
        json.put("gamePlayers", gameVariables.getGamePlayers());
        json.put("governmentVotesThisRound", gameVariables.getSenateVotesThisRound());
        json.put("loyalistPoliciesPassed", gameVariables.getLoyalistPolicyCount());
        json.put("separatistPoliciesPassed", gameVariables.getSeparatistPolicyCount());
        json.put("players", playerManager.getPlayerNames());
        json.put("cardsInDeck", this.getPolicyDeck().getDeckCardsCount());
        json.put("state", this.getGameStateType().toString());

        if (this.getPlayerManager().getPlayers().size() != this.getVariables().getElectionResults().size()) {
            json.put("electionResults", "");
        } else {
            json.put("electionResults", electionResults);
        }
        
        return json;
    }
}
