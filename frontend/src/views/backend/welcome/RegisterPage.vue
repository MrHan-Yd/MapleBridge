<script setup>
import {Lock, User, Message, EditPen} from '@element-plus/icons-vue'
import {computed, reactive, ref} from "vue" ;
import router from "@/router";
import {ElMessage} from "element-plus";
import {get, post} from "@/net/NetWork" ;

/* 验证码冷却时间校验 */
const coldTime = ref(0) ;

/* 注册表单校验 */
const formRef = ref() ;

/* 判断邮箱是否正确 */
const isEmailValid = computed(() => /^[\w.-]+@[\w.-]+\.\w+$/.test(form.email)) ;

const form = reactive({
  username: '' ,
  password: '' ,
  password_repeat: '' ,
  email: '' ,
  code: ''
}) ;

/* 验证用户名 */
const validateUserName = (rule, value, callback) => {
  if(value === '') {
    callback(new Error("请输入用户名")) ;
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error("用户名不能包含特殊字符，只能是中/英文")) ;
  } else {
    callback() ;
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

/*表单规则校验*/
const rule = {
  username: [
    {
      /* 用户名校验*/
      validator: validateUserName ,
      /* 触发方式 */
      trigger: ['blur', 'change']
    }
  ] ,
  password: [
    {
      required: true ,
      message: '请输入密码' ,
      trigger: 'blur'
    } ,
    {
      min: 6 ,
      max: 20 ,
      message: '密码的长度必须在6-16个字符之间' ,
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

/* 请求验证码 */
function askCode() {
  if (isEmailValid) {
    /* 可以发送验证码，冷却时间60秒 */
    coldTime.value = 60 ;
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success(`验证码已发送到邮箱:${form.email},请注意查收`) ;
      /* 冷却时间做自减，一秒钟一次 */
      const intervalId = setInterval(() => {coldTime.value --; /* 冷却时间小于0时，暂停 */
        if(coldTime.value <= 0) {
          // 暂停定时器的执行
          clearInterval(intervalId) ;
        }}, 1000) ;

    } , (message) => {
      ElMessage.warning(message) ;
      /* 如果获取验证码的途中出现了问题，直接清空冷却时间 */
      coldTime.value = 0 ;
    }) ;
  } else {
    ElMessage.warning("请输入正确的电子邮件") ;
  }
}

/* 注册 */
function register() {
  formRef.value.validate((valid) => {
    /* 如果说是合法的 */
    if (valid) {
      post('/api/auth/register', {...form}, () => {
        ElMessage.success("注册成功，欢迎加入我们") ;
        router.push('/') ;
      }) ;
    } else {
      ElMessage.warning("请完整填写注册表单内容") ;
    }
  }) ;
}
</script>
<template>
  <div style="text-align:center; margin: 0 20px;">
    <div style="margin-top: 100px;">
      <div style="font-size: 25px; font-weight: bold;">注册新用户</div>
      <div style="font-size:14px; color: grey;">欢迎注册我们的学习平台，请在下方填写相关信息</div>
      <div style="margin-top:50px;">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="username">
            <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="10" style="width:100%">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon><EditPen /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button @click="askCode" :disabled="!isEmailValid || coldTime" type="success">
                  {{coldTime > 0 ? `请稍后${coldTime}秒` : '获取验证码'}}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px;">
        <el-button @click="register" type="warning" plain style="width:270px;">立即注册</el-button>
      </div>
      <div style="margin-top: 20px;">
        <span style="font-size: 14px; line-height:15px; color:green">已有账号？</span>
        <el-link style="translate:0 -1px;" @click="router.push('/')">立即登录</el-link>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>