import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SongDetailsView from '../views/SongDetailsView.vue'
import UploadView from '../views/UploadView.vue'
import keycloakService from '@/security/keycloak'
import MySongsView from '../views/MySongsView.vue'
import UpdateView from '../views/UpdateView.vue'
import PlaylistDetailsView from '../views/PlaylistDetailsView.vue'

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
