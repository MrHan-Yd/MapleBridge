<script setup>
import PostUserAssembly from "@/components/PostUserAssembly.vue";
import {ChatDotSquare, Star, StarFilled} from "@element-plus/icons-vue";
import {defineEmits, reactive, ref} from "vue";
import {formatNumber} from "@/util/FormatData";
import {getUserId, put} from "@/net/http";

const prop = defineProps({
  comment: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['updateData'])
/* 评论的用户 */
/* 关联的评论ID  */
const commentForm = reactive({
  replyId: '',
  commentId: '',
  replyName: '',
});

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

// 使用 ref 创建响应式对象来保存点赞状态
const likedPosts = ref({});

// 初始化帖子点赞状态
function initLikedPosts() {
  /* 外层：回复帖子评论 */
  prop.comment.forEach(item => {
    const userLike = item.likes.find(like => like.userId === getUserId());
    likedPosts.value[item.id] = !!userLike;

    if (item.subComments === undefined) {
      item.subComments = [] ;
    }
    /* 内层：回复评论 */
    item.subComments.forEach(subItem => {
      if (subItem.likes.length !== 0) {
        const userLike = subItem.likes.find(like => like.userId === getUserId());
        likedPosts.value[subItem.id] = !!userLike;
      }
    });
  });
}

/* 刷新点赞状态 */
initLikedPosts();

// 点赞和取消点赞函数
function toggleLike(id, version, postId) {
  /* 内圈点赞找不到id */
  const isPostLiked = likedPosts.value[id];
  // 根据当前状态决定执行点赞或取消点赞操作
  if (isPostLiked) {
    // 取消点赞
    put("api/index/like", {id: postId, userId: getUserId(), version: version, commentId: id, type: 'unlike'}, (rs) => {
      if (rs.code === 200) {
        // 更新评论点赞数
        updateLikeCount(id, -1);
        // 更新点赞状态
        likedPosts.value[id] = false;
      }
    });
  } else {
    // 点赞
    put("api/index/like", {id: postId, userId: getUserId(), version: version, commentId: id, type: 'like'}, (rs) => {
      if (rs.code === 200) {
        // 更新评论点赞数
        updateLikeCount(id, 1);
        // 更新点赞状态
        likedPosts.value[id] = true;
      }
    });
  }
}

// 更新评论点赞数函数
function updateLikeCount(id, amount) {
  const comment = prop.comment.find(item => item.id === id);
  if (comment) {
    comment.likeCount = Math.max(0, parseInt(comment.likeCount) + amount);
  } else {
    for (let i = 0; i < prop.comment.length; i++) {
      if (!prop.comment[i].subComments) {
        continue;
      }
      const comment = prop.comment[i].subComments.find(item => item.id === id);
      if (comment) {
        comment.likeCount = Math.max(0, parseInt(comment.likeCount) + amount);
        break;
      }
    }
  }
}
</script>

<template>
  <div id="comment-assembly" v-for="item in comment" :key="item.id">
    <div id="top">
      <post-user-assembly shape="circle" :font-size="16" :size="20" :user="item.user"/>
    </div>
    <div id="bottom">
      <div id="left">
        {{ item.content }}
      </div>
      <div id="right">
        <el-icon id="comment" @click="comments(item)" style="display: flex;align-items:center;justify-content:center;" >
          <ChatDotSquare/>
        </el-icon>
        <span v-if="!likedPosts[item.id]" style="display: flex;align-items: center;justify-content: center;">
          <el-icon
              class="star"
              @click="toggleLike(item.id, item.version, item.postId)"
          >
          <Star/>
        </el-icon>
        <span style="display: flex;align-items: center;justify-content: center;width: 20px;">{{ formatNumber(item.likeCount, "num") }}</span>
        </span>
        <span v-else style="display: flex;align-items: center;justify-content: center;">
          <el-icon
              class="star-filled"
              @click="toggleLike(item.id, item.version, item.postId)"
          >
          <StarFilled/>
        </el-icon>
          <span style="display: flex;align-items: center;justify-content: center;width: 20px;">{{ formatNumber(item.likeCount, "num") }}</span>
        </span>
      </div>
    </div>
    <div id="sub-comment-assembly" v-if="item.subComments" v-for="item in item.subComments" :key="item.id">
      <div id="top">
        <post-user-assembly shape="circle" :font-size="16" :size="20" :user="item.user"/>
      </div>
      <div id="bottom">
        <div id="left">
          <span style="color: #b0b0b0">回复@{{ item.user.nickname }}：</span>{{ item.content }}
        </div>
        <div id="right">
          <el-icon id="comment" @click="comments(item)">
            <ChatDotSquare/>
          </el-icon>
          <span v-if="!likedPosts[item.id]" style="display: flex;align-items: center;justify-content: center;">
          <el-icon
              class="star"
              @click="toggleLike(item.id, item.version, item.postId)"
          >
          <Star/>
        </el-icon>
        <span style="display: flex;align-items: center;justify-content: center;width: 20px;">{{ formatNumber(item.likeCount, "num") }}</span>
        </span>
          <span v-else style="display: flex;align-items: center;justify-content: center;">
          <el-icon
              class="star-filled"
              @click="toggleLike(item.id, item.version, item.postId)"
          >
          <StarFilled/>
        </el-icon>
          <span style="display: flex;align-items: center;justify-content: center;width: 20px;">{{ formatNumber(item.likeCount, "num") }}</span>
        </span>
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
    margin-bottom: 10px;

    #left {
      height: 100%;
      width: 80%;
      padding-left: 65px;
      font-size: 16px;
    }

    #right {
      height: 100%;
      width: 20%;
      text-align: right;
      display: flex;
      align-items: center;
      justify-content: center;

      #comment {
        font-size: 20px;
        margin-right: 20px;

        &:hover {
          color: #7699f8;
        }
      }

      .star {
        font-size: 20px;
        width: 25px;
        overflow: hidden;
        position: relative;

        &:hover {
          color: #7699f8 ;
        }
      }

      .star-filled {
        font-size:24px;
        width: 25px;
        color: #7699f8;

        &:hover {
          color: #999999 ;
        }
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