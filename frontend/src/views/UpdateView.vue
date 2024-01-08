<template>
    <Header />
    <error-block :errors="errors"/>
    <div v-if="song">
        <div>
            Name - <input type="text" v-model="song.name"/>
        </div>
        <div>
            Artist / Band - <input type="text" v-model="song.artist"/>
        </div>
        <div>
            Album - <input type="text" v-model="song.album"/>
        </div>
        <div>
          Release date - <input type="date" v-model="song.releaseDate"/>
        </div>
      <button @click="updateSong">Update</button>
    </div>
  </template>
  
  <script>
  import Header from '@/components/Header.vue';
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import http from '@/api';
  import { UPDATE_SONG } from '@/api/routes'
  
  export default {
    name: 'song-update',
    components: {
      Header,
      ErrorBlock
    },
    data() {
      return {
        song: null,
        errors: null
      }
    },
    async created() {
      try {
        const {id} = this.$route.params;
        if(id) {
          const {data} = await this.$store.dispatch('fetchSingleSong', id)
          this.song = data
        }
      } catch(err) {
        console.error(err)
      }
    },
    methods: {
        validateSongData() {
            let errors = [];
            if(this.song.name == "") {
                errors.push("Name cannot be empty");
            }
            if(this.song.album && this.song.album == "") {
                errors.push("Album cannot be empty");
            }
            if(this.song.artist == "") {
                errors.push("Artist cannot be empty");
            }
            if(!this.song.releaseDate){
              errors.push("Please enter release date");
            }
            return errors;
        },
        updateSong() {
            const errors = this.validateSongData();
            if(errors.length == 0) {
                http.put(UPDATE_SONG(this.song.id), 
                {
                    name: this.song.name, 
                    artist: this.song.artist,
                    album: this.song.album,
                    releaseDate: this.song.releaseDate
                }).then(()=>{
                    this.$router.push({path: `/song/${this.song.id}`})
                }).catch((err) => {
                    console.error(err);
                    this.errors = ["Song cannot be updated"];
                })
            } else {
                this.errors = errors
            }
        },
    }
  }
  
  </script>
  