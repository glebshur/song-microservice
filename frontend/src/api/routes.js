export const ALL_SONGS = () => 'song-api/songs-data/';
export const SONG_BY_ID = (id) => `song-api/songs-data/${id}`;
export const DELETE_SONG = (id) => `song-api/songs-data/${id}/delete`;
export const UPDATE_SONG = (id) => `song-api/songs-data/${id}/update`;

export const UPLOAD_FILE = () => 'file-api/files/upload';
export const DOWNLOAD_FILE = (id) => `file-api/files/${id}`;
export const DELETE_FILE = (id) => `file-api/files/${id}/delete`;