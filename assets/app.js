import Vue from "vue";
import { router } from "./router";

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