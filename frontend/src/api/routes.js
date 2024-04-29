export const ALL_SONGS = () => 'song-api/songs-data/';
export const SONG_BY_ID = (id) => `song-api/songs-data/${id}`;
export const DELETE_SONG = (id) => `song-api/songs-data/${id}/delete`;
export const UPDATE_SONG = (id) => `song-api/songs-data/${id}/update`;
export const ALL_PLAYLISTS = () => 'song-api/playlists/';
export const PLAYLIST_BY_ID = (id) => `song-api/playlists/${id}`;
export const CREATE_PLAYLIST = () => `song-api/playlists/create`;
export const UPDATE_PLAYLIST = (id) => `song-api/playlists/${id}/update`;
export const DELETE_PLAYLIST = (id) => `song-api/playlists/${id}/delete`;

export const UPLOAD_FILE = () => 'file-api/files/upload';
export const DOWNLOAD_FILE = (id) => `file-api/files/${id}`;
export const DELETE_FILE = (id) => `file-api/files/${id}/delete`;
export const CUT_FILE = ()=> 'file-api/files/cut'