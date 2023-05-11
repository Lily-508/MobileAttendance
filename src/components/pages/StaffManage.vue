<template>
  <div>
    <el-table :data="tableData" border height="650">
      <el-table-column prop="sId" label="员工id"></el-table-column>
      <el-table-column prop="dId" label="部门id"></el-table-column>
      <el-table-column prop="sName" label="员工名"></el-table-column>
      <el-table-column prop="sSex" label="性别"></el-table-column>
      <el-table-column prop="sPhone" label="手机号"></el-table-column>
      <el-table-column prop="sEmail" label="邮箱"></el-table-column>
      <el-table-column prop="sImei" label="imei号"> </el-table-column>
      <el-table-column prop="sStatus" label="在职状态"></el-table-column>
      <el-table-column prop="sRight" label="权限类别">
        <template slot-scope="scope">
          <el-tag size="medium">{{ sRightName(scope.row.sRight) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sBirthday" label="出生日期"></el-table-column>
      <el-table-column prop="sHiredate" label="入职日期"></el-table-column>
      <el-table-column label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button
            @click="handleEdit(scope.row), (showEdit = !showEdit)"
            type="primary"
            >编辑</el-button
          >
          <el-popconfirm
            title="确定删除该员工信息？"
            @confirm="handleDelete(scope.$index, scope.row)"
          >
            <el-button slot="reference" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :page-sizes="[5, 10, 20]"
      :page-size="pageSize"
      :current-page.sync="currentPage"
      layout="total,sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-dialog :visible.sync="showEdit" title="编辑员工信息" append-to-body>
      <el-form
        :model="dialogData"
        :rules="rules"
        ref="dialogData"
        label-width="100px"
      >
        <el-form-item label="部门id" prop="dId">
          <el-input
            v-model="dialogData.dId"
            placeholder="请输入新的部门id"
          ></el-input>
        </el-form-item>
        <el-form-item label="员工名" prop="sName">
          <el-input
            v-model="dialogData.sName"
            placeholder="请输入新的员工名"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="dialogData.sSex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="sPhone">
          <el-input
            v-model="dialogData.sPhone"
            placeholder="请输入新的手机号"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="sEmail">
          <el-input
            v-model="dialogData.sEmail"
            placeholder="请输入新的邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item label="在职状态">
          <el-radio-group v-model="dialogData.sStatus">
            <el-radio label="在职">在职</el-radio>
            <el-radio label="离职">离职</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限类别">
          <el-radio-group v-model="dialogData.sRight">
            <el-radio :label="0">普通员工</el-radio>
            <el-radio :label="1">领导</el-radio>
            <el-radio :label="2">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker
            v-model="dialogData.sBirthday"
            type="date"
            placeholder="选择新的出生日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="dialogData.sHiredate"
            type="date"
            placeholder="选择新的入职日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showEdit = false">取 消</el-button>
        <el-button type="primary" @click="comfirmEndit(dialogData)"
          >确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "StaffManage",
  data() {
    let checkmail = (rule, value, callback) => {
      let reg = /^\w+@\w+(\.\w+)+$/g
      if (reg.test(value)) {
        callback()
      } else {
        callback(new Error("请输入正确的邮箱"))
      }
    }
    let checkPhone = (rule, value, callback) => {
      let reg = /1[3|4|5|7|8][0-9]{9}$/g
      if (reg.test(value)) {
        callback()
      } else {
        callback(new Error("请输入正确的手机号"))
      }
    }
    return {
      rules: {
        dId: [{ required: true, message: "部门id不为空", trigger: "blur" }],
        sName: [{ required: true, message: "员工名不为空", trigger: "blur" }],
        sEmail: [
          { required: true, message: "员工邮箱不为空", trigger: "blur" },
          { validator: checkmail, trigger: "blur" },
        ],
        sPhone: [
          { required: true, message: "员工手机号不为空", trigger: "blur" },
          { validator: checkPhone, trigger: "blur" },
        ],
      },
      dialogData: {},
      tableData: [
        {
          sId: "2016-05-03",
          dId: "王小虎",
          sName: "上海市普陀区金沙江路 1518 弄",
          sSex: "2016-05-02",
          sPhone: "待审核",
          sEmail: "否",
          sImei: "0",
          sStatus: "是",
          sRight: "是",
          sBirthday: "是",
          sHiredate: "是",
        },
      ],
      showEdit: false,
      currentPage: 1,
      pageSize: 5,
      total: 0,
    }
  },
  created() {
    this.updateTable(this.currentPage, this.pageSize)
  },
  methods: {
    handleSizeChange(size) {
      this.currentPage = 1
      this.pageSize = size
      this.updateTable(this.currentPage, this.pageSize)
    },
    handleCurrentChange(currentPage) {
      this.updateTable(currentPage, this.pageSize)
    },
    sRightName(sRight) {
      switch (sRight) {
        case 0:
          return "普通员工"
        case 1:
          return "领导"
        case 2:
          return "管理员"
      }
    },
    handleEdit(row) {
      this.dialogData = row
    },
    comfirmEndit(row) {
      console.log(row)
      this.showEdit = !this.showEdit
      this.$axiosJwt({
        url: "/staffs",
        method: "put",
        data: row,
        success: (response) => {
          this.$message({
            message: response.data.msg,
            type: "success",
          })
        },
        error: (err) => {
          this.$message({
            message: err.msg,
            type: "error",
          })
        },
      })
    },
    handleDelete(index, row) {
      this.tableData.splice(index, 1)
      // 发送axios请求
      this.$axiosJwt({
        url: "/staffs",
        method: "delete",
        params: {
          sId: row.sId,
        },
        success: (response) => {
          this.$message({
            message: response.data.msg,
            type: "success",
          })
          // this.gettableData()
        },
      })
    },
    updateTable(pageCur, pageSize) {
      this.$axiosJwt({
        url: "/staffs/page",
        method: "get",
        params: {
          pageCur,
          pageSize,
        },
        success: (response) => {
          console.log(response)
          this.tableData = response.data.data.records
          this.currentPage = pageCur
          this.pageSize = pageSize
          this.total = response.data.data.total
        },
      })
    },
    gettableData() {
      if (this.tableData.length < this.currentPage * this.pageSize) {
        this.javaPageCur += 1
        this.updateTable(this.javaPageCur, this.pageSize)
      }
      return this.tableData.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      )
    },
  },
}
</script>
