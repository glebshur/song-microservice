<template>
  <div class="home">
    <Header /> 
    Songs Microservice

    <info-block :message="message"/>
    <songs-viewer :songs-per-page="3"/>
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
