/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */
const { defineConfig } = require('@vue/cli-service');
const path = require('path');
const vueSrc = './src';
module.exports = defineConfig({
  transpileDependencies: ['vuetify'],
});
module.exports = {
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "~@/sass/_variables.scss";`,
      },
      postcss: {
        // options here will be passed to postcss-loader
      },
    },
  },

  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, vueSrc),
      },
      extensions: ['.js', '.vue', '.json'],
    },
  },

  transpileDependencies: ['vuetify'],
};
