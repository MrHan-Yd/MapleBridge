<script setup lang="ts">
import {ref, reactive, onMounted, watch, nextTick, computed} from 'vue';
import {Avatar, CaretTop, ChatDotRound, CloseBold, Warning} from "@element-plus/icons-vue";
import {formatNumber, showDateFormat} from "@/util/FormatData";
import CommentAssembly from "@/components/CommentAssembly.vue";
import MyIconButton from "@/components/MyIconButton.vue";
import PostUserAssembly from "@/components/PostUserAssembly.vue";
import {getAllParameters, get, getUserId, post, put} from "@/net/http";
import {ElWarning, ElWarningMessage, ElSuccess} from "@/util/MessageUtil"
import {Post} from "@/domain/Post";
import PostHitsCollectionUtil from "@/util/ts/PostHitsCollectionUtil";
import {recordData} from "@/util/CollectingData";
import {PostType} from "@/domain/PostType";
import {User} from "@/domain/User";
import {Comment} from "@/domain/Comment";
import {Like} from "@/domain/Like";
import {SearchUser} from "@/domain/SearchUser";
import {ElTag} from "element-plus";
import UserInfoShowAssembly from "@/components/UserInfoShowAssembly.vue";
import BadgeAssembly from "@/components/BadgeAssembly.vue";

const props = defineProps({
  userInfo: {
    type: Object,
    default: {}
  },
  searchStr: {
    type: String,
  },
});
/* 滚动加载 */
const load = () => {
  loading.value = true;

  if (activeIndex.value === '3') {
    if (!noMore.value) {
      page.num += 1;
    }
    getPostEs(page);
  } else {
    if (!noMore.value) {
      page.num += 1;
    }

    getUserEs();
  }
}

const dataUser = ref<SearchUser[]>([]);
const whetherShowUser = ref(false);

const getUserEs = (pages?: any) => {
  if (!pages) {
    page.num = 1;
    page.size = 4;
    page.total = 0;
  } else {
    page.num = pages.num;
    page.size = pages.size;
    page.total = pages.total;
  }

  let url = "api/index/search?pageNum=" + page.num + "&pageSize=" + page.size + "&queryString=" + props.searchStr + "&type=USER";

  get(url, (rs) => {
    if (rs.code === 200) {
      /* 监听 */
      if (searchStr.value !== props.searchStr) {
        searchStr.value = <string>props.searchStr;
        dataUser.value = rs.data.content;
        /* 重置滚动条 */
        resetScroll();
        $emit('restScroll');
      }else {
        dataUser.value = [...data.value, ...rs.data.content];
      }

      noMore.value = rs.data.last;
      page.num = rs.data.pageable.pageNumber + 1;
      page.size = rs.data.pageable.pageSize;
      page.total = rs.totalPages;
      loading.value = false;

      /* 显示数据 */
      whetherShowUser.value = true;
      /* 刷新点赞状态 */
      initLikedPosts();
    } else {
      /* 显示骨架 */
      whetherShowUser.value = false;
    }
    console.log(rs);
  }, (message, code) => {
    if (code === 400) {
      ElWarning("暂无匹配数据");
      return;
    }
    ElWarning(message);
  });
}


/* 默认选中推荐 */
const activeIndex = ref<string>('3');

/* 菜单选择 */
const handleClick = (key: string, _keyPath: string[]) => {
  /* 重置滚动条*/
  if (activeIndex.value !== key) {
    resetScroll();
  }

  data.value = [];
  dataUser.value = [];
  activeIndex.value = key;
  loadData();
}
/* 加载数据 */
const loadData = () => {
  /* 帖子 */
  if (activeIndex.value === '3') {
    getPostEs();
  } else {
    /* 用户 */
    console.log("222")
    getUserEs();
  }
}
/* 暴露出去 */
defineExpose({
  loadData
});

// 定义是否显示骨架屏的变量
const whetherShow = ref(true);

/* 计算骨架的高度 */
function calculateSkeletonRows() {
  // 获取屏幕高度
  const screenHeight = window.innerHeight;

  // 假设每行骨架高度为 48.5px，根据屏幕高度计算行数
  const rowHeight = 48.5; // 假设每行高度为 48.5px
  return Math.floor(screenHeight / rowHeight);
}

