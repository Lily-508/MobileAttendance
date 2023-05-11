<template>
  <el-container>
    <el-header>
      <el-button
        @click="collapse"
        style="height: 60px; background-color: inherit; float: left"
        type="text"
      >
        <i :class="collapseClass" style="font-size: 20px"></i>
      </el-button>
      你好，{{ userdata.sName }} !!! 欢迎登录后台管理系统
      <el-dropdown @command="handleCommand" style="float: right">
        <i class="el-icon-switch-button" style="font-size: 20px"> </i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="exit">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-header>

    <el-container>
      <el-menu
        class="el-menu-vertical-demo"
        :default-active="this.$route.path"
        router
        :collapse="isCollapse"
        background-color="#242f42"
        text-color="#ffffff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/user/status">
          <i class="el-icon-user-solid"></i>
          <span slot="title">个人信息</span>
        </el-menu-item>
        <el-menu-item index="/user/staff">
          <i class="el-icon-s-management"></i>
          <span slot="title">员工管理</span>
        </el-menu-item>
        <el-submenu index="/user/attendance">
          <template slot="title">
            <i class="el-icon-s-grid"></i>
            <span slot="title">考勤管理</span></template
          >
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">考勤规则管理</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/record">
            <i class="el-icon-edit"></i>
            <span slot="title">考勤记录管理</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu index="/user/notice">
          <template slot="title">
            <i class="el-icon-s-grid"></i>
            <span slot="title">公告管理</span></template
          >
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">公告查看</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/record">
            <i class="el-icon-edit"></i>
            <span slot="title">公告发布</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu index="/user/record">
          <template slot="title">
            <i class="el-icon-s-grid"></i>
            <span slot="title">拜访管理</span></template
          >
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">拜访规则查看</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">拜访规则发布</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">拜访计划查看</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/record">
            <i class="el-icon-edit"></i>
            <span slot="title">拜访计划发布</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu index="/user/status">
          <template slot="title">
            <i class="el-icon-s-grid"></i>
            <span slot="title">事务管理</span></template
          >
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">事务申请</span>
          </el-menu-item>
          <el-menu-item index="/user/attendance/rule">
            <i class="el-icon-edit"></i>
            <span slot="title">事务审批</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>

      <el-container>
        <el-main>
          <router-view :userdata="userdata"></router-view>
        </el-main>
        <el-footer>移动考勤后台管理系统</el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>
<style scoped>
.el-header,
.el-footer {
  color: #ffffff;
  background-color: #242f42;
  text-align: center;
  line-height: 60px;
}
.el-menu--collapse,
.el-menu-item {
  border: none;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  border: none;
}
.el-main {
  background-color: #f0f0f0;
}
</style>
<script>
export default {
  name: "UserMenu",
  data() {
    return {
      userdata: {},
      isCollapse: false,
      collapseClass: "el-icon-s-unfold",
    }
  },
  methods: {
    handleCommand(command) {
      if (command === "exit") {
        this.$axiosJwt({
          url: "/logout",
          method: "get",
          success: (response) => {
            if (response.data.code === 200) {
              sessionStorage.clear()
              this.$router.push("/login")
            } else {
              console.log(response)
              this.$message({
                message: "登出失败请重试!",
                type: "error",
              })
            }
          },
        })
      }
    },
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) this.collapseClass = "el-icon-s-unfold"
      else this.collapseClass = "el-icon-s-fold"
    },
  },
  created() {
    this.userdata = JSON.parse(sessionStorage.getItem("staff"))
  },
  mounted() {
    document
      .querySelector(".el-container")
      .setAttribute("style", "min-height:" + window.screen.availHeight + "px")
  },
}
</script>
