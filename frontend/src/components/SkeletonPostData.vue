<script setup>
import {defineProps, ref, computed, reactive} from 'vue';
import {getAllParameters, getUserId, post, put} from '@/net/NetWork' ;
import {ElSuccess, ElWarning, ElWarningMessage} from "@/util/MessageUtil";
import {CaretTop, ChatDotRound, CloseBold, Warning} from "@element-plus/icons-vue";
import {formatNumber} from "@/util/FormatData" ;
import PostUserAssembly from "@/components/PostUserAssembly.vue";
import CommentAssembly from "@/components/CommentAssembly.vue";
import { recordData } from "@/util/CollectingData" ;

/* 分页信息 */
const page = reactive({
  num: 1,
  size: 4,
  total: 0,
});

/* 加载实体类 */
const data = ref([]);

// 定义组件的 props
const props = defineProps({
  bottomText: {
    type: String,
    default: '底部'
  },
  whetherShow: Boolean, // 添加 whetherShow 到 props 中
  user: {
    type: Object,
    default: {}
  }
});

const loading = ref(false);
const noMore = ref(false);
const disabled = computed(() => loading.value || noMore.value);

/* 滚动加载 */
const load = () => {
  loading.value = true;

  if (!noMore.value) {
    page.num += 1;
  }
  getPostEs(page);
}


let $emit = defineEmits(['whetherShow']);


/* 页面打开查询帖子数据 */
function getPostEs(page) {
  getAllParameters("api/index/post-es?pageNum=" + page.num + "&pageSize=" + page.size, (rs) => {
    if (rs.code === 200) {
      data.value = [...data.value, ...rs.data.content];
      noMore.value = rs.data.last;
      page.num = rs.data.pageable.pageNumber + 1;
      page.size = rs.data.pageable.pageSize;
      page.total = rs.totalPages;
      loading.value = false;
      /* 显示数据 */
      $emit('whetherShow', true);
      /* 刷新点赞状态 */
      initLikedPosts();
    } else {
      /* 显示骨架 */
      $emit('whetherShow', false);
    }
  }, (message) => {
    ElWarning(message);
    /* 显示骨架 */
    $emit('whetherShow', false);
  }, (err => {
    ElWarning(err.message);
    /* 显示骨架 */
    $emit('whetherShow', false);
  }));
}

getPostEs(page);

// 使用 ref 创建响应式对象来保存点赞状态
const likedPosts = ref({});

// 点赞和取消点赞函数
function toggleLike(postId, version, typeId) {
  const isPostLiked = likedPosts.value[postId];

  // 根据当前状态决定执行点赞或取消点赞操作
  if (isPostLiked) {
    // 取消点赞
    put("api/index/like", {id: postId, userId: getUserId(), version: version, type: 'unlike'}, (rs) => {
      if (rs.code === 200) {
        // 更新帖子点赞数
        updateLikeCount(postId, -1);
        // 更新点赞状态
        likedPosts.value[postId] = false;
      }
    });
  } else {
    // 点赞
    // /* 记录收集用户偏好类型 */
    recordData(typeId) ;
    put("api/index/like", {id: postId, userId: getUserId(), version: version, type: 'like'}, (rs) => {
      if (rs.code === 200) {
        // 更新帖子点赞数
        updateLikeCount(postId, 1);
        // 更新点赞状态
        likedPosts.value[postId] = true;
      }
    });
  }
}

// 更新帖子点赞数函数
function updateLikeCount(postId, amount) {
  const post = data.value.find(item => item.postId === postId);
  if (post) {
    post.likeCount = Math.max(0, parseInt(post.likeCount) + amount);
  }
}

// 初始化帖子点赞状态
function initLikedPosts() {
  data.value.forEach(item => {
    const userLike = item.like.find(like => like.userId === getUserId());
    likedPosts.value[item.postId] = !!userLike;
  });
}

/* 浏览弹窗 */
const dialogVisible = ref(false)
/* 浏览详情标题 */
const dialogTitle = ref('');
/* 浏览详情内容 */
const dialogContent = ref('');
/* 浏览详情图片 */
const dialogImg = ref([]);
/* 浏览详情图片首页 */
const dialogImgIndex = ref('');
/* 浏览详情作者信息 */
const dialogUser = ref('');
/* 浏览详情页帖子类型 */
const dialogType = ref('');
/* 浏览帖子评论 */
const dialogComment = ref([]);
/* 浏览帖子详情评论输入框 */
const dialogCommentButton = ref(false);
/* 浏览帖子评论数量 */
const dialogCommentCount = ref(0);
/* 浏览的帖子信息 */
const commentForm = reactive({
  postId: '',
  userId: '',
  content: '',
  commentId: '',
  replyId: '',
});

/* 切换评论框状态 */
function toggleCommentButton() {
  return dialogCommentButton.value = !dialogCommentButton.value;
}

