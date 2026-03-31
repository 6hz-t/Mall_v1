<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.jobName" clearable size="small" placeholder="输入定时任务名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <el-select v-model="query.pauseStatus" class="filter-item">
          <el-option :value="1" label="已暂停" />
          <el-option :value="0" label="未暂停" />
        </el-select>
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="150px">
        <el-form-item label="定时任务名称" prop="jobName">
          <el-input v-model="form.jobName" />
        </el-form-item>
        <el-form-item label="bean名称" prop="beanName">
          <el-input v-model="form.beanName" />
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName">
          <el-input v-model="form.methodName" />
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" />
        </el-form-item>
        <el-form-item label="参数" prop="params">
          <el-input v-model="form.params" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
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
      <el-table-column label="定时任务名称" prop="jobName" />
      <el-table-column label="bean名称" prop="beanName" />
      <el-table-column label="方法名称" prop="methodName" />
      <el-table-column label="cron表达式" prop="cronExpression" />
      <el-table-column label="参数" prop="params" />
      <el-table-column label="备注" prop="remark" />
      <el-table-column label="暂停状态" prop="pauseStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.pauseStatus === true">已暂停</span>
          <span v-else>未暂停</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','job:edit','job:del'])" label="操作" width="200px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="runNow(scope.row)">立即执行</el-button>
          <el-button type="text" @click="pause(scope.row)">暂停</el-button>
          <el-button type="text" @click="resume(scope.row)">恢复</el-button>
          <udOperation
            :data="scope.row"
            :permission="permission"
          />
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import crudJob, { runNow, resume, pause } from '@/api/common/job'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, jobName: null, beanName: null, cronExpression: null, methodName: null, params: null, remark: null, pauseStatus: null }
export default {
  name: 'Job',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '定时任务', url: '/v1/commonJob/searchByPage', crudMethod: { ...crudJob }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      units: [],
      rules: {
        jobName: [
          { required: true, message: '请输入定时任务名称', trigger: 'blur' }
        ],
        beanName: [
          { required: true, message: '请输入bean名称', trigger: 'blur' }
        ],
        cronExpression: [
          { required: true, message: '请输入cron表达式', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'job:add'],
        edit: ['admin', 'job:edit'],
        del: ['admin', 'job:del']
      }
    }
  },
  methods: {
    viewLog(row) {
      this.$router.push({
        name: '/common/jobLog',
        query: {
          jobId: row.id
        },
        newTab: true
      })
    },
    runNow(row) {
      const that = this
      runNow(row.id).then(function() {
        that.crud.toQuery()
        that.$notify({
          title: '消息通知',
          type: 'success',
          message: '立即执行任务成功'
        })
      })
    },
    pause(row) {
      const that = this
      pause(row.id).then(function() {
        that.crud.toQuery()
        that.$notify({
          title: '消息通知',
          type: 'success',
          message: '暂停任务成功'
        })
      })
    },
    resume(row) {
      const that = this
      resume(row.id).then(function() {
        that.crud.toQuery()
        that.$notify({
          title: '消息通知',
          type: 'success',
          message: '恢复任务成功'
        })
      })
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    }
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
