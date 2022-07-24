<template>
  <div>
    <div class="display-1 text-center mb-3">Danh mục sản phẩm - Category</div>

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
                <v-form @submit.prevent="handleSubmit(deleteMultiItem)">
                  <v-card-title>
                    <span class="text-h5">Xác nhận xóa (các) danh mục sau:</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row dense v-for="item in selectedItems" :key="item.id">
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.name"
                          label="Tên danh mục"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.code"
                          label="Mã danh mục"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeDeleteAll"> Trở lại </v-btn>
                    <v-btn
                      color="error darken-1"
                      text
                      type="submit"
                      :loading="loading.deleteCategory"
                    >
                      Xóa
                    </v-btn>
                  </v-card-actions>
                </v-form>
              </ValidationObserver>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialog.add" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                dark
                class="mb-2"
                v-bind="attrs"
                v-on="on"
                @click="openAddDialog"
              >
                Thêm danh mục
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }" ref="addDialog">
                <v-form @submit.prevent="handleSubmit(addNewCategory)">
                  <v-card-title>
                    <span class="text-h5">Thêm danh mục</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <validation-provider
                          name="Tên danh mục"
                          rules="required"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newCategoryInfor.name"
                            label="Tên danh mục"
                            :error-messages="errors"
                            @blur="handleOnblurCategoryNameInput"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <validation-provider
                          name="Mã danh mục"
                          rules="required|uniqueCategoryCode"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newCategoryInfor.code"
                            label="Mã danh mục"
                            :error-messages="errors"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                    </v-row>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeAdd"> Trở lại </v-btn>
                    <v-btn
                      color="blue darken-1"
                      text
                      type="submit"
                      :loading="loading.addNewCategoryButton"
                    >
                      Thêm mới
                    </v-btn>
                  </v-card-actions>
                </v-form>
              </ValidationObserver>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="openEditDialog(item)"> mdi-pencil </v-icon>
        <v-icon small @click="openDeleteDialog(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
    <div class="action-item-dialog">
      <v-dialog v-model="dialog.edit" max-width="500px">
        <v-card>
          <ValidationObserver ref="editDialog" v-slot="{ handleSubmit }">
            <v-form class="mt-5" @submit.prevent="handleSubmit(editItemConfirm)">
              <v-card-title class="text-h5">Chỉnh sửa thông tin danh mục</v-card-title>
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <validation-provider name="Tên danh mục" rules="required" v-slot="{ errors }">
                      <v-text-field
                        v-model="selectedItem.name"
                        label="Tên danh mục"
                        prepend-icon="mdi-order-alphabetical-ascending"
                        color="primary accent-3"
                        clearable
                        :error-messages="errors"
                      >
                      </v-text-field>
                    </validation-provider>
                    <validation-provider
                      name="Mã danh mục"
                      rules="required|uniqueCategoryCode"
                      v-slot="{ errors }"
                    >
                      <v-text-field
                        v-model="selectedItem.code"
                        label="Mã danh mục"
                        prepend-icon="mdi-alphabetical"
                        color="primary accent-3"
                        clearable
                        :error-messages="errors"
                      >
                      </v-text-field>
                    </validation-provider>
                  </v-col>
                </v-row>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeEdit">Trở lại</v-btn>
                <v-btn color="blue darken-1" outlined text type="submit">Cập nhật</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-form>
          </ValidationObserver>
        </v-card>
      </v-dialog>
      <v-dialog v-model="dialog.delete" max-width="500px">
        <v-card>
          <ValidationObserver v-slot="{ handleSubmit }">
            <v-form class="mt-5" @submit.prevent="handleSubmit(deleteOneItem)">
              <v-card-title class="text-h5"
                >Bạn có chắc chắn <strong>&nbsp;xoá&nbsp;</strong> danh mục này?</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-text-field
                      v-model="selectedItem.name"
                      label="Tên danh mục"
                      prepend-icon="mdi-order-alphabetical-ascending"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>

                    <v-text-field
                      v-model="selectedItem.code"
                      label="Mã danh mục"
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
                <v-btn
                  color="warning darken-1"
                  class="warning--text"
                  outlined
                  text
                  type="submit"
                  :loading="loading.deleteCategory"
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
import {
  getAllCategory,
  insertCategory,
  deleteCategories,
} from '@/services/admin/all-category-form';
import { transNameToCode } from '@/utils';
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
      addNewCategoryButton: false,
      deleteCategory: false,
    },
    dialog: {
      add: false,
      delete: false,
      deleteAll: false,
      edit: false,
    },
    search: '',
    headers: [
      {
        text: 'ID',
        value: 'id',
      },
      {
        text: 'Tên danh mục',
        align: 'start',
        value: 'name',
      },
      { text: 'Mã code', value: 'code' },
      { text: 'Hành động', value: 'actions', sortable: false },
    ],
    data: [],
    editedIndex: -1,
    newCategoryInfor: {
      name: '',
      code: '',
    },
    editedItem: {
      attributeName: '',
      variantNames: '',
    },
    defaultItem: {
      attributeName: '',
      variantNames: '',
    },
    selectedItems: [],
    selectedItem: {},
  }),

  computed: {},

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
      const data = await getAllCategory(page, itemsPerPage);

      this.page = data?.page;
      this.totalPages = data?.totalPages;
      this.totalItems = data?.totalItems;
      this.data = data?.data;
      this.loading.table = false;
    },

    handleOnblurCategoryNameInput() {
      const result = transNameToCode(this.newCategoryInfor.name);
      this.newCategoryInfor.code = result;
    },

    async addNewCategory() {
      this.loading.addNewCategoryButton = true;
      const { data, status } = await insertCategory(this.newCategoryInfor);
      if (status === 'Success') {
        this.dialog.add = false;
        this.readDataFromAPI();
        alert('Thêm danh mục "' + data?.name + '" với code là "' + data?.code + '" thành công!');
      } else {
        alert(status);
      }
      this.loading.addNewCategoryButton = false;
    },

    async deleteMultiItem() {
      this.loading.deleteCategory = true;
      this.dialog.deleteAll = false;
      const chosenItems = this.selectedItems.map((item) => item.id);
      this.selectedItems = [];
      const isUpdateSuccess = await deleteCategories(chosenItems);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.loading.deleteCategory = false;
    },

    openAddDialog() {
      this.newCategoryInfor = { name: '', code: '' };
      this.$refs.editDialog.reset();
    },

    openEditDialog(item) {
      this.selectedItem = { ...item };
      this.dialog.edit = true;
    },

    openDeleteDialog(item) {
      this.selectedItem = item;
      this.dialog.delete = true;
    },

    async deleteOneItem() {
      this.loading.deleteCategory = true;
      this.dialog.delete = false;
      const isUpdateSuccess = await deleteCategories([this.selectedItem.id]);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.selectedItem = {};
      this.loading.deleteCategory = false;
    },

    closeAdd() {
      this.dialog.add = false;
    },

    closeEdit() {
      this.dialog.edit = false;
      this.$refs.editDialog.reset();
    },
    closeDelete() {
      this.dialog.delete = false;
    },
    closeDeleteAll() {
      this.dialog.deleteAll = false;
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
