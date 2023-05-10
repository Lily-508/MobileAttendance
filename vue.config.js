const { defineConfig } = require('@vue/cli-service')
const fs=require('fs');
const path=require('path');
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    devtool: 'source-map'
  },
  devServer: {
    open: false,
    https: {
      cert: fs.readFileSync(path.join(__dirname, 'ssl/file.crt')),
      key: fs.readFileSync(path.join(__dirname, 'ssl/private.pem'))
    },
    host:'0.0.0.0',
    port:8080,
    hot: true,
  }
})
