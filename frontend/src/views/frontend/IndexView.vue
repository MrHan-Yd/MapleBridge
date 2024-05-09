<script setup>
import {ref, reactive, onMounted} from 'vue'
import { Plus, Search} from "@element-plus/icons-vue";
import MyIcon from "@/components/MyIcon.vue";
import {logout} from "@/net/Login";
import router from "@/router";
import {get, put, getUserId, postFormData} from "@/net/NetWork";
import {ElSuccess, ElWarning} from "@/util/MessageUtil";
import * as FormatDate from "@/util/FormatData";
import {ElTag} from "element-plus";

/* 页面打开时默认调用 */
onMounted(() => {
  router.push('/frontend-index-home');
});

/* 退出登录 */
function userLogout() {
  logout(() => {
    router.push('/frontend-welcome-login');
  });
}

/* 搜索表单 */
const searchForm = reactive({
  content: '',
});

/* 菜单对应页面 */
const activeIndex = ref('1');

/* 菜单选择 */
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

/* 模态框 */
const dialogVisible = ref(false)

/* 表单校验 */
const formRef = ref();

/* 登录表单校验 */
const rule = {
  typeId: [
    {
      required: true, message: "请选择分享类型"
    }
  ],
  topic: [
    {
      required: true, message: "请输入标题"
    }
  ],
  content: [
    {
      required: true, message: "请输入内容"
    }
  ]
}

/* 初始化表单 */
const clearPostForm = () => {
  postForm.postId = '';
  postForm.userId = getUserId();
  postForm.topic = '';
  postForm.content = '';
  postForm.fileList = '';
  postForm.typeId = '';
  /*
  *由于formData对象是不可变的，所以每次初始化，
  * 都需要创建一个新的，不需要担心内存泄漏问题，
  * 在JS中当一个对象没有任何引用指向它时，
  * 该对象会被视为垃圾，由垃圾回收机制自动处理
  * */
  formData = new FormData();
}

/* 请求后端的携带数据 */
let formData = '';

/* 开始分享，清空表单 */
const startSharing = () => {
  /* 初始化表单 */
  clearPostForm();

  /* 查询帖子类型 */
  getPostTypes();

  /* 显示对话框 */
  dialogVisible.value = true;
}

/* 关闭分享中心对话框 */
const closeSharing = () => {
  /* 初始化表单 */
  clearPostForm();

  /* 显示对话框 */
  dialogVisible.value = false;
}

/* 帖子类型选择器 */
const postTypes = ref([{}]);

/* 获取帖子类型 */
function getPostTypes() {
  /* 获取帖子类型 */
  get("api/index/post-types", (rs) => {
    if (rs.code === 200) {
      postTypes.value = rs.data;
    } else {
      ElWarning("分享类型获取失败")
    }
  });
}

/* 发布帖子 */
function submitPostForm() {
  /* 发布帖子 */
  formRef.value.validate(valid => {
    /* 校验通过 */
    if (valid) {

      /* 处理上传文件 */
      formData.append("userId", postForm.userId);
      formData.append("topic", postForm.topic);
      formData.append("content", postForm.content);
      formData.append("typeId", postForm.typeId);

      // 使用 Axios 发送 POST 请求
      postFormData("api/index/post", formData, (rs) => {
        if (rs.code === 200) {
          ElSuccess(rs.message);
        } else {
          ElWarning(rs.message);
        }
      });
      /* 显示对话框 */
      dialogVisible.value = false;
    }
  });

}

/* 帖子表单 */
const postForm = reactive({
  postId: '',
  userId: '',
  topic: '',
  content: '',
  typeId: '',
  /* 分享中心发布图片墙 */
  fileList: []
});


/* 预览图像 */
const dialogImageUrl = ref('')
/* 预览按钮 */
const dialogVisible2 = ref(false)

/* 删除照片墙 */
const handleRemove = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles);
}
/* 照片墙预览 */
const handlePictureCardPreview = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url;
  dialogVisible2.value = true;
};

/* 照片选择触发 */
const choiceImages = (file) => {
  formData.append("files", file.file);
  return file;
}
/* 限制上传文件类型 */
const checkFileType = (file) => {
  const fileName = file.name;
  const fileType = fileName.substring(fileName.lastIndexOf('.'));
  if (fileType !== '.jpg' && fileType !== '.jpeg' && fileType !== '.png' && fileType !== '.gif') {
    ElWarning("只能上传：jpg、jpeg、png、gif文件");
    return false;
  }
  return true;
}

