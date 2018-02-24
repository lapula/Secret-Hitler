/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        Player legistlator = null;
        String header = "";
        String subheader = "";

        //TODO fix map CHOICE
        Integer index = 1;
        for (Policy policy : policies) {
            policyIdMapper.put("CHOICE" + index.toString(), policy.toString());
            index++;
        };
        
        if (policies.size() == 3) {
            legistlator = game.getVariables().getSupremeChancellor();
            header = LEGISTLATE_HEADER_SUPREME_CHANCELLOR;
            subheader = LEGISTLATE_SUB_HEADER_SUPREME_CHANCELLOR;
        } else {
            legistlator = game.getVariables().getViceChair();
            header = LEGISTLATE_HEADER_VICE_CHAIR;
            subheader = LEGISTLATE_SUB_HEADER_VICE_CHAIR;
            
            if (game.getVariables().getSeparatistPolicyCount() == 5) {
                policyIdMapper.put("VETO", VETO_PROPOSAL);
            }
        }
        List<Player> target = new ArrayList<>();
        target.add(legistlator);
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target, policyIdMapper, header, subheader, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        
        Policy discard = null;

        //TODO fix frontend side of data
        if (policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString())) {
            discard = Policy.LOYALIST_POLICY;
        } else if (policyIdMapper.get(data).equals(Policy.SEPARATIST_POLICY.toString())) {
            discard = Policy.SEPARATIST_POLICY;
        } else {
            game.getVariables().setVetoedPolicies(policies);
            game.changeState(State.VETO);
            return;
        }
        policyIdMapper = new HashMap<>();
        
        for (int i = 0; i < policies.size(); i++) {
            if (policies.get(i).equals(discard)) {
                policies.remove(i);
                break;
            }
        }

        //TODO is this communicated to the player?
        if (policies.size() == 1) {
            if (policies.get(0).equals(Policy.LOYALIST_POLICY)) {
                game.getVariables().addLoyalistPolicy();
            } else {
                game.getVariables().addSeparatistPolicy();
            }
            game.changeState(State.DETERMINE_EXECUTIVE_ACTION);
        } else {
            doAction();
        }
    }
    
}
