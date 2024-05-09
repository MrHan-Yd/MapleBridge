<script setup>
import { ref,nextTick, onMounted } from 'vue';
import china from '@/json/china.json';
import * as echarts from 'echarts';
/* 地图数据 */
import map from "@/json/china.json";
let dataList = ref([]);
let initMap;
const mapEcharts = () =>{
  initMap = echarts.init(document.querySelector('#mapDom'))
  echarts.registerMap('china', china);
  let options = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}<br/>{c} (人)'
    },
    visualMap: {
      min: 0,
      max: 10000,
      text: ['最高', '最低'],
      realtime: true,
      calculable: true,
      inRange: {
        color: ['lightskyblue', 'yellow', 'orangered']
      }
    },
    series: [
      {
        name: '中国',
        type: 'map',
        map: 'china',
        // roam: true, // 允许缩放和平移
        label: {
          show: false
        },
        data: dataList.value,
        zoom: 1.2,// 设置缩放级别为1.2，即放大20%
        emphasis: { // 鼠标悬浮在地图上，对应省份高亮显示
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
            areaColor: '#fff',
          }
        }
      }
    ],
    selectedMode:'single', //单选模式
    select: { // 指定特定省份时，高亮
      itemStyle: {
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)',
        areaColor: '#fff',
      }
    }

  }
  /* 渲染 */
  initMap.setOption(options)
}
onMounted(()=>{
  dataList.value = [
    {name:'北京市',value:290},
    {name:'天津市',value:320},
    {name:'河北省',value:240},
    {name:'山西省',value:520},
    {name:'内蒙古自治区',value:120},
    {name:'辽宁省',value:210},
    {name:'吉林省',value:620},
    {name:'黑龙江省',value:20},
    {name:'上海市',value:220},
    {name:'江苏省',value:820},
    {name:'浙江省',value:320},
    {name:'安徽省',value:520},
    {name:'福建省',value:120},
    {name:'江西省',value:620},
    {name:'山东省',value:920},
    {name:'河南省',value:220},
    {name:'湖北省',value:720},
    {name:'湖南省',value:210},
    {name:'广东省',value:1377},
    {name:'广西壮族自治区',value:20},
    {name:'海南省',value:1023},
    {name:'重庆市',value:20},
    {name:'四川省',value:240},
    {name:'贵州省',value:420},
    {name:'云南省',value:320},
    {name:'西藏自治区',value:20},
    {name:'陕西省',value:240},
    {name:'甘肃省',value:920},
    {name:'青海省',value:720},
    {name:'宁夏回族自治区',value:120},
    {name:'新疆维吾尔自治区',value:420},
    {name:'台湾省',value:230},
    {name:'香港特别行政区',value:420},
    {name:'澳门特别行政区',value:210},
    {name:'南海诸岛',value:0},
  ]
  nextTick(()=>{
    mapEcharts()
  }) ;

  /* 降序排序 */
  dataList.value.sort((a, b) => b.value - a.value);
});


/* 表格数据 */
const indexMethod = (index) => {
  return index + 1;
}

/* 选中列表地图高亮 */
function handleProvinceHover(provinceName) {
  initMap && initMap.dispatchAction({
    type: 'select',
    name: provinceName
  });
}
/* 离开列表时，取消高亮 */
function handleProvinceLeave(provinceName) {
  initMap && initMap.dispatchAction({
    type: 'unselect',
    name:provinceName
  });
}
</script>

<template>
  <div id="alumni-map">
    <div id="left">
      <el-text class="title">
        校友分布
      </el-text>
      <div id="mapDom" style="width: 100%; height:95%" />
    </div>
    <div id="right">
      <el-text class="title">
        分布排行
      </el-text>
      <el-table
          @cell-mouse-enter="(row) => handleProvinceHover(row.name)"
          @cell-mouse-leave="(row) => handleProvinceLeave(row.name)"
          :data="dataList"
          style="width: 100%; margin-top: 10px;"
      >
        <el-table-column type="index" :index="indexMethod" label="排名" width="60" />
        <el-table-column prop="name" label="地区" width="140" />
        <el-table-column prop="value" label="人数" sortable width="170" />
      </el-table>
    </div>
  </div>
</template>

<style scoped>
#alumni-map {
  height: 87.5vh;
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: rgba(215, 215, 215, 0.4);
  display: flex;

  #left {
    height: 100%;
    width: 70%;
  }

  #right {
    height: 100%;
    width: 30%;
    background-color: white;
    overflow-y: auto;

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