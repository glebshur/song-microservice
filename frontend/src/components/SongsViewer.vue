<template>
    <div class="container-fluid">
      <div class="row justify-content-center my-2">
        <div class="col-9">
          <div class="input-group">
            <input type="text" class="form-control" v-model="name" placeholder="Song name..." @keyup.enter="updateSongs"/>
            <input type="text" class="form-control" v-model="artist" placeholder="Artist / Band..." @keyup.enter="updateSongs"/>
            <input type="text" class="form-control" v-model="album" placeholder="Album..." @keyup.enter="updateSongs"/>
            <button class="btn btn-primary" @click="updateSongs">Search</button>
          </div>
        </div>
      </div>
      
      <div class="song-list">
        <song-block v-for="song in songs"
        :key="song.id"
        :song="song">
        </song-block>
      </div>
  
      <div v-if="!songs.length">
        No results found!
      </div>
  
      <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
          <li class="page-item">
            <button :class="prevButtonClass" @click="prevPage" :disabled="currentPage <= 0">
              Previous
            </button>
          </li>
          <li class="page-item">
            <button :class="nextButtonClass" @click="nextPage" :disabled="songs.length < songsPerPage">
              Next
            </button>
          </li>
        </ul>
      </nav>
      <!-- <button @click="prevPage" :disabled="currentPage <= 0">
        Previous
      </button>
      <button @click="nextPage" :disabled="songs.length < songsPerPage">
        Next
      </button> -->
    </div>
  </template>
  
  <script>
  import SongBlock from '@/components/SongBlock.vue';
  
  export default {
    name: 'songs-viewer',
    components: {
      SongBlock
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
        album: ""
      }
    },
    computed: {
      songs() {
        return this.$store.state.songs;
      },
      prevButtonClass() {
        return {
          'page-link' : true,
          'disabled' : this.currentPage <= 0
        }
      },
      nextButtonClass() {
        return {
          'page-link' : true,
          'disabled' : !this.songs || this.songs.length < this.songsPerPage
        }
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
      }
    },
    watch: {
      currentPage: {
        handler() {
          this.updateSongs()
        },
        immediate: true
      },
    }
  }
  </script>
  