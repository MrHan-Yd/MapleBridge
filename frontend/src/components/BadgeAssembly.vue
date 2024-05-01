<script setup>
import {levelBadge} from "@/util/CurrentUtil";

const props = defineProps({
  level: {
    type: String,
    default: "1",
    required: true
  },
  levelName: {
    type: String,
    default: "新生",
    required: true
  },
  size: {
    type: Number,
    default: 25,
  },
});

</script>

<template>
  <el-popover
      :width="550"
      direction="vertical"
      :column="2"
      style="vertical-align: middle;"
      popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
  >
    <template #reference>
      <el-avatar style="vertical-align: middle;" :size="size" :src="levelBadge(level)"/>
    </template>
    <template #default>
      <div
          class="demo-rich-conent"
          style="display: flex; gap: 16px; flex-direction: column"
      >
        <el-avatar
            :size="60"
            :src="levelBadge(level)"
            style="margin-bottom: 8px"
        />
        <div>
          <p
              class="demo-rich-content__name"
              style="margin: 0; font-weight: 500"
          >
            <el-tag size="small">LV {{level}}</el-tag>
            {{levelName}}
          </p>
          <p
              class="demo-rich-content__mention"
              style="margin: 0; font-size: 14px; color: var(--el-color-info)"
          >

          </p>
        </div>

        <p class="demo-rich-content__desc" style="margin: 0">
          <el-descriptions
              title="等级说明"
              border
              :column="2"
              direction="vertical"
          >
            <el-descriptions-item label="等级信息" style="overflow-y: auto">
              <el-descriptions
                  :border="false"
                  :column="4"
                  size="small"
              >
                <el-descriptions-item>
                  <el-tag size="small">LV 1</el-tag>
                  <el-popover
                      placement="top-start"
                      title="新生"
                      :width="160"
                      trigger="hover"
                      content="经验值范围：0~150"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('1')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 2</el-tag>
                  <el-popover
                      placement="top-start"
                      title="学员"
                      :width="170"
                      trigger="hover"
                      content="经验值范围：151~450"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('2')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 3</el-tag>
                  <el-popover
                      placement="top-start"
                      title="活跃学员"
                      :width="170"
                      trigger="hover"
                      content="经验值范围：451~900"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('3')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 4</el-tag>
                  <el-popover
                      placement="top-start"
                      title="创作者"
                      :width="180"
                      trigger="hover"
                      content="经验值范围：901~1800"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('4')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 5</el-tag>
                  <el-popover
                      placement="top-start"
                      title="互动大师"
                      :width="190"
                      trigger="hover"
                      content="经验值范围：1801~3750"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('5')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 6</el-tag>
                  <el-popover
                      placement="top-start"
                      title="领导者"
                      :width="200"
                      trigger="hover"
                      content="经验值范围：3751~15000"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="70" :src="levelBadge('6')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-tag size="small">LV 7</el-tag>
                  <el-popover
                      placement="top-start"
                      title="行业专家"
                      :width="190"
                      trigger="hover"
                      content="经验值范围：15001以上"
                  >
                    <template #reference>
                      <el-avatar style="vertical-align: middle;margin-left:20px;" :size="120" :src="levelBadge('7')"/>
                    </template>
                  </el-popover>
                </el-descriptions-item>
              </el-descriptions>
            </el-descriptions-item>
          </el-descriptions>
        </p>
      </div>
    </template>
  </el-popover>
</template>

<style scoped>

</style>