/* 决定个人中心是否模块框是否显示 */
const personalCenterDialogVisible = ref(false);

const personalCenterDialog = () => {
  /* 重置为编辑状态 */
  personalCenterOperationButton.value = true;
  /* 显示对话框 */
  personalCenterDialogVisible.value = true;
}

/* 获取用户信息 */
// 获取用户数据
function getUser() {
  return new Promise((resolve, reject) => {
    if (userData.value !== undefined && Object.keys(userData.value).length !== 0) {
      resolve(userData);
    } else {
      // 如果userData.value不存在或者没有数据，则进行请求
      get("api/index/user/" + getUserId(), (rs) => {
        if (rs.code === 200) {
          userData.value = rs.data;
          resolve(userData);
        } else {
          ElWarning("个人信息获取失败");
          reject("个人信息获取失败");
        }
      });
    }
  });
}


/* 获取等级信息 */
const level = ref();

/* 等级信息处理 */
function getLevel() {

  get("api/index/level", (rs) => {
    if (rs.code === 200) {
      level.value = rs.data;

      /* 当前等级经验值 */
      let experience = userData.value.experience;
      // 这里可以继续处理等级信息
      for (let i = 0; i < level.value.length; i++) {

        /* 根据用户经验确定等级 */
        if (experience >= level.value[i].requiredExperience) {

          /* 初始经验值 */
          levelData.initialExperienceValue = level.value[i].requiredExperience;
          /* 等级不是最高 */
          if (level.value[i + 1] !== undefined) {
            /* 等级不是最高，取下一等级的达成经验值 */
            levelData.onlineExperienceValue = level.value[i + 1].requiredExperience;
            /* 下一级名称 */
            levelData.nextLevelName = level.value[i + 1].levelName;
            /* 获取当前等级范围的下限和上限 */
            const lowerBound = level.value[i].requiredExperience; // 当前等级范围的下限
            const upperBound = level.value[i + 1].requiredExperience; // 下一个等级范围的下限，也即当前等级范围的上限
            /* 计算相对经验值 */
            const relativeExp = experience - lowerBound; // 用户相对于当前等级范围下限的经验值
            /* 计算经验值百分比 */
            levelData.currentExperiencePercentage = (relativeExp / (upperBound - lowerBound)) * 100; // 经验值百分比
          } else {
            /* 等级最高 */
            levelData.onlineExperienceValue = level.value[i].requiredExperience;
            /* 下一级名称 */
            levelData.nextLevelName = '最高等级';
            levelData.currentExperiencePercentage = 100; // 经验值百分比
          }
          /* 当前等级 */
          levelData.level = level.value[i].level;
        }
      }
    } else {
      ElWarning("等级信息获取失败");
    }
  });
}


/* 存储用户信息 */
const userData = ref();

/* 用户等级换算数据 */
const levelData = {
  initialExperienceValue: '',
  onlineExperienceValue: '',
  nextLevelName: '',
  currentExperiencePercentage: 0,
  level: ''
};

/* 获取用户信息和等级信息 */
function getUserAndLevel() {
  getUser().then(() => {
    // 获取等级信息
    getLevel();
  }).catch(error => {
    ElWarning(error);
  });
}

/* 页面启动时调用获取用户数据和等级信息 */
getUserAndLevel();

/* 个人中心编辑、取消、修改按钮 */
const personalCenterOperationButton = ref(true);

/* 切换按钮监听事件 */
function editButton() {
  /* 清空表单和头像信息 */
  clearPersonalInformationFromData();

  /* 赋值到表单中 */
  if (userData.value.avatars !== null) {
    imageUrl.value = userData.value.avatars.avatarPath + userData.value.avatars.fileName;
  }

  if (userData.value.nickname !== null) {
    personalInformationFromData.nickname = userData.value.nickname;
  }

  if (userData.value.gender !== null) {
    personalInformationFromData.gender = userData.value.gender;
  } else {
    personalInformationFromData.gender = '男';
  }

  if (userData.value.birthday !== null) {
    personalInformationFromData.birthday = FormatDate.momentFormatDate(userData.value.birthday);
  }

  if (userData.value.userProfile !== null) {
    personalInformationFromData.profileId = userData.value.userProfile.profileId;
    personalInformationFromData.bio = userData.value.userProfile.bio;
  }

  if (userData.value.profile !== null) {
    personalInformationFromData.lastName = userData.value.profile.lastName;
    personalInformationFromData.firstName = userData.value.profile.firstName;
    personalInformationFromData.region = userData.value.profile.region;
    personalInformationFromData.location = userData.value.profile.location;
    personalInformationFromData.graduationDepartment = userData.value.profile.graduationDepartment;
    personalInformationFromData.major = userData.value.profile.major;
    personalInformationFromData.graduationYear = userData.value.profile.graduationYear;
  }

  /* 切换表单 */
  personalCenterOperationButton.value = false;
}

