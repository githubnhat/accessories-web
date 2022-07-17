<template>
  <div class="admin-login-container">
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row justify="center">
          <v-dialog
            v-model="otpDialog"
            persistent
            max-width="600px"
            @keydown.esc="otpDialog = false"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn v-show="false" color="primary" dark v-bind="attrs" v-on="on">
                Open Dialog
              </v-btn>
            </template>
            <v-card>
              <ValidationObserver v-slot="{ handleSubmit }">
                <v-form class="mt-5" ref="formOTP" @submit.prevent="handleSubmit(verifyOTP)">
                  <v-card-title>
                    <span class="text-h5">Xác thực email</span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-row>
                        <v-col cols="12">
                          <validation-provider
                            name="OTP"
                            rules="required|min:8|max:8"
                            v-slot="{ errors }"
                          >
                            <v-text-field
                              label="OTP"
                              v-model="otpCode"
                              required
                              :error-messages="errors"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="otpDialog = false"> Đóng </v-btn>
                    <v-btn color="blue darken-1" text type="submit" :loading="loading.verify">
                      Xác nhận
                    </v-btn>
                  </v-card-actions></v-form
                >
              </ValidationObserver>
            </v-card>
          </v-dialog>
        </v-row>
        <v-row align="center" justify="center" class="pt-10">
          <v-col cols="12" sm="8" md="8">
            <v-card class="elevation-12" rounded="10">
              <v-row>
                <v-col cols="4"></v-col>
                <v-col cols="4">
                  <ValidationObserver v-slot="{ handleSubmit }">
                    <v-form class="mt-5" ref="formLogin" @submit.prevent="handleSubmit(login)">
                      <v-card-text class="mt-5">
                        <h1
                          class="text-center display-2 primary--text text--accent-3 text-capitalize"
                        >
                          Đăng nhập
                        </h1>
                        <v-row no-gutters>
                          <v-col cols="12">
                            <validation-provider
                              name="Tên đăng nhập"
                              rules="required"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="loginInput.username"
                                label="Tên người dùng"
                                name="username"
                                prepend-icon="mdi-account"
                                type="text"
                                color="primary accent-3"
                                clearable
                                :error-messages="errors"
                              >
                              </v-text-field>
                            </validation-provider>
                          </v-col>
                        </v-row>

                        <v-row no-gutters>
                          <v-col cols="12">
                            <validation-provider
                              name="Mật khẩu"
                              rules="required|min:4"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="loginInput.password"
                                id="password"
                                label="Mật khẩu"
                                name="password"
                                type="password"
                                prepend-icon="mdi-lock"
                                clearable
                                @keypress.enter="login"
                                :error-messages="errors"
                              ></v-text-field>
                            </validation-provider>
                          </v-col>
                        </v-row>

                        <v-row no-gutters>
                          <v-col cols="12">
                            <v-checkbox
                              v-model="loginInput.isRememberMe"
                              label="Ghi nhớ đăng nhập"
                              class="text-center"
                            ></v-checkbox>
                          </v-col>
                        </v-row>

                        <p class="caption text-center">Quên mật khẩu?!</p>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded color="primary" dark type="submit" :loading="loading.login">
                          Đăng nhập
                        </v-btn>
                      </div>
                    </v-form>
                  </ValidationObserver>
                </v-col>
                <v-col cols="4"></v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </div>
</template>

<script>
import { doAdminLogin } from '@/services';
import { mapState } from 'vuex';
import router from '@/router';
export default {
  name: 'LogIn',
  data() {
    return {
      loginInput: {
        username: '',
        password: '',
        isRememberMe: false,
      },
      loading: {
        login: false,
      },
    };
  },
  props: {
    source: {
      type: String,
      default: '',
    },
  },
  computed: {
    ...mapState({
      accessToken: (state) => state._accessToken.state.accessToken,
    }),
  },
  methods: {
    reset() {
      this.$refs.form.reset();
    },
    async login() {
      this.loading.login = true;
      const { data, status } = await doAdminLogin(this.loginInput);
      if (status === 200) {
        const token = data?.data?.accessToken;
        this.$store.commit('_accessToken/setAccessToken', token);
        router.push('/admin');
      } else alert('Đăng nhập không thành công. Vui lòng thử lại!');
      this.loading.login = false;
    },
  },
};
</script>

<style scoped></style>
