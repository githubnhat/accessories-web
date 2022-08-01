<template>
  <m-layout>
    <v-container>
      <v-card class="mt-10" color="transparent" elevation="0" min-height="100vh">
        <h1 class="primary white--text pa-2 mb-5" @click="goToHome" role="button">
          Accessory Shop | Giỏ hàng
        </h1>
        <div v-if="data[0].id !== '' || data == null">
          <v-list color="transparent" two-line>
            <v-row>
              <v-col cols="12">
                <v-list-item-action>
                  <v-checkbox
                    @click="checkAll"
                    @change="updateCheckall"
                    v-model="isCheckAll"
                    v-show="disabledCheckboxAll"
                    label="Tất cả"
                    color="primary"
                  ></v-checkbox>
                </v-list-item-action>
              </v-col>
              <v-col cols="12">
                <v-list-item
                  class="mb-5 white"
                  v-for="product in data"
                  outlined
                  elevation="1"
                  :key="product.id"
                  avatar
                >
                  <v-row>
                    <v-col class="ma-auto">
                      <v-list-item-action>
                        <v-checkbox
                          v-model="cartSeleted"
                          :value="product.id"
                          @change="updateCheckall"
                          color="primary"
                          v-if="product.disabledCheckbox === true"
                        ></v-checkbox>
                        <h3 v-else class="grey--text">Hết hàng</h3>
                      </v-list-item-action>
                    </v-col>
                    <v-col class="ma-auto">
                      <v-list-item>
                        <img
                          :src="product.thumbnail"
                          class="img-thumbnail mt-2 mb-2"
                          height="100px"
                          width="120px"
                        /> </v-list-item
                    ></v-col>
                    <v-col cols="2" class="ma-auto">
                      <v-list-item
                        ><h3 class="text-truncate">{{ product.productName }}</h3></v-list-item
                      >
                      <v-list-item
                        v-if="product.productVariantName !== null"
                        class="product-variant-name text-truncate"
                        >Phân loại: {{ product.productVariantName }}</v-list-item
                      >
                    </v-col>
                    <v-col class="ma-auto">
                      <v-list-item>
                        <del class="grey--text mr-2">₫{{ product.price }}</del>
                        {{ product.priceDiscount }}
                      </v-list-item>
                    </v-col>
                    <v-col cols="2" class="ma-auto">
                      <v-list-item>
                        <v-text-field
                          :disabled="product.disabledQuantity"
                          v-model="product.quantity"
                          label="Quantity"
                          hide-spin-buttons
                          height="30"
                          @change="handleOnChangeQuantity(product.id)"
                          type="number"
                        >
                          <v-icon
                            :disabled="product.disabledQuantity"
                            slot="append"
                            color="red"
                            @click="append(product.id)"
                          >
                            mdi-plus
                          </v-icon>
                          <v-icon
                            :disabled="product.disabledQuantity"
                            slot="prepend"
                            color="green"
                            @click="prepend(product.id)"
                          >
                            mdi-minus
                          </v-icon>
                        </v-text-field>
                      </v-list-item>
                    </v-col>
                    <v-col class="ma-auto">
                      <v-list-item v-if="product.disabledTotalPrice">
                        {{ product.totalPrice }}
                      </v-list-item>
                    </v-col>
                    <v-col class="ma-auto">
                      <v-btn icon ripple @click="deleteOneItem(product.id)">
                        <v-icon color="red lighten-1">mdi-delete</v-icon>
                      </v-btn>
                    </v-col>
                  </v-row>
                </v-list-item>
              </v-col>
            </v-row>
          </v-list>

          <v-container>
            <v-btn color="error" larger class="mb-2 mr-2" @click="openDeleteDialog"> Xóa </v-btn>
            <v-btn color="success" larger style="float: right" @click="handleCheckout"
              >Go to payment</v-btn
            >
            <div class="action-item-dialog">
              <v-dialog v-model="dialog.delete" max-width="500px">
                <v-card>
                  <ValidationObserver v-slot="{ handleSubmit }">
                    <v-form class="mt-5" @submit.prevent="handleSubmit(deleteListItem)">
                      <v-card-title class="text-h5"
                        >Bạn có chắc chắn <strong class="warning--text">&nbsp;xoá&nbsp;</strong
                        >{{ cartSeleted.length }} sản phẩm?</v-card-title
                      >
                      <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue darken-1" text @click="closeDeleteDialog">Trở lại</v-btn>
                        <v-btn color="warning darken-1" outlined text type="submit">Xóa</v-btn>
                        <v-spacer></v-spacer>
                      </v-card-actions>
                    </v-form>
                  </ValidationObserver>
                </v-card>
              </v-dialog>
            </div>
          </v-container>
        </div>
        <div v-else class="grey--text mt-5">Giỏ hàng trống</div>
      </v-card>
    </v-container>
  </m-layout>
</template>

