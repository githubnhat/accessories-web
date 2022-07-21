<template>
  <v-container class="payment-container">
    <h1 class="primary white--text pa-2 mb-5" @click="goToHome" role="button">
      Accessory Shop | Payment
    </h1>
    <v-card flat color="white" class="pa-2" rounded="0" min-width="60vw" min-height="80vh">
      <v-card-title>
        <v-row dense align-content="center" v-show="!isAnotherAddress">
          <v-col cols="3">
            <h4 class="text-right pa-3">Thông tin giao hàng:</h4>
          </v-col>
          <v-col cols="6">
            <v-select
              v-model="selectedAddress"
              :items="this.addresses"
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
        <v-row dense align-content="center" v-show="isAnotherAddress">
          <v-col cols="3"> <h4 class="text-right pa-3">Thông tin giao hàng mới:</h4> </v-col>
          <v-col cols="4">
            <validation-provider name="Địa chỉ" rules="required|min:15" v-slot="{ errors }">
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
          </v-col>
        </v-row>
      </v-card-title>
    </v-card>
  </v-container>
</template>

<script>
import router from '@/router';
import { getAddresses } from '@/services/user/accounts';
export default {
  data() {
    return {
      isAnotherAddress: false,
      selectedAddress: {},
      addresses: [],
      newAddress: '',
      newPhone: '',
      loading: false,
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.readAddressFromAPI();
    },
    async readAddressFromAPI() {
      this.loading = true;
      const { data } = await getAddresses();

      this.addresses = data?.data;
      console.log('data address: ', this.addresses);
      this.selectedAddress = this.addresses[0];

      this.loading = false;
    },

    goToHome() {
      router.push({ name: 'home' });
    },
  },
};
</script>

<style lang="scss" scoped>
.payment-container {
  background: #f0f0f0;
}
</style>
