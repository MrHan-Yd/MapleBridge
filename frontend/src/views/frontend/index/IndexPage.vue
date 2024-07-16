<script setup>
import { ref, nextTick, watch, onMounted } from "vue";
import SkeletonPostData from "@/components/SkeletonPostData.vue";
import {SkeletonMenuEnum} from "@/domain/enum/SkeletonMenuEnum";

/* 登录用户信息 */
const props = defineProps({
  userInfo: {
    type: Object,
  },
  menuId: ref({
    type: String,
    default: ''
  })
});

onMounted(() => {
    /* 子菜单切回推荐 */
    activeIndex.value = '1';
    activeName.value = SkeletonMenuEnum[activeIndex.value];
    /* 调用子组件重新获取数据 */
    switchMainMenu();
});

/* 第一次不监听 */
const whetherFirstLoad = ref(false);
/* 主菜单变化调用重新获取数据 */
watch(props, () => {
    /* 子菜单切回推荐 */
  if (whetherFirstLoad.value) {
    activeIndex.value = '1';
    activeName.value = SkeletonMenuEnum[activeIndex.value];
    /* 调用子组件重新获取数据 */
    switchMainMenu();
  }
});
/* 延迟半秒 */
setTimeout(() => {
  /* 改为非首次加载 */
  whetherFirstLoad.value = true;
}, 500);

/* 默认选中推荐 */
const activeIndex = ref('1');
const activeName = ref(SkeletonMenuEnum['1']);
const child = ref(null);

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

const handleClick = (key, _keyPath) => {
  activeIndex.value = key;
  activeName.value = SkeletonMenuEnum[activeIndex.value];

  /* 调用子组件获取帖子函数 */
  switchMainMenu();
}
const switchMainMenu = () => {
  nextTick(() => {
    // 调用子组件的方法
    child.value.getPostEs();
  });
}

/* 暴露出去 */
defineExpose({
  switchMainMenu
});


// 定义是否显示骨架屏的变量
const whetherShow = ref(true);

/* 子组件调用，rs为结果，true为查询到数据，显示数据，false为未查询到数据，显示骨架*/
const skeletonPost = function (rs) {
  whetherShow.value = rs ;
}

/* 重置滚动条 */
const scrollContainer = ref(null);
const resetScroll = () => {
  if (scrollContainer.value) {
   scrollContainer.value.scrollTo({
      top: 0,
      behavior: 'smooth' // 可选，平滑滚动
    });
  }
};
</script>

<template>
  <div id="box">
    <div id="top">
      <el-menu
          :default-active="activeIndex"
          class="el-menu"
          mode="horizontal"
          :popper-offset="16"
          @select="handleClick"
      >
        <el-menu-item index="1">推荐</el-menu-item>
        <el-menu-item index="2">热门</el-menu-item>
      </el-menu>
    </div>
    <div id="bottom">
      <div class="content" v-if="whetherShow" ref="scrollContainer">
        <SkeletonPostData @restScroll="resetScroll" ref="child" @whetherShow="skeletonPost" :menu="activeName" :mainMenuId="menuId" :user="userInfo" bottom-text="底部" />
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