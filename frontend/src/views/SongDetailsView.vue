<template>
  <Header />
  <div v-if="song">
    <div class="bg-image">
      <div class="p-3 d-flex justify-content-center">
        <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">
          <img class="w-100 rounded" :src="getSongImageUrl()"/>
          <div class="pt-3 text-dark">
            <h2>{{ song.name }}</h2>
            <table class="table table-info table-borderless text-start">
              <thead></thead>
              <tbody>
                <tr>
                  <th scope="row">Artist / Band</th>
                  <td>{{ song.artist }}</td>
                  <td><a v-bind:href=song.albumLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">Album</th>
                  <td>{{ song.album }}</td>
                  <td><a v-bind:href=song.albumLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">Duration</th>
                  <td id="duration">{{ getDurationInMinutesAndSeconds() }}</td>
                </tr>
                <tr>
                  <th scope="row">Release date</th>
                  <td>{{ song.releaseDate }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="btn-group justify-content-center">
            <button id="downloadButton" class="btn btn-outline-light" v-if="hasUserRole()" @click="download">Download</button>
            <button id="updateButton" class="btn btn-outline-light" v-if="hasAdminRole()" @click="redirectToUpdate">Update</button>
            <button id="deleteButton" class="btn btn-outline-danger" v-if="hasAdminRole()" @click="deleteSong">Delete</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import http from '@/api';
import { DELETE_SONG, DOWNLOAD_FILE } from '@/api/routes';
import keycloakService from '@/security/keycloak';
import ImageSelector from "@/imageSelector/imageSelector";

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
      console.error('Cannot download song')
    }
  },
  methods: {
    hasUserRole() {
      return keycloakService.hasResourceRole('USER')
    },
    hasAdminRole() {
      return keycloakService.hasResourceRole('ADMIN')
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
    },
    deleteSong() {
      http.delete(DELETE_SONG(this.song.id))
      .then(() => {
        this.$router.push({name: 'SongsHome', query: {message: 'Song was deleted!'}});
      })
    },
    redirectToUpdate() {
      this.$router.push({path: `/song/${this.song.id}/update`});
    },
    getSongImageUrl() {
      return this.song.imageLink ? this.song.imageLink : ImageSelector.getRandomImage()
    },
    getDurationInMinutesAndSeconds(){
      var minutes = Math.floor(this.song.duration / 60000);
      var seconds = ((this.song.duration % 60000) / 1000).toFixed(0);
      return (
        seconds == 60 ?
        (minutes+1) + ":00" :
        minutes + ":" + (seconds < 10 ? "0" : "") + seconds
      );
    }
  }
}

</script>

<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/18/62/AJDWt2ve_o.jpg');
}

</style>
