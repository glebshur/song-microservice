<template>
    <header>
        <nav>
            <ul>
                <li><a href="/">Home</a></li>
                <li v-if="hasAdminRole"><a href="/song/upload">Upload</a></li>
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
        keycloakService.logout({redirectUri: 'http://192.168.86.129:8081/'});
      },
      login() {
        keycloakService.login({redirectUri: 'http://192.168.86.129:8081/'})
      },
      hasAdminRole(){
        keycloakService.hasResourceRole('ADMIN')
      }
    },
  };
</script>