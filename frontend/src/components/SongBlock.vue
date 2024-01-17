<template>
    <div class="card bg-light border-dark text-start m-3" style="width: 250px; cursor: pointer;" @click="detailedInfo">
        <img class="card-img-top" :src="getSongImageUrl()"/>
        <div class="card-body">
            <strong class="card-title">
                {{ song.name }}
            </strong>
            <div class="card-text mt-2">
                <p>
                    {{ song.artist }}
                </p>
            </div>
        </div>
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