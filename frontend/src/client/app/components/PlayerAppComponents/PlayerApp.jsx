import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import {textConstants} from '../constants.jsx'
import styles from './playerapp-style.css'
import RoleDialog from './RoleDialog.jsx';
import OptionList from './OptionList.jsx';

const PING = "PING";
const REGISTER_PLAYER = "REGISTER_PLAYER";
const PLAYER_INIT = "PLAYER_INIT";
const PLAYER_QUERY = "PLAYER_QUERY";
const SET_SPECIAL_ROLE = "SET_SPECIAL_ROLE";
const ALERT_PLAYER = "ALERT_PLAYER";
const QUERY_RESPONSE = "QUERY_RESPONSE";
const PING_INTERVAL = 10000;
const RECONNECT_DELAY = 3000;
const MAX_RECONNECT_ATTEMPTS = 400;

class PlayerApp extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      hasWebsocketConnection: false,
      phase: textConstants.gameStarting,
      playerRole: textConstants.roleNotAssigned,
      queryData: null,
      specialRole: "",
      dialogOpen: false,
      dialogHeader: "",
      dialogText: ""
    };

    this.handleDialogClose = this.handleDialogClose.bind(this);
    this.sendQueryResponse = this.sendQueryResponse.bind(this);
  }

  componentWillMount() {
    this.initSocketConnection(this, this.props.playerName, this.props.gameName, 0);
  }

  handleDialogClose() {
    this.setState({
      dialogOpen: false
    })
  }

  renderHeader() {
    let header = <h1 className={styles.header}>{this.state.phase}</h1>;
    if (!this.state.hasWebsocketConnection) {
      header = (
        <div className={styles.connectingContainer}>
          <h1 className={styles.header}>{textConstants.connecting}</h1>
        </div>
      );
    }
    return header;
  }

  render() {
    return (
      <div className={styles.container}>
        <div className={styles.specialRole}>{this.state.specialRole}</div>
        {this.renderHeader()}
        <OptionList
          queryData={this.state.queryData}
          visible={this.state.hasWebsocketConnection}
          sendQueryResponse={this.sendQueryResponse}
        />
        <RoleDialog role={this.state.playerRole} />
        <Dialog
          title={this.state.dialogHeader}
          actions={<FlatButton label={textConstants.confirmAlert} primary={true} onTouchTap={this.handleDialogClose} />}
          modal={true}
          open={this.state.dialogOpen}
        >
          {this.state.dialogText}
        </Dialog>
      </div>
    );
  }


  //####################### WebSocket ###########################

  initSocketConnection(elem, playerName, gameName, attemptCounter) {
    const component = elem;
    const protocol = (location.hostname == "localhost") ? "ws" : "wss";
    let webSocket = new WebSocket(protocol + "://" + location.hostname + ":" + location.port + "/players");
    component.setState({webSocket: webSocket});

    console.log(location.hostname);
    let pingInterval = setInterval(() => {
      webSocket.send(JSON.stringify({
        "type": PING,
        "playerName": playerName,
        "gameName": gameName
      }));
    }, PING_INTERVAL);

    webSocket.onopen = function() {
      component.setState({
        hasWebsocketConnection: true
      })
      let message =  {
        "type": REGISTER_PLAYER,
        "playerName": playerName,
        "gameName": gameName
      }
      webSocket.send(JSON.stringify(message));
    };

    webSocket.onmessage = function (msg) {
      let data = JSON.parse(msg.data);
      console.log(data)

      if (data.type == PLAYER_INIT) {
        component.setState({playerRole: data.role});
      } else if (data.type == PLAYER_QUERY) {
        component.setState({
          queryData: data,
          phase: data.header
        });
      } else if (data.type == SET_SPECIAL_ROLE) {
        component.setState({
          specialRole: data.role
        });
      } else if (data.type == ALERT_PLAYER) {
        component.setState({
          dialogOpen: true,
          dialogHeader: data.header,
          dialogText: data.text
        });
      }
    };

    webSocket.onclose = function () {
      const time = new Date();
      console.log("WebSocket connection closed at: " + time.getHours() + ":" + time.getMinutes());

      clearInterval(pingInterval);
      component.setState({
        hasWebsocketConnection: false
      });

      if (attemptCounter < MAX_RECONNECT_ATTEMPTS) {
        setTimeout(function(){
          console.log("Attempting to reopen websocket.")
          component.initSocketConnection(component, playerName, gameName, attemptCounter + 1);
        }, RECONNECT_DELAY);
      }
    };

    webSocket.onerror = function(err) {
        console.log("Socket error: ");
        console.log(err);
    };
  }

  sendQueryResponse(responseKey) {
    this.state.webSocket.send(JSON.stringify({
      "type": QUERY_RESPONSE,
      "playerName": this.props.playerName,
      "gameName": this.props.gameName,
      "response": responseKey
    }));
    this.setState({
      queryData: null
    });
  }

}

export default PlayerApp;
