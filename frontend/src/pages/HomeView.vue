<template>
  <m-layout>
    <m-slide-banners></m-slide-banners>
    <v-divider></v-divider>
    <v-card flat width="100%" class="d-flex flex-wrap mb-10 px-16" color="blue-grey lighten-5">
      <v-col cols="2" v-for="(item, index) in products" :key="index">
        <product-item :item="item"></product-item>
      </v-col>
    </v-card>
  </m-layout>
</template>

<script>
import MLayout from '@/shared/MLayout.vue';
import MSlideBanners from '@/components/Slide/MSlideBanners.vue';
import ProductItem from '@/components/ProductItem/ProductItem.vue';
import { PRODUCTS } from '@/utils/mocks';
import { getHomePageProduct } from '@/services/user/products';

export default {
  components: {
    MLayout,
    MSlideBanners,
    ProductItem,
  },
  data() {
    return {
      products: PRODUCTS,
    };
  },
  async created() {
    // get products from api
    const data = await getHomePageProduct();
    this.products = data?.data;
  },
};
</script>

<style></style>
