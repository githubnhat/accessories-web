import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import _accessToken from './_accessToken';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    _accessToken,
  },
});
