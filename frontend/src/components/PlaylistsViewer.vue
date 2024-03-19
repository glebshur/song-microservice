<template>
    <div class="container-fluid">
      <div class="row justify-content-center my-2">
        <div class="col-sm-11 col-md-9 col-lg-7 col-xxl-5">
          <div class="input-group">
            <input type="text" class="form-control" v-model="name" :placeholder="$t('playlistsViewer.search.namePlaceholder')" @keyup.enter="updatePlaylists"/>
            <button id="searchButton" class="btn btn-primary" @click="updateSongs">{{$t('playlistsViewer.search.button')}}</button>
          </div>
        </div>
      </div>

      <error-block :errors="errors"/>
      
      <div class="song-list">
        <playlist-block v-for="playlist in playlists"
        :key="playlist.id"
        :playlist="playlist">
        </playlist-block>
      </div>

      <nav aria-label="Page navigation" class="col-sm-11 col-md-9 col-lg-7 col-xxl-5 fixed-nav">
        <div class="pagination justify-content-center btn-group">
          <button class="page-link btn btn-block bg-dark text-primary" :class="prevButtonClass" @click="firstPage" :disabled="isPrevButtonDisabled">
            1
          </button>
          <button id="prevButton" class="page-link btn btn-block bg-dark text-primary" :class="prevButtonClass" @click="prevPage" :disabled="isPrevButtonDisabled">
            &lt;&lt;
          </button>
          <div class="page-link bg-dark text-primary">
            {{ currentPage + 1 }}
          </div>
          <button id="nextButton" class="page-link btn btn-block bg-dark text-primary" :class="nextButtonClass" @click="nextPage" :disabled="isNextButtonDisabled">
            >>
          </button>
          <button class="page-link btn btn-block bg-dark text-primary" :class="nextButtonClass" @click="lastPage" :disabled="isNextButtonDisabled">
            {{totalPagesNumber}}
          </button>
        </div>
      </nav>
    </div>
  </template>
  
  <script>
  import PlaylistBlock from '@/components/PlaylistBlock.vue';
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import { useI18n } from 'vue-i18n';
  
  export default {
    name: 'playlists-viewer',
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t }
    },
    components: {
      PlaylistBlock,
      ErrorBlock
    },
    props: {
      playlistsPerPage: {
        type: Number,
        required: true
      },
      userId: {
        type: String,
        default: ""
      }
    },
    data() {
      return {
        currentPage: 0,
        name: "",
        errors: null
      }
    },
    computed: {
      playlists() {
        return this.$store.state.playlists;
      },
      totalPagesNumber (){
        return Math.ceil(this.$store.state.totalPlaylistsNumber / this.playlistsPerPage);
      },
      prevButtonClass() {
        return {
          'disabled' : this.isPrevButtonDisabled
        }
      },
      nextButtonClass() {
        return {
          'disabled' : this.isNextButtonDisabled
        }
      },
      isPrevButtonDisabled() {
        return this.currentPage <= 0;
      },
      isNextButtonDisabled() {
        return this.currentPage + 1 >= this.totalPagesNumber;
      }
    },
    methods: {
      updatePlaylists(){
        this.$store.dispatch('fetchPlaylists', {
          offset: this.currentPage * this.playlistsPerPage,
          limit: this.playlistsPerPage,
          name: this.name,
          userId: this.userId
        });
      },
      prevPage() {
        this.currentPage--;
      },
      nextPage() {
        this.currentPage++;
      },
      firstPage() {
        this.currentPage = 0;
      },
      lastPage() {
        this.currentPage = this.totalPagesNumber - 1;
      }
    },
    watch: {
      currentPage: {
        handler() {
          this.updatePlaylists()
        },
        immediate: true
      },
      playlists:{
        handler() {
          this.errors = null;
          if(!this.playlists.length){
            this.errors = [this.t('playlistsViewer.errors.notFound')]
          }
        },
        immediate: true
      }
    }
  }
</script>
