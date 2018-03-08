/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;

/**
 *
 * @author pulli
 */
public class DetermineExecutiveActionState implements GameState {

    private static final String EVENT_SPECIAL = "EVENT_SPECIAL";
    private static final String EVENT_SPECIAL_HEADER = "Emergency powers granted!";
    private static final String EVENT_SPECIAL_SUBHEADER = "The Supreme Chancellor will nominate the next Supreme Chancellor.";


    private Game game;
    private int gamePlayers;
    private int separatistPolicies;
    private boolean hasPower;

    public DetermineExecutiveActionState(Game game) {
        this.game = game;
        gamePlayers = game.getVariables().getGamePlayers();
        separatistPolicies = game.getVariables().getSeparatistPolicyCount();
        hasPower = game.getVariables().hasExecutivePower();
    }

    @Override
    public void doAction() {
        if (separatistPolicies == 0 || !hasPower) {
            game.stateStatusUpdate(State.ROUND_START);
        } else if (separatistPolicies >= 4) {
            game.stateStatusUpdate(State.EXECUTION);
        } else if (separatistPolicies == 3) {
            if (gamePlayers > 6) { //>6
                game.stateStatusUpdate(State.CALL_SPECIAL_ELECTION);
            } else {
                game.stateStatusUpdate(State.POLICY_PEEK);
            }
        } else if (separatistPolicies == 2) {
            if (gamePlayers > 6) { //>6
                game.stateStatusUpdate(State.INVESTIGATE_LOYALTY);
            } else {
                game.stateStatusUpdate(State.ROUND_START);
            }
        } else if (separatistPolicies == 1) {
            if (gamePlayers > 8) {//>8
                game.stateStatusUpdate(State.INVESTIGATE_LOYALTY);
            } else {
                game.stateStatusUpdate(State.ROUND_START);
            }
        } else {
            game.stateStatusUpdate(State.ROUND_START);
        }
    }

    @Override
    public int sendData() {
        // a bit hacky
        game.stateStatusAction();
        return 0;
    }

    @Override
    public void receiveData(String player, String data) {
        // do nothing
    }


    @Override
    public void sendEndMessages() {
        if (separatistPolicies == 3) {
            if (gamePlayers > 6) { //>6
                game.getGameScreenMessageActions().sendGameEvent(
                        game.getGameListeners(), EVENT_SPECIAL, EVENT_SPECIAL_HEADER, EVENT_SPECIAL_SUBHEADER);

            }
        }
    }
}