/* 加载实体类 */
const data = ref<Post[]>([]);
/* 分页信息 */
const page = reactive({
  num: 1,
  size: 4,
  total: 0,
});

const loading = ref(false);
const noMore = ref(false);
const disabled = computed(() => loading.value || noMore.value);

let $emit = defineEmits(['restScroll']);

const searchStr = ref('');

/* 页面打开查询帖子数据 */
function getPostEs(pages? : any) {
  if (!pages) {
    page.num = 1;
    page.size = 4;
    page.total = 0;
  } else {
    page.num = pages.num;
    page.size = pages.size;
    page.total = pages.total;
  }

  let url = "api/index/search?pageNum=" + page.num + "&pageSize=" + page.size + "&queryString=" + props.searchStr + "&type=POST";

  getAllParameters(url, (rs) => {
    if (rs.code === 200) {
      /* 监听 */
      if (searchStr.value !== props.searchStr) {
        searchStr.value = <string>props.searchStr;
        data.value = rs.data.content;
        /* 重置滚动条 */
        resetScroll();
        $emit('restScroll');
      }else {
        data.value = [...data.value, ...rs.data.content];
      }

      noMore.value = rs.data.last;
      page.num = rs.data.pageable.pageNumber + 1;
      page.size = rs.data.pageable.pageSize;
      page.total = rs.totalPages;
      loading.value = false;

      /* 显示数据 */
      whetherShow.value = true;
      /* 刷新点赞状态 */
      initLikedPosts();
    } else {
      /* 显示骨架 */
      whetherShow.value = false;
    }
  }, (message) => {
    ElWarning(message);
    /* 显示骨架 */
    whetherShow.value = false;
  }, (err => {
    ElWarning(err.message);
    /* 显示骨架 */
    whetherShow.value = false;
  }));
}

// 使用 ref 创建响应式对象来保存点赞状态
const likedPosts = ref<any>({});

// 初始化帖子点赞状态
function initLikedPosts() {
  data.value.forEach((item: any) => {
    const userLike = item.like.find((like: any) => like.userId === getUserId());
    likedPosts.value[item.postId] = !!userLike;
  });
}
/* 浏览帖子详情评论输入框 */
const dialogCommentButton = ref(false);
/* 浏览弹窗 */
const dialogVisible = ref(false);
/* 浏览详情标题 */
const dialogTitle = ref('');
/* 浏览详情内容 */
const dialogContent = ref('');
/* 浏览详情页帖子类型 */
const dialogType = ref<PostType>({typeId: "", typeName: ""});
/* 浏览帖子评论数量 */
const dialogCommentCount = ref('0');
/* 浏览详情图片首页 */
const dialogImgIndex = ref('');
/* 浏览详情图片 */
const dialogImg = ref<string[]>([]);
/* 浏览详情作者信息 */
const dialogUser = ref<User>({fileName: "", level: "", levelName: "", nickname: "", path: "", userId: ""});
/* 浏览帖子评论 */
const dialogComment = ref<Comment[]>([]);
/* 浏览的帖子信息 */
const commentForm = reactive({
      postId: '',
      userId: '',
      content: '',
      commentId: '',
      replyId: '',
});


/* 点击查看帖子详情 */
function check(item: Post) {
  /* 收集帖子点击量 */
  PostHitsCollectionUtil.collectData(item.postId);
  /* 收集用户偏好类型 */
  recordData(item.type.typeId);
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

// 更新帖子点赞数函数
function updateLikeCount(postId: string, amount: number) {
  const post = data.value.find(item => item.postId === postId);
  if (post) {
    post.likeCount = String(Math.max(0, Number(post.likeCount) + amount));
  }
}

// 点赞和取消点赞函数
function toggleLike(postId: string, version: number, typeId: string) {
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
    /* 收集帖子点击量 */
    PostHitsCollectionUtil.collectData(postId);
    /* 记录收集用户偏好类型 */
    recordData(typeId);
    put("api/index/like", {id: postId, userId: getUserId(), version: version, type: 'like'},
        (rs) => {
          if (rs.code === 200) {
            // 更新帖子点赞数
            updateLikeCount(postId, 1);
            // 更新点赞状态
            likedPosts.value[postId] = true;
          }
        });
  }
}
/* 关闭详情对话框回调 */
function closeDialog() {
  /* 浏览的帖子ID */
  commentForm.postId = '';
  /* 浏览详情图片 */
  dialogImg.value = [];
  /* 浏览详情图片首页 */
  dialogImgIndex.value = '';
  /* 评论数据 */
  dialogComment.value = [];
}

