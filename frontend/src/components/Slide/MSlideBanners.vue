<template>
  <v-carousel cycle height="500" hide-delimiter-background show-arrows>
    <v-carousel-item v-for="(slide, i) in slides" :key="i">
      <v-sheet color="transparent">
        <v-row class="fill-height" align="center" justify="center">
          <div>
            <img :src="slide" alt="slide" />
          </div>
        </v-row>
      </v-sheet>
    </v-carousel-item>
  </v-carousel>
</template>

<script>
import { SLIDE_LIST } from '@/utils/mocks';

export default {
  name: 'MSlideBanners',
  data: () => ({
    colors: ['indigo', 'warning', 'pink darken-2', 'red lighten-1', 'deep-purple accent-4'],
    slides: SLIDE_LIST,
  }),
  props: {
    items: {
      type: Array,
      default: () => [],
    },
  },
};
</script>

<style></style>
