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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author pulli
 */
public class LegistlativeSessionState implements GameState {

    private static final String LEGISTLATE_HEADER_SUPREME_CHANCELLOR = "Legistlative session: Supreme Chancellor";
    private static final String LEGISTLATE_SUB_HEADER_SUPREME_CHANCELLOR = "Supreme Chancellor, discard one policy";
    private static final String LEGISTLATE_HEADER_VICE_CHAIR = "Legistlative session: Vice Chair";
    private static final String LEGISTLATE_SUB_HEADER_VICE_CHAIR = "Vice Chair, discard one policy";
    private static final String VETO_PROPOSAL = "Veto proposal";
    private static final String POLICY = "POLICY";

    private static final String EVENT_LEGISTLATION_PROCESS = "LEGISTLATION_PROCESS";
    private static final String EVENT_LEGISTLATION_PROCESS_HEADER = "Silence!";
    private static final String EVENT_LEGISTLATION_PROCESS_SUBHEADER = "The government is in session.";

    private static final String EVENT_LEGISTLATION_SEPARATISTS = "LEGISTLATION_SEPARATISTS";
    private static final String EVENT_LEGISTLATION_HEADER_SEPARATISTS = "Separatist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS = "This is a dark day for The Republic.";

    private static final String EVENT_LEGISTLATION_LOYALISTS = "LEGISTLATION_LOYALISTS";
    private static final String EVENT_LEGISTLATION_HEADER_LOYALISTS = "Loyalist policy enacted!";
    private static final String EVENT_LEGISTLATION_SUBHEADER_LOYALISTS = "There is still hope for democracy.";

    private Game game;
    private List<Policy> policies;
    private Map<String, String> policyIdMapper;
    
    public LegistlativeSessionState(Game game) {
        this.game = game;
        this.policies = game.getPolicyDeck().drawNextThree();
        this.policyIdMapper = new HashMap<>();
    }

    @Override
    public void doAction() {

        Player legistlator;
        String header;
        String subheader;

        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_LEGISTLATION_PROCESS, EVENT_LEGISTLATION_PROCESS_HEADER, EVENT_LEGISTLATION_PROCESS_SUBHEADER);

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
                policyIdMapper.put("VETO", VETO_PROPOSAL);
            }
        }
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), Arrays.asList(legistlator), policyIdMapper, header, subheader, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {

        if (policyIdMapper.get(data).equals(VETO_PROPOSAL)) {
            System.out.println("VETO STARTED");
            game.getVariables().setVetoedPolicies(policies);
            game.changeState(State.VETO);
        } else  {
            Policy discard = policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString()) ? Policy.LOYALIST_POLICY : Policy.SEPARATIST_POLICY;
            policyIdMapper.remove(data);
            policies.remove(discard);

            if (policies.size() == 1) {
                if (policies.get(0).equals(Policy.LOYALIST_POLICY)) {
                    game.getVariables().addLoyalistPolicy();
                    game.getGameScreenMessageActions().sendGameEvent(
                            game.getGameListeners(), EVENT_LEGISTLATION_LOYALISTS, EVENT_LEGISTLATION_HEADER_LOYALISTS, EVENT_LEGISTLATION_SUBHEADER_LOYALISTS);
                } else {
                    game.getVariables().addSeparatistPolicy();
                    game.getGameScreenMessageActions().sendGameEvent(
                            game.getGameListeners(), EVENT_LEGISTLATION_SEPARATISTS, EVENT_LEGISTLATION_HEADER_SEPARATISTS, EVENT_LEGISTLATION_SUBHEADER_SEPARATISTS);
                }
                game.changeState(State.DETERMINE_EXECUTIVE_ACTION);
            } else {
                doAction();
            }
        }
    }
    
}
