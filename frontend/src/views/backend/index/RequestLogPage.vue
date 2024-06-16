<script lang="ts" setup>
import { ref, reactive} from "vue";
import { get } from '@/net/http';
import { formatDate} from "@/util/FormatData";
import {ElError, ElWarning} from "@/util/MessageUtil";
import Page from "@/domain/Page";
import LoginLog from "@/domain/LoginLog";
import Response from "@/domain/Response";

/* 分页参数 */
const pageData: Page = reactive<Page>(new Page()) ;

/* 分页 */
const page = ref<number>(1);
const pageSize = ref<number>(10);
const small = ref<boolean>(false);
const disabled = ref<boolean>(false);
const background = ref<boolean>(false);

/* 分页切换每页数据显示几条 */
const handleSizeChange = (pageSize: number) => {
  getData(pageData.current, pageSize);
}

/* 换页 */
const handleCurrentChange = (pageNum: number) => {
  getData(pageNum, pageData.size);
}

/* 判断表格是否有数据 */
function isTableDataEmpty() {
  return tableData.value.length > 0 && !tableData.value.every((obj: LoginLog) => Object.values(obj).every(value => value === ''));
}

/* 表格数据 */
const tableData: RequestLog[] = ref<RequestLog[]>([{
  url: '',
  method: '',
  ip: '',
  position: '',
  parameter: '',
  responseValue: '',
  timeConsuming: '',
  requestTime: '',
  country: '',
  province: '',
  region: '',
  network: '',
  deviceType: '',
  osName: '',
  browserType: '',
  browserVersion: ''
}]);
/* 获取数据 */
const getData = async (num?: number, size?: number) => {
  // 使用空值合并运算符 ?? 设置默认值
  page.value = num ?? page.value;
  pageSize.value = size ?? pageSize.value;

  /* 页面加载后请求后台获取数据 */
  try {
    const response: Promise<Response | string> = await new Promise((resolve, reject) => {
      get("api/backend/request-log?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs: any) => {
        if (rs.code === 200) {
          resolve(rs);
        } else {
          reject(rs);
        }
      }, (message: string, code: number) => {
        /* 状态码400时，如果只是本页没有数据，但是有上一页(有数据)的同时当前页-1需要大雨等于1时，表示当前页无数据，需要查询上一页，会重新请求上一页 */
        if (code === 400 && pageData.pages - 1 >= 1) {
          getData(page.value - 1, pageSize.value);
        }

        /* 请求状态码400时，标识没有数据，并且当前页-1小于1表示已删除最后一条数据 */
        if (code === 400 && (pageData.pages === undefined || pageData.pages - 1 < 1)) {
          tableData.value = "" ;
          ElWarning(message) ;
          reject(message);
        }

      });
    });

    /* 分页数据大于0条时赋值，否则输出后端返回提示 */
    if (response.data.total > 0) {
      /* 数据 */
      tableData.value = response.data.records ;
      /* 分页数据 */
      pageData.total = response.data.total;
      pageData.size = response.data.size;
      pageData.current = response.data.current;
      pageData.pages = response.data.pages;
    } else {
      ElWarning(response);
    }

  } catch (error) {
    if (error.data.code === 403) {
      ElWarning(error.data.message) ;
    } else {
      ElError("获取数据时异常" + error);
    }
  }
}

getData() ;
</script>

<template>
  <div id="request-log">
    <el-text class="title">
      请求日志
    </el-text>
    <div id="tables" v-if="isTableDataEmpty()">
      <el-table :data="tableData" :height="'73.5vh'"
                :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
        <el-table-column prop="url" label="请求地址" width="300"/>
        <el-table-column prop="method" label="请求方法" width="150"/>
        <el-table-column prop="ip" label="IP地址" width="200" />
        <el-table-column prop="position" label="程序位置" width="500" />
        <el-table-column prop="timeConsuming" label="耗时" width="100" />
        <el-table-column prop="requestTime" label="请求时间" :formatter="formatDate" width="220"/>
        <el-table-column prop="country" label="国家" width="200"/>
        <el-table-column prop="province" label="省份" width="200"/>
        <el-table-column prop="region" label="地区" width="200"/>
        <el-table-column prop="network" label="网络类型" width="200"/>
        <el-table-column prop="deviceType" label="登录设备" width="250"/>
        <el-table-column prop="osName" label="登录系统" width="200"/>
        <el-table-column prop="browserType" label="浏览器类型" width="200"/>
        <el-table-column prop="browserVersion" label="浏览器版本" width="200"/>
        <el-table-column prop="parameter" label="请求参数" width="1000" />
        <el-table-column prop="responseValue" label="响应值" width="1500" />
      </el-table>
      <div class="pagination">
        <el-pagination
            v-model:current-page="pageData.current"
            v-model:page-size="pageData.size"
            :page-count="pageData.pages"
            :page-sizes="[10, 20, 30, 40]"
            :small="small"
            :disabled="disabled"
            :background="background"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageData.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <div style="height:62vh;" v-else>
      <el-empty>
        <el-button type="primary" @click="getData(1, 10)">重试</el-button>
      </el-empty>
    </div>
  </div>
</template>

<style scoped>
#request-log {
  height: 87.5vh;
  margin-right: 7px;
  margin-bottom: 10px;
  padding-left: 10px;
  padding-right: 10px;
  background-color: white;

  & > .title {
    display: block;
    padding-top: 10px;
    font-size: 18px;
  }

  & >>> #tables {
    margin-top: 20px;
  }

  & >>> .pagination {
    margin-top: 12px;
    display: flex;
    justify-content: right;
    align-items: center;
  }
}

</style>