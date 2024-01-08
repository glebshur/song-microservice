import { createStore } from 'vuex'
import http from '@/api/index'
import { ALL_SONGS, SONG_BY_ID } from '@/api/routes'

export default createStore({
  state: {
    songs: {},
  },
  getters: {
  },
  mutations: {
    setSongs(state, songs) {
      state.songs = songs
    }
  },
  actions: {
    fetchSongs({commit}, { offset, limit, name, artist, album, userId }) {
      const body = { offset, limit, name, artist, album, userId };
      http.post(ALL_SONGS(), body)
        .then(({data}) => {
          commit('setSongs', data);
        })
        .catch(err => console.log(err));
    },
    fetchSingleSong(_, id) {
      return http.get(SONG_BY_ID(id))
    }
  }
})