/* 取消按钮 */
function cancelButton() {

  /* 清空到表单 */
  imageUrl.value = '';
  personalInformationFromData.nickname = '';
  personalInformationFromData.gender = '';
  personalInformationFromData.bio = '';
  personalInformationFromData.lastName = '';
  personalInformationFromData.firstName = '';
  personalInformationFromData.region = '';
  personalInformationFromData.location = '';
  personalInformationFromData.graduationDepartment = '';
  personalInformationFromData.major = '';
  personalInformationFromData.graduationYear = '';
  personalInformationFromData.profileId = '';

  /* 切换显示数据 */
  personalCenterOperationButton.value = true;
}

/* 保存按钮 */
function saveButton() {

  /* 没有上传头像 */
  if (personalInformationFromData.avatars === '') {
    /* 空对象，不能直接赋值为null，后端会识别为字符串，然后没有办法转成File对象而报错 */
    personalInformationFromData.avatars = new File([], '');
  }
  /* 提交FormData */
  const userFormData = new FormData();
  userFormData.append("id", getUserId());
  userFormData.append("avatars", personalInformationFromData.avatars);
  userFormData.append("nickname", personalInformationFromData.nickname);
  userFormData.append("gender", personalInformationFromData.gender);
  userFormData.append("birthday", FormatDate.dateFormat(personalInformationFromData.birthday));
  userFormData.append("profileId", personalInformationFromData.profileId);
  userFormData.append("bio", personalInformationFromData.bio);
  userFormData.append("lastName", personalInformationFromData.lastName);
  userFormData.append("firstName", personalInformationFromData.firstName);
  userFormData.append("region", personalInformationFromData.region);
  userFormData.append("location", personalInformationFromData.location);
  userFormData.append("graduationDepartment", personalInformationFromData.graduationDepartment);
  userFormData.append("major", personalInformationFromData.major);
  userFormData.append("graduationYear", personalInformationFromData.graduationYear);

  put("api/index/user", userFormData, (rs) => {
    if (rs.code === 200) {
      ElSuccess(rs.message);
      /* 清空表单及头像内容 */
      clearPersonalInformationFromData();

      /* 做了缓存，有值不发请求，数据变动需要更新，清空缓存 */
      userData.value = undefined ;

      /* 重新获取用户数据 */
       getUser();

      /* 切换为编辑按钮 */
      personalCenterOperationButton.value = true;
    }
  }, (message) => {
    ElWarning(message);
    personalCenterOperationButton.value = false;
  });

}

/* 个人信息修改FromData */
const personalInformationFromData = reactive({
  avatars: '',
  nickname: '',
  gender: '',
  birthday: '',
  profileId: '',
  bio: '',
  lastName: '',
  firstName: '',
  region: '',
  location: '',
  graduationDepartment: '',
  major: '',
  graduationYear: ''
});

/* 清空个人信息修改表单及头像 */
function clearPersonalInformationFromData() {
  imageUrl.value = '';
  personalInformationFromData.avatars = '';
  personalInformationFromData.nickname = '';
  personalInformationFromData.gender = '';
  personalInformationFromData.birthday = '';
  personalInformationFromData.profileId = '';
  personalInformationFromData.bio = '';
  personalInformationFromData.lastName = '';
  personalInformationFromData.firstName = '';
  personalInformationFromData.region = '';
  personalInformationFromData.location = '';
  personalInformationFromData.graduationDepartment = '';
  personalInformationFromData.major = '';
  personalInformationFromData.graduationYear = '';
}

/* 头像上传图片选择钩子 */
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElWarning('头像图片必须是JPG/PNG格式！')
    return false
  } else if (rawFile.size / 1024 / 1024 > 1) {
    ElWarning('头像图片大小不能超过1MB！')
    return false
  }

  return true
}

