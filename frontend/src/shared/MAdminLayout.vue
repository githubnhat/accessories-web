<template>
  <v-app>
    <div class="m-layout">
      <m-admin-header />
      <div class="m-layout-container">
        <v-container class="mt-12 pa-0" fluid>
          <slot />
        </v-container>
      </div>
      <m-footer />
    </div>
  </v-app>
</template>

<script>
import MFooter from '@/components/Partitions/MFooter.vue';
import MAdminHeader from '@/components/Partitions/MAdminHeader.vue';

export default {
  components: {
    MAdminHeader,
    MFooter,
  },
};
</script>

<style scoped></style>
