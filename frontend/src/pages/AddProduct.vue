<template>
  <m-admin-layout>
    <div class="display-1 pa-5 text-center">Thêm sản phẩm</div>
    <v-stepper :value="step">
      <v-stepper-header class="px-16">
        <v-stepper-step editable step="1" @click="step = 1"> Thông tin sản phẩm </v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step editable step="2"> Thuộc tính sản phẩm </v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step editable step="3"> Ảnh sản phẩm </v-stepper-step>
      </v-stepper-header>

      <v-stepper-content step="1">
        <v-card elevation="2" outlined class="mb-12 pa-5">
          <v-card-title>
            <span class="text-h5">Thông tin sản phẩm</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field label="Tên sản phẩm" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field label="Giá bán (VNĐ)" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field
                    label="Chiết khấu/ giảm giá (%)"
                    type="text"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-file-input
                    v-model="files"
                    placeholder="Thêm ảnh"
                    label="Ảnh sản phẩm"
                    multiple
                    prepend-icon="mdi-image"
                  >
                    <template v-slot:selection="{ text }">
                      <v-chip small label color="primary">
                        {{ text }}
                      </v-chip>
                    </template></v-file-input
                  >
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-card>
        <div class="d-flex justify-end">
          <v-btn color="primary" @click="stepp++"> Tiếp tục </v-btn>
        </div>
      </v-stepper-content>

      <v-stepper-content step="2">
        <v-card elevation="2" outlined class="mb-12 pa-5">
          <v-card-title>
            <v-row>
              <v-col cols="8"> <span class="text-h5"> Thuộc tính sản phẩm</span> </v-col>
              <v-col cols="2">
                <v-select
                  :items="listVariant"
                  item-text="attributeName"
                  return-object
                  dense
                  outlined
                  v-model="selectedAttribute"
                ></v-select>
              </v-col>
              <v-col cols="2">
                <v-btn
                  color="primary"
                  @click="handleAddVariant"
                  :disabled="selectedAttribute.id === 0"
                >
                  Thêm thuộc tính
                </v-btn>
              </v-col>
              <!-- <v-col cols="2"></v-col> -->
            </v-row>
          </v-card-title>
          <v-row v-for="(item, n) in variants" :key="n">
            <v-col cols="2">
              <m-text-field
                :nameVar="item.nameVariant"
                label="Tên thuộc tính"
                @input="handleInputName"
                v-model="item.nameVariant"
                :disabled="!!!item.disable"
              ></m-text-field>
            </v-col>
            <v-col cols="8">
              <m-text-field
                :nameVar="item.nameVariant"
                label="Danh sách biến thể"
                @input="handleInputVariants"
                v-model="item.variantValues"
              ></m-text-field>
            </v-col>
            <v-col cols="2">
              <v-btn color="primary" @click="handleDeleteVariant(item.nameVariant)"> Xóa </v-btn>
            </v-col>
          </v-row>
        </v-card>

        <div class="d-flex justify-end">
          <v-btn class="mr-2" text @click="step--"> Quay lại </v-btn>
          <v-btn color="primary" @click="handleNextStep2"> Tiếp tục </v-btn>
        </div>
      </v-stepper-content>

      <v-stepper-content step="3">
        <v-card color="grey lighten-1" class="mb-12" height="200px"> </v-card>
        <div class="d-flex justify-end">
          <v-btn class="mr-2" text @click="step--"> Quay lại </v-btn>
          <v-btn color="primary" @click="step = 3"> Tiếp tục </v-btn>
        </div>
      </v-stepper-content>
    </v-stepper>
  </m-admin-layout>
</template>

<script>
import MAdminLayout from '@/shared/MAdminLayout.vue';
import MTextField from '@/components/TextFields/MTextField.vue';
import { VARIANTS } from '@/utils/mocks/';
import { getProductAttributes } from '@/services/admin';

export default {
  data() {
    return {
      selectedAttribute: VARIANTS[0],
      step: 1,
      completeStep: [],
      variants: [],
      files: [],
      numberVariants: 0,
      listVariant: VARIANTS,
    };
  },
  created() {
    this.getAttributes();
    return null;
  },
  components: {
    MAdminLayout,
    MTextField,
  },
  methods: {
    async getAttributes() {
      let data = await getProductAttributes('http://localhost:8081/api/v1/admin/attribute');
      let existedVariant = data?.data;
      this.listVariant = this.listVariant.concat(existedVariant);
    },
    handleAddVariant() {
      let newVariant =
        this.selectedAttribute.id !== -1
          ? {
              nameVariant: this.selectedAttribute.attributeName,
              variantValues: '',
            }
          : {
              nameVariant: this.selectedAttribute.attributeName + ' ' + this.numberVariants++,
              variantValues: '',
              disable: true,
            };
      let isExisted =
        this.variants.filter((e) => e.nameVariant === this.selectedAttribute.attributeName).length >
        0;
      this.variants = isExisted ? [...this.variants] : [...this.variants, newVariant];
    },
    handleDeleteVariant(name) {
      this.variants = this.variants.filter((item) => item.nameVariant !== name);
    },
    handleInputName(value, name) {
      this.variants = this.variants.map((item) => {
        if (item.nameVariant === name) {
          return { ...item, nameVariant: value };
        }
        return item;
      });
    },
    handleInputVariants(value, name) {
      this.variants = this.variants.map((item) => {
        if (item.nameVariant === name) {
          return { ...item, variantValues: value };
        }
        return item;
      });
    },
    handleNextStep2() {
      console.log('submit', JSON.stringify(this.variants));
      this.step = 3;
    },
  },
};
</script>

<style></style>
