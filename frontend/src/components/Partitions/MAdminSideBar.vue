<template>
  <v-navigation-drawer :value="drawer" app>
    <v-sheet color="primary lighten-2" class="pa-4">
      <div class="white--text display-1">{{ 'Hello ' + username }}</div>
    </v-sheet>

    <v-divider></v-divider>

    <v-list>
      <v-list-group
        v-for="item in items"
        :key="item.title"
        v-model="item.active"
        :prepend-icon="item.action"
        no-action
      >
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title v-text="item.title"></v-list-item-title>
          </v-list-item-content>
        </template>

        <v-list-item
          v-for="child in item.items"
          :key="child.title"
          @click="handleControlAdmin(child.title)"
          :class="renderClassControl(child.title)"
        >
          <v-list-item-content>
            <v-list-item-title v-text="child.title"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import jwt_decode from 'jwt-decode';
import { ADMIN_MENU } from '@/utils/mocks';

export default {
  props: {
    drawer: {
      type: Boolean,
      default: true,
    },
  },
  data: () => ({
    items: ADMIN_MENU,
    activeControl: 'All orders',
  }),
  methods: {
    handleControlAdmin(control) {
      this.activeControl = control;
      this.$emit('changeControl', control);
    },
    renderClassControl(title) {
      return title === this.activeControl ? 'control-admin__active' : '';
    },
  },
  created() {
    const accessToken = localStorage.getItem('accessToken');
    this.username = !accessToken ? null : jwt_decode(accessToken).sub;
  },
};
</script>

<style scoped>
.control-admin__active {
  color: white !important;
  background: #187498;
  border: white 1px dashed;
}
.v-list-item--active .v-list-item__title {
  font-weight: bold !important;
}
</style>
