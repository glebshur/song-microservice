import { createRouter, createWebHistory } from 'vue-router'
import SongsHomeView from '../views/SongsHomeView.vue'
import SongDetailsView from '../views/SongDetailsView.vue'
import SecuredPage from '../views/SecuredPage.vue'
import keycloakService from '@/security/keycloak'

const routes = [
  {
    path: '/',
    name: 'SongsHome',
    component: SongsHomeView
  },
  {
    path: '/song/:id',
    name: 'SongDetails',
    component: SongDetailsView
  },
  {
    path: '/secured',
    name: 'SecuredPage',
    component: SecuredPage,
    meta: {
      requiresAuth: true,
    }
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    if (keycloakService.authenticated) {
      next();
    } else {
      keycloakService.login();
    }
  } else {
    next();
  }
});

export default router
