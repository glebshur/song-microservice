<template>
    <div>
      <h1>This is a Secured Page</h1>
      <p v-if="authenticated">You are authenticated!</p>
      <p v-else>You are not authenticated. Please log in.</p>
      <button v-if="authenticated" @click="logout">Logout</button>
      <p>{{ token }}</p>
      <p>{{ hasAdminRole }}</p>
    </div>
  </template>
  
  <script>
  import keycloakService from '@/security/keycloak';
  
  export default {
    data() {
      return {
        authenticated: false,
        token: '',
        hasAdminRole: false,
      };
    },
    mounted() {
      this.authenticated = keycloakService.authenticated;
      this.token = keycloakService.token
      this.hasAdminRole = keycloakService.hasResourceRole('ADMIN')
    },
    methods: {
      logout() {
        keycloakService.logout({redirectUri: 'http://192.168.86.129:8081/'});
      },
    },
  };
  </script>