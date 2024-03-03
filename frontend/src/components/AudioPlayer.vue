<template>
    <div class="bg-dark bg-opacity-50 text-light p-3 rounded">
        <button class="btn" @click="toggleAudio">
            <svg v-if="!isPlaying" fill="#bbdafe" height="100px" width="100px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" 
            viewBox="0 0 512 512" xml:space="preserve">
                <path d="M256,0C114.618,0,0,114.618,0,256s114.618,256,256,256s256-114.618,256-256S397.382,0,256,0z M256,469.333 
                c-117.818,0-213.333-95.515-213.333-213.333S138.182,42.667,256,42.667S469.333,138.182,469.333,256S373.818,469.333,256,469.333 z"/> 
                <path d="M375.467,238.933l-170.667-128c-14.064-10.548-34.133-0.513-34.133,17.067v256c0,17.58,20.07,27.615,34.133,17.067 
                l170.667-128C386.844,264.533,386.844,247.467,375.467,238.933z M213.333,341.333V170.667L327.111,256L213.333,341.333z"/>
            </svg>
            <svg v-else stroke="#bbdafe" fill="none" xmlns="http://www.w3.org/2000/svg" height="100px" width="100px" viewBox="0 0 24 24" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                <g id="SVGRepo_iconCarrier" >
                    <circle cx="12" cy="12" r="11"></circle> 
                    <line x1="10" y1="16" x2="10" y2="8"></line> 
                    <line x1="14" y1="16" x2="14" y2="8"></line> 
                </g>
            </svg>
        </button>

        <div v-if="audio">
            <div class="row align-items-end">
                <div class="col-9">
                    <div class="d-flex justify-content-between">
                    <div>{{ formatTime(currentTime) }}</div>
                    <div>{{ formatTime(audio.duration()) }}</div>
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

export default {    
    name: 'audio-player',
    props: {
        fileId: {
            type: Number,
            required: true
        }
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
        }
    },
    methods: {
        formatTime(totalSeconds) {
            var minutes = Math.floor(totalSeconds / 60);
            var seconds = (totalSeconds % 60).toFixed(0);
            return (
                seconds == 60 ?
                (minutes+1) + ":00" :
                minutes + ":" + (seconds < 10 ? "0" : "") + seconds
            );
        },
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
                http.get(DOWNLOAD_FILE(this.fileId), {
                responseType: 'blob',
                }).then((response) => {
                    let fileURL = window.URL.createObjectURL(new Blob([response.data]));
                    this.audio = new Howl({
                        src: fileURL,
                        html5: true,
                        volume: this.volume,
                        onplay: () => {this.startUpdatingTime();},
                        onend: () => {
                            this.stopUpdatingTime(); 
                            this.isPlaying = false
                        },
                        onpause: () => {this.stopUpdatingTime();},
                    });
                    this.audio.play();
                    this.isPlaying = true;
                })
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
        }
    }
}

</script>