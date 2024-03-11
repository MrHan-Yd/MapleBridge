import {createRouter, createWebHistory} from "vue-router" ;
import {unauthorized} from "@/net/Login";
import {getUserRole} from "@/net/NetWork";

const router = createRouter({
    history: createWebHistory(import.meta.BASE_URL) ,
    routes: [
        {
            path: '/backend-welcome' ,
            name: 'backend-welcome' ,
            component: () => import('@/views/backend/WelcomeView.vue') ,
            children: [
                {
                    path: '/backend-welcome-login' ,
                    name: 'backend-welcome-login' ,
                    component: () => import('@/views/backend/welcome/LoginPage.vue')
                } , {
                    path: '/backend-welcome-register' ,
                    name: 'backend-welcome-register' ,
                    component: () => import('@/views/backend/welcome/RegisterPage.vue')
                } , {
                    path: '/backend-welcome-reset' ,
                    name: 'backend-welcome-reset' ,
                    component: () => import('@/views/backend/welcome/ResetPage.vue')
                }
            ]
        } , {
            path: '/backend-index' ,
            name: 'backend-index' ,
            component: () => import('@/views/backend/IndexView.vue'),
            roles: ['ADMIN'] ,
            children: [
                {
                    path: '/backend-index-workbench',
                    name: 'backend-index-workbench' ,
                    component: () => import('@/views/backend/index/WorkbenchPage.vue') ,
                } , {
                    path: '/backend-index-role',
                    name: 'backend-index-role',
                    component: () => import('@/views/backend/index/RolePage.vue')
                } , {
                    path: '/backend-index-role-status',
                    name: 'backend-index-role-status',
                    component: () => import('@/views/backend/index/RoleStatusPage.vue') ,
                } , {
                    path: '/backend-index-permission',
                    name: 'backend-index-permission',
                    component:() => import('@/views/backend/index/PermissionPage.vue')
                } , {
                    path: '/backend-index-permission-status',
                    name: 'backend-index-permission-status',
                    component: () => import('@/views/backend/index/PermissionStatusPage.vue')
                } , {
                    path: '/backend-index-user-level',
                    name: 'backend-index-user-level',
                    component: () => import('@/views/backend/index/UserLevelPage.vue')
                } , {
                    path: '/backend-index-user-status',
                    name: 'backend-index-user-status',
                    component: () => import('@/views/backend/index/UserStatusPage.vue')
                } , {
                    path: '/backend-index-user',
                    name: 'backend-index-user',
                    component: () => import('@/views/backend/index/UserPage.vue')
                } , {
                    path: '/backend-index-work-types',
                    name: 'backend-index-work-types',
                    component: () => import('@/views/backend/index/WorkTypePage.vue')
                } , {
                    path: '/backend-index-feedback',
                    name: 'backend-index-feedback',
                    component: () => import('@/views/backend/index/FeedbackPage.vue')
                } , {
                    path: '/backend-index-announcement',
                    name: 'backend-index-announcement',
                    component: () => import('@/views/backend/index/AnnouncementPage.vue')
                } , {
                    path: '/backend-index-announcement-status',
                    name: 'backend-index-announcement-status',
                    component: () => import('@/views/backend/index/AnnouncementStatusPage.vue')
                } , {
                    path: '/backend-index-announcement-types',
                    name: 'backend-index-announcement-types',
                    component: () => import('@/views/backend/index/AnnouncementTypePage.vue')
                } , {
                    path: '/backend-index-post-types',
                    name: 'backend-index-post-types',
                    component: () => import('@/views/backend/index/PostTypePage.vue')
                } , {
                    path: '/backend-index-post',
                    name: 'backend-index-post',
                    component: () => import('@/views/backend/index/PostPage.vue')
                }
            ]
        } , {
            path: "/frontend-welcome",
            name: 'welcome',
            component: () => import("@/views/frontend/WelcomeView.vue"),
            children: [
                {
                    path: "/frontend-welcome-login",
                    name: "welcome-login",
                    component: () => import("@/views/frontend/welcome/LoginPage.vue")
                } , {
                    path: "/frontend-welcome-register",
                    name: "welcome-register",
                    component: () => import("@/views/frontend/welcome/RegisterPage.vue")
                } , {
                    path: "/frontend-welcome-reset",
                    name: "welcome-reset",
                    component: () => import("@/views/frontend/welcome/ResetPage.vue")
                }
            ]
        } , {
            path: "/frontend-index",
            name: 'index',
            component: () => import("@/views/frontend/IndexView.vue"),
            children: [
                {
                    path: "/frontend-index-home",
                    name: "indexHome",
                    component: () => import("@/views/frontend/index/IndexPage.vue")
                }
            ]
        } , {
            path: '/unauthorized',
            name: 'unauthorized',
            component: () => import('@/views/backend/system/UnauthorizedPage.vue')
        } , {
            /*404*/
            path: '/404',
            name: 'not-found',
            component: () => import('@/views/backend/system/404Page.vue')
        } , {
        // 最后的通配符路由{
            path: '/:catchAll(.*)',
            redirect: '/404' // 将匹配到的路径重定向到 /not-found,
        }
    ]
}) ;

/* 配置路由守卫，防止用户不登录能访问需要登录的页面 */
router.beforeEach((to, from, next) => {
    /* 从本地存储获取用户角色信息 */
    const userRole = getUserRole();

    /* 判断是否登录 */
    const isUnauthorized = unauthorized();

    /* 如果用户已经登录了，还要访问登录页面*/
    if ((to.name.startsWith("backend-welcome-") || to.name.startsWith("frontend-welcome-")) && !isUnauthorized) {
        if (userRole === "ADMIN") {
            next("/backend-index");
        } else {
            next("/frontend-index");
        }
    }
    /* 用户没有登录，但是去访问主页 */
    else if (to.fullPath.startsWith("/frontend-index-") && isUnauthorized) {
        next("/frontend-welcome-login");
    } else if (to.fullPath.startsWith("/backend-index-") && isUnauthorized) {
        next("/backend-welcome-login");
    }
    /* 针对管理员和普通用户的权限判断 */
    else if (to.fullPath.startsWith("/backend-index-") && userRole !== "ADMIN") {
        // 如果是普通用户访问后台页面，可以跳转到没有权限的提示页面
        next("/unauthorized");
    } else if (to.fullPath.startsWith("/frontend-index-") && userRole !== "USER") {
        // 如果是管理员访问前台页面，可以跳转到没有权限的提示页面
        next("/unauthorized");
    } else {
        next();
    }
});

export default router