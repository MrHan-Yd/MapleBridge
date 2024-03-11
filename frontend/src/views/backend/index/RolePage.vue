<script setup>
import {reactive, ref} from 'vue' ;
import {ElMessageBox} from "element-plus";
import {Avatar, CirclePlus, Search, UserFilled} from "@element-plus/icons-vue";
import {post, get, put, delete_, getUserId} from "@/net/NetWork";
import {ElError, ElSuccess, ElWarning} from "@/util/MessageUtil" ;
import {formatDate} from "@/util/FromatDate" ;
import MyIconButton from "@/components/MyIconButton.vue";

/* 查询表单 */
const formInline = reactive({
  user: '',
  region: '',
  date: '',
});

/* 表格数据 */
const tableData = ref([{
  "roleId": '',
  "roleName": '',
  "roleNameCn": '',
  "statusId": '',
  "createId": '',
  "createTime": '',
  "updateId": '',
  "updateTime": '',
  "permissionList": [{
    "permissionId": '',
    "permissionName": '',
    "permissionUrl": ''
  }],
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
    roleId: row.roleId,
    roleName: row.roleName,
    roleNameCn: row.roleNameCn,
    permissionList: row.permissionList
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
  roleId: '',
  roleName: '',
  roleNameCn: '',
  permissionIdList: '',
  createId: ''
});

/* 表单判断 */
const rule = {
  roleName: [
    {
      required: true, message: '请输入角色名称'
    }
  ],
  roleNameCn: [
    {
      required: true, message: '请输入角色中文名称'
    }
  ],
  permissionIdList: [
    {
      required: true, message: '请选择角色权限'
    }
  ]
}

/* 获取所有权限 */
function getPermission() {
  get("api/backend-admin/permission?isItPaginated=false",
      (rs) => {
        cities.value = rs.data;
      }
  );
}

/* 新增角色权限选择器 */
const cities = ref([{}]);

/* 新增或修改的抽屉标题 */
let drawerTitle = "";

/* 打开抽屉 */
function openDrawer(data) {
  /* 获取权限 */
  getPermission();
  /*新增*/
  if (data.type === 1) {
    drawerTitle = "新增角色";
    clearRoleForm();
  } else {
    drawerTitle = "编辑角色";

    let permissionIdList = new Array(data.permissionList.length);

    for(let i = 0; i < data.permissionList.length; i++) {
      permissionIdList[i] = data.permissionList[i].permissionId ;
    }
    /*修改*/
    updateRole(data.roleId, data.roleName, data.roleNameCn, permissionIdList);
  }

  drawer.value = true;
}

/* 新增，清空表单内容 */
function clearRoleForm() {
  form.roleId = '';
  form.roleName = '';
  form.roleNameCn = '';
  form.permissionIdList = '';
  form.createId = getUserId();
}

/* 修改，为表单内容赋值 */
function updateRole(roleId, roleName, roleNameCn, permissionIdList) {
  form.roleId = roleId;
  form.roleName = roleName;
  form.roleNameCn = roleNameCn;
  form.permissionIdList = permissionIdList ;
}

/* 添加角色 */
function cancelClick() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      if (form.roleId === "") {
        post(
            "api/backend-admin/role",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            });
      } else {
        /* 修改 */
        put(
            "api/backend-admin/role",
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
      get("api/backend-admin/role?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
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
      ElWarning(error.data.message);
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

/* 是否属于加载状态 */
let loading;
/* 开关事件 */
/** 修改用户状态 */
const editStatus = async (row) => {
  loading = true;
  const statusId = row.statusId === '1749414567304179713' ? '1749402591433838593' : '1749414567304179713';
  try {
    await putState({
      roleId: row.roleId,
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
    put("api/backend-admin/role", data,
        () => {
          ElSuccess(data.statusId === '1749402591433838593' ? "开启成功" : "禁用成功");
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
function deleteRoleData(roleId) {
  if (!(roleId === "") || !(roleId === undefined)) {
    /* 请求后台删除数据 */
    delete_(
        "api/backend-admin/role/" + roleId,
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
  popoverVisible.value[roleId] = false;
}

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(roleId) {
  // 关闭 popover
  popoverVisible.value[roleId] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(roleId) {
  if (popoverVisible.value === undefined) {
    popoverVisible.value[roleId] = true;
  }
  popoverVisible.value[roleId] = true;
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(roleId) {
  return popoverVisible.value[roleId];
}

</script>

<template>
  <div id="role">
    <div id="top">
      <el-text class="title">
        平台角色管理
      </el-text>
      <el-form :inline="true" :model="formInline" class="form-inline form-top">
        <el-form-item label="唯一标识">
          <el-input v-model="formInline.id" :size="'default'" placeholder="唯一标识" clearable/>
        </el-form-item>
        <el-form-item label="角色名称">
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
        <el-table :data="tableData" :row-key="'roleId'" :height="'53vh'"
                  :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
          <el-table-column fixed prop="roleId" label="唯一标识" width="200"/>
          <el-table-column prop="roleName" label="角色名称" width="120"/>
          <el-table-column prop="roleNameCn" label="角色中文名称" width="200"/>
          <el-table-column prop="statusId" label="状态" width="120">
            <template #default="scope">
              <el-switch
                  :loading="loading"
                  :model-value="scope.row.statusId === '1749402591433838593'"
                  :checked-value="0"
                  :unchecked-value="1"
                  @change="editStatus(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createId" label="创建人" width="200"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatDate" width="220"/>
          <el-table-column prop="updateId" label="更新人" width="120"/>
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
                    :visible="getShowAndHide(scope.row.roleId)"
                    :width="160"
                >
                  <p>确定要删除此条数据?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="closeDeletePopover(scope.row.roleId)">取消</el-button>
                    <el-button size="small" type="primary" @click="deleteRoleData(scope.row.roleId)">确定
                    </el-button>
                  </div>
                  <template #reference>
                    <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.roleId)">
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
  </div>
  <el-drawer
      v-model="drawer"
      :title="drawerTitle"
      :direction="'rtl'"
  >
    <template #default>
      <div>
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="roleName">
            <el-input :prefix-icon="Avatar" v-model="form.roleName" placeholder="角色名称" clearable/>
          </el-form-item>
          <el-form-item prop="roleNameCn">
            <el-input :prefix-icon="UserFilled" v-model="form.roleNameCn" placeholder="角色中文名称" clearable/>
          </el-form-item>
          <el-form-item prop="permissionList">
            <el-select
                v-model="form.permissionIdList"
                multiple
                clearable
                collapse-tags
                collapse-tags-tooltip
                placeholder="选择权限"
                popper-class="custom-header"
                :max-collapse-tags="2"
            >
              <el-option
                  v-for="item in cities"
                  :key="item.permissionId"
                  :label="item.permissionName"
                  :value="item.permissionId"
              >
                <span style="float: left">{{ item.permissionName }}</span>
                <span
                    style="float: right;color: var(--el-text-color-secondary);font-size: 13px;"
                >{{ item.permissionUrl }}</span>
              </el-option>
            </el-select>
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
#role {
  margin-left: 7px;
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#role >>> #top {
  padding-left: 10px;
  padding-right: 10px;
  //margin: 10px ;
}

#role >>> #bottom {
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

#role >>> #buttons {

}

#role >>> #tables {
  margin-top: 20px;
}

#role >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}

#role >>> .form-top {
  margin-top: 25px;
}

#role >>> .pagination {
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