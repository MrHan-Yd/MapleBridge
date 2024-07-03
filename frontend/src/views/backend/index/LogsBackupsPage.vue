<script setup lang="ts">
import {ref, reactive, computed} from 'vue'
import {delete_, get, delete_batch, get_batch} from "@/net/http";
import {ElError, ElSuccess, ElWarning} from "@/util/MessageUtil" ;
import Page from "@/domain/Page";
import {Logs} from "@/domain/Logs";
import {formatDate} from "@/util/FormatData";
import {Response} from "@/domain/Response";
import {Delete, Download} from "@element-plus/icons-vue";
import ExcelUtil from "@/util/ts/ExcelUtil";

const tableData = ref<Logs[]>([]);

/* 判断表格是否有数据 */
function isTableDataEmpty() {
  return tableData.value.length > 0 && !tableData.value.every((obj: Logs) => Object.values(obj).every(value => value === ''));
}

/* 分页参数 */
const pageData: Page = reactive<Page>(new Page());

/* 分页 */
const page = ref<number>(1);
const pageSize = ref<number>(10);
const small = ref<boolean>(false);
const disabled = ref<boolean>(false);
const background = ref<boolean>(false);

/* 获取数据 */
const getData = async (num?: number, size?: number) => {
  if (num !== undefined) {
    page.value = num;
  }
  if (size !== undefined) {
    pageSize.value = size;
  }
  /* 页面加载后请求后台获取数据 */
  try {
    const response: any = await new Promise<Response<Logs> | any>((resolve, reject) => {
      get("api/backend/backup-logs?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
        if (rs.code === 200) {
          resolve(rs);
        } else {
          reject(rs);
        }
      }, (message, code) => {
        /* 状态码400时，如果只是本页没有数据，但是有上一页(有数据)的同时当前页-1需要大雨等于1时，表示当前页无数据，需要查询上一页，会重新请求上一页 */
        if (code === 400 && pageData.pages - 1 >= 1) {
          getData(page.value - 1, pageSize.value);
        }

        /* 请求状态码400时，标识没有数据，并且当前页-1小于1表示已删除最后一条数据 */
        if (code === 400 && pageData.pages - 1 < 1) {
          tableData.value = [];
          ElWarning(message);
        }
      });
    });

    /* 分页数据大于0条时赋值，否则输出后端返回提示 */
    if (response.data.total > 0) {
      /* 数据 */
      tableData.value = response.data.records;

      /* 分页数据 */
      pageData.total = response.data.total;
      pageData.size = response.data.size;
      pageData.current = response.data.current;
      pageData.pages = response.data.pages;
    } else {
      ElWarning(response.message);
    }

  } catch (error: any) {
    if (error.data.code === 403) {
      ElWarning(error.data.message);
    } else {
      ElError("获取数据时异常" + error);
    }
  }
}
/* 加载页面数据 */
getData();

const deleteRow = (index: number) => {
  tableData.value.splice(index, 1)
}

/* 删除 */
const popoverVisible = ref<Record<string, boolean>>({}); // 存储弹窗显示状态的对象

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(tableName: string) {
  return popoverVisible.value[tableName];
}

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(tableName: string) {
  // 关闭 popover
  popoverVisible.value[tableName] = false;
}

