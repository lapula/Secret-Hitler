import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';

import GameScreen from './GameScreen.jsx';

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
  paper: {
    height: "100%",
    width: "100%",
    padding: "8px 25px"
  }
}

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
      <div style={style.container}>
        <div>
          <Paper style={style.paper} zDepth={2}>
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
          <RaisedButton label="Create game!" primary={true} style={style.button} onTouchTap={this.handleSubmit} />
        </Paper>
      </div>
    </div>
    )
  }

}

export default CreateGame;
