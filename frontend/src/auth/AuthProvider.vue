<template>
  <div class="auth"><slot /></div>
</template>

<script>
import router from '@/router';
export default {
  name: 'AuthProvider',
  created() {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) router.push('/login');
  },
};
</script>

<style></style>
