/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Policy;

import java.util.*;
import java.util.stream.IntStream;

/**
 *
 * @author pulli
 */

public class VetoState implements GameState {

    private static final String VETO_PROPOSED_HEADER = "Veto proposed to the Supreme Chancellor";
    private static final String VETO_PROPOSED_SUB_HEADER = "Supreme Chancellor, do you accept the veto?";
    private static final String VETO_DENIED_HEADER = "Veto denied, Vice Chair will enact a policy";
    private static final String VETO_DENIED_SUB_HEADER = "Vice Chair, discard one policy";
    protected static final String YES = "Yes";
    protected static final String NO = "No";
    private static final String POLICY = "POLICY";

    private static final String EVENT_VETO = "VETO";
    private static final String EVENT_VETO_HEADER = "Veto power used!";
    private static final String EVENT_VETO_SUBHEADER = "The Government has blocked a policy from being put into action!";

    private static final String EVENT_NO_VETO = "NO_VETO";
    private static final String EVENT_NO_VETO_HEADER = "Veto power left unused!";
    private static final String EVENT_NO_VETO_SUBHEADER = "Surely the Vice Chair will now pass a loyalist policy.";


    private static final String EVENT_LEGISTLATION_SEPARATISTS = "LEGISTLATION_SEPARATISTS";
    private static final String EVENT_LEGISTLATION_HEADER_SEPARATISTS = "Separatist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS = "This is a dark day for The Republic.";

    private static final String EVENT_LEGISTLATION_LOYALISTS = "LEGISTLATION_LOYALISTS";
    private static final String EVENT_LEGISTLATION_HEADER_LOYALISTS = "Loyalist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_LOYALISTS = "There is still hope for democracy.";

    private Game game;
    private boolean proposingVeto;
    protected List<Policy> vetoedPolicies;
    protected Map<String, String> policyIdMapper;
    private String header;
    private String subheader;
    private List<Player> target;
    protected Map<String, String> choices;
    private boolean vetoUnused;
    protected Player legistlator;


    public VetoState(Game game) {
        this.game = game;
        this.proposingVeto = true;
        this.vetoedPolicies = game.getVariables().getVetoedPolicies();
        this.policyIdMapper = new HashMap<>();
        vetoUnused = false;
    }



    @Override
    public void doAction() {
        if (proposingVeto) {
            legistlator = game.getVariables().getSupremeChancellor().get();
            header = VETO_PROPOSED_HEADER;
            subheader = VETO_PROPOSED_SUB_HEADER;
            choices = new HashMap<>();
            choices.put(YES, YES);
            choices.put(NO, NO);
        } else {
            legistlator = game.getVariables().getViceChair().get();
            header = VETO_DENIED_HEADER;
            subheader = VETO_DENIED_SUB_HEADER;

            IntStream.range(0, vetoedPolicies.size()).forEach(index -> policyIdMapper.put(POLICY + index, vetoedPolicies.get(index).toString()));
            choices = new HashMap<>(policyIdMapper);
        }
        target = Collections.singletonList(legistlator);
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target,
                choices, header, subheader, State.VETO.toString());

    }

    @Override
    public void receiveData(String player, String data) {
        if (proposingVeto) {
            if (data.equals(NO)) {
                proposingVeto = false;
                vetoUnused = true;
                game.stateStatusUpdate(null);
            } else {
                //TODO should not reset government votes this round
                game.stateStatusUpdate(State.ROUND_START);
            }
        } else {
            Policy discard = policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString()) ? Policy.LOYALIST_POLICY : Policy.SEPARATIST_POLICY;
            policyIdMapper.remove(data);
            vetoedPolicies.remove(discard);

            if (vetoedPolicies.get(0).equals(Policy.LOYALIST_POLICY)) {
                game.getVariables().addLoyalistPolicy();
            } else {
                game.getVariables().addSeparatistPolicy();
            }
            game.stateStatusUpdate(State.DETERMINE_EXECUTIVE_ACTION);
        }
    }

    @Override
    public void sendEndMessages() {
        if (proposingVeto) {
            game.getGameScreenMessageActions().sendGameEvent(
                    game.getGameListeners(), EVENT_VETO, EVENT_VETO_HEADER, EVENT_VETO_SUBHEADER);
        } else if (vetoUnused) {
            game.getGameScreenMessageActions().sendGameEvent(
                    game.getGameListeners(), EVENT_NO_VETO, EVENT_NO_VETO_HEADER, EVENT_NO_VETO_SUBHEADER);
        } else {
            if (vetoedPolicies.get(0).equals(Policy.LOYALIST_POLICY)) {
                game.getGameScreenMessageActions().sendGameEvent(game.getGameListeners(), EVENT_LEGISTLATION_LOYALISTS,
                        EVENT_LEGISTLATION_HEADER_LOYALISTS, EVENT_LEGISTLATION_SUBHEADER_LOYALISTS);
            } else {
                game.getGameScreenMessageActions().sendGameEvent(game.getGameListeners(), EVENT_LEGISTLATION_SEPARATISTS,
                        EVENT_LEGISTLATION_HEADER_SEPARATISTS, EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS);
            }
        }
    }
}