/* 文件选择成功的钩子 */
const imageUrl = ref('');
const handleAvatar = (file) => {

  /* 预览图像赋值 */
  imageUrl.value = URL.createObjectURL(file.raw);

  /* 表单内容赋值 */
  personalInformationFromData.avatars = file.raw;
  return file;
}

/* 图片选择放大查看是事件 */
const dialogVisibleImg = ref(false)
const disabled = ref(false)
const handlePictureCardPreviewImg = () => {
  /* 弹出对话框 */
  dialogVisibleImg.value = true;
}

</script>

<template>
  <div id="box">
    <div id="box_top">
      <div id="box_top_content">
        <el-row class="centered" :gutter="20">
          <el-col :span="3">
            <el-image @click="router.push('/frontend-index-home')" :src="'/images/logo/MapleBridg-logo-blue.png'"
                      style="height: 70px; transform: scale(1.4)"
                      alt="logo"/>
          </el-col>
          <el-col :span="11">
            <el-menu
                :default-active="activeIndex"
                mode="horizontal"
                @select="handleSelect"
            >
              <el-menu-item index="1">
                <template #title>
                  <my-icon name="icon-shouye"/>
                  <span>首页</span>
                </template>
              </el-menu-item>
              <el-menu-item index="2">
                <template #title>
                  <my-icon name="icon-fenxiang"/>
                  <span>学术分享</span>
                </template>
              </el-menu-item>
              <el-menu-item index="3">
                <template #title>
                  <my-icon name="icon-xiaoyuanshenghuo"/>
                  <span>校园生活</span>
                </template>
              </el-menu-item>
              <el-menu-item index="4">
                <template #title>
                  <my-icon name="icon-hubanghuzhu"/>
                  <span>互帮互助</span>
                </template>
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :span="8">
            <el-input
                v-model="searchForm.content"
                style="width: 350px; height: 40px;"
                placeholder="输入搜索关键词"
                class="input-with-select"
            >
              <template #append>
                <el-button :icon="Search"/>
              </template>
            </el-input>
          </el-col>
          <el-col :span="2" class="el-dropdown-link">
            <el-dropdown>
              <el-avatar v-if="userData && userData.avatars !== null"
                         :src="userData.avatars.avatarPath + userData.avatars.fileName" id="avatar" shape="square"/>
              <el-avatar v-else id="avatar" shape="square" :size="50" :src="'/images/touxiang-1.jpg'"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="personalCenterDialog">
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item>设置中心</el-dropdown-item>
                  <!--                  <el-dropdown-item>Action 3</el-dropdown-item>-->
                  <!--                  <el-dropdown-item disabled>Action 4</el-dropdown-item>-->
                  <el-dropdown-item divided @click="userLogout">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-dialog v-model="personalCenterDialogVisible" title="个人中心" width="700" top="0.6vh">
              <div class="personalCenter" v-if="userData">
                <div id="top">
                  <div id="left">
                    <p>
                      <el-tag size="small">LV {{ levelData.level }}</el-tag>
                    </p>
                    <p>{{ userData.level.levelName }}</p>
                  </div>
                  <div id="middle">
                    <el-progress
                        show-text
                        text-inside
                        class="progress"
                        :text-inside="true"
                        :striped="true"
                        :striped-flow="true"
                        duration="60"
                        default="经验值"
                        :stroke-width="20"
                        :percentage="levelData.currentExperiencePercentage">
                      {{ userData.experience }}
                    </el-progress>
                    <div id="experienceValue">
                      <div id="left">
                        <span>{{ levelData.initialExperienceValue }}</span>
                      </div>
                      <div id="right">
                        <span>{{ levelData.onlineExperienceValue - 1 }}</span>
                      </div>
                    </div>
                  </div>
                  <div id="right">
                    &nbsp;&nbsp;{{ levelData.nextLevelName }}
                  </div>
                </div>
                <div id="middle">
                  <div id="button">
                    <el-button v-if="personalCenterOperationButton" @click="editButton" id="edit" type="primary" plain>
                      编辑
                    </el-button>
                    <div v-else>
                      <el-button id="cancel" @click="cancelButton" plain>取消</el-button>
                      <el-button id="preserve" @click="saveButton" type="primary" plain>保存</el-button>
                    </div>
                  </div>
                  <div id="show" v-if="personalCenterOperationButton">
                    <div class="content">
                      <p>头像</p>
                      <p>
                        <el-image class="avatars" :src="userData.avatars.avatarPath + userData.avatars.fileName"
                                  v-if="userData.avatars !== null"/>
                        <el-image class="avatars" :src="'/images/touxiang-1.jpg'" v-else/>
                      </p>
                    </div>
                    <div class="content">
                      <p>昵称</p>
                      <p>{{ userData.nickname }}</p>
                    </div>
                    <div class="content">
                      <p>性别</p>
                      <p>{{ userData.gender === null ? "未知" : userData.gender }}</p>
                    </div>
                    <div class="content">
                      <p>生日</p>
                      <p>{{ userData.birthday === null ? "未填写" : FormatDate.showDateFormat(userData.birthday) }}</p>
                    </div>
                    <div class="content" style="height: 100px;display: flex;align-items: center;text-align: left">
                      <p>简介</p>
                      <p>{{ userData.userProfile === null ? "未填写" : userData.userProfile.bio }}</p>
                    </div>
                    <div class="content">
                      <p>姓</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.lastName === null ? "未填写" : userData.profile.lastName
                        }}</p>
                    </div>
                    <div class="content">
                      <p>名</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.firstName === null ? "未填写" : userData.profile.firstName
                        }}</p>
                    </div>
                    <div class="content">
                      <p>来自</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.region === null ? "未填写" : userData.profile.region
                        }}</p>
                    </div>
                    <div class="content">
                      <p>目前所在地</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.location === null ? "未填写" : userData.profile.location
                        }}</p>
                    </div>
                    <div class="content">
                      <p>毕业院系</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.graduationDepartment === null ? "未填写" : userData.profile.graduationDepartment
                        }}</p>
                    </div>
                    <div class="content">
                      <p>专业</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.major === null ? "未填写" : userData.profile.major
                        }}</p>
                    </div>
                    <div class="content">
                      <p>年级</p>
                      <p>
                        {{
                          userData.profile === null || userData.profile.graduationYear === null ? "未填写" : userData.profile.graduationYear
                        }}</p>
                    </div>
                  </div>
                  <div id="from" v-else>
                    <el-form :model="personalInformationFromData">
                      <el-form-item style="text-align: left;display: flex;align-items: center;">
                        头像&nbsp;&nbsp;
                        <el-upload
                            class="avatar-uploader"
                            action="#"
                            v-model="personalInformationFromData.avatars"
                            :on-change="handleAvatar"
                            :before-upload="beforeAvatarUpload"
                            :show-file-list="false"
                            limit="1"
                            style="width: 20%;padding: 0;text-align: center"
                        >
                          <el-image v-if="imageUrl" :src="imageUrl" class="avatar"/>
                          <el-icon v-else class="avatar-uploader-icon">
                            <Plus/>
                          </el-icon>
                        </el-upload>
                        <div style="width: 15%;">
                          &nbsp;&nbsp;
                          <el-button @click="handlePictureCardPreviewImg" round>预览</el-button>
                        </div>
                        <div style="width: 59%;display: flex;align-items: center;justify-content: center">
                          姓&nbsp;&nbsp;
                          <el-input v-model="personalInformationFromData.lastName" style="width: 30%;"
                                    placeholder="姓氏"/>&nbsp;&nbsp;
                          名&nbsp;&nbsp;
                          <el-input v-model="personalInformationFromData.firstName" style="width: 40%"
                                    placeholder="名字"/>
                        </div>
                        <el-dialog title="头像预览" v-model="dialogVisibleImg"
                                   width="700">
                          <el-image w-full :src="imageUrl" style="height: 100%; width: 100%" alt="头像预览"/>
                        </el-dialog>
                      </el-form-item>
                      <el-form-item>
                        昵称&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.nickname" style="width: 67%" maxlength="10"
                                  show-word-limit placeholder="昵称"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;性别&nbsp;&nbsp;&nbsp;&nbsp;
                        <el-radio-group v-model="personalInformationFromData.gender">
                          <el-radio-button label="男" value="男"/>
                          <el-radio-button label="女" value="女"/>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item>
                        生日&nbsp;&nbsp;
                        <el-date-picker
                            v-model="personalInformationFromData.birthday"
                            type="date"
                            format="YYYY/MM/DD"
                            value-format="YYYY-MM-DD"
                            style="width: 90%"
                            placeholder="生日"
                            size="default"
                        />
                      </el-form-item>
                      <el-form-item>
                        简介&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.bio" style="width: 90%" type="textarea"
                                  maxlength="50" show-word-limit placeholder="简介"/>
                      </el-form-item>
                      <el-form-item>
                        来自&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.region" style="width: 90%" placeholder="来自"/>
                      </el-form-item>
                      <el-form-item>
                        目前所在地&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.location" style="width: 83.5%"
                                  placeholder="目前所在地"/>
                      </el-form-item>
                      <el-form-item>
                        毕业院系&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.graduationDepartment" style="width: 85.5%"
                                  placeholder="毕业院系"/>
                      </el-form-item>
                      <el-form-item>
                        专业&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.major" style="width: 90%" placeholder="专业"/>
                      </el-form-item>
                      <el-form-item>
                        年级&nbsp;&nbsp;
                        <el-input v-model="personalInformationFromData.graduationYear" style="width: 90%"
                                  placeholder="年级"/>
                      </el-form-item>
                    </el-form>
                  </div>
                  <!--上次更新时间-->
                  <span
                      style="display: inline-block; margin-top: 10px">上次更新时间：{{
                      userData.updateTime === null ? "暂无更新时间" : FormatDate.showDateTimeFormat(userData.updateTime)
                    }}</span>
                </div>
                <div id="bottom">

                </div>
              </div>
            </el-dialog>
          </el-col>
        </el-row>
      </div>
    </div>
    <div id="box_bottom">
      <div id="box_bottom_content">
        <div id="bottom_content_left">
          <router-view :userInfo="userData" v-slot="{ Component }">
            <!--过渡动画-->
            <transition name="el-fade-in-linear" mode="out-in">
              <div :key="$route.path">
                <component :is="Component"/>
              </div>
            </transition>
          </router-view>
        </div>
        <div id="bottom_content_right">
          <div id="content_right_top">
            <div class="right_top_title">
              <my-icon name="icon-fenxiang"/>
              分享中心
            </div>
            <div id="right_top_content">
              <div id="top_content_text">
                <p>开始分享之旅</p>
                <span>分享校园生活、学术、互帮互助</span>
              </div>
              <div id="top_content_img">
                <el-image style=" height: 80%" :src="'/images/kapibala.jpg'" alt="卡皮巴拉"/>
              </div>
            </div>
            <div id="right_top_button">
              <el-button :icon="Plus" @click="startSharing" style="width: 100%" size="small">
                开&nbsp;始&nbsp;分&nbsp;享
              </el-button>
              <el-dialog v-model="dialogVisible" title="分享中心" width="664" draggable>
                <el-scrollbar height="400px">
                  <el-form :model="postForm" :rules="rule" ref="formRef">
                    <el-form-item prop="typeId" v-if="postTypes">
                      <span>选择分享类型</span>
                      <el-select
                          v-model="postForm.typeId"
                          :value="String(postForm.typeId)"
                          placeholder="选择分享类型"
                      >
                        <el-option
                            v-for="item in postTypes"
                            :key="item.typeId"
                            :label="item.typeName"
                            :value="item.typeId"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item prop="topic">
                      <span>标题</span>
                      <el-input v-model="postForm.topic" maxlength="20" placeholder="输入标题" show-word-limit/>
                    </el-form-item>
                    <el-form-item prop="content">
                      <span>内容</span>
                      <el-input
                          v-model="postForm.content"
                          type="textarea"
                          maxlength="500"
                          placeholder="输入内容"
                          show-word-limit
                          :autosize="{ minRows: 6 , maxRows: 13 }"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-upload
                          v-model:file-list="postForm.fileList"
                          list-type="picture-card"
                          :before-upload="checkFileType"
                          :on-preview="handlePictureCardPreview"
                          :on-remove="handleRemove"
                          :http-request="choiceImages"
                          name="fileList"
                          limit="8"
                      >
                        <el-icon>
                          <Plus/>
                        </el-icon>
                      </el-upload>
                      <el-dialog title="图像预览" v-model="dialogVisible2" width="700">
                        <img w-full :src="dialogImageUrl" style="height: 100%; width: 100%" alt="照片墙"/>
                      </el-dialog>
                    </el-form-item>
                  </el-form>
                </el-scrollbar>
                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="closeSharing">取消</el-button>
                    <el-button type="primary" @click="submitPostForm">
                      发布
                    </el-button>
                  </div>
                </template>
              </el-dialog>
            </div>
          </div>
          <div id="content_right_middle">
            <div class="right_top_title">
              <my-icon name="icon-remensousuo"/>
              热门搜索
            </div>
          </div>
          <div id="content_right_bottom">

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.centered {
  display: flex;
  justify-content: center;
  align-items: center;
}

