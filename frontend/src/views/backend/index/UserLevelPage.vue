<script setup>
import {reactive, ref} from 'vue' ;
import {ElMessageBox} from "element-plus";
import {CirclePlus, DataAnalysis, Finished, Reading, Search, TurnOff} from "@element-plus/icons-vue";
import {post, get, put, delete_} from "@/net/NetWork";
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
  "levelId": '',
  "levelName": '',
  "level": '',
  "requiredExperience": '',
  "privilegeDescription": ''
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
    levelId: row.levelId,
    levelName: row.levelName,
    level: row.level,
    requiredExperience: row.requiredExperience,
    privilegeDescription: row.privilegeDescription
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
  levelId: '',
  levelName: '',
  level: '',
  requiredExperience: '',
  privilegeDescription: ''
});

/* 表单判断 */
const rule = {
  levelName: [
    {
      required: true, message: '请输入等级名称 '
    }
  ],
  level: [
    {
      required: true, message: '请输入等级 '
    }
  ],
  requiredExperience: [
    {
      required: true, message: '请输入所需经验 '
    }
  ],
  privilegeDescription: [
    {
      required: true, message: '请输入特权描述 '
    }
  ]
}

/* 新增或修改的抽屉标题 */
let drawerTitle = "";

/* 打开抽屉 */
function openDrawer(data) {
  /*新增*/
  if (data.type === 1) {
    drawerTitle = "新增用户等级";
    clearStatusRoleForm();
  } else {
    drawerTitle = "编辑用户等级";
    /*修改*/
    updateStatusRole(data.levelId, data.levelName, data.level, data.requiredExperience, data.privilegeDescription);
  }

  drawer.value = true;
}

/* 新增，清空表单内容 */
function clearStatusRoleForm() {
  form.levelId = '';
  form.levelName = '';
  form.level = '';
  form.requiredExperience = '';
  form.privilegeDescription = '';
}

/* 修改，为表单内容赋值 */
function updateStatusRole(levelId, levelName, level, requiredExperience, privilegeDescription) {
  form.levelId = levelId;
  form.levelName = levelName;
  form.level = level;
  form.requiredExperience = requiredExperience;
  form.privilegeDescription = privilegeDescription;
}

/* 添加用户等级 */
function cancelClick() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 关闭抽屉后提交 */
      drawer.value = false;
      /* 新增 */
      if (form.levelId === "") {
        post(
            "/api/auth/user-level",
            {...form},
            () => {
              ElSuccess("请求成功");
              /* 获取最新数据 */
              getData(page.value, pageSize.value);
            });
      } else {
        /* 修改 */
        put(
            "/api/auth/user-level",
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
      get("api/auth/user-level?pageNum=" + page.value + "&pageSize=" + pageSize.value, (rs) => {
        if (rs.code === 200) {
          resolve(rs);
        } else {
          reject(rs);
        }
      }, (message, code) => {
        if (code === 400 && page.value - 1 >= 0) {
          getData(page.value - 1, pageSize.value);
        } else {
          /* 如果只有一条数据，没有查询到时清空原有tableData中的数据缓存 */
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

/* 判断表格是否有数据 */
function isTableDataEmpty() {
  return tableData.value.length > 0 && !tableData.value.every(obj => Object.values(obj).every(value => value === ''));
}

/* 删除 */
const popoverVisible = ref({}); // 存储弹窗显示状态的对象
/* 确认删除 */
function deleteStatusData(levelId) {
  if (!(levelId === "") || !(levelId === undefined)) {
    /* 请求后台删除数据 */
    delete_(
        "/api/auth/user-level/" + levelId,
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
  popoverVisible.value[levelId] = false;
}

/* 删除弹窗取消按钮事件 */
function closeDeletePopover(levelId) {
  // 关闭 popover
  popoverVisible.value[levelId] = false;
}

/* 显示删除弹窗按钮事件 */
function showDeletePopover(levelId) {
  if (popoverVisible.value === undefined) {
    popoverVisible.value[levelId] = true;
  }
  popoverVisible.value[levelId] = true;
}

/* 获取删除的对应数据弹窗状态，根据数据ID获取 */
function getShowAndHide(levelId) {
  return popoverVisible.value[levelId];
}
</script>

<template>
  <div id="user_level">
    <div id="top">
      <el-text class="title">
        用户等级管理
      </el-text>
      <el-form :inline="true" :model="formInline" class="form-inline form-top">
        <el-form-item label="唯一标识">
          <el-input v-model="formInline.id" :size="'default'" placeholder="唯一标识" clearable/>
        </el-form-item>
        <el-form-item label="等级名称">
          <el-input v-model="formInline.name" placeholder="等级名称" clearable/>
        </el-form-item>
        <el-form-item label="等级状态">
          <el-select
              v-model="formInline.state"
              placeholder="等级状态"
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
        <el-table :data="tableData" :row-key="'levelId'" :height="'53vh'"
                  :header-cell-style="{'background':'#E6E8EB',}" style="width: 100%;">
          <el-table-column fixed prop="levelId" label="唯一标识" width="200"/>
          <el-table-column prop="levelName" label="等级名称" width="200"/>
          <el-table-column prop="level" label="等级" width="150"/>
          <el-table-column prop="requiredExperience" label="所需经验" width="220"/>
          <el-table-column prop="privilegeDescription" label="特权描述" width="350"/>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <div>
                <el-button link type="primary" size="small" @click="handleClick(scope.row)">
                  编辑
                </el-button>
                <el-popover
                    placement="top"
                    trigger="click"
                    :visible="getShowAndHide(scope.row.levelId)"
                    :width="160"
                >
                  <p>确定要删除此条数据?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="closeDeletePopover(scope.row.levelId)">取消</el-button>
                    <el-button size="small" type="primary" @click="deleteStatusData(scope.row.levelId)">确定
                    </el-button>
                  </div>
                  <template #reference>
                    <el-button link type="primary" size="small" @click="showDeletePopover(scope.row.levelId)">删除
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
          <el-form-item prop="levelName">
            <el-input :prefix-icon="CirclePlus" v-model="form.levelName" placeholder="等级名称" clearable/>
          </el-form-item>
          <el-form-item prop="level">
            <el-input :prefix-icon="Finished" v-model="form.level" placeholder="等级" clearable/>
          </el-form-item>
          <el-form-item prop="requiredExperience">
            <el-input :prefix-icon="DataAnalysis" v-model="form.requiredExperience" placeholder="所需经验" clearable/>
          </el-form-item>
          <el-form-item prop="privilegeDescription">
            <el-input :prefix-icon="Reading" v-model="form.privilegeDescription" placeholder="特权说明" clearable/>
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
#user_level {
  margin-left: 7px;
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
}

#user_level >>> #top {
  padding-left: 10px;
  padding-right: 10px;
  //margin: 10px ;
}

#user_level >>> #bottom {
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
}

#user_level >>> #buttons {

}

#user_level >>> #tables {
  margin-top: 20px;
}

#user_level >>> .title {
  display: block;
  padding-top: 10px;
  font-size: 18px;
}

#user_level >>> .form-top {
  margin-top: 25px;
}

#user_level >>> .pagination {
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