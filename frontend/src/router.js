import Home from "@/components/Home.vue";
import Tags from "@/components/Tags.vue";
import {createRouter,createWebHistory} from "vue-router";
import Edit from "@/components/Edit.vue";

const routes=[
    {path:'/', name:'Home', component:Home},
    {path: '/tags', name:'Tags', component: Tags},
    {path: '/edit', name:'Edit', component: Edit}
];
const router=createRouter({history: createWebHistory(), routes});
export default router