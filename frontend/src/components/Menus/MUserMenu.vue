<template>
  <v-menu offset-y>
    <template v-slot:activator="{ on, attrs }">
      <v-btn color="white" dark v-bind="attrs" v-on="on" icon>
        <v-icon>{{ icon }}</v-icon>
      </v-btn>
    </template>
    <v-list>
      <v-list-item v-for="(item, index) in items" :key="index">
        <v-list-item-title
          ><v-btn :color="item.color" @click="handleClick(item)">
            <span>
              {{ item.title }}
            </span>
          </v-btn></v-list-item-title
        >
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
import router from '@/router';
import { doLogout } from '@/services';
export default {
  name: 'MUserMenu',
  props: {
    icon: {
      type: String,
      default: 'mdi-dots-vertical',
    },
    items: {
      type: Array,
      default: () => [
        {
          index: 1,
          title: 'Profile',
          action: 'profile',
          color: 'primary',
        },
        {
          index: 2,
          title: 'Cart',
          action: 'cart',
          color: 'primary',
        },
        {
          index: 3,
          title: 'Log out',
          action: 'log-out',
          color: 'warning',
        },
      ],
    },
  },
  methods: {
    async handleClick(item) {
      if (item.action === 'log-out') {
        localStorage.removeItem('accessToken');
        const status = await doLogout();
        if (status === 200) {
          this.$store.commit('_accessToken/resetAccessToken');
          router.push('/login');
        }
      }
      if (item.action === 'profile') {
        router.push('/profile');
      }
    },
  },
};
</script>

<style lang="scss" scoped>
button {
  width: 100%;
}
</style>
