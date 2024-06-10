<script setup>
import {reactive, ref} from 'vue' ;
import {ElMessageBox} from "element-plus";
import {CirclePlus, Search, TurnOff} from "@element-plus/icons-vue";
import {post, get, put, getUserId} from "@/net/http";
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
  "statusId": '',
  "statusName": '',
  "state": '',
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
  // console.log('reset!');
}

/* 表格 */
/* 编辑 */
const handleClick = (row) => {
  const data = {
    type: 2,
    statusId: row.statusId,
    statusName: row.statusName,
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
  statusId: '',
  statusName: '',
  createId: '',
  updateId: ''
});

/* 表单判断 */
const rule = {
  statusName: [
    {
      required: true, message: '请输入状态名称 '
    }
  ]
}

/* 新增或修改的抽屉标题 */
let drawerTitle = "";

/* 打开抽屉 */
function openDrawer(data) {
  /*新增*/
  if (data.type === 1) {
    drawerTitle = "新增角色状态";
    clearStatusRoleForm();
  } else {
    drawerTitle = "编辑角色状态";
    /*修改*/
    updateStatusRole(data.statusId, data.statusName, data.createId);
  }

  drawer.value = true;
}

/* 新增，清空表单内容 */
function clearStatusRoleForm() {
  form.statusName = '';
  form.createId = getUserId() ;
  form.updateId = '' ;
}

/* 修改，为表单内容赋值 */
function updateStatusRole(statusId, statusName, createId) {
  form.statusId = statusId;
  form.statusName = statusName;
  form.createId = createId;
  form.updateId = getUserId() ;
}

/* 添加状态 */
function cancelClick() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      if (form.statusId === "") {
        post(
            "api/backend/role-status",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            });
      } else {
        /* 修改 */
        put(
            "api/backend/role-status",
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
      get("api/backend/role-status?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
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
  // console.log('submit!');
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
  const state = row.state === '0' ? '1' : '0';
  try {
    await putState({
      statusId: row.statusId,
      state
    });
    row.state = state;
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
    put("api/backend/role-status", data,
        () => {
          ElSuccess(data.state === '0' ? "开启成功" : "禁用成功");
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
function deleteStatusData(statusId) {
  if (!(statusId === "") || !(statusId === undefined)) {
    /* 请求后台删除数据 */
    put(
        "api/backend/role-status",
        {
          statusId: statusId,
          state: "2"
        },
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
  popoverVisible.value[statusId] = false;
}

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(statusId) {
  // 关闭 popover
  popoverVisible.value[statusId] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(statusId) {
  if (popoverVisible.value === undefined) {
    popoverVisible.value[statusId] = true;
  }
  popoverVisible.value[statusId] = true;
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(statusId) {
  return popoverVisible.value[statusId];
}

</script>

<template>
  <div id="role_status">
    <div id="top">
      <el-text class="title">
        角色状态管理
      </el-text>
      <el-form :inline="true" :model="formInline" class="form-inline form-top">
        <el-form-item label="唯一标识">
          <el-input v-model="formInline.id" :size="'default'" placeholder="唯一标识" clearable/>
        </el-form-item>
        <el-form-item label="状态名称">
          <el-input v-model="formInline.name" placeholder="状态名称" clearable/>
        </el-form-item>
        <el-form-item label="角色状态">
          <el-select
              v-model="formInline.state"
              placeholder="角色状态"
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
        <el-table :data="tableData" :row-key="'statusId'" :height="'53vh'"
                  :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
          <el-table-column fixed prop="statusId" label="唯一标识" width="200"/>
          <el-table-column prop="statusName" label="状态名称" width="120"/>
          <el-table-column prop="state" label="状态" width="120">
            <template #default="scope">
              <el-switch
                  :loading="loading"
                  :model-value="scope.row.state === '0'"
                  :checked-value="0"
                  :unchecked-value="1"
                  @change="editStatus(scope.row)"
              />
            </template>
          </el-table-column>
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
                    :visible="getShowAndHide(scope.row.statusId)"
                    :width="160"
                >
                  <p>确定要删除此条数据?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="closeDeletePopover(scope.row.statusId)">取消</el-button>
                    <el-button size="small" type="primary" @click="deleteStatusData(scope.row.statusId)">确定
                    </el-button>
                  </div>
                  <template #reference>
                    <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.statusId)">删除
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
          <el-form-item prop="statusName">
            <el-input :prefix-icon="TurnOff" v-model="form.statusName" placeholder="状态名称" clearable/>
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
#role_status {
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#role_status >>> #top {
  padding-left: 10px;
  padding-right: 10px;
  //margin: 10px ;
}

#role_status >>> #bottom {
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

#role_status >>> #buttons {

}

#role_status >>> #tables {
  margin-top: 20px;
}

#role_status >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}

#role_status >>> .form-top {
  margin-top: 25px;
}

#role_status >>> .pagination {
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