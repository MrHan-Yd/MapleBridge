<script setup>
import {Back, ChatDotRound, Lock, User} from "@element-plus/icons-vue";
import router from "@/router";
import {computed, reactive, ref} from "vue";
import {askCode, coldTime, resetAskCodeAntiShake} from '@/util/CaptchaUtil' ;
import { EmailType } from '@/enum/EmailType' ;
import {ElWarning, ElSuccess} from "@/util/MessageUtil";
import {get, put} from "@/net/NetWork";


/* 电子邮件表单 */
const form = reactive({
  email: '' ,
  code: '' ,
  password: '' ,
  password_repeat: ''
}) ;

/* 步骤条校验 */
const active = ref(0) ;

/* 判断邮箱是否正确 */
const isEmailValid = computed(() => /^[\w.-]+@[\w.-]+\.\w+$/.test(form.email)) ;


/* 获取验证码 */
function getCaptcha() {
  console.log(isEmailValid) ;
  if (isEmailValid) {
    askCode(EmailType.RESET, form.email);
  } else {
    ElWarning("请输入正确的电子邮件");
  }
}

/* 密码校验 */
const validatePassword = (rule, value, callback) => {
  if(value === '') {
    callback(new Error("请再次输入密码")) ;
  } else if(value !== form.password) {
    callback(new Error("两次输入的密码不一致")) ;
  } else {
    callback() ;
  }
}

/* 重置密码表单校验 */
const formRef = ref() ;

/*表单规则校验*/
const rules = {
  password: [
    {
      required: true ,
      message: '请输入密码' ,
      trigger: 'blur'
    } ,
    {
      min: 6 ,
      max: 20 ,
      message: '密码的长度必须在6-20个字符之间' ,
      trigger: ['blur', 'change']
    }
  ] ,
  password_repeat: [
    {
      /* 密码一致校验 */
      validator: validatePassword ,
      /* 触发方式 */
      trigger: ['blur', 'change']
    }
  ] ,
  email: [
    {
      required: true ,
      message: '请输入邮件地址' ,
      trigger: ['blur', 'change']
    } ,
    {
      type: 'email' ,
      message: '请输入合法的电子邮件地址' ,
      trigger: ['blur', 'change']
    }
  ] ,
  code: [
    {
      required: true ,
      message: '请输入获取的验证码' ,
      trigger: ['blur', 'change']
    }
  ]
}

/* 第一步：验证验证码是否正确 */
function confirmReset() {
  formRef.value.validate((valid) => {
    if(valid) {
      get('/api/auth/reset-confirm?email=' + form.email + "&code=" + form.code, (rs) => {
        if (rs.code === 200) {
          /* 清除验证码获取cd */
          resetAskCodeAntiShake() ;

          /* 验证码正确，步骤条推到第二步 */
          active.value++
        } else {
          ElWarning(rs.message) ;
        }
      }) ;
    }
  }) ;
}

/* 第二步：重置密码 */
function doReset() {
  formRef.value.validate((valid) => {
    if(valid) {
      put('/api/auth/reset-password', {...form}, () => {
        ElSuccess("密码重置成功，请重新登录") ;
        router.push('/frontend-welcome-login') ;
      })
    }
  }) ;
}

</script>

<template>
  <el-link :icon="Back" @click="router.push('/frontend-welcome-login')">返回</el-link>
  <p>忘记密码</p>
  <el-steps :active="active" align-center>
    <el-step title="验证电子邮件" />
    <el-step title="重新设定密码" />
  </el-steps>
  <el-form :model="form" :rules="rules" ref="formRef" style="margin-top: 20px;" v-if="active === 0">
    <el-form-item>
      <el-input v-model="form.email" class="input" type="text" placeholder="邮箱" >
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-input v-model="form.code" class="input" type="text" @click="getCaptcha" placeholder="验证码" >
        <template #prefix>
          <el-icon><ChatDotRound /></el-icon>
        </template>
        <template #append>
          <el-button style="height: 50px" @click="getCaptcha" :disabled="!isEmailValid || coldTime" type="success">
            {{coldTime > 0 ? `请稍后${coldTime}秒` : '获取验证码'}}
          </el-button>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="confirmReset" style="width: 100%">验证</el-button>
    </el-form-item>
  </el-form>

  <el-form :model="form" :rules="rules" ref="formRef" style="margin-top: 20px;" v-if="active === 1">
    <el-form-item prop="password">
      <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="password_repeat">
      <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复输入密码">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button  @click="doReset" style="width: 100%" type="success" plain>立即重置密码</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
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