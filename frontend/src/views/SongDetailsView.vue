<template>

  <div v-if="song">
    <h2>{{ song.name }}</h2>
    <div>
      Artist / Band - {{ song.artist }} <a v-bind:href=song.artistLink>Spotify</a>
    </div>
    <div>
      Album - {{ song.album }} <a v-bind:href=song.albumLink>Spotify</a>
    </div>
    <div>
      Duration - {{ song.duration / 60000}}
    </div>
    <div>
      Release date - {{ song.releaseDate }}
    </div>

    <button @click="goHome">Home</button>
  </div>
</template>

<script>

export default {
  name: 'song-details',
  data() {
    return {
      song: null
    }
  },
  async created() {
    try {
      const {id} = this.$route.params;
      if(id) {
        const {data} = await this.$store.dispatch('fetchSingleSong', id)
        this.song = data
        console.log(data)
      }
    } catch(err) {
      console.log(err)
    }
  },
  methods: {
    goHome() {
      this.$router.push({name: 'SongsHome'})
    }
  }
}

</script>
