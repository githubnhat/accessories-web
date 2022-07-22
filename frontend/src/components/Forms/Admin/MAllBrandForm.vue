<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách thương hiệu - Brand</div>

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
                <v-form @submit.prevent="handleSubmit(deleteBrand)">
                  <v-card-title>
                    <span class="text-h5">Xác nhận xóa (các) thương hiệu sau:</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row dense v-for="item in selectedItems" :key="item.id">
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.name"
                          label="Tên thương hiệu"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.code"
                          label="Mã thương hiệu"
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
                    <v-btn color="error darken-1" text type="submit" :loading="loading.deleteBrand">
                      Xóa
                    </v-btn>
                  </v-card-actions>
                </v-form>
              </ValidationObserver>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialog.add" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
                Thêm thương hiệu
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }">
                <v-form @submit.prevent="handleSubmit(addNewBrand)">
                  <v-card-title>
                    <span class="text-h5">Thêm thương hiệu mới</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <validation-provider
                          name="Tên thương hiệu"
                          rules="required|uniqueBrandName"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newBrandInfor.name"
                            label="Tên thương hiệu"
                            :error-messages="errors"
                            @blur="handleOnblurBrandNameInput"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <validation-provider
                          name="Mã thương hiệu"
                          rules="required|uniqueBrandCode"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newBrandInfor.code"
                            label="Mã thương hiệu"
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
                      :loading="loading.addNewBrandButton"
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
            <v-form class="mt-5" @submit.prevent="handleSubmit(editOneItem)">
              <v-card-title class="text-h5">Chỉnh sửa thông tin thương hiệu</v-card-title>
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <validation-provider
                      name="Tên thương hiệu"
                      rules="required"
                      v-slot="{ errors }"
                    >
                      <v-text-field
                        v-model="selectedItem.name"
                        label="Tên thương hiệu"
                        prepend-icon="mdi-order-alphabetical-ascending"
                        color="primary accent-3"
                        clearable
                        :error-messages="errors"
                      >
                      </v-text-field>
                    </validation-provider>
                    <validation-provider
                      name="Mã thương hiệu"
                      rules="required|uniqueCategoryCode"
                      v-slot="{ errors }"
                    >
                      <v-text-field
                        v-model="selectedItem.code"
                        label="Mã thương hiệu"
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
                >Bạn có chắc chắn <strong class="warning--text">&nbsp;xoá&nbsp;</strong> thương hiệu
                này?</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-text-field
                      v-model="selectedItem.name"
                      label="Tên thương hiệu"
                      prepend-icon="mdi-order-alphabetical-ascending"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>

                    <v-text-field
                      v-model="selectedItem.code"
                      label="Mã thương hiệu"
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
                  outlined
                  text
                  type="submit"
                  :loading="loading.deleteBrand"
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
import { getAllBrand, insertBrand, deleteBrands } from '@/services/admin/all-brand-form';
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
      addNewBrandButton: false,
      deleteBrand: false,
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
        text: 'Tên thương hiệu',
        align: 'start',
        value: 'name',
      },
      { text: 'Mã code', value: 'code' },
      { text: 'Hành động', value: 'actions', sortable: false },
    ],
    data: [],
    editedIndex: -1,
    newBrandInfor: {
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
      const data = await getAllBrand(page, itemsPerPage);
      this.page = data?.page;
      this.totalPages = data?.totalPages;
      this.totalItems = data?.totalItems;
      this.data = data?.data;
      this.loading.table = false;
    },

    handleOnblurBrandNameInput() {
      const result = transNameToCode(this.newBrandInfor.name);
      this.newBrandInfor.code = result;
    },

    async addNewBrand() {
      this.loading.addNewBrandButton = true;
      this.dialog.add = false;
      const { data, status } = await insertBrand(this.newBrandInfor);
      if (status === 'Success') {
        this.readDataFromAPI();
        alert('Thêm thương hiệu "' + data?.name + '" với code là "' + data?.code + '" thành công!');
      } else {
        alert(status);
      }
      this.loading.addNewBrandButton = false;
    },

    async deleteBrand() {
      this.loading.deleteBrand = true;
      this.dialog.deleteAll = false;
      const chosenItems = this.selectedItems.map((item) => item.id);
      this.selectedItems = [];
      const isUpdateSuccess = await deleteBrands(chosenItems);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.loading.deleteBrand = false;
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
      this.loading.deleteBrand = true;
      this.dialog.delete = false;
      const isUpdateSuccess = await deleteBrands([this.selectedItem.id]);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.selectedItem = {};
      this.loading.deleteBrand = false;
    },

    editOneItem() {
      // do edit item
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
