import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';

import {textConstants, materialUiOverrides} from './constants.jsx'
import styles from './general-style.css';
import GameScreen from './GameScreenComponents/GameScreen.jsx';

class RegisterGame extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      gamePlayers: "",
      gameName: "",
      playersErrors: textConstants.playersError,
      nameErrors: textConstants.gameNameError
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
    this.handlePlayersChange = this.handlePlayersChange.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
  }

  handleSubmit() {
    if (this.state.playersErrors == null || this.state.nameErrors == null) {
      if (this.props.createNewGame) {
        this.setState({ gamePlayers: this.refs.gamePlayers.input.value });
      } else {
        this.setState({ gameName: this.refs.gameName.input.value });
      }
    }
  }

  handleKeyPress(event) {
    if(event.key == 'Enter'){
      this.handleSubmit();
    }
  }

  renderHeader() {
    return (this.props.createNewGame ? <h1>{textConstants.createGame}</h1> : <h1>{textConstants.openGameScreen}</h1>);
  }

  handlePlayersChange(event) {
    const players = parseInt(event.target.value);
    if (players < 5 || players > 10 || Number.isNaN(players)) {
      this.setState({playersErrors: textConstants.playersError})
    } else {
      this.setState({playersErrors: null})
    }
  };

  handleNameChange(event) {
    const gameName = event.target.value;
    if (gameName.length != 6) {
      this.setState({nameErrors: textConstants.gameNameError})
    } else {
      this.setState({nameErrors: null})
    }
  }

  renderField() {
    if (this.props.createNewGame) {
      return (
        <TextField
          floatingLabelText={textConstants.numberOfPlayers}
          ref="gamePlayers"
          onKeyPress={this.handleKeyPress}
          onChange={this.handlePlayersChange}
          errorText={this.state.playersErrors}
        />
      );
    } else {
      return (
        <TextField
          floatingLabelText={textConstants.gameName}
          ref="gameName"
          onKeyPress={this.handleKeyPress}
          onChange={this.handleNameChange}
          errorText={this.state.nameErrors}
        />
      );
    }
  }

  render() {
    if (this.state.gamePlayers != "" || this.state.gameName != "") {
      return (<GameScreen createGame={this.props.createNewGame} gamePlayers={this.state.gamePlayers} gameName={this.state.gameName} />)
    }

    return (
      <div className={styles.container}>
        <div>
          <Paper className={styles.paper} zDepth={2}>
            {this.renderHeader()}
            {this.renderField()}
            <RaisedButton
              label={textConstants.createGame}
              primary={true}
              className={styles.formButton}
              onTouchTap={this.handleSubmit}
            />
        </Paper>
      </div>
    </div>
    )
  }
}

export default RegisterGame;
