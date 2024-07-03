import axios from 'axios';
import {refreshToken, isRefresh} from '@/net/RefreshToken';
import {ElSuccess, ElWarning} from "@/util/MessageUtil";
import {unauthorized} from "@/net/Login";

let accessAuthItem = "user";
let tokenAuth = "token";
let refreshTokenAuth = "refreshToken";

const http = axios.create({
    baseURL: 'http://localhost:9999/',
    //baseURL: 'https://hanyongding.xyz:9999/',
    timeout: 5000,
    headers: {
        Authorization: `Bearer ${getToken()}`
    }
});


/* 请求拦截器 */
http.interceptors.request.use( async (config) => {
        if(!!config.__isLogin) {
            config.headers["Content-Type"] = 'application/x-www-form-urlencoded';
        } else {
            config.headers["Content-Type"] = isFromData(config) ? 'application/json' : 'multipart/form-data';
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

function isFromData(config) {
    return !!config.__isFromData;
}

/* 响应拦截器 */
http.interceptors.response.use(async (response) => {
    if (response.headers.authorization) {
        const token = response.headers.authorization;
        setToken(token);
        http.defaults.headers.Authorization = `Bearer ${token}`;
    }

    if (response.headers.refreshtoken) {
        const refreshToken = response.headers.refreshtoken;
        setRefreshToken(refreshToken);
    }

    /* 没有权限并且不是刷新token的情况下， 未登录页不需要刷新 */
    if (response.data.code === 401 && !isRefresh(response.config) && !unauthorized()) {
        /* 刷新token */
        const isSuccess = await refreshToken();

        /* 刷新成功 */
        if (isSuccess) {
            /* 有新的token，重新请求 */
            response.config.headers.Authorization = `Bearer ${getToken()}`;
            return await http.request(response.config);
        } else {
            /* 刷新失败，清除token */
            deleteAccessToken();
        }

    }
    return response;
});

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

/* 删除Token */
function deleteAccessToken() {
    localStorage.removeItem(accessAuthItem);
    sessionStorage.removeItem(accessAuthItem);
    localStorage.removeItem(tokenAuth);
    sessionStorage.removeItem(tokenAuth);
    localStorage.removeItem(refreshTokenAuth);
    sessionStorage.removeItem(refreshTokenAuth);
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

/* 是否记住 */
function setRemember(remember) {
    const authObj = {
        remember: remember
    }

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

/* 保存 Token */
function storeAccessToken(remember, id, account, role) {
    const authObj = {
        remember: remember,
        id: id,
        account: account,
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

/* 保存 Token */
function setToken(token) {
    if (isRemember()) {
        /* 长期存储，关闭浏览器依然存在 */
        localStorage.setItem(tokenAuth, token);
    } else {
        /* 会话存储，关闭浏览器就不存在了 */
        sessionStorage.setItem(tokenAuth, token);
    }
}

/* 保存刷新token */
function setRefreshToken(refreshToken) {
    if (isRemember()) {
        /* 长期存储，关闭浏览器依然存在 */
        localStorage.setItem(refreshTokenAuth, refreshToken);
    } else {
        /* 会话存储，关闭浏览器就不存在了 */
        sessionStorage.setItem(refreshTokenAuth, refreshToken);
    }
}

/* 是否记住我 */
function isRemember() {
    const str = JSON.parse(localStorage.getItem(accessAuthItem) || sessionStorage.getItem(accessAuthItem));
    return str ? !!str.remember : false;
}
/* 获取令牌 */
function getToken() {
    const str = localStorage.getItem(tokenAuth) || sessionStorage.getItem(tokenAuth) ;
    if (!str) {
        return null ;
    }
    /* 返回token */
    return str;
}

/* 获取刷新令牌 */
function getRefreshToken() {
    const str = localStorage.getItem(refreshTokenAuth) || sessionStorage.getItem(refreshTokenAuth) ;
    if (!str) {
        return null ;
    }
    /* 返回刷新token */
    return str;
}

/* 内部使用Post请求 */
function internalPost(url, data, header = true, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    http.post(url, data, header === 'login' ? { __isLogin : true} : isData(header))
        .then(({data}) => {
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
function internalPut(url, data, header = true, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    http.put(url, data, isData(header)).then(({data}) => {
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
function internalGet(url, dataArray, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    let apiURI = url;

    // 如果 jsonData 存在且是数组，则作为请求体传递
    if (dataArray && Array.isArray(dataArray) && dataArray.length > 0) {
        // 构建 DELETE 请求的 URL，将 jsonData 中的 ID 列表作为查询参数传递给后端
        apiURI = `${url}?ids=${dataArray.join(",")}`;
    }


    http.get(apiURI).then(({data}) => {
        if (data.code === 200 || data.code === 403) {
            success(data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => {
        if (err.response !== undefined) {
            error(err.response.data);
        } else {
            error(err);
        }

    });
}

/* 内部使用delete请求 */
function internalDelete(url, dataArray, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    let apiURI = url;

    // 如果 jsonData 存在且是数组，则作为请求体传递
    if (dataArray && Array.isArray(dataArray) && dataArray.length > 0) {
        // 构建 DELETE 请求的 URL，将 jsonData 中的 ID 列表作为查询参数传递给后端
        apiURI = `${url}?ids=${dataArray.join(",")}`;
    }

    http.delete(apiURI).then(({data}) => {
        if (data.code === 200) {
            success(data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => error(err));
}

/* 普通get 暴露给外面使用 */
function get(url, success = defaultSuccess, failure = defaultFailure) {
    internalGet(url,"", success, failure);
}
function getAllParameters(url, success = defaultSuccess, failure = defaultFailure, error = defaultError) {
    internalGet(url,"", success, failure, error);
}

/* 批量获取 */
function get_batch(url, data, success = defaultSuccess, failure = defaultFailure) {
    internalGet(url, data, success, failure);
}

/* 普通post 暴露给外面使用 */
function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data,true, success, failure);
}
/* 上传Post请求 */
function postFormData(url, data, success, failure = defaultFailure) {
    internalPost(url, data, false, success, failure);
}

/* 普通put 暴露给外面使用 */
function put(url, data, success = defaultSuccess, failure = defaultFailure) {
    internalPut(url, data,  true, success, failure);
}

/* 上传Put请求 */
function putFormData(url, data, success, failure = defaultFailure) {
    internalPut(url, data, false, success, failure);
}

/* 普通delete 暴露给外面使用 */
function delete_(url, success = defaultSuccess, failure = defaultFailure) {
    internalDelete(url, "", success, failure);
}

/* 批量删除 */
function delete_batch(url, data, success = defaultSuccess, failure = defaultFailure) {
    internalDelete(url, data, success, failure);
}

function isData(header) {
    return header ? {
        __isFromData: true
    } : {
        __isFromData: false
    }
}


/* 将定义好的函数暴露出去 */
export {
    get,
    get_batch,
    post,
    postFormData,
    put,
    putFormData,
    delete_,
    delete_batch,
    defaultFailure,
    internalPost,
    storeAccessToken,
    deleteAccessToken,
    getUserRole,
    getUserId,
    getUserName,
    getAllParameters,
    getUserAccount,
    getToken,
    setToken,
    setRefreshToken,
    getRefreshToken,
    internalGet,
    setRemember
}


export default http;