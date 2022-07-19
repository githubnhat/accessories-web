import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import HomeView from '@/pages/HomeView.vue';
import store from '@/store/index';
import jwt_decode from 'jwt-decode';

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/pages/AboutView.vue'),
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '@/pages/Login.vue'),
  },
  {
    path: '/all',
    name: 'all',
    component: () => import(/* webpackChunkName: "all" */ '@/pages/AllProductView.vue'),
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import(/* webpackChunkName: "profile" */ '@/pages/Profile.vue'),
  },
  {
    path: '/admin',
    name: 'admin',
    beforeEnter: (to, from, next) => {
      const token: Token = jwt_decode(store.getters['_accessToken/getAccessToken']);
      const role = token.role;

      if (to.name !== 'login' && !token) {
        next({ name: 'login' });
      }
      if (role !== 'ROLE_ADMIN') {
        next({ name: 'home' });
      } else next();
    },
    // route level code-splitting
    // this generates a separate chunk (admin.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "admin" */ '@/pages/AdminPage.vue'),
  },
  {
    path: '/admin-login',
    name: 'admin-login',
    // route level code-splitting
    // this generates a separate chunk (admin.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "admin-login" */ '@/pages/AdminLogin.vue'),
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

interface Token {
  access_token: boolean;
  exp: Date;
  fullName: string;
  iat: Date;
  role: string;
  sub: string;
  username: string;
}

export default router;
