<script setup>

import MyIcon from "@/components/MyIcon.vue";
import {getUserAccount} from "@/net/NetWork";
import WorkbenchAssembly from "@/components/WorkbenchAssembly.vue";
import {onMounted} from "vue";
import * as echarts from 'echarts';
import Ranking from "@/components/Ranking.vue";


onMounted(() => {
  /* 折线图开始 */
  const chatDom = document.getElementById('content-data');
  const myChatDom = echarts.init(chatDom);
  let option ;

  option = {
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      name: '日期',
      type: 'category',
      data: ['2024-04-01', '2024-04-02', '2024-04-03', '2024-04-04', '2024-04-05', '2024-04-06', '2024-04-07'],
    },
    yAxis: {
      name: '篇',
      type: 'value',
    },
    grid: {
      top: '10%',
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    series: [
      {
        name: '每日发布统计',
        data: [50, 130, 298, 218, 330, 147, 500],
        type: 'line',
      }
    ]
  };
  option && myChatDom.setOption(option);
  /* 折线图结束 */

  /* 饼图开始 */
  const pieDom = document.getElementById('pie-data');
  const myPieDom = echarts.init(pieDom);
  let pieOption ;

  pieOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: 'bottom'
    },
    series: [
      {
        name: '类型分布',
        type: 'pie',
        radius: ['35%', '50%'],
        center: ['50%', '35%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 8
        },
        data: [
          { value: 50, name: '校园生活' },
          { value: 100, name: '学术分享' },
          { value: 30, name: '互帮互助' },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  pieOption && myPieDom.setOption(pieOption);
  /* 饼图结束 */

  /* 柱状图开始 */
  const barDom = document.getElementById('bar-data');
  const myBarDom = echarts.init(barDom);
  let barOption ;

  barOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: ['04-01', '04-02', '04-03', '04-04', '04-05', '04-06', '04-07']
    },
    yAxis: {
      type: 'value'
    },
    grid: {
      top: '10%',
      left: '3%',
      right: '4%',
      bottom: '5%',
      containLabel: true
    },
    series: [
      {
        name: '反馈统计',
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'bar',
        // showBackground: true,
        backgroundStyle: {
          color: 'rgba(180, 180, 180, 0.2)'
        }
      }
    ]
  };
  barOption && myBarDom.setOption(barOption);
  /* 柱状图结束 */

  /* 等级统计开始 */
  const levelDom = document.getElementById('pie-level-data');
  const myLevelDom = echarts.init(levelDom);
  let levelOption ;

  levelOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '等级统计',
        type: 'pie',
        radius: ['40%', '130%'],
        center: ['50%', '90%'],
        startAngle: 180,
        endAngle: 360,
        data: [
          { value: 1048, name: '新生' },
          { value: 735, name: '学员' },
          { value: 580, name: '活跃学员' },
          { value: 484, name: '创作者' },
          { value: 300, name: '互动大师' },
          { value: 150, name: '领导者' },
          { value:50, name: '行业专家' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  /* 等级统计结束 */
  levelOption && myLevelDom.setOption(levelOption);
}) ;

const data = [{name:"测试",value:1000},{name:"测试2",value:2000},{name:"测试3",value:3000}];
</script>

<template>
  <div id="workbench">
    <div id="top">
      <div id="left">
        <div id="title">
          <el-text class="title">
            <my-icon name="icon-qingzhu" style="color: #ff9100;font-size: 20px;margin-right: 0"/>
            欢迎回来，{{ getUserAccount() }}!
          </el-text>
        </div>
        <div id="content">
          <div id="top">
            <el-row style="width: 100%; height:100%;margin: 0;" :gutter="30">
              <el-col class="el-col-border" :span="6">
                <workbench-assembly
                    icon="icon-tiezi"
                    icon-color="#7699f8"
                    title="线上总内容"
                    value="1234567"
                    unit="篇"
                />
              </el-col>
              <el-col class="el-col-border" :span="6">
                <workbench-assembly
                    icon="icon-dianzan"
                    icon-color="#7699f8"
                    title="线上总点赞"
                    value="123456"
                    unit="个"
                />
              </el-col>
              <el-col class="el-col-border" :span="6">
                <workbench-assembly
                    icon="icon-pinglun1"
                    icon-color="#7699f8"
                    title="日新增评论"
                    value="12345"
                    unit="条"
                />
              </el-col>
              <el-col class="el-col-border" :span="6">
                <workbench-assembly
                    icon="icon-jinrixinzeng"
                    icon-color="#7699f8"
                    title="较昨日新增评论"
                    value="2.8"
                    type="rise"
                />
              </el-col>
            </el-row>
          </div>
          <div id="bottom">
            <div id="bottom-title">
              <el-text class="title">
                每日文章发布量<em style="font-size: 12px;">（近七日）</em>
              </el-text>
            </div>
            <div id="content-data" ></div>
          </div>
        </div>
      </div>
      <div id="right">
        <el-text class="title">
          热门搜索
        </el-text>
        <el-divider/>
        <div>
          <ranking :data="data" />
        </div>
      </div>
    </div>

    <div id="bottom">
      <div>
        <el-text class="title">
          等级统计
        </el-text>
        <div id="pie-level-data" style="height: 100%; width: 100%;" />
      </div>
      <div>
        <el-text class="title">
          反馈统计
        </el-text>
        <div id="bar-data" style="height: 100%; width: 100%;" />
      </div>
      <div>
        <el-text class="title">
          类型分布
        </el-text>
        <div id="pie-data" style="height: 100%; width: 100%;" />
      </div>
    </div>
  </div>
</template>

<style scoped>
#workbench {
  height: 87.5vh;
  margin-right: 7px;
  margin-bottom: 10px;

  & >>> #top {
    height: calc(70% - 10px);
    width: 100%;
    display: flex;

    #left {
      height: 100%;
      width: calc(80% - 10px);
      margin-right: 10px;
      background-color: white;

      #title {
        height: 12%;
        width: 100%;
        border-bottom: 1px solid #e6e6e6;
      }

      #content {
        height: 88%;
        width: 100%;

        #top {
          height: 20%;
          width: 100%;
          border-bottom: 1px solid #e6e6e6;

          .el-col-border {
            border-right: 1px solid #e6e6e6;
            display: flex;
            align-items: center;
            justify-content: center;

            &:last-child {
              border-right: none;
            }
          }
        }

        #bottom {
          margin: 0;
          height: 80%;
          width: 100%;
          display: block;

          #bottom-title {
            height: 15% ;
            width: 100%;
            margin: 0;
          }

          #content-data {
            height: calc(100% - 15%);
            width: 100%;
          }
        }
      }
    }

    #right {
      height: 100.3%;
      width: 20%;
      background-color: white;
    }
  }

  & >>> #bottom {
    height: 30%;
    width: 100%;
    margin-top: 10px;
    display: flex;

    div:nth-child(1) {
      height: calc(100% - 31px);
      width: 30%;
      background-color: white;
      margin-right: 10px;
    }

    div:nth-child(2) {
      height: calc(100% - 31px);
      width: calc(40% - 10px);
      background-color: white;
      margin-right: 10px;
    }

    div:nth-child(3) {
      height: calc(100% - 31px);
      width: calc(30% - 10px);
      background-color: white;

      #pie-data {
      }
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
</style>