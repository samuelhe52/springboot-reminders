import { createRouter, createWebHistory } from "vue-router";
import AuthView from "../views/AuthView.vue";
import TodoView from "../views/TodoView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", redirect: "/auth" },
    { path: "/auth", component: AuthView },
    { path: "/todo", component: TodoView },
  ],
});

export default router;
