<template>
  <auth-provider>
    <div>This is profile page</div>
    <v-form @submit.prevent="uploadFile">
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
          <template v-slot:selection="{ text }">
            <v-chip small label color="primary">
              {{ text }}
            </v-chip>
          </template></v-file-input
        >
      </v-col>
      <div class="d-flex justify-end">
        <v-btn color="primary" type="submit"> Tiếp tục </v-btn>
      </div>
    </v-form>
    <div v-if="images.length > 0">
      <div v-for="img in images" :key="img.src"><img :src="img.src" alt="" /></div>
    </div>
  </auth-provider>
</template>

<script>
import AuthProvider from '@/auth/AuthProvider.vue';
import { upLoadImg } from '@/services/uploadFile';
export default {
  name: 'ProfilePage',
  data() {
    return {
      files: [],
      images: [],
    };
  },

  components: { AuthProvider },
  methods: {
    async uploadFile() {
      this.images = await upLoadImg(this.files);
      this.files = null;
    },
  },
};
</script>

<style></style>
