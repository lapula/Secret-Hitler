import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import styles from './gamescreen-style.css';
import GamePlayerColumn from './GamePlayerColumn.jsx';
import GamePolicies from './GamePolicies.jsx';
import GameRoleInfo from './GameRoleInfo.jsx';

const testStateNormal = {
  "data": {
        "supremeChancellor": "Aapeli",
        "cardsInDeck": 17,
        "governmentVotesThisRound": 0,
        "electionResults": {},
        "players": [
            "Aapeli",
            "Bert",
            "Cecilia",
            "David"
        ],
        "loyalistPoliciesPassed": 0,
        "separatistPoliciesPassed": 0,
        "viceChair": "",
        "state": "GAME_START",
        "gamePlayers": 5
    },
}

const testStateElectionResults = {
  "data": {
        "supremeChancellor": "Bert",
        "cardsInDeck": 11,
        "governmentVotesThisRound": 2,
        "electionResults": {
            "Aapeli": "YES",
            "Bert": "NO",
            "Cecilia": "YES",
            "David": "YES",
            "Eemeli": "NO"
        },
        "players": [
          "Aapeli",
          "Bert",
          "Cecilia",
          "David",
          "Eemeli"
        ],
        "loyalistPoliciesPassed": 1,
        "separatistPoliciesPassed": 0,
        "viceChair": "Aapeli",
        "state": "LEGISTLATIVE_SESSION",
        "gamePlayers": 5,
    },
    "type": "STATUS_UPDATE"
}

class GameScreen extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      data: testStateElectionResults.data
    };
  }

  render() {
    return (
      <div className={styles.container}>
        <GamePlayerColumn
          players={this.state.data.players}
          electionResults={this.state.data.electionResults}
        />
      <div className={styles.gameScreenRightWrapper}>
          <GamePolicies
            loyalistPoliciesPassed={this.state.data.loyalistPoliciesPassed}
            separatistPoliciesPassed={this.state.data.separatistPoliciesPassed}
          />
          <GameRoleInfo
            supremeChancellor={this.state.data.supremeChancellor}
            viceChair={this.state.data.viceChair}
          />
        </div>
      </div>
    );
  }

  componentWillMount() {
    var component = this;
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/games");

    let type;
    if (component.props.createGame) {
      type = "CREATE_GAME";
    } else {
      type = "LISTEN_GAME"
    }

    webSocket.onopen = function() {
        var message =  {
          "type":type,
          "gameName": component.props.gameName,
          "gamePlayers": component.props.gamePlayers
        }
        webSocket.send(JSON.stringify(message));

        setInterval(function(){
            webSocket.send(JSON.stringify({
              "type":"POLL",
              "gameName": component.props.gameName
            }));
        }, 5000);
    };

    webSocket.onmessage = function (msg) {
        let data = JSON.parse(msg.data)
        console.log(JSON.stringify(data, null, 4))
        if (data.type == "STATUS_UPDATE") {
            component.setState({data: JSON.stringify(data)});
        }
    };

    webSocket.onclose = function () {
      var time = new Date();
      var closeTime = time.getHours() + ":" + time.getMinutes();
      alert("WebSocket connection closed at: " + closeTime);
    };
  }

}

export default GameScreen;
