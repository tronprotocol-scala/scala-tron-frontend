import VueRouter from "vue-router";
import Vue from "vue";
import Dashboard from "./components/dashboard.vue";
import Network from "./components/network.vue";

Vue.use(VueRouter);

const routes = [
    { path: '/dashboard', component: Dashboard },
    { path: '/network', component: Network },
];

export const router = new VueRouter({
  routes
});