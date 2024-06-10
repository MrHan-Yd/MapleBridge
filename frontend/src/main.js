import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import ElementPlus from "element-plus" ;
import zhCn from "element-plus/es/locale/lang/zh-cn" ;
import "element-plus/dist/index.css"

import './assets/iconfont/iconfont.css'; // 引入自定义图标样式文件
import MyIcon from '@/components/MyIcon.vue'; // 你自己的vue文件路径

/* 暗黑模式css */
import 'element-plus/theme-chalk/dark/css-vars.css' ;

/* 滚动条样式 */
import '@/assets/css/global.css';

/* 引入echarts */
import echarts from './util/echarts' ;

const app = createApp(App) ;

/* 路由 */
app.use(router) ;

/* 饿了么UI，语言为中文 */
app.use(ElementPlus, {
    locale: zhCn
}) ;

/* echarts 挂在到Vue实例中 */
/**
 * 注意：app.config.globalProperties 和 app.provide('$echarts', echarts) 二选一即可
 * Vue.prototype.$echarts = echarts; -> vue2的挂载方式
 */

/* vue3的挂载方式（一个用于注册能够被应用内所有组件实例访问到的全局属性的对象。） */
app.config.globalProperties.$echarts = echarts;
/* vue3采用provide, inject方式使用 */
app.provide('$echarts', echarts);

/* 使用自定义组件，ICON */
app.component('MyIcon', MyIcon) ;

app.mount('#app') ;

