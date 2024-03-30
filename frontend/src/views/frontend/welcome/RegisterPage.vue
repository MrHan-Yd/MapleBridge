<script setup>
import {ChatDotRound, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {EmailType} from '@/enum/EmailType' ;
import {askCode, coldTime, resetAskCodeAntiShake} from "@/util/CaptchaUtil";
import {computed, reactive, ref} from "vue";
import {ElWarning, ElSuccess} from "@/util/MessageUtil";
import {post} from "@/net/NetWork" ;

const form = reactive({
  account: '',
  email: '',
  code: ''
});
/* 注册表单校验 */
const formRef = ref();

/* 判断邮箱是否正确 */
const isEmailValid = computed(() => /^[\w.-]+@[\w.-]+\.\w+$/.test(form.email)) ;

const rule = {
  account: [
    {
      required: true, message: '请输入账号'
    }
  ],
  email: [
    {
      required: true, message: '请输入邮箱'
    }
  ],
  code: [
    {
      required: true, message: '请输入邮箱验证码'
    }
  ]
}

/* 获取验证码 */
function getCaptcha() {
  if (isEmailValid) {
    askCode(EmailType.REGISTER, form.email);
  } else {
    ElWarning("请输入正确的电子邮件");
  }
}

/* 注册 */
function register() {
  formRef.value.validate(valid => {
    /* 验证有效 */
    if (valid) {
      /* 注册 */
      post("api/auth/register", {...form}, (rs) => {
        if (rs.code === 200) {
          /* 清除验证码倒计时 */
          resetAskCodeAntiShake() ;

          /* 注册成功 */
          ElSuccess("注册成功，正在为您跳转登录页");
          router.push("/frontend-welcome-login");
        } else {
          ElWarning(rs.message);
        }
      })
    }
  })
}

</script>

<template>
  <p>注册</p>
  <span>已有账号?</span>&nbsp;&nbsp;
  <el-link @click="router.push('/frontend-welcome-login')">登录</el-link>
  <el-form :model="form" :rules="rule" ref="formRef" style="margin-top: 20px;">
    <el-form-item prop="account">
      <el-input v-model="form.account" class="input" type="text" placeholder="账号">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="email">
      <el-input v-model="form.email" class="input" type="text" placeholder="邮箱">
        <template #prefix>
          <el-icon>
            <Message/>
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="code">
      <el-input v-model="form.code" class="input" type="text" placeholder="验证码">
        <template #prefix>
          <el-icon>
            <ChatDotRound/>
          </el-icon>
        </template>
        <template #append>
          <el-button @click="getCaptcha" :disabled="!isEmailValid || coldTime">
            {{ coldTime > 0 ? `请稍后${coldTime}秒` : '获取验证码' }}
          </el-button>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="register" style="width: 100%">注册</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

p {
  font-size: 28px;
}

span {
  font-size: 13px;
}

.input {
  height: 40px;
}
</style>