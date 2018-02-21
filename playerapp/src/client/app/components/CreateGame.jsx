import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';

import styles from './general-style.css';
import GameScreen from './GameScreen.jsx';

class CreateGame extends React.Component {
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

  render() {
    if (this.state.gamePlayers != "" && this.state.gameName != "") {
      return (<GameScreen createGame={true} gamePlayers={this.state.gamePlayers} gameName={this.state.gameName} />)
    }

    return (
      <div className={styles.container}>
        <div>
          <Paper className={styles.paper} zDepth={2}>
            <h1>Create game</h1>
            <TextField
                floatingLabelText="Game name"
                ref="gameName"
              /><br />
            <TextField
                  floatingLabelText="Number of players"
                  ref="gamePlayers"
                  onKeyPress={this.handleKeyPress}
            /><br />
          <RaisedButton label="Create game!" primary={true} className={styles.formButton} onTouchTap={this.handleSubmit} />
        </Paper>
      </div>
    </div>
    )
  }

}

export default CreateGame;
