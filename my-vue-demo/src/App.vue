<template>
  <div id="app">
<!--    <img src="./assets/img/bg1.jpg" style="">-->
    <router-view/>
  </div>
</template>
<script>
import $ from 'jquery'
import '@/assets/css/global.css'

export default {
  name: 'App',
  data() {
    return {}
  },
  mounted() {
    // 实现APP的高度占满全屏
    console.log($('#app').height($(window).height()))
  }
}
</script>
<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  position: relative;
}

/*隐藏滚轮*/
::-webkit-scrollbar {
  display: none;
}
</style>
