<template>
  <v-autocomplete
    v-bind="$attrs"
    :value="selectedVariants"
    :items="variantItems"
    filled
    chips
    hide-details
    multiple
    dense
    flat
    solo
    outlined
    hide-selected
    :menu-props="menuSetting"
    @change="handleChange"
    :search-input.sync="searchValue"
    @update:search-input="handleUpdateSearchInput"
  >
    <template v-slot:selection="data">
      <v-chip
        class="my-1"
        v-bind="data.attrs"
        :input-value="data.selected"
        label
        color="background"
        close
        close-icon="mdi-close"
        @click:close="remove(data.item)"
      >
        <span class="text-17 font-weight-medium text-no-wrap">{{ data.item }} </span>
      </v-chip>
    </template>

    <template v-slot:item="{ item, on, attrs }">
      <v-list-item v-bind="attrs" v-on="on" class="autocomplete-item">
        <v-list-item-content>
          <v-list-item-title class="text-start text-17">{{ item }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </template>
    <template v-slot:no-data>
      <v-btn @click="handleAddNewVariant"> Thêm biến thể: {{ searchValue }} </v-btn>
    </template>
  </v-autocomplete>
</template>

<script>
export default {
  props: {
    menuSetting: {
      type: Object,
      default: () => ({
        closeOnClick: false,
        closeOnContentClick: false,
        disableKeys: true,
        openOnClick: false,
        maxHeight: 304,
        maxWidth: 330,
        offsetY: true,
        offsetOverflow: true,
        transition: false,
        rounded: 'lg',
        elevation: 12,
        contentClass: 'pa-2',
      }),
    },
    variantItems: {
      type: Array,
      default: () => [],
    },
    nameAttribute: {
      type: String,
      default: '',
    },
    indexSearch: {
      type: Number,
      default: 0,
    },
    search: {
      type: String,
      default: '',
    },
    selectedVariantsProp: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      selectedVariants: this.selectedVariantsProp,
      searchValue: this.search,
    };
  },
  methods: {
    handleChange(value) {
      this.selectedVariants = value;
      this.$emit('input', value, this.nameAttribute);
    },
    remove(item) {
      const index = this.selectedVariants.indexOf(item);
      if (index >= 0) this.selectedVariants.splice(index, 1);
    },
    handleUpdateSearchInput(value) {
      this.$emit('update-search-input', value, this.indexSearch);
    },
    handleAddNewVariant() {
      const isSelected =
        this.selectedVariants.filter((item) => item === this.searchValue).length > 0;
      if (this.searchValue !== '' && this.searchValue && !isSelected) {
        this.$emit('addVariant', this.nameAttribute, this.indexSearch);
        this.searchValue = '';
      }
    },
  },
};
</script>

<style></style>
