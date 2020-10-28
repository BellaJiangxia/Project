<template>
  <div id="app">
    <img src="./assets/background.jpg" style="width:100%;height: 100%;">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {}
  },
  // fetch实现跨域
  created() {
    fetch("/api/user/showList", {
      method: "POST",
    }).then(res => {
      console.log(res);
    })
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
  width:100%;
  height: calc(100vh);
}
/*隐藏滚轮*/
::-webkit-scrollbar {
  display: none;
}
</style>
