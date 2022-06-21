<template>
  <v-text-field v-bind="$attrs" @input="handleInput" outlined dense hide-details="auto">
    <template v-slot:prepend>
      <slot name="prepend" />
    </template>

    <template v-slot:append-outer>
      <slot name="append-outer" />
    </template>
  </v-text-field>
</template>

<script>
export default {
  name: 'MTextField',
  props: {
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
