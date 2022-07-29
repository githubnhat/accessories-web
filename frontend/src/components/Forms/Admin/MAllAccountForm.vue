<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách tài khoản - Account</div>

    <v-data-table
      v-model="selectedItems"
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
                <v-form @submit.prevent="handleSubmit(deleteAccount)">
                  <v-card-title>
                    <span class="text-h5">Xác nhận xóa (các) tài khoản sau:</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row dense v-for="item in selectedItems" :key="item.id">
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.username"
                          label="Tên thương hiệu"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-text-field
                          v-model="item.gmail"
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
                    <v-btn
                      color="error darken-1"
                      text
                      type="submit"
                      :loading="loading.deleteAccount"
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
                @click="openAddDialog"
                v-on="on"
              >
                Thêm tài khoản
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver ref="addDialog" v-slot="{ handleSubmit }">
                <v-form @submit.prevent="handleSubmit(addNewAccount)">
                  <v-card-title>
                    <span class="text-h5">Thêm tài khoản mới</span>
                  </v-card-title>

                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <validation-provider
                          name="Tên người dùng"
                          rules="required"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newAccount.username"
                            label="Tên người dùng"
                            :error-messages="errors"
                            @blur="handleOnblurBrandNameInput"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <validation-provider
                          name="Gmail"
                          rules="required|email"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            v-model="newAccount.gmail"
                            label="Gmail"
                            :error-messages="errors"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <validation-provider name="Tên đầy đủ" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="newAccount.fullName"
                            label="Tên đầy đủ"
                            :error-messages="errors"
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <validation-provider name="Mật khẩu" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="newAccount.password"
                            label="Mật khẩu"
                            :error-messages="errors"
                            type="password"
                            clearable
                            outlined
                            dense
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12">
                        <v-select
                          label="Phân quyền"
                          :items="roles"
                          item-text="roleName"
                          persistent-hint
                          return-object
                          dense
                          outlined
                          v-model="selectedRole"
                        ></v-select>
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
              <v-card-title class="text-h5">Chỉnh sửa thông tin tài khoản</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <validation-provider name="Tên người dùng" rules="required" v-slot="{ errors }">
                      <v-text-field
                        v-model="selectedItem.username"
                        label="Tên người dùng"
                        :error-messages="errors"
                        @blur="handleOnblurBrandNameInput"
                        disabled
                        outlined
                        dense
                      ></v-text-field>
                    </validation-provider>
                  </v-col>
                  <v-col cols="12">
                    <validation-provider name="Gmail" rules="required|email" v-slot="{ errors }">
                      <v-text-field
                        v-model="selectedItem.gmail"
                        label="Gmail"
                        :error-messages="errors"
                        outlined
                        dense
                      ></v-text-field>
                    </validation-provider>
                  </v-col>
                  <v-col cols="12">
                    <validation-provider name="Tên đầy đủ" rules="required" v-slot="{ errors }">
                      <v-text-field
                        v-model="selectedItem.fullName"
                        label="Tên đầy đủ"
                        :error-messages="errors"
                        outlined
                        dense
                      ></v-text-field>
                    </validation-provider>
                  </v-col>
                  <v-col cols="12">
                    <v-select
                      label="Phân quyền"
                      :items="roles"
                      item-text="roleName"
                      persistent-hint
                      return-object
                      dense
                      outlined
                      v-model="selectedRole"
                    ></v-select>
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
                >Bạn có chắc chắn <strong class="warning--text">&nbsp;xoá&nbsp;</strong> tài khoản
                này?</v-card-title
              >
              <v-card-text>
                <v-row no-gutters dense>
                  <v-col cols="12">
                    <v-text-field
                      v-model="selectedItem.username"
                      label="Tên thương hiệu"
                      prepend-icon="mdi-order-alphabetical-ascending"
                      color="primary accent-3"
                      readonly
                    >
                    </v-text-field>

                    <v-text-field
                      v-model="selectedItem.gmail"
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
                <v-btn color="warning darken-1" text type="submit" :loading="loading.deleteAccount"
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
  getAllAccounts,
  getAllRoles,
  addAccount,
  updateAccount,
  deleteAccount,
} from '@/services/admin/all-account-form';
export default {
  data: () => ({
    page: 0,
    newAccount: {
      username: '',
      fullName: '',
      gmail: '',
      password: '',
      roleCode: '',
    },
    totalItems: 0,
    totalPages: 0,
    roles: [],
    roleNames: [],
    options: {
      page: 1,
      itemsPerPage: 5,
      sortBy: [],
      sortDesc: [],
    },
    loading: {
      table: true,
      addNewBrandButton: false,
      deleteAccount: false,
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
        align: 'start',
        value: 'id',
      },
      {
        text: 'Tên tài khoản',
        value: 'username',
      },
      { text: 'Gmail', value: 'gmail' },
      { text: 'Họ và tên', value: 'fullName' },
      { text: 'Vai trò', value: 'role' },
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
    selectedRole: '',
    selectedItems: [],
    editItem: {},
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
      const sort = this.getSort();
      const data = await getAllAccounts(page, itemsPerPage, sort);
      this.page = data?.page;
      this.totalPages = data?.totalPages;
      this.totalItems = data?.totalItems;
      this.data = data?.data;
      await this.getAllRoles();
      this.loading.table = false;
    },
    async getAllRoles() {
      this.roles = await getAllRoles();
      this.selectedRole = this.roles[1];
    },
    getSort() {
      let listParamSort = [];
      const length = this.options.sortBy.length;
      if (length > 0) {
        listParamSort = [];
        for (let i = 0; i < length; i++) {
          if (this.options.sortBy[i] === 'role') {
            listParamSort.push({
              sortBy: 'roles_roleName',
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

    handleOnblurBrandNameInput() {
      // const result = transNameToCode(this.newBrandInfor.name);
      this.newBrandInfor.code = '';
    },

    async addNewAccount() {
      this.loading.addNewBrandButton = true;
      this.newAccount.roleCode = this.selectedRole.roleCode;
      const data = await addAccount(this.newAccount);
      if (data?.status === 'Success') {
        await this.readDataFromAPI();
        alert('Tài khoản mới thêm thành công!');
      } else {
        alert('Thêm tài khoản không thành công!');
      }
      this.loading.addNewBrandButton = false;
      this.closeAdd();
    },

    async deleteAccount() {
      this.loading.deleteAccount = true;
      this.dialog.deleteAll = false;
      const chosenItems = this.selectedItems.map((item) => item.id);
      this.selectedItems = [];
      const data = await deleteAccount(chosenItems);
      const isUpdateSuccess = data?.data.data;
      if (isUpdateSuccess) {
        alert(data?.data.message);
        this.readDataFromAPI();
      } else {
        alert(data?.data.message);
      }
      this.loading.deleteAccount = false;
    },

    async openAddDialog() {
      this.newAccount = {
        username: '',
        fullName: '',
        gmail: '',
        password: '',
        roleCode: '',
      };
      await this.getAllRoles();
    },

    openEditDialog(item) {
      this.selectedItem = { ...item };
      this.selectedRole = this.roles.filter((r) => r.roleName === item.role)[0];
      this.dialog.edit = true;
    },

    openDeleteDialog(item) {
      this.selectedItem = item;
      this.dialog.delete = true;
    },

    async deleteOneItem() {
      this.loading.deleteAccount = true;
      const data = await deleteAccount([this.selectedItem.id]);
      const isUpdateSuccess = data?.data.data;
      if (isUpdateSuccess) {
        alert(data?.data.message);
        this.readDataFromAPI();
      } else {
        alert(data?.data.message);
      }
      this.selectedItem = {};
      this.loading.deleteAccount = false;
      this.closeDelete();
    },

    async editOneItem() {
      this.editItem = {
        id: this.selectedItem.id,
        fullName: this.selectedItem.fullName,
        gmail: this.selectedItem.gmail,
        roleCode: this.selectedRole.roleCode,
      };
      const data = await updateAccount(this.editItem);
      if (data?.status === 'Success') {
        await this.readDataFromAPI();
        alert('Cập nhật tài khoản thành công!');
      } else {
        alert('Cập nhật tài khoản không thành công!');
      }
      this.closeEdit();
    },

    closeAdd() {
      this.$refs.addDialog.reset();
      this.dialog.add = false;
    },

    closeEdit() {
      this.dialog.edit = false;
      this.$refs.editDialog.reset();
    },
    closeDelete() {
      this.dialog.delete = false;
      this.dialog.deleteAll = false;
    },
  },
  mounted() {
    this.readDataFromAPI();
  },
};
</script>

<style></style>
