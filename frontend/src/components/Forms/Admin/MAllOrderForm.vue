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
      multi-sort
      :loading="loading.table"
      class="elevation-1"
      :footer-props="{
        showFirstLastPage: true,
        showCurrentPage: true,
      }"
    >
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
          <ValidationObserver v-slot="{ handleSubmit }">
            <v-form @submit.prevent="handleSubmit(updateOrder)">
              <v-card-title class="text-h5"
                >Chi tiết đơn đặt hàng {{ selectedOrder.id }}</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-row>
                      <v-col cols="12">Tên khách hàng: {{ selectedOrder.customerName }}</v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="8">
                        <validation-provider name="Địa chỉ" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="selectedOrder.address"
                            label="Địa chỉ"
                            dense
                            outlined
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
                            v-model="selectedOrder.phone"
                            label="Số điện thoại"
                            dense
                            outlined
                            :error-messages="errors"
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="2">
                        <v-select
                          :items="statusList"
                          item-text="name"
                          label="Trạng thái đơn hàng"
                          dense
                          outlined
                          v-model="selectedOrder.status"
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row> </v-row>
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
                                :src="product.imageLink"
                                class="img-thumbnail mt-2 mb-2"
                                height="100px"
                              /> </v-list-item
                          ></v-col>
                          <v-col cols="2" class="ma-auto">
                            <v-list-item
                              ><h3 class="product-name">{{ product.productName }}</h3></v-list-item
                            >
                            <v-list-item class="product-variant-name"
                              >Phân loại: {{ product.productCombination }}</v-list-item
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
                                label="Số lượng"
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
                <v-btn color="blue darken-1" text type="submit"> Lưu </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-form>
          </ValidationObserver>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>
<script>
import { getAllOrder, getInforOrder, updateOrder } from '@/services/admin/all-order-form';
import { STATUS_ORDER } from '@/utils/mocks';
export default {
  data: () => ({
    page: 0,
    totalItems: 0,
    totalPages: 0,
    statusList: [],
    options: {
      page: 1,
      itemsPerPage: 10,
      sortBy: [],
      sortDesc: [],
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
      this.statusList = STATUS_ORDER;
      const { page, itemsPerPage } = this.options;
      const sort = this.getSort();
      const { data } = await getAllOrder(page, itemsPerPage, sort);

      this.page = data?.page;
      this.totalPages = data?.totalPages;
      this.totalItems = data?.totalItems;
      this.data = data?.data;

      this.loading.table = false;
    },

    async updateOrder() {
      const dataForm = {
        id: this.selectedOrder.id,
        address: this.selectedOrder.address,
        phone: this.selectedOrder.phone,
        status: this.selectedOrder.status,
      };

      const data = await updateOrder(dataForm);
      if (data) {
        await this.readDataFromAPI();
      }
      this.closeInfoDialog();
    },

    async openInfoDialog(item) {
      this.loading.info = true;
      const orderId = item?.id;
      const orderResponse = await getInforOrder(orderId);
      this.selectedOrder = orderResponse?.data?.data;

      this.dialog.info = true;
      this.loading.info = false;
    },

    closeInfoDialog() {
      this.dialog.info = false;
    },
    getSort() {
      let listParamSort = [];
      const length = this.options.sortBy.length;
      if (length > 0) {
        listParamSort = [];
        for (let i = 0; i < length; i++) {
          if (this.options.sortBy[i] === 'insertDate') {
            listParamSort.push({
              sortBy: 'createdDate',
              sortDesc: this.options.sortDesc[i],
            });
          } else {
            listParamSort.push({
              sortBy: this.options.sortBy[i],
              sortDesc: this.options.sortDesc[i],
            });
          }
        }
      } else {
        listParamSort.push({
          sortBy: null,
          sortDesc: null,
        });
      }
      return listParamSort;
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
