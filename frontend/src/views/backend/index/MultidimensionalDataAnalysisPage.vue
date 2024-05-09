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
          ['2018', 50],
          ['2019', 100],
          ['2020', 160],
          ['2021', 210],
          ['2022', 98],
          ['2023', 115],
          ['2024', 195],
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
        data: [200, 300, 500],
        itemStyle: {
          borderRadius: [50, 50, 50, 50] // 这里设置圆角的大小
        }
      },
      {
        name: '评论',
        type: 'bar',
        data: [222, 333, 555],
        itemStyle: {
          borderRadius: [50, 50, 50, 50] // 这里设置圆角的大小
        }

      }
    ]
  };
  /* 渲染 */
  topOption && topDom.setOption(topOption);
  /* 柱状图结束 */

  /* 饼图1开始 */
  const oneDom = echarts.init(document.getElementById("one"));

  let oneOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '2022年工作占比分析',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '55%'],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 10
        },
        label: {
          show: false,
          position: 'center',
          normal: {
            show: true,
            position: 'center', // 展示在中间位置
            fontSize: 25,
            formatter: () => {
              return `2022年`
            }
          },
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 378, name: '非对口'},
          {value: 544, name: '对口工作'}
        ]
      }
    ]
  }
  /* 渲染 */
  oneOption && oneDom.setOption(oneOption);
  /* 饼图1结束 */

  /* 饼图2开始 */
  const twoDom = echarts.init(document.getElementById("two"));

  let twoOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '2023年工作占比分析',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '55%'],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 10
        },
        label: {
          show: false,
          position: 'center',
          normal: {
            show: true,
            position: 'center', // 展示在中间位置
            fontSize: 25,
            formatter: () => {
              return `2023年`
            }
          },
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 454, name: '非对口'},
          {value: 353, name: '对口工作'}
        ]
      }
    ]
  }
  /* 渲染 */
  twoOption && twoDom.setOption(twoOption);
  /* 饼图2结束 */

  /* 饼图3开始 */
  const threeDom = echarts.init(document.getElementById("three"));

  let threeOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '2024年工作占比分析',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '55%'],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 10
        },
        label: {
          show: false,
          position: 'center',
          normal: {
            show: true,
            position: 'center', // 展示在中间位置
            fontSize: 25,
            formatter: () => {
              return `2024年`
            }
          },
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 565, name: '非对口'},
          {value: 175, name: '对口工作'}
        ]
      }
    ]
  }
  /* 渲染 */
  threeOption && threeDom.setOption(threeOption);
  /* 饼图3结束 */

  /* 对口专业统计(近期年)折线图开始*/
  const bottomDom = echarts.init(document.getElementById("bottomDom"));
  const data = [["2018", 70],
    ["2019", 636], ["2020", 565], ["2021", 767],
    ["2022", 544], ["2023", 353], ["2024", 175]];
  const data2 = [["2018", 70],
    ["2019", 175], ["2020", 222], ["2021", 353],
    ["2022", 378], ["2023", 454], ["2024", 565]];
  const dateList = data.map(function (item) {
    return item[0];
  });
  const valueList = data.map(function (item) {
    return item[1];
  });
  const dateList2 = data2.map(function (item) {
    return item[0];
  });
  const valueList2 = data2.map(function (item) {
    return item[1];
  });
  let bottomOption = {
    visualMap: [
      {
        show: false,
        type: 'continuous',
        seriesIndex: 0,
        min: 0,
        max: 400
      },
      {
        show: false,
        type: 'continuous',
        seriesIndex: 1,
        dimension: 0,
        min: 0,
        max: dateList.length - 1
      }
    ],
    title: [
      {text: '对口工作统计（近七年）'},
      {
        top: '10%',
        left: 'center',
        text: '对口'
      },
      {
        top: '55%',
        left: 'center',
        text: '非对口'
      }
    ],
    tooltip: {
      trigger: 'axis'
    },
    xAxis: [
      {
        data: dateList
      },
      {
        data: dateList2,
        gridIndex: 1
      }
    ],
    yAxis: [
      {
        show: false
      },
      {
        show: false,
        gridIndex: 1
      }
    ],
    grid: [
      {
        bottom: '60%'
      },
      {
        top: '80%'
      }
    ],
    series: [
      {
        type: 'line',
        showSymbol: false,
        data: valueList
      },
      {
        type: 'line',
        showSymbol: false,
        data: valueList2,
        xAxisIndex: 1,
        yAxisIndex: 1
      }
    ]
  }

  /* 渲染 */
  bottomOption && bottomDom.setOption(bottomOption);
  /* 对口专业统计(近期年)折线图结束*/
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
                  value="517"
                  icon="icon-yonghushuliang"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="日新增内容"
                  value="776"
                  icon="icon-tiezi"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="日新增点赞"
                  value="1227"
                  icon="icon-dianzan"
              />
            </el-col>
            <el-col :span="6">
              <multidimensional-data-analysis-assembly
                  title="活跃用户"
                  value="208"
                  icon="icon-huoyueyonghu"
              />
            </el-col>
          </el-row>
        </div>
        <div id="chartsDom" style="height: calc(80% - 31px);width: 100%;"/>
      </div>
      <div id="right">
        <div id="topDom" style="height: calc(40% - 10px);width: 100%;"/>

        <div id="bottomDom" style=" height: 60%;width: 100%;"/>
      </div>
    </div>
    <div id="bottom">
      <el-text class="title">
        工作占比分析（近三年）
      </el-text>
      <el-row :gutter="10" style="width: 100%;height: 80%;">
        <el-col :span="8">
          <div id="one" style="height: 100%;width: 100%;"/>
        </el-col>
        <el-col :span="8">
          <div id="two" style="height: 100%;width: 100%;"/>
        </el-col>
        <el-col :span="8">
          <div id="three" style="height: 100%;width: 100%;"/>
        </el-col>
      </el-row>
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


      #bottomDom {
        height: 60%;
        width: 100%;
        background-color: white;
      }
    }
  }

  #bottom {
    margin-top: 10px;
    height: 40%;
    width: 100%;
    background-color: white;
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