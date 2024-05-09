<script setup>
import BadgeAssembly from "@/components/BadgeAssembly.vue";
import UserInfoShowAssembly from "@/components/UserInfoShowAssembly.vue";
import { ref, defineProps } from "vue";

const props = defineProps({
  user: {
    type: Object,
    required: true
  },
  type: {
    type: Object,
  },
  shape: {
    type: String,
    default: "square"
  },
  size: {
    type: Number,
  },
  avatarSize: {
    type: Number,
    default: 35
  },
  fontSize: {
    type: Number,
    default: 18
  }
}) ;

/* 图片选择放大查看是事件 */
const dialogVisibleImg = ref(false)
const disabled = ref(false)
const imageUrl = ref('')
const handlePictureCardPreviewImg = (url) => {
  imageUrl.value = url;
  /* 弹出对话框 */
  dialogVisibleImg.value = true;
}
</script>

<template>
  <div id="post-user-assembly">
    <div id="left">
      <el-avatar :shape="shape" @click="handlePictureCardPreviewImg(user.path + user.fileName)" :src="user.path + user.fileName" />
    </div>
    <div id="middle">
      <span :style="`vertical-align: middle; margin-right: 10px;font-size:${fontSize}px;`">
        <user-info-show-assembly :user="user" />
      </span>
      <badge-assembly :level="user.level" :size="size" :level-name="user.levelName"/>
    </div>
    <div id="right" style="color: #b3b3b3; font-size: 18px;text-align: right">
      <span v-if="type !== undefined">#{{type.typeName}}</span>
    </div>
  </div>
  <el-dialog :title="`${user.nickname}-头像预览`" v-model="dialogVisibleImg"
             width="700">
    <el-image w-full :src="imageUrl" style="height: 100%; width: 100%" alt="头像预览"/>
  </el-dialog>
</template>

<style scoped>
#post-user-assembly {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  #left {
    height: 100%;
    width: 8%;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  #middle {
    height: 100%;
    width: 50%;
    font-size: 20px;
  }

  #right {
    height: 100%;
    width: 40%;
  }
}
</style>