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
            <div class="position-absolute top-50 start-50 translate-middle w-100 px-2">
              <div class="bg-dark bg-opacity-50 text-light p-3 rounded">
                <div class="text-dark" id="waveform"></div>

                <div class="row justify-content-around pt-2">
                  <div class="col align-self-end">
                    <div class="text-start pb-2">
                      <input type="checkbox" v-model="loop" />
                      {{ t('audioEditor.loop')}}
                    </div>
                    <div class="align-self-end">
                    <div class="text-center">
                      {{ t('audioEditor.zoom')}}
                    </div>
                    <input id="zoomSlider" type="range" class="form-range" min="1" max="50" value="1">
                    </div>
                  </div>
                  <button class="btn col-auto" @click="toggleAudio">
                      <img v-if="!isPlaying" src="/player/play.svg">
                      <img v-else src="/player/pause.svg">
                  </button>

                  <div class="col align-self-end">
                    <div>{{ formatVolume() }}</div>
                    <input type="range" class="form-range" v-model="volume" max="1" step="0.01" @input="changeVolume">
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="pt-3 row align-items-center">
            <div class="col-2"></div>
            <h2 class="col text-dark">{{ song.name }}</h2>
            <div class="col-2">
              <button class="btn" @click="downloadCutedAudio()">
                <img src="/icons/download.svg" width="40" height="40">
              </button>
            </div>
          </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Header from '@/components/Header.vue';
  import keycloakService from '@/security/keycloak';
  import ImageSelector from "@/imageSelector/imageSelector";
  import { useI18n } from 'vue-i18n';
  import TimeFormatter from '@/utils/timeFormatter';
  import SongDownloader from '@/utils/songDownloader';
  import InfoBlock from '@/components/InfoBlock.vue';
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import http from '@/api';
  import { DOWNLOAD_FILE, CUT_FILE } from '@/api/routes';
  import WaveSurfer from 'wavesurfer.js';
  import RegionsPlugin from 'wavesurfer.js/dist/plugins/regions.esm.js';
  import TimelinePlugin from 'wavesurfer.js/dist/plugins/timeline.esm.js'
  
  export default {
    name: 'song-details',
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t, SongDownloader }
    },
    components: {
      Header,
      InfoBlock,
      ErrorBlock
    },
    data() {
      return {
        song: null,
        randomImageUrl: ImageSelector.getRandomImage(),
        wavesurfer: null,
        audio: null,
        activeRegion: null,
        loop: true,
        isPlaying: false,
        onRegion: false,
        volume: 0.5,
        message: null,
        errors: null,
      }
    },
    async created() {
      try {
        const {id} = this.$route.params;
        if(id) {
          const {data} = await this.$store.dispatch('fetchSingleSong', id)
          this.song = data;
          const response = await http.get(DOWNLOAD_FILE(this.song.fileId), {responseType: 'blob'});
          this.audio = new Blob([response.data]);
          this.createWavesurfer();
          this.message = this.t('audioEditor.messages.info')
        }

      } catch(err) {
        console.log(err)
        this.$router.push({ name: 'Error', params: {errorCode: 404} });
      }
    },
    methods: {
      parseFilename(response) {
        let headerLine = response.headers['content-disposition'];
        let startIndex = headerLine.indexOf('"') + 1
        let endIndex = headerLine.lastIndexOf('"');
        return headerLine.substring(startIndex, endIndex);
      },
      hasUserRole() {
        return keycloakService.hasResourceRole('USER')
      },
      hasAdminRole() {
        return keycloakService.hasResourceRole('ADMIN')
      },
      getDuration(){
        var seconds = (this.song.duration / 1000).toFixed(0);
        return TimeFormatter.formatTime(seconds)
      },
      getSongImageUrl() {
        return this.song.imageLink ? this.song.imageLink : this.randomImageUrl
      },
      createWavesurfer(){
        const bottomTimline = TimelinePlugin.create({
          height: 25,
          timeInterval: 1,
          primaryLabelInterval: 5,
          style: {
            fontSize: '12px',
            color: '#89BEFD',
          },
        })
        this.wavesurfer = WaveSurfer.create({
          container: '#waveform',
          waveColor: '#89BEFD',
          progressColor: '#0a97c7',
          normalize: true,
          barWidth: 2,
          barGap: 1,
          barRadius: 2,
          url: window.URL.createObjectURL(this.audio),
          plugins: [bottomTimline]
        });

        const wsRegions = this.wavesurfer.registerPlugin(RegionsPlugin.create());
        this.wavesurfer.on('decode', () => {
          const duration = this.wavesurfer.getDuration();
          this.activeRegion = wsRegions.addRegion({
            start: 0 + duration / 4,
            end: 0.75 * duration,
            color: '#bbdafe97',
            content: this.t('audioEditor.region'),
          })

          const slider = document.querySelector('#zoomSlider')

          slider.addEventListener('input', (e) => {
            const minPxPerSec = e.target.valueAsNumber
            this.wavesurfer.zoom(minPxPerSec)
          })
          this.wavesurfer.setVolume(this.volume);
        })

        wsRegions.on('region-clicked', (region, e) => {
          if(!this.onRegion) {
            this.activeRegion = region
            this.isPlaying = true
            region.play()
            this.onRegion = true
            e.stopPropagation();
          }
        })

        wsRegions.on('region-in', () => {
          this.onRegion = true
        })
        wsRegions.on('region-out', () => {
          this.onRegion = false
          if(this.loop) {
            this.activeRegion.play()
            this.onRegion = true
          }
        })
      },
      downloadCutedAudio(){
        const startTime = this.activeRegion.start;
        const endTime = this.activeRegion.end;

        http.post(CUT_FILE(),{
          songId: this.song.fileId,
          startTime,
          endTime
        }, {
          responseType: 'blob',
        }).then((response) => {
          let fileURL = window.URL.createObjectURL(new Blob([response.data]));
          const downloadLink = document.createElement('a');
          downloadLink.href = fileURL;
          downloadLink.download = this.parseFilename(response);

          document.body.appendChild(downloadLink);
          downloadLink.click();

          document.body.removeChild(downloadLink);
        });
      },

      toggleAudio(){
        this.wavesurfer.playPause();
        this.isPlaying = !this.isPlaying
      },
      changeVolume() {
        this.wavesurfer.setVolume(this.volume);
      },
      formatVolume() {
        return (this.volume * 100).toFixed(0) + "%";
      },
    }
  }
  
  </script>
  
  <style scoped>
  .bg-image {
    background-image: url('https://images2.imgbox.com/18/62/AJDWt2ve_o.jpg');
  }
  </style>