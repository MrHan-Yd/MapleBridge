<script setup>
import {computed, reactive, ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {get, post, put} from "@/net/NetWork";
import {ElMessage} from "element-plus";
import router from "@/router";
import {ElSuccess, ElWarning} from '@/util/MessageUtil' ;

/* 步骤条校验 */
const active = ref(0) ;

/* 重置密码表单校验 */
const formRef = ref() ;

/* 电子邮件表单 */
const form = reactive({
  email: '' ,
  code: '' ,
  password: '' ,
  password_repeat: ''
}) ;

/* 验证码冷却时间校验 */
const coldTime = ref(0) ;

/* 判断邮箱是否正确 */
const isEmailValid = computed(() => /^[\w.-]+@[\w.-]+\.\w+$/.test(form.email)) ;

/* 请求验证码 */
function askCode() {
  if (isEmailValid) {
    /* 可以发送验证码，冷却时间60秒 */
    coldTime.value = 60 ;
    get(`/api/auth/ask-code?email=${form.email}&type=reset`, () => {
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
        router.push('/backend-welcome-login') ;
      })
    }
  }) ;
}

</script>

<template>
  <div style="text-align:center;">
    <div style="margin-top: 30px;">
      <el-steps :active="active" align-center>
        <el-step title="验证电子邮件" />
        <el-step title="重新设定密码" />
      </el-steps>
    </div>
    <div style="margin: 0 20px" v-if="active === 0">
      <div style="margin-top: 80px;">
        <div style="font-size: 25px; font-weight: bold;">重置密码</div>
        <div style="font-size:14px;color:grey;">请输入需要重置密码的电子邮件地址</div>
      </div>
      <div style="margin-top:50px;">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item style="padding: 10px">
            <el-input style="height: 50px" v-model="form.email" type="text" placeholder="电子邮件地址">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item >
          <el-form-item prop="code" style="padding: 10px">
            <el-row :gutter="10" style="width:100%">
              <el-col :span="17">
                <el-input style="height: 50px" v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon><EditPen /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                  <el-button style="height: 50px" @click="askCode" :disabled="!isEmailValid || coldTime > 0" type="success">
                    {{coldTime > 0 ? `请稍后${coldTime}秒` : '获取验证码'}}
                  </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px;">
        <el-button @click="confirmReset" round plain style="width:270px; height: 50px;font-size: 15px">开始重置密码</el-button>
      </div>
    </div>
    <div style="margin: 0 20px" v-if="active === 1">
      <div style="margin-top: 80px;">
        <div style="font-size: 25px; font-weight: bold;">重置密码</div>
        <div style="font-size:14px;color:grey;">请填写你的新密码，务必牢记，防止丢失</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="password" style="padding: 10px">
            <el-input style="height: 50px" v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_repeat" style="padding: 10px">
            <el-input style="height: 50px" v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px;">
        <el-button  @click="doReset" style="width: 270px; height: 50px;font-size: 15px" type="success" round plain>立即重置密码</el-button>
      </div>
    </div>
  </div>
</template>
<style scoped>
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