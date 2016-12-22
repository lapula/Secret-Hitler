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

        let list = new Array();

        list.push(this.createListItem("asdf", "1"))
        list.push(this.createListItem("asdfa", "12"))

        return(
            <div style={listStyle.root}>
                <List>
                    <Subheader>General</Subheader>
                    {list}
                </List>
            </div>
        )
    }
}

export default OptionList;
