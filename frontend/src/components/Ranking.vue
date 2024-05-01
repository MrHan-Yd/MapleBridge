<script setup>

const props = defineProps({
  data: {
    type: Array,
    required: true
  },
  quantity: {
    type: Number,
    default: 10
  }
})

/* 降序排序 */
props.data.sort((a, b) => b.value - a.value);

</script>

<template>
  <div id="ranking">
    <ul v-for="index in quantity" :key="index">
      <li v-if="data[index - 1] !== undefined && index - 1 === 0" style="color: #ff0000">
        {{ index }}、{{ data[index - 1].name }}
      </li>
      <li v-else-if="data[index - 1] !== undefined && index - 1 === 1" style="color: #ff3232">
        {{ index }}、{{ data[index - 1].name }}
      </li>
      <li v-else-if="data[index - 1] !== undefined && index - 1 === 2" style="color: #fa5050">
        {{ index }}、{{ data[index - 2].name }}
      </li>
      <li v-else-if="data[index - 1] !== undefined">{{ index }}、{{ data[index - 1].name }}
      </li>
      <li v-else-if="data[index - 1] === undefined">{{ index }}、暂无排名</li>
    </ul>
  </div>

</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

#ranking {
  padding-left: 10px;

  ul {
    margin: 0;
    list-style: none;
    line-height: 35px;
    padding-left: 10px;
    height: 100%;
    width: 100%;

    li {
      width: 60%;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }
}
</style>