<template>
    <div>
      <div>
        <input type="text" v-model="name" placeholder="Song name..." @keyup.enter="updateSongs"/>
        <input type="text" v-model="artist" placeholder="Artist / Band..." @keyup.enter="updateSongs"/>
        <input type="text" v-model="album" placeholder="Album..." @keyup.enter="updateSongs"/>
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
  
      <button @click="prevPage" :disabled="currentPage <= 0">
        Previous
      </button>
      <button @click="nextPage" :disabled="songs.length < songsPerPage">
        Next
      </button>
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
  