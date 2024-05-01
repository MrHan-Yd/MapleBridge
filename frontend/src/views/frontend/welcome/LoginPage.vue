<script setup>
import {ChatDotRound, Lock, User} from "@element-plus/icons-vue";
import router from "@/router";
import { EmailType } from '@/enum/EmailType' ;
import { reactive, ref } from "vue";
import {askCode, coldTime, emailConfirm, resetAskCodeAntiShake} from "@/util/CaptchaUtil";
import { ElWarning } from "@/util/MessageUtil";
import { loginFrontend } from "@/net/Login" ;

/* 登录表单 */
const loginForm = reactive({
  username : '' ,
  password : '' ,
  captcha : ''
}) ;

/* 表单校验 */
const formRef = ref();

/* 判断邮箱是否正确 */
let isEmailValid = ref() ;

/* 获取验证码 */
function getCaptcha() {
  if (loginForm.username !== '') {
    isEmailValid = true ;
    askCode(EmailType.LOGIN, loginForm.username) ;
  } else {
    isEmailValid = false ;
    ElWarning("请先输入账户/邮箱再尝试获取验证码") ;
  }
}

/* 登录表单校验 */
const rule = {
  username: [
    {
      required: true, message: "请输入账号/邮箱"
    }
  ],
  password: [
    {
      required: true, message: "请输入密码"
    }
  ],
  code: [
    {
      required: true, message: "请输入邮箱验证码"
    }
  ]
}

/* 登录 */
function login() {
  formRef.value.validate(valid => {
    /* 验证有效 */
    if (valid) {
      /* 验证码验证 */
      emailConfirm(loginForm.username, loginForm.captcha, (rs) =>{
        /* 验证码验证通过 */
        if (rs.code === 200) {
          /* 清除验证码倒计时 */
          resetAskCodeAntiShake() ;

          /* 登录 */
          loginFrontend(loginForm.username, loginForm.password, () => {
            router.push("/frontend-index-home");
          }, (message) => {
            ElWarning(message) ;
          });
        } else {
          ElWarning(rs.message) ;
        }
      }, (err) => {
        ElWarning(err) ;
      }) ;
    }
  })
}
</script>

<template>
    <p>登录</p>
    <span>还没有账户?</span>&nbsp;&nbsp;
    <el-link @click="router.push('/frontend-welcome-register')">注册</el-link>
    <el-form :model="loginForm" :rules="rule" ref="formRef" style="margin-top: 20px;">
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" class="input" type="text" placeholder="账号/邮箱" >
          <template #prefix>
            <el-icon>
              <User/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" class="input" type="password" placeholder="密码" >
          <template #prefix>
            <el-icon>
              <Lock/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input v-model="loginForm.captcha" class="input" type="text" placeholder="验证码" >
          <template #prefix>
            <el-icon><ChatDotRound /></el-icon>
          </template>
          <template #append>
              <el-button @click="getCaptcha" :disabled="coldTime > 0" >
                {{coldTime > 0 ? `请稍后${coldTime}秒` : '获取验证码'}}
              </el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="login" style="width: 100%">登录</el-button>
      </el-form-item>
    </el-form>
    <div id="box">
      <el-link @click="router.push('/frontend-welcome-reset')">忘记密码？</el-link>
    </div>
</template>

<style scoped>
#box {
  text-align: center;
}
p {
  font-size: 28px ;
}
span {
  font-size: 13px;
}
.input {
  height: 40px;
}
</style>