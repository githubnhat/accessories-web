<template>
  <m-layout>
    <h1>{{ namePage }}</h1>

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

import { getProductByBrand, getProductByCategory } from '@/services/user/products';
export default {
  data() {
    return {
      namePage: '',
      products: [],
      code: '',
      page: 1,
      itemPerPage: 12,
      totalPage: 1,
    };
  },

  watch: {
    $route: {
      deep: true,
      handler() {
        this.namePage = this.$route.name === 'brand' ? 'Brand: ' : 'Category: ';
        this.code = this.$route.params.code;
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

  methods: {
    async loadProductFromAPI() {
      if (this.$route.name === 'brand') {
        const data = await getProductByBrand(this.page, this.itemPerPage, this.code);
        this.products = data?.data;
        this.totalPage = data?.totalPages;
      } else if (this.$route.name === 'category') {
        const data = await getProductByCategory(this.page, this.itemPerPage, this.code);
        this.products = data?.data;
        this.totalPage = data?.totalPages;
      }
      console.log('products', this.products);
    },
  },
  components: {
    MLayout,
    ProductItem,
  },
};
</script>

<style></style>
