<template>
  <Header />
  <div v-if="playlist">
    <div class="bg-image">
      <error-block :errors="errors"/>
      <div class="p-3 d-flex justify-content-center">
        <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">
          
          <div class="position-relative">
            <img class="img-fluid w-100 rounded" :src="randomImageUrl">
            <div v-if="hasUserRole()" class="position-absolute top-50 start-50 translate-middle w-75">
              <AudioPlayer :songs="playlist.songs" :visible-name="true"/>
            </div>
            <img v-if="playlist.personal" src="/icons/private.svg" class="right-top-image m-2" width="50" height="50">
          </div>

          <div class="row my-2 align-items-center">
            <div class="col-2">
              <div v-if="(hasUserRole() && isOwner()) || hasAdminRole()" >
                <button v-if="!edited" class="btn" @click="startEdit()">
                  <img src="/icons/edit.svg" width="40" height="40">
                </button>
                <button v-else class="btn" @click="endEdit(true)">
                  <img src="/icons/confirm.svg" width="40" height="40">
                </button>
              </div>
            </div>

            <div class="col">
              <h2 v-if="!edited" >{{ playlist.name }}</h2>
              <input v-else class="form-control bg-light fs-3" type="text" v-model="playlist.name"/>
            </div>

            <div class="col-2">
              <div v-if="hasUserRole()" >
                <button v-if="!edited" class="btn" @click="downloadAll()">
                  <img src="/icons/download.svg" width="40" height="40">
                </button>
                <button v-else class="btn" @click="endEdit(false)">
                  <img src="/icons/cancel.svg" width="40" height="40">
                </button>
              </div>
            </div>
          </div>
          

          <div class="list-group">
            <div v-for="song in playlist.songs" :key="song.id" class="list-group-item list-group-item-action bg-light">
              <a v-if="!edited" class="stretched-link" :href="getSongUrl(song)"></a>
              
              <div class="row my-1">
                <div class="col text-start">
                  <strong> {{ song.name }} </strong>
                  <div> {{ song.artist }} </div>
                </div>
                
                <div class="row col-auto align-items-center">
                  <div class="col text-end"> {{ getDuration(song) }}</div>
                  <button v-if="edited" class="col btn" @click="removeSong(song)">
                    <img src="/icons/delete.svg" width="30" height="30">
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div v-if="edited" class="d-flex align-items-center mt-1">
            <input class="form-check-input col-auto" type="checkbox" v-model="playlist.personal">
            <label class="form-check-label col-auto mx-2 fs-5">
              {{$t('playlistDetails.checkbox')}}
            </label>
          </div>

          <button v-if="(hasUserRole() && isOwner()) || hasAdminRole()" id="deleteButton" class="btn btn-outline-danger mt-2" 
          @click="deletePlaylist">{{$t('playlistDetails.buttons.delete')}}</button>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import http from '@/api';
import { DELETE_PLAYLIST, PLAYLIST_BY_ID, UPDATE_PLAYLIST } from '@/api/routes';
import keycloakService from '@/security/keycloak';
import ImageSelector from "@/imageSelector/imageSelector";
import AudioPlayer from '@/components/AudioPlayer.vue';
import { useI18n } from 'vue-i18n';
import TimeFormatter from '@/utils/timeFormatter';
import SongDownloader from '@/utils/songDownloader';
import ErrorBlock from '@/components/ErrorBlock.vue';

export default {
  name: 'playlist-details',
  setup() {
    const { t } = useI18n({useScope: 'global'});
    return { t }
  },
  components: {
    Header,
    ErrorBlock,
    AudioPlayer
  },
  data() {
    return {
      playlist: null,
      randomImageUrl: ImageSelector.getRandomImage(),
      edited: false,
      tempPlaylist: null,
      errors: null,
    }
  },
  async created() {
    const {id} = this.$route.params;
    if(id) {
        http.get(PLAYLIST_BY_ID(id))
        .then((response) => {
          this.playlist = response.data;
        })
        .catch(() => {
          console.error('Cannot get playlist')
        })
    }
  },
  methods: {
    hasUserRole() {
      return keycloakService.hasResourceRole('USER')
    },
    hasAdminRole() {
      return keycloakService.hasResourceRole('ADMIN')
    },
    isOwner() {
      return keycloakService.tokenParsed.sub === this.playlist.userId;
    },
    getSongUrl(song) {
      return '/song/' + song.id;
    },
    getDuration(song) {
      var seconds = (song.duration / 1000).toFixed(0);
      return TimeFormatter.formatTime(seconds)
    },
    startEdit() {
      this.edited = true;
      this.tempPlaylist = JSON.parse(JSON.stringify(this.playlist));
    },
    validateSongData() {
      let errors = [];
      if(this.playlist.name == "") {
        errors.push(this.t('playlistDetails.errors.nameEmpty'));
      }
      return errors;
    },
    endEdit(confirm) {
      if(confirm) {
        const errors = this.validateSongData();
        if(errors.length == 0) {
          this.edited = false;
          http.put(UPDATE_PLAYLIST(this.playlist.id), this.playlist)
          .then((response) => {
            this.playlist = response.data;
          })
          .catch(() => {
            this.errors = [ this.t('playlistDetails.errors.updateRequest') ]
          })
        } else {
          this.errors = errors;
        }
      }
      else {
        this.playlist = this.tempPlaylist
        this.edited = false;
      }
    },
    removeSong(song) {
      const index = this.playlist.songs.indexOf(song);
      if (index !== -1) {
        this.playlist.songs.splice(index, 1);
      }
    },
    downloadAll() {
      for(const song of this.playlist.songs) {
        SongDownloader.download(song);
      }
    },
    deletePlaylist() {
      http.delete(DELETE_PLAYLIST(this.playlist.id))
      .then(() => {
        this.$router.push({name: 'SongsHome', query: {message: this.t('playlistDetails.messages.deletion')}});
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