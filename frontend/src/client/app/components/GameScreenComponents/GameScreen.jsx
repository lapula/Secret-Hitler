import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import GamePlayerColumn from './GamePlayerColumn.jsx';
import GamePolicies from './GamePolicies.jsx';
import GameGeneralInfo from './GameGeneralInfo.jsx';
import GameEvent from './GameEvent.jsx';

const CREATE_GAME = "CREATE_GAME";
const LISTEN_GAME = "LISTEN_GAME";
const STATUS_UPDATE = "STATUS_UPDATE";
const GAME_EVENT = "GAME_EVENT";
const PING = "PING";
const PING_INTERVAL = 10000;

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
    const type = component.props.createGame ? CREATE_GAME : LISTEN_GAME;

    webSocket.onopen = function() {
      var message =  {
        "type":type,
        "gameName": component.props.gameName,
        "gamePlayers": component.props.gamePlayers
      }
      webSocket.send(JSON.stringify(message));

      setInterval(function(){
        webSocket.send(JSON.stringify({
          "type": PING,
          "gameName": component.props.gameName
        }));
      }, PING_INTERVAL);
    };

    webSocket.onmessage = function (msg) {
      const message = JSON.parse(msg.data)
      console.log(message);

      if (message.type == STATUS_UPDATE) {
        component.setState({statusUpdateData: message.data});
      } else if (message.type == GAME_EVENT) {
        component.setState({gameEvent: message});
      }
    };

    webSocket.onclose = function () {
      var time = new Date();
      var closeTime = time.getHours() + ":" + time.getMinutes();
      console.log("WebSocket connection closed at: " + closeTime);
    };
  }

}

export default GameScreen;
