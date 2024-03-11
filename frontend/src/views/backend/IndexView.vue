<script setup>
import {logout} from "@/net/Login";
import router from "@/router" ;
import {
  Document,
  HomeFilled,
  Setting,
  DataLine,
  Odometer,
  TurnOff,
} from '@element-plus/icons-vue' ;
import {onMounted} from "vue";
import MyIcon from "@/components/MyIcon.vue";

/* 页面打开时默认调用 */
onMounted(() => {
  router.push('/backend-index-workbench') ;
});

/* 退出登录 */
function userLogout() {
  logout(() => {
    router.push('/backend-welcome-login');
  });
}

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<template>
  <div id="index_content">
    <div id="top">
      <el-row :gutter="20" class="centered-content">
        <el-col :span="6" class="centered-content">
          <el-image :src="'/images/logo/MapleBridg-logo.png'" id="logo-img" alt="logo"/>
          <el-text id="logo-text">校友会智慧管理平台</el-text>
        </el-col>
        <el-col :span="12">

        </el-col>
        <el-col :span="6" class="centered-content flex-end">
          <el-tooltip
              effect="light"
              content="主页"
              placement="bottom"
          >
            <el-link @click="router.push('/index')" :underline="false" id="home_link">
              <el-icon :size="30">
                <HomeFilled/>
              </el-icon>
            </el-link>
          </el-tooltip>
          <div id="avatar-div" class="centered-content centered-content-center el-dropdown-link">
            <el-dropdown>
              <el-avatar :size="50" :src="'/images/touxiang.jpg'"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>Action 2</el-dropdown-item>
                  <el-dropdown-item>Action 3</el-dropdown-item>
                  <el-dropdown-item disabled>Action 4</el-dropdown-item>
                  <el-dropdown-item divided @click="userLogout">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-col>
      </el-row>
    </div>
    <div id="bottom">
      <el-row style="margin-top: 5px; height: 100%;">
        <el-col :span="3" style="background-color: white; height: 100%;  overflow-x: hidden; overflow-y: scroll;">
          <el-menu
              default-active="1-1"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose"
          >
            <el-sub-menu index="1" >
              <template #title>
                <my-icon name="icon-yibiaopan" />
                <span>仪表盘</span>
              </template>
                <el-menu-item index="1-1">
                  <el-icon><DataLine /></el-icon>
                  <span @click="router.push('backend-index-workbench')">工作台</span>
                </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Odometer /></el-icon>
                <span>数据可视化</span>
              </template>
              <el-menu-item index="2-1">
                <my-icon name="icon-shujufenxi"/>
                <span @click="router.push('/backend-index-user-level')">数据分析</span>
              </el-menu-item>
              <el-menu-item index="2-2">
                <my-icon name="icon-duoweishujufenxi"/>
                <span @click="router.push('/backend-index-user-level')">多维数据分析</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <my-icon name="icon-gonggaoguanli" />
                <span>公告管理</span>
              </template>
              <el-menu-item index="3-1">
                <my-icon name="icon-navicon-gggl"/>
                <span @click="router.push('/backend-index-announcement')">平台公告管理</span>
              </el-menu-item>
              <el-menu-item index="3-2">
                <my-icon name="icon-gonggaoleixingguanli"/>
                <span @click="router.push('/backend-index-announcement-types')">公告类型管理</span>
              </el-menu-item>
              <el-menu-item index="3-3">
                <el-icon><TurnOff /></el-icon>
                <span @click="router.push('/backend-index-announcement-status')">公告状态管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="4">
              <template #title>
                <my-icon name="icon-yonghu"/>
                <span>用户管理</span>
              </template>
              <el-menu-item index="4-1">
                <my-icon name="icon-yonghuguanli"/>
                <span @click="router.push('/backend-index-user')">平台用户管理</span>
              </el-menu-item>
              <el-menu-item index="4-2">
                <el-icon><TurnOff /></el-icon>
                <span @click="router.push('/backend-index-user-status')">用户状态管理</span>
              </el-menu-item>
              <el-menu-item index="4-3">
                <my-icon name="icon-gongzuo" />
                <span @click="router.push('/backend-index-work-types')">工作类型管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="5">
              <template #title>
                <my-icon name="icon-guanliyuan_jiaoseguanli"/>
                <span>角色管理</span>
              </template>
              <el-menu-item index="5-1">
                <my-icon name="icon-jiaoseguanli"/>
                <span @click="router.push('/backend-index-role')">平台角色管理</span>
              </el-menu-item>
              <el-menu-item index="5-2">
                <el-icon><TurnOff /></el-icon>
                <span @click="router.push('/backend-index-role-status')">角色状态管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="6">
              <template #title>
                <my-icon name="icon-quanxianguanli-" />
                <span>权限管理</span>
              </template>
              <el-menu-item index="6-1">
                <my-icon name="icon-authority"/>
                <span @click="router.push('/backend-index-permission')">平台权限管理</span>
              </el-menu-item>
              <el-menu-item index="6-2">
                <el-icon><TurnOff /></el-icon>
                <span @click="router.push('/backend-index-permission-status')">权限状态管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="7">
              <template #title>
                <my-icon name="icon-menhu_yonghudengjiguanli"/>
                <span>等级管理</span>
              </template>
              <el-menu-item index="7-1">
                <my-icon name="icon-dengjiguanli1"/>
                <span @click="router.push('/backend-index-user-level')">平台等级管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="8">
              <template #title>
                <my-icon name="icon-fankuiguanli1"/>
                <span>反馈管理</span>
              </template>
              <el-menu-item index="8-1">
                <my-icon name="icon-fankuiguanli"/>
                <span @click="router.push('/backend-index-feedback')">平台反馈管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="9">
              <template #title>
                <my-icon name="icon-tieziguanli"/>
                <span>帖子管理</span>
              </template>
              <el-menu-item index="9-1">
                <my-icon name="icon-tieziguanli1"/>
                <span @click="router.push('/backend-index-post')">平台帖子管理</span>
              </el-menu-item>
              <el-menu-item index="9-2">
                <my-icon name="icon-type" />
                <span @click="router.push('/backend-index-post-types')">帖子类型管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="10" disabled>
              <el-icon><document /></el-icon>
              <span>Navigator Three</span>
            </el-menu-item>
            <el-menu-item index="11">
              <el-icon><setting /></el-icon>
              <span>Navigator Four</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="21">
            <router-view v-slot="{ Component }">
              <!--过渡动画-->
              <transition name="el-fade-in-linear" mode="out-in">
                <div :key="$route.path">
                  <component :is="Component" />
                </div>
              </transition>
            </router-view>
        </el-col>
      </el-row>
    </div>
  </div>
