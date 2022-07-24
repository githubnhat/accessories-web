<template>
  <div>
    <div class="display-1 text-center mb-3">Đơn đặt hàng của bạn - Orders</div>

    <v-data-table
      v-model="selectedItems"
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
  </div>
</template>
<script>
import { getAllOrders } from '@/services/user/orders';
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
    },
    dialog: {
      info: false,
    },
    search: '',
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

      console.log('data', data);
      console.log('this.data', this.data);
      this.loading.table = false;
    },

    openInfoDialog() {
      // open info
    },

    closeInfoDialog() {
      // close info
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
