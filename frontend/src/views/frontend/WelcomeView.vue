<script setup>
import router from "@/router";
import {onMounted} from "vue";


/* 页面打开时默认调用 */
onMounted(() => {
  router.push('/frontend-welcome-login') ;
});
</script>

<template>
  <div id="login_back">
    <div id="login_left">
      <div id="login_logo_box">
        <el-image id="login_logo"
                  style="
                background-color: whitesmoke;
                height: 50px;
                transform: translateX(70%) translateY(70%) scale(2) rotate(3deg) ;
                border-radius: 100px 1px;
                box-shadow: 2px 3px 3px #0bf1b7;
                mix-blend-mode: overlay ;
                "
                :src="'/images/logo/MapleBridg-logo.png'" />
      </div>

    </div>
    <div id="login_right">
      <div style="padding: 20px 40px">
        <router-view v-slot="{ Component }">
          <!--过渡动画-->
          <transition name="el-fade-in-linear" mode="out-in">
            <div :key="$route.path">
              <component :is="Component" />
            </div>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<style scoped>
#login_back {
  width: 100vw;
  height: 100vh;
  background-color: #f5f6f9;
  display: flex;
  justify-content: center;
  align-items: center;
}

#login_left {
  width: 55%;
  height: 60%;
  background: url('../../../public/images/shiji.jpg') center/cover ;
  border-radius: 10px 0 0 10px ;
  box-shadow:7px 5px 5px #999999;
}
#login_logo_box{
  height: 100px ;
  width: 200px;
  //overflow: hidden;
}

#login_right {
  width: 25%;
  height: 60%;
  background-color: white;
  border-radius: 0 10px 10px 0 ;
  box-shadow:5px 5px 5px #ababab;
}
</style>