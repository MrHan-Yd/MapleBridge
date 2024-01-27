import {ElMessageBox, ElNotification} from "element-plus";

/* 常用通知 */
function ElSuccess(message) {
    ElNotification.success({
        title: '通知',
        message: message,
        offset: 100,
    })
}

function ElWarning(message) {
    ElNotification.warning({
        title: '通知',
        message: message,
        offset: 100,
    })
}

function ElError(message) {
    ElNotification.error({
        title: '通知',
        message: message,
        offset: 100,
    })
}

export {ElSuccess, ElWarning, ElError}