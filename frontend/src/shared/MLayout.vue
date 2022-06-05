<template>
  <v-app>
    <div class="m-layout">
      <m-header />
      <div class="m-layout-container">
        <v-container class="mt-10" fluid>
          <v-layout align-center justify-center>
            <slot />
          </v-layout>
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

<style></style>
