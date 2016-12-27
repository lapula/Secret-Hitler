import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';

const style = {
  root: {
      width: "100%"
  },
  subheader: {
      fontSize: "24px"
  },
  listItemLarge: {
      fontSize: "32px",
      height: "64px",
      lineHeight: "32px"
  },
  listItemSmall: {
      fontSize: "24px",
      height: "48px",
      lineHeight: "16px"
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

        if (this.state.listData == null) {
          if (this.state.previousChoice != null) {
            return (<h2>You chose: <b>{this.state.previousChoice}</b></h2>);
          }
          return null;
        }
        let listStyle = style.listItemLarge;

        if (this.state.listData.choices.length > 2) {
            listStyle = style.listItemSmall
        }

        let list = new Array();
        for (let i in this.state.listData.choices) {
          list.push(this.createListItem(this.state.listData.choices[i], i, listStyle))
        }

        return(
            <div style={style.root}>
                <List>
                    <Subheader style={style.subheader}>{this.state.listData.subheader}</Subheader>
                    {list}
                </List>
            </div>
        )
    }
}

export default OptionList;
