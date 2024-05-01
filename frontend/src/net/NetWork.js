import axios from 'axios' ;
import {ElWarning, ElSuccess, ElError} from "@/util/MessageUtil" ;

const accessAuthItem = "access_token";
/* 默认错误方法 */
const defaultFailure = (message, code, url) => {
    // console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`) ;
    // ElMessage.warning(message) ;
    if (code !== 401) {
        ElWarning(message);
    }
}
/* 默认成功方法 */
const defaultSuccess = (message) => {
    ElSuccess(message);
}

/* 默认异常方法 */
const defaultError = (err) => {
    // ElMessage.warning(err) ;
    ElWarning(err.message);
}
/* 用于保存刷新令牌的 Promise 对象 */
let tokenRefreshPromise = null; //
/* 用于保存等待的请求队列 */
const requestQueue = [];

/* 获取Token */
function takeAccessToken() {
    const str = localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem);
    /* 如果都没有拿到则没有保存，返回null */
    if (!str) {
        return null;
    }
    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str);

    /* 令牌过期时间 */
    let accessTokenExpire = new Date(authObj.accessTokenExpire);
    /* 刷新令牌过期时间 */
    let refreshTokenExpire = new Date(authObj.refreshTokenExpire);
    /* 过期时间小于当前时间(过期) */
    if (accessTokenExpire <= new Date()) {
        /* 刷新token过期则清除用户信息，提示重新登录 */
        if (refreshTokenExpire <= new Date()) {
            /* 删除Token */
            deleteAccessToken();
            // ElMessage.warning("登录状态已过期，请重新登录!") ;
            ElWarning("登录状态已过期，请重新登录!");
            return null;
        } else {
            if (!tokenRefreshPromise) {
                // 如果没有正在进行的刷新令牌请求，则发起刷新令牌的请求
                tokenRefreshPromise = fetchAccessToken(authObj.refreshToken).then(rs => {
                    if (rs.code === 200) {
                        authObj.accessToken = rs.data.token;
                        authObj.accessTokenExpire = rs.data.tokenExpire;
                        /* 判断有没有勾选记住我 */
                        if (authObj.remember) {
                            localStorage.setItem(accessAuthItem, JSON.stringify(authObj));
                        } else {
                            sessionStorage.setItem(accessAuthItem, JSON.stringify(authObj));
                        }
                    } else {
                        /* 刷新失败，删除本地用户信息并提示用户重新登录 */
                        deleteAccessToken();
                        ElWarning(rs.message + "，请重新登录!");
                    }
                    tokenRefreshPromise = null;
                    // 刷新完成后处理等待的请求
                    handleRequestQueue();
                });
            } else {
                // 如果已经有刷新令牌的请求在进行，则将当前请求加入到等待队列中
                requestQueue.push({ type: "accessToken", authObj });
            }
            return null; // 返回 null 表示等待刷新 token
        }
    }
    /* 如果都没有问题(有Token并且没有过期)，则返回Token */
    return authObj.accessToken;
}


/*
handleRequestQueue() 函数的作用是确保在刷新 token 完成后，处理等待队列中的请求 */
function handleRequestQueue() {
    if (requestQueue.length > 0) {
        // 如果等待队列中有请求，则取出第一个请求并处理
        const nextRequest = requestQueue.shift();
        if (nextRequest.type === "accessToken") {
            takeAccessToken(); // 继续处理等待的访问令牌请求
        }
    }
}

/* 刷新令牌 */
function fetchAccessToken(refreshToken) {
    if (!tokenRefreshPromise) {
        // 如果没有正在进行的刷新令牌请求，则发起刷新令牌的请求
        const encodedRefreshToken = encodeURIComponent(refreshToken);
        tokenRefreshPromise = axios.get(`/api/auth/refresh-token/Bearer ${encodedRefreshToken}`)
            .then(response => {
                const data = response.data;
                /* 释放，才能让下一次刷新的人进来 */
                tokenRefreshPromise = null;
                /* 当前等级 */
                handleRequestQueue();
                return data;
            })
            .catch(error => {
                /* 释放，才能让下一次刷新的人进来 */
                tokenRefreshPromise = null;

                ElWarning("令牌刷新失败", error)
                throw error;
            });
    }
    return tokenRefreshPromise;
}

/* 删除Token */
function deleteAccessToken() {
    localStorage.removeItem(accessAuthItem);
    sessionStorage.removeItem(accessAuthItem);
}

