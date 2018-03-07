/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author pulli
 */
public class InvestigateLoyaltyState implements GameState {

    private static final String HEADER = "Investigation of loyalty!";
    private static final String SUB_HEADER = "Supreme Chancellor, who do you wish to investigate?";
    protected static final String INVESTIGATION_INFO = "Player %s is a %s!";

    private static final String EVENT_INVESTIGATION = "INVESTIGATION";
    private static final String EVENT_INVESTIGATION_HEADER = "I sense a disturbance in the force...";
    private static final String EVENT_INVESTIGATION_SUBHEADER = "The acting Supreme Chancellor has received knowledge of player %s's role!";

    private Game game;
    private Player supremeChancellor;
    protected Map<String, String> choices;
    protected String investigationInfo;
    protected Player investigate;

    public InvestigateLoyaltyState(Game game) {
        this.game = game;
        supremeChancellor = game.getVariables().getSupremeChancellor().get();
    }

    @Override
    public void doAction() {
        choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName()))
                .collect(Collectors.toMap(Player::getName, Player::getName));
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                Collections.singletonList(supremeChancellor), choices, HEADER, SUB_HEADER, State.INVESTIGATE_LOYALTY.toString());

    }

    @Override
    public void receiveData(String player, String data) {
        investigate = game.getPlayerManager().getPlayerByName(data).get();
        Role investigateRole = investigate.getRole();
        if (investigateRole.equals(Role.SHEEV_PALPATINE)) {
            investigateRole = Role.SEPARATIST;
        }
        investigationInfo = String.format(INVESTIGATION_INFO, investigate.getName(), investigateRole.toString());

        game.stateStatusUpdate(State.ROUND_START);
    }

    @Override
    public void sendEndMessages() {
        game.getGamePlayerMessageActions().alertPlayer(supremeChancellor, HEADER, investigationInfo);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_INVESTIGATION, EVENT_INVESTIGATION_HEADER, String.format(EVENT_INVESTIGATION_SUBHEADER, investigate.getName()));

    }
}
