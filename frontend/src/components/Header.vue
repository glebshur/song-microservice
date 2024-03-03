<template>
    <header>
        <nav class="navbar navbar-expand-md navbar-dark bg-dark">
          <div class="container-fluid">
            <a class="navbar-brand" href="/">
              <img src="https://thumbs2.imgbox.com/96/ad/OUnIls2P_t.png">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuItems" aria-controls="menuItems" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menuItems">
              <ul class="navbar-nav me-auto mb-2 mb-md-0">
                  <li class="nav-item">
                    <a class="nav-link active text-primary" href="/">{{ $t('header.home') }}</a>
                  </li>
                  <li class="nav-item" v-if="hasAdminRole()">
                    <a class="nav-link active text-primary" href="/song/upload">{{ $t('header.upload') }}</a>
                  </li>
                  <li class="nav-item" v-if="hasAdminRole()">
                    <a class="nav-link active text-primary" href="/mysongs">{{ $t('header.mySongs') }}</a>
                  </li>
              </ul>
            </div>

            <div class="nav-item dropdown pe-3">
              <a class="nav-link dropdown-toggle text-primary" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                {{ $t('header.changeLanguage') }}
              </a>
              <ul class="dropdown-menu bg-light">
                <li>
                  <a class="dropdown-item" href="#" @click="changeLocale('by')">
                    <span class="align-middle">Беларуская</span>
                    <img src="/flags/by.svg" width="40" height="30" class="mx-2">
                  </a>
                </li>
                <li class="d-flex align-items-center">
                  <a class="dropdown-item" href="#" @click="changeLocale('ru')">
                    <span class="align-middle">Русский</span>
                    <img src="/flags/ru.svg" width="40" height="30" class="mx-2">
                  </a>
                </li>
                <li>
                  <a class="dropdown-item" href="#" @click="changeLocale('en')">
                    <span class="align-middle">English</span>
                    <img src="/flags/gb.svg" width="40" height="30" class="mx-2">
                  </a>
                </li>
                <li>
                  <a class="dropdown-item" href="#" @click="changeLocale('de')">
                    <span class="align-middle">Deutsch</span>
                    <img src="/flags/de.svg" width="40" height="30" class="mx-2">
                  </a>
                </li>
              </ul>
            </div>
            <button class="btn btn-outline-danger" v-if="authenticated" @click="logout">{{ $t('header.buttons.logout') }}</button>
            <button class="btn btn-outline-success" v-else @click="login">{{ $t('header.buttons.login') }}</button>
          </div>
        </nav>
    </header>
</template>

<script>
import keycloakService from '@/security/keycloak';
import { useI18n } from 'vue-i18n';
  
  export default {
    name: 'custome-header',
    setup() {
      const { t, locale } = useI18n({useScope: 'global'});
      const changeLocale = (newLocale) => {
        locale.value = newLocale;
        localStorage.setItem('locale', locale.value)
      }
      return { t, changeLocale }
    },
    data() {
      return {
        authenticated: false,
      };
    },
    mounted() {
      this.authenticated = keycloakService.authenticated;
    },
    methods: {
      logout() {
        keycloakService.logout({redirectUri: process.env.VUE_APP_BASE_URL});
      },
      login() {
        keycloakService.login({redirectUri: process.env.VUE_APP_BASE_URL})
      },
      hasAdminRole(){
        return keycloakService.hasResourceRole('ADMIN')
      }
    },
  };
</script>