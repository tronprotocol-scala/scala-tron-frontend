import Vue from "vue";
import { router } from "./router";
import io from 'socket.io-client';

let url = `http://${window.location.host}`;

Vue.prototype.$socket = io(url);

import Wrap from "./components/wrap";

new Vue({
  router,
  components: {
    Wrap,
  },
  el: '#app',
  data() {
    return {
    };
  }
});