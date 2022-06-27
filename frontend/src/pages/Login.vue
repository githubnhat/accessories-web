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
              <v-card-title>
                <span class="text-h5">Xác thực email</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field label="OTP" v-model="otpCode" required></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="otpDialog = false"> Đóng </v-btn>
                <v-btn color="blue darken-1" text @click="verifyOTP"> Xác nhận </v-btn>
              </v-card-actions>
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

                            <p class="caption text-center">Quên mật khẩu?!</p>
                          </v-card-text>
                          <div class="text-center mb-5">
                            <v-btn rounded color="primary" dark type="submit"> Đăng nhập </v-btn>
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
                              rules="required|min:4"
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
                              rules="required|min:4|confirmed:password"
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
                            <v-btn rounded color="primary" dark type="submit"> Đăng ký </v-btn>
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
import { doLogin, doRegister } from '@/services';
export default {
  name: 'LogIn',
  data() {
    return {
      step: 1,
      loginInput: {
        username: '',
        password: '',
      },
      registerInput: {
        username: '',
        password: '',
        repassword: '',
        email: '',
        fullname: '',
      },
      userId: null,
      otpDialog: true,
      otpCode: '',
    };
  },
  props: {
    source: {
      type: String,
      default: '',
    },
  },
  methods: {
    reset() {
      this.$refs.form.reset();
    },
    login() {
      doLogin(this.loginInput);
    },
    async register() {
      const payload = {
        username: this.registerInput.username,
        password: this.registerInput.password,
        fullName: this.registerInput.fullname,
        gmail: this.registerInput.email,
        roleCode: 'ROLE_USER',
      };
      console.log('payload', payload);
      const response = await doRegister(payload);
      console.log('response', response);
    },
    verifyOTP() {
      // service verify otpCode here
    },
  },
};
</script>

<style scoped></style>
