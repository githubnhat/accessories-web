<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách thuộc tính</div>
    <v-data-table
      v-model="selectedItems"
      :page="page"
      :pageCount="totalPages"
      :headers="headers"
      :items="data"
      :options.sync="options"
      :server-items-length="totalItems"
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
                <v-form @submit.prevent="handleSubmit(deleteAttributes)">
                  <v-card-title>
                    <span class="text-h5">Xác nhận xóa (các) thuộc tính sau:</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row dense v-for="item in selectedItems" :key="item.id">
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.attributeName"
                          label="Tên thuộc tính"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.variantNames"
                          label="Giá trị"
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
                    <v-btn
                      color="error darken-1"
                      text
                      type="submit"
                      :loading="loading.deleteListAttribute"
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
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
                Thêm thuộc tính
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }">
                <v-form @submit.prevent="handleSubmit(addNewAttributes)">
                  <v-card-title>
                    <span class="text-h5">Thêm thuộc tính</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <validation-provider
                          name="Tên thuộc tính"
                          rules="required|uniqueAttributeName"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="editedItem.attributeName"
                            label="Tên thuộc tính"
                            :error-messages="errors"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.variantNames"
                          label="Các giá trị được cách nhau bằng dấu phẩy (,)"
                          :error-messages="errors"
                          outlined
                          dense
                        ></v-text-field>
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
                      :loading="loading.addNewAttributeButton"
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
        <v-btn color="primary" @click="readDataFromAPI"> Reset </v-btn>
      </template>
    </v-data-table>
    <div class="action-item-dialog">
      <v-dialog retain-focus v-model="dialog.edit" max-width="500px">
        <v-card>
          <ValidationObserver ref="editDialog" v-slot="{ handleSubmit }">
            <v-form class="mt-5" @submit.prevent="handleSubmit(editOneItem)">
              <v-card-title class="text-h5">Chỉnh sửa thông tin thuộc tính</v-card-title>
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <validation-provider name="Tên thuộc tính" rules="required" v-slot="{ errors }">
                      <v-text-field
                        v-model="selectedItem.attributeName"
                        label="Tên thuộc tính"
                        prepend-icon="mdi-order-alphabetical-ascending"
                        color="primary accent-3"
                        clearable
                        :error-messages="errors"
                      >
                      </v-text-field>
                    </validation-provider>
                    <validation-provider
                      name="Mã thuộc tính"
                      rules="required|uniqueCategoryCode"
                      v-slot="{ errors }"
                    >
                      <v-text-field
                        v-model="selectedItem.variantNames"
                        label="Mã thuộc tính"
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
                >Bạn có chắc chắn <strong class="warning--text">&nbsp;xoá&nbsp;</strong> thuộc tính
                này?</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-text-field
                      v-model="selectedItem.attributeName"
                      label="Tên thuộc tính"
                      prepend-icon="mdi-order-alphabetical-ascending"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>

                    <v-text-field
                      v-model="selectedItem.variantNames"
                      label="Mã thuộc tính"
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
                <v-btn color="error darken-1" text type="submit" :loading="loading.deleteAttribute"
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
  getAllAttributes,
  addNewAttributes,
  deleteAttributes,
} from '@/services/admin/attribute/index';
export default {
  data: () => ({
    page: 0,
    totalItems: 0,
    totalPages: 0,
    itemsPerPage: 0,
    options: {
      page: 1,
      itemsPerPage: 10,
    },
    loading: {
      table: true,
      addNewAttributeButton: false,
      deleteListAttribute: false,
      deleteAttribute: false,
    },
    dialog: {
      add: false,
      delete: false,
      deleteAll: false,
      edit: false,
    },
    // dialog: false,
    // dialogDelete: false,
    search: '',
    headers: [
      {
        text: 'ID',
        align: 'start',
        value: 'id',
      },
      {
        text: 'Tên thuộc tính',
        value: 'attributeName',
      },
      { text: 'Giá trị', value: 'variantNames' },
      { text: 'Hành động', value: 'actions', sortable: false },
    ],
    data: [],
    selectedItems: [],
    selectedItem: {},
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
      this.data = await getAllAttributes(page, itemsPerPage);
      //Then injecting the result to datatable parameters.
      this.itemsPerPage = itemsPerPage;
      this.totalItems = this.data.totalItems;
      this.totalPages = this.data.totalPages;
      this.data = this.data.data;
      this.loading.table = false;
    },

    openEditDialog(item) {
      this.selectedItem = { ...item };
      this.dialog.edit = true;
    },

    openDeleteDialog(item) {
      this.selectedItem = item;
      this.dialog.delete = true;
    },
    openDeleteAllDialog() {
      this.dialog.deleteAll = true;
    },

    closeAdd() {
      this.dialog.add = false;
    },

    closeDelete() {
      this.dialog.deleteAll = false;
      this.dialog.delete = false;
    },
    closeEdit() {
      this.dialog.edit = false;
      this.selectedItem = {};
      this.$refs.editDialog.reset();
    },

    async deleteOneItem() {
      this.loading.deleteAttribute = true;
      this.dialog.delete = false;
      const isUpdateSuccess = await deleteAttributes([this.selectedItem.id]);
      if (isUpdateSuccess) {
        alert('Xóa thành công!');
        await this.readDataFromAPI();
      } else {
        alert('Xóa không thành công, vui lòng thử lại!');
      }
      this.selectedItem = {};
      this.loading.deleteAttribute = false;
    },

    async deleteAttributes() {
      this.loading.deleteAttribute = true;
      const chosenItems = this.selectedItems.map((item) => item.id);
      this.dialog.deleteAll = false;
      this.selectedItems = [];
      const data = await deleteAttributes(chosenItems);
      if (data === true) {
        this.readDataFromAPI();
        alert('Xoá thành công');
      } else {
        alert('Xoá không thành công, vui lòng thử lại!');
      }
      this.loading.deleteAttribute = false;
    },

    async addNewAttributes() {
      var listVariantNames = this.editedItem.variantNames.split(',').map((a) => a.trim());
      var res = [];
      listVariantNames.forEach(function (i) {
        let isInclude = false;
        res.forEach((_x) => {
          if (_x.toLowerCase() == i.toLowerCase()) {
            isInclude = true;
          }
        });
        if (!isInclude) {
          res.push(i);
        }
      });
      listVariantNames = res.filter(function (element) {
        return element !== '';
      });
      let request = {
        attributeName: this.editedItem.attributeName,
        variantNames: listVariantNames,
      };
      const newData = await addNewAttributes(request);
      if (newData !== null) this.readDataFromAPI();
      this.closeAdd();
    },
    editOneItem() {
      console.log('edit');
    },
    async update() {
      // const newData = await addNewAttributes(
      //   'http://localhost:8081/api/v1/admin/attribute',
      //   this.editedItem,
      // );
      // this.data = [...this.data, newData];
      this.close();
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style>
</style>