import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';

import {textConstants} from './constants.jsx'
import styles from './general-style.css';
import GameScreen from './GameScreenComponents/GameScreen.jsx';

class RegisterGame extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      gamePlayers: "",
      gameName: ""
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this, event);
  }

  handleSubmit() {
    this.setState({
      gamePlayers: this.refs.gamePlayers.input.value,
      gameName: this.refs.gameName.input.value
    });
  }

  handleKeyPress(component, event) {
    if(event.key == 'Enter'){
      this.handleSubmit();
    }
  }

  renderHeader() {
    return (this.props.createNewGame ? <h1>{textConstants.createGame}</h1> : <h1>{textConstants.openGameScreen}</h1>);
  }

  renderPlayersNumber() {
    if (this.props.createNewGame) {
      return (
          <TextField
                floatingLabelText={textConstants.numberOfPlayers}
                ref="gamePlayers"
                onKeyPress={this.handleKeyPress}
          />
      );
    }
    return "";
  }

  render() {
    if (this.state.gamePlayers != "" && this.state.gameName != "") {
      return (<GameScreen createGame={this.props.createNewGame} gamePlayers={this.state.gamePlayers} gameName={this.state.gameName} />)
    }

    return (
      <div className={styles.container}>
        <div>
          <Paper className={styles.paper} zDepth={2}>
            {this.renderHeader()}
            <TextField
                floatingLabelText={textConstants.gameName}
                ref="gameName"
              />
            {this.renderPlayersNumber()}
            <RaisedButton label={textConstants.createGame} primary={true} className={styles.formButton} onTouchTap={this.handleSubmit} />
        </Paper>
      </div>
    </div>
    )
  }
}

export default RegisterGame;
