import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Main from '@/components/Main'

Vue.use(Router)

export default new Router({
  // vue默认hash模式，即地址栏url中的#符号，history没有#符号
  mode: 'history',
  routes: [
    {
      path: '/',
      // 主页面重定向到登录页面
      redirect:'/login',
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/main',
      name: 'main',
      component: Main
    }
  ]
})
