<template>
  <Header /> 
  <div class="bg-image">
    <div class="d-flex justify-content-center p-3">
      <div class="bg-dark bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-6 col-xl-6 col-xxl-4 rounded">
        <p>
          Welcome to <b>Micro Song</b>, a website that not only allows you to share music, 
          but also to have unlimited access to it, as the 
          system is highly reliable thanks to its microservice architecture.
        </p>  
      </div>
    </div>
        
    <info-block :message="message"/>
    <songs-viewer :songs-per-page="5"/>
  </div>
</template>

<script>
import InfoBlock from '@/components/InfoBlock.vue';
import Header from '@/components/Header.vue';
import SongsViewer from '@/components/SongsViewer.vue';

export default {
  name: 'songs-home-view',
  components: {
    Header,
    InfoBlock,
    SongsViewer
    
  },
  data() {
    return {
      message: this.$route.query.message,
    }
  },
}
</script>