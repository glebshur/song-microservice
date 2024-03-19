import http from '@/api';
import { DOWNLOAD_FILE } from '@/api/routes';

const SongDownloader = {
    parseFilename(response) {
        let headerLine = response.headers['content-disposition'];
        let startIndex = headerLine.indexOf('"') + 1
        let endIndex = headerLine.lastIndexOf('"');
        return headerLine.substring(startIndex, endIndex);
    },
    download(song) {
        http.get(DOWNLOAD_FILE(song.fileId), {
          responseType: 'blob',
        }).then((response) => {
          let fileURL = window.URL.createObjectURL(new Blob([response.data]));
          let fileLink = document.createElement('a');
          
          fileLink.href = fileURL;
          fileLink.setAttribute('download', this.parseFilename(response));
          document.body.appendChild(fileLink);
     
          fileLink.click();
  
          document.body.removeChild(fileLink);
        })
    }
};

export default SongDownloader;