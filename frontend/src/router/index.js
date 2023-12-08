import { createRouter, createWebHistory } from 'vue-router'
import SongsHomeView from '../views/SongsHomeView.vue'
import SongDetailsView from '../views/SongDetailsView.vue'
import SongUploadView from '../views/SongUploadView.vue'
import keycloakService from '@/security/keycloak'
import MySongsView from '../views/MySongsView.vue'

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
    path: '/song/upload',
    name: 'SongUpload',
    component: SongUploadView,
    meta: {
      requiresAuth: true,
      requiredRole: 'ADMIN'
    }
  },
  {
    path: '/mysongs',
    name: 'MySongs',
    component: MySongsView,
    meta: {
      requiresAuth: true,
      requiredRole: 'ADMIN'
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
      const requiredRole = to.meta.requiredRole
      if(requiredRole && !keycloakService.hasResourceRole(requiredRole)){
        next("/");
      }
      else{
        next();
      }
    } else {
      keycloakService.login();
    }
  } else {
    next();
  }
});

export default router
