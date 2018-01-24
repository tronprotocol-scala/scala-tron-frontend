const path = require('path');

module.exports = {
  watch: true,
  entry: './assets/main.js',
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'assets/build')
  },
  resolve: {
    extensions: ['.ts', '.js', '.vue'],
    alias: {
      'vue$': 'vue/dist/vue.common.js'
    }
  },
  module: {
    rules: [
      {
        test: /\.scss$/,
        use: ["style-loader", "css-loader", "sass-loader"]
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"]
      },
      {
        test: /\.less$/,
        use: [
          'style-loader',
          {loader: 'css-loader', options: {importLoaders: 1}},
          'less-loader'
        ]
      },
      {
        test: /\.png$/,
        use: "url-loader?limit=100000"
      },
      {
        test: /\.jpg$/,
        use: "file-loader"
      },
      {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        use: "url-loader?limit=10000&minetype=application/font-woff"
      },
      {
        test: /\.(gif|svg|ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        use: "file-loader"
      }
    ]
  },
  devServer: {
    contentBase: __dirname + "/assets/build",
    watchContentBase: true,
    filename: "bundle.js",
    inline: true,
    publicPath: "http://localhost:8080/assets/",
    stats: {
      colors: true
    },
  }
};