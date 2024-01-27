<script setup>
import {useDark, useToggle} from "@vueuse/core" ;
/* element-plus深色模式适配*/
useDark({
  selector: 'html' ,
  attribute: 'class' ,
  valueDark: 'dark' ,
  valueLight: 'light'
}) ;
/*设置*/
useDark({
  /*当浏览器手动去切换颜色，同步*/
  onChanged(dark) {
    useToggle(dark) ;
  }
})
</script>

<template>
  <div>
    <router-view />
  </div>
</template>

<style scoped>

</style>