<script>
import MLayout from '@/shared/MLayout.vue';
import { getListCart, deleteCart, updateCart } from '@/services/user/cart';
import { getProductCombination } from '@/services/user/products';
import { toOneDiscountPrice, formatNumber, totalPrice } from '@/utils';
export default {
  data() {
    return {
      cartSeleted: [],
      isCheckAll: false,
      disabledCheckboxAll: true,
      dialog: {
        delete: false,
      },
      data: [
        {
          id: '',
          thumbnail: '',
          productId: '',
          productName: '',
          priceDiscount: '',
          productVariantName: '',
          discount: 0,
          price: '',
          totalPrice: '',
          quantity: 0,
          disabledCheckbox: true,
          disabledQuantity: false,
          disabledTotalPrice: true,
        },
      ],
    };
  },
  components: {
    MLayout,
  },

  async created() {
    await this.initialize();
  },

  methods: {
    async initialize() {
      const data = await getListCart();
      let countCheck = 0;
      if (data != null) {
        this.data = data;
        this.data?.forEach((e) => {
          e.priceDiscount = toOneDiscountPrice(e.price, e.discount);
          e.price = formatNumber(e.price);
          e.totalPrice = totalPrice(e.price, e.discount, e.quantity);
          e.disabledCheckbox = e.quantity == 0 ? false : true;
          e.disabledQuantity = e.quantity == 0 ? true : false;
          e.disabledTotalPrice = e.quantity == 0 ? false : true;
          if (e.quantity == 0) {
            countCheck++;
          }
          e.thumbnail =
            e.thumbnail !== null
              ? e.thumbnail
              : 'https://firebasestorage.googleapis.com/v0/b/minhnhat569-eecaa.appspot.com/o/images%2Fimage-not-found.png?alt=media&token=ae8ed2ef-b7ee-4921-b494-fe662aca6778';
        });
      }
      if (data != null && countCheck == this.data.length) this.disabledCheckboxAll = false;
      if (data == null) {
        this.data = [
          {
            id: '',
            thumbnail: '',
            productId: '',
            productName: '',
            priceDiscount: '',
            productVariantName: '',
            discount: 0,
            price: '',
            totalPrice: '',
            quantity: 0,
            disabledCheckbox: true,
            disabledQuantity: false,
            disabledTotalPrice: true,
          },
        ];
      }
    },

    async updateCart(cardId) {
      const dataForm = this.data.filter((e) => e.id === cardId);
      if (dataForm[0].quantity > 0) {
        const data = await updateCart({
          id: dataForm[0]?.id,
          quantity: dataForm[0]?.quantity,
        });
        if (data) {
          await this.initialize();
        }
      }
    },

    async append(cardId) {
      const check = this.data.filter((e) => e.id == cardId);

      const data = await getProductCombination({
        productId: check[0].productId,
        productVariantName:
          check[0].productVariantName != null ? check[0].productVariantName : null,
      });
      this.data.forEach((e) => {
        if (e.id === cardId && e.quantity < data?.quantity) {
          e.quantity++;
          e.totalPrice = totalPrice(e.price, e.discount, e.quantity);
        }
      });
      await this.updateCart(cardId);
    },
    async prepend(cardId) {
      // let check;
      this.data.forEach((e) => {
        if (e.id === cardId && e.quantity > 1) {
          e.quantity--;
          e.totalPrice = totalPrice(e.price, e.discount, e.quantity);
          // check = e.quantity;
        }
      });
      await this.updateCart(cardId);
    },
    async handleOnChangeQuantity(cardId) {
      this.data.forEach((e) => {
        if ((e.id === cardId, e.quantity == 0)) {
          e.quantity = 1;
        }
      });
      await this.updateCart(cardId);
    },

    async deleteOneItem(cartId) {
      const isUpdateSuccess = await deleteCart([cartId]);
      if (isUpdateSuccess) {
        await this.initialize();
        if (this.isCheckAll) {
          this.checkAll();
        }
      }
    },

    async deleteListItem() {
      const isUpdateSuccess = await deleteCart(this.cartSeleted);
      if (isUpdateSuccess) {
        await this.initialize();
      }
      this.closeDeleteDialog();
    },
    openDeleteDialog() {
      if (this.cartSeleted.length > 0) {
        this.dialog.delete = true;
      } else {
        alert('Vui lòng chọn sản phẩm!');
      }
    },

    closeDeleteDialog() {
      this.dialog.delete = false;
    },

    checkAll() {
      this.isCheckAll = !this.isCheckAll;
      this.cartSeleted = [];
      if (this.isCheckAll) {
        this.data.forEach((e) => {
          if (e.disabledCheckbox) {
            this.cartSeleted.push(e.id);
          }
        });
      }
    },
    updateCheckall() {
      let count = 0;
      this.data.forEach((e) => {
        if (e.disabledCheckbox) {
          count++;
        }
      });
      if (this.cartSeleted.length == count && count != 0) {
        this.isCheckAll = true;
      } else {
        this.isCheckAll = false;
      }
    },

    handleCheckout() {
      if (this.cartSeleted.length > 0) {
        const selectedItems = this.data.filter((item) => this.cartSeleted.indexOf(item.id) !== -1);
        this.$router.push({
          name: 'checkout',
          params: {
            checkoutItems: selectedItems,
          },
        });
      } else {
        alert('Chọn ít nhất một sản phẩm để thanh toán');
      }
    },
    goToHome() {
      this.$route.push({ name: 'home' });
    },
  },
};
</script>

<style scoped>
.price-text {
  color: #000000;
}
.product-name {
  width: 200px;
  height: 60px;
  overflow: hidden;
}
.product-variant-name {
  width: 150px;
  height: 40px;
  overflow: hidden;
}
</style>
