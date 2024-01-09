<template>
  <div class="container-fluid">
    <Header /> 
    <div class="bg-image">
    <div class="fs-3 text-white">Songs Microservice</div>

    <info-block :message="message"/>
    <songs-viewer :songs-per-page="5"/>
    </div>
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