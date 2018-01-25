import VueRouter from "vue-router";
import Vue from "vue";
import Dashboard from "./components/dashboard.vue";

Vue.use(VueRouter);

const routes = [
    { path: '/', component: Dashboard },
];

export const router = new VueRouter({
  routes
});