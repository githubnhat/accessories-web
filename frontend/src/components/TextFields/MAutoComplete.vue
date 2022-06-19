<template>
  <v-autocomplete
    v-bind="$attrs"
    v-model="selectedVariants"
    :items="variants"
    filled
    chips
    hide-details
    multiple
    dense
    flat
    solo
    outlined
    hide-selected
    append-icon=""
    :menu-props="menuSetting"
    @change="handleChange"
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
  },
  data() {
    return {
      selectedVariants: [],
      variants: this.variantItems,
    };
  },
  methods: {
    handleChange(value) {
      this.selectedVariants = value;
      this.$emit('input', value, this.nameAttribute);
    },
    remove(item) {
      console.log('remove', item);
      const index = this.selectedVariants.indexOf(item);
      console.log(this.selectedVariants);
      console.log('index', index);
      if (index >= 0) this.selectedVariants.splice(index, 1);
    },
  },
};
</script>

<style></style>
