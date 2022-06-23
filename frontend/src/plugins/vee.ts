import Vue from 'vue';
import { ValidationProvider, ValidationObserver, extend } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';
import { required, email, min, max, between, numeric, confirmed } from 'vee-validate/dist/rules';

// add all available rules in vee validate
for (const [rule, validation] of Object.entries(rules)) {
  extend(rule, {
    ...validation,
  });
}

extend('required', {
  ...required,
  message: (field) => field + ' không được để trống.',
});

extend('email', {
  ...email,
  message: 'Email không hợp lệ.',
});

extend('min', {
  ...min,
  message: (field, value) => field + ' tối thiểu là ' + value?.length + ' ký tự.',
});

extend('max', {
  ...max,
  message: (field, value) => field + ' tối đa là ' + value?.length + ' ký tự.',
});

extend('between', {
  ...between,
  message: (field, value) =>
    field + ' phải nằm trong khoảng ' + value.min + ' đến' + value.max + '.',
});

extend('numeric', {
  ...numeric,
  message: 'Dữ liệu phải thuộc kiểu số.',
});

extend('confirmed', {
  ...confirmed,
  message: (field) => field + ' không khớp.',
});
Vue.component('ValidationProvider', ValidationProvider);
Vue.component('ValidationObserver', ValidationObserver);
