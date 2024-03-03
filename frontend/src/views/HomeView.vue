<template>
  <Header /> 
  <div class="bg-image">
    <div class="d-flex justify-content-center p-3">
      <div class="bg-dark bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-6 col-xl-6 col-xxl-4 rounded">
        <p>{{ $t('home.welcomeMessage') }}</p>  
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
import { useI18n } from 'vue-i18n';

export default {
  name: 'songs-home-view',
  setup() {
    const { t } = useI18n({useScope: 'global'});
    return { t }
  },
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