#box {
  margin: 0;
  padding: 0;
  height: 100vh;
  width: 100vw;
  background-color: #f5f6f9;
}

#box_top {
  height: 70px;
  width: 100%;
  background-color: white;
  box-shadow: 0 2px 2px #999;
  display: flex;
  justify-content: center;
  align-items: center;
}

#box_top_content {
  height: 100%;
  width: 80%;
}

#box_bottom {
  margin-top: 10px;
  height: calc(100vh - 70px - 20px);
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

#box_bottom_content {
  height: 100%;
  width: 70%;
  display: flex;
  //background-color: white;
}

#bottom_content_left {
  height: 100%;
  width: calc(75% - 20px);
  background-color: white;
  margin-right: 10px;
  box-shadow: 1px 2px 3px #999;
}

#bottom_content_right {
  height: 100%;
  width: 25%;
  //background-color: white;
}

#content_right_top {
  height: 20%;
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}

.right_top_title {
  height: 20%;
  width: 100%;
}

#right_top_content {
  height: 55%;
  width: 100%;
  background-color: #f5f6f9;
  display: flex;
}

#top_content_text {
  margin-top: 5px;
  height: 100%;
  width: 68%;
  //display: flex;
  //justify-content: center;
  //align-items: center;
  padding-left: 10px;
}

