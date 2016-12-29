import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import NavigationClose from 'material-ui/svg-icons/navigation/close';

import PlayerApp from './PlayerApp.jsx';

const style = {
  container: {
    display: "flex",
    textAlign: "center",
    height: "100%",
    flexDirection: "column",
    justifyContent: "center"
  },
  button: {
    margin: "12px"
  },
  buttonContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    flex: 1
  }
}

class Main extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      renderComponent: null
    };

    this.showPlayerScreen = this.showPlayerScreen.bind(this);
    this.showGameCreationScreen = this.showGameCreationScreen.bind(this);
    this.showGameScreen = this.showGameScreen.bind(this);
  }

  showPlayerScreen() {
    this.setState({renderComponent: "PLAYER_APP"});
  }

  showGameCreationScreen() {
    this.setState({renderComponent: "PLAYER_APP"});
  }

  showGameScreen() {
    this.setState({renderComponent: "PLAYER_APP"});
  }

  render() {

    if (this.state.renderComponent == "PLAYER_APP") {
      return (
        <div style={style.container}>
          <AppBar title="Secret Hitler"
              iconElementLeft={<div></div>}
           />
          <PlayerApp />
        </div>
        )
    } else {
      return (
        <div style={style.container}>
          <AppBar title="Secret Hitler"
              iconElementLeft={<div></div>}
           />
         <div style={style.buttonContainer}>
            <RaisedButton label="Join a game" primary={true} style={style.button}
              onTouchTap={this.showPlayerScreen} /><br />
            <RaisedButton label="Create a game" primary={true} style={style.button}
              onTouchTap={this.showGameCreationScreen} /><br />
            <RaisedButton label="Open game screen" primary={true} style={style.button}
              onTouchTap={this.showGameScreen} /><br />
          </div>
        </div>
      )
    }
  }

}

export default Main;
