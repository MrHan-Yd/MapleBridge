<script setup>
import PostUserAssembly from "@/components/PostUserAssembly.vue";
import {ChatDotSquare, Star} from "@element-plus/icons-vue";
import { ref, defineEmits, reactive } from "vue";

const prop = defineProps({
  comment: {
    type: Object,
    required: true
  }
}) ;

const emit = defineEmits(['updateData'])
/* 评论的用户 */
/* 关联的评论ID  */
const commentForm = reactive({
  replyId: '',
  commentId: '',
  replyName: '',
}) ;
function comments(item) {
  /* 如果不是第一层则评论同属于上一层的评论 */
  if (item.commentId !== '') {
    commentForm.commentId = item.commentId;
  } else {
    /* 评论是第一层,则评论是其评论的子评论 */
    commentForm.commentId = item.id;
  }

  /* 回复人,其实就是此条评论的发布人  */
  commentForm.replyId = item.user.userId;
  commentForm.replyName = item.user.nickname;

  emit('updateData', commentForm)
}
</script>

<template>
  <div id="comment-assembly" v-for="item in comment" :key="item.id">
    <div id="top">
      <post-user-assembly shape="circle" :font-size="16" :size="20" :user="item.user"/>
    </div>
    <div id="bottom">
      <div id="left">
        {{item.content}}
      </div>
      <div id="right">
        <el-icon id="comment"  @click="comments(item)" >
          <ChatDotSquare />
        </el-icon>
        <el-icon id="star">
          <Star />
        </el-icon>
      </div>
    </div>
    <div id="sub-comment-assembly" v-if="item.subComments" v-for="item in item.subComments" :key="item.id">
      <div id="top">
        <post-user-assembly shape="circle" :font-size="16" :size="20" :user="item.user"/>
      </div>
      <div id="bottom">
        <div id="left">
          <span style="color: #b0b0b0">回复@{{item.user.nickname}}：</span>{{item.content}}
        </div>
        <div id="right">
          <el-icon id="comment"  @click="comments(item)" >
            <ChatDotSquare />
          </el-icon>
          <el-icon id="star">
            <Star />
          </el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#comment-assembly {
  height: 100%;
  width: 100%;

  #top {
    height: 50px;
    width: 100%;
  }

  #bottom {
    height: 30px;
    width: 100%;
    display: flex;
    margin-bottom:10px;

    #left {
      height: 100%;
      width: 70%;
      padding-left: 65px;
      font-size: 16px;
    }

    #right {
      height: 100%;
      width: 30%;
      text-align: right;
      margin-right: 20px;

      #comment {
        font-size: 20px;
        margin-right: 20px;

          &:hover {
          color: #7699f8;
        }
      }

      #star {
        font-size: 20px;
      }
    }
  }

  #sub-comment-assembly {
    height: 100%;
    width: calc(100% - 50px);
    padding-left: 50px;
  }
}
</style>