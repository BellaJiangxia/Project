import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/sys/login'
import Main from '@/components/sys/main'
import CaseManagement from '@/components/sys/case/caseManagement'
import CooperateOrgan from '@/components/sys/organ/cooperateOrgan'

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
      component: Main,
      children:[
        {path:'/caseManagement',component: CaseManagement},
        {path:'/cooperateOrgan',component: CooperateOrgan}
      ]
    }
  ]
})