/* 点击查看帖子详情 */
function check(item) {
  /* 收集用户偏好类型 */
  recordData(item.type.typeId) ;
  dialogCommentButton.value = false;
  dialogVisible.value = true;
  dialogTitle.value = item.topic;
  dialogContent.value = item.content;
  dialogType.value = item.type;
  dialogCommentCount.value = item.commentCount;
  if (item.filePost.length !== 0) {
    /* 浏览图片的封面 */
    dialogImgIndex.value = item.filePost[0].url;
    /* 浏览图片集合 */
    let imgList = [];
    for (let i = 0; i < item.filePost.length; i++) {
      imgList.push(item.filePost[i].url);
    }
    /* 赋值 */
    dialogImg.value = imgList;
  }
  /* 作者信息  */
  if (item.user !== undefined) {
    dialogUser.value = item.user;
  }

  /* 评论 */
  if (item.comment.length !== 0) {
    dialogCommentButton.value = true;
    dialogComment.value = item.comment;
    dialogCommentButton.value = false;
  } else {
    dialogCommentButton.value = true;
  }

  /* 浏览的帖子ID */
  commentForm.postId = item.postId;
}

/* 关闭详情对话框回调 */
function closeDialog() {
  /* 浏览的帖子ID */
  commentForm.postId = '';
  /* 浏览详情图片 */
  dialogImg.value = '';
  /* 浏览详情图片首页 */
  dialogImgIndex.value = '';
  /* 评论数据 */
  dialogComment.value = [];
}

/* 发布评论 */
function submitComment() {
  if (commentForm.content.trim() === '') {
    ElWarningMessage("不能发布空评论！");
  } else {
    /* 发布人 */
    commentForm.userId = getUserId();

    /* 判断是不是回复评论  */
    if (replyForm.replyId !== '') {
      commentForm.replyId = replyForm.replyId;
      commentForm.commentId = replyForm.commentId;
    }
    /* 发布评论 */
    post("/api/index/comment", {...commentForm}, (rs) => {
      if (rs.code === 200) {
        /* 添加数据方便前端查看 */
        data.value.forEach(item => {
          if (item.postId === commentForm.postId) {
            /* 优化:
            * 人造数据给前端查看,实际响应数据库已插入,
            * 用户下次访问会访问数据库中的数据,并无任何影响
            */
            const comment = {
              id: rs.data,
              postId: commentForm.postId,
              content: commentForm.content,
              commentId: commentForm.commentId,
              replyId: commentForm.replyId,
              likeCount: 0,
              likes: [],
              comment: [],
              user: {
                userId: props.user.id,
                nickname: props.user.nickname,
                level: String(props.user.level.level),
                levelName: props.user.level.levelName,
                path: props.user.avatars.avatarPath,
                fileName: props.user.avatars.fileName
              },
            }
            /* 评论+1 */
            item.commentCount = Math.max(0, parseInt(item.commentCount) + 1);
            /* 回复评论 */
            if (replyForm.replyId !== '') {

              item.comment.forEach(comments => {
                if (comments.id === commentForm.commentId) {
                  /**
                   * 如果没有子评论数组则创建一个，后端返回哪怕没有子评论也都会有空数组，
                   * 这种情况一般是用户继续浏览用前端生成的人造数据，这样的数据只会有重要且不会影响业务的字段
                   * */
                  if (comments.subComments === '' || comments.subComments === undefined) {
                    comments.subComments = [];
                  }

                  /* 放进子评论数组  */
                  comments.subComments.push(comment);
                }
              });
            } else {
              /* 发布评论 */
              item.comment.push(comment);
            }

            /* 加载评论 */
            check(item);
          }
        });
        /* 清空回复表单 */
        deleteReplyForm();
        commentForm.content = '';
        commentForm.commentId =  '';
        commentForm.replyId =  '';
        ElSuccess("发布成功");
      } else {
        ElWarning(rs.message);
      }
    });
  }
}
/* 回复评论表单 */
const replyForm = reactive({
  replyId: '',
  commentId: '',
  replyName: '',
});

/* 子组件回复评论响应 */
function handleData(value) {
  replyForm.replyId = value.replyId;
  replyForm.commentId = value.commentId;
  replyForm.replyName = value.replyName;
}

/* 取消评论，清空回复表单 */
function deleteReplyForm() {
  replyForm.replyId = '';
  replyForm.commentId = '';
  replyForm.replyName = '';
}
</script>

