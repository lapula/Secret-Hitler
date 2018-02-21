import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import styles from './general-style.css';
import Footer from './Footer.jsx';
import OptionList from './OptionList.jsx';

class GameScreen extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      data: ""
    };
  }

  render() {
    let state = JSON.stringify(this.state.data, null, 4);
    return (
      <div className={styles.container}>
        <span style={{backgroundColor: "white"}}>{state}</span>
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