#top_content_text p {
  margin: 0;
  font-size: 14px;
  padding: 5px 0 0 0;
}

#top_content_text span {
  font-size: 12px;
  color: #999999;
}

#top_content_img {
  height: 100%;
  width: 30%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-right: 8px;
  margin-left: 4px;
}

#right_top_button {
  height: 25%;
  width: 100%;
  margin-top: 10px;
}

#content_right_middle {
  margin-top: 10px;
  margin-bottom: 10px;
  height: calc(51% - 20px);
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}

#content_right_bottom {
  height: 20%;
  width: 90%;
  background-color: white;
  box-shadow: 1px 2px 3px #999;
  padding: 10px;
}

.personalCenter {
  height: 100%;
  width: 100%;
  background-color: white;

  #top {
    display: flex;
    height: 50px;
    width: 100%;

    #left {
      height: 100%;
      width: 30%;
      display: flex;
      -webkit-justify-content: flex-end;
      justify-content: flex-end;

      p {
        margin-right: 8px;

        &:first-child {
          margin-right: 20px;
        }
      }
    }

    #middle {
      height: 100%;
      width: 100%;

      .progress {
        height: 100%;
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
      }

      #experienceValue {
        display: -webkit-flex;
        display: flex;
        -webkit-justify-content: space-between;
        justify-content: space-between;

        #left {
          width: 50%;
          position: relative;

          span {
            position: absolute;
            left: 0;
            top: -10px;
          }
        }

        #right {
          width: 50%; /* 设置父元素宽度 */
          position: relative;

          span {
            position: absolute;
            right: 0;
            top: -10px;
          }
        }
      }


    }

    #right {
      height: 100%;
      width: 20%;
      display: flex;
      align-items: center;
    }

  }

  #middle {
    background-color: white;
    text-align: right;

    #button {
      margin-top: 20px;
      margin-right: 40px;
    }

    #show {

      .content {
        display: flex;
        height: 50px;
        border-bottom: 1px solid #f1f0f0;

        p {
          height: 25px;
          font-size: 16px;
          font-weight: 600;
          display: flex;
          align-items: center;

          &:first-child {
            margin-left: 60px;
            width: 45%;
          }

          &:last-child {
            width: 30%;
            font-size: 16px;
            font-weight: normal;
          }

        }

        &:first-child {
          height: 60px;
        }

        .avatars {
          height: 50px;
        }
      }

    }

    #from {
      margin-top: 15px;
    }
  }
}

.avatar-uploader .avatar {
  width: 100%;
  height: 90px;
  display: block;
}

.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 90px;
  text-align: center;
}
</style>