<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.couponId" clearable size="small" placeholder="输入优惠券ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.userId" clearable size="small" placeholder="输入用户ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-select v-model="query.useStatus" placeholder="请选择">
          <el-option v-for="option in dict.use_status" :key="option.value" :label="option.label" :value="option.value" />
        </el-select>
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="450px"
    >
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="优惠券ID" prop="couponId">
          <el-input v-model="form.couponId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" style="width: 180px" />
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
      <el-table-column label="优惠券ID" prop="couponId" />
      <el-table-column label="优惠券名称" prop="couponName" />
      <el-table-column label="用户ID" prop="userId" />
      <el-table-column label="用户名称" prop="userName" />
      <el-table-column label="使用状态" prop="useStatus">
        <template v-if="dict.use_status[scope.row.useStatus]" slot-scope="scope">
          {{ dict.use_status[scope.row.useStatus].label }}
        </template>
      </el-table-column>
      <el-table-column label="使用时间" prop="useTime">
        <template slot-scope="{row}">
          {{ row.useTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="checkPer(['admin','couponUserReceive:edit','couponUserReceive:del'])"
        label="操作"
        width="130px"
        align="center"
        fixed="right"
      >
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
import crudCouponUserReceive from '@/api/marketing/couponUserReceive'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = {
  id: null,
  couponId: null,
  userId: null,
  useStatus: null,
  useTime: null
}
const sortField = ['create_time,desc']
export default {
  name: 'CouponUserReceive',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({
      title: '优惠券领取',
      url: '/v1/couponUserReceive/searchByPage',
      crudMethod: { ...crudCouponUserReceive },
      sortField: sortField
    })
  },
  // 数据字典
  dicts: ['use_status'],
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        couponId: [
          { required: true, message: '请输入优惠券ID', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '请输入用户ID，0表示所有用户', trigger: 'blur' }
        ],
        useStatus: [
          { required: true, message: '请输入使用状态 1:已使用 0:未使用', trigger: 'blur' }
        ],
        useTime: [
          { required: true, message: '请输入使用时间', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'couponUserReceive:add'],
        edit: ['admin', 'couponUserReceive:edit'],
        del: ['admin', 'couponUserReceive:del']
      }
    }
  },
  created() {
    this.crud.optShow.download = false
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
  ::v-deep .vue-treeselect__control, ::v-deep .vue-treeselect__placeholder, ::v-deep .vue-treeselect__single-value {
    height: 30px;
    line-height: 30px;
  }
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
  ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
