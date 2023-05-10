<template>
  <div id="loginbox">
    <el-card class="login-card" shadow="hover">
      <div slot="header" class="head">
        <h2>移动考勤后台管理系统</h2>
      </div>
      <el-form
        ref="loginForm"
        :model="loginForm"
        label-width="80px"
        :rules="rules"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="el-icon-user-solid"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="userpwd">
          <el-input
            v-model="loginForm.userpwd"
            prefix-icon="el-icon-lock"
            placeholder="请输入密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item prop="code" label="验证码">
          <el-col :span="11">
            <el-input
              v-model="loginForm.code"
              prefix-icon="el-icon-circle-check"
              placeholder="验证码"
            ></el-input>
          </el-col>
          <el-col :span="11">
            <img :src="codeUrl" @click="updateCode" />
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')"
            >登陆</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "loginMenu",
  data() {
    return {
      codeUrl: "",
      uuid: "",
      loginForm: {
        username: "100002",
        userpwd: "123456",
        code: "",
      },
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        userpwd: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" },
        ],
        code: [{ required: true, message: "验证码不能为空", trigger: "blur" }],
      },
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((vaild) => {
        if (vaild) {
          // 连接后端登录函数进行判断 使用axios
          this.$axiosNoToken({
            url: "/login",
            methods: "post",
            data: {
              uuid: this.uuid,
              code: this.loginForm.code,
              //用户名密码登陆
              loginType: 0,
              loginPlatform: "web",
              username: this.loginForm.username,
              password: this.loginForm.userpwd,
            },
            success: (response) => {
              console.log(response)
              if (response.data.code == 200) {
                window.sessionStorage.setItem("token", response.data.token)
                window.sessionStorage.setItem("staff", response.data.data)
                this.$message({
                  message: response.data.msg,
                  type: "success",
                })
              } else {
                this.$message({
                  message: `未知错误 ${response.data}`,
                  type: "error",
                })
              }
            },
            error: (err) => {
              this.$alert(err.msg, "登陆失败", {
                confirmButtonText: "确定",
              }).then(() => {
                this.loginForm.userpwd = ""
                this.loginForm.code = ""
                this.updateCode()
              })
            },
          })
        } else {
          return false
        }
      })
    },
    updateCode() {
      this.$axiosNoToken({
        url: "/captcha",
        methods: "get",
        responseType: "blob",
        success: (response) => {
          this.codeUrl = window.URL.createObjectURL(response.data)
          this.uuid = response.headers.uuid
        },
        error: (err) => {
          console.log(err)
          this.$message({
            message: "获取验证码失败",
            type: "error",
          })
        },
      })
    },
  },
  created() {
    this.updateCode()
  },
}
</script>

<style scoped>
@keyframes twinkle-opacity {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
#loginbox {
  text-align: center;
  color: #2c3e50;
  width: 550px;
  margin: 0 auto;
}
.head {
  animation: twinkle-opacity 1.5s linear 2s infinite alternate;
}
</style>
