<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách sản phẩm</div>

    <v-data-table
      v-model="selectedItems"
      :page="page"
      :pageCount="totalPages"
      :headers="headers"
      :items="data"
      :options.sync="options"
      :server-items-length="totalItems"
      multi-sort
      :loading="loading.table"
      :footer-props="{
        showFirstLastPage: true,
        showCurrentPage: true,
      }"
      class="elevation-1"
      show-select
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog.deleteAll" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                :disabled="selectedItems.length === 0"
                color="primary"
                dark
                class="mb-2 mr-2"
                v-bind="attrs"
                v-on="on"
              >
                Xóa
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }">
                <v-form @submit.prevent="handleSubmit(deleteProducts)">
                  <v-card-title>
                    <span class="text-h5">Xác nhận xóa (các) thuộc tính sau:</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row dense v-for="item in selectedItems" :key="item.id">
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.productName"
                          label="Tên sản phẩm"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.originalQuantity"
                          label="Số lượng"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeDelete"> Trở lại </v-btn>
                    <v-btn color="error darken-1" text type="submit" :loading="loading.deleteAll">
                      Xóa
                    </v-btn>
                  </v-card-actions>
                </v-form>
              </ValidationObserver>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon small @click="openDeleteDialog(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="readDataFromAPI"> Reset </v-btn>
      </template>
    </v-data-table>
    <div class="action-item-dialog">
      <v-dialog v-model="dialog.delete" max-width="500px">
        <v-card>
          <ValidationObserver v-slot="{ handleSubmit }">
            <v-form class="mt-5" @submit.prevent="handleSubmit(deleteOneItem)">
              <v-card-title class="text-h5"
                >Bạn có chắc chắn <strong class="warning--text">&nbsp;xoá&nbsp;</strong> sản phẩm
                này?</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-text-field
                      v-model="selectedItem.productName"
                      label="Tên sản phẩm"
                      prepend-icon="mdi-order-alphabetical-ascending"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>

                    <v-text-field
                      v-model="selectedItem.originalQuantity"
                      label="Số lượng"
                      prepend-icon="mdi-alphabetical"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Trở lại</v-btn>
                <v-btn color="error darken-1" text type="submit" :loading="loading.delete"
                  >Xóa</v-btn
                >
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
import { getAllProducts, deleteProducts } from '@/services/admin/all-product-form/index';
export default {
  data: () => ({
    page: 0,
    totalItems: 0,
    totalPages: 0,
    itemsPerPage: 0,
    loading: {
      table: false,
      delete: false,
      deleteAll: false,
    },
    selectedItems: [],
    selectedItem: {},
    options: {
      page: 1,
      itemsPerPage: 10,
      sortBy: [],
      sortDesc: [],
    },
    dialog: {
      deleteAll: false,
      delete: false,
    },
    search: '',
    headers: [
      {
        text: 'ID',
        align: 'start',
        value: 'id',
      },
      {
        text: 'Tên sản phẩm',
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
      this.loading.table = true;
      const { page, itemsPerPage } = this.options;
      const sort = this.getSort();
      this.data = await getAllProducts(page, itemsPerPage, sort);

      this.itemsPerPage = itemsPerPage;
      this.totalItems = this.data.totalItems;
      this.totalPages = this.data.totalPages;
      this.data = this.data.data;
      this.loading.table = false;
    },

    async deleteProducts() {
      this.loading.deleteAll = true;
      const chosenItems = this.selectedItems.map((item) => item.id);
      this.dialog.deleteAll = false;
      this.selectedItems = [];
      const data = await deleteProducts(chosenItems);
      if (data === true) {
        this.readDataFromAPI();
        alert('Xoá thành công');
      } else {
        alert('Xoá không thành công, vui lòng thử lại!');
      }
      this.loading.deleteAll = false;
    },

    async deleteOneItem() {
      this.loading.delete = true;
      this.dialog.delete = false;
      const isUpdateSuccess = await deleteProducts([this.selectedItem.id]);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        await this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.selectedItem = {};
      this.loading.delete = false;
    },

    openDeleteAllDialog() {
      this.dialog.deleteAll = true;
    },

    openDeleteDialog(item) {
      this.selectedItem = item;
      this.dialog.delete = true;
    },

    closeDelete() {
      this.dialog.deleteAll = false;
      this.dialog.delete = false;
    },

    editItem(item) {
      const productId = item?.id;
      this.$router.push({
        name: 'update-product',
        params: {
          id: productId,
        },
      });
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
    getSort() {
      let listParamSort = [];
      const length = this.options.sortBy.length;
      if (length > 0) {
        listParamSort = [];
        for (let i = 0; i < length; i++) {
          if (this.options.sortBy[i] === 'brandName') {
            listParamSort.push({
              sortBy: 'brands_name',
              sortDesc: this.options.sortDesc[i],
            });
          } else if (this.options.sortBy[i] === 'categoryName') {
            listParamSort.push({
              sortBy: 'categories_name',
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
