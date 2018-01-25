
const gulp = require("gulp");
const gutil = require("gulp-util");
const webpack = require("webpack");
const path = require("path");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const WebpackDevServer = require("webpack-dev-server");

function webpackOptions(options) {

  let plugins = [];

  if (options.stats) {
    plugins.push(
      new BundleAnalyzerPlugin()
    );
  }

  return {
    watch: options.watch,
    entry: {
      main: './assets/main.js',
    },
    output: {
      path: options.outputPath,
      filename: "[name].js",
      publicPath: options.publicPath
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
          test: /\.js$/,
          include: [
            path.resolve(__dirname, "assets"),
          ],
          exclude: [
            path.resolve(__dirname, "assets/build"),
          ],
          loader: 'babel-loader',
          options: {
            presets: ['es2015'],
            plugins: [
              "transform-vue-jsx",
              "transform-object-rest-spread",
              "async-import",
            ]
          }
        },
        {
          test: /\.html/,
          use: 'html-loader'
        },
        {
          test: /\.vue$/,
          loader: 'vue-loader',
          options: {
            loaders: {
              js: 'babel-loader?' + JSON.stringify({
                presets: ['es2015'],
                plugins: [
                  "transform-vue-jsx",
                  "transform-object-rest-spread",
                  "async-import",
                ]
              }),
              javascript: 'babel-loader?' + JSON.stringify({
                presets: ['es2015'],
                plugins: [
                  "transform-vue-jsx",
                  "transform-object-rest-spread",
                  "async-import",
                ]
              }),
              scss: "vue-style-loader!style-loader!css-loader!sass-loader",
            }
          }
        },
        {
          test: /\.(ts|tsx)?$/,
          loader: 'awesome-typescript-loader',
          options: {
            transpileOnly: true
          },
        },
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
          test: /\.(gif|svg|ttf|eot)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
          use: "file-loader"
        }
      ]
    },
    plugins: plugins
  };
}

gulp.task("watch", function() {
    webpack(webpackOptions({
        watch: true,
        debug: true,
        publicPath: '/assets/build/',
        outputPath: __dirname + "/public/build",
    }), function(err, stats) {
        if(err) throw new gutil.PluginError("webpack", err);
        gutil.log("[webpack]", stats.toString({}));
    });
});

gulp.task("server", function() {
    let compiler = webpack(webpackOptions({
        watch: false,
        debug: true,
        publicPath: 'http://localhost:8181/assets/build/',
        outputPath: __dirname + "/assets/build",
    }), function(err, stats) {
        if(err) throw new gutil.PluginError("webpack", err);
        gutil.log("[webpack]", stats.toString({}));
    });

  const server = new WebpackDevServer(compiler, {
    // webpack-dev-server options

    contentBase: __dirname + "/assets/build",
    watchContentBase: true,
    // Can also be an array, or: contentBase: "http://localhost/",

    hot: false,

    inline: true,
    // Enable special support for Hot Module Replacement
    // Page is no longer updated, but a "webpackHotUpdate" message is sent to the content
    // Use "webpack/hot/dev-server" as additional module in your entry point
    // Note: this does _not_ add the `HotModuleReplacementPlugin` like the CLI option does.

    // compress: true,
    // Set this if you want to enable gzip compression for assets
    //
    // proxy: {
    //   "**": "http://localhost:9090"
    // },

    // webpack-dev-middleware options
    // quiet: false,
    // noInfo: false,
    // lazy: true,
    filename: "main.js",
    watchOptions: {
      aggregateTimeout: 300,
      // poll: 1000
    },

    // It's a required option.
    publicPath: "/assets/build/",
    stats: {
      colors: true
    },
  });

  server.listen(8181, "localhost", (err) => {
    if(err) throw new gutil.PluginError("webpack-dev-server", err);
    // Server listening
    gutil.log("[webpack-dev-server]", "http://localhost:8181/webpack-dev-server/index.html");
  });

});

gulp.task("production", function() {
    webpack(webpackOptions({
        watch: false,
        debug: false,
        publicPath: '/assets/build/',
        outputPath: __dirname + "/public/build",
    }), function(err, stats) {
        if(err) throw new gutil.PluginError("webpack", err);
        gutil.log("[webpack]", stats.toString({
            // output options
        }));
    });
});