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

class ListenGame extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      gameName: ""
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this, event);
  }

  handleSubmit() {
    this.setState({
      gameName: this.refs.gameName.input.value
    });
  }

  handleKeyPress(component, event) {
    if(event.key == 'Enter'){
      this.handleSubmit();
    }
  }

  render() {
    if (this.state.gameName != "") {
      return (<GameScreen createGame={false} gamePlayers={""} gameName={this.state.gameName} />)
    }

    return (
      <div style={style.container}>
        <div>
          <Paper style={style.paper} zDepth={2}>
            <h1>Register game screen</h1>
            <TextField
                floatingLabelText="Game name"
                ref="gameName"
                onKeyPress={this.handleKeyPress}
              /><br />
            <RaisedButton label="Register screen!" primary={true} style={style.button} onTouchTap={this.handleSubmit} />
        </Paper>
      </div>
    </div>
    )
  }

}

export default ListenGame;
