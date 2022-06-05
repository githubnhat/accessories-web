<template>
  <v-text-field v-bind="$attrs" class="nde-text-field" outlined dense hide-details="auto" clearable>
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
  methods: {},
};
</script>

<style></style>
