import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import Footer from './Footer.jsx';
import OptionList from './OptionList.jsx';

const style = {
  button: {
    margin: "22px"
  },
  container: {
    display: "flex",
    flexGrow: "inherit",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    flex: 1
  },
  connectingContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    flexGrow: "inherit"
  },
  paper: {
    height: "100%",
    width: "100%",
    padding: "8px 25px"
  },
  header: {
    color: "white",
    textShadow: "1px 1px 5px black"
  },
  specialRole: {
    width: "100%",
    backgroundColor: "#ff4081",
    color: "white",
    fontSize: "18px"
  }
}

class PlayerApp extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      hasWebsocketConnection: false,
      phase: "Game is starting...",
      playerRole: "Not assigned yet.",
      queryData: null,
      playerName: null,
      gameName: null,
      specialRole: "",
      dialogOpen: false,
      dialogHeader: "",
      dialogText: ""
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this, event);
    this.handleDialogClose = this.handleDialogClose.bind(this);
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

  handleDialogClose() {
    this.setState({
      dialogOpen: false
    })
  }

  render() {
    if (this.state.playerName == null) {
      return (
        <div style={style.container}>
          <div>
            <Paper style={style.paper} zDepth={2}>
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
          </Paper>
          </div>
        </div>
      )
    } else {
      let header = <h1 style={style.header}>{this.state.phase}</h1>;
      if (!this.state.hasWebsocketConnection) {
        header = (
          <div style={style.connectingContainer}>
              <h1 style={style.header}>Connecting...</h1>
          </div>
        )
    }

      return (
        <div style={style.container}>
            <div style={style.specialRole}>{this.state.specialRole}</div>
            {header}
            <OptionList
              gameName={this.state.gameName}
              playerName={this.state.playerName}
              queryData={this.state.queryData}
              webSocket={this.state.webSocket}
              visible={this.state.hasWebsocketConnection} />
            <Footer role={this.state.playerRole}/>
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
  }

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
      var time = new Date();
      var closeTime = time.getHours() + ":" + time.getMinutes();
      console.log("WebSocket connection closed at: " + closeTime);
      component.setState({
        hasWebsocketConnection: false
      })

      setTimeout(function(){
        console.log("opening connection again")
        component.initSocketConnection(elem, playerName, gameName);
      }, 3000);


    };

    webSocket.onerror = function(err) {
        console.log("Error: " + err);
    };
  }

}

export default PlayerApp;
