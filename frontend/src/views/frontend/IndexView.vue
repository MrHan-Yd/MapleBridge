<script setup>
import { ref, reactive, onMounted } from 'vue'
import {Plus, Search} from "@element-plus/icons-vue";
import MyIcon from "@/components/MyIcon.vue";
import {logout} from "@/net/Login";
import router from "@/router";


/* 页面打开时默认调用 */
onMounted(() => {
  router.push('/frontend-index-home') ;
});

/* 退出登录 */
function userLogout() {
  logout(() => {
    router.push('/frontend-welcome-login');
  });
}

/* 所有表单 */
const searchForm = reactive({
  content : '' ,
}) ;

/* 菜单对应页面 */
const activeIndex = ref('1');

/* 菜单选择 */
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<template>
  <div id="box">
    <div id="box_top">
      <div id="box_top_content">
        <el-row class="centered" :gutter="20">
          <el-col :span="2">
            <el-image :src="'/images/logo/MapleBridg-logo-blue.png'" style="height: 70px; transform: scale(1.4)"
                      alt="logo"/>
          </el-col>
          <el-col :span="11">
            <el-menu
                :default-active="activeIndex"
                mode="horizontal"
                @select="handleSelect"
            >
              <el-menu-item index="1">
                <template #title>
                  <my-icon name="icon-shouye"/>
                  <span>首页</span>
                </template>
              </el-menu-item>
              <el-menu-item index="2">
                <template #title>
                  <my-icon name="icon-fenxiang"/>
                  <span>学术分享</span>
                </template>
              </el-menu-item>
              <el-menu-item index="3">
                <template #title>
                  <my-icon name="icon-xiaoyuanshenghuo"/>
                  <span>校园生活</span>
                </template>
              </el-menu-item>
              <el-menu-item index="4">
                <template #title>
                  <my-icon name="icon-hubanghuzhu"/>
                  <span>互帮互助</span>
                </template>
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :span="8">
            <el-input
                v-model="searchForm.content"
                style="width: 350px; height: 40px;"
                placeholder="输入搜索关键词"
                class="input-with-select"
            >
              <template #append>
                <el-button :icon="Search"/>
              </template>
            </el-input>
          </el-col>
          <el-col :span="3" class="el-dropdown-link">
            <el-dropdown>
              <el-avatar id="avatar" shape="square" :size="50" :src="'/images/touxiang-1.jpg'"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>设置中心</el-dropdown-item>
<!--                  <el-dropdown-item>Action 3</el-dropdown-item>-->
<!--                  <el-dropdown-item disabled>Action 4</el-dropdown-item>-->
                  <el-dropdown-item divided @click="userLogout">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
        </el-row>
      </div>
    </div>
    <div id="box_bottom">
      <div id="box_bottom_content">
        <div id="bottom_content_left">
          <router-view v-slot="{ Component }">
            <!--过渡动画-->
            <transition name="el-fade-in-linear" mode="out-in">
              <div :key="$route.path">
                <component :is="Component" />
              </div>
            </transition>
          </router-view>
        </div>
        <div id="bottom_content_right">
          <div id="content_right_top">
            <div class="right_top_title">
              <my-icon name="icon-fenxiang" />
              分享中心
            </div>
            <div id="right_top_content">
              <div id="top_content_text">
                <p>开始分享之旅</p>
                <span>分享校园生活、学术、互帮互助</span>
              </div>
              <div id="top_content_img">
                <el-image style=" height: 80%" :src="'/images/kapibala.jpg'" alt="卡皮巴拉" />
              </div>
            </div>
            <div id="right_top_button">
              <el-button :icon="Plus" style="width: 100%" size="small">
                开&nbsp;始&nbsp;分&nbsp;享
              </el-button>
            </div>
          </div>
          <div id="content_right_middle">
            <div class="right_top_title">
              <my-icon name="icon-remensousuo" />
              热门搜索
            </div>
          </div>
          <div id="content_right_bottom">

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.centered {
  display: flex;
  justify-content: center;
  align-items: center;
}

#box {
  margin: 0;
  padding: 0;
  height: 100vh;
  width: 100vw;
  background-color: #f5f6f9;
}

#box_top {
  height: 70px;
  width: 100%;
  background-color: white;
  box-shadow: 0 2px 2px #999;
  display: flex;
  justify-content: center;
  align-items: center;
}

#box_top_content {
  height: 100%;
  width: 80%;
}

#box_bottom {
  margin-top: 10px;
  height: calc(100vh - 70px - 20px);
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

#box_bottom_content {
  height: 100%;
  width: 70%;
  display: flex;
  //background-color: white;
}

#bottom_content_left {
  height: 100% ;
  width: calc(75% - 20px );
  background-color: white;
  margin-right: 10px ;
  box-shadow: 1px 2px 3px #999;
}
#bottom_content_right {
  height: 100% ;
  width: 25% ;
  //background-color: white;
}
#content_right_top {
  height: 20%;
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}
.right_top_title {
  height: 20%;
  width: 100%;
}
#right_top_content {
  height: 55%;
  width: 100%;
  background-color: #f5f6f9;
  display: flex;
}
#top_content_text {
  margin-top: 5px;
  height: 100%;
  width: 68%;
  //display: flex;
  //justify-content: center;
  //align-items: center;
  padding-left: 10px;
}
#top_content_text p {
  margin: 0;
  font-size: 14px;
  padding: 5px 0 0 0 ;
}
#top_content_text span {
  font-size: 12px;
  color: #999999;
}
#top_content_img {
  height: 100%;
  width: 30%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-right: 8px;
  margin-left: 4px;
}
#right_top_button {
  height: 25%;
  width: 100%;
  margin-top: 10px;
}

#content_right_middle {
  margin-top: 10px;
  margin-bottom: 10px;
  height: calc(51% - 20px);
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}
#content_right_bottom {
  height: 20%;
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}
</style>