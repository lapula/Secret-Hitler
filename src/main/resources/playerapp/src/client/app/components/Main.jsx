import React, {Component} from 'react';
import $ from 'jquery';
import AppBar from 'material-ui/AppBar';

import Footer from './Footer.jsx';
import OptionList from './OptionList.jsx';

class Main extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      phase: "Game is starting...",
      playerRole: "Not assigned yet.",
      queryData: null
    };
  }

  componentDidMount() {
      this.initSocketConnection(this, "laur", "jammailu");
  }

  render() {
    return (
      <div id="container">

          <h1>{this.state.phase}</h1>
          <OptionList queryData={this.state.queryData} />
          <Footer role={this.state.playerRole}/>
      </div>
    );
  }


  initSocketConnection(elem, playerName, gameName) {
    var component = elem;
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/players");

    webSocket.onopen = function() {
        var message =  {
          "type":"REGISTER_PLAYER",
          "content":{
              "playerName": playerName,
              "gameName": gameName
            }
        }
        webSocket.send(JSON.stringify(message));
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
        alert("WebSocket connection closed")
    };
  }

}

export default Main;
