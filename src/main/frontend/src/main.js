import Vue from 'vue';
import App from './App.vue';
import VueResource from "vue-resource";

Vue.use(VueResource);

// Skonfiguruj nową zależność
// ustaw prefix dla wszystkich żądań taki jak w backendzie: /api.
Vue.http.options.root = '/api';

new Vue({
    el: '#app',
    render: h => h(App)
});
