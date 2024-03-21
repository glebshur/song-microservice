import { createStore } from 'vuex'
import http from '@/api/index'
import { ALL_PLAYLISTS, ALL_SONGS, SONG_BY_ID } from '@/api/routes'

export default createStore({
  state: {
    songs: {},
    totalSongsNumber: 0,
    playlists: {},
    totalPlaylistsNumber: 0,
  },
  getters: {
  },
  mutations: {
    setSongs(state, songs) {
      state.songs = songs
    },
    setTotalSongsNumber(state, totalSongsNumber) {
      state.totalSongsNumber = totalSongsNumber
    },
    setPlaylists(state, playlists) {
      state.playlists = playlists;
    },
    setTotalPlaylistsNumber(state, totalPlaylistsNumber) {
      state.totalPlaylistsNumber = totalPlaylistsNumber;
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
    },
    fetchPlaylists({commit}, {offset, limit, name, userId, personal}) {
      const body = { offset, limit, name, userId, personal };
      http.post(ALL_PLAYLISTS(), body, {responseType: 'json'})
        .then(response => {
          const contentRange = response.headers.get('Content-Range');
          const rangeRegex = /(\d+)-(\d+)\/(\d+)/;
          const [, , , total] = rangeRegex.exec(contentRange);
          commit('setTotalPlaylistsNumber', total);
          commit('setPlaylists', response.data);
          
        })
        .catch(err => console.log(err));
    }
  }
})
