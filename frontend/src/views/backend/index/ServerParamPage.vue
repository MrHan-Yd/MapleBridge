<script setup>
import { onMounted, ref } from 'vue';
import DashboardAssembly from "@/components/DashboardAssembly.vue";
import { get } from "@/net/NetWork" ;
import {ElWarning} from "@/util/MessageUtil"; ;

const data = ref({
  systemName: "",
  systemManufacturer: "",
  systemVersion: "",
  systemType: "",
  systemInternalVersionNumber: "",
  usedMemory: 0.0,
  totalMemory: 0.0,
  cpuModel: "",
  cpuPhysicalProcessorNums: 0,
  cpuLogicProcessorNums: 0,
  cpuLoad: 0.0,
  systemTotalDiskSpace: 0.0,
  systemAvailableDiskSpace: 0.0,
  projectTotalDiskSpace: 0.0,
  projectAvailableDiskSpace: 0.0
});

/* 请求服务器获取数据 */
function getData() {
  get('api/backend/server-param', (rs) => {
    if (rs.code === 200) {
      data.value = rs.data ;
    } else {
      ElWarning("数据获取失败") ;
    }
  }) ;
}
onMounted(async () => {
  await getData();
}) ;

</script>

<template>
  <div id="server-param">
    <div id="top">
      <el-descriptions title="服务器监控(后台须等待半秒获取CPU使用情况)" :column="3" border>
        <el-descriptions-item
            label="系统名称"
            label-align="right"
            align="center"
            label-class-name="my-label"
            class-name="my-content"
            width="150px"
        >
          <el-tag size="small">{{data.systemName}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="系统制造商" label-align="right" align="center">
          <el-tag size="small">{{data.systemManufacturer}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="系统版本" label-align="right" align="center">
          <el-tag size="small">{{data.systemVersion}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="系统类型" label-align="right" align="center">
          <el-tag size="small">{{data.systemType}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="系统内部版本号" label-align="right" align="center">
          <el-tag size="small">{{data.systemInternalVersionNumber}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理器核心数" label-align="right" align="center">
          <el-tag size="small">{{data.cpuPhysicalProcessorNums}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理器线程数" label-align="right" align="center">
          <el-tag size="small">{{data.cpuLogicProcessorNums}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理器型号" label-align="right" align="center">
          <el-tag size="small">{{data.cpuModel}}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <div id="bottom">
      <div id="top">
        <div class="left" id="top-left-data">
          <DashboardAssembly :value="data.cpuLoad" title="处理器使用情况" data-name="CPU使用情况" unit="%"/>
        </div>
        <div class="right">
          <div id="top-right-data">
            <DashboardAssembly :value="data.usedMemory" :max-value="data.totalMemory" title="内存使用情况" data-name="RAM使用情况" unit="GB"/>
          </div>
        </div>
      </div>
      <div id="bottom">
        <div class="left" id="bottom-left-data">
          <DashboardAssembly :value="data.systemTotalDiskSpace - data.systemAvailableDiskSpace" :max-value="data.systemTotalDiskSpace" title="系统盘使用情况" data-name="System使用情况" unit="GB"/>
        </div>
        <div class="left" id="bottom-right-data">
          <DashboardAssembly :value="data.projectTotalDiskSpace - data.projectAvailableDiskSpace" :max-value="data.projectTotalDiskSpace" title="项目盘使用情况" data-name="Project使用情况" unit="GB"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#server-param {
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#server-param >>> #top {
  padding-top: 10px;
  padding-left: 10px;
  padding-right: 10px;
}

#server-param >>> #bottom {
  height: 65vh;
  width: 100%;

  & >#top {
    padding: 0;
    height: 50%;
    width: 100%;
    display: flex;

    .left {
      height: 100% ;
      width: 50% ;

      #top-left-data {
        height: 100%;
        width: 100%;
      }
    }

    .right {
      height: 100% ;
      width: 50% ;

      #top-right-data {
        height: 100%;
        width: 100%;
      }
    }
  }

  & > #bottom {
    padding: 0;
    height: 50%;
    width: 100%;
    display: flex;

    .left {
      height: 100% ;
      width: 50% ;

      #top-left-data {
        height: 100%;
        width: 100%;
      }
    }

    .right {
      height: 100% ;
      width: 50% ;

      #top-right-data {
        height: 100%;
        width: 100%;
      }
    }
  }
}

#server-param >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}
</style>