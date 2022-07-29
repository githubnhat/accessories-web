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
      />
    </template>

    <v-list>
      <v-list-item v-for="(item, index) in menuList" :key="index" link :to="`${item.link}`">
        <v-list-item-title>{{ item.title }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
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
  },
};
</script>

<style scoped>
.button-menu:hover {
  font-weight: 900;
  text-decoration: underline;
}
</style>
