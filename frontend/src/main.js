import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/main.scss'
import keycloakService from './security/keycloak'

keycloakService.init({
    onLoad: 'check-sso',
  })
  .then(() => createApp(App).use(store).use(router).mount('#app'))
  .catch((err) => console.log(err));
