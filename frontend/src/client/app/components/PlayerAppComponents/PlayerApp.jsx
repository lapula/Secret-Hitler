import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import textConstants from '../textConstants.jsx'
import styles from './playerapp-style.css'
import RoleDialog from './RoleDialog.jsx';
import OptionList from './OptionList.jsx';


class PlayerApp extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      hasWebsocketConnection: false,
      phase: "Game is starting...",
      playerRole: "Not assigned yet.",
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
    this.initSocketConnection(this, this.props.playerName, this.props.gameName);
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
            <h1 className={styles.header}>Connecting...</h1>
        </div>
      )
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
          webSocket={this.state.webSocket}
          visible={this.state.hasWebsocketConnection}
          sendQueryResponse={this.sendQueryResponse}
        />
        <RoleDialog role={this.state.playerRole} />
        <Dialog
          title={this.state.dialogHeader}
          actions={<FlatButton label="I see." primary={true} onTouchTap={this.handleDialogClose} />}
          modal={true}
          open={this.state.dialogOpen}
        >
          {this.state.dialogText}
        </Dialog>
      </div>
    );
  }


  //####################### WebSocket ###########################

  initSocketConnection(elem, playerName, gameName) {
    var component = elem;
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/players");
    component.setState({webSocket: webSocket});

    var pingInterval = setInterval(function(){
        webSocket.send(JSON.stringify({
          "type":"PING",
          "playerName": playerName,
          "gameName": gameName
        }));
    }, 10000);

    webSocket.onopen = function() {
      component.setState({
        hasWebsocketConnection: true
      })
      var message =  {
        "type":"REGISTER_PLAYER",
        "playerName": playerName,
        "gameName": gameName
      }
      webSocket.send(JSON.stringify(message));
    };

    webSocket.onmessage = function (msg) {
      let data = JSON.parse(msg.data)
      console.log(data)
      if (data.type == "PLAYER_INIT") {
          component.setState({playerRole: data.role});
      } else if (data.type == "PLAYER_QUERY") {
          component.setState({
            queryData: data,
            phase: data.header
          });
      } else if (data.type == "SET_SPECIAL_ROLE") {
        component.setState({
          specialRole: data.role
        });
      } else if (data.type == "ALERT_PLAYER") {
        component.setState({
          dialogOpen: true,
          dialogHeader: data.header,
          dialogText: data.text
        })
      }
    };

    webSocket.onclose = function () {
      clearInterval(pingInterval);
      let time = new Date();
      let closeTime = time.getHours() + ":" + time.getMinutes();
      console.log("WebSocket connection closed at: " + closeTime);
      component.setState({
        hasWebsocketConnection: false
      });

      setTimeout(function(){
        console.log("opening connection again")
        component.initSocketConnection(elem, playerName, gameName);
      }, 3000);


    };

    webSocket.onerror = function(err) {
        console.log("Error: " + err);
    };
  }

  sendQueryResponse(responseKey) {
    this.state.webSocket.send(JSON.stringify({
      "type": "QUERY_RESPONSE",
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
