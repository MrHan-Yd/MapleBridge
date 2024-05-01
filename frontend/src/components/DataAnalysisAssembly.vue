<script setup>
import * as echarts from 'echarts';
import { ref, onMounted } from "vue";
import {formatNumber} from "@/util/FormatData" ;
import {CaretBottom, CaretTop, Minus} from "@element-plus/icons-vue";

/* 传参 */
const props = defineProps({
  title: {
    type: String,
    default: '标题'
  },
  value: {
    type: Number,
    default: 0
  },
  riseAndFall: {
    type: String,
    default: ""
  },
  comparedToYesterday: {
    type: Number,
    default: 0
  },
  type: {
    type: String,
    default: "line"
  },
  data: {
    type: Array,
    default: () => []
  }
});

/* x渲染容器 */
const echartsRef = ref(null);
onMounted(() => {

  /* 图表开始 */
  const myChatDom = echarts.init(echartsRef.value);
  let echartsOption = "";
  if (props.type === "pie") {
    /* 饼图开始 */
    echartsOption = {
      tooltip: {
        trigger: 'none',
      },
      legend: {
        show: false,
      },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          padAngle: 5,
          itemStyle: {
            borderRadius: 10
          },
          label: {
            show: false,
          },
          labelLine: {
            show: false
          },
          data: props.data
        }
      ]
    };
    /* 饼图结束 */
  } else if (props.type === "bar") {
    /* 柱状图开始 */
    echartsOption = {
      title: {
        show: false
      },
      tooltip: {
        trigger: 'axis',
        show: false ,
      },
      legend: {
        show: false,
      },
      grid: {
        top: '-20%',
        left: '-30%',
        right: '-20%',
        bottom: '-5%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          show: false,
        }
      ],
      yAxis: [
        {
          type: 'value',
          show: false,
        }
      ],
      series: [
        {
          name: '校园生活',
          type: 'bar',
          stack: 'Ad',
          data: props.data[0],
        },
        {
          name: '学术分享',
          type: 'bar',
          stack: 'Ad',
          data: props.data[1],
        },
        {
          name: '互帮互助',
          type: 'bar',
          stack: 'Ad',
          data: props.data[2],
        },
      ]
    };
    /* 柱状图结束 */
  } else {
    /* 折线图开始 */
    echartsOption = {
      title: {
        show: false
      },
      xAxis: {
        show: false,
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        show: false,
        type: 'value'
      },
      grid: {
        top: '20%',
        left: '-10%',
        right: '4%',
        bottom: '-10%',
        containLabel: true
      },
      series: [
        {
          data: props.data,
          type: 'line',
          smooth: true,
          showSymbol: false,
        }
      ]
    };
    /* 折线图结束 */
  }

  /* 渲染图标 */
  echartsOption && myChatDom.setOption(echartsOption);

}) ;

/* 判断用什么颜色做背景 */
function getColor() {
  if (props.type === "pie") {
    return "rgb(244 244 255)";
  } else if(props.type === "bar") {
    return "rgb(240 254 241)" ;
  } else {
    return "rgb(239 247 254)" ;
  }
}
</script>

<template>
  <div id="data-analysis-assembly" :style="`background-color:${getColor()}`">
    <div id="top">
      <span>{{title}}</span>
    </div>
    <div id="bottom">
      <div id="left">
        <p>{{formatNumber(value, "num")}}</p>
        <p>
          较昨日：
          <span v-if="riseAndFall === 'rise'" style="color: red">{{formatNumber(comparedToYesterday, "num")}}
            <el-icon><CaretTop /></el-icon>
          </span>
          <span v-else-if="riseAndFall === 'fall'" style="color: green">{{formatNumber(comparedToYesterday, "num")}}
            <el-icon><CaretBottom /></el-icon>
          </span>
          <span v-else style="color: #999999">{{formatNumber(comparedToYesterday, "num")}}
            <el-icon><Minus /></el-icon>
          </span>
        </p>
      </div>
      <div ref="echartsRef" id="right" style="height: 100% ;width: 50%;" />
    </div>
  </div>
</template>

<style scoped>
#data-analysis-assembly {
  border-radius: 5px;
  background-color: pink;
  padding: 5px;
  //padding: 10px;
  //border: 1px solid black;

  #top {
    padding-left: 10px;
  }

  #bottom {
    height: calc(100% - 30px) ;
    width: calc(100% - 10px) ;
    display: flex;
    //border: 1px solid purple;
    padding: 0;

    #left {
      padding-left: 10px;
      height: 100% ;
      width: 50%;

      p:first-child {
        font-size: 20px;
        margin-top:25px;
        margin-bottom: 10px;
      }

      p:last-child {
        margin-top:0;
        font-size: 14px;
      }
    }

    #right {
      height: 100% ;
      width: 50%;
      //border: 1px solid rgba(222, 255, 0, 0.66);
    }
  }
}
</style>