<template>
  <el-form style="top:0px;float:left;position:absolute;height:113%">
    <el-col :span="12" style="width:245px;height:100%">
      <el-menu
        style="height:100%"
        default-active="2"
        v-for="(item,index) in menuArr"
        menu-trigger="click"
        ref="elMenu"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#519ED6"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-submenu class="submenu-popper" index="1">
          <template slot="title">
            <i class="el-icon-search" style="margin-left:-100px"></i>
            <span>病人病历</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" style="padding-left:35px">病例管理</el-menu-item>
            <el-menu-item index="1-2" style="padding-left:73px">合作机构病例库</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="2">
          <template slot="title">
            <i class="el-icon-share" style="margin-left:-100px"></i>
            <span>远诊申请</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="2-1" style="padding-left:35px">我的申请</el-menu-item>
            <el-menu-item index="2-2" style="padding-left:35px">机构申请</el-menu-item>
            <el-menu-item index="2-3" style="padding-left:63px">修改报告申请</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="3">
          <template slot="title">
            <i class="el-icon-document-checked" style="margin-left:-100px"></i>
            <span>远诊诊断</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="3-1" style="padding-left:18px">书写中</el-menu-item>
            <el-menu-item index="3-2" style="padding-left:35px">诊断申请</el-menu-item>
            <el-menu-item index="3-3" style="padding-left:35px">内部流转</el-menu-item>
            <el-menu-item index="3-4" style="padding-left:35px">我的报告</el-menu-item>
            <el-menu-item index="3-5" style="padding-left:35px">机构报告</el-menu-item>
            <el-menu-item index="3-6" style="padding-left:62px">我的修改申请</el-menu-item>
            <el-menu-item index="3-7" style="padding-left:35px">诊断统计</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="4">
          <template slot="title">
            <i class="el-icon-document-add" style="margin-left:-100px"></i>
            <span>机构转诊</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="4-1" style="padding-left:35px">转诊申请</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="5">
          <template slot="title">
            <i class="el-icon-service" style="margin-left:-100px"></i>
            <span>问诊服务</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="5-1" style="padding-left:35px">新的问诊</el-menu-item>
            <el-menu-item index="5-2" style="padding-left:35px">问诊回复</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="6">
          <template slot="title">
            <i class="el-icon-coffee-cup" style="margin-left:-100px"></i>
            <span>随访管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="6-1" style="padding-left:35px">批次管理</el-menu-item>
            <el-menu-item index="6-2" style="padding-left:35px">问卷管理</el-menu-item>
            <el-menu-item index="6-3" style="padding-left:35px">模板管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="7">
          <template slot="title">
            <i class="el-icon-thumb" style="margin-left:-100px"></i>
            <span>预约服务</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="7-1" style="padding-left:48px">申请的预约</el-menu-item>
            <el-menu-item index="7-2" style="padding-left:48px">受理的预约</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="8">
          <template slot="title">
            <i class="el-icon-share" style="margin-left:-100px"></i>
            <span>学习共享</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="8-1" style="padding-left:35px">分享检索</el-menu-item>
            <el-menu-item index="8-2" style="padding-left:35px">机构分享</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu class="submenu-popper" index="11">
          <template slot="title">
            <i class="el-icon-setting" style="margin-left:-100px"></i>
            <span>系统设置</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="11-1" style="padding-left:12px">设置1</el-menu-item>
            <el-menu-item index="11-2" style="padding-left:12px">设置2</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-col>
  </el-form>
</template>

<script>
import '@/assets/css/navMenu.css'

export default {
  data() {
    return {
      menuArr: [1],
      indexArr: []
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      const _this = this;
      const elMenu = _this.$refs['elMenu'];
      const popper = document.getElementsByClassName('submenu-popper');
      _this.indexArr.push(key);
      for (let i = 0; i < popper.length; i++) {
        //鼠标离开popper时，关闭popper
        popper[i].onmouseleave = function () {
          _this.closePopper();
        }
      }
      for (let i = 0; i < elMenu.length; i++) {
        //鼠标进入menu时，关闭popper
        elMenu[i].$el.onmouseenter = function () {
          _this.closePopper();
        }
      }
    },
    closePopper() {
      const elMenu = this.$refs['elMenu'];
      for (let i = 0; i < elMenu.length; i++) {
        this.indexArr.forEach(item => {
          elMenu[i].close(item)
        })
      }
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    }
  }
}
</script>
<style scoped>
</style>
