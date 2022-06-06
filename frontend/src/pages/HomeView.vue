<template>
  <m-layout>
    <m-slide-banners></m-slide-banners>
    <v-divider></v-divider>
    <v-card flat width="100%" class="d-flex justify-space-between mb-10" color="transparent">
      <v-col cols="2" v-for="(item, index) in products" :key="index" class="d-flex justify-center">
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
  computed: {},
};
</script>

<style></style>
