var webpack = require('webpack');
const path = require('path');
const paths = {
  DIST: path.resolve(__dirname, 'dist'),
  SRC: path.resolve(__dirname, 'src'),
  JS: path.resolve(__dirname, 'src/client/app'),
};
const prod = process.argv.indexOf('-p') !== -1;
const APP_DIR = path.resolve(__dirname, 'src/client/app');
const JS_DIR = path.resolve(__dirname, '../src/main/resources/public/js');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

var config = {
  entry: path.join(paths.JS, 'index.jsx'),
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
      },
      {
        test: /\.css$/,
        loaders: [
          'style?sourceMap',
          'css?modules&importLoaders=1&localIdentName=[path]___[name]__[local]___[hash:base64:5]'
        ]
      },
      { test: /\.(png|jpg)$/, loader: 'url-loader' },
      { test: /\.(eot|svg|ttf|woff|woff2)$/, loader: 'file?name=[name].[ext]'}
    ]
  }
};

config.plugins = config.plugins||[];
if (prod) {
  config.plugins.push(new webpack.DefinePlugin({
      'process.env': {
          'NODE_ENV': `"production"`
      }
  }));
  config.plugins.push(new webpack.optimize.DedupePlugin());
  config.plugins.push(new webpack.optimize.UglifyJsPlugin());
  config.plugins.push(new webpack.optimize.AggressiveMergingPlugin());
} else {
  config.plugins.push(new webpack.DefinePlugin({
      'process.env': {
          'NODE_ENV': `""`
      }
  }));
}

module.exports = config;
