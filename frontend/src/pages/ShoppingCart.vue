<template>
  <v-container>
    <h2 class="display-2 mb-4">Giỏ hàng</h2>

    <v-list two-line>
      <template v-for="(product, index) in data">
        <v-list-tile :key="product.product_id" avatar>
          <v-list-tile-avatar>
            <img :src="product.image" />
          </v-list-tile-avatar>

          <v-list-tile-content>
            <v-list-tile-productName v-html="product.productName"></v-list-tile-productName>
            <v-list-tile-sub-productName v-html="product.description"></v-list-tile-sub-productName>
          </v-list-tile-content>

          <v-list-tile> {{ product.price }}$ </v-list-tile>

          <v-list-tile-action>
            <v-text-field label="Quantity" reverse :value="product.quantity"></v-text-field>
          </v-list-tile-action>

          <v-list-tile> {{ product.price * product.quantity }}$ </v-list-tile>

          <v-list-tile-action>
            <v-btn icon ripple>
              <v-icon color="red lighten-1">delete</v-icon>
            </v-btn>
          </v-list-tile-action>
        </v-list-tile>

        <v-divider v-if="index + 1 < data.length" :key="index"></v-divider>
      </template>
    </v-list>

    <v-container>
      <v-btn color="success" larger style="float: right">Go to payment</v-btn>
    </v-container>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    data: [
      {
        product_id: 'kitchen-1',
        image: 'https://cdn.vuetifyjs.com/images/cards/kitchen.png',
        productName: 'QW cooking utensils',
        description: 'Our vintage kitchen utenils delight any chef. Made of bamboo by hand',
        price: 14.99,
        quantity: 1,
      },
      {
        product_id: 'kitchen-1',
        image: 'https://cdn.vuetifyjs.com/images/cards/kitchen.png',
        productName: 'QW cooking utensils',
        description: 'Our vintage kitchen utenils delight any chef. Made of bamboo by hand',
        price: 44.99,
        quantity: 2,
      },
      {
        product_id: 'kitchen-1',
        image: 'https://cdn.vuetifyjs.com/images/cards/kitchen.png',
        productName: 'QW cooking utensils',
        description: 'Our vintage kitchen utenils delight any chef. Made of bamboo by hand',
        price: 64.99,
        quantity: 2,
      },
    ],
  }),
};
</script>

<style scoped>
</style>