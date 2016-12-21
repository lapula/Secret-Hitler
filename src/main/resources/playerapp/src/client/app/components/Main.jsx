import React, {Component} from 'react';
import $ from 'jquery';
import AppBar from 'material-ui/AppBar';

import Footer from './Footer.jsx';

class Main extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      phase: "Game is starting..."
    };
  }


  render() {
    return (
      <div id="container">

          <h1>{this.state.phase}</h1>

          <Footer />
      </div>
    );
  }
}


export default Main;
