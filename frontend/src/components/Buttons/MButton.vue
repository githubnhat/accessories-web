<template>
  <v-btn v-bind="$attrs" @click="$emit('click', $event)">
    <slot />
  </v-btn>
</template>

<script>
export default {
  name: 'MButton',
  emits: ['click'],

  props: {},
};
</script>

<style></style>
