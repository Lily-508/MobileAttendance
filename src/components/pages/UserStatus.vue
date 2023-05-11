<template>
  <div>
    <el-descriptions title="个人信息" border size="medium" :column="1">
      <el-descriptions-item label="用户id">{{
        userdata.sId
      }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{
        userdata.sName
      }}</el-descriptions-item>
      <el-descriptions-item label="部门id">{{
        userdata.dId
      }}</el-descriptions-item>
      <el-descriptions-item label="性别">{{
        userdata.sSex || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{
        userdata.sEmail || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{
        userdata.sPhone || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="在职状态">{{
        userdata.sStatus || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="出生日期">{{
        userdata.sBirthday || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="雇佣日期">{{
        userdata.sHiredate || "未绑定"
      }}</el-descriptions-item>
      <el-descriptions-item label="权限类别"
        ><el-tag size="small">{{ type }}</el-tag></el-descriptions-item
      >
    </el-descriptions>
  </div>
</template>
<script>
export default {
  name: "UserStatus",
  props: ["userdata"],
  computed: {
    type() {
      console.log(this.userdata)
      if (this.userdata.sRight === 1) return "领导"
      else return "管理员"
    },
  },
}
</script>
