import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import _accessToken from './_accessToken';
import _controlAdmin from './_controlAdmin';
import _cart from './_cart';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    _accessToken,
    _cart,
    _controlAdmin,
  },
});
