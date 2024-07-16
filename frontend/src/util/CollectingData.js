import { reactive, ref } from 'vue' ;
import {getUserId, post} from "@/net/http";

/* 提交的数据列表 */
const dataList = reactive({
    userId: '',
    typeList: []
});

/* 每10条上传一次 */
const count = ref(0) ;

/* 记录数据 */
function recordData(typeId) {
    count.value++;
    if (dataList.typeList.length === 0) {
        const types = {
            typeId: typeId,
            count: 1
        }
        dataList.typeList.push(types);
    } else {
        let index = dataList.typeList.find(type => type.typeId === typeId);
        if (index) {
            index.count++;
        } else {
            const types = {
                typeId: typeId,
                count: 1
            }
            dataList.typeList.push(types);
        }
    }

    /* 大于十条才上传 */
    if (count.value >= 10) {
        uploadData();
    }
}

/* 上传数据 */
function uploadData() {
    /* 最少有收集一条 数据才上传 */
    if (count.value > 0) {
        /* 获取用户ID */
        dataList.userId = getUserId() ;
        post('api/index/collect-preference', {...dataList}, (rs) => {
            if (rs.code === 200) {
                /* 清空数据和计数 */
                dataList.typeList = [];
                count.value = 0;
            }
        }) ;
    }
}

export {recordData, uploadData} ;