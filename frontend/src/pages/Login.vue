<template>
  <div class="login">
    <v-main>
      <v-container class="fill-height" fluid>
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
                          Login to shopping
                        </h1>
                        <v-form class="mt-5" ref="formLogin">
                          <v-text-field
                            v-model="username"
                            label="User Name"
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
                            label="Password"
                            name="password"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                            @keypress.enter="login"
                          ></v-text-field>
                        </v-form>
                        <p class="caption text-center">Forgot password?!</p>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded color="primary" dark @click="login"> LOG IN </v-btn>
                      </div>
                    </v-col>
                    <v-col cols="12" md="4" class="primary accent-3">
                      <v-card-text class="white--text mt-5">
                        <h1 class="text-center display-1">Hi, Bitches!!</h1>
                        <h5 class="text-center">
                          Have no account?! I'll give you one right now...
                        </h5>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded outlined dark @click="step++">REGISTER</v-btn>
                      </div>
                    </v-col>
                  </v-row>
                </v-window-item>
                <v-window-item :value="2">
                  <v-row
                    ><v-col cols="12" md="4" class="primary accent-3">
                      <v-card-text class="white--text mt-5">
                        <h1 class="text-center display-1">Hi, Bitches!!</h1>
                        <h5 class="text-center">
                          Had an account?! Let's sign in to shopping right now...
                        </h5>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded outlined dark @click="step--">LOG IN</v-btn>
                      </div>
                    </v-col>
                    <v-col cols="12" md="8">
                      <v-card-text class="mt-5">
                        <h1
                          class="text-center display-2 primary--text text--accent-3 text-capitalize"
                        >
                          Create an Account
                        </h1>
                        <v-form class="mt-5">
                          <v-text-field
                            v-model="username"
                            label="User Name"
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
                            label="Full Name"
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
                            label="Password"
                            name="password"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                          ></v-text-field>
                          <v-text-field
                            v-model="repassword"
                            id="repassword"
                            label="Retype Password"
                            name="repassword"
                            type="password"
                            prepend-icon="mdi-lock"
                            clearable
                          ></v-text-field>
                        </v-form>
                      </v-card-text>
                      <div class="text-center mb-5">
                        <v-btn rounded color="primary" dark> REGISTER </v-btn>
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
  },
};
</script>

<style scoped></style>
