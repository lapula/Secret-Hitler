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
public class LegistlativeSessionState implements GameState {
    
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
        
        Integer index = 1;
        for (Policy policy : policies) {
            policyIdMapper.put("CHOICE" + index.toString(), policy.toString());
            index++;
        };
        
        if (policies.size() == 3) {
            legistlator = game.getVariables().getPresident();
            header = "Legistlative session: president";
            subheader = "President, discard one policy";
        } else {
            legistlator = game.getVariables().getChancellor();
            header = "Legistlative session: chancellor";
            subheader = "Chancellor, discard one policy";
            
            if (game.getVariables().getFascistPolicyCount() == 5) {
                policyIdMapper.put("VETO", "Veto proposal");
            }
        }
        List<Player> target = new ArrayList<>();
        target.add(legistlator);
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), target, policyIdMapper, header, subheader);
    }

    @Override
    public void receiveData(String player, String data) {
        
        Policy discard = null;
        
        if (policyIdMapper.get(data).equals("Liberal")) {
            discard = Policy.LIBERAL;
        } else if (policyIdMapper.get(data).equals("Fascist")) {
            discard = Policy.FASCIST;
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
        
        if (policies.size() == 1) {
            if (policies.get(0).equals(Policy.LIBERAL)) {
                game.getVariables().addLiberalPolicy();
            } else {
                game.getVariables().addFascistPolicy();
            }
            game.changeState(State.ROUND_START);
        } else {
            doAction();
        }
    }
    
}
