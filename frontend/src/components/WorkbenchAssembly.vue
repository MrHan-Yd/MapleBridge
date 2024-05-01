<script setup>
import MyIcon from "@/components/MyIcon.vue";
import {formatNumber} from "@/util/FormatData" ;
import {CaretBottom, CaretTop} from "@element-plus/icons-vue";

const props = defineProps({
  icon: {
    type: String,
    default: "icon-qingzhu"
  },
  iconColor: {
    type: String,
    default: "#333"
  },
  title: {
    type: String,
    default: "标题"
  },
  value: {
    type: Number,
    default: 0
  },
  unit: {
    type: String,
    default: ""
  },
  type: {
    type: String,
    default: "num",
  }
});
</script>

<template>
  <div id="workbench-assembly">
    <div id="icon">
      <my-icon :name="icon" :style="`color: ${iconColor};font-size: 30px;transform: translateX(15%);`"/>
    </div>
    <div id="content">
      <div id="title">
        {{ title }}
      </div>
      <div id="NumericalValue">
        <span>
          {{formatNumber(value, type)}}
        </span>
        <span id="unit" v-if="type === 'num'">
          {{ unit }}
        </span>
        <span id="unit" style="font-size: 16px" v-if="type === 'rise' && type !== 'num'">
          <el-icon color="red"><CaretTop/></el-icon>
        </span>
        <span id="unit" style="font-size: 16px" v-if="type !== 'rise' && type !== 'num'">
          <el-icon color="green"><CaretBottom/></el-icon>
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
#workbench-assembly {
  display: flex;
  align-items: center;
  height: 100%;
  width: 100%;
  padding: 10px;

  #icon {
    border-radius: 50%;
    margin-left: 20px;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f4f4f4;

  }

  #content {
    width: calc(70% - 10px);
    height: 70%;
    padding-left: 10px;

    #title {
      height: 30%;
      width: 100%;
      font-size: 12px;
      color: #8c939d;
    }

    #NumericalValue {
      display: flex;
      align-items: center;
      height: 70%;
      width: 100%;

      span:first-child {
        font-size: 18px;
      }

      #unit {
        font-size: 12px;
      }
    }
  }
}
</style>