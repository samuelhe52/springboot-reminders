import {createRouter, createWebHistory} from 'vue-router'
import AuthView from '../views/AuthView.vue'
import CategoryView from '../views/CategoryView.vue'
import TodoView from '../views/TodoView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {path: '/', redirect: '/auth'},
    {path: '/auth', component: AuthView},
    {path: '/category', component: CategoryView},
    {path: '/todo', component: TodoView}
  ]
})

export default router
