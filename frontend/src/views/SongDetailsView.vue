<template>
  <Header />
  <div v-if="song">
    <div class="bg-image">
      <div class="p-3 d-flex justify-content-center">
        <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">

          <div class="position-relative">
            <img class="img-fluid w-100 rounded" :src="getSongImageUrl()">
            <div v-if="hasUserRole()" class="position-absolute top-50 start-50 translate-middle w-75">
              <AudioPlayer :file-id="song.fileId"/>
            </div>
          </div>

          <div class="pt-3 text-dark">
            <h2>{{ song.name }}</h2>
            <table class="table table-info table-borderless text-start">
              <thead></thead>
              <tbody>
                <tr>
                  <th scope="row">Artist / Band</th>
                  <td>{{ song.artist }}</td>
                  <td v-if="song.artistLink"><a v-bind:href=song.artistLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">Album</th>
                  <td>{{ song.album }}</td>
                  <td v-if="song.albumLink"><a v-bind:href=song.albumLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">Duration</th>
                  <td id="duration">{{ getDuration() }}</td>
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
import AudioPlayer from '@/components/AudioPlayer.vue';

export default {
  name: 'song-details',
  components: {
    Header,
    AudioPlayer
},
  data() {
    return {
      song: null,
      randomImageUrl: ImageSelector.getRandomImage(),
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
      return this.song.imageLink ? this.song.imageLink : this.randomImageUrl
    },
    formatTime(totalSeconds) {
      var minutes = Math.floor(totalSeconds / 60);
      var seconds = (totalSeconds % 60).toFixed(0);
      return (
        seconds == 60 ?
        (minutes+1) + ":00" :
        minutes + ":" + (seconds < 10 ? "0" : "") + seconds
      );
    },
    getDuration(){
      var seconds = (this.song.duration / 1000).toFixed(0);
      return this.formatTime(seconds)
    },
  }
}

</script>

<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/18/62/AJDWt2ve_o.jpg');
}
</style>