<!--返回顶部-->
  <template>
    <el-backtop :right="100" :bottom="100" />
  </template>
  <!--返回顶部-->
</template>

<style scoped>
#index_content {
  //display: flex ;
  background-color: #f5f6f9;
  margin: 0; /* 可以去除 body 的默认边距 */
  min-height: 100vh; /* 使 body 至少铺满视窗高度 */
}

#top {
  //background-color: burlywood;
  height: 80px;
  //height: calc(100% * 20 / 100) ;
  width: 100%;
  background-color: white;
  align-items: center; /* 垂直居中 */
  box-shadow: 0 2px 2px #999;
}

#top >>> #home_link {
  margin-right: 20px;
  font-size: 20px;
}

#top >>> #logo-img {
  margin-left: 25px;
  width: auto;
  height: 80px;
  max-width: 100%; /* 设置最大宽度，防止超过容器宽度 */
  max-height: 60px; /* 设置最大高度为容器高度，确保在容器内显示 */
  transform: scale(2);
  transform-origin: center 40%;
}

#top >>> #logo-text {
  margin-left: 20px;
  font-size: 20px;
}

#top >>> #avatar-div {
  height: 100%;
  width: 90px;
}

#top >>> #avatar-div:hover {
  background-color: #e5e5e5;
}

#bottom {
  //height: calc(100% * 20) ;
  height: 88.5vh;
  width: 100%;
  //background-color: white ;
  //background-color: pink;
}

.centered-content {
  display: flex;
  align-items: center; /* 垂直居中 */
  height: 100%;
}

.centered-content-center {
  display: flex;
  justify-content: center;
  height: 100%;
}

.flex-end {
  display: flex;
  justify-content: flex-end;
}
</style>