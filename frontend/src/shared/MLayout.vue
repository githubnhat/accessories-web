<template>
  <v-app>
    <div class="m-layout">
      <m-header />
      <div class="m-layout-container">
        <v-container class="main-content my-10" fluid>
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
.main-content {
  height: 100vh;
}
.scrollable {
  overflow-y: scroll;
}
.m-layout-container {
  background: #eceff1;
  min-height: 100%;
  padding-bottom: 600px;
}
</style>
