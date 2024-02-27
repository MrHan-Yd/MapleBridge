<template>
  <div style="width:100vw; height:100vh; overflow: hidden; display: flex">
    <div style="flex: 1; background-color: black ;">
      <el-image style="width:100%; height:100%" fit="cover"
          src="./images/shiji.jpg" />
    </div>
    <div class="welcome-title">
      <div style="font-size:30px; font-weight:bold">欢迎来到校友会管理平台</div>
      <div style="margin-top: 10px">在这里你可以更高效地管理校友会平台</div>
      <div style="margin-top: 5px"></div>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <!--过渡动画-->
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import router from "@/router";
import {onMounted} from "vue";

/* 页面打开时默认调用 */
onMounted(() => {
  router.push('/backend-welcome-login') ;
});
</script>

<style scoped>
.welcome-title {
  position: absolute ;
  bottom: 30px ;
  left: 30px ;
  color: white ;
  text-shadow: 0 0 10px black ;
}
.right-card {
  width: 450px ;
  z-index: 1 ;
  background-color: var(--el-bg-color) ;
}
</style>