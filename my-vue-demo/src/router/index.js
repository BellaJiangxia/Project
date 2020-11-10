import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/sys/login'
import Main from '@/components/sys/main'
import CaseManagement from '@/components/sys/case/caseManagement'
import CooperateOrgan from '@/components/sys/organ/cooperateOrgan'
import MyApply from '@/components/sys/process/apply/myApply'
import OrganApply from '@/components/sys/process/apply/organApply'
import ModifyApply from '@/components/sys/process/apply/modifyApply'

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
        {path:'/cooperateOrgan',component: CooperateOrgan},
        {path:'/myApply',component: MyApply},
        {path:'/organApply',component: OrganApply},
        {path:'/modifyApply',component: ModifyApply},
      ]
    }
  ]
})
