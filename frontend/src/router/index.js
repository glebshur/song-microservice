import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SongDetailsView from '../views/SongDetailsView.vue'
import UploadView from '../views/UploadView.vue'
import keycloakService from '@/security/keycloak'
import MySongsView from '../views/MySongsView.vue'
import UpdateView from '../views/UpdateView.vue'
import PlaylistDetailsView from '../views/PlaylistDetailsView.vue'
import ErrorView from '../views/ErrorView.vue'
import AudioEditView from '../views/AudioEditView.vue'

const routes = [
  {
    path: '/',
    name: 'SongsHome',
    component: HomeView
  },
  {
    path: '/song/:id',
    name: 'SongDetails',
    component: SongDetailsView
  },
  {
    path: '/song/upload',
    name: 'SongUpload',
    component: UploadView,
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
      requiredRole: 'USER'
    }
  },
  {
    path: '/song/:id/update',
    name: 'SongUpdate',
    component: UpdateView,
    meta: {
      requiresAuth: true,
      requiredRole: 'ADMIN'
    }
  },
  {
    path: '/playlist/:id',
    name: 'PlaylistDetails',
    component: PlaylistDetailsView
  },
  {
    path: '/song/:id/edit',
    name: 'AudioEdit',
    component: AudioEditView,
    meta: {
      requiresAuth: true,
      requiredRole: 'USER'
    }
  },
  {
    path: '/error/:errorCode',
    name: 'Error',
    component: ErrorView,
    props: true
  },
  {
    path: '/:catchAll(.*)',
    name: 'NotFound',
    component: ErrorView,
    props: {
      errorCode: '404'
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
        next("/error/403");
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
