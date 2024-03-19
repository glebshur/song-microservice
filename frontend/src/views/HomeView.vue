<template>
  <Header /> 
  <div class="bg-image">
    <div class="d-flex justify-content-center p-3">
      <div class="bg-dark bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-6 col-xl-6 col-xxl-4 rounded">
        <p>{{ $t('home.welcomeMessage') }}</p>  
      </div>
    </div>

    <div class="py-2">
      <ul class="nav nav-tabs d-flex justify-content-center">
        <li class="nav-item" style="cursor: pointer;">
          <a class="nav-link" :class="{ active: activeTab === 'songs' }" @click="changeTab('songs')">{{ $t('home.tabs.songs') }}</a>
        </li>
        <li class="nav-item" style="cursor: pointer;">
          <a class="nav-link" :class="{ active: activeTab === 'playlists' }" @click="changeTab('playlists')">{{$t('home.tabs.playlists')}}</a>
        </li>
      </ul>
    </div>
    
    <info-block :message="message"/>
    <songs-viewer v-show="activeTab === 'songs'" :songs-per-page="5"/>
    <playlists-viewer v-show="activeTab === 'playlists'" :playlists-per-page="5"/>

  </div>
</template>

<script>
import InfoBlock from '@/components/InfoBlock.vue';
import Header from '@/components/Header.vue';
import SongsViewer from '@/components/SongsViewer.vue';
import PlaylistsViewer from '@/components/PlaylistsViewer.vue';
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
    SongsViewer,
    PlaylistsViewer
    
  },
  data() {
    return {
      message: this.$route.query.message,
      activeTab: 'songs',
    }
  },
  methods: {
    changeTab(newTab) {
      this.activeTab = newTab;
    }
  }
}
</script>