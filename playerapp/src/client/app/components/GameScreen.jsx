import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import Footer from './Footer.jsx';
import OptionList from './OptionList.jsx';

const style = {
  container: {
    display: "flex",
    flexGrow: "inherit",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    flex: 1
  }
}

class GameScreen extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      data: ""
    };
  }

  render() {
    return (
      <div style={style.container}>
        {this.state.data}
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
              "type":"PING",
              "gameName": component.props.gameName
            }));
        }, 5000);
    };

    webSocket.onmessage = function (msg) {
        let data = JSON.parse(msg.data)
        console.log(data)
        if (data.type == "STATUS_UPDATE") {
            component.setState({data: data});
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
