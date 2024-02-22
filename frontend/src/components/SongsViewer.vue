<template>
    <div class="container-fluid">
      <div class="row justify-content-center my-2">
        <div class="col-sm-11 col-md-9 col-lg-7 col-xxl-5">
          <div class="input-group">
            <input type="text" class="form-control" v-model="name" placeholder="Song name..." @keyup.enter="updateSongs"/>
            <input type="text" class="form-control" v-model="artist" placeholder="Artist / Band..." @keyup.enter="updateSongs"/>
            <input type="text" class="form-control" v-model="album" placeholder="Album..." @keyup.enter="updateSongs"/>
            <button id="searchButton" class="btn btn-primary" @click="updateSongs">Search</button>
          </div>
        </div>
      </div>

      <error-block :errors="errors"/>
      
      <div class="song-list">
        <song-block v-for="song in songs"
        :key="song.id"
        :song="song">
        </song-block>
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
  import SongBlock from '@/components/SongBlock.vue';
  import ErrorBlock from '@/components/ErrorBlock.vue';
  
  export default {
    name: 'songs-viewer',
    components: {
      SongBlock,
      ErrorBlock
    },
    props: {
      songsPerPage: {
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
        artist: "",
        album: "",
        errors: null,
      }
    },
    computed: {
      songs() {
        return this.$store.state.songs;
      },
      totalPagesNumber (){
        return Math.ceil(this.$store.state.totalSongsNumber / this.songsPerPage);
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
      updateSongs(){
        this.$store.dispatch('fetchSongs', {
          offset: this.currentPage * this.songsPerPage,
          limit: this.songsPerPage,
          name: this.name,
          artist: this.artist,
          album: this.album,
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
          this.updateSongs()
        },
        immediate: true
      },
      songs:{
        handler() {
          this.errors = null;
          if(!this.songs.length){
            this.errors = ["No results found!"]
          }
        },
        immediate: true
      }
    }
  }
</script>
