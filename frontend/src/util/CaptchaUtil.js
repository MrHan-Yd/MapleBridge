import { EmailType } from '@/enum/EmailType' ;
import {ElError, ElWarning, ElSuccess} from '@/util/MessageUtil' ;
import { ref } from 'vue' ;
import { get } from "@/net/NetWork" ;


/* 获取验证码 */
function askCode(type, email) {
    if (!type || !email) {
        ElWarning("参数错误：验证码类型和邮箱不能为空");
        return;
    }

    switch (type) {
        case EmailType.RESET :
            askCodeRequest(email, type);
            break;
        case EmailType.REGISTER :
            askCodeRequest(email, type);
            break;
        case EmailType.LOGIN :
            askCodeRequest(email, type);
            break;
        default:
            ElWarning("没有该验证码类型");
    }
}

/* 获取验证码请求 */
function askCodeRequest(email, type) {
    try {
        askCodeAntiShake() ;
        get(`api/auth/ask-code?email=${email}&type=${type}`, (rs) => {
            ElSuccess(rs.message) ;
        });
    } catch (error) {
        resetAskCodeAntiShake()
        ElError("获取验证码失败" + error);
    }
}

/* 验证码冷却时间校验 */
const coldTime = ref(0) ;

/* 验证码倒计时 */
function askCodeAntiShake() {
    /* 可以发送验证码，冷却时间60秒 */
    coldTime.value = 60 ;

    /* 冷却时间做自减，一秒钟一次 */
    const intervalId = setInterval(() => {coldTime.value --; /* 冷却时间小于0时，暂停 */
        if(coldTime.value <= 0) {
            // 暂停定时器的执行
            clearInterval(intervalId) ;
        }}, 1000) ;
}

/* 重置验证码倒计时 */
function resetAskCodeAntiShake() {
    coldTime.value = 0 ;
}

/* 登录验证码验证 */
const emailConfirm = (usernameOrEmail, code, success, failure) => {
    try {
        get(`api/auth/email-confirm?accountOrEmail=${usernameOrEmail}&code=${code}`, (rs) => {
            success(rs) ;
            ElSuccess(rs.message) ;
        });
    } catch (error) {
        failure(error) ;
        ElError("验证码验证失败" + error);
    }
}

export {
    coldTime,
    askCode,
    askCodeAntiShake,
    resetAskCodeAntiShake,
    emailConfirm }