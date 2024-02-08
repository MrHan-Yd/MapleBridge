import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import axios from "axios";
import ElementPlus from "element-plus" ;
import zhCn from "element-plus/es/locale/lang/zh-cn" ;
import "element-plus/dist/index.css"

import './assets/iconfont/iconfont.css'; // 引入自定义图标样式文件
import MyIcon from '@/components/MyIcon.vue'; // 你自己的vue文件路径

/* 暗黑模式css */
import 'element-plus/theme-chalk/dark/css-vars.css' ;

/* 滚动条样式 */
import '@/assets/css/global.css';

/* 配置后端服务器接口 */
axios.defaults.baseURL = 'http://localhost:9999' ;

const app = createApp(App) ;

/* 路由 */
app.use(router) ;

/* 饿了么UI，语言为中文 */
app.use(ElementPlus, {
    locale: zhCn
}) ;

/* 使用自定义组件，ICON */
app.component('MyIcon', MyIcon) ;

app.mount('#app') ;
