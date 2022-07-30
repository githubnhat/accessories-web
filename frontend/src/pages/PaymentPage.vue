<template>
  <div class="payment">
    <v-container class="payment-container">
      <h1 class="primary white--text pa-2 mb-5" @click="goToHome" role="button">
        Accessory Shop | Thanh toán
      </h1>
      <v-card flat color="white" class="pa-2" rounded="0" min-width="60vw" min-height="80vh">
        <ValidationObserver v-slot="{ handleSubmit }">
          <v-form class="mt-5" @submit.prevent="handleSubmit(handleCheckout)">
            <v-card-title>
              <v-row dense align-content="center" v-if="!isAnotherAddress && !!addresses">
                <v-col cols="3">
                  <h4 class="text-right pa-3">Thông tin giao hàng:</h4>
                </v-col>
                <v-col cols="6">
                  <v-select
                    v-model="selectedAddress"
                    :items="addresses"
                    :loading="loading"
                    label="Chọn địa chỉ"
                    outlined
                  >
                    <template v-slot:selection="{ item }">
                      <!-- HTML that describe how select should render selected items -->
                      <v-chip v-if="item.isMainAddress" color="primary">
                        <span>Địa chỉ mặc định: {{ item.address }} </span>
                        <span> (Số điện thoại: {{ item.phone }})</span>
                      </v-chip>
                      <v-chip v-else color="background">
                        <span>Địa chỉ: {{ item.address }}</span>
                        <span> (Số điện thoại: {{ item.phone }})</span>
                      </v-chip>
                    </template>
                    <template v-slot:item="{ item }">
                      <!-- HTML that describe how select should render items when the select is open -->
                      <v-chip v-if="item.isMainAddress" color="primary">
                        <span>Địa chỉ mặc định: {{ item.address }}</span>
                        <span> (Số điện thoại: {{ item.phone }}) </span>
                      </v-chip>
                      <v-chip v-else color="background">
                        <span>Địa chỉ: {{ item.address }}</span>
                        <span> (Số điện thoại: {{ item.phone }})</span>
                      </v-chip>
                    </template>
                  </v-select>
                </v-col>
                <v-col cols="3">
                  <v-checkbox
                    v-model="isAnotherAddress"
                    hide-details
                    label="Thêm địa chỉ khác"
                  ></v-checkbox>
                </v-col>
              </v-row>
              <v-row dense align-content="center" v-if="isAnotherAddress">
                <v-col cols="3"> <h4 class="text-right pa-3">Thông tin giao hàng mới:</h4> </v-col>
                <v-col cols="4">
                  <validation-provider name="Địa chỉ" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="newAddress"
                      :disabled="!isAnotherAddress"
                      label="Địa chỉ"
                      :error-messages="errors"
                    ></v-text-field>
                  </validation-provider>
                </v-col>

                <v-col cols="2">
                  <validation-provider
                    name="Số điện thoại"
                    rules="required|min:10|numeric"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      v-model="newPhone"
                      :disabled="!isAnotherAddress"
                      label="Số điện thoại"
                      :error-messages="errors"
                    ></v-text-field>
                  </validation-provider>
                </v-col>
                <v-col cols="3">
                  <v-checkbox
                    v-model="isAnotherAddress"
                    hide-details
                    label="Thêm địa chỉ khác"
                  ></v-checkbox>
                  <v-checkbox
                    v-model="isMainAddress"
                    :readonly="!!!addresses"
                    hide-details
                    label="Đặt làm địa chỉ mặc định"
                  ></v-checkbox>
                </v-col>
              </v-row>
            </v-card-title>

            <v-card-text>
              <!-- for list checkout items:=> product in data  -->
              <v-list color="transparent" two-line>
                <v-row>
                  <v-col cols="12">
                    <v-list-item
                      class="mb-5 white"
                      v-for="(product, index) in checkoutItems"
                      outlined
                      elevation="1"
                      :key="product.id"
                      avatar
                    >
                      <v-card elevation="0" rounded="md" width="100%" height="130px" outlined>
                        <v-row>
                          <v-col class="ma-auto">
                            <h5 class="pa-3">
                              <span>STT: {{ ++index }}</span>
                            </h5>
                          </v-col>
                          <v-col class="ma-auto">
                            <v-list-item>
                              <img
                                :src="product.thumbnail"
                                class="img-thumbnail mt-2 mb-2"
                                height="100px"
                              /> </v-list-item
                          ></v-col>
                          <v-col cols="2" class="ma-auto">
                            <v-list-item
                              ><h3 class="product-name">{{ product.productName }}</h3></v-list-item
                            >
                            <v-list-item class="product-variant-name"
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
                                readonly
                              >
                              </v-text-field>
                            </v-list-item>
                          </v-col>
                          <v-col class="ma-auto">
                            <v-list-item v-if="product.disabledTotalPrice">
                              {{ product.totalPrice }}
                            </v-list-item>
                          </v-col>
                        </v-row>
                      </v-card>
                    </v-list-item>
                  </v-col>
                </v-row>
              </v-list>
            </v-card-text>

            <v-card-actions>
              <v-row dense align-content="center">
                <v-col cols="6"></v-col>
                <v-col cols="3">
                  <h2 class="text-right"><span>Phí vận chuyển:</span></h2>
                </v-col>
                <v-col cols="3">
                  <span class="pt-4 title primary--text">{{ shippingFee }} VNĐ</span></v-col
                >
              </v-row>
            </v-card-actions>
            <v-card-actions>
              <v-row dense align-content="center">
                <v-col cols="6"></v-col>
                <v-col cols="3">
                  <h2 class="text-right"><span>Tổng hóa đơn:</span></h2>
                </v-col>
                <v-col cols="3"
                  ><span class="pt-4 title error--text"> {{ totalBill }} </span></v-col
                >
              </v-row>
            </v-card-actions>
            <v-card-actions>
              <v-row dense>
                <v-col cols="9"></v-col>
                <v-col cols="3">
                  <v-btn color="primary" @click="handleCheckout" x-large>Thanh toán</v-btn>
                </v-col>
              </v-row>
            </v-card-actions>
          </v-form>
        </ValidationObserver>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import router from '@/router';
