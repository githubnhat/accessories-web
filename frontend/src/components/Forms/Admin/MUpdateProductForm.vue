<template>
  <div>
    <div class="display-1 text-center mb-3">Cập nhật sản phẩm</div>
    <v-stepper :value="step">
      <v-stepper-header class="px-16">
        <v-stepper-step editable step="1" @click="step = 1"> Thông tin sản phẩm </v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :editable="completeStep.includes(2)" step="2">
          Thuộc tính sản phẩm
        </v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :editable="completeStep.includes(2)" step="3">
          Chi tiết các biến thể kết hợp
        </v-stepper-step>
      </v-stepper-header>

      <v-stepper-content step="1">
        <ValidationObserver v-slot="{ handleSubmit }">
          <v-form @submit.prevent="handleSubmit(onSubmitStep1)">
            <v-card elevation="2" outlined class="mb-12 pa-5">
              <v-card-title>
                <span class="text-h5">Thông tin sản phẩm</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row dense>
                    <v-col cols="12" md="9">
                      <validation-provider name="Tên sản phẩm" rules="required" v-slot="{ errors }">
                        <v-text-field
                          label="Tên sản phẩm"
                          v-model="productName"
                          outlined
                          :error-messages="errors"
                          dense
                          autofocus
                        ></v-text-field>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="3">
                      <validation-provider
                        name="Giá"
                        rules="required|numeric|min_value:0"
                        v-slot="{ errors }"
                      >
                        <v-text-field
                          label="Giá gốc (VNĐ)"
                          v-model="originalPrice"
                          outlined
                          dense
                          :error-messages="errors"
                          suffix="VNĐ"
                        ></v-text-field>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12">
                      <validation-provider name="Mô tả sản phẩm" rules="" v-slot="{ errors }">
                        <v-textarea
                          label="Mô tả sản phẩm"
                          v-model="description"
                          outlined
                          dense
                          :error-messages="errors"
                        ></v-textarea>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="3">
                      <validation-provider
                        name="Chiết khấu"
                        rules="between:0,100|required"
                        v-slot="{ errors }"
                      >
                        <v-text-field
                          label="Chiết khấu/ giảm giá (%)"
                          type="text"
                          v-model="discount"
                          :error-messages="errors"
                          outlined
                          dense
                          suffix="%"
                        ></v-text-field>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="3">
                      <validation-provider
                        name="Số lượng"
                        rules="required|numeric|min_value:1"
                        v-slot="{ errors }"
                      >
                        <v-text-field
                          label="Số lượng"
                          v-model="originalQuantity"
                          outlined
                          clearable
                          type="number"
                          dense
                          :error-messages="errors"
                          suffix="Sản phẩm"
                        ></v-text-field>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="3">
                      <v-select
                        :items="brands"
                        item-text="name"
                        label="Chọn brand"
                        dense
                        outlined
                        v-model="brandName"
                      ></v-select>
                    </v-col>
                    <v-col cols="12" md="3">
                      <v-select
                        :items="categories"
                        item-text="name"
                        label="Chọn category"
                        dense
                        outlined
                        v-model="categoryName"
                      ></v-select>
                    </v-col>
                    <v-col cols="12">
                      <v-file-input
                        v-model="files"
                        placeholder="Thêm ảnh"
                        label="Ảnh sản phẩm"
                        multiple
                        outlined
                        prepend-icon="mdi-image"
                        dense
                      >
                        <template v-slot:selection="{ index, text }">
                          <v-chip
                            small
                            label
                            color="primary"
                            close
                            @click:close="deleteImage(index, text)"
                          >
                            {{ text }}
                          </v-chip>
                        </template>
                      </v-file-input>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
            </v-card>
            <div class="d-flex justify-end">
              <v-btn color="primary" type="submit" :loading="loading.step1"> Tiếp tục </v-btn>
            </div>
          </v-form>
        </ValidationObserver>
      </v-stepper-content>

      <v-stepper-content step="2">
        <ValidationObserver v-slot="{ handleSubmit }">
          <v-form @submit.prevent="handleSubmit(onSubmitStep2)">
            <v-card elevation="2" outlined class="mb-12 pa-5">
              <v-card-title>
                <v-row>
                  <v-col cols="8"> <span class="text-h5"> Thuộc tính sản phẩm</span> </v-col>
                  <v-col cols="2">
                    <v-select
                      :items="attributeList"
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
                      @click="handleAddAttribute"
                      :disabled="selectedAttribute.id === 0"
                    >
                      Thêm thuộc tính
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card-title>
              <v-row v-for="(item, n) in attributes" :key="n">
                <v-col cols="2">
                  <m-text-field
                    :nameInput="`Tên thuộc tính`"
                    :rules="`required`"
                    :nameVar="item.attributeName"
                    label="Tên thuộc tính"
                    @input="handleInputName"
                    v-model="item.attributeName"
                    :readonly="!!!item.disable"
                  ></m-text-field>
                </v-col>
                <v-col cols="8">
                  <m-auto-complete
                    :value="item.selectedVariants"
                    label="Danh sách biến thể"
                    :selectedVariantsProp="item.selectedVariants"
                    :variantItems="item.variantNames"
                    :nameAttribute="item.attributeName"
                    :indexSearch="n"
                    @input="handleInputVariants"
                    required
                    :search="search[n]"
                    @update-search-input="handleUpdateSearchInput"
                    @addVariant="handleAddNewVariant"
                  >
                  </m-auto-complete>
                </v-col>
                <v-col cols="2">
                  <v-btn color="primary" @click="handleDeleteVariant(item.attributeName)">
                    Xóa
                  </v-btn>
                </v-col>
              </v-row>
            </v-card>

            <div class="d-flex justify-end">
              <v-btn class="mr-2" text @click="step--"> Quay lại </v-btn>
              <v-btn
                color="primary mr-2"
                type="submit"
                @click="isChangeAttribute = true"
                :loading="loading.step2"
              >
                Tiếp tục
              </v-btn>
              <v-btn
                color="primary"
                type="submit"
                @click="isChangeAttribute = false"
                :loading="loading.step2"
              >
                Bỏ qua thay đổi
              </v-btn>
            </div>
          </v-form>
        </ValidationObserver>
      </v-stepper-content>

      <v-stepper-content step="3">
        <ValidationObserver v-slot="{ handleSubmit }">
          <v-form @submit.prevent="handleSubmit(async () => await onSubmitStep3())">
            <v-card elevation="2" outlined class="mb-12 pa-5">
              <v-row v-for="(item, n) in combineVariants" :key="n">
                <v-col cols="3">
                  <m-text-field
                    label="Biến thể kết hợp của sản phẩm"
                    color
                    v-model="item.productVariantName"
                    readonly
                    :disable="item.isUse"
                  ></m-text-field>
                </v-col>
                <v-col cols="2" />
                <v-col cols="2">
                  <m-text-field
                    :nameInput="`Số lượng`"
                    :rules="`required|min_value:0|numeric`"
                    v-model="item.quantity"
                    label="Số lượng sản phẩm"
                    :nameAttribute="item.productVariantName"
                    @input="handleInputCombinationQuantity"
                    required
                    type="number"
                    :disable="item.isUse"
                  >
                  </m-text-field>
                </v-col>
                <v-col cols="1" />
                <v-col cols="2">
                  <m-text-field
                    :nameInput="`Giá sản phẩm`"
                    :rules="`required|numeric|min_value:1`"
                    v-model="item.price"
                    label="Giá sản phẩm"
                    :nameAttribute="item.productVariantName"
                    @input="handleInputCombinationPrice"
                    required
                    :disable="item.isUse"
                  >
                  </m-text-field>
                </v-col>
                <v-col cols="2">
                  <v-select
                    :items="selectItems"
                    item-text="title"
                    item-value="value"
                    :value="item.isUse"
                    dense
                    outlined
                    @change="handleSelectAdditionalCombination(item.productVariantName)"
                  ></v-select>
                </v-col>
              </v-row>
            </v-card>
            <div class="d-flex justify-end">
              <v-btn class="mr-2" text @click="step--"> Quay lại </v-btn>
              <v-btn color="primary" type="submit" :loading="loading.step3"> Tiếp tục </v-btn>
            </div>
          </v-form>
        </ValidationObserver>
      </v-stepper-content>
    </v-stepper>
  </div>
