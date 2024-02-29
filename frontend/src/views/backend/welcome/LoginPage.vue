<script setup>
import {ChatDotRound, Lock, User} from '@element-plus/icons-vue'
import {reactive, ref} from "vue" ;
import {login, validateCaptchaImage} from "@/net/Login";
import router from "@/router";
import {generateUniqueId} from "@/util/UUID" ;
import {ElError, ElWarning} from "@/util/MessageUtil";
import axios from "axios";


/* 表单校验 */
const formRef = ref();

const form = reactive({
  username: '',
  password: '',
  remember: false,
  code: ''
});

/* 验证码Base64字符串 */
const captchaImage = ref("") ;

/* 刷新英文验证码 */
const refreshCaptcha = async () => {
  try {
    const response = await axios.get(`/api/auth/captcha-image/${generateUniqueId()}`, {
      responseType: 'blob', // 设置响应类型为 blob
    });

    if (response.status === 200) {
      captchaImage.value = URL.createObjectURL(new Blob([response.data]));
    } else {
      ElWarning("无法获取验证码图像");
      console.error('Failed to fetch captcha image');
    }
  } catch (error) {
    ElError("获取图像时出错：" + error)
  }
}

/* 页面打开时默认调用 */
refreshCaptcha();


/* 表单判断 */
const rule = {
  username: [
    {
      required: true, message: '请输入账户名/邮箱'
    }
  ],
  password: [
    {
      required: true, message: '请输入登录密码'
    }
  ],
  // code: [
  //   {
  //     required: 4strue, message: '请输入验证码'
  //   }
  // ]
}

/* 用户登录 */
function userLogin() {
  formRef.value.validate((valid) => {
    /* 验证有效 */
    if (valid) {
      /* 验证验证码 */
      validateCaptchaImage(form.code, (rs) => {
        if (rs.code === 200) {
          /* 登录 */
          login(form.username, form.password, form.remember, () => {
            router.push("/backend-index-workbench");
          });
        } else {
          ElWarning(rs.message) ;
        }
        refreshCaptcha() ;
      }, (err) => {
        ElWarning(err) ;
        refreshCaptcha() ;
      });

    }
  });
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="color: #333333; font-size: 35px; font-weight: bold; opacity: .7;letter-spacing: 5px;">登录</div>
      <!--      <div style="font-size: 14px; color: grey">在进入系统之前，请先输入用户名和密码进行登录</div>-->
    </div>
    <div style="margin-top: 70px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username" style="padding: 10px">
          <el-input style="height: 50px" v-model="form.username" maxlength="10" type="text" placeholder="账户名/邮箱"
                    clearable>
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password" style="padding: 10px;">
          <el-input style="height: 50px;" v-model="form.password" maxlength="20" type="password"
                    placeholder="请输入登录密码" clearable show-password>
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code" style="padding: 10px;">
          <el-input style="height: 50px;" v-model="form.code" maxlength="6" type="text" placeholder="请输入验证密码"
                    clearable>
            <template #prefix>
              <el-icon><ChatDotRound /></el-icon>
            </template>
            <template #append>
              <el-image @click="refreshCaptcha" style="display: block" :src="captchaImage" lazy alt="验证码"  />
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left;">
            <el-form-item prop="remember">
              <el-checkbox v-model="form.remember" label="记住我"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-link @click="router.push('/backend-welcome-reset')">忘记密码？</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 40px;">
      <el-button @click="userLogin" style="width: 270px;height: 50px;font-size: 15px" round plain>立即登录</el-button>
    </div>
    <!--    <el-divider>-->
    <!--      <span style="font-size: 13px; color:grey" >没有账号</span>-->
    <!--    </el-divider>-->
    <!--    <el-button @click="router.push('/register')" style="width:270px" type="warning" plain>立即注册</el-button>-->
  </div>
</template>

<style scoped>

/**改变input里的字体颜色*/
/deep/ input::-webkit-input-placeholder {
  color: black;
  font-size: 12px;
  opacity: .5;
}

/**改变input框背景颜色*/
/deep/ .el-input__wrapper {
  background-color: #f5f5f5 !important;
}

/deep/ .el-input__inner {
  background-color: #f5f5f5 !important;
}

</style>