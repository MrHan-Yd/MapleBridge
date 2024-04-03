import axios from 'axios' ;
import {ElWarning, ElSuccess, ElError} from "@/util/MessageUtil" ;

const accessAuthItem = "access_token";
/* 默认错误方法 */
const defaultFailure = (message, code, url) => {
    // console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`) ;
    // ElMessage.warning(message) ;
    ElWarning(message);
}
/* 默认成功方法 */
const defaultSuccess = (message) => {
    ElSuccess(message);
}

/* 默认异常方法 */
const defaultError = (err) => {
    // ElMessage.warning(err) ;
    ElWarning(err);
}

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
            /* 使用刷新令牌刷新令牌 */
            fetchAccessToken(authObj.refreshToken).then(rs => {
                /* 刷新成功 */
                if (rs.code === 200) {
                    /* 新令牌替换掉原来的令牌 */
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
            })
        }
    }
    /* 如果都没有问题(有Token并且没有过期)，则返回Token */
    return authObj.accessToken;
}


let fetchPromise = null;

/* 刷新令牌 */
function fetchAccessToken(refreshToken) {
    /* 如果有请求正在进行，则不进入此项 */
    if (!fetchPromise) {
        /* 登录颁发的刷新令牌 */
        const encodedRefreshToken = encodeURIComponent(refreshToken);
        /* 发起请求 */
        fetchPromise = axios.get(`/api/auth/refresh-token/Bearer ${encodedRefreshToken}`)
            .then(response => {
                const data = response.data;
                /* 置空表示当前请求完成，可以发起新的请求 */
                fetchPromise = null;
                /* 返回数据 */
                return data;
            })
            .catch(error => {
                fetchPromise = null;
                console.warn('令牌刷新失败', error);
                throw error;
            });
    }

    return fetchPromise;
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
        ElError(err.response.data.message)
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
    getUserName
}
