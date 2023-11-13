<template>
  <Header />
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

    <button v-if="hasUserRole()" @click="download">Download</button>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import http from '@/api';
import { DOWNLOAD_FILE } from '@/api/routes'
import keycloakService from '@/security/keycloak';

export default {
  name: 'song-details',
  components: {
    Header
  },
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
      }
    } catch(err) {
      console.error(err)
    }
  },
  methods: {
    hasUserRole() {
      return keycloakService.hasResourceRole('USER')
    },
    parseFilename(response) {
      let headerLine = response.headers['content-disposition'];
      let startIndex = headerLine.indexOf('"') + 1
      let endIndex = headerLine.lastIndexOf('"');
      return headerLine.substring(startIndex, endIndex);
    },
    download() {
      http.get(DOWNLOAD_FILE(this.song.fileId), {
        responseType: 'blob',
      }).then((response) => {
        let fileURL = window.URL.createObjectURL(new Blob([response.data]));
        let fileLink = document.createElement('a');
        
        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.parseFilename(response));
        document.body.appendChild(fileLink);
   
        fileLink.click();

        document.body.removeChild(fileLink);
      })
    }
  }
}

</script>
