const path = require('path');
const webpack = require('webpack');
const TerserPlugin = require('terser-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const cssLoader = {
  loader: 'css-loader',
  options: {
    importLoaders: 1,
  },
};

const PRODUCTION = false;
const DIST_FOLDER = 'dist';

module.exports = {
  entry: {
    app: path.resolve(__dirname, 'src', 'scripts', 'main.js'),
  },

  output: {
    path: path.resolve(__dirname, DIST_FOLDER),
    filename: '[name].[hash].js',
    publicPath: '/',
  },

  mode: (PRODUCTION ? 'production' : 'development'),
  devtool: (PRODUCTION ? false : 'inline-source-map'),

  resolve: {
    extensions: ['.js', '.jsx', '.css'],
  },

  devServer: {
    // ...other devServer options...
  },

  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react'],
            plugins: [
              '@babel/transform-runtime',
              require('@emotion/babel-plugin'),
            ],
          },
        },
      },

      {
        test: /\.css$/i,
        use: [
          MiniCssExtractPlugin.loader,
          cssLoader,
        ],
      },

      {
        test: /\.(png|jpg|gif)/i,
        use: {
          loader: 'file-loader',
          options: {
            name: '[name].[hash].[ext]',
            outputPath: 'images',
          },
        },
      },
    ],
  },

  optimization: {
    splitChunks: {
      chunks: 'all',
    },
  },

  plugins: [
    new webpack.ProgressPlugin(),

    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'src', 'index.html'),
      filename: './index.html',
      hash: true,
    }),

    new CopyPlugin({
      patterns: [
        {
          context: path.resolve(__dirname, 'src'),
          from: '**/*.{html,css,svg,json,ico,png,gif,woff2,eot,ttf}',
          to: DIST_FOLDER,
        },
      ],
    }),

    new MiniCssExtractPlugin({
      filename: '[name].[contenthash].css',
    }),

    new TerserPlugin({
      include: /\.js$|\.jsx$/,
      exclude: /node_modules/,
      terserOptions: {
        compress: {
          warnings: PRODUCTION ? false : true,
        },
      },
    }),
  ],
};
