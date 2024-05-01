<script setup>

import MyIcon from "@/components/MyIcon.vue";
import {formatNumber} from "@/util/FormatData" ;

const props = defineProps({
  title: {
    type: String,
    default: "标题"
  },
  value: {
    type: Number,
    default: 0
  },
  icon: {
    type: String,
    default: "icon-yonghushuliang"
  }
})
</script>

<template>
  <div id="content">
    <div id="title">
      <span>{{title}}</span>
    </div>
    <div id="content" style="display: flex;">
      <my-icon :name="props.icon" style="background-color: #dbf0f4;color:#7699f8;font-size: 26px;border-radius: 10px; " />
      {{formatNumber(value,"num")}}
    </div>
  </div>
</template>

<style scoped>
#content {
  padding-top: 5px;
  padding-left: 5px;

  #title {
    span {
      padding-left: 7px;
      font-size: 14px;
    }
  }

  #content {
    font-size: 20px;
  }
}
</style>