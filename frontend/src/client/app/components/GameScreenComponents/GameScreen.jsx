import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import GamePlayerColumn from './GamePlayerColumn.jsx';
import GamePolicies from './GamePolicies.jsx';
import GameGeneralInfo from './GameGeneralInfo.jsx';
import GameEvent from './GameEvent.jsx';

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
        "loyalistPoliciesPassed": 2,
        "separatistPoliciesPassed": 3,
        "viceChair": "Aapeli",
        "state": "LEGISTLATIVE_SESSION",
        "gamePlayers": 5,
    },
    "type": "STATUS_UPDATE"
}

const testGameEvent = {
  eventType: "VOTE_FAILED",
  header: "Vote failed!",
  subheader: "The representatives were not able to form a government.",
  type: "GAME_EVENT"
}

/*
this.state = {
  statusUpdateData: testStateElectionResults.data,
  gameEvent: testGameEvent
};*/

class GameScreen extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      statusUpdateData: "",
      gameEvent: ""
    };
  }

  render() {
    if (!this.state.statusUpdateData) {return null}
    return (
      <div className={styles.container}>
        <GamePlayerColumn
          players={this.state.statusUpdateData.players}
          electionResults={this.state.statusUpdateData.electionResults}
          supremeChancellor={this.state.statusUpdateData.supremeChancellor}
          viceChair={this.state.statusUpdateData.viceChair}
        />
        <div className={styles.gameScreenRightWrapper}>
          <div className={styles.gameInfoWrapper}>
            <GameEvent
              gameEvent={this.state.gameEvent}
            />
            <GameGeneralInfo
              cardsInDeck={this.state.statusUpdateData.cardsInDeck}
              governmentVotesThisRound={this.state.statusUpdateData.governmentVotesThisRound}
              state={this.state.statusUpdateData.state}
            />
          </div>
          <GamePolicies
            loyalistPoliciesPassed={this.state.statusUpdateData.loyalistPoliciesPassed}
            separatistPoliciesPassed={this.state.statusUpdateData.separatistPoliciesPassed}
          />
        </div>
      </div>
    );
  }

  componentWillMount() {
    var component = this;
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/games");
    const type = component.props.createGame ? "CREATE_GAME" : "LISTEN_GAME";

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
      let message = JSON.parse(msg.data)
      console.log(message);
      if (message.type == "STATUS_UPDATE") {
        component.setState({statusUpdateData: message.data});
      } else if (message.type == "GAME_EVENT") {
        component.setState({gameEvent: message});
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
