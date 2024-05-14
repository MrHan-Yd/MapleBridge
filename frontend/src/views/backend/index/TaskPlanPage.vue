<script setup>
import {reactive, ref} from 'vue' ;
import {ElMessageBox} from "element-plus";
import {CirclePlus, EditPen, Search} from "@element-plus/icons-vue";
import {post, get, put, getUserId, delete_} from "@/net/NetWork";
import {ElError, ElSuccess, ElWarning} from "@/util/MessageUtil" ;
import {formatDate} from "@/util/FormatData" ;
import MyIconButton from "@/components/MyIconButton.vue";

/* 查询表单 */
const formInline = reactive({
  user: '',
  region: '',
  date: '',
});

/* 表格数据 */
const tableData = ref([{
  "id": '',
  "cron": '',
  "remarks": '',
  "status": '',
  "task": '',
  "createId": '',
  "createTime": '',
  "updateId": '',
  "updateTime": '',
}]);

/* 分页参数 */
const pageData = reactive([{
  "total": '',
  "size": '',
  "current": '',
  "pages": ''
}]);

/* 分页 */
const page = ref(1);
const pageSize = ref(10);
const small = ref(false);
const disabled = ref(false);
const background = ref(false);

/* 查询重置 */
const onReset = () => {
  console.log('reset!');
}

/* 表格 */
/* 编辑 */
const handleClick = (row) => {
  console.log(row.task)
  const data = {
    type: 2,
    id: row.id,
    taskId: row.task.taskId,
    taskName: row.task.taskName,
    taskRemarks: row.task.taskRemarks,
    cron: row.cron,
    remarks: row.remarks,
    createId: row.createId,
  }
  openDrawer(data);
}
/* 分页切换每页数据显示几条 */
const handleSizeChange = (pageSize) => {
  getData(1, pageSize);
}
/* 换页 */
const handleCurrentChange = (pageNum) => {
  getData(pageNum);
}

/* 新增或编辑抽屉 */
const drawer = ref(false);

/* 表单校验 */
const formRef = ref();
const form = reactive({
  id: '',
  taskId: '',
  cron: '',
  status: '',
  remarks: '',
  createId: '',
  updateId: '',
});

/* 表单判断 */
const rule = {
  taskId: [
    {
      required: true, message: '请选择任务'
    }
  ],
  cron: [
    {
      required: true, message: '请输入定时表达式'
    }
  ],
  remarks: [
    {
      required: true, message: '请输入备注'
    }
  ]
}

/* 新增或修改的抽屉标题 */
let drawerTitle = "";

/* 打开抽屉 */
function openDrawer(data) {
  /* 查询所有任务 */
  getTask() ;

  /*新增*/
  if (data.type === 1) {
    drawerTitle = "新增任务计划";
    clearTaskPlanForm();
  } else {
    drawerTitle = "编辑任务计划";
    /*修改*/
    updateTaskPlan(data);
  }

  drawer.value = true;
}

/* 任务列表  */
const taskList = ref([]);

/* 查询所有任务 */
function getTask() {
  get("api/backend/task", (rs) => {
    if (rs.code === 200) {
      taskList.value = rs.data;
    }
  }, (message, code) => {
    if(code === 400) {
      ElWarning("暂无任务") ;
    } else {
      ElWarning(message) ;
    }
  }) ;
}

/* 新增，清空表单内容 */
function clearTaskPlanForm() {
  taskList.value = [] ;
  form.id = '';
  form.taskId = '';
  form.remarks = '';
  form.cron = '';
  form.createId = getUserId() ;
  form.updateId = '' ;
}

/* 修改，为表单内容赋值 */
function updateTaskPlan(data) {
  form.id = data.id;
  const task = {
    taskId: data.taskId,
    taskName: data.taskName,
    taskRemarks: data.taskRemarks,
  }
  form.status = data.status;
  form.taskId = data.taskId;
  taskList.value.push(task);
  form.cron = data.cron;
  form.remarks = data.remarks;
  form.createId = data.createId;
  form.updateId = getUserId() ;
}

/* 添加任务计划 */
function cancelClick() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      if (form.id === "") {
        post(
            "api/backend/task-plan",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            });
      } else {
        /* 修改 */
        put(
            "api/backend/task-plan",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            }
        )
      }
    }
  });

}

