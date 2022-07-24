<template>
  <m-admin-layout
    :controlList="controlList"
    :roleTitle="roleTitle"
    :title="title"
    @changeControl="handleControlUser"
  >
    <component v-bind:is="controler"></component>
  </m-admin-layout>
</template>

<script>
import MAdminLayout from '@/shared/MAdminLayout.vue';
import UserInformation from '@/components/Forms/User/UserInformation.vue';
import OrdersForm from '@/components/Forms/User/OrdersForm.vue';
import { USER_MENU } from '@/utils/mocks';

export default {
  data() {
    return {
      dialog: false,
      searchKey: '',
      controler: 'user-information',
      controlList: USER_MENU,
      title: 'AccessoriesShop User Profile',
      roleTitle: 'User Profile',
    };
  },
  methods: {
    handleControlUser(value) {
      switch (value) {
        case 'Informations':
          this.controler = 'user-information';
          break;
        case 'Orders':
          this.controler = 'orders-form';
          break;
        default:
          this.controler = 'user-information';
          break;
      }
    },
  },
  components: {
    MAdminLayout,
    UserInformation,
    OrdersForm,
  },
};
</script>

<style scoped>
.scrollable {
  overflow-y: scroll;
}
.v-tab {
  background: #187498;
  color: white !important;
}

.v-tab.v-tab--active {
  background: white;
  color: #187498 !important;
}
</style>
