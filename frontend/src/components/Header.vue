<template>
    <header>
        <nav>
            <ul>
                <li><a href="/">Home</a></li>
                <li v-if="hasAdminRole()"><a href="/song/upload">Upload</a></li>
                <li v-if="hasAdminRole()"><a href="/mysongs">My Songs</a></li>
            </ul>
            <button v-if="authenticated" @click="logout">Logout</button>
            <button v-else @click="login">Login</button>
        </nav>
    </header>
</template>

<script>
import keycloakService from '@/security/keycloak';
  
  export default {
    name: 'custome-header',
    data() {
      return {
        authenticated: false,
      };
    },
    mounted() {
      this.authenticated = keycloakService.authenticated;
    },
    methods: {
      logout() {
        keycloakService.logout({redirectUri: process.env.VUE_APP_BASE_URL});
      },
      login() {
        keycloakService.login({redirectUri: process.env.VUE_APP_BASE_URL})
      },
      hasAdminRole(){
        return keycloakService.hasResourceRole('ADMIN')
      }
    },
  };
</script>