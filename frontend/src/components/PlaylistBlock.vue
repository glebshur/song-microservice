<template>
    <div class="card bg-light border-dark text-start m-3" style="width: 250px; cursor: pointer;" @click="detailedInfo">
        <img class="card-img-top" :src="randomImageUrl"/>
        <div class="card-body">
            <strong class="card-title">
                {{ playlist.name }}
            </strong>
            <div class="card-text mt-2">
                <p>
                    {{ $t('playlistBlock.songs') }} {{ playlist.songs.length }}
                </p>
            </div>
        </div>
    </div>
</template>

<script>
import ImageSelector from "@/imageSelector/imageSelector";
import { useI18n } from 'vue-i18n';
export default {
    name: 'playlist-block',
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t }
    },
    props: {
        playlist: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            randomImageUrl: ImageSelector.getRandomImage(),
        }
    },
    methods: {
        detailedInfo() {
            this.$router.push({ name: 'PlaylistDetails', params: {id: this.playlist.id}})
        }
    }
}

</script>