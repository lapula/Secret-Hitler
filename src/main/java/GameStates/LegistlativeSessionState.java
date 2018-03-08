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
public class LegistlativeSessionState implements GameState {

    private static final String LEGISTLATE_HEADER_SUPREME_CHANCELLOR = "Legistlative session: Supreme Chancellor";
    private static final String LEGISTLATE_SUB_HEADER_SUPREME_CHANCELLOR = "Supreme Chancellor, please DISCARD one policy.";
    private static final String LEGISTLATE_HEADER_VICE_CHAIR = "Legistlative session: Vice Chair";
    private static final String LEGISTLATE_SUB_HEADER_VICE_CHAIR = "Vice Chair, please DISCARD one policy.";
    protected static final String VETO = "VETO";
    protected static final String VETO_PROPOSAL = "Veto proposal";
    private static final String POLICY = "POLICY";

    private static final String EVENT_LEGISTLATION_SEPARATISTS = "LEGISTLATION_SEPARATISTS";
    private static final String EVENT_LEGISTLATION_HEADER_SEPARATISTS = "Separatist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS = "This is a dark day for The Galactic Republic.";

    private static final String EVENT_LEGISTLATION_LOYALISTS = "LEGISTLATION_LOYALISTS";
    private static final String EVENT_LEGISTLATION_HEADER_LOYALISTS = "Loyalist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_LOYALISTS = "There is still hope for democracy.";

    private Game game;
    protected List<Policy> policies;
    protected Map<String, String> policyIdMapper;
    protected Player legistlator;
    private String header;
    private String subheader;
    private boolean proposedVeto;

    public LegistlativeSessionState(Game game) {
        this.game = game;
        this.policies = game.getPolicyDeck().drawNextThree();
        this.policyIdMapper = new HashMap<>();
        this.proposedVeto = false;
    }

    @Override
    public void doAction() {
        if (policies.size() == 3) {
            IntStream.range(0, policies.size()).forEach(index -> policyIdMapper.put(POLICY + index, policies.get(index).toString()));
            legistlator = game.getVariables().getSupremeChancellor().get();
            header = LEGISTLATE_HEADER_SUPREME_CHANCELLOR;
            subheader = LEGISTLATE_SUB_HEADER_SUPREME_CHANCELLOR;
        } else {
            legistlator = game.getVariables().getViceChair().get();
            header = LEGISTLATE_HEADER_VICE_CHAIR;
            subheader = LEGISTLATE_SUB_HEADER_VICE_CHAIR;

            if (game.getVariables().getSeparatistPolicyCount() == 5) {
                policyIdMapper.put(VETO, VETO_PROPOSAL);
            }
        }
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                Collections.singletonList(legistlator), policyIdMapper, header, subheader, State.LEGISTLATIVE_SESSION.toString());
    }

    @Override
    public void receiveData(String player, String data) {

        if (policyIdMapper.get(data).equals(VETO_PROPOSAL)) {
            game.getVariables().setVetoedPolicies(policies);
            proposedVeto = true;
            game.stateStatusUpdate(State.VETO);
        } else  {
            Policy discard = policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString()) ? Policy.LOYALIST_POLICY : Policy.SEPARATIST_POLICY;
            policyIdMapper.remove(data);
            policies.remove(discard);

            if (policies.size() == 1) {
                if (policies.get(0).equals(Policy.LOYALIST_POLICY)) {
                    game.getVariables().addLoyalistPolicy();
                } else {
                    game.getVariables().addSeparatistPolicy();
                }
                game.stateStatusUpdate(State.DETERMINE_EXECUTIVE_ACTION);
            } else {
                game.stateStatusUpdate(null);
            }
        }
    }



    @Override
    public void sendEndMessages() {
        if (!proposedVeto) {
            if (policies.get(0).equals(Policy.LOYALIST_POLICY)) {
                game.getGameScreenMessageActions().sendGameEvent(
                        game.getGameListeners(), EVENT_LEGISTLATION_LOYALISTS, EVENT_LEGISTLATION_HEADER_LOYALISTS, EVENT_LEGISTLATION_SUBHEADER_LOYALISTS);
            } else {
                game.getGameScreenMessageActions().sendGameEvent(
                        game.getGameListeners(), EVENT_LEGISTLATION_SEPARATISTS, EVENT_LEGISTLATION_HEADER_SEPARATISTS, EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS);
            }
        }
    }

}
