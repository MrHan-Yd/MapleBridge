<script setup>
import { defineProps, ref, onMounted, computed, reactive } from 'vue';
import {get, getAllParameters} from '@/net/NetWork' ;
import {ElWarning} from "@/util/MessageUtil";

/* 分页信息 */
const page = reactive({
  num: 1,
  size: 4,
  total: 0,
});

/* 加载实体类 */
const data = ref([]);

// 定义组件的 props
const props = defineProps({
  bottomText: {
    type: String,
    default: '底部'
  },
  whetherShow: Boolean // 添加 whetherShow 到 props 中
});

const loading = ref(false);
const noMore = ref(false);
const disabled = computed(() => loading.value || noMore.value);

/* 滚动加载 */
const load = () => {
  loading.value = true;

  if (!noMore.value) {
    page.num += 1;
  }
  getPostEs(page);
}


let $emit = defineEmits(['whetherShow']);


/* 页面打开查询帖子数据 */
function getPostEs(page) {
  getAllParameters("api/index/post-es?pageNum=" + page.num + "&pageSize=" + page.size, (rs) => {
    if (rs.code === 200) {
      data.value = [...data.value, ...rs.data.content];
      noMore.value = rs.data.last;
      page.num = rs.data.pageable.pageNumber + 1;
      page.size = rs.data.pageable.pageSize;
      page.total = rs.totalPages;
      loading.value = false;
      /* 显示数据 */
      $emit('whetherShow', true) ;
    } else {
      /* 显示骨架 */
      $emit('whetherShow', false) ;
    }
  }, (message) => {
    ElWarning(message) ;
    /* 显示骨架 */
    $emit('whetherShow', false) ;
  }, (err => {
    ElWarning(err.message) ;
    /* 显示骨架 */
    $emit('whetherShow', false) ;
  }));
}
getPostEs(page);
</script>

<template>
  <div class="skeleton"
       v-for="item in data" :key="item"
       :infinite-scroll-disabled="disabled"
       v-infinite-scroll="load">
    <div class="title">
      {{ item.topic }}
    </div>
    <div class="content">
      <div class="box" v-if="item.filePost.length !== 0">
        <div class="left">
          <el-image style="height: 100%; width: 100%" :src="item.filePost[0].url" alt="图片"/>
        </div>
        <div class="right">
          {{ item.content }}
          <button>你好</button>
        </div>
      </div>
      <div class="box2" v-else>
        <div class="content">
          {{ item.content }}
        </div>
      </div>
    </div>
    <div class="bottom">
      {{ bottomText }}
    </div>
  </div>
  <div id="footer">
    <p v-if="loading">加载中...</p>
    <p v-if="noMore">没有更多数据...</p>
  </div>
</template>

<style scoped>
.skeleton {
  border-top: 1px solid #e3e3e3;
  margin-top: 5px;
  margin-bottom: 5px;

  .title {
    padding: 10px;
    width: 100%;
    height: 10%;
    font-size: 20px;
  }

  .content {
    height: 100px;
    padding: 0 10px;

    .box {
      height: 100%;
      display: flex;
      align-items: center;
      background-color: white;

      .left {
        height: 100%;
        width: 30%;
        display: flex;
        align-items: center;
        justify-content: center;

      }

      .right {
        margin-left: 10px;
        height: 100%;
        width: calc(80% - 15px);
        line-height: 1.5em;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
      }
    }

    .box2 {
      height: 100%;

      .content {
        width: calc(100% - 20px);
        line-height: 1.5em;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
      }
    }
  }

  .bottom {
    height: 30px;
    width: calc(100% - 20px);
    margin-left: 10px;
    display: flex;
    align-items: center;
  }
}

#footer {
  background-color: #f1f0f0;
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>