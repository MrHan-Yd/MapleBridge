import { post } from '@/net/http' ;

type PostHits = {
    postId: string;
    hits: number;
}
 export default class PostHitsCollectionUtil {
    /* 提交数据数组 */
    private static data: PostHits[] = new Array<PostHits>();
    private static count: number = 0;

    /* 收集数据 */
    public static collectData = (data: string) => {
        this.count ++ ;
        if (this.data.length === 0) {
            this.data.push({postId: data, hits: 1}) ;
        } else {
            let item: PostHits | undefined = this.data.find((item: PostHits) => item.postId === data);
            if (item) {
                item.hits ++ ;
            } else {
                this.data.push({postId: data, hits: 1}) ;
            }
        }

        /* 每10条上传一次 */
        if (this.count >= 10) {
            this.uploadData();
        }
    }

    /* 上传数据 */
    public static uploadData() {
        if (this.data.length > 0) {
            post('api/index/post-hits', this.data, () => {
                this.data = []
                this.count = 0;
            }, () => {

            });
        }
    }
}