/* 切换评论框状态 */
function toggleCommentButton() {
  return dialogCommentButton.value = !dialogCommentButton.value;
}

/* 回复评论表单 */
const replyForm = reactive({
  replyId: '',
  commentId: '',
  replyName: '',
});

/* 子组件回复评论响应 */
function handleData(value: any) {
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
    post("/api/index/comment", {...commentForm}, (rs: any) => {
      if (rs.code === 200) {
        /* 添加数据方便前端查看 */
        data.value.forEach(item => {
          if (item.postId === commentForm.postId) {
            /* 优化:
            * 人造数据给前端查看,实际响应数据库已插入,
            * 用户下次访问会访问数据库中的数据,并无任何影响
            */
            const comment: {
              like: Like[];
              replyId: string;
              commentId: string;
              likeCount: string;
              comment: Comment[];
              id: string;
              postId: string;
              user: { path: string; fileName: string; level: string; nickname: string; levelName: string; userId: string };
              content: string
            } = {
              id: rs.data,
              postId: commentForm.postId,
              content: commentForm.content,
              commentId: commentForm.commentId,
              replyId: commentForm.replyId,
              likeCount: "0",
              like: [],
              comment: [],
              user: {
                userId: props.userInfo.id,
                nickname: props.userInfo.nickname,
                level: String(props.userInfo.level.level),
                levelName: props.userInfo.level.levelName,
                path: props.userInfo.avatars.avatarPath,
                fileName: props.userInfo.avatars.fileName
              },
            }
            /* 评论+1 */
            item.commentCount = String(Math.max(0, parseInt(item.commentCount) + 1));
            /* 回复评论 */
            if (replyForm.replyId !== '') {

              item.comment.forEach(comments => {
                if (comments.id === commentForm.commentId) {
                  /**
                   * 如果没有子评论数组则创建一个，后端返回哪怕没有子评论也都会有空数组，
                   * 这种情况一般是用户继续浏览用前端生成的人造数据，这样的数据只会有重要且不会影响业务的字段
                   * */
                  if (comments.subComments === undefined) {
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
        commentForm.commentId = '';
        commentForm.replyId = '';
        ElSuccess("发布成功");
      } else {
        ElWarning(rs.message);
      }
    });
  }
}
/* 重置滚动条 */
const scrollContainer = ref(null);
const resetScroll = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollTo({
      top: 0,
      behavior: 'smooth' // 可选，平滑滚动
    });
  }
};

/* 用户弹窗 */
const show = ref<boolean>(false);
const showUser = ref<SearchUser>({});
const userCheck = (item :SearchUser) => {
  showUser.value = item;
  show.value = true;
}

// /* 图片选择放大查看是事件 */
const dialogVisibleImg = ref<boolean>(false)
const imageUrl = ref<string>('')
const handlePictureCardPreviewImg = (url: string) => {
  imageUrl.value = url;
  /* 弹出对话框 */
  dialogVisibleImg.value = true;
}

</script>

<template>
  <div id="box">
    <div id="top">
      <el-menu
          :default-active="activeIndex"
          class="el-menu"
          mode="horizontal"
          :popper-offset="16"
          @select="handleClick"
      >
        <el-menu-item index="3">分享</el-menu-item>
        <el-menu-item index="4">用户</el-menu-item>
      </el-menu>
    </div>
    <div id="bottom">
      <div id="post" ref="scrollContainer" v-if="activeIndex === '3'">
        <div class="skeleton"
             v-for="item in data" :key="item.postId"
             :infinite-scroll-disabled="disabled"
             v-infinite-scroll="load"
             :infinite-scroll-distance="-600"
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
            <el-button :icon="ChatDotRound" @click="check(item)">{{
                formatNumber(item.commentCount, "num")
              }}条评论
            </el-button>
            <el-button :icon="Warning">举报</el-button>
            <el-text style="margin-left: 10px;">
              <my-icon-button name="icon-dianjiliang"/>
              {{ formatNumber(item.views, "num") }}
            </el-text>
          </div>
        </div>
        <div id="footer">
          <p v-if="!noMore">
            <svg class="circular" viewBox="0 0 50 50">
              <circle class="path" cx="25" cy="25" r="20" fill="none"/>
            </svg>
          </p>
          <p v-if="noMore">没有更多分享...</p>
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
                <el-avatar v-if="userInfo.avatars" shape="square" :size="49" :src="userInfo.avatars.avatarPath + userInfo.avatars.fileName"/>
                <el-avatar v-else :size="49" :src="'/images/touxiang-1.jpg'"/>
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
      </div>
      <div id="user" v-else>
        <div class="content" v-if="whetherShowUser"  ref="scrollContainer">
          <div class="skeleton"
               @click="userCheck(item)"
               v-for="item in dataUser" :key="item.id"
               :infinite-scroll-disabled="disabled"
               v-infinite-scroll="load"
               :infinite-scroll-distance="-600"
               infinite-scroll-delay="1000"
          >
            <div id="userBox">
              <div id="left">
                <el-avatar v-if="item.avatars" :size="60" :src="item.avatars.avatarPath + item.avatars.fileName"/>
                <el-avatar v-else :size="60" :src="'/images/touxiang-1.jpg'"/>
              </div>
              <div id="right">
                <div id="user-information">
                    {{ item.nickname }}
                    <el-tag size="small">LV {{ item.level.level }}</el-tag>
                </div>
                <div id="bio">
                  {{item.userProfile.bio}}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="content" v-else ref="scrollContainer">
          用户没数据
        </div>
        <div id="footer">
          <p v-if="!noMore">
            <svg class="circular" viewBox="0 0 50 50">
              <circle class="path" cx="25" cy="25" r="20" fill="none"/>
            </svg>
          </p>
          <p v-if="noMore">没有更多用户...</p>
        </div>
        <el-dialog
            v-if="showUser"
            v-model="show"
            :title="`用户：${showUser.nickname}-主页`" width="500"
            :modal="false"
            style="
      height: 82vh;
      overflow-y: scroll;
      "
            top="10vh"
            destroy-on-close
        >
          <div id="userShow">
            <div id="top">
              <div id="avatar">
                <el-avatar v-if="showUser.avatars" @click="handlePictureCardPreviewImg(showUser.avatars.avatarPath + showUser.avatars.fileName)" :size="100" :src="showUser.avatars.avatarPath + showUser.avatars.fileName"/>
                <el-avatar v-else :size="100" :src="'/images/touxiang-1.jpg'"/>
              </div>
              <el-dialog title="头像预览" v-model="dialogVisibleImg"
                         width="700">
                <el-image w-full :src="imageUrl" style="height: 100%; width: 100%" alt="头像预览"/>
              </el-dialog>
            </div>
            <div id="bottom">
              <div id="userInfo">
                <div id="nickname">
                  <p>{{showUser.nickname}}</p>
                  <el-tag size="small" style="margin-right: 10px;margin-left: 5px;">LV {{showUser.level.level}}</el-tag>
                  <badge-assembly :level="showUser.level.level" :size="30" :level-name="showUser.level.levelName"/>
                </div>
                <div id="otherInformation">
                  <ul>
                    <li>
                      <p>简介</p>
                      <p>
                        <el-popover
                            title="简介"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            offset="-450"
                            :content="showUser.userProfile.bio"
                        >
                          <template #reference>
                            {{showUser.userProfile.bio}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>姓名</p>
                      <p>
                        <el-popover
                            title="姓名"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="'姓：'+showUser.profile.lastName+'&nbsp;&nbsp;名：'+showUser.profile.firstName"
                        >
                          <template #reference>
                            {{showUser.profile.lastName}}-{{showUser.profile.firstName}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>性别</p>
                      <p>{{showUser.gender}}</p>
                    </li>
                    <li>
                      <p>生日</p>
                      <p>{{showDateFormat(showUser.birthday)}}</p>
                    </li>
                    <li>
                      <p>来自</p>
                      <p>
                        <el-popover
                            title="来自"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="showUser.profile.region"
                        >
                          <template #reference>
                            {{showUser.profile.region}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>现居</p>
                      <p>
                        <el-popover
                            title="现居"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="showUser.profile.location"
                        >
                          <template #reference>
                            {{showUser.profile.location}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>年级</p>
                      <p>
                        <el-popover
                            title="年级"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="showUser.profile.graduationYear"
                        >
                          <template #reference>
                            {{showUser.profile.graduationYear}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>学院</p>
                      <p>
                        <el-popover
                            title="学院"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="showUser.profile.graduationDepartment"
                        >
                          <template #reference>
                            {{showUser.profile.graduationDepartment}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                    <li>
                      <p>专业</p>
                      <p>
                        <el-popover
                            title="专业"
                            placement="right"
                            :width="200"
                            trigger="hover"
                            :content="userInfo.profile.major"
                        >
                          <template #reference>
                            {{showUser.profile.major}}
                          </template>
                        </el-popover>
                      </p>
                    </li>
                  </ul>
                </div>
                <!--          <p style="text-align: right;margin-right: 15px;color: #999;padding-bottom: 20px;">更新时间: {{showDateFormat(userInfo.updateTime)}}</p>-->
              </div>
            </div>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import "@/assets/css/loading.css";
#box {
  height: calc(100vh - 70px - 20px);
  background-color: #f5f6f9;
  width: calc(100vw - 95.5%);

  #top {
    background-color: white;
    height: 10%;
    display: flex;
    align-items: center;
    width: 100%;

    .el-menu {
      padding-left: 20px;
      width: 100%;
    }

  }

  #bottom {
    margin-top: 10px;
    height: calc(90% - 10px);
    background-color: white;

    #post {
      padding: 10px;
      height: calc(100% - 10px - 20px);
      overflow: hidden auto;

      .skeleton {
        border-top: 1px solid #e3e3e3;
        margin-top: 5px;
        margin-bottom: 5px;

        &:hover {
          background-color: #f5f6f9;
        }

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
            //background-color: white;

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
    }

    .content {
      padding: 10px;
      height: calc(100% - 10px - 20px);
      overflow: hidden auto;

    }
  }

  #footer {
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
}
  #userBox {
    display: flex;
    align-items: center;
    padding: 10px;
    border-top: 1px solid #e3e3e3;

    &:hover {
      background-color: #f5f6f9;
    }

    #left {

    }

    #right {
      margin-left: 10px;
      #user-information {
        height: 50%;
      }
      #bio {
        margin-top: 10px;
        height: 50%;
        color: #666;
      }
    }


  }
  #userShow {
    height: 80vh;
    width: 100%;
    z-index: 999;

    #top {
      height: 20% ;
      width: 100% ;
      border-radius: 100px 100px 0 0;
      position: relative;
      background:-webkit-linear-gradient(top,skyblue,lightblue);

      #avatar {
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        height: 100px;
        width: 100px;
        border-radius: 50%;
        background-color: whitesmoke;
      }
    }

    #bottom {
      margin: 0;
      #userInfo {
        height: 500px;
        width: 100%;
        background:-webkit-linear-gradient(top,lightblue,white);

        #nickname {
          display: flex;
          justify-content: center;
          align-items: center;

          p {
            font-size: 20px;
          }
        }

        #otherInformation {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;

          ul {
            margin: 0;
            padding: 0;
            width: 100%;
            list-style-type: none ;

            li {
              text-align: center;
              width: 100%;
              display: flex;
              justify-content: center;
              align-items: center;
              //border-top: 1px solid black;

              p:first-child {
                margin-right: 20px;
                font-size: 16px;
              }
              p:last-child {
                font-size: 16px;
                overflow: hidden; /* 隐藏超出容器的内容 */
                white-space: nowrap; /* 防止文本换行 */
                text-overflow: ellipsis; /* 用省略号表示被截断的文本 */
                width: 25%;
              }
            }
          }
        }
      }
    }
}
</style>