import { getAddresses, insertAddress } from '@/services/user/accounts';
import { toPriceValue, toPriceString } from '@/utils/index';
import { insertOrder } from '@/services/user/orders';
export default {
  data() {
    return {
      checkoutItems: [],
      isAnotherAddress: false,
      isMainAddress: false,
      selectedAddress: {},
      addresses: [],
      newAddress: '',
      newPhone: '',
      loading: false,
      shippingFee: '30.000',
      payload: {},
      orderResponse: undefined,
    };
  },
  created() {
    this.init();
    this.checkoutItems = this.$route.params?.checkoutItems;
  },
  computed: {
    totalBill() {
      let result = this.checkoutItems.reduce((sum, item) => {
        sum += toPriceValue(item.totalPrice);
        return sum;
      }, 0);
      const shippingFee = 30000;
      result += shippingFee;
      return toPriceString(result);
    },
  },
  methods: {
    init() {
      this.readAddressFromAPI();
    },
    async readAddressFromAPI() {
      this.loading = true;
      const { data } = await getAddresses();

      this.addresses = data?.data;
      if (this.addresses) {
        this.selectedAddress = this.addresses[0];
      } else {
        this.addresses = [];
        this.isAnotherAddress = true;
        this.isMainAddress = true;
      }
      this.loading = false;
    },

    async handleCheckout() {
      this.loading = true;

      const addressPayload = this.isAnotherAddress ? this.newAddress : this.selectedAddress.address;
      const phonePayload = this.isAnotherAddress ? this.newPhone : this.selectedAddress.phone;

      if (this.isAnotherAddress) {
        try {
          const addressRequest = {
            address: addressPayload,
            phone: phonePayload,
            isMainAddress: this.isMainAddress,
          };
          const addressResponse = await insertAddress(addressRequest);
          console.log('address process', addressResponse);
        } catch (error) {
          console.log('error: ' + error);
          alert('Lỗi hệ thống! Vui lòng thử lại...');
          return;
        }
      }
      const orderItemsPayload = this.checkoutItems.map((item) => ({
        productId: item.productId,
        price: parseFloat(toPriceValue(item.priceDiscount)),
        productCombination: item.productVariantName,
        quantity: item.quantity,
        discount: item.discount,
        cartId: item.id,
      }));
      this.payload = {
        address: addressPayload,
        phone: phonePayload,
        totalBill: this.totalBill,
        orderItems: orderItemsPayload,
      };
      try {
        this.orderResponse = await insertOrder(this.payload);

        alert('Đặt hàng thành công!');
        this.goToHome();
      } catch (error) {
        console.log('error: ' + error);
        alert('Lỗi hệ thống! Vui lòng thử lại...');
      }

      // console.log('payload', this.payload);
      this.loading = false;
    },

    goToHome() {
      router.push({ name: 'home' });
    },
  },
};
</script>

<style lang="scss" scoped>
.payment {
  background: #eceff1;
  min-height: 100vh;
}
</style>
