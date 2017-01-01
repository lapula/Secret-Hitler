import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';
import Paper from 'material-ui/Paper';

const style = {
  subheader: {
      fontSize: "14px",
      padding: "0px"
  },
  listItemLarge: {
      fontSize: "32px",
      height: "64px",
      lineHeight: "32px"
  },
  listItemSmall: {
      fontSize: "24px",
      height: "48px",
      lineHeight: "20px",
      fontWeight: "bold"

  },
  listContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    flexGrow: "inherit",
    width: "80%"
  },
  paper: {
    height: "100%",
    width: "100%",
    padding: "10px"
  },
  list: {
    width: "100%"
  }
};

class OptionList extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        listData: null,
        previousChoice: null
      };
    }

    createListItem(text, key, itemStyle) {
        let listClickEvent = this.handleClick.bind(this, key, text);
        return (
          <ListItem
            style={itemStyle}
            primaryText={text}
            key={key}
            onClick={listClickEvent}
          />
        )
    }

    handleClick(key, text, elem) {
        this.props.webSocket.send(JSON.stringify({
          "type": "QUERY_RESPONSE",
          "playerName": this.props.playerName,
          "gameName": this.props.gameName,
          "response": key
        }));
        this.setState({
          listData: null,
          previousChoice: text
        });
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
          listData: nextProps.queryData,
          previousChoice: null
        });
    }

    render() {

        if (!this.props.visible) {
          return null;
        }

        if (this.state.listData == null) {
          if (this.state.previousChoice != null) {
            return (<div style={style.listContainer}>
                      <h2>You chose: <b>{this.state.previousChoice}</b></h2>
                    </div>);
          }
          return (<div style={style.listContainer}></div>);
        } else if (this.state.listData.choices.length == 0) {
          return (<div style={style.listContainer}></div>);
        }

        let listStyle = style.listItemSmall;

        let list = new Array();
        for (let i in this.state.listData.choices) {
          list.push(this.createListItem(this.state.listData.choices[i], i, listStyle))
        }

        return(
            <div style={style.listContainer}>
                <List style={style.list}>
                  <Paper style={style.paper} zDepth={4}>
                    <Subheader style={style.subheader}>{this.state.listData.subheader}</Subheader>
                    {list}
                  </Paper>
                </List>
            </div>
        )
    }
}

export default OptionList;
