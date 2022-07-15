<template>
  <div>
    <div class="display-1 text-center mb-3">Danh mục sản phẩm - Category</div>

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
      <template v-slot:top>
        <v-toolbar flat>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog.add" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
                Thêm danh mục
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }">
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
                    <v-btn color="blue darken-1" text @click="close"> Trở lại </v-btn>
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
        <v-dialog v-model="dialog.edit" max-width="500px">
          <v-card>
            <ValidationObserver v-slot="{ handleSubmit }">
              <v-form class="mt-5" @submit.prevent="handleSubmit(editItemConfirm(item))">
                <v-card-title class="text-h5">Chỉnh sửa thông tin danh mục</v-card-title>
                <v-card-text>
                  <v-row no-gutters dense>
                    <v-col cols="12">
                      <validation-provider name="Tên danh mục" rules="required" v-slot="{ errors }">
                        <v-text-field
                          v-model="item.name"
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
                          v-model="item.code"
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
                  <v-btn color="blue darken-1" text type="submit">Đồng ý</v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-form>
            </ValidationObserver>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialog.delete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Bạn có chắc chắn xoá danh mục này?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Trở lại</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm(item)">Đồng ý</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-icon small class="mr-2" @click="openEditDialog"> mdi-pencil </v-icon>
        <v-icon small @click="openDeleteDialog"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
  </div>
</template>
<script>
import { getAllCategory, insertCategory } from '@/services/admin/all-category-form';
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
    },
    dialog: {
      add: false,
      delete: false,
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
      console.log('data', data);
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
        alert('Thêm danh mục "' + data?.name + '" với code là "' + data?.code + '" thành công!');
      } else {
        alert(status);
      }
      this.loading.addNewCategoryButton = false;
    },

    openEditDialog() {
      this.dialog.edit = true;
    },

    openDeleteDialog() {
      this.dialog.delete = true;
    },

    editItem(item) {
      this.editedIndex = this.data.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.data.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog.delete = true;
    },

    deleteItemConfirm() {
      this.data.splice(this.editedIndex, 1);
      this.closeDelete();
    },

    close() {
      this.dialog.add = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeEdit() {
      this.dialog.edit = false;
    },
    closeDelete() {
      this.dialog.delete = false;
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
