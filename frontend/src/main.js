import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/main.scss'
import keycloakService from './security/keycloak'
import "bootstrap/dist/js/bootstrap.js"
import { languages, defaultLocale } from './i18n'
import { createI18n } from 'vue-i18n'

const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('locale') || defaultLocale,
  fallbackLocale: 'en',
  messages: Object.assign(languages)
})

keycloakService.init({
    onLoad: 'check-sso',
  })
  .then(() => createApp(App).use(store).use(router).use(i18n).mount('#app'))
  .catch((err) => console.log(err));
