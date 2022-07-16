<template>
  <v-card
    class="product-item mt-1"
    flat
    width="200px"
    height="300px"
    color="white"
    link
    @click="handleClickProduct"
  >
    <v-img
      width="200px"
      height="180px"
      class="mb-2"
      src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
    ></v-img>
    <v-card-text>
      <div class="text-product-name mb-2">
        {{ item.productName }}
      </div>

      <div class="text-product-price primary--text mb-2">
        <span class="currency">₫</span>
        {{ item.originalPrice }}
      </div>
      <v-rating v-model="rating" background-color="white" color="yellow darken-3" small></v-rating>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      rating: 5,
    };
  },
  props: {
    item: {
      type: Object,
      default: () => ({}),
    },
  },
  methods: {
    handleClickProduct() {
      console.log('click');
      this.$router.push('/detail/' + this.item.id);
    },
  },
  computed: {
    link() {
      return '/detail/' + this.item.id;
    },
  },
};
</script>

<style lang="scss" scoped>
.text-product-name {
  word-wrap: break-word;
  white-space: normal;
  overflow: hidden;
  display: -webkit-box;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-height: 14px;
  font-size: 0.75rem;
}
.text-product-price {
  font-size: 1.05rem;
}
.currency {
  font-size: 0.5rem;
}
.product-item:hover {
  margin-top: 0 !important;
  border: 2px solid #187498 !important;
  transition: 0.3s;
}
</style>
