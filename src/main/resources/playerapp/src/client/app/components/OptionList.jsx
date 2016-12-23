import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';

const listStyle = {
  root: {
    display: 'flex',
    flexWrap: 'wrap',
  },
};

class OptionList extends React.Component {

    constructor(props) {
      super(props);
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
      alert(key)
    }

    render() {
      
        if (this.props.queryData == null) {
          return null;
        }

        let list = new Array();
        for (let i in this.props.queryData.choices) {
          list.push(this.createListItem(this.props.queryData.choices[i], i))
        }

        return(
            <div style={listStyle.root}>
                <List>
                    <Subheader>{this.props.queryData.subheader}</Subheader>
                    {list}
                </List>
            </div>
        )
    }
}

export default OptionList;
