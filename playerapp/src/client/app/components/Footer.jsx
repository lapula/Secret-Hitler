import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

const customButtonStyle = {
    lineHeight: 72,
    height: 72
}
const style = {
  container: {
    width: "90%",
    margin: "10px"
  }
}

class Footer extends React.Component {

  constructor(props) {
    super(props);

    this.handleRequestClose = this.handleRequestClose.bind(this);
    this.handleTouchTap = this.handleTouchTap.bind(this);

    this.state = {
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
      <footer style={style.container}>
        <Dialog
          open={this.state.open}
          title="Your role is:"
          actions={roleDialog}
          onRequestClose={this.handleRequestClose}
        >
          {this.props.role}
        </Dialog>
        <RaisedButton
          primary={true}
          label="Your role"
          labelStyle={{fontSize: '36px'}}
          style={{height: "60px", lineHeight: "0px"}}
          buttonStyle={{overflow: "hidden"}}
          overlayStyle={{paddingTop: "8px"}}
          fullWidth={true}
          onTouchTap={this.handleTouchTap}
        />
      </footer>
    );
  }
}

export default Footer;
