<script setup>
import { ref } from 'vue';
import { get } from '@/net/NetWork' ;
import {ElWarning} from "@/util/MessageUtil";
import BadgeAssembly from "@/components/BadgeAssembly.vue";
import { showDateFormat } from "@/util/FormatData" ;

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
}) ;

/* 是否显示用户信息弹窗 */
const show = ref(false);
/* 展示的用户信息 */
const userInfo = ref();

function showUserInfo() {
  show.value = !show.value;
  if(show.value) {
    get("api/index/user/" + props.user.userId, (rs) => {
      if (rs.code === 200) {
        userInfo.value = rs.data ;
      } else {
        ElWarning(rs.message) ;
      }
    })
  }
}

/* 图片选择放大查看是事件 */
const dialogVisibleImg = ref(false)
const disabled = ref(false)
const imageUrl = ref('')
const handlePictureCardPreviewImg = (url) => {
  imageUrl.value = url;
  /* 弹出对话框 */
  dialogVisibleImg.value = true;
}

</script>

<template>
    <span @click="showUserInfo" style="display: inline-block">{{user.nickname}}</span>
  <el-dialog
      v-if="userInfo"
      v-model="show"
      :title="`用户：${user.nickname}-主页`" width="500"
      :modal="false"
      style="
      height: 82vh;
      overflow-y: scroll;
      "
      top="10vh"
      destroy-on-close
  >
    <div id="box">
      <div id="top">
        <div id="avatar">
          <el-avatar @click="handlePictureCardPreviewImg(userInfo.avatars.avatarPath + userInfo.avatars.fileName)" :size="100" :src="userInfo.avatars.avatarPath + userInfo.avatars.fileName"/>
        </div>
        <el-dialog title="头像预览" v-model="dialogVisibleImg"
                   width="700">
          <el-image w-full :src="imageUrl" style="height: 100%; width: 100%" alt="头像预览"/>
        </el-dialog>
      </div>
      <div id="bottom">
        <div id="userInfo">
          <div id="nickname">
            <p>{{user.nickname}}</p>
            <el-tag size="small" style="margin-right: 10px;margin-left: 5px;">LV {{user.level}}</el-tag>
            <badge-assembly :level="user.level" :size="30" :level-name="user.levelName"/>
          </div>
          <div id="otherInformation">
            <ul>
              <li>
                <p>简介</p>
                <p>
                  <el-popover
                      title="简介"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      offset="-450"
                      :content="userInfo.userProfile.bio"
                  >
                    <template #reference>
                      {{userInfo.userProfile.bio}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>姓名</p>
                <p>
                  <el-popover
                      title="姓名"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="'姓：'+userInfo.profile.lastName+'&nbsp;&nbsp;名：'+userInfo.profile.firstName"
                  >
                    <template #reference>
                      {{userInfo.profile.lastName}}-{{userInfo.profile.firstName}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>性别</p>
                <p>{{userInfo.gender}}</p>
              </li>
              <li>
                <p>生日</p>
                <p>{{showDateFormat(userInfo.birthday)}}</p>
              </li>
              <li>
                <p>来自</p>
                <p>
                  <el-popover
                      title="来自"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="userInfo.profile.region"
                  >
                    <template #reference>
                      {{userInfo.profile.region}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>现居</p>
                <p>
                  <el-popover
                      title="现居"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="userInfo.profile.location"
                  >
                    <template #reference>
                      {{userInfo.profile.location}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>年级</p>
                <p>
                  <el-popover
                      title="年级"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="userInfo.profile.graduationYear"
                  >
                    <template #reference>
                      {{userInfo.profile.graduationYear}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>学院</p>
                <p>
                  <el-popover
                      title="学院"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="userInfo.profile.graduationDepartment"
                  >
                    <template #reference>
                      {{userInfo.profile.graduationDepartment}}
                    </template>
                  </el-popover>
                </p>
              </li>
              <li>
                <p>专业</p>
                <p>
                  <el-popover
                      title="专业"
                      placement="right"
                      :width="200"
                      trigger="hover"
                      :content="userInfo.profile.major"
                  >
                    <template #reference>
                      {{userInfo.profile.major}}
                    </template>
                  </el-popover>
                </p>
              </li>
            </ul>
          </div>
<!--          <p style="text-align: right;margin-right: 15px;color: #999;padding-bottom: 20px;">更新时间: {{showDateFormat(userInfo.updateTime)}}</p>-->
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
#box {
  height: 80vh;
  width: 100%;
  z-index: 999;

  #top {
    height: 20% ;
    width: 100% ;
    border-radius: 100px 100px 0 0;
    position: relative;
    background:-webkit-linear-gradient(top,skyblue,lightblue);

    #avatar {
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      height: 100px;
      width: 100px;
      border-radius: 50%;
      background-color: whitesmoke;
    }


  }

  #bottom {
    #userInfo {
      height: 500px;
      width: 100%;
      background:-webkit-linear-gradient(top,lightblue,white);

      #nickname {
        display: flex;
        justify-content: center;
        align-items: center;

        p {
          font-size: 20px;
        }
      }

      #otherInformation {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;

        ul {
          margin: 0;
          padding: 0;
          width: 100%;
          list-style-type: none ;

          li {
            text-align: center;
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            //border-top: 1px solid black;

            p:first-child {
              margin-right: 20px;
              font-size: 16px;
            }
            p:last-child {
              font-size: 16px;
              overflow: hidden; /* 隐藏超出容器的内容 */
              white-space: nowrap; /* 防止文本换行 */
              text-overflow: ellipsis; /* 用省略号表示被截断的文本 */
              width: 25%;
            }
          }
        }
      }
    }
  }
}
</style>