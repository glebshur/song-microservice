<template>
  <Header />
  <div class="bg-image">
    <div class="d-flex justify-content-center p-3">
      <div class="bg-dark bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-6 col-xl-6 col-xxl-4 rounded">
        <p>Here are the songs you uploaded. Enjoy your playlist!</p>
      </div>
    </div>
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
<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/df/f6/9A8vtB1d_o.jpg');
}

</style>