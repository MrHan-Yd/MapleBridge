import {
    defaultFailure,
    storeAccessToken,
    deleteAccessToken,
    get, getToken, setRemember, internalPost
} from '@/net/http' ;
import {ElMessage} from "element-plus";
import {ElSuccess, ElWarning} from "@/util/MessageUtil";
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
    /**
     * 待解决： 登录后勾选记住我，拦截器会在响应时存入token，因前端登录逻辑是登录成功后才存入，
     * 导致，哪怕勾选，由于还没有响应，拦截器已经存入了，所以默认存入会话中，
     * 需要修复！！！！！！
     */
    /* 先存储记住状态 */
    setRemember(remember) ;

    internalPost('/api/auth/login', {
            username: username,
            password: password
        },
        'login',
        (rs) => {

            /* 存储Token */
            storeAccessToken(remember, rs.data.id, rs.data.account, rs.data.role);
            ElMessage.success("登录成功");
            ElSuccess(`欢迎${rs.data.account}使用校友会管理平台`)
            success(rs);
        }, (message) => {
            /* 清空本地信息 */
            deleteAccessToken();
            failure(message);
        });
}

function loginFrontend(username, password, success, failure = defaultFailure) {

    /* 先存储记住状态 */
    setRemember(true) ;

    internalPost("/api/auth/login", {
        username: username,
        password: password
    }, "login",(rs) => {
        /* 存储Token, 前端登录，默认勾选记住我，也就是长时间保存用户令牌 */
        storeAccessToken(true, rs.data.id, rs.data.account, rs.data.role);
        ElMessage.success("登录成功");
        success(rs);
    }, (message) => {
        /* 登录失败，提示错误信息 */
        ElWarning(message);
        /* 清空本地信息 */
        deleteAccessToken();
        failure(message);
    })
    // post('/api/auth/login', {
    //         username: username,
    //         password: password
    //     },{
    //         'Content-Type': 'application/x-www-form-urlencoded'
    //     },
    //     (rs) => {
    //         /* 存储Token, 前端登录，默认勾选记住我，也就是长时间保存用户令牌 */
    //         storeAccessToken(true, rs.data.id, rs.data.account, rs.data.role);
    //         ElMessage.success("登录成功");
    //         success(rs);
    //     }, (message) => {
    //         /* 登录失败，提示错误信息 */
    //         ElWarning(message);
    //         /* 清空本地信息 */
    //         deleteAccessToken();
    //         failure(message);
    //     });
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
    /*验证为true，此函数是判断未验证，所以用了非(!)*/
    return !getToken();
}

/* 将定义好的函数暴露出去 */
export {login, logout, unauthorized, validateCaptchaImage, loginFrontend}