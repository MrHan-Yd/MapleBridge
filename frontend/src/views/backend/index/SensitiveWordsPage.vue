<script setup lang="ts">
import {ref, reactive, computed} from 'vue'
import {delete_, get, getUserId, post, delete_batch} from "@/net/http";
import {ElError, ElSuccess, ElWarning} from "@/util/MessageUtil" ;
import Page from "@/domain/Page";

import {formatDate} from "@/util/FormatData";
import {Response} from "@/domain/Response";
import {SensitiveWords} from "@/domain/SensitiveWords";
import {CirclePlus, Delete, EditPen} from "@element-plus/icons-vue";

const tableData = ref<SensitiveWords[]>([]);

/* 判断表格是否有数据 */
function isTableDataEmpty() {
  return tableData.value.length > 0 && !tableData.value.every((obj: SensitiveWords) => Object.values(obj).every(value => value === ''));
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
    const response: any = await new Promise<Response<SensitiveWords> | any>((resolve, reject) => {
      get("api/backend/sensitive-words?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
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
function getShowAndHide(id: string) {
  return popoverVisible.value[id];
}

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(id: string) {
  // 关闭 popover
  popoverVisible.value[id] = false;
}

/* 确认删除 */
function deleteRoleData(id: string) {
  if (!(id === "")) {
    /* 请求后台删除数据 */
    delete_(
        "api/backend/sensitive-words/" + id,
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
  }
  // 关闭 popover
  popoverVisible.value[id] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(id: string) {
  popoverVisible.value[id] = true;
}

/* 分页切换每页数据显示几条 */
const handleSizeChange = (pageSize: number) => {
  getData(pageData.current, pageSize);
}

/* 换页 */
const handleCurrentChange = (pageNum: number) => {
  getData(pageNum, pageData.size);
}

/* 打开新增弹窗 */
const openDrawer = () => {
  // 重置表单对象的属性
  Object.assign(form, {
    id: '',
    word: '',
    createId: '',
  });

  /* 打开新增弹窗 */
  drawerTitle.value = "新增敏感词";
  form.createId = getUserId();

  drawer.value = true;
}

/* 抽屉 */
const drawer = ref<boolean>(false);
const drawerTitle = ref<string>("");

/* 表单 */
const form = reactive<SensitiveWords>({
  id: '',
  word: '',
  createId: '',
});
/* 表单校验 */
const formRef = ref();
/* 表单判断 */
const rule = {
  word: [
    {
      required: true, message: '请输入关键词'
    }
  ]
}

/* 关闭 */
function confirmClick() {
  drawer.value = false;
}

/* 提交 */
function cancelClick() {
  formRef.value.validate((valid: any) => {
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      post(
          "api/backend/sensitive-words",
          {...form},
          (rs: Response<SensitiveWords>) => {
            if (rs.code === 200) {
              ElSuccess(rs.message);
            } else {
              ElWarning(rs) ;
            }
            /* 获取最新数据 */
            getData(page.value, pageSize.value);
          }, (message) => {
            ElError(message);
          });
    }
  });
}

/* 批量删除多选 */
const batchDeletionRef = ref<any>();
const batchDeletionArr = ref<string[]>([]);
/* 选择 */
const batchDeletionChange = (val: SensitiveWords[]) => {
  batchDeletionArr.value = val.map(s => s.id);
}
/* 计算属性：按钮是否可点击 */
const isClickable = computed(() => {
  /* 选中两条以上 */
  return !(batchDeletionArr.value.length > 1);
});

/* 批量删除 */
const batchDeletion = () => {
  // 批量删除逻辑
  delete_batch("api/backend/sensitive-words",
      batchDeletionArr.value,
      (rs: Response<any>) => {
        if (rs.code === 200) {
          ElSuccess("请求成功");
          /* 清空删除ID数组 */
          batchDeletionArr.value = [];

          /* 清空选择 */
          batchDeletionRef.value!.clearSelection();

          /* 获取最新数据 */
          getData(page.value, pageSize.value);
        } else {
          ElWarning(rs.message);
        }
      }, (message: string) => {
        ElError(message);
        /* 清空删除ID数组 */
        batchDeletionArr.value = [];

        /* 清空选择 */
        batchDeletionRef.value!.clearSelection();
      });
}
</script>

<template>
  <div class="container">
    <el-text class="title">
      关键词库管理
    </el-text>
    <div id="buttons">
      <el-button :icon="CirclePlus" type="primary" @click="openDrawer()">新增</el-button>
      <el-button :icon="Delete" type="danger" :disabled="isClickable" @click="batchDeletion">批量删除</el-button>
    </div>
    <div id="content" v-if="isTableDataEmpty()">
      <el-table
          :data="tableData"
          :row-key="'id'"
          :height="'65.5vh'"
          style="width: 100%;margin-top: 20px;"
          :header-cell-style="{'background':'#E6E8EB',}"
          @selection-change="batchDeletionChange"
          ref="batchDeletionRef">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="唯一标识" width="300"/>
        <el-table-column  prop="word" label="关键词" width="300"/>
        <el-table-column prop="createId" label="创建人" width="300"/>
        <el-table-column prop="createTime" label="创建时间" :formatter="formatDate" width="220"/>
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <div>
              <el-popover
                  placement="top"
                  trigger="click"
                  :visible="getShowAndHide(scope.row.id)"
                  :width="160"
              >
                <p>确定要删除此条数据?</p>
                <div style="text-align: right; margin: 0">
                  <el-button size="small" text @click="closeDeletePopover(scope.row.id)">取消</el-button>
                  <el-button size="small" type="primary" @click="deleteRoleData(scope.row.id)">确定
                  </el-button>
                </div>
                <template #reference>
                  <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.id)">
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
  <el-drawer
      v-model="drawer"
      :title="drawerTitle"
      :direction="'rtl'"
  >
    <template #default>
      <div>
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="word">
            <el-input :prefix-icon="EditPen" v-model="form.word" placeholder="敏感词" clearable/>
          </el-form-item>
        </el-form>
      </div>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="confirmClick">关闭</el-button>
        <el-button type="primary" @click="cancelClick">提交</el-button>
      </div>
    </template>
  </el-drawer>
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