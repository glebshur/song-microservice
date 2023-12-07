<template>
  <Header />
  <info-block :message="info"/>
  <error-block :errors="errors"/>
  <div class="drop-zone" @dragover.prevent="setActive" @dragenter.prevent="setActive"
  @dragleave.prevent="setInactive" @drop="handleDrop">
    <div v-if="!active">
      <p>Drag audio file here</p>
      <p><span v-for="extension in allowedExtensions" :key="extension"> .{{ extension }}</span></p>
    </div>
    <p v-else>Drop it!</p>
  </div>
  <input type="file" @change="handleInputChange"/>
  <div v-if="file">Choosen file: {{ file.name }}</div>
  <button @click="uploadFile">Upload</button>
</template>
  
<script>
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import InfoBlock from '@/components/InfoBlock.vue';
  import Header from '@/components/Header.vue';
  import http from '@/api';
  import { UPLOAD_FILE } from '@/api/routes'
  
  export default {
    components: {
      ErrorBlock,
      Header,
      InfoBlock,
    },
    data() {
      return {
        active: false,
        activeTimeout: null,
        file: null,
        allowedExtensions: ['mp3', 'ogg', 'wav'],
        maxSize: 15, // size in MBs 
        errors: null,
        info: null
      };
    },
    methods: {
    setActive() {
      this.active = true;
      clearTimeout(this.activeTimeout);
    },
    setInactive() {
      this.activeTimeout = setTimeout(() => {
        this.active = this.false;
      }, 50)
    },
    handleDrop(event) {
      event.preventDefault();
      this.setInactive();
      this.setFile(event.dataTransfer.files[0]);
    },
    handleInputChange(event){
      event.preventDefault()
      this.setFile(event.target.files[0]);
    },
    setFile(file) {
      this.errors = null
      this.info = null

      const errors = this.getFileErrors(file) 
      if(errors.length == 0){
        this.file = file;
      } else {
        this.errors = errors
      }
    },
    getFileErrors(file) {
      let errors = [];

      const extension = file.name.split('.').pop();
      if(!this.allowedExtensions.includes(extension)){
        errors.push("File extension is invalid");
      }

      if(file.size > this.maxSize * 1048576){
        errors.push("File cannot be larger than " + this.maxSize + " MBs")
      }
      
      return errors;
    },
    uploadFile() {
      this.info = null;
      this.errors = null;
      
      if(this.file)
      {
        const formData = new FormData();
        formData.append('song', this.file)

        http.post(UPLOAD_FILE(), formData, {
          headers:{
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(() => {
          this.info = "File was uploaded, wait for it to show up on the list";
          this.file = null;
        })
        .catch(() => {
          this.errors = ["File cannot be uploaded, it may have already been uploaded earlier"];
          this.file = null;
        })
      } else {
        this.errors = [ "Choose file for uploading"]
      }
    
    },
  }
};
</script>

<style>
.drop-zone {
  width: 300px;
  height: 200px;
  border: 2px dashed gray;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>