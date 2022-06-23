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
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field label="OTP" v-model="otp" required></v-text-field>
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
                      <v-card-text class="mt-5">
                        <h1
                          class="text-center display-2 primary--text text--accent-3 text-capitalize"
                        >
                          Đăng nhập
                        </h1>
                        <v-form class="mt-5" ref="formLogin">
                          <v-text-field
                            v-model="username"
                            label="Tên người dùng"
                            name="username"
                            prepend-icon="mdi-account"
                            type="text"
                            color="primary accent-3"
                            clearable
                          >
                          </v-text-field>
                          <v-text-field
                            v-model="password"
                            id="password"
                            label="Mật khẩu"
                            name="password"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                            @keypress.enter="login"
                          ></v-text-field>
                        </v-form>
                        <p class="caption text-center">Quên mật khẩu?!</p>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded color="primary" dark @click="login"> Đăng nhập </v-btn>
                      </div>
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
                      <v-card-text class="mt-5">
                        <h1
                          class="text-center display-2 primary--text text--accent-3 text-capitalize"
                        >
                          Tạo tài khoản
                        </h1>
                        <v-form class="mt-5">
                          <v-text-field
                            v-model="username"
                            label="Tên tài khoản"
                            name="username"
                            prepend-icon="mdi-account"
                            type="text"
                            color="primary accent-3"
                            clearable
                          >
                          </v-text-field>
                          <v-text-field
                            v-model="email"
                            label="Email"
                            name="email"
                            prepend-icon="mdi-email"
                            type="text"
                            color="primary accent-3"
                            clearable
                          >
                          </v-text-field>
                          <v-text-field
                            v-model="fullname"
                            label="Tên đầy đủ"
                            name="fullname"
                            prepend-icon="mdi-human-edit"
                            type="text"
                            color="primary accent-3"
                            clearable
                          >
                          </v-text-field>
                          <v-text-field
                            v-model="password"
                            id="password"
                            label="Mật khẩu"
                            name="password"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                          ></v-text-field>
                          <v-text-field
                            v-model="repassword"
                            id="repassword"
                            label="Nhập lại mật khẩu"
                            name="repassword"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                          ></v-text-field>
                        </v-form>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded color="primary" dark> Đăng ký </v-btn>
                      </div>
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
import { doLogin } from '@/services';
export default {
  name: 'LogIn',
  data() {
    return {
      step: 1,
      username: '',
      password: '',
      repassword: '',
      email: '',
      fullname: '',
      otpDialog: false,
      otp: '',
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
      doLogin('http://localhost:8081/api/v1/login', {
        username: this.username,
        password: this.password,
      });
    },
    verifyOTP() {
      // service verify otp here
    },
  },
};
</script>

<style scoped></style>
