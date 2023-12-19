<template>
    <div class="song-block" @click="detailedInfo">
        <!-- <img src="https://i.scdn.co/image/ab67616d0000b273721c52311e729e994613567d"/> -->
        <img :src="getSongImageUrl()"/>
        <strong>
            {{ song.name }}
        </strong>
        <span>
            Artist / Band - {{ song.artist }}
        </span>
        <span>
            Album - {{ song.album }}
        </span>
    </div>
</template>

<script>
import ImageSelector from "@/imageSelector/imageSelector"
export default {
    name: 'song-block',
    props: {
        song: {
            type: Object,
            required: true
        }
    },
    methods: {
        detailedInfo() {
            this.$router.push({ name: 'SongDetails', params: {id: this.song.id}})
        },
        getSongImageUrl() {
            return this.song.imageLink ? this.song.imageLink : ImageSelector.getRandomImage()
        }
    }
}

</script>