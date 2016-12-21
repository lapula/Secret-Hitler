import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

const customButtonStyle = {
    lineHeight: 72,
    height: 72
}

class Footer extends React.Component {

  constructor(props) {
    super(props);

    this.handleRequestClose = this.handleRequestClose.bind(this);
    this.handleTouchTap = this.handleTouchTap.bind(this);

    this.state = {
      role: "Not assigned",
      open: false
    };
  }

  handleRequestClose() {
    this.setState({
      open: false,
    });
  }

  handleTouchTap() {
    this.setState({
      open: true,
    });
  }

  render() {

    const roleDialog = (
      <FlatButton
        label="Got it!"
        primary={true}
        onTouchTap={this.handleRequestClose}
      />
    );

    return (
      <footer id="footer">
        <Dialog
          open={this.state.open}
          title="Your role is:"
          actions={roleDialog}
          onRequestClose={this.handleRequestClose}
        >
          {this.state.role}
        </Dialog>
        <RaisedButton
          id="footer-button"
          label="Super Secret Password"
          fullWidth={true}
          onTouchTap={this.handleTouchTap}
        />
      </footer>
    );
  }
}

export default Footer;
