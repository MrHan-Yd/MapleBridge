import {User} from "@/domain/User";
import {PostType} from "@/domain/PostType";
import {FilePost} from "@/domain/FilePost";
import {Comment} from "@/domain/Comment";
import {Like} from "@/domain/Like";

export interface Post {
    postId: string;
    user: User;
    topic: string;
    content: string;
    timestamp: Date;
    type: PostType;
    filePost: FilePost[];
    likeCount: string;
    commentCount: string;
    comment: Comment[];
    like: Like[];
    version: number;
    views: string;
}