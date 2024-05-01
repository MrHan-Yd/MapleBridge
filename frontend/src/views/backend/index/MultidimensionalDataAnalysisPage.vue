<script setup>
import MultidimensionalDataAnalysisAssembly from "@/components/MultidimensionalDataAnalysisAssembly.vue";
import * as echarts from "echarts";
import {onMounted} from "vue";


onMounted(() => {
  /* 渲染用户增长图表 */
  const chartsDom = echarts.init(document.getElementById("chartsDom"));
  let option = {
    tooltip: {
      show: true,
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    title: {
      text: '新增用户统计 (近七年)',
    },
    grid: {
      left: '3%',
      right: '6%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      name: '年份',
      type: 'category',
      boundaryGap: false
    },
    yAxis: {
      name: '人数',
      type: 'value',
      boundaryGap: [0, '30%']
    },
    visualMap: {
      type: 'piecewise',
      show: false,
      dimension: 0,
      seriesIndex: 0,
      pieces: [
        {
          gt: 0,
          lt: 7,
          color: 'rgba(0, 0, 180, 0.4)'
        }
      ]
    },
    series: [
      {
        name: '新增用户',
        type: 'line',
        smooth: 0.5,
        symbol: 'none',
        lineStyle: {
          color: '#5470C6',
          width: 5
        },
        areaStyle: {},
        data: [
          ['2018', 100],
          ['2019', 200],
          ['2020', 560],
          ['2021', 750],
          ['2022', 580],
          ['2023', 250],
          ['2024', 300],
        ]
      }
    ]
  };
  /* 渲染 */
  option && chartsDom.setOption(option);
  /* 折线图结束 */

  /* 柱状图开始 */
  const topDom = echarts.init(document.getElementById("topDom"));
  let topOption = {
    title: {
      text: '今日赞评统计'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },

    legend: {
      show: false
    },
    grid: {
      top: '20%',
      left: '3%',
      right: '10%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: ['校园生活', '学术分享', '互帮互助']
    },
    series: [
      {
        name: '点赞',
        type: 'bar',
        data: [ 1001, 1500, 2050],
        itemStyle: {
          borderRadius: [50, 50, 50, 50] // 这里设置圆角的大小
        }
      },
      {
        name: '评论',
        type: 'bar',
        data: [588, 1059, 1950],
        itemStyle: {
          borderRadius: [50, 50, 50, 50] // 这里设置圆角的大小
        }

      }
    ]
  };
  /* 渲染 */
  topOption && topDom.setOption(topOption);
  /* 柱状图结束 */
});
</script>

<template>
  <div id="multidimensional-data-analysis-page">
    <div id="top">
      <div id="left">
        <el-text class="title">
          数据总览
        </el-text>
        <div id="content">
          <el-row :gutter="10" style="width: 100%;height: 100%;margin:0 auto;">
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="总用户数量"
                  value="56789"
                  icon="icon-yonghushuliang"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="日新增内容"
                  value="3456"
                  icon="icon-tiezi"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="日新增点赞"
                  value="77889"
                  icon="icon-dianzan"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="活跃用户"
                  value="789"
                  icon="icon-huoyueyonghu"
              />
            </el-col>
          </el-row>
        </div>
        <div id="chartsDom" style="height: calc(80% - 31px);width: 100%;" />
      </div>
      <div id="right">
        <div id="topDom" style="height: calc(40% - 10px);width: 100%;" />
        <div id="bottom" style=" height: 60%;width: 100%;">

        </div>
      </div>
    </div>
    <div id="bottom">

    </div>
  </div>
</template>

<style scoped>
#multidimensional-data-analysis-page {
  height: 87.5vh;
  margin-right: 7px;
  margin-bottom: 10px;

  #top {
    height: 60%;
    width: 100%;
    display: flex;

    #left {
      height: 100%;
      width: calc(60% - 10px);
      background-color: white;
      margin-right: 10px;

      #content {
        height: 20%;
        width: 100%;
      }

      #chartsDom {
        height: calc(80% - 31px);
        width: 100%;
      }
    }

    #right {
      height: 100%;
      width: 40%;

      #topDom {
        height: calc(40% - 10px);
        width: 100%;
        background-color: white;
        margin-bottom: 10px;
      }


      #bottom {
        height: 60%;
        width: 100%;
        background-color: white;
      }
    }
  }

  #bottom {
    height: 40%;
    width: 100%;
    background-color: #0bf1b7;
  }

  .title {
    display: flex;
    padding-top: 10px;
    padding-left: 10px;
    font-size: 16px;
    width: calc(100% - 10px);
    align-items: center;
  }
}
</style>