<template>
  <div>
    <div class="display-1 text-center mb-3">Đơn đặt hàng của bạn - Orders</div>
    <v-overlay :value="loading.info">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
    <v-data-table
      :page="page"
      :pageCount="totalPages"
      :server-items-length="totalItems"
      :headers="headers"
      :options.sync="options"
      :items="data"
      :loading="loading.table"
      class="elevation-1"
      :footer-props="{
        showFirstLastPage: true,
        showCurrentPage: true,
      }"
    >
      <template v-slot:top="{ items }">
        <div class="pa-4">
          <v-badge
            :content="items.length"
            class="mr-3"
            :value="items.length"
            color="primary"
            overlap
          >
            <v-chip color="primary" label outlined large> Tổng đơn </v-chip>
          </v-badge>

          <v-badge :content="totalPendingOrder" :value="totalPendingOrder" color="primary" overlap>
            <v-chip color="info" label outlined large> Chờ xác nhận </v-chip>
          </v-badge>
        </div>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="openInfoDialog(item)"> mdi-information </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
    <div class="action-item-dialog">
      <v-dialog v-model="dialog.info" max-width="1200px">
        <v-card>
          <v-card-title class="text-h5">Chi tiết đơn đặt hàng {{ selectedOrder.id }}</v-card-title>
          <v-card-text>
            <v-row no-gutters dense>
              <v-col cols="12">
                <v-list-item
                  class="mb-5 white"
                  v-for="(product, index) in selectedOrder.orderItems"
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
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeInfoDialog">Trở lại</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>
<script>
import { getAllOrders, getInforOrder } from '@/services/user/orders';
export default {
  data: () => ({
    page: 0,
    totalItems: 0,
    totalPages: 0,
    options: {
      page: 1,
      itemsPerPage: 5,
    },
    loading: {
      table: true,
      info: false,
    },
    dialog: {
      info: false,
    },
    headers: [
      {
        text: 'ID',
        value: 'id',
      },
      {
        text: 'Địa chỉ',
        value: 'address',
      },
      {
        text: 'Điện thoại',
        value: 'phone',
      },
      {
        text: 'Trạng thái',
        value: 'status',
      },
      {
        text: 'Thời điểm đặt hàng',
        value: 'insertDate',
      },
      {
        text: 'Tổng hóa đơn',
        value: 'totalBill',
      },
      {
        text: 'Hành động',
        value: 'actions',
        sortable: false,
      },
    ],
    data: [],
    selectedOrder: {},
  }),

  computed: {
    totalPendingOrder() {
      return this.data.reduce((result, item) => {
        if (item.status === 'Chờ xác nhận') result++;
        return result;
      }, 0);
    },
  },

  watch: {
    options: {
      handler() {
        this.readDataFromAPI();
      },
    },
    deep: true,
  },

  async created() {
    await this.initialize();
  },

  methods: {
    async initialize() {
      await this.readDataFromAPI();
    },

    async readDataFromAPI() {
      this.loading.table = true;
      const { page, itemsPerPage } = this.options;
      const { data } = await getAllOrders(page, itemsPerPage);

      this.page = data?.data?.page;
      this.totalPages = data?.data?.totalPages;
      this.totalItems = data?.data?.totalItems;
      this.data = data?.data?.data;

      // console.log('data', data);
      // console.log('this.data', this.data);
      this.loading.table = false;
    },

    async openInfoDialog(item) {
      this.loading.info = true;
      const orderId = item?.id;
      const orderResponse = await getInforOrder(orderId);
      this.selectedOrder = orderResponse?.data?.data;
      console.log('selectedOrder', this.selectedOrder);

      this.dialog.info = true;
      this.loading.info = false;
    },

    closeInfoDialog() {
      this.dialog.info = false;
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
