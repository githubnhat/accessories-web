<template>
  <v-card
    class="product-item mt-1"
    flat
    width="200px"
    height="320px"
    color="white"
    link
    @click="handleClickProduct"
  >
    <v-img width="200px" height="180px" class="mb-2" :src="`${imgLink}`"></v-img>
    <v-card-text>
      <div class="text-product-name mb-2">
        {{ item.productName }}
      </div>

      <div v-if="item.originalQuantity != 0" class="text-product-price primary--text mb-2">
        {{ item.originalPrice }}
      </div>
      <div v-else class="red--text">Hết hàng</div>
      <v-rating v-model="rating" background-color="white" color="yellow darken-3" small></v-rating>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      rating: 5,
      imgLink: undefined,
    };
  },
  created() {
    const defaultLink =
      'https://firebasestorage.googleapis.com/v0/b/minhnhat569-eecaa.appspot.com/o/images%2Fimage-not-found.png?alt=media&token=ae8ed2ef-b7ee-4921-b494-fe662aca6778';
    this.imgLink = this.item.imageLinks[0] ? this.item.imageLinks[0] : defaultLink;
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
