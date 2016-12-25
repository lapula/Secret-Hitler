import React, {Component} from 'react';
import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

import Footer from './Footer.jsx';
import OptionList from './OptionList.jsx';

const style = {
  button: {
    margin: "12px"
  }
}

class Main extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      phase: "Game is starting...",
      playerRole: "Not assigned yet.",
      queryData: null,
      playerName: null,
      gameName: null
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this, event);
  }

  handleSubmit() {
    this.setState({
      playerName: this.refs.playerName.input.value,
      gameName: this.refs.gameName.input.value
    });
    this.initSocketConnection(this, this.refs.playerName.input.value, this.refs.gameName.input.value);
  }

  handleKeyPress(component, event) {
    if(event.key == 'Enter'){
      this.handleSubmit();
    }
  }

  render() {

    if (this.state.playerName == null) {
      return (
        <div id="container">
          <h1>Join game</h1>
          <TextField
              floatingLabelText="Player name"
              ref="playerName"
            /><br />
          <TextField
                floatingLabelText="Game name"
                ref="gameName"
                onKeyPress={this.handleKeyPress}
          /><br />
        <RaisedButton label="Enter game!" primary={true} style={style.button} onTouchTap={this.handleSubmit} />
        </div>
      )
    }

    return (
      <div id="container">
          <h1>{this.state.phase}</h1>
          <OptionList queryData={this.state.queryData} webSocket={this.state.webSocket} />
          <Footer role={this.state.playerRole}/>
      </div>
    );
  }

  initSocketConnection(elem, playerName, gameName) {
    var component = elem;
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/players");
    component.setState({webSocket: webSocket});

    webSocket.onopen = function() {
        var message =  {
          "type":"REGISTER_PLAYER",
          "content":{
              "playerName": playerName,
              "gameName": gameName
            }
        }
        webSocket.send(JSON.stringify(message));

        setInterval(function(){
            webSocket.send(JSON.stringify({"type":"REGISTER_PLAYER"}));
        }, 10000);
    };

    webSocket.onmessage = function (msg) {
        let data = JSON.parse(msg.data)

        if (data.type == "PLAYER_INIT") {
            component.setState({playerRole: data.role});
        } else if (data.type == "PLAYER_QUERY") {
            component.setState({
              queryData: data,
              phase: data.header
            });
        }
    };

    webSocket.onclose = function () {
      var time = new Date();
      var closeTime = time.getHours() + ":" + time.getMinutes();
      alert("WebSocket connection closed at: " + closeTime);
    };
  }

}

export default Main;
