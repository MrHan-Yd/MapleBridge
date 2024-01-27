import {
    defaultFailure,
    internalPost,
    storeAccessToken,
    deleteAccessToken,
    takeAccessToken,
} from '@/net/NetWork' ;
import {ElMessage} from "element-plus";


/* 登录函数 */
function login(username, password, remember, success, failure = defaultFailure) {
    internalPost('/api/auth/login', {
            username: username ,
            password: password
        }, {
            'Content-Type': 'application/x-www-form-urlencoded'
        } ,
        (data) => {
            /* 存储Token */
            storeAccessToken(data.token, remember, data.expire) ;
            ElMessage.success(`登录成功，欢迎${data.username}来到我们的系统`) ;
            success(data) ;
        } , failure) ;
}

/* 退出登录函数 */
function logout(success, failure = defaultFailure){
    /*t退出登录*/
    get('/api/auth/logout', () => {
        /* 删除本地令牌 */
        deleteAccessToken() ;
        ElMessage.success("退出登录成功，欢迎您再次使用！") ;
        success() ;
    }, failure)
}

/* 判断是否登录(判断是否未验证) */
function unauthorized() {
    /*takeAccessToken验证为true，此函数是判断未验证，所以用了非(!)*/
    return !takeAccessToken() ;
}

/* 将定义好的函数暴露出去 */
export {login, logout, unauthorized}