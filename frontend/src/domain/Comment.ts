import {User} from "@/domain/User";
import {Like} from "@/domain/Like";

export type Comment = {
    id: string;
    postId: string;
    user: User;
    content: string;
    timestamp?: Date;
    likeCount: string;
    like: Like[];
    comment: Comment[];
    commentId: string;
    replyId: string;
    subComments?: Comment[] | undefined;
    version?: number;

}