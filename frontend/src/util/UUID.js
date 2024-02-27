
/* 生成验证码ID */
let lastGeneratedUniqueId = null;

function generateUniqueId() {
    // 使用时间戳和随机数生成唯一标识符
    const timestamp = Date.now().toString(36).substr(-8); // 获取当前时间戳并转换为base36编码
    const randomString = Math.random().toString(36).substring(2, 9); // 生成随机字符串

    lastGeneratedUniqueId = `${timestamp}-${randomString}`; // 将时间戳与随机字符串连接起来作为唯一标识
    return lastGeneratedUniqueId;
}

/* 获取上次生成的验证码ID */
function getUniqueId() {
    return lastGeneratedUniqueId;
}


export {generateUniqueId, getUniqueId};