<template>
  <Header />
  <div class="bg-image">
    <info-block :message="info"/>
    <error-block :errors="errors"/>

    <div v-if="!downloaded" class="p-3 d-flex justify-content-center">
      <div class="p-2 bg-info col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">

        <div id="fileDropZone" class="drop-zone mx-auto text-light fs-5" @dragover.prevent="setActive" @dragenter.prevent="setActive"
        @dragleave.prevent="setInactive" @drop="handleDrop" @click.prevent="handleClick">
          <div v-if="!active">
            <div>{{$t('uploadView.dropZone.unactive')}}</div>
            <div><span v-for="extension in allowedExtensions" :key="extension"> .{{ extension }}</span></div>
            <div v-if="file">{{$t('uploadView.dropZone.choosenFile')}} {{ file.name }}</div>
          </div>
          <p class="fs-4" v-else>{{$t('uploadView.dropZone.active')}}</p>
        </div>
        <input id="fileChooser" class="d-none" type="file" @change="handleInputChange"/>

        <div class="pt-2">
          <button id="uploadButton" class="btn btn-outline-light" @click="uploadFile">{{$t('uploadView.buttons.upload')}}</button>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
  import ErrorBlock from '@/components/ErrorBlock.vue';
  import InfoBlock from '@/components/InfoBlock.vue';
  import Header from '@/components/Header.vue';
  import http from '@/api';
  import { UPLOAD_FILE } from '@/api/routes';
  import { useI18n } from 'vue-i18n';
  
  export default {
    components: {
      ErrorBlock,
      Header,
      InfoBlock,
    },
    setup() {
      const { t } = useI18n({useScope: 'global'});
      return { t }
    },
    data() {
      return {
        active: false,
        activeTimeout: null,
        file: null,
        allowedExtensions: ['mp3', 'ogg', 'wav'],
        maxSize: 15, // size in MBs 
        errors: null,
        info: null,
        downloaded: false,
      };
    },
    methods: {
    setActive() {
      this.active = true;
      document.getElementById('fileDropZone').classList.add('dragover');
      clearTimeout(this.activeTimeout);
    },
    setInactive() {
      this.activeTimeout = setTimeout(() => {
        this.active = this.false;
        document.getElementById('fileDropZone').classList.remove('dragover');
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
        errors.push(this.t('uploadView.errors.invalidExtension'));
      }

      if(file.size > this.maxSize * 1048576){
        errors.push(this.t('uploadView.errors.tooLarge') + " " + this.maxSize)
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
          this.info = this.t('uploadView.info.fileUploaded');
          this.file = null;
          this.downloaded = true;
        })
        .catch(() => {
          this.errors = [this.t('uploadView.errors.uploadingError')];
          this.file = null;
        })
      } else {
        this.errors = [this.t('uploadView.errors.isntChoosen')]
      }
    
    },
    handleClick() {
      document.getElementById('fileChooser').click()
    }
  }
};
</script>