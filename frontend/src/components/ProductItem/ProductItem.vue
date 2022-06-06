<template>
  <v-card
    class="ma-3"
    flat
    width="200px"
    height="300px"
    color="blue"
    rounded="lg"
    link
    @click="handleClickProduct"
  >
    <v-img height="200" src="https://cdn.vuetifyjs.com/images/cards/cooking.png"></v-img>
    <div class="w-100 text-left px-5 title">{{ item.brand }}</div>
    <div class="w-100 text-left px-5 subtitle-1">{{ item.productName }}</div>
    <div class="w-100 text-left px-5 subtitle-1">{{ item.price }}</div>
  </v-card>
</template>

<script>
export default {
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

<style></style>
