import { ElNotification } from "element-plus";
import { ElMessage } from 'element-plus'
import { h } from 'vue'

/* 常用通知 */
function ElSuccess(message) {
    ElNotification.success({
        title: '通知',
        message: h('i', { style: 'color: #67C23A' }, message),
        offset: 100,
    })
}

function ElInfo(message) {
    ElNotification.info({
        title: '通知',
        message: h('i', { style: 'color: #909399' }, message),
        offset: 100,
    })
}

function ElWarning(message) {
    ElNotification.warning({
        title: '通知',
        message: h('i', { style: 'color: #E6A23C' }, message),
        offset: 100,
    })
}

function ElError(message) {
    ElNotification.error({
        title: '通知',
        message: h('i', { style: 'color: #F56C6C' }, message),
        offset: 100,
    })
}

function ElWarningMessage(message) {
    ElMessage({
        message: message,
        type: 'warning',
    })
}

export {ElSuccess, ElWarning, ElError, ElWarningMessage}