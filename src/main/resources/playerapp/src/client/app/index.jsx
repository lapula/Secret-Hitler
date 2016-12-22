import React from 'react';
import {render} from 'react-dom';
import $ from 'jquery';
import injectTapEventPlugin from 'react-tap-event-plugin';
import {deepOrange500} from 'material-ui/styles/colors';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import darkBaseTheme from 'material-ui/styles/baseThemes/darkBaseTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import Main from '../app/components/Main.jsx';

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

const muiTheme = getMuiTheme({
  palette: {
    accent1Color: deepOrange500,
  },
});

class App extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      /* muiTheme={getMuiTheme(darkBaseTheme)}*/
      <MuiThemeProvider>
          <Main />
      </MuiThemeProvider>
    );
  }
}

render(<App/>, document.getElementById('app'));