/* 获取数据 */
const getData = async (num, size) => {
  if (num !== undefined) {
    page.value = num;
  }
  if (size !== undefined) {
    pageSize.value = size;
  }
  /* 页面加载后请求后台获取数据 */
  try {
    const response = await new Promise((resolve, reject) => {
      get("api/backend/task-plan?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
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
          tableData.value = "" ;
          ElWarning(message) ;
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

  } catch (error) {
    if (error.data.code === 403) {
      ElWarning(error.data.message) ;
    } else {
      ElError("获取数据时异常" + error);
    }
  }
}
/* 加载页面数据 */
getData();

/*  查询提交 */
const onSubmit = () => {
  console.log('submit!');
}

/* 关闭表单*/
function confirmClick() {
  ElMessageBox.confirm("关闭后表单中的数据将会被销毁！")
      .then(() => {
        drawer.value = false
      })
      .catch(() => {
        // catch error
      });
}

let loading;
/* 开关事件 */
/** 修改用户状态 */
const editStatus = async (row) => {
  loading = true;
  const status = row.status === '0' ? '1' : '0';
  try {
    await putState({
      id: row.id,
      updateId: getUserId(),
      status
    });
    row.status = status;
    /* 更新数据 */
    await getData(page.value, pageSize.value);
  } catch (err) {
    ElError(err.message);
  } finally {
    loading = false;
  }
}

/* 修改状态 */
const putState = (data) => {
  return new Promise((resolve, reject) => {
    put("api/backend/task-plan", data,
        () => {
          ElSuccess(data.status === '0' ? "开启成功" : "禁用成功");
          resolve(); // 成功时 resolve
        },
        (error) => {
          ElError(error.message);
          reject(error); // 失败时 reject
        }
    );
  });
};

/* 判断表格是否有数据 */
function isTableDataEmpty() {
  return tableData.value.length > 0 && !tableData.value.every(obj => Object.values(obj).every(value => value === ''));
}

/* 删除 */
const popoverVisible = ref({}); // 存储弹窗显示状态的对象
/* 确认删除 */
function deleteStatusData(id) {
  if (!(id === "") || !(id === undefined)) {
    /* 请求后台删除数据 */
    delete_ (
        "api/backend/task-plan/" + id,
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

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(id) {
  // 关闭 popover
  popoverVisible.value[id] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(id) {
  if (popoverVisible.value === undefined) {
    popoverVisible.value[id] = true;
  }
  popoverVisible.value[id] = true;
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(id) {
  return popoverVisible.value[id];
}

</script>

<template>
  <div id="task_plan">
    <div id="top">
      <el-text class="title">
        任务执行计划
      </el-text>
      <el-form :inline="true" :model="formInline" class="form-inline form-top">
        <el-form-item label="唯一标识">
          <el-input v-model="formInline.id" :size="'default'" placeholder="唯一标识" clearable/>
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="formInline.name" placeholder="任务名称" clearable/>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select
              v-model="formInline.state"
              placeholder="任务状态"
              clearable
          >
            <el-option label="在用" value="0"/>
            <el-option label="禁用" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" type="primary" @click="onSubmit">查询</el-button>
          <el-button @click="onReset">
            <my-icon-button class="el-icon--left reset" name="icon-zhongzhi"/>
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <el-divider/>
    </div>
    <div id="bottom">
      <div id="buttons">
        <el-button :icon="CirclePlus" type="primary" @click="openDrawer({type:1})">新增</el-button>
      </div>
      <div id="tables" v-if="isTableDataEmpty()">
        <el-table :data="tableData" :row-key="'id'" :height="'53vh'"
                  :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
          <el-table-column fixed prop="id" label="唯一标识" width="200"/>
          <el-table-column prop="cron" label="定时表达式" width="120"/>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-switch
                  :loading="loading"
                  :model-value="scope.row.status === '0'"
                  :checked-value="0"
                  :unchecked-value="1"
                  @change="editStatus(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" width="200"/>
          <el-table-column prop="task.taskRemarks" label="任务" width="200"/>
          <el-table-column prop="createId" label="创建人" width="200"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatDate" width="220"/>
          <el-table-column prop="updateId" label="更新人" width="200"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatDate" width="220"/>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <div>
                <el-button link type="primary" size="small" @click="handleClick(scope.row)">
                  编辑
                </el-button>
                <el-popover
                    placement="top"
                    trigger="click"
                    :visible="getShowAndHide(scope.row.id)"
                    :width="160"
                >
                  <p>确定要删除此条数据?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="closeDeletePopover(scope.row.id)">取消</el-button>
                    <el-button size="small" type="primary" @click="deleteStatusData(scope.row.id)">确定
                    </el-button>
                  </div>
                  <template #reference>
                    <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.id)">删除
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
  </div>
  <el-drawer
      v-model="drawer"
      :title="drawerTitle"
      :direction="'rtl'"
  >
    <template #default>
      <div>
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="taskId" >
            <el-select
                v-model="form.taskId"
                placeholder="选择任务"
                :disabled="form.taskId !== ''"
                clearable>
              <el-option
                  v-for="item in taskList"
                  :key="item.taskId"
                  :label="item.taskRemarks"
                  :value="item.taskId"
              >
                <span style="float: left">{{ item.taskName }}</span>
                <span
                    style="float: right;color: var(--el-text-color-secondary);font-size: 13px;"
                >{{ item.taskRemarks }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="cron">
            <el-input :prefix-icon="EditPen" v-model="form.cron" placeholder="cron表达式" clearable/>
          </el-form-item>
          <el-form-item prop="remarks" >
            <el-input :prefix-icon="EditPen" v-model="form.remarks" placeholder="备注" clearable/>
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
#task_plan {
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#task_plan >>> #top {
  padding-left: 10px;
  padding-right: 10px;
  //margin: 10px ;
}

#task_plan >>> #bottom {
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

#task_plan >>> #buttons {

}

#task_plan >>> #tables {
  margin-top: 20px;
}

#task_plan >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}

#task_plan >>> .form-top {
  margin-top: 25px;
}

#task_plan >>> .pagination {
  margin-top: 12px;
  display: flex;
  justify-content: right;
  align-items: center;
}

.form-inline .el-input {
  --el-input-width: 220px;
}

.form-inline .el-select {
  --el-select-width: 220px;
}
</style>