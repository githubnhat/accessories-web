<template>
  <v-container align="center" justify="center">
    <ValidationObserver v-slot="{ handleSubmit }">
      <v-form class="mt-5" @submit.prevent="handleSubmit(updateInfor)">
        <v-card>
          <v-row align="center" justify="center">
            <v-col cols="12" lg="2" md="12" class="mt-10">
              <v-app-bar flat color="rgba(0, 0, 0, 0)">
                <v-hover v-slot="{ hover }">
                  <div class="avatar-box">
                    <label for="file-input">
                      <v-avatar size="150" class="">
                        <img :src="`${newAvatar === '' ? defaultAvatar : newAvatar}`" />
                      </v-avatar>
                    </label>
                    <v-file-input
                      v-model="files"
                      id="file-input"
                      class="avatar-box_input"
                      hide-input
                      color="white"
                      v-show="hover"
                      prepend-icon="mdi-upload"
                      @change="previewFiles"
                      type="file"
                    >
                    </v-file-input>
                  </div>
                </v-hover>
                <v-spacer></v-spacer>
              </v-app-bar>
            </v-col>
            <v-divider vertical></v-divider>
            <v-col cols="12" lg="10" md="12">
              <v-card-text class="mt-5">
                <validation-provider name="Tên tài khoản" rules="required" v-slot="{ errors }">
                  <v-text-field
                    v-model="data.username"
                    label="Tên tài khoản"
                    name="username"
                    prepend-icon="mdi-account"
                    type="text"
                    color="primary accent-3"
                    readonly
                    disabled
                    :error-messages="errors"
                  >
                  </v-text-field>
                </validation-provider>

                <validation-provider name="Email" rules="required|existedEmail" v-slot="{ errors }">
                  <v-text-field
                    v-model="data.gmail"
                    label="Email"
                    name="email"
                    prepend-icon="mdi-email"
                    type="text"
                    color="primary accent-3"
                    clearable
                    :error-messages="errors"
                  >
                  </v-text-field>
                </validation-provider>
                <validation-provider name="Tên đầy đủ" rules="required" v-slot="{ errors }">
                  <v-text-field
                    v-model="data.fullName"
                    label="Tên đầy đủ"
                    name="fullname"
                    prepend-icon="mdi-human-edit"
                    type="text"
                    color="primary accent-3"
                    clearable
                    :error-messages="errors"
                  >
                  </v-text-field>
                </validation-provider>
              </v-card-text>
              <div class="text-center mb-5">
                <v-btn rounded color="primary" dark type="submit" :loading="loading.edit">
                  Lưu
                </v-btn>
              </div>
            </v-col>
          </v-row>
        </v-card>
      </v-form>
    </ValidationObserver>
    <!-- </v-col> -->
  </v-container>
</template>

<script>
import { getUserInfor, updateUserInfor } from '@/services';
import { upLoadAvatar } from '@/services/uploadFile';
export default {
  name: 'app-profile-contact-card',
  data() {
    return {
      files: [],
      newAvatar: '',
      defaultAvatar:
        'https://firebasestorage.googleapis.com/v0/b/minhnhat569-eecaa.appspot.com/o/images%2Fimage-not-found.png?alt=media&token=ae8ed2ef-b7ee-4921-b494-fe662aca6778',
      data: {
        username: '',
        gmail: '',
        fullName: '',
        thumbnail: null,
      },
      loading: {
        edit: false,
      },
    };
  },
  async created() {
    await this.initialize();
  },
  methods: {
    async initialize() {
      await this.readDataFromAPI();
    },
    async readDataFromAPI() {
      this.data = await getUserInfor();
      if (this.data.thumbnail != null) {
        this.newAvatar = this.data?.thumbnail;
      }
    },
    async updateInfor() {
      this.loading.edit = true;
      this.data.thumbnail = await upLoadAvatar(this.data.thumbnail);
      const data = await updateUserInfor(this.data);
      if (data != null) {
        await this.readDataFromAPI();
      }

      this.loading.edit = false;
    },
    previewFiles() {
      const file = this.files;
      this.data.thumbnail = file;
      const theReader = new FileReader();
      // Nhớ sử dụng async/await để chờ khi đã convert thành công image sang base64 thì mới bắt đầu gán cho biến newImage
      // đây là 1 kinh nghiệm của mình khi upload multiple ảnh
      theReader.onloadend = async () => {
        this.newAvatar = await theReader.result;
      };
      theReader.readAsDataURL(file);
    },
  },
};
</script>

<style>
.avatar-box {
  position: relative;
}
.avatar-box_input {
  position: absolute;
  bottom: 50px;
  left: 64px;
}
.avatar-box_input .v-icon::before {
  color: #ffffff;
  font-size: 60px;
}
</style>