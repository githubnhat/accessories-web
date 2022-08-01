<template>
  <v-menu v-bind="$attrs" menu-props>
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="primary"
        class="white--text my-2 button-menu"
        elevation="0"
        dark
        v-bind="attrs"
        v-on="on"
        v-text="btnTitle"
        @click="handleGoTo"
      />
    </template>

    <v-list v-if="menuList.length > 0">
      <v-list-item v-for="(item, index) in menuList" :key="index" link :to="`${item.link}`">
        <v-list-item-title>{{ item.title }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
import router from '@/router';
export default {
  props: {
    btnTitle: {
      type: String,
      default: 'Menu Title',
    },
    menuList: {
      type: Array,
      default: () => [],
    },
    link: {
      type: String,
      default: '',
    },
  },
  methods: {
    handleGoTo() {
      if (this.link !== '') {
        router.push(this.link).catch((error) => {
          if (
            error.name !== 'NavigationDuplicated' &&
            !error.message.includes('Avoided redundant navigation to current location')
          ) {
            console.log(error);
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.button-menu:hover {
  font-weight: 900;
  text-decoration: underline;
}
</style>
