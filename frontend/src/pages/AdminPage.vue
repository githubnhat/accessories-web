<template>
  <m-admin-layout>
    <v-card height="671" class="scrollable admin-page" flat rounded="0">
      <v-tabs vertical>
        <v-tab>
          <v-icon left> mdi-cart </v-icon>
          Sản phẩm
        </v-tab>
        <v-tab>
          <v-icon left> mdi-order-bool-ascending-variant </v-icon>
          Đơn hàng
        </v-tab>
        <v-tab>
          <v-icon left> mdi-account </v-icon>
          Tài khoản
        </v-tab>
        <!-- tab product -->
        <v-tab-item>
          <v-card flat>
            <v-row class="pa-2">
              <v-col cols="4" class="d-flex pl-10">
                <v-text-field
                  v-model="searchKey"
                  placeholder="Tìm sản phẩm"
                  outlined
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="1">
                <v-btn color="primary" icon dark> <v-icon dark>mdi-magnify</v-icon></v-btn>
              </v-col>
              <v-col cols="5"></v-col>
              <v-col cols="2">
                <v-btn color="primary" dark to="/add-product"> Thêm sản phẩm </v-btn>
              </v-col>
            </v-row>
            <v-row class="pa-2">
              <v-col cols="12"> </v-col>
            </v-row>
          </v-card>
        </v-tab-item>
        <!-- end tab product -->

        <v-tab-item>
          <v-card flat>
            <v-card-text>
              <p>
                Morbi nec metus. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante
                convallis tellus, vitae iaculis lacus elit id tortor. Sed mollis, eros et ultrices
                tempus, mauris ipsum aliquam libero, non adipiscing dolor urna a orci. Curabitur
                ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Nunc sed
                turpis.
              </p>

              <p>
                Suspendisse feugiat. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante
                convallis tellus, vitae iaculis lacus elit id tortor. Proin viverra, ligula sit amet
                ultrices semper, ligula arcu tristique sapien, a accumsan nisi mauris ac eros. In
                hac habitasse platea dictumst. Fusce ac felis sit amet ligula pharetra condimentum.
              </p>

              <p>
                Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida
                magna mi a libero. Nam commodo suscipit quam. In consectetuer turpis ut velit. Sed
                cursus turpis vitae tortor. Aliquam eu nunc.
              </p>

              <p>
                Etiam ut purus mattis mauris sodales aliquam. Ut varius tincidunt libero. Aenean
                viverra rhoncus pede. Duis leo. Fusce fermentum odio nec arcu.
              </p>

              <p class="mb-0">
                Donec venenatis vulputate lorem. Aenean viverra rhoncus pede. In dui magna, posuere
                eget, vestibulum et, tempor auctor, justo. Fusce commodo aliquam arcu. Suspendisse
                enim turpis, dictum sed, iaculis a, condimentum nec, nisi.
              </p>
            </v-card-text>
          </v-card>
        </v-tab-item>
        <v-tab-item>
          <v-card flat>
            <v-card-text>
              <p>
                Fusce a quam. Phasellus nec sem in justo pellentesque facilisis. Nam eget dui. Proin
                viverra, ligula sit amet ultrices semper, ligula arcu tristique sapien, a accumsan
                nisi mauris ac eros. In dui magna, posuere eget, vestibulum et, tempor auctor,
                justo.
              </p>

              <p class="mb-0">
                Cras sagittis. Phasellus nec sem in justo pellentesque facilisis. Proin sapien
                ipsum, porta a, auctor quis, euismod ut, mi. Donec quam felis, ultricies nec,
                pellentesque eu, pretium quis, sem. Nam at tortor in tellus interdum sagittis.
              </p>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs>
    </v-card>
  </m-admin-layout>
</template>

<script>
import MAdminLayout from '@/shared/MAdminLayout.vue';
export default {
  data() {
    return {
      dialog: false,
      searchKey: '',
    };
  },
  components: {
    MAdminLayout,
  },
  created() {
    // authentication
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
