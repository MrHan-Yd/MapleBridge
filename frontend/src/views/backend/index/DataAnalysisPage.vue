<script setup>

import DataAnalysisAssembly from "@/components/DataAnalysisAssembly.vue";
import * as echarts from 'echarts';
import { onMounted } from 'vue';
import {formatNumber} from "@/util/FormatData" ;

/* 访问总人数 */
const totalNumberOfVisitors = [100, 152, 78, 350, 300, 450, 250];
/* 内容发布量 */
const totalContentRelease = [
  [120, 132, 101, 134, 90, 230, 210],
  [220, 182, 191, 234, 290, 330, 310],
  [150, 232, 201, 154, 190, 330, 410]
];
/* 评论总量 */
const totalComment = [552, 343, 434, 251, 456, 291, 140];
/* 点赞总量 */
const totalLike = [
  { value: 1048, name: '校园生活' },
  { value: 735, name: '学术分享' },
  { value: 580, name: '互帮互助' },
];

onMounted(() => {
  const chart = echarts.init(document.getElementById('chart'));
  /* 折线图开始 */
  let option = {
    title: {
      text: '内容发布时段分析'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['0:00', '2:00', '4:00', '6:00', '8:00', '10:00', '12:00', '14:00', '16:00', "18:00", "20:00", "22:00"]
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '校园生活',
        type: 'line',
        stack: 'Total',
        data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90]
      },
      {
        name: '学术分享',
        type: 'line',
        stack: 'Total',
        data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290, 330]
      },
      {
        name: '互帮互助',
        type: 'line',
        stack: 'Total',
        data: [150, 232, 201, 154, 190, 330, 410, 150, 232, 201, 154, 190, 330]
      }
    ]
  };
  option && chart.setOption(option);
  /* 折线图结束 */
});

/* 表格数据 */
const indexMethod = (index) => {
  return index + 1;
}
const tableData = [
  {
    author: '我滴乖',
    content: formatNumber(3331, "num"),
    like: formatNumber(312312, "num"),
  },
  {
    author: '姬霓太美',
    content: formatNumber(2331, "num"),
    like: formatNumber(23321, "num"),
  },
  {
    author: '让我再写一篇',
    content: formatNumber(1001, "num"),
    like: formatNumber(12331,"num"),
  },
  {
    author: '用魔法打败魔法',
    content: formatNumber(558,"num"),
    like: formatNumber(1997, "num"),
  },
  {
    author: '我爱学习',
    content: formatNumber(558,"num"),
    like: formatNumber(1997, "num"),
  },
  {
    author: '啊对对对对',
    content: formatNumber(558,"num"),
    like: formatNumber(1997, "num"),
  },
  {
    author: '是嘛是嘛是嘛',
    content: formatNumber(558,"num"),
    like: formatNumber(1997, "num"),
  },
]
</script>

<template>
  <div id="data-analysis">
    <div id="top">
      <el-row :gutter="20">
        <el-col :span="6">
          <data-analysis-assembly
              title="访问总量"
              value="123123"
              riseAndFall="rise"
              comparedToYesterday="208"
              :data="totalNumberOfVisitors"
          />
        </el-col>
        <el-col :span="6">
          <data-analysis-assembly
              title="内容总发布量"
              value="1235567"
              riseAndFall="rise"
              comparedToYesterday="311"
              type="bar"
              :data="totalContentRelease"
          />
        </el-col>
        <el-col :span="6">
          <data-analysis-assembly
              title="评论总量"
              value="3311"
              riseAndFall="fall"
              comparedToYesterday="151"
              :data="totalComment"
          />
        </el-col>
        <el-col :span="6">
          <data-analysis-assembly
              title="点赞总量"
              value="123455"
              riseAndFall="flat"
              type="pie"
              :data="totalLike"
          />
        </el-col>
      </el-row>
    </div>
    <div id="bottom">
      <el-row :gutter="20" style="height: 100%; width: 100.7%">
        <el-col :span="17">
          <div id="chart" />
        </el-col>
        <el-col :span="7" style="background-color: white;">
          <el-text class="title">
            热门作者榜
          </el-text>
          <el-table
              :data="tableData"
              style="width: 100%; margin-top: 10px;"
          >
            <el-table-column type="index" :index="indexMethod" label="排名" width="60" />
            <el-table-column prop="author" label="作者" width="100" />
            <el-table-column prop="content" label="内容量" sortable width="100" />
            <el-table-column prop="like" label="点赞量" sortable width="100" />
          </el-table>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
#data-analysis{
  height: 87.5vh;
  margin-right: 7px;
  margin-bottom: 10px;

  #top{
    padding: 10px;
    height: 20% ;
    width: calc(100% - 20px) ;
    background-color: white;
    margin-bottom: 10px;
  }

  #bottom {
    height: calc(80% - 30px) ;
    width: 100% ;

    #chart{
      padding-top: 5px;
      padding-left: 5px;
      height: calc(100% - 5px);
      width: calc(100% - 5px);
      background-color: white;
    }
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
:deep(.el-scrollbar__bar.is-horizontal){
  height: 0 !important;
}

/deep/.el-table th.is-leaf {
  background-color: #f3f3f3;
}

</style>