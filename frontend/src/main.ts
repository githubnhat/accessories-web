import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import 'roboto-fontface/css/roboto/roboto-fontface.css';
import '@mdi/font/css/materialdesignicons.css';
import '@/plugins/vee';
import {setup} from '@/interceptors/axios-setup';
import { initializeApp } from 'firebase/app';
Vue.config.productionTip = false;

const firebaseConfig = {
  apiKey: "AIzaSyCihS1CDNxkc3z-y60KJECdHJdfhe9XvpY",
  authDomain: "minhnhat569-eecaa.firebaseapp.com",
  databaseURL: "https://minhnhat569-eecaa.firebaseio.com",
  projectId: "minhnhat569-eecaa",
  storageBucket: "minhnhat569-eecaa.appspot.com",
  messagingSenderId: "987914771144",
  appId: "1:987914771144:web:ad674ef460d78d4753a3fd",
  measurementId: "G-LMZ881S20T"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
setup();
new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount('#app');
