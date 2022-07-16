<template>
  <v-app>
    <div class="m-layout">
      <m-header />
      <div class="m-layout-container">
        <v-container class="mt-10 scrollable" fluid>
          <slot />
        </v-container>
      </div>
      <m-footer />
    </div>
  </v-app>
</template>

<script>
import MHeader from '@/components/Partitions/MHeader.vue';
import MFooter from '@/components/Partitions/MFooter.vue';
export default {
  components: {
    MHeader,
    MFooter,
  },
};
</script>

<style scoped>
.scrollable {
  overflow-y: scroll;
}
.m-layout-container {
  background: #eceff1;
}
</style>
