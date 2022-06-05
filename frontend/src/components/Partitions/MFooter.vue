<template>
  <v-bottom-navigation
    background-color="primary"
    :value="value"
    color="white"
    horizontal
    v-bind="$attrs"
    fixed
  >
    <v-btn>
      <span>Recents</span>

      <v-icon>mdi-history</v-icon>
    </v-btn>

    <v-btn>
      <span>Favorites</span>

      <v-icon>mdi-heart</v-icon>
    </v-btn>

    <v-btn>
      <span>Nearby</span>

      <v-icon>mdi-map-marker</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: 'MFooter',
  props: {},
  data: () => ({ value: 1 }),
};
</script>

<style></style>
