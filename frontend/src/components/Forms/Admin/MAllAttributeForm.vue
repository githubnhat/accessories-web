<template>
  <div>
    <div class="display-1 text-center mb-3">Danh sách thuộc tính</div>
    <v-data-table :headers="headers" :items="data" sort-by="calories" class="elevation-1">
      <template v-slot:top>
        <v-toolbar flat>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
                Thêm thuộc tính
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="text-h5">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.attributeName"
                        label="Tên thuộc tính"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="12">
                      <v-text-field
                        v-model="editedItem.variantNames"
                        label="Các giá trị được cách nhau bằng dấu phẩy (,)"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close"> Trở lại </v-btn>
                <v-btn color="blue darken-1" text v-if="formAction === 'save'" @click="save">
                  Lưu
                </v-btn>
                <v-btn color="blue darken-1" text v-else @click="update"> Cập nhật </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
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
import { getAllAttributes, addNewAttributes } from '@/services/admin/attribute/index';
export default {
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: '',
    headers: [
      {
        text: 'Tên thuộc tính',
        align: 'start',
        value: 'attributeName',
      },
      { text: 'Giá trị', value: 'variantNames' },
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

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'Thêm thuộc tính' : 'Chỉnh sửa thuộc tính';
    },
    formAction() {
      return this.editedIndex === -1 ? 'save' : 'update';
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  async created() {
    this.initialize();
  },

  methods: {
    async initialize() {
      this.data = await getAllAttributes('/admin/attribute/listVariants');
      console.log('return data', this.data);
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

    async save() {
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
      const newData = await addNewAttributes('/admin/attribute', request);
      if (newData !== null) this.data = [...this.data, newData];
      this.close();
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
};
</script>

<style>
</style>