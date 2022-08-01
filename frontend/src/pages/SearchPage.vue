<template>
  <m-layout>
    <h1>Có {{ totalItem }} sản phẩm tìm được với từ khoá: "{{ keyword }}"</h1>

    <v-card
      v-if="products.length > 0"
      flat
      width="100%"
      class="d-flex flex-wrap mb-10 px-16"
      color="blue-grey lighten-5"
    >
      <v-col cols="2" v-for="(item, index) in products" :key="index">
        <product-item :item="item"></product-item>
      </v-col>
      <v-col cols="12">
        <div class="text-center">
          <v-pagination v-model="page" :length="totalPage" circle></v-pagination>
        </div>
      </v-col>
    </v-card>
    <h2 v-else>Không có sản phẩm nào</h2>
  </m-layout>
</template>

<script>
import MLayout from '@/shared/MLayout.vue';
import ProductItem from '@/components/ProductItem/ProductItem.vue';

import { searchProduct } from '@/services/user/products';
export default {
  data() {
    return {
      namePage: '',
      products: [],
      keyword: '',
      page: 1,
      itemPerPage: 12,
      totalPage: 1,
      totalItem: 0,
    };
  },

  watch: {
    $route: {
      deep: true,
      handler() {
        this.keyword = this.$route.params.keyword;
        this.loadProductFromAPI();
      },
    },
    page: {
      deep: true,
      handler() {
        this.loadProductFromAPI();
      },
    },
  },
  created() {
    this.keyword = this.$route.params.keyword;
    this.loadProductFromAPI();
  },

  methods: {
    async loadProductFromAPI() {
      const data = await searchProduct(this.page, this.itemPerPage, this.keyword);
      this.products = data?.data;
      this.totalPage = data?.totalPages;
      this.totalItem = data?.totalItems;
    },
  },
  components: {
    MLayout,
    ProductItem,
  },
};
</script>

<style></style>
