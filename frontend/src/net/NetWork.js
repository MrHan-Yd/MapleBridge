import axios from 'axios' ;
import {warn} from "vue";
import {ElMessage} from "element-plus";
import {ElWarning, ElSuccess} from "@/util/MessageUtil" ;

const authItemName  = "access_token"
/* 默认错误方法 */
const defaultFailure = (message, code, url) => {
    // console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`) ;
    // ElMessage.warning(message) ;
    ElWarning(message) ;
}
/* 默认成功方法 */
const defaultSuccess = (message) => {
    ElSuccess(message) ;
}

/* 默认异常方法 */
const defaultError = (err) => {
    // ElMessage.warning(err) ;
    ElWarning(err) ;
}

/* 获取Token */
function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName) ;
    /* 如果都没有拿到则没有保存，返回null */
    if(!str) {
        return null ;
    }
    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str) ;
    /* 过期时间小于当前时间(过期) */
    if(authObj.expire <= new Date()) {
        /* 删除Token */
        deleteAccessToken() ;
        // ElMessage.warning("登录状态已过期，请重新登录!") ;
        ElWarning("登录状态已过期，请重新登录!") ;
        return null ;
    }
    /* 如果都没有问题(有Token并且没有过期)，则返回Token */
    return authObj.token ;
}

/* 删除Token */
function deleteAccessToken() {
    localStorage.removeItem(authItemName) ;
    sessionStorage.removeItem(authItemName) ;
}

/* 保存 Token */
function storeAccessToken(token, remember, expire) {
    const authObj = {
        token: token ,
        expire: expire
    } ;
    /* 转成JSON字符串 */
    const str = JSON.stringify(authObj) ;
    /* 勾选记住我 */
    if (remember) {
        /* 长期存储，关闭浏览器依然存在 */
        localStorage.setItem(authItemName, str) ;

    /* 不勾选记住我 */
    } else {
        /* 会话存储，关闭浏览器就不存在了 */
        sessionStorage.setItem(authItemName, str) ;
    }

}

/* 获取请求头 */
function accessHeader() {
    const token = takeAccessToken() ;
    /* 如果拿到token就返回获取到的token，否则返回空*/
    return token ? {
        'Authorization': `Bearer ${token}`
    } : {} ;
}

/* 内部使用Post请求 */
function internalPost(url, data, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, {
        headers: header
    }).then(({data}) => {
        if(data.code === 200 || data.code === 403) {
            success(data.message) ;
        } else {
            failure(data.message, data.code, url) ;
        }
    }).catch(err => error(err)) ;
}
/* 内部使用Put请求 */
function internalPut(url, data, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.put(url, data, {
        headers: header
    }).then(({data}) => {
        if(data.code === 200) {
            success(data) ;
        } else {
            failure(data.message, data.code, url) ;
        }
    }).catch((err) => {
        if (err.response.data.code === 403) {
            success(err.response) ;
        } else {
            error(err)
        }
    }) ;
}

/* 内部使用Get请求 */
function internalGet(url, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.get(url, {
        headers: header
    }).then(({data}) => {
        if(data.code === 200 || data.code === 403) {
            success(data) ;
        } else {
            failure(data.message, data.code, url) ;
        }
    }).catch(err => {
        if (err.response.data.code === 403) {
            success(err.response) ;
        } else {
            error(err)
        }
    }) ;
}
/* 内部使用delete请求 */
function internalDelete(url, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.delete(url, {
        headers: header
    }).then(({data}) => {
        if(data.code === 200) {
            success(data) ;
        } else {
            failure(data.message, data.code, url) ;
        }
    }).catch(err => error(err)) ;
}

/* 普通get 暴露给外面使用 */
function get(url, success = defaultSuccess, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure) ;
}

/* 普通post 暴露给外面使用 */
function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader(), success, failure) ;
}

/* 普通put 暴露给外面使用 */
function put(url, data, success = defaultSuccess, failure = defaultFailure) {
    internalPut(url, data, accessHeader(), success, failure) ;
}

/* 普通delete 暴露给外面使用 */
function delete_(url, success = defaultSuccess, failure = defaultFailure) {
    internalDelete(url, accessHeader(), success, failure) ;
}


/* 将定义好的函数暴露出去 */
export {
    get,
    post,
    put,
    delete_,
    defaultFailure,
    internalPost,
    storeAccessToken,
    deleteAccessToken,
    takeAccessToken
}
