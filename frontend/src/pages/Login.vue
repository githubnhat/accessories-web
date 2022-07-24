<template>
  <div class="login">
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
              <v-window v-model="step">
                <v-window-item :value="1">
                  <v-row>
                    <v-col cols="12" md="8">
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
                              <v-col cols="4"></v-col>
                              <v-col cols="4">
                                <v-checkbox
                                  v-model="loginInput.isRememberMe"
                                  label="Ghi nhớ đăng nhập"
                                  class="text-center"
                                ></v-checkbox>
                              </v-col>
                              <v-col cols="4"></v-col>
                            </v-row>

                            <p class="caption text-center">Quên mật khẩu?!</p>
                          </v-card-text>
                          <div class="text-center mb-5">
                            <v-btn
                              rounded
                              color="primary"
                              dark
                              type="submit"
                              :loading="loading.login"
                            >
                              Đăng nhập
                            </v-btn>
                          </div>
                        </v-form>
                      </ValidationObserver>
                    </v-col>
                    <v-col cols="12" md="4" class="primary accent-3">
                      <v-card-text class="white--text mt-5">
                        <h1 class="text-center display-1">Accessories Shop</h1>
                        <h5 class="text-center">Chưa có tài khoản?! Đăng ký ngay...</h5>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded outlined dark @click="step++">Đăng ký</v-btn>
                      </div>
                    </v-col>
                  </v-row>
                </v-window-item>
                <v-window-item :value="2">
                  <v-row
                    ><v-col cols="12" md="4" class="primary accent-3">
                      <v-card-text class="white--text mt-5">
                        <h1 class="text-center display-1">Accessories Shop</h1>
                        <h5 class="text-center">Đã có tài khoản?! Đăng nhập ngay</h5>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded outlined dark @click="step--">Đăng nhập</v-btn>
                      </div>
                    </v-col>
                    <v-col cols="12" md="8">
                      <ValidationObserver v-slot="{ handleSubmit }">
                        <v-form class="mt-5" @submit.prevent="handleSubmit(register)">
                          <v-card-text class="mt-5">
                            <h1
                              class="text-center display-2 primary--text text--accent-3 text-capitalize"
                            >
                              Tạo tài khoản
                            </h1>

                            <validation-provider
                              name="Tên tài khoản"
                              rules="required"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="registerInput.username"
                                label="Tên tài khoản"
                                name="username"
                                prepend-icon="mdi-account"
                                type="text"
                                color="primary accent-3"
                                clearable
                                :error-messages="errors"
                              >
                              </v-text-field>
                            </validation-provider>

                            <validation-provider
                              name="Email"
                              rules="required|email"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="registerInput.email"
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
                            <validation-provider
                              name="Tên đầy đủ"
                              rules="required"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="registerInput.fullname"
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

                            <validation-provider
                              name="Mật khẩu"
                              rules="required|min:8"
                              v-slot="{ errors }"
                              vid="password"
                            >
                              <v-text-field
                                v-model="registerInput.password"
                                id="password"
                                label="Mật khẩu"
                                type="password"
                                prepend-icon="mdi-lock"
                                clearable
                                :error-messages="errors"
                              >
                              </v-text-field>
                            </validation-provider>

                            <validation-provider
                              name="Xác nhận mật khẩu"
                              rules="required|min:8|confirmed:password"
                              v-slot="{ errors }"
                            >
                              <v-text-field
                                v-model="registerInput.repassword"
                                label="Xác nhận mật khẩu"
                                type="password"
                                prepend-icon="mdi-lock"
                                clearable
                                :error-messages="errors"
                              >
                              </v-text-field>
                            </validation-provider>
                          </v-card-text>
                          <div class="text-center mb-5">
                            <v-btn
                              rounded
                              color="primary"
                              dark
                              type="submit"
                              :loading="loading.register"
                            >
                              Đăng ký
                            </v-btn>
                          </div>
                        </v-form>
                      </ValidationObserver>
                    </v-col>
                  </v-row>
                </v-window-item>
              </v-window>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </div>
</template>

<script>
import { doLogin, doRegister, verifyOTP } from '@/services';
import { mapState } from 'vuex';
import router from '@/router';
export default {
  name: 'LogIn',
  data() {
    return {
      step: 1,
      loginInput: {
        username: '',
        password: '',
        isRememberMe: false,
      },
      registerInput: {
        username: '',
        password: '',
        repassword: '',
        email: '',
        fullname: '',
      },
      userId: null,
      otpDialog: false,
      otpCode: '',
      loading: {
        register: false,
        login: false,
        verify: false,
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
      const { data, status } = await doLogin(this.loginInput);
      if (status === 200) {
        const token = data?.data?.accessToken;
        this.$store.commit('_accessToken/setAccessToken', token);
        router.push('/');
      } else alert('Đăng nhập không thành công. Vui lòng thử lại!');
      this.loading.login = false;
    },
    async register() {
      this.loading.register = true;

      const payload = {
        username: this.registerInput.username,
        password: this.registerInput.password,
        fullName: this.registerInput.fullname,
        gmail: this.registerInput.email,
        roleCode: 'ROLE_USER',
      };
      const response = await doRegister(payload);
      if (response?.status === 'Success') {
        this.userId = response?.data?.id;
        this.otpDialog = true;
      } else {
        alert(response?.message);
      }
      this.loading.register = false;
    },
    async verifyOTP() {
      this.loading.verify = true;
      // service verify otpCode here
      await verifyOTP({
        userId: this.userId,
        otpCode: this.otpCode,
      });
      this.loading.verify = false;
    },
  },
};
</script>

<style scoped></style>
