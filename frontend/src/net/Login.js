import {
    defaultFailure,
    internalPost,
    storeAccessToken,
    deleteAccessToken,
    takeAccessToken,
    get
} from '@/net/NetWork' ;
import {ElMessage} from "element-plus";
import {ElSuccess} from "@/util/MessageUtil";
import {getUniqueId} from '@/util/UUID' ;


/* 验证英文验证码 */
function validateCaptchaImage(code, success, failure) {
    get("/api/auth/validate-captcha/" + code + "-_-" + getUniqueId(),
        (rs) => {
            success(rs);
        }, (err) => {
            failure(err);
        });
}

/* 登录函数 */
function login(username, password, remember, success, failure = defaultFailure) {
    internalPost('/api/auth/login', {
            username: username,
            password: password
        }, {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        (rs) => {
            /* 存储Token */
            storeAccessToken(rs.data.accessToken, remember, rs.data.accessTokenExpire, rs.data.id, rs.data.account, rs.data.refreshToken, rs.data.refreshTokenExpire, rs.data.role);
            ElMessage.success("登录成功");
            ElSuccess(`欢迎${rs.data.account}使用校友会管理平台`)
            success(rs);
        }, failure);
}

/* 退出登录函数 */
function logout(success, failure = defaultFailure) {
    /*退出登录*/
    get('/api/auth/logout', () => {
        /* 删除本地令牌 */
        deleteAccessToken();
        ElSuccess("退出登录成功，欢迎您再次使用！");
        success();
    }, failure)
}

/* 判断是否登录(判断是否未验证) */
function unauthorized() {
    /*takeAccessToken验证为true，此函数是判断未验证，所以用了非(!)*/
    return !takeAccessToken();
}

/* 将定义好的函数暴露出去 */
export {login, logout, unauthorized, validateCaptchaImage}