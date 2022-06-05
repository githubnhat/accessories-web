<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> | <router-link to="/about">About</router-link>|
      <router-link to="/login">Login</router-link>|
      <router-link to="/register">Register</router-link>
    </nav>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'MNavigation',
};
</script>

<style lang="scss"></style>
