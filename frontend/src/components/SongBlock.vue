<template>
    <div class="song-block" @click="detailedInfo">
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
        }
    }
}

</script>