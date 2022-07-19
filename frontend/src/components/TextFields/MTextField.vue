<template>
  <validation-provider :name="`${nameInput}`" :rules="`${rules}`" v-slot="{ errors }">
    <v-text-field
      v-bind="$attrs"
      @input="handleInput"
      outlined
      dense
      hide-details="auto"
      :error-messages="errors"
    >
      <template v-slot:prepend>
        <slot name="prepend" />
      </template>

      <template v-slot:append-outer>
        <slot name="append-outer" />
      </template>
    </v-text-field>
  </validation-provider>
</template>

<script>
export default {
  name: 'MTextField',
  props: {
    nameInput: {
      type: String,
      default: () => '',
    },
    rules: {
      type: String,
      default: () => '',
    },
    nameVar: {
      type: String,
      default: () => '',
    },
  },
  methods: {
    handleInput(value) {
      this.$emit('input', value, this.nameVar);
    },
  },
};
</script>

<style></style>
