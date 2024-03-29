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
    path: '/brand/:code',
    name: 'brand',
    component: () => import(/* webpackChunkName: "all" */ '@/pages/ProductsShow.vue'),
  },
  {
    path: '/category/:code',
    name: 'category',
    component: () => import(/* webpackChunkName: "all" */ '@/pages/ProductsShow.vue'),
  },
  {
    path: '/search/:keyword',
    name: 'search',
    component: () => import('@/pages/SearchPage.vue'),
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import(/* webpackChunkName: "profile" */ '@/pages/UserProfilePage.vue'),
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
  {
    path: '/detail/:id',
    name: 'detail',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/pages/ProductDetailView.vue'),
  },
  {
    path: '/shopping-cart',
    name: 'shopping-cart',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/pages/ShoppingCart.vue'),
  },
  {
    path: '/update/:id',
    name: 'update-product',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '@/components/Forms/Admin/MUpdateProductForm.vue'),
  },
  {
    path: '/checkout',
    name: 'checkout',
    // route level code-splitting
    // this generates a separate chunk (admin.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "admin-login" */ '@/pages/PaymentPage.vue'),
    props: (route) => ({
      ...route.params,
      itemIds: [],
    }),
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
