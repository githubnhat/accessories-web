<template>
  <m-layout>
    <div>
      <v-container>
        <v-card class="mt-10" min-height="100vh">
          <v-row class="ml-5">
            <v-col cols="12" sm="5">
              <v-card elevation="24" max-width="400" class="mx-auto mt-3">
                <v-carousel
                  cycle
                  hide-delimiter-background
                  show-arrows-on-hover
                  delimiter-icon="mdi-minus"
                  height="500"
                >
                  <v-carousel-item
                    v-for="(listImage, i) in listImages"
                    :key="i"
                    :src="listImage"
                    reverse-transition="fade-transition"
                    transition="fade-transition"
                  >
                  </v-carousel-item>
                </v-carousel>
              </v-card>
            </v-col>
            <v-col cols="12" sm="7">
              <h1 class="grey--text text--darken-3 mt-5">{{ this.data?.productName }}</h1>
              <v-row>
                <v-col class="price-text" cols="7">
                  <div class="">
                    <h2>{{ price }}</h2>
                  </div>
                </v-col>
                <v-col cols="6">
                  <div>
                    <span class="discount-card">{{ this.data?.discount }}% Giảm</span>
                  </div>
                </v-col>
                <v-col v-for="(attribute, a) in attributes" :key="a" cols="12" sm="8">
                  <v-row>
                    <v-col cols="4">
                      <h3 class="mt-4">{{ attribute?.attributeName }}:</h3>
                    </v-col>
                    <v-col cols="8">
                      <div class="mt-3">
                        <v-btn
                          :color="variantName.color"
                          class="mr-2 mt-1"
                          @click="addItem(attribute?.attributeName, variantName)"
                          v-for="(variantName, v) in attribute?.variantNames"
                          :key="v"
                        >
                          {{ variantName.variantName }}
                        </v-btn>
                      </div>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col cols="12" sm="8">
                  <v-row>
                    <v-col cols="4">
                      <v-text-field
                        hide-spin-buttons
                        :value="quantityInput"
                        height="30"
                        @change="handleOnChangeQuantity"
                        type="number"
                      >
                        <v-icon slot="append" color="red" @click="append"> mdi-plus </v-icon>
                        <v-icon slot="prepend" color="green" @click="prepend"> mdi-minus </v-icon>
                      </v-text-field>
                    </v-col>
                    <v-col cols="8">
                      <div class="mt-5 grey--text">
                        {{ quantity }}
                        <span> Sản phẩm có sẵn. </span>
                      </div>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col cols="12" sm="8">
                  <v-btn class="ma-2" outlined color="deep-orange accent-3">
                    Thêm vào giỏ hàng
                  </v-btn>
                  <v-btn color="deep-orange accent-3" class="mr-2 ma-2 white--text">
                    Mua ngay
                  </v-btn>
                </v-col>
              </v-row>
            </v-col>
          </v-row>
          <v-divider class="mt-2"> </v-divider>
          <v-row class="ml-5">
            <v-col cols="12">
              <h2 class="mt-2 grey--text">Mô tả sản phẩm</h2>
            </v-col>
            <v-col cols="12">
              <span class="grey--text mt-5"> {{ this.data?.description }} </span>
            </v-col>
          </v-row>
        </v-card>
      </v-container>
    </div>
  </m-layout>
</template>

<script>
import MLayout from '@/shared/MLayout.vue';
import { getProductDetail, getProductCombination } from '@/services/user/products';

export default {
  data() {
    return {
      data: {},
      attributes: [],
      listAttrAndVari: [],
      listImages: [],
      price: 0,
      quantity: 0,
      quantityInput: 1,
    };
  },
  created() {
    this.initialize();
  },
  components: {
    MLayout,
  },
  methods: {
    // get data
    async initialize() {
      const id = this.$route.params.id;
      this.data = await getProductDetail(id);
      this.listImages = this.data?.imageLinks;
      this.data?.attributeAndVariants.forEach((i) => {
        let variantNames = [];
        i?.variantNames.forEach((v) => {
          variantNames.push({
            variantName: v,
            color: '',
          });
        });

        this.attributes.push({
          attributeId: i.attributeId,
          attributeName: i.attributeName,
          variantNames: variantNames,
        });
      });
      this.price = this.data?.originalPrice;
      this.quantity = this.data?.originalQuantity;
    },
    addItem(attrName, variantName) {
      this.tranColorBtn(attrName, variantName);
      const item = {
        attributeName: attrName,
        variantName: variantName.variantName,
      };

      let itemRemove = {
        attributeName: null,
        variantName: null,
      };
      if (this.listAttrAndVari.length > 0) {
        this.listAttrAndVari.forEach((a) => {
          if (item.attributeName === a.attributeName) {
            itemRemove = a;
          }
        });
        if (itemRemove.variantName == item.variantName) {
          this.listAttrAndVari = this.listAttrAndVari.filter((i) => i !== itemRemove);
        } else {
          this.listAttrAndVari = this.listAttrAndVari.filter((i) => i !== itemRemove);
          this.listAttrAndVari.push(item);
        }
      } else {
        this.listAttrAndVari.push(item);
      }

      if (this.listAttrAndVari.length === this.attributes.length) {
        let productVariantName = '';
        this.listAttrAndVari.forEach((item, index) => {
          if (index > 0) {
            productVariantName = productVariantName + '-' + item.variantName;
          } else {
            productVariantName = item.variantName;
          }
        });
        const id = this.$route.params.id;
        this.readDataFromAPI(id, productVariantName);
      }
      if (this.listAttrAndVari.length < this.attributes.length) {
        this.price = this.data?.originalPrice;
        this.quantity = this.data?.originalQuantity;
      }
    },

    async readDataFromAPI(id, productVariantName) {
      const { price, quantity } = await getProductCombination({
        productId: id,
        productVariantName: productVariantName,
      });
      this.price = price;
      this.quantity = quantity;
      if (this.quantityInput > this.quantity) {
        this.quantityInput = this.quantity;
      }
    },

    tranColorBtn(attrName, variantName) {
      this.attributes.forEach((a) => {
        if (a.attributeName == attrName) {
          a.variantNames.forEach((v) => {
            if (v.variantName == variantName.variantName) {
              if (v.color != 'primary') {
                v.color = 'primary';
                a.variantNames.forEach((t) => {
                  if (t != v) {
                    t.color = '';
                  }
                });
              } else {
                v.color = '';
              }
            }
          });
        }
      });
    },

    append() {
      if (this.quantityInput < this.quantity) {
        this.quantityInput++;
      }
    },
    prepend() {
      if (this.quantityInput > 1) {
        this.quantityInput--;
      }
    },
    handleOnChangeQuantity(value) {
      if (value <= this.quantity) {
        this.quantityInput = value;
      } else {
        this.quantityInput = this.quantity;
      }
    },
  },
};
</script>

<style>
.price-text {
  color: #ff3d00;
}
.discount-card {
  padding: 5px 10px;
  background-color: #ff3d00;
  color: #ffffff;
  font-weight: 600;
  border-radius: 3px;
}
</style>
