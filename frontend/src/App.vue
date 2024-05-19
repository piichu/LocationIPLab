<script>
import Home from "@/components/Home.vue";
import Tags from "@/components/Tags.vue";
import Edit from "@/components/Edit.vue";
export default {
  routes:[
    {path:'/', Home},
    {path: '/tags', Tags},
    {path: '/edit', Edit}
  ]
}
</script>
<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>