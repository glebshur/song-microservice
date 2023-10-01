import { createRouter, createWebHistory } from 'vue-router'
import SongsHomeView from '../views/SongsHomeView.vue'
import SongDetailsView from '../views/SongDetailsView.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
