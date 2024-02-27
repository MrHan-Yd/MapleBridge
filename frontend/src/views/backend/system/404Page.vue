<script setup>

import router from "@/router";

function resetHome(){
  router.push("/frontend-index") ;
}
</script>

<template>
  <div class="not-found">
    <img src="../../../assets/image/404.gif" alt="404" />
  </div>
  <div class="not-found-text">
    <el-button size="large" round @click="resetHome">返回首页</el-button>
  </div>
</template>

<style scoped>
.not-found {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
}
.not-found img {
  width: 100%;
  height: 100%;
}
.not-found-text{
  position: absolute;
  top: 50px;
  right: 50px;
  //transform: rotate(-10deg);
}
</style>