/* 确认删除 */
function deleteRoleData(tableName: string) {
  if (!(tableName === "")) {
    dropTableName.value = tableName;
    dropConfirmVisible.value = true;
  }
  // 关闭 popover
  popoverVisible.value[tableName] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(tableName: string) {
  popoverVisible.value[tableName] = true;
}

/* 分页切换每页数据显示几条 */
const handleSizeChange = (pageSize: number) => {
  getData(pageData.current, pageSize);
}

/* 换页 */
const handleCurrentChange = (pageNum: number) => {
  getData(pageNum, pageData.size);
}

/* 导出 */
const exportClick = (row: Logs) => {
  const tableName = row.tableName;
  get(`api/backend/backup-logs/${tableName}/export`,
      (rs: Response<any>) => {
        if (rs.code === 200) {
          const data: any = rs.data;
          const header: string[] = Object.keys(data[0]);
          const eu: ExcelUtil = new ExcelUtil();
          eu.sheetName = tableName;
          eu.exportToExcel(header, data);
          eu.downloadExcel(tableName);
        }
      },
      (message: string) => {
        console.log(message)
      });
}

/* 删除确认可见 */
const dropConfirmVisible = ref<boolean>(false);
/* 确认删除表名 */
const dropTableName = ref<string>("");

/* 删除确认弹出框取消 */
const dropCancel = () => {
  dropConfirmVisible.value = false;
  dropTableName.value = "";
}

/* 删除确认弹出框确认 */
const dropConfirm = () => {
  /* 请求后台删除数据 */
  delete_(
      "api/backend/backup-logs/" + dropTableName.value + "/drop",
      async (rs) => {
        if (rs.code === 200) {
          ElSuccess(rs.message);
          /* 更新数据 */
          await getData(page.value, pageSize.value);
        }
      },
      (message) => {
        ElError(message);
      }
  );
  dropTableName.value = "";
  dropConfirmVisible.value = false;
}
const tableNameArr = ref<string[]>([]);
const batchRef = ref<any>();
/* 选择 */
const batchChange = (selection: Logs[]) => {
  tableNameArr.value = selection.map(s => s.tableName);
}

/* 计算属性：按钮是否可点击 */
const isClickable = computed(() => {
  /* 选中两条以上 */
  return !(tableNameArr.value.length > 1);
});

/* 批量删除 */
const batchDeletion = () => {
  /* 请求后台删除 */
  delete_batch(
      "api/backend/backup-logs/drop",
      tableNameArr.value,
      async (rs) => {
        if (rs.code === 200) {
          ElSuccess(rs.message);

          /* 清空表名数组 */
          tableNameArr.value = [];
          /* 取消选择 */
          batchRef.value!.clearSelection();

          /* 更新数据 */
          await getData(page.value, pageSize.value);
        }
      },
      (message) => {
        ElError(message);
      }
  );
}

/* 批量导出 */
const batchExport = () => {
  get_batch(
      "api/backend/backup-logs/export",
      tableNameArr.value,
      (rs: Response<any>) => {
        if (rs.code === 200) {
          const data: any = rs.data;
          const eu: ExcelUtil = new ExcelUtil();
          let fileName: string[] = [];
          for (const tableName in data) {
            const header: string[] = Object.keys(data[tableName][0]);
            eu.sheetName = tableName;
            eu.exportToExcel(header, data[tableName]);
            fileName.push(tableName);
          }
          eu.downloadExcel(fileName.join("-"));

          /* 清空数组 */
          tableNameArr.value = [];
          /* 取消选择 */
          batchRef.value!.clearSelection() ;
        }
      }, (message: string) => {
        ElWarning(message);
      });
}
</script>

<template>
  <div class="container">
    <el-text class="title">
      日志备份
    </el-text>
    <div id="buttons">
      <el-button :icon="Download" type="primary" :disabled="isClickable" @click="batchExport">批量导出</el-button>
      <el-button :icon="Delete" type="danger" :disabled="isClickable" @click="batchDeletion">批量删除</el-button>
    </div>
    <div id="content" v-if="isTableDataEmpty()">
      <el-table :data="tableData" :height="'73.5vh'" style="width: 100%"
                :header-cell-style="{'background':'#E6E8EB',}"
                @selection-change="batchChange"
                ref="batchRef">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="tableName" label="表名" width="240"/>
        <el-table-column prop="tableComment" label="表注释" width="260"/>
        <el-table-column prop="tableType" label="表类型" width="120"/>
        <el-table-column prop="engine" label="表引擎" width="120"/>
        <el-table-column prop="tableCollation" label="表的排序规则(字符集)" width="165"/>
        <el-table-column prop="tableRows" label="表中的数据行数" width="125"/>
        <el-table-column prop="avgRowLength" label="平均每行数据长度" width="140"/>
        <el-table-column prop="dataLength" label="表数据长度" width="120"/>
        <el-table-column prop="createTime" label="表创建时间" :formatter="formatDate" width="220"/>
        <el-table-column prop="updateTime" label="表最近一次更新时间" :formatter="formatDate" width="220"/>
        <el-table-column prop="indexLength" label="表索引据长度" width="120"/>
        <el-table-column prop="version" label="表版本" width="120"/>
        <el-table-column fixed="right" label="操作" width="120">
          <template #default="scope">
            <div>
              <el-button link type="primary" size="small" @click="exportClick(scope.row)">
                导出
              </el-button>
              <el-popover
                  placement="top"
                  trigger="click"
                  :visible="getShowAndHide(scope.row.tableName)"
                  :width="160"
              >
                <p>确定要删除该备份?</p>
                <div style="text-align: right; margin: 0">
                  <el-button size="small" text @click="closeDeletePopover(scope.row.tableName)">取消</el-button>
                  <el-button size="small" type="primary" @click="deleteRoleData(scope.row.tableName)">确定</el-button>
                </div>
                <template #reference>
                  <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.tableName)">
                    删除
                  </el-button>
                </template>
              </el-popover>
            </div>
          </template>
        </el-table-column>
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
  <el-dialog class="dialog-title" v-model="dropConfirmVisible" title="QAQ~警告!!!" width="500" draggable>
    <span style="color: red; font-size: 18px; font-weight: bold;">删除后将无法恢复，请确认您已知晓~QWQ</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dropCancel">容我考虑考虑~(－‸ლ)</el-button>
        <el-button type="primary" @click="dropConfirm">
          知道了~(｀・ω・´)ゞ
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
@import "@/assets/css/container.css";

#content {
  width: 100%;
  margin-top: 20px;
}

#buttons {
  margin-top: 20px;
}

</style>