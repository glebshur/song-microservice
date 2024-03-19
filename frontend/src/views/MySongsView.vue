<template>
  <Header />
  <div class="bg-image">
    <div class="d-flex justify-content-center p-3">
      <div class="bg-dark bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-6 col-xl-6 col-xxl-4 rounded">
        <p>{{$t('mySongs.welcomeMessage')}}</p>
      </div>
    </div>

    <div class="py-2">
      <ul class="nav nav-tabs d-flex justify-content-center">
        <li v-if="keycloakService.hasResourceRole('ADMIN')" class="nav-item" style="cursor: pointer;">
          <a class="nav-link" :class="{ active: activeTab === 'songs' }" @click="changeTab('songs')">{{ $t('mySongs.tabs.songs') }}</a>
        </li>
        <li class="nav-item" style="cursor: pointer;">
          <a class="nav-link" :class="{ active: activeTab === 'playlists' }" @click="changeTab('playlists')">{{ $t('mySongs.tabs.playlists') }}</a>
        </li>
      </ul>
    </div>

    <songs-viewer v-show="activeTab === 'songs'" :songs-per-page="5" :user-id="userId"/>
    <playlists-viewer v-show="activeTab === 'playlists'" :playlists-per-page="5" :user-id="userId"/>
  </div>
</template>
  
<script>
  import Header from '@/components/Header.vue';
  import SongsViewer from '@/components/SongsViewer.vue';
  import PlaylistsViewer from '@/components/PlaylistsViewer.vue';
  import keycloakService from '@/security/keycloak';
  import { useI18n } from 'vue-i18n';
  
  export default {
    name: 'songs-home-view',
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t, keycloakService}
    },
    components: {
      Header,
      SongsViewer,
      PlaylistsViewer
      
    },
    data() {
      return {
        message: this.$route.query.message,
        userId: keycloakService.tokenParsed.sub,
        activeTab: "playlists"
      }
    },
    methods: {
      changeTab(newTab) {
        this.activeTab = newTab;
      }
    }
  }
</script>
<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/df/f6/9A8vtB1d_o.jpg');
}

</style>