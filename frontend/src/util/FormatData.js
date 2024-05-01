import moment from 'moment' ;

/* 时间格式化 */
function formatDate(row, column) {
    // 获取单元格数据
    let data = row[column.property];

    if(data == null) {
        return null;
    }

    return returnTime(new Date(data)) ;
}

/* 开始时间截止时间格式化 */
function formatTime(row, column) {
    // 获取单元格数据
    let data = row[column.property];
    if(data == null) {
        return null;
    }
    let startTime = new Date(data[0])
    let endTime = new Date(data[1])

    /* 格式化 */
    return returnTime(startTime) + '—' + returnTime(endTime) ;
}
function returnTime(dt) {
        return dt.getFullYear() + '年'
            + (dt.getMonth() + 1) + '月'
            + dt.getDate() + '日 '
            + dt.getHours() + '时'
            + dt.getMinutes() + '分'
            + dt.getSeconds() + '秒' ;
}

/* 日期 格式，没有时间，只有日期 */
function dateFormat(date) {
    const birthdayDate = new Date(date);
     // 将日期转换为 ISO 8601 格式并截取日期部分
    return birthdayDate.toISOString().split('T')[0];
}
/* moment格式化日期 */
function momentFormatDate(dateString) {
    return moment(dateString).format('YYYY-MM-DD') ;
}
/* 显示日日期格式 */
function showDateFormat(date) {
    const birthdayDate = new Date(date);
    // 获取年月日部分
    const year = birthdayDate.getFullYear();
    const month = String(birthdayDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1
    const day = String(birthdayDate.getDate()).padStart(2, '0');
    // 拼接成年月日的 ISO 8601 格式
    return `${year}-${month}-${day}`;
}

/* 显示的日期时间格式 */
function showDateTimeFormat(isoDateTimeString) {
    const date = new Date(isoDateTimeString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/* 格式化数字 */
function formatNumber(num, type) {
    if (type === 'num') {
        if (num < 10000) {
            return num.toString();
        } else {
            return (num / 10000).toFixed(1) + 'w+';
        }
    } else {
        return num.toString() + '%';
    }

}

export {
    formatDate,
    formatTime,
    dateFormat,
    showDateFormat,
    showDateTimeFormat,
    momentFormatDate,
    formatNumber
}