</template>

<script>
import MTextField from '@/components/TextFields/MTextField.vue';
import MAutoComplete from '@/components/TextFields/MAutoComplete.vue';
import { VARIANTS, SELECT_ITEMS } from '@/utils/mocks/';
import {
  getProductAttributes,
  getBrands,
  getCategories,
  insertProduct,
} from '@/services/admin/add-product-form';
import { getInforProduct } from '@/services/admin/update-product-form';
import { combineVariants } from '@/utils';
import { upLoadImg } from '@/services/uploadFile';

export default {
  data() {
    return {
      productId: 0,
      productName: '',
      description: '',
      originalPrice: 1000,
      originalQuantity: 1,
      discount: null,
      brandName: '',
      categoryName: '',
      selectedAttribute: VARIANTS[0],
      search: [],
      step: 1,
      completeStep: [],
      attributes: [],
      brands: [],
      categories: [],
      files: [],
      images: null,
      productData: {},
      numberAttributes: 0,
      attributeList: VARIANTS,
      selectItems: SELECT_ITEMS,
      combineVariants: [],
      payload: {},
      loading: {
        step1: false,
        step2: false,
        step3: false,
      },
      isChangeAttribute: false,
    };
  },
  created() {
    this.productId = this.$route.params.id;
    this.init();
  },

  watch: {
    $route: {
      deep: true,
      handler() {
        this.productId = this.$route.params.id;
        this.init();
      },
    },
  },

  components: {
    MTextField,
    MAutoComplete,
  },
  methods: {
    async init() {
      // get infor of product
      let productResponse = await getInforProduct(this.productId);
      this.productData = productResponse?.data;

      // get existed attributes of product
      let attributesData = await getProductAttributes();
      let existedVariant = attributesData?.data;
      this.attributeList = this.attributeList.concat(existedVariant);

      // get existed brands
      const brandsData = await getBrands();
      const existedBrands = brandsData?.data;
      this.brands = existedBrands;

      // get existed categories
      const categoriesData = await getCategories();
      const existedCategories = categoriesData?.data;
      this.categories = existedCategories;

      this.passDataToForm(this.productData);
    },

    passDataToForm(product) {
      this.passDataInStep1(product);
      this.passDataInStep2(product);
      this.passDataInStep3(product);
    },

    passDataInStep1({
      productId,
      productName,
      description,
      originalPrice,
      originalQuantity,
      discount,
      imageLinks,
      brandName,
      categoryName,
    }) {
      this.productId = productId;
      this.productName = productName;
      this.description = description;
      this.originalPrice = originalPrice.slice(1).replaceAll('.', '');
      this.originalQuantity = originalQuantity;
      this.discount = discount;
      this.images = imageLinks;
      this.brandName = this.brands.filter((item) => item.name === brandName)[0];
      this.categoryName = this.categories.filter((item) => item.name === categoryName)[0];
    },

    passDataInStep2({ attributeAndVariants }) {
      this.attributes = attributeAndVariants.map((item) => ({
        attributeName: item.attributeName,
        selectedVariants: item.variantNames,
        variantNames: item.variantNames,
      }));
    },

    passDataInStep3({ combinations }) {
      this.combineVariants = combinations.map((item) => ({
        isUse: true,
        price: item.price.replaceAll('.', ''),
        productVariantName: item.productVariantName,
        quantity: item.quantity,
      }));
    },

    async uploadFile() {
      this.images = await upLoadImg(this.files);

      return {
        id: this.productId,
        productName: this.productName,
        description: this.description,
        originalPrice: this.originalPrice,
        originalQuantity: this.originalQuantity,
        imageLinks: this.images,
        discount: parseInt(this.discount),
        attributes: this.attributes.map((item) => ({
          attributeName: item.attributeName,
          variantNames: item.selectedVariants,
        })),
        combinations: this.combineVariants,
        brandName: this.brandName,
        categoryName: this.categoryName,
      };
    },

    deleteImage(index, image) {
      this.files = this.files.filter((item, i) => i !== index);
      console.log('remove: ', image);
    },

    handleAddAttribute() {
      let newAttribute =
        this.selectedAttribute.id !== -1
          ? {
              attributeName: this.selectedAttribute.attributeName,
              variantNames: this.selectedAttribute.variantNames,
              selectedVariants: [],
            }
          : {
              attributeName: this.selectedAttribute.attributeName + ' ' + this.numberAttributes++,
              variantNames: [],
              selectedVariants: [],
              disable: true,
            };
      let isExisted =
        this.attributes.filter((e) => e.attributeName === this.selectedAttribute.attributeName)
          .length > 0;
      this.attributes = isExisted ? [...this.attributes] : [...this.attributes, newAttribute];
      console.log('attribute', this.attributes);
    },
    handleAddNewVariant(name, index) {
      this.attributes = this.attributes.map((item) =>
        item.attributeName !== name
          ? item
          : {
              attributeName: item.attributeName,
              variantNames: [...item.variantNames, this.search[index]],
              selectedVariants: [...item?.selectedVariants, this.search[index]],
            },
      );
      this.search[index] = '';
    },
    handleUpdateSearchInput(value, index) {
      this.search[index] = value;
    },
    handleDeleteVariant(name) {
      this.attributes = this.attributes.filter((item) => item.attributeName !== name);
    },
    handleInputName(value, name) {
      this.attributes = this.attributes.map((item) => {
        if (item.attributeName === name) {
          return { ...item, attributeName: value };
        }
        return item;
      });
    },
    handleInputVariants(value, name) {
      this.attributes = this.attributes.map((item) => {
        if (item.attributeName === name) {
          return { ...item, selectedVariants: value };
        }
        return item;
      });
    },
    handleInputCombinationQuantity(value, name) {
      this.combineVariants = this.combineVariants.map((item) => {
        if (item.productVariantName === name) {
          return { ...item, quantity: parseInt(value) };
        } else return item;
      });
    },
    handleInputCombinationPrice(value, name) {
      this.combineVariants = this.combineVariants.map((item) => {
        if (item.productVariantName === name) {
          return { ...item, price: parseInt(value) };
        } else return item;
      });
    },
    handleSelectAdditionalCombination(combineName) {
      this.combineVariants = this.combineVariants.map((item) => {
        if (item.productVariantName === combineName) return { ...item, isUse: !item.isUse };
        else return item;
      });
    },
    onSubmitStep1() {
      this.loading.step1 = true;
      this.step = 2;
      this.completeStep = [1, ...this.completeStep];
      this.loading.step1 = false;
    },
    onSubmitStep2() {
      this.loading.step2 = true;
      if (this.isChangeAttribute) {
        let variantList = this.attributes.map((item) => item?.selectedVariants);
        this.combineVariants = combineVariants(variantList);
        this.step = 3;
        this.completeStep = [2, ...this.completeStep];
      } else {
        this.passDataInStep3({ ...this.productData });
        this.step = 3;
        this.completeStep = [2, ...this.completeStep];
      }
      this.loading.step2 = false;
    },
    // submit complete form add product
    async onSubmitStep3() {
      this.loading.step3 = true;
      // await this.upLoadFile();

      this.payload = await this.uploadFile();
      console.log('this.images', this.images);

      const product = await insertProduct(this.payload);
      console.log('product', JSON.stringify(product, null, 10));

      // console.log('payload', JSON.stringify(this.payload, null, 10));
      this.loading.step3 = false;
    },
  },
};
</script>

<style></style>
