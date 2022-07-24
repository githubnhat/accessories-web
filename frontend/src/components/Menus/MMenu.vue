<template>
  <v-menu v-bind="$attrs" menu-props>
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="white"
        class="primary--text ma-2"
        elevation="0"
        dark
        v-bind="attrs"
        v-on="on"
        v-text="btnTitle"
        disabled
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

<style></style>
