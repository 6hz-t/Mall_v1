<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="验证码" prop="smsCode">
          <el-input v-model="form.smsCode" />
        </el-form-item>
        <el-form-item label="有效期" prop="expireSecond">
          <el-input v-model="form.expireSecond" />
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
      <el-table-column label="手机号" prop="phone" />
      <el-table-column label="验证码" prop="smsCode" />
      <el-table-column label="有效期" prop="expireSecond" />
      <el-table-column label="发送时间" prop="sendTime" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','commonSmsRecord:edit','commonSmsRecord:del'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
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
import crudCommonSmsRecord from '@/api/common/smsRecord'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, phone: null, smsCode: null, expireSecond: null, sendTime: null }
const sortField = ['create_time,desc']
export default {
  name: 'CommonSmsRecord',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '短信发送记录', url: '/v1/commonSmsRecord/searchByPage', crudMethod: { ...crudCommonSmsRecord }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        smsCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
        expireSecond: [{ required: true, message: '请输入有效期', trigger: 'blur' }],
        sendTime: [{ required: true, message: '请输入发送时间', trigger: 'blur' }]
      },
      permission: {
        add: ['admin', 'commonSmsRecord:add'],
        edit: ['admin', 'commonSmsRecord:edit'],
        del: ['admin', 'commonSmsRecord:del']
      }
    }
  },
  created() {
    this.crud.optShow.download = false
    this.crud.optShow.add = false
    this.crud.optShow.edit = false
    this.crud.optShow.del = false
  },
  methods: {
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
