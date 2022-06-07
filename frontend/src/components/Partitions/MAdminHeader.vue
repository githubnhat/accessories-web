<template>
  <nav>
    <v-app-bar color="primary" dense dark v-bind="$attrs" fixed short>
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title class="text-upper-case">
        <span class="font-weight-light">Admin | </span>
        <span>Accessories</span>
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <span> {{ 'Hello ' + username }}</span>
      <m-user-menu></m-user-menu>
    </v-app-bar>
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
