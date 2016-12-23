import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';

const listStyle = {
  root: {
    width: "100%"
  },
};

class OptionList extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        listData: null
      };
    }

    createListItem(text, key) {
        let listClickEvent = this.handleClick.bind(this, key);
        return (
          <ListItem
            primaryText={text}
            key={key}
            onClick={listClickEvent}
          />
        )
    }

    handleClick(key, elem) {
        this.props.webSocket.send(JSON.stringify({
          "type": "QUERY_RESPONSE",
          "response": key
        }));
        this.setState({listData: null});
    }

    componentWillReceiveProps(nextProps) {
        this.setState({listData: nextProps.queryData});
    }

    render() {

        if (this.state.listData == null) {
          return null;
        }

        let list = new Array();
        for (let i in this.state.listData.choices) {
          list.push(this.createListItem(this.state.listData.choices[i], i))
        }

        return(
            <div style={listStyle.root}>
                <List>
                    <Subheader>{this.state.listData.subheader}</Subheader>
                    {list}
                </List>
            </div>
        )
    }
}

export default OptionList;
