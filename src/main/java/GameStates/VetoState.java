/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Policy;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class VetoState implements GameState {
    
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
            legistlator = game.getVariables().getPresident();
            header = "Veto proposed to president";
            subheader = "President, do you accept the veto?";
            choices.put("YES", "Yes");
            choices.put("NO", "No");
        } else {
            legistlator = game.getVariables().getChancellor();
            header = "Veto denied, chancellor will enact a policy";
            subheader = "Chancellor, discard one policy";
            
            Integer index = 0;
            for (Policy policy : vetoedPolicies) {
                choices.put("CHOICE" + index.toString(), policy.toString());
                index++;
            };
            
            policyIdMapper = choices;
        }
        
        List<Player> target = new ArrayList<>();
        target.add(legistlator);
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), target, choices, header, subheader);
    }

    // use legistlative sessions receiveData instead?
    
    @Override
    public void receiveData(String player, String data) {
        
        if (proposingVeto) {
            if (data.equals("NO")) {
                proposingVeto = false;
                doAction();
            } else {
                game.changeState(State.NOMINATE_CHANCELLOR);
            }
        } else {
            
            Policy discard = null;
        
            if (policyIdMapper.get(data).equals("Liberal")) {
                discard = Policy.LIBERAL;
            } else if (policyIdMapper.get(data).equals("Fascist")) {
                discard = Policy.FASCIST;
            }
            
            for (int i = 0; i < vetoedPolicies.size(); i++) {
                if (vetoedPolicies.get(i).equals(discard)) {
                    vetoedPolicies.remove(i);
                    break;
                }
            }
            
            if (vetoedPolicies.size() == 1) {
                if (vetoedPolicies.get(0).equals(Policy.LIBERAL)) {
                    game.getVariables().addLiberalPolicy();
                } else {
                    game.getVariables().addFascistPolicy();
                }
                game.changeState(State.ROUND_START);
            }
        }
        
    }
    
    
    
}
