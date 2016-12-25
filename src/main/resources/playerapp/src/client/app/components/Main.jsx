import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';

import PlayerApp from './PlayerApp.jsx';

const style = {
  button: {
    margin: "12px"
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
      return (<PlayerApp />)
    } else {
      return (
        <div id="container">
          <RaisedButton label="Join a game" primary={true} style={style.button}
            onTouchTap={this.showPlayerScreen} /><br />
          <RaisedButton label="Create a game" primary={true} style={style.button}
            onTouchTap={this.showGameCreationScreen} /><br />
          <RaisedButton label="Register game screen" primary={true} style={style.button}
            onTouchTap={this.showGameScreen} /><br />
        </div>
      )
    }
  }

}

export default Main;
