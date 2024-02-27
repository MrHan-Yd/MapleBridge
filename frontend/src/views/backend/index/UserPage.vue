<script setup>
import {reactive, ref} from 'vue' ;
import {ElMessageBox} from "element-plus";
import {CirclePlus, Search, TurnOff} from "@element-plus/icons-vue";
import {post, get, put, delete_} from "@/net/NetWork";
import {ElError, ElSuccess, ElWarning} from "@/util/MessageUtil" ;
import {formatDate} from "@/util/FromatDate" ;
import MyIcon from "@/components/MyIcon.vue";
import MyIconButton from "@/components/MyIconButton.vue";

/* 查询表单 */
const formInline = reactive({
  user: '',
  region: '',
  date: '',
});

/* 表格数据 */
const tableData = ref([{
  "id": "",
  "account": "",
  "email": "",
  "nickname": "",
  "gender": "",
  "birthday": "",
  "experience": "",
  "registerTime": "",
  "statusId": "",
  "createId": "",
  "createTime": "",
  "updateId": "",
  "updateTime": "",
  "level": {},
  "role": {}
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
  const data = {
    type: 2,
    id: row.id,
    account: row.account,
    email: row.email,
    roleId: row.role.roleId,
  }
  openDrawer(data);
}

/* 确认重置密码 */
function resetPassword(id) {
  if (!(id === "") || !(id === undefined)) {
    /* 请求后台重置密码 */
    put(
        "/api/auth/user",
        {
          id: id,
          password: "xyh123456"
        },
        async (rs) => {
          if (rs.code === 200) {
            ElSuccess(rs.message + "，密码重置为：xyh123456");
            /* 更新数据 */
            await getData(page.value, pageSize.value);
          }
        },
        (message, code) => {
          ElError(message);
        }
    );
  }
  // 关闭 popover
  popoverVisibleResetPassword.value[id] = false;
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
  account: '',
  email: '',
  roleId: ''
});

/* 表单判断 */
const rule = {
  account: [
    {
      required: true, message: '请输入用户账号'
    }
  ],
  email: [
    {
      required: true, message: '请输入用户邮箱'
    }
  ],
  roleId: [
    {
      required: true, message: "请选择用户角色"
    }
  ]
}

/* 新增或修改的抽屉标题 */
let drawerTitle = "";

/* 打开抽屉 */
function openDrawer(data) {
  /* 获取角色 */
  getUserRole() ;

  /*新增*/
  if (data.type === 1) {
    drawerTitle = "新增平台用户";
    clearStatusRoleForm();
  } else {
    drawerTitle = "编辑平台用户";
    /*修改*/
    updateStatusRole(data.id, data.account, data.email, data.roleId);
  }

  drawer.value = true;
}
/* 获取所有角色 */
function getUserRole() {
  get("/api/auth/role?isItPaginated=false",
      (rs) => {
        cities.value = rs.data;
      }
  );
}


/* 新增角色权限选择器 */
const cities = ref([{
  "roleId": '',
  "roleName": '',
  "roleNameCn": '',
}]);

/* 新增，清空表单内容 */
function clearStatusRoleForm() {
  form.id = '' ;
  form.account = '';
  form.email = '';
  form.roleId = '';
}

/* 修改，为表单内容赋值 */
function updateStatusRole(id, account, email, roleId) {
  form.id = id ;
  form.account = account;
  form.email = email;
  form.roleId = roleId ;
}

/* 添加用户 */
function cancelClick() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      if (form.id === "") {
        post(
            "/api/auth/user",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            });
      } else {
        /* 修改 */
        put(
            "/api/auth/user",
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
      get("api/auth/user?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
        if (rs.code === 200) {
          resolve(rs);
        } else {
          reject(rs);
        }
      }, (message, code) => {
        if (code === 400 && page.value - 1 >= 0) {
          getData(page.value - 1, pageSize.value);
        } else {
          ElWarning(message) ;
        }
      });
    });

    if (tableData.value.length > 0) {
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
  const statusId = row.statusId === '1755492769986392066' ? '1755493082902441985' : '1755492769986392066';
  try {
    await putState({
      id: row.id,
      statusId
    });
    row.statusId = statusId;
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
    put("/api/auth/user", data,
        () => {
          ElSuccess(data.statusId === '1755492769986392066' ? "开启成功" : "禁用成功");
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

/* 重置密码 */
const popoverVisibleResetPassword = ref({}); // 存储弹窗显示状态的对象

/* 确认删除 */
function deleteStatusData(id) {
  if (!(id === "") || !(id === undefined)) {
    /* 请求后台删除数据 */
    delete_(
        "/api/auth/user",
        {
          id: id
        },
        async (rs) => {
          if (rs.code === 200) {
            ElSuccess(rs.message);
            /* 更新数据 */
            await getData(page.value, pageSize.value);
          }
        },
        (message, code) => {
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

/* 重置密码弹窗取消按钮事件 */
function closeResetPasswordPopover(id) {
  // 关闭 popover
  popoverVisibleResetPassword.value[id] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(id) {
  if (popoverVisible.value === undefined) {
    popoverVisible.value[id] = true;
  }
  popoverVisible.value[id] = true;
}

/* 显示重置密码弹窗按钮事件 */
function showResetPasswordPopover(id) {
  if (popoverVisibleResetPassword.value === undefined) {
    popoverVisibleResetPassword.value[id] = true;
  }
  popoverVisibleResetPassword.value[id] = true;
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(id) {
  return popoverVisible.value[id];
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHideResetPassword(id) {
  return popoverVisibleResetPassword.value[id];
}

</script>

<template>
  <div id="user">
    <div id="top">
      <el-text class="title">
        平台用户管理
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
        <el-table :data="tableData" :row-key="'id'" :height="'53vh'"
                  :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
          <el-table-column fixed prop="id" label="唯一标识" width="200"/>
          <el-table-column prop="account" label="用户账号" width="120"/>
          <el-table-column prop="email" label="用户邮箱" width="220"/>
          <el-table-column prop="statusId" label="用户状态" width="120">
            <template #default="scope">
              <el-switch
                  :loading="loading"
                  :model-value="scope.row.statusId === '1755492769986392066'"
                  :checked-value="0"
                  :unchecked-value="1"
                  @change="editStatus(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="role.roleNameCn" label="用户角色" width="120"/>
          <el-table-column prop="level.levelName" label="用户等级" width="120"/>
          <el-table-column prop="experience" label="用户经验值" width="120"/>
          <el-table-column prop="nickname" label="用户昵称" width="120"/>
          <el-table-column prop="gender" label="用户性别" width="120"/>
          <el-table-column prop="birthday" label="用户生日" :formatter="formatDate" width="220"/>
          <el-table-column prop="registerTime" label="注册时间" :formatter="formatDate" width="220"/>
          <el-table-column prop="createId" label="创建人" width="120"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatDate" width="220"/>
          <el-table-column prop="updateId" label="更新人" width="120"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatDate" width="220"/>
          <el-table-column fixed="right" label="操作" width="165">
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
                <el-popover
                    placement="top"
                    trigger="click"
                    :visible="getShowAndHideResetPassword(scope.row.id)"
                    :width="160"
                >
                  <p>确定重置用户:{{scope.row.account}}的密码?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="closeResetPasswordPopover(scope.row.id)">取消</el-button>
                    <el-button size="small" type="primary" @click="resetPassword(scope.row.id)">确定
                    </el-button>
                  </div>
                  <template #reference>
                    <el-button link type="primary" size="small" @click="showResetPasswordPopover(scope.row.id)">重置密码
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
          <el-form-item prop="account">
            <el-input :prefix-icon="TurnOff" minlength="6" maxlength="10" show-word-limit v-model="form.account" placeholder="用户账号" clearable/>
          </el-form-item>
          <el-form-item prop="email">
            <el-input :prefix-icon="TurnOff" minlength="6" maxlength="30" show-word-limit v-model="form.email" placeholder="用户邮箱" clearable/>
          </el-form-item>
          <el-form-item prop="roleId" >
            <el-select
                v-model="form.roleId"
                placeholder="选择用户角色"
                clearable>
              <el-option
                  v-for="item in cities"
                  :key="item.roleId"
                  :label="item.roleNameCn"
                  :value="item.roleId"
              >
                <span style="float: left">{{ item.roleNameCn }}</span>
                <span
                    style="float: right;color: var(--el-text-color-secondary);font-size: 13px;"
                >{{ item.roleName }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-alert type="warning" show-icon :closable="false">
            <p>用户密码初始化为："xyh123456"</p>
          </el-alert>
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
#user {
  margin-left: 7px;
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#user >>> #top {
  padding-left: 10px;
  padding-right: 10px;
  //margin: 10px ;
}

#user >>> #bottom {
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

#user >>> #buttons {

}

#user >>> #tables {
  margin-top: 20px;
}

#user >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}

#user >>> .form-top {
  margin-top: 25px;
}

#user >>> .pagination {
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