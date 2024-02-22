import { createStore } from 'vuex'
import http from '@/api/index'
import { ALL_SONGS, SONG_BY_ID } from '@/api/routes'

export default createStore({
  state: {
    songs: {},
    totalSongsNumber: 0,
  },
  getters: {
  },
  mutations: {
    setSongs(state, songs) {
      state.songs = songs
    },
    setTotalSongsNumber(state, totalSongsNumber) {
      state.totalSongsNumber = totalSongsNumber
    }
  },
  actions: {
    fetchSongs({commit}, { offset, limit, name, artist, album, userId }) {
      const body = { offset, limit, name, artist, album, userId };
      http.post(ALL_SONGS(), body, {responseType: 'json'})
        .then(response => {
          const contentRange = response.headers.get('Content-Range');
          const rangeRegex = /(\d+)-(\d+)\/(\d+)/;
          const [, , , total] = rangeRegex.exec(contentRange);
          commit('setTotalSongsNumber', total);
          commit('setSongs', response.data);
          
        })
        .catch(err => console.log(err));
    },
    fetchSingleSong(_, id) {
      return http.get(SONG_BY_ID(id))
    }
  }
})
