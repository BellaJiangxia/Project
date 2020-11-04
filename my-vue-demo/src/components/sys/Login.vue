<template>
  <el-form :rules="tableRules" ref="rulesForm" :model="rulesForm"
           style="display:flex;justify-content:center;z-index:9999;top:20%;right:10%;position:absolute">
    <el-card style="width: 310px;height:345px">
      <div slot="header" class="clearfix">
        <span>登录</span>
      </div>
      <el-form-item label="用户名：" prop="userName" style="margin-top:-10px">
        <el-input v-model="rulesForm.userName" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码：" prop="passWord" style="margin-top:-10px">
        <el-input v-model="rulesForm.passWord" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item style="margin-top:-5px">
        <el-button type="primary" @click="onSubmit">登录</el-button>
        <el-button @click="onRegister">注册</el-button>
      </el-form-item>
      <el-container style="margin-top:-10px">
        <el-link type="primary" style="margin-left:195px;">忘记密码?</el-link>
      </el-container>
    </el-card>
  </el-form>
</template>
<script>
export default {
  data() {
    const checkUser = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名不能为空"));
      } else {
        callback();
      }
    };
    const vapass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };
    return {
      errorMsg: "",
      displayStsates: 'none',
      tableRules: {
        userName: [{validator: checkUser, trigger: "blur"}],
        passWord: [{validator: vapass, trigger: "blur"}]
      },
      rulesForm: {
        userName: 'zhangsan',
        passWord: '123',
      },
    }
  },
  methods: {
    // 登录
    onSubmit() {
      const user = {
        userName: this.rulesForm.userName,
        passWord: this.rulesForm.passWord
      }
      // fetch实现跨域
      fetch("/api/user/login", {
        method: "POST",
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        // body: JSON.stringify({ username:userPhone, password: userPassword})
        body: JSON.stringify(user)
      }).then(response => {
        if (response.status == 200) {
          response.json().then(data => {
            if (data == true) {
              this.$router.push('/main')
            }
          })
        } else {
          return response.json().then(data => {
            this.$alert(data.message+',请检查你的用户名密码', '错误提示', {
              confirmButtonText: '确定',
              type: 'error'
            });
          })
        }
      })
    },
    // 注册
    onRegister() {
      alert(JSON.stringify(this.user))
    }
  }
}
</script>
<style scoped>
</style>
