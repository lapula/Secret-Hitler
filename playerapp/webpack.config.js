var webpack = require('webpack');
var path = require('path');

var APP_DIR = path.resolve(__dirname, 'src/client/app');
var JS_DIR = path.resolve(__dirname, '../src/main/resources/public/js');

var config = {
  entry: APP_DIR + '/index.jsx',
  output: {
    path: JS_DIR,
    filename: 'bundle.js'
  },
  module : {
    loaders : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel'
      }
    ]
  }
};

module.exports = config;
