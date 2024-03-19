<template>
  <Header />
  <div v-if="song">
    <div class="bg-image">
      <info-block :message="message"/>
      <error-block :errors="errors"/>

      <div class="p-3 d-flex justify-content-center">
        <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">

          <div class="position-relative">
            <img class="img-fluid w-100 rounded" :src="getSongImageUrl()">
            <div v-if="hasUserRole()" class="position-absolute top-50 start-50 translate-middle w-75">
              <AudioPlayer :songs="[song]"/>
            </div>
          </div>

          <div class="pt-3 text-dark">

            <div class="row align-items-center">
              <div class="col-2">
                <div v-if="hasAdminRole()" >
                  <button class="btn" @click="redirectToUpdate">
                    <img src="/icons/edit.svg" width="40" height="40">
                  </button>
                </div>
              </div>

              <div class="col">
                <h2>{{ song.name }}</h2>
              </div>

              <div class="col-2">
                <div v-if="hasUserRole()">
                  <button class="btn" @click="SongDownloader.download(song)">
                    <img src="/icons/download.svg" width="40" height="40">
                  </button>
                </div>
              </div>
            </div>

            <table class="table table-info table-borderless text-start">
              <thead></thead>
              <tbody>
                <tr>
                  <th scope="row">{{$t('songDetails.table.artist')}}</th>
                  <td>{{ song.artist }}</td>
                  <td v-if="song.artistLink"><a v-bind:href=song.artistLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">{{$t('songDetails.table.album')}}</th>
                  <td>{{ song.album }}</td>
                  <td v-if="song.albumLink"><a v-bind:href=song.albumLink>Spotify</a></td>
                </tr>
                <tr>
                  <th scope="row">{{$t('songDetails.table.duration')}}</th>
                  <td id="duration">{{ getDuration() }}</td>
                </tr>
                <tr>
                  <th scope="row">{{$t('songDetails.table.releaseDate')}}</th>
                  <td>{{ song.releaseDate }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div v-if="hasUserRole()" class="dropdown my-2">
            <button class="btn btn-outline-light dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
              {{$t('songDetails.dropdown.title')}}
            </button>
            <ul class="dropdown-menu bg-light">
              <li>
                <button class="dropdown-item align-items-center" @click="createNewPlaylist()">
                  <img src="/icons/add.svg" width="30" height="30">
                  {{$t('songDetails.dropdown.create')}}
                </button>
              </li>
              <li v-for="playlist in userPlaylists" :key="playlist.id">
                <button class="dropdown-item" @click="addToPlaylist(playlist)">
                  {{ playlist.name }} ({{$t('songDetails.dropdown.songs')}} {{ playlist.songs.length }})
                </button>
              </li>
            </ul>
          </div>

          <div class="btn-group justify-content-center">
            <!-- <button id="downloadButton" class="btn btn-outline-light" v-if="hasUserRole()" @click="SongDownloader.download(song)">{{$t('songDetails.buttons.download')}}</button> -->
            <!-- <button id="updateButton" class="btn btn-outline-light" v-if="hasAdminRole()" @click="redirectToUpdate">{{$t('songDetails.buttons.update')}}</button> -->
            <button id="deleteButton" class="btn btn-outline-danger" v-if="hasAdminRole()" @click="deleteSong">{{$t('songDetails.buttons.delete')}}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import http from '@/api';
import { DELETE_SONG, ALL_PLAYLISTS, UPDATE_PLAYLIST, CREATE_PLAYLIST } from '@/api/routes';
import keycloakService from '@/security/keycloak';
import ImageSelector from "@/imageSelector/imageSelector";
import AudioPlayer from '@/components/AudioPlayer.vue';
import { useI18n } from 'vue-i18n';
import TimeFormatter from '@/utils/timeFormatter';
import SongDownloader from '@/utils/songDownloader';
import InfoBlock from '@/components/InfoBlock.vue';
import ErrorBlock from '@/components/ErrorBlock.vue';

export default {
  name: 'song-details',
  setup() {
    const { t } = useI18n({useScope: 'global'});
    return { t, SongDownloader }
  },
  components: {
    Header,
    AudioPlayer,
    InfoBlock,
    ErrorBlock
  },
  data() {
    return {
      song: null,
      randomImageUrl: ImageSelector.getRandomImage(),
      userPlaylists: null,
      message: null,
      errors: null,
      defaultPlaylistName: 'My New Playlist',
    }
  },
  async created() {
    try {
      const {id} = this.$route.params;
      if(id) {
        const {data} = await this.$store.dispatch('fetchSingleSong', id)
        this.song = data
      }

      if(this.hasUserRole()) {
        this.getUserPlaylists();
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
    deleteSong() {
      http.delete(DELETE_SONG(this.song.id))
      .then(() => {
        this.$router.push({name: 'SongsHome', query: {message: this.t('songDetails.messages.deletion')}});
      })
    },
    redirectToUpdate() {
      this.$router.push({path: `/song/${this.song.id}/update`});
    },
    getSongImageUrl() {
      return this.song.imageLink ? this.song.imageLink : this.randomImageUrl
    },
    getDuration(){
      var seconds = (this.song.duration / 1000).toFixed(0);
      return TimeFormatter.formatTime(seconds)
    },
    getUserPlaylists() {
      const body = {
        offset: 0,
        limit: 10,
        name: '',
        userId: keycloakService.tokenParsed.sub,
      }
      http.post(ALL_PLAYLISTS(), body, {responseType: 'json'})
      .then((response) => {
        this.userPlaylists = response.data;
        console.log(this.userPlaylists);
      })
      .catch((error) => {
        console.error('Error on getting playlists: {}',error);
      })
    }, 
    addToPlaylist(playlist) {
      this.errors = null;
      this.message = null;
      if(playlist) {
        if(!this.isSongInPlaylist(playlist, this.song)) {
          playlist.songs.push(this.song)
          http.put(UPDATE_PLAYLIST(playlist.id), playlist)
            .then(() => {
              this.message = this.t('songDetails.messages.addition') + playlist.name;
              this.getUserPlaylists();
            })
            .catch(() => {
              this.errors = [ this.t('songDetails.messages.addToPlaylist') ]
            })
        } else {
          this.errors = [ this.t('songDetails.messages.alreadyAdded') ]
        }
      }
    },
    createNewPlaylist() {
      this.errors = null;
      this.message = null;
      const body = {
        name: this.defaultPlaylistName,
        songs: [this.song]
      }
      http.post(CREATE_PLAYLIST(), body)
      .then((response) => {
        this.message = this.t('songDetails.messages.additionInCreated') + response.data.name;
        this.getUserPlaylists();
      })
      .catch(() => {
        this.errors = [ this.t('songDetails.messages.addToCreatedPlaylist') ]
      })
    },
    isSongInPlaylist(playlist, songToFind) {
      return playlist.songs.some((song) => {
        return song.id === songToFind.id;
      })
    }
  }
}

</script>

<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/18/62/AJDWt2ve_o.jpg');
}
</style>