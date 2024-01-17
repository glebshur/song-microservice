<template>
    <header>
        <nav class="navbar navbar-expand-md navbar-dark bg-dark">
          <div class="container-fluid">
            <a class="navbar-brand" href="/">
              <img src="https://thumbs2.imgbox.com/96/ad/OUnIls2P_t.png">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuItems" aria-controls="menuItems" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menuItems">
              <ul class="navbar-nav me-auto mb-2 mb-md-0">
                  <li class="nav-item">
                    <a class="nav-link active text-primary" href="/">Home</a>
                  </li>
                  <li class="nav-item" v-if="hasAdminRole()">
                    <a class="nav-link active text-primary" href="/song/upload">Upload</a>
                  </li>
                  <li class="nav-item" v-if="hasAdminRole()">
                    <a class="nav-link active text-primary" href="/mysongs">My Songs</a>
                  </li>
              </ul>
            </div>
            <button class="btn btn-outline-danger" v-if="authenticated" @click="logout">Logout</button>
            <button class="btn btn-outline-success" v-else @click="login">Login</button>
          </div>
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