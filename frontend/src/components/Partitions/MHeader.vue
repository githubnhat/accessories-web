<template>
  <nav>
    <v-app-bar color="primary" dense dark v-bind="$attrs" fixed short>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title class="text-upper-case">
        <span class="font-weight-light">Shop</span>
        <span>Accessories</span>
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <v-form v-if="hidden">
        <v-row>
          <v-btn @click="handleSubmit" icon><v-icon> mdi-magnify </v-icon></v-btn>
          <v-text-field
            class="mt-1"
            placeholder="Finding something..."
            v-bind="$attrs"
            outlined
            dense
            hide-details="auto"
            color="white"
            autocomplete
          >
          </v-text-field>
        </v-row>
      </v-form>

      <v-btn class="ml-2" color="white" @click="hidden = !hidden" icon>
        <v-icon>{{ hidden ? 'mdi-close' : 'mdi-magnify' }}</v-icon>
      </v-btn>
      <v-btn v-if="!username" @click="handleToLogin" color="primary" elevation="5">
        <v-icon>mdi-login</v-icon>
        <span class="px-2">Login</span>
      </v-btn>
      <span v-else> {{ 'Hello ' + username }}</span>
      <m-user-menu></m-user-menu>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" absolute bottom temporary app hide-overlay>
      <v-list nav dense>
        <v-list-item v-for="item in links" :key="item.text" router :to="item.route">
          <v-list-item-action>
            <v-icon class="primary--text">{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-title class="primary--text">{{ item.text }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import MUserMenu from '../Menus/MUserMenu.vue';
import jwt_decode from 'jwt-decode';
import router from '@/router';

export default {
  name: 'MHeader',
  data: () => ({
    username: '',

    hidden: false,
    drawer: false,
    links: [
      { icon: 'mdi-shape-outline', text: 'All', route: '/all' },
      { icon: 'mdi-hat-fedora', text: 'Hats', route: '/hats' },
      { icon: 'mdi-bag-personal', text: 'Bags', route: '/bags' },
    ],
  }),
  created() {
    const accessToken = localStorage.getItem('accessToken');
    this.username = !accessToken ? null : jwt_decode(accessToken).sub;
  },
  props: {},
  methods: {
    handleSubmit() {
      console.log('click');
    },
    handleToLogin() {
      router.push('/login');
    },
  },

  components: {
    MUserMenu,
  },
  watch: {},
};
</script>

<style scoped>
header {
  height: 48px !important;
}
</style>