/* 获取角色权限 */
function getUserRole() {
    const str = localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem);
    /* 如果都没有拿到则没有保存，返回null */
    if (!str) {
        return null;
    }

    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str);
    return authObj.role.roleName;

}
/* 获取用户名称 */
function getUserAccount() {
    const str = localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem);
    /* 如果都没有拿到则没有保存，返回null */
    if (!str) {
        return null;
    }

    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str);
    return authObj.account;
}

/* 获取登录用户唯一标识 */
function getUserId() {
    const str = localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem);
    /* 如果都没有拿到则没有保存，返回null */
    if (!str) {
        return null;
    }

    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str);
    return authObj.id;
}

/* 获取登录用户名称 */
function getUserName() {
    const str = localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem);
    /* 如果都没有拿到则没有保存，返回null */
    if (!str) {
        return null;
    }

    /* 拿到了封装回authObj */
    const authObj = JSON.parse(str);
    return authObj.account;
}

/* 保存 Token */
function storeAccessToken(token, remember, expire, id, account, refreshToken, refreshTokenExpire, role) {
    const authObj = {
        accessToken: token,
        accessTokenExpire: expire,
        remember: remember,
        id: id,
        account: account,
        refreshToken: refreshToken,
        refreshTokenExpire: refreshTokenExpire,
        role: role
    };
    /* 转成JSON字符串 */
    const str = JSON.stringify(authObj);
    /* 勾选记住我 */
    if (remember) {
        /* 长期存储，关闭浏览器依然存在 */
        localStorage.setItem(accessAuthItem, str);

        /* 不勾选记住我 */
    } else {
        /* 会话存储，关闭浏览器就不存在了 */
        sessionStorage.setItem(accessAuthItem, str);
    }

}

/* 获取请求头 */
function accessHeader() {
    const token = takeAccessToken();
    /* 如果拿到token就返回获取到的token，否则返回空*/
    return token ? {
        'Authorization': `Bearer ${token}`
    } : {};
}
/* 上传请求头 */
function accessHeaderFormData() {
    const token = takeAccessToken();
    /* 如果拿到token就返回获取到的token，否则返回空*/
    return token ? {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'multipart/form-data'
    } : {};
}

/* 内部使用Post请求 */
function internalPost(url, data, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, {
        headers: header
    }).then(({data}) => {
        if (data.code === 200 || data.code === 403) {
            if (data.code === 200) {
                success(data);
            } else {
                success(data.message);
            }
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => error(err));
}

/* 内部使用Put请求 */
function internalPut(url, data, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.put(url, data, {
        headers: header
    }).then(({data}) => {
        if (data.code === 200) {
            success(data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch((err) => {
        if (err.response.data.code === 403) {
            success(err.response);
        } else {
            error(err)
        }
    });
}

/* 内部使用Get请求 */
function internalGet(url, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.get(url, {
        headers: header
    }).then(({data}) => {
        if (data.code === 200 || data.code === 403) {
            success(data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => {
        // console.log(err)
        // ElError(err.response.data.message)
        if (err.response !== undefined) {
            error(err.response.data);
        } else {
            error(err) ;
        }

        // console.log(err.response)
        // if (err.response.data.code === 403) {
        //     success(err.response) ;
        // } else {
        //     error(err)
        // }
    });
}

/* 内部使用delete请求 */
function internalDelete(url, header, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    axios.delete(url, {
        headers: header
    }).then(({data}) => {
        if (data.code === 200) {
            success(data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => error(err));
}

/* 普通get 暴露给外面使用 */
function get(url, success = defaultSuccess, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure);
}
function getAllParameters(url, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    internalGet(url, accessHeader(), success, failure, error);
}

/* 普通post 暴露给外面使用 */
function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader(), success, failure);
}
/* 上传Post请求 */
function postFormData(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeaderFormData(), success, failure);
}

/* 普通put 暴露给外面使用 */
function put(url, data, success = defaultSuccess, failure = defaultFailure) {
    internalPut(url, data, accessHeader(), success, failure);
}

/* 普通delete 暴露给外面使用 */
function delete_(url, success = defaultSuccess, failure = defaultFailure) {
    internalDelete(url, accessHeader(), success, failure);
}


/* 将定义好的函数暴露出去 */
export {
    get,
    post,
    postFormData,
    put,
    delete_,
    defaultFailure,
    internalPost,
    storeAccessToken,
    deleteAccessToken,
    takeAccessToken,
    getUserRole,
    getUserId,
    getUserName,
    getAllParameters,
    getUserAccount
}
