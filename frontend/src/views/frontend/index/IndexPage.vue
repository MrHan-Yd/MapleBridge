<script setup>
import { get } from "@/net/NetWork";
import { ref, provide } from "vue";
import SkeletonPostData from "@/components/SkeletonPostData.vue";

/* 默认选中推荐 */
const activeIndex = ref('1');

/* 决定布局是否包含图片 */
const whetherHaveImg = ref(true);

/* 计算骨架的高度 */
function calculateSkeletonRows() {
  // 获取屏幕高度
  const screenHeight = window.innerHeight;

  // 假设每行骨架高度为 48.5px，根据屏幕高度计算行数
  const rowHeight = 48.5; // 假设每行高度为 48.5px
  return Math.floor(screenHeight / rowHeight);
}

// 定义是否显示骨架屏的变量
const whetherShow = ref(true);

/* 子组件调用，rs为结果，true为查询到数据，显示数据，false为未查询到数据，显示骨架*/
const skeletonPost = function (rs) {
  whetherShow.value = rs ;
}


/* 页面打开调用子组件函数获取数据 */
</script>

<template>
  <div id="box">
    <div id="top">
      <el-menu
          :default-active="activeIndex"
          class="el-menu"
          mode="horizontal"
          :popper-offset="16"
      >
        <el-menu-item index="1">推荐</el-menu-item>
        <el-menu-item index="2">热门</el-menu-item>
      </el-menu>
    </div>
    <div id="bottom">
      <div class="content" v-if="whetherShow">
        <SkeletonPostData @whetherShow="skeletonPost" bottom-text="底部" />
      </div>
      <div class="content" v-else>
        <el-skeleton :rows="calculateSkeletonRows()" animated />
      </div>
    </div>
  </div>
</template>

<style scoped>
#box {
  height: calc(100vh - 70px - 20px);
  background-color: #f5f6f9;
  width: calc(100vw - 95.5%);
  //height: calc(100vh - 70px - 70px - 20px - 5px);

  #top {
    background-color: white;
    height: 10% ;
    display: flex;
    align-items: center;
    width: 100%;

    .el-menu {
      padding-left: 20px;
      width: 100%;
    }

  }
  #bottom {
    margin-top: 10px;
    height: calc(90% - 10px);
    background-color: white;

    .content {
      padding: 10px ;
      height: calc(100% - 10px - 20px) ;
      overflow: hidden auto ;

    }
  }
}
</style>