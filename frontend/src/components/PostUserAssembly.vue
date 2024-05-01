<script setup>
import { onMounted } from 'vue';
import BadgeAssembly from "@/components/BadgeAssembly.vue";

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


</script>

<template>
  <div id="post-user-assembly">
    <div id="left">
      <el-avatar :shape="shape" :src="user.path + user.fileName" />
    </div>
    <div id="middle">
      <span :style="`vertical-align: middle; margin-right: 10px;font-size:${fontSize}px;`">
        {{ user.nickname }}
      </span>
      <badge-assembly :level="user.level" :size="size" :level-name="user.levelName"/>
    </div>
    <div id="right" style="color: #b3b3b3; font-size: 18px;text-align: right">
      <span v-if="type !== undefined">#{{type.typeName}}</span>
    </div>
  </div>
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