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
  }

  handleKeyPress(component, event) {
    if(event.key == 'Enter'){
      this.handleSubmit();
    }
  }

  render() {
    if (this.state.playerName == null) {
      return (
        <div className={styles.container}>
          <div>
            <Paper className={styles.paper} zDepth={2}>
              <h1>Join game</h1>
              <TextField
                  floatingLabelText={textConstants.playerName}
                  ref="playerName"
                /><br />
              <TextField
                    floatingLabelText={textConstants.gameName}
                    ref="gameName"
                    onKeyPress={this.handleKeyPress}
              /><br />
            <RaisedButton label="Enter game!" primary={true} className={styles.button} onTouchTap={this.handleSubmit} />
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
