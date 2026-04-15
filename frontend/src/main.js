import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "element-plus/theme-chalk/dark/css-vars.css";
import App from "./App.vue";
import router from "./router";

const darkModeQuery = window.matchMedia("(prefers-color-scheme: dark)");
const syncDarkMode = () => {
  document.documentElement.classList.toggle("dark", darkModeQuery.matches);
};

syncDarkMode();
darkModeQuery.addEventListener("change", syncDarkMode);

createApp(App).use(router).use(ElementPlus).mount("#app");
