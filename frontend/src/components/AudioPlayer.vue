<template>
    <div class="bg-dark bg-opacity-50 text-light p-3 rounded">

        <span v-if="visibleName" class="fs-4">{{ currSong.name }}</span>

        <div class="row justify-content-around">
            <button v-if="songs.length > 1" class="btn col-auto" @click="prevSong">
                <img src="/player/prev.svg" width="80" height="80" fill="#bbdafe">
            </button>
            <button class="btn col-auto" @click="toggleAudio">
                <img v-if="!isPlaying" src="/player/play.svg">
                <img v-else src="/player/pause.svg">
            </button>
            <button v-if="songs.length > 1" class="btn col-auto" @click="nextSong">
                <img src="/player/next.svg" width="80" height="80">
            </button>
        </div>

        <div v-if="audio">
            <div class="row align-items-end">
                <div class="col-9">
                    <div class="d-flex justify-content-between">
                    <div>{{ TimeFormatter.formatTime(currentTime) }}</div>
                    <div>{{ TimeFormatter.formatTime(audio.duration()) }}</div>
                    </div>
                    <input type="range" class="form-range" v-model="currentTime" :max="audio.duration()" 
                    @change="seekToTime" @mousedown="startSliderDrag" @mouseup="endSliderDrag">
                </div>
                <div class="col">
                    <div>{{ formatVolume() }}</div>
                    <input type="range" class="form-range" v-model="volume" max="1" step="0.01" @input="changeVolume">
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import http from '@/api';
import { DOWNLOAD_FILE } from '@/api/routes';
import {Howl} from 'howler';
import TimeFormatter from '@/utils/timeFormatter';

export default {    
    name: 'audio-player',
    props: {
        songs: {
            type: Array,
            required: true
        },
        visibleName: {
            type: Boolean,
            default: false
        }
    },
    setup() {
        return {TimeFormatter};
    },
    data() {
        return {
            audio: null,
            audioUrl: null,
            isPlaying: false,
            updateTimeInterval: false,
            currentTime: 0,
            isSliderDragging: false,
            volume: 0.5,
            currSong: this.songs[0]
        }
    },
    methods: {
        toggleAudio() {
            if(this.audio) {
                if(this.isPlaying) {
                this.audio.pause();
                }
                else {
                this.audio.play();
                }
                this.isPlaying = !this.isPlaying;
            }
            else {
                this.startSong()
            }
        },
        startUpdatingTime() {
            this.updateTimeInterval = setInterval(this.updateTime, 500);
        },
        stopUpdatingTime() {
            clearInterval(this.updateTimeInterval);
        },
        updateTime() {
            if(!this.isSliderDragging) {
                this.currentTime = this.audio.seek();
            }
        },
        seekToTime() {
            this.audio.seek(this.currentTime);
        },
        startSliderDrag() {
            this.isSliderDragging = true;
        },
        endSliderDrag() {
            this.isSliderDragging = false;
        },
        changeVolume() {
            this.audio.volume(this.volume);
        },
        formatVolume() {
            return (this.volume * 100).toFixed(0) + "%";
        },
        nextSong() {
            if(this.audio) {
                this.audio.stop()
            }
            const index = (this.songs.indexOf(this.currSong) + 1) % this.songs.length;
            this.currSong = this.songs[index];
            this.startSong();
        },
        prevSong() {
            if(this.audio) {
                this.audio.stop()
            }
            const index = (this.songs.indexOf(this.currSong) - 1 + this.songs.length) % this.songs.length;
            this.currSong = this.songs[index];
            this.startSong();
        },
        startSong() {
            http.get(DOWNLOAD_FILE(this.currSong.fileId), {responseType: 'blob'}
            ).then((response) => {
                let fileURL = window.URL.createObjectURL(new Blob([response.data]));
                this.audio = new Howl({
                    src: fileURL,
                    html5: true,
                    volume: this.volume,
                    onplay: () => {this.startUpdatingTime();},
                    onend: () => {
                        if(this.songs.length > 1) {
                            this.nextSong();
                        } else {
                            this.stopUpdatingTime(); 
                            this.isPlaying = false
                        }
                    },
                    onpause: () => {this.stopUpdatingTime();},
                });
                this.audio.play();
                this.isPlaying = true;
            })
        }
    }
}

</script>