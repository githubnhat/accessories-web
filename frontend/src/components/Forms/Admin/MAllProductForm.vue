<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách sản phẩm</div>

    <v-data-table
      :page="page"
      :pageCount="totalPages"
      :headers="headers"
      :items="data"
      :options.sync="options"
      :server-items-length="totalItems"
      :loading="loading"
      :footer-props="{
        showFirstLastPage: true,
        showCurrentPage: true,
      }"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
              Thêm thuộc tính
            </v-btn>
          </template>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="text-h5">Bạn có chắc chắn xoá thuộc tính?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Trở lại</v-btn>
                <v-btn color="blue darken-1" text @click="deleteItemConfirm">Đồng ý</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
  </div>
</template>
<script>
import { getAllProducts } from '@/services/admin/all-product-form/index';
export default {
  data: () => ({
    page: 0,
    totalItems: 0,
    totalPages: 0,
    itemsPerPage: 0,
    loading: true,
    options: {
      page: 1,
      itemsPerPage: 10,
    },
    dialog: false,
    dialogDelete: false,
    search: '',
    headers: [
      {
        text: 'Tên sản phẩm',
        align: 'start',
        value: 'productName',
      },
      { text: 'Giá', value: 'originalPrice' },
      { text: 'Số lượng', value: 'originalQuantity' },
      { text: 'Thương hiệu', value: 'brandName' },
      { text: 'Danh mục', value: 'categoryName' },
      { text: 'Hành động', value: 'actions', sortable: false },
    ],
    data: [],
    editedIndex: -1,
    editedItem: {
      attributeName: '',
      variantNames: '',
    },
    defaultItem: {
      attributeName: '',
      variantNames: '',
    },
  }),

  computed: {},

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
    options: {
      handler() {
        this.readDataFromAPI();
      },
    },
    deep: true,
  },

  methods: {
    async readDataFromAPI() {
      this.loading = true;
      const { page, itemsPerPage } = this.options;
      this.data = await getAllProducts(page, itemsPerPage);

      this.itemsPerPage = itemsPerPage;
      this.totalItems = this.data.totalItems;
      this.totalPages = this.data.totalPages;
      this.data = this.data.data;
      this.loading = false;
    },

    editItem(item) {
      this.editedIndex = this.data.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.data.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.data.splice(this.editedIndex, 1);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style>
</style>