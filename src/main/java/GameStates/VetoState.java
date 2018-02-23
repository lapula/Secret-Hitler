/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Policy;
import SocketInterface.PlayerWebSocketActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class VetoState implements GameState {

    private static final String VETO_PROPOSED_HEADER = "Veto proposed to the Supreme Chancellor";
    private static final String VETO_PROPOSED_SUB_HEADER = "Supreme Chancellor, do you accept the veto?";
    private static final String VETO_DENIED_HEADER = "Veto denied, Vice Chair will enact a policy";
    private static final String VETO_DENIED_SUB_HEADER = "Vice Chair, discard one policy";
    private static final String YES = "Yes";
    private static final String NO = "No";

    private Game game;
    private boolean proposingVeto;
    private List<Policy> vetoedPolicies;
    private Map<String, String> policyIdMapper;
    
    public VetoState(Game game) {
        this.game = game;
        this.proposingVeto = true;
        this.vetoedPolicies = game.getVariables().getVetoedPolicies();
        this.policyIdMapper = new HashMap<>();
    }
    
    @Override
    public void doAction() {
        
        Player legistlator = null;
        String header = "";
        String subheader = "";
        Map<String, String> choices = new HashMap<>();
        
        if (proposingVeto) {
            legistlator = game.getVariables().getSupremeChancellor();
            header = VETO_PROPOSED_HEADER;
            subheader = VETO_PROPOSED_SUB_HEADER;
            choices.put("YES", YES);
            choices.put("NO", NO);
        } else {
            legistlator = game.getVariables().getViceChair();
            header = VETO_DENIED_HEADER;
            subheader = VETO_DENIED_SUB_HEADER;

            //TODO fix CHOICE map
            Integer index = 0;
            for (Policy policy : vetoedPolicies) {
                choices.put("CHOICE" + index.toString(), policy.toString());
                index++;
            };
            
            policyIdMapper = choices;
        }
        
        List<Player> target = new ArrayList<>();
        target.add(legistlator);
        PlayerWebSocketActions.sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target, choices, header, subheader, game.getGameStateType().toString());
    }

    // use legistlative sessions receiveData instead?
    
    @Override
    public void receiveData(String player, String data) {
        
        if (proposingVeto) {
            if (data.equals(NO)) {
                proposingVeto = false;
                doAction();
            } else {
                game.changeState(State.NOMINATE_VICE_CHAIR);
            }
        } else {
            
            Policy discard = null;
        
            if (policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString())) {
                discard = Policy.LOYALIST_POLICY;
            } else if (policyIdMapper.get(data).equals(Policy.SEPARATIST_POLICY.toString())) {
                discard = Policy.SEPARATIST_POLICY;
            }
            
            for (int i = 0; i < vetoedPolicies.size(); i++) {
                if (vetoedPolicies.get(i).equals(discard)) {
                    vetoedPolicies.remove(i);
                    break;
                }
            }
            
            if (vetoedPolicies.size() == 1) {
                if (vetoedPolicies.get(0).equals(Policy.LOYALIST_POLICY)) {
                    game.getVariables().addLoyalistPolicy();
                } else {
                    game.getVariables().addSeparatistPolicy();
                }
                game.changeState(State.DETERMINE_EXECUTIVE_ACTION);
            }
        }
        
    }
    
    
    
}
