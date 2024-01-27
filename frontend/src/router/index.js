import {createRouter, createWebHistory} from "vue-router" ;
// import {unauthorized} from "@/net/NetWork";

const router = createRouter({
    history: createWebHistory(import.meta.BASE_URL) ,
    routes: [
        {
            path: '/backend-welcome' ,
            name: 'backend-welcome' ,
            component: () => import('@/views/backend/WelcomeView.vue') ,
            children: [
                {
                    path: '/backend-login' ,
                    name: 'backend-login' ,
                    component: () => import('@/views/backend/welcome/LoginPage.vue')
                } , {
                    path: '/backend-register' ,
                    name: 'backend-register' ,
                    component: () => import('@/views/backend/welcome/RegisterPage.vue')
                } , {
                    path: '/backend-reset' ,
                    name: 'backend-reset' ,
                    component: () => import('@/views/backend/welcome/ResetPage.vue')
                }
            ]
        } , {
            path: '/backend-index' ,
            name: 'backend-index' ,
            component: () => import('@/views/backend/IndexView.vue'),
            children: [
                {
                    path: '/backend-workbench',
                    name: 'backend-workbench' ,
                    component: () => import('@/views/backend/index/WorkbenchPage.vue') ,
                } , {
                    path: '/backend-role',
                    name: 'backend-role',
                    component: () => import('@/views/backend/index/RolePage.vue')
                } , {
                    path: '/backend-role-status',
                    name: 'backend-role-status',
                    component: () => import('@/views/backend/index/RoleStatusPage.vue') ,
                } , {
                    path: '/backend-permission',
                    name: 'backend-permission',
                    component:() => import('@/views/backend/index/PermissionPage.vue')
                } , {
                    path: '/backend-permission-status',
                    name: 'backend-permission-status',
                    component: () => import('@/views/backend/index/PermissionStatusPage.vue')
                }
            ]
        }
    ]
}) ;

/* 配置路由守卫，防止用户不登录能访问需要登录的页面 */
// router.beforeEach((to, from, next) => {
//     /* 判断是否登录 */
//     const isUnauthorized = unauthorized() ;
//     /* 如果用户已经登录了，还要访问页面的登录页面*/
//     if(to.name.startsWith("welcome-") && !isUnauthorized) {
//         next("/index") ;
//
//         /*用户没有登录，但是去访问主页*/
//     } else if(to.fullPath.startsWith("/index") && isUnauthorized) {
//         next("/") ;
//     } else {
//         next() ;
//     }
// }) ;

export default router