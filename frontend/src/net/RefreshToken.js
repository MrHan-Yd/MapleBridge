import http  from '@/net/http' ;
import { getRefreshToken } from '@/net/http' ;

let promise ;
export async function refreshToken() {
    if (promise) {
        return promise ;
    }

    promise = new Promise(async (resolve) => {
        const resp = await http.get('/api/auth/refresh-token', {
            headers: {
                'Authorization': `Bearer ${getRefreshToken()}`
            },
            __isRefresh: true
        }).then(({data}) => {
            return data ;
        });
        /* 刷新成功 */
        resolve(resp.code === 200)
    });

    /* 完成后清空 */
    promise.finally(() => {
        promise = null ;
    });

    return promise ;
}

export function isRefresh(config) {
    return !!config.__isRefresh;
}