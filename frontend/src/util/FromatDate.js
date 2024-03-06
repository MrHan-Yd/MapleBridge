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

export {formatDate, formatTime}