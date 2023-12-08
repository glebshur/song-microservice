<template>
    <div>
      <Header /> 
      <div>Songs you've uploaded</div>
      <songs-viewer :songs-per-page="3" :user-id="userId"/>
    </div>
  </template>
  
  <script>
  import Header from '@/components/Header.vue';
  import SongsViewer from '@/components/SongsViewer.vue';
  import keycloakService from '@/security/keycloak';
  
  export default {
    name: 'songs-home-view',
    components: {
      Header,
      SongsViewer
      
    },
    data() {
      return {
        message: this.$route.query.message,
        userId: keycloakService.tokenParsed.sub
      }
    },
  }
  </script>