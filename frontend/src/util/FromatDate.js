/* 时间格式化 */
function formatDate(row, column) {
    // 获取单元格数据
    let data = row[column.property];
    if(data == null) {
        return null;
    }
    let dt = new Date(data)
    return dt.getFullYear() + '年'
        + (dt.getMonth() + 1) + '月'
        + dt.getDate() + '日 '
        + dt.getHours() + '时'
        + dt.getMinutes() + '分'
        + dt.getSeconds() + '秒' ;
}

export {formatDate}