<template>
  <div>
    <el-table
      :data="
        tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      "
      border
      height="650"
    >
      <el-table-column prop="wuId" label="外派事务id"></el-table-column>
      <el-table-column prop="sId" label="员工id"></el-table-column>
      <el-table-column prop="aId" label="考勤规则id"> </el-table-column>
      <el-table-column prop="content" label="申请内容"></el-table-column>
      <el-table-column prop="startTime" label="开始时间"></el-table-column>
      <el-table-column prop="endTime" label="结束时间"></el-table-column>
      <el-table-column prop="total" label="总计(分钟)"></el-table-column>
      <el-table-column prop="result" label="审核结果"></el-table-column>
      <el-table-column label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button @click="updateWorkOutside(scope.row, true)" type="primary"
            >通过</el-button
          >
          <el-popconfirm
            title="确定不通过该外派申请？"
            @confirm="updateWorkOutside(scope.row, false)"
          >
            <el-button slot="reference" type="danger">不通过</el-button>
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
      :total="tableData.length"
    >
    </el-pagination>
  </div>
</template>
<script>
export default {
  name: "WorkOutsideManage",
  props: ["userdata"],
  data() {
    return {
      currentPage: 1,
      pageSize: 5,
      tableData: [
        {
          wuId: -1,
          sId: -1,
          aId: -1,
          content: "内容",
          startTime: "内容",
          endTime: "内容",
          total: "总计",
          result: "审核结果",
        },
      ],
    }
  },
  methods: {
    handleSizeChange(size) {
      this.currentPage = 1
      this.pageSize = size
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage
    },
    updateWorkOutside(row, agree) {
      console.log(row, agree)
      row.result = agree ? "通过" : "未通过"
      this.$axiosJwt({
        url: "/work-outside",
        method: "put",
        data: row,
        success: (response) => {
          this.$message.success(response.data.msg)
        },
      })
    },
    getTableData() {
      this.$axiosJwt({
        url: "/work-outside",
        method: "get",
        params: {
          reviewerId: this.userdata.sId,
        },
        success: (response) => {
          console.log(response)
          this.$message.success(response.data.msg)
          this.tableData = Array.from(response.data.data)
        },
      })
    },
  },
  created() {
    this.getTableData()
  },
}
</script>
