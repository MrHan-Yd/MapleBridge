<script setup>
import {onMounted, ref, watch} from 'vue';
import * as echarts from "echarts";

const prop = defineProps({
  title: {
    type: String,
    required: true
  },
  dataName: {
    type: String,
    required: true
  },
  maxValue: {
    type: Number,
    default: 100
  },
  unit : {
    type: String,
    required: true
  },
  value: {
    type: Number,
    required: true,
    default: 0
  }
})

/* x渲染容器 */
const echartsRef = ref(null);
const chartInstance = ref(null);
onMounted(() => {
  // 初始化echarts实例
  chartInstance.value = echarts.init(echartsRef.value);
});
function updateChart() {

  console.log(prop)

  if (chartInstance.value) {
    chartInstance.value.setOption({
      title: {
        text: prop.title,
        left: '1%',  // 往右移动一点
        top: '3%',  // 往下移动一点
      },
      tooltip: {
        formatter: '{a} <br/>{b} : {c}%',
      },
      series: [
        {
          name: prop.title,
          type: 'gauge',
          radius: '100%',
          center: ['50%', '55%'],  // 将仪表盘图表居中
          axisLine: {
            lineStyle: {
              width: 10,  // 仪表盘条的宽度
              color: [
                [0.0, '#ffe6e6'],   // 浅红色到0%
                [0.1, '#ffffff'],   // 白色到10%
                [0.2, '#ffe6e6'],   // 浅红色到20%
                [0.3, '#ffcccc'],   // 更深的浅红色到30%
                [0.4, '#ffb3b3'],   // 更深的红色到40%
                [0.5, '#ff9999'],   // 更深的红色到50%
                [0.6, '#ff8080'],   // 更深的红色到60%
                [0.7, '#ff6666'],   // 更深的红色到70%
                [0.8, '#ff4d4d'],   // 更深的红色到80%
                [0.9, '#ff3333'],   // 更深的红色到90%
                [1, '#ff0000']      // 红色到100%
              ],
            }
          },
          pointer: {
            width: 6,  // 指针宽度
            length: '70%',  // 指针长度
            shadowBlur: 5,  // 阴影模糊度
            shadowColor: 'rgba(0, 0, 0, 0.5)',  // 阴影颜色
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [
                { offset: 0, color: 'rgba(255, 0, 0, 0.8)' },
                { offset: 0.5, color: 'rgba(255, 215, 0, 0.8)' },
                { offset: 1, color: 'rgba(255, 255, 255, 0.8)' }
              ]),  // 指针颜色渐变
              shadowBlur: 5,  // 阴影模糊度
              shadowColor: 'rgba(0, 0, 0, 0.5)',  // 阴影颜色
              borderWidth: 1,  // 指针边框宽度
              borderColor: '#333',  // 指针边框颜色
            }
          },
          progress: {
            show: true,
            itemStyle: {
              shadowBlur: 7,  // 阴影模糊度
              color: 'rgba(255, 255, 255, 0.1)',  // 设置进度条颜色为半透明的白色
              borderColor: '#fff',  // 边框颜色
              borderWidth: 1.5,  // 边框宽度
            },
          },
          detail: {
            valueAnimation: true,
            formatter: `{value}${prop.unit}` ,
            offsetCenter: [0, '50%'],// 将文字向下偏移
            textStyle: {
              fontSize: 18 // 设置文字大小
            }
          },
          data: [
            {
              value: prop.value,
              name: prop.dataName,
            },
          ],
          max: prop.maxValue,  // 最大值
        }
      ]
    });
  }
}
//数据变化时更新图表
watch(prop, updateChart, { deep: true });

</script>

<template>
  <div ref="echartsRef" style="width: 100%; height: 100%;" />
</template>

<style scoped>

</style>