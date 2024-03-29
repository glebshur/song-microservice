<template>
    <Header />
    <div v-if="song">
      <div class="bg-image">
        <error-block :errors="errors"/>
        <div class="p-3 d-flex justify-content-center">
          <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">
            <img class="w-100 rounded" :src="song.imageLink"/>
            <div class="pt-3 text-dark">
              <table class="table table-info table-borderless text-start">
                <thead></thead>
                <tbody>
                  <tr>
                    <th scope="row">{{$t('updateView.table.name')}}</th>
                    <td>
                      <input class="form-control bg-light" type="text" v-model="song.name"/>  
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">{{$t('updateView.table.artist')}}</th>
                    <td>
                      <input class="form-control bg-light" type="text" v-model="song.artist"/>  
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">{{$t('updateView.table.album')}}</th>
                    <td>
                      <input class="form-control bg-light" type="text" v-model="song.album"/>  
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">{{$t('updateView.table.releaseDate')}}</th>
                    <td>
                      <input class="form-control bg-light" type="date" v-model="song.releaseDate"/>
                    </td>
                  </tr>
                </tbody>
              </table>
              <button class="btn btn-outline-light" @click="updateSong">Update</button>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>
  
<script>
  import Header from '@/components/Header.vue';
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import http from '@/api';
  import { UPDATE_SONG } from '@/api/routes';
  import ImageSelector from "@/imageSelector/imageSelector";
  import { useI18n } from 'vue-i18n';
  
  export default {
    name: 'song-update',
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t }
    },
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
          this.song.imageLink = this.getSongImageUrl(); // prevents img from auto changing if url is random
        }
      } catch(err) {
        console.error(err)
      }
    },
    methods: {
      validateSongData() {
        let errors = [];
        if(this.song.name == "") {
          errors.push(this.t('updateView.errors.nameEmpty'));
        }
        if(this.song.artist == "") {
          errors.push(this.t('updateView.errors.artistEmpty'));
        }
        if(!this.song.releaseDate){
          errors.push(this.t('updateView.errors.releaseDateNull'));
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
              this.errors = [this.t('updateView.errors.updateRequest')];
            })
        } else {
          this.errors = errors
        }
      },
      getSongImageUrl() {
        return this.song.imageLink ? this.song.imageLink : ImageSelector.getRandomImage()
      },
    }
  }
</script>
<style scoped>
.bg-image {
  background-image: url('https://images2.imgbox.com/18/62/AJDWt2ve_o.jpg');
}

</style>
  