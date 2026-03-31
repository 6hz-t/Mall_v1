<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.jobName" clearable size="small" placeholder="输入定时任务名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      row-key="id"
      @select="crud.selectChange"
      @select-all="crud.selectAllChange"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column :selectable="checkboxT" type="selection" width="55" />
      <el-table-column label="系统编号" prop="id" />
      <el-table-column label="定时任务ID" prop="jobId" />
      <el-table-column label="定时任务名称" prop="jobName" />
      <el-table-column label="bean名称" prop="beanName" />
      <el-table-column label="开始执行时间" prop="startTime">
        <template slot-scope="{row}">
          {{ row.startTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="结束执行时间" prop="endTime">
        <template slot-scope="{row}">
          {{ row.endTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="异常信息" prop="exception" />
      <el-table-column label="执行状态" prop="runStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.runStatus === 1" style="color:blue">执行中</span>
          <span v-else-if="scope.row.runStatus === 2" style="color:yellow">暂停</span>
          <span v-else-if="scope.row.runStatus === 3" style="color:green">成功</span>
          <span v-else-if="scope.row.runStatus === 4" style="color:red">失败</span>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import crudJobLog from '@/api/common/jobLog'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'

export default {
  name: 'JobLog',
  components: { crudOperation, rrOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '定时任务执行日志', url: '/v1/commonJobLog/searchByPage', crudMethod: { ...crudJobLog }})
  },
  mixins: [presenter(), header(), crud()],
  data() {
    return {
      rules: {
      },
      permission: {
      }
    }
  },
  methods: {
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .vue-treeselect__control,::v-deep .vue-treeselect__placeholder,::v-deep .vue-treeselect__single-value {
    height: 30px;
    line-height: 30px;
  }
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