<template>
  <div class="skeleton"
       v-for="item in data" :key="item"
       :infinite-scroll-disabled="disabled"
       v-infinite-scroll="load"
       infinite-scroll-delay="1000"
  >
    <div class="title">
      {{ item.topic }}
    </div>
    <div class="content" @click="check(item)">
      <div class="box" v-if="item.filePost.length !== 0">
        <div class="left">
          <el-image style="height: 100%; width: 100%" :src="item.filePost[0].url" alt="图片"/>
        </div>
        <div class="right">
          {{ item.content }}
        </div>
      </div>
      <div class="box2" v-else>
        <div class="content">
          {{ item.content }}
        </div>
      </div>
    </div>
    <div class="bottom">
      <!--      {{ bottomText }}-->
      <el-button @click="toggleLike(item.postId, item.version, item.type.typeId)" type="primary" :icon="CaretTop"
                 :plain="!likedPosts[item.postId]">
        {{ likedPosts[item.postId] ? '取消赞' : '点赞' }} {{ formatNumber(item.likeCount, "num") }}
      </el-button>
      <el-button :icon="ChatDotRound" @click="check(item)">{{ formatNumber(item.commentCount, "num") }}条评论</el-button>
      <el-button :icon="Warning">举报</el-button>
    </div>
  </div>
  <div id="footer">
    <p v-if="!noMore">加载中...</p>
    <p v-if="noMore">没有更多数据...</p>
  </div>
  <!--浏览弹窗-->
  <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800"
      draggable
      :modal="false"
      style="
      height: 82vh;
      overflow-y: scroll;
      "
      top="10vh"
      destroy-on-close
      @close="closeDialog"
  >
    <div style="height: 50px;">
      <post-user-assembly :user="dialogUser" :type="dialogType"/>
    </div>
    <span style="display: inline-block;width: 100%;">{{ dialogContent }}</span>
    <div style="margin-top: 20px;">
      <el-image
          v-if="dialogImg.length !== 0"
          style="width: 100%; height: 100%"
          :src="dialogImgIndex"
          :zoom-rate="1.2"
          :max-scale="7"
          :min-scale="0.2"
          :preview-src-list="dialogImg"
          :initial-index="0"
          fit="cover"
      />
    </div>
    <div id="commentSection">
      <div style="
      height: 50px;
      width: calc(100% - 22px);
      border: 1px solid #e1e1e1;
      padding-left: 20px;
      display: flex;
      align-items:center;">
        {{ formatNumber(dialogCommentCount, "num") }}条评论
      </div>
      <div v-if="dialogComment.length === 0">
        <el-empty description="暂无评论">
          <el-button type="primary" @click="toggleCommentButton">
            {{ !dialogCommentButton ? '收起' : '说点什么吧~' }}
          </el-button>
        </el-empty>
      </div>
      <div v-else
           style="
      border-left: 1px solid #e1e1e1;
      border-right: 1px solid #e1e1e1;
      border-bottom: 1px solid #e1e1e1;
      padding: 10px;">
        <comment-assembly @updateData="handleData" :comment="dialogComment"/>
      </div>
      <div id="commentInputBox"
           v-if="!dialogCommentButton">
        <div id="left">
          <el-avatar shape="square" :size="49" :src="user.avatars.avatarPath + user.avatars.fileName"/>
        </div>
        <div id="right">
          <div style="width: 80%;height:100%;position:relative;">
            <span
                v-if="replyForm.replyId"
                style="font-size: 16px;
                position: absolute;
                top:-25px;
                left:0;
                color: #b3b3b3;
                display: flex;
                align-items: center;
                ">
              @{{ replyForm.replyName }}
              <el-icon id="cancelReply" @click="deleteReplyForm"><CloseBold/></el-icon>
            </span>
            <el-input
                v-model="commentForm.content"
                style="width: 100%;height:100%;"
                maxlength="30"
                placeholder="欢迎参与讨论，恶言伤人心，请理性讨论！"
                show-word-limit
                type="text"
            />
          </div>
          <el-button
              @click="submitComment"
              type="primary"
              style="width: 20%;height:100%;"
              plain>
            发布
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.skeleton {
  border-top: 1px solid #e3e3e3;
  margin-top: 5px;
  margin-bottom: 5px;

  .title {
    padding: 10px;
    width: 100%;
    height: 10%;
    font-size: 20px;
  }

  .content {
    height: 100px;
    padding: 0 10px;

    .box {
      height: 100%;
      display: flex;
      align-items: center;
      background-color: white;

      .left {
        height: 100%;
        width: 30%;
        display: flex;
        align-items: center;
        justify-content: center;

      }

      .right {
        margin-left: 10px;
        height: 100%;
        width: calc(80% - 15px);
        line-height: 1.5em;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
      }
    }

    .box2 {
      height: 100%;

      .content {
        width: calc(100% - 20px);
        line-height: 1.5em;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
      }
    }
  }

  .bottom {
    margin-top: 5px;
    width: calc(100% - 20px);
    margin-left: 10px;
    display: flex;
    align-items: center;
  }
}

#footer {
  background-color: #f1f0f0;
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

#commentSection {
  font-size: 16px;
  margin-top: 10px;
  height: 100%;
  width: 100%;

  #commentInputBox {
    height: 50px;
    width: 100%;
    position: sticky; /* 使用粘性定位 */
    bottom: 0; /* 固定在顶部 */
    z-index: 99; /* 确保盒子在对话框标题上方 */
    display: flex;
    align-items: center;
    justify-content: center;

    #left {
      height: 100%;
      width: 10%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    #right {
      height: 100%;
      width: 89%;
      //border: 1px solid #999999;
      //border-radius: 5px;
      display: flex;
      align-items: center;

      #cancelReply {
        &:hover {
          color: #7699f8;
        }
      }
    }
  }
}

</style>