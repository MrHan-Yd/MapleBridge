<template>
  <span :class="['iconfont', `${name}`]"></span>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';

defineProps({
  name: {
    type: String ,
    required: true,
  }
});
</script>

<style scoped>
span {
  /* 和饿了么统一 */
  color: #303133 ;
  vertical-align: middle;
  text-align: center;
  //font-size: 18px ;
}
</style>