<template>
  <v-navigation-drawer :value="drawer" app>
    <v-sheet color="primary lighten-2" class="pa-4">
      <div>
        <v-row>
          <v-col cols="3" class="d-flex align-self-center">
            <v-icon size="48">mdi-ballot</v-icon>
          </v-col>
          <v-col cols="9">
            <span class="white--text font-weight-bold title"> {{ title }}</span>
          </v-col>
        </v-row>
      </div>
    </v-sheet>

    <v-divider></v-divider>

    <v-list class="pa-0">
      <v-list-group
        v-for="item in controlList"
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
import { mapState } from 'vuex';

export default {
  props: {
    title: {
      type: String,
      default: 'AccessoriesShop Administration ',
    },
    controlList: {
      type: Array,
      default: () => [],
    },
    drawer: {
      type: Boolean,
      default: true,
    },
  },
  data: () => ({
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
  computed: {
    ...mapState({
      accessToken: (state) => state._accessToken.state.accessToken,
    }),
  },
  created() {
    this.username = !this.accessToken ? null : jwt_decode(this.accessToken).fullName;
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
