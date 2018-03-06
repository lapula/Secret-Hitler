import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';

import {textConstants} from './constants.jsx'
import styles from './PlayerAppComponents/playerapp-style.css'
import PlayerApp from './PlayerAppComponents/PlayerApp.jsx'


class RegisterPlayer extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      playerName: null,
      gameName: null,
      playerErrors: textConstants.playerNameError,
      gameErrors: textConstants.gameNameError
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
    this.handlePlayerChange = this.handlePlayerChange.bind(this);
    this.handleGameChange = this.handleGameChange.bind(this);
  }

  handleSubmit() {
    if (this.state.playerErrors == null && this.state.gameErrors == null) {
      this.setState({
        playerName: this.refs.playerName.input.value,
        gameName: this.refs.gameName.input.value
      });
    }
  }

  handleKeyPress(event) {
    if (event.key == 'Enter') {
      this.handleSubmit();
    }
  }

  handlePlayerChange(event) {
    const playerName = event.target.value;
    if (playerName.length == 0 || playerName.length > 12) {
      this.setState({playerErrors: textConstants.playerNameError})
    } else {
      this.setState({playerErrors: null})
    }
  };

  handleGameChange(event) {
    const gameName = event.target.value;
    if (gameName.length != 6) {
      this.setState({gameErrors: textConstants.gameNameError})
    } else {
      this.setState({gameErrors: null})
    }
  }

  render() {
    if (this.state.playerName == null) {
      return (
        <div className={styles.container}>
          <div>
            <Paper className={styles.paper} zDepth={2}>
              <h1>{textConstants.joinGame}</h1>
              <TextField
                  floatingLabelText={textConstants.playerName}
                  ref="playerName"
                  onChange={this.handlePlayerChange}
                  errorText={this.state.playerErrors}
                /><br />
              <TextField
                  floatingLabelText={textConstants.gameName}
                  ref="gameName"
                  onKeyPress={this.handleKeyPress}
                  onChange={this.handleGameChange}
                  errorText={this.state.gameErrors}
              /><br />
            <RaisedButton
              label={textConstants.enterGame}
              primary={true}
              className={styles.button}
              onTouchTap={this.handleSubmit}
            />
          </Paper>
          </div>
        </div>
      );
    } else {
      return (<PlayerApp playerName={this.state.playerName} gameName={this.state.gameName}/>);
    }
  }
}

export default RegisterPlayer;
