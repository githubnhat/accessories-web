<template>
  <v-app id="inspire">
    <m-admin-side-bar
      :title="title"
      :controlList="controlList"
      :drawer="drawer"
      @changeControl="handleControlAdmin"
    />

    <v-main>
      <m-admin-header :title="roleTitle" :drawer="drawer" @onClickDrawer="handleDrawer" />
      <v-container class="pa-3" fluid>
        <slot />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import MAdminHeader from '@/components/Partitions/MAdminHeader.vue';
import MAdminSideBar from '@/components/Partitions/MAdminSideBar.vue';

export default {
  props: {
    title: {
      type: String,
      default: 'AccessoriesShop Administration ',
    },
    roleTitle: {
      type: String,
      default: 'Admin', // role admin: 0 otherwise role user: 1
    },
    controlList: {
      type: Array,
      default: () => [],
    },
  },
  data: () => ({
    drawer: true,
    cards: ['Today', 'Yesterday'],
  }),
  methods: {
    handleDrawer(drawer) {
      this.drawer = drawer;
    },
    handleControlAdmin(control) {
      this.$emit('changeControl', control);
    },
  },
  components: {
    MAdminHeader,
    MAdminSideBar,
  },
};
</script>

<style scoped></style>
