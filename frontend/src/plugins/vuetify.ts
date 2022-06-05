import Vue from 'vue';
import Vuetify from 'vuetify';

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    options: {
      customProperties: true,
    },
    themes: {
      light: {
        primary: '#187498',
        secondary: '#424242',
        accent: '#82B1FF',
        error: '#FF5252',
        info: '#F9D923',
        success: '#36AE7C',
        warning: '#EB5353',
        background: '#F0F0F0',
      },
    },
  },
});
