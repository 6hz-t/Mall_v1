<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.code" clearable size="small" placeholder="输入订单编码搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.userID" clearable size="small" placeholder="输入用户ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
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
      <el-table-column label="订单编码" prop="code" />
      <el-table-column label="用户ID" prop="userId" />
      <el-table-column label="用户名称" prop="userName" />
      <el-table-column label="总金额" prop="totalAmount" />
      <el-table-column label="付款金额" prop="paymentAmount" />
      <el-table-column prop="orderTime" label="下单时间">
        <template slot-scope="{row}">
          {{ row.orderTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="订单状态" prop="orderStatus">
        <template v-if="dict.order_status[scope.row.orderStatus]" slot-scope="scope">
          {{ dict.order_status[scope.row.orderStatus - 1].label }}
        </template>
      </el-table-column>
      <el-table-column label="支付状态" prop="payStatus">
        <template v-if="dict.pay_status[scope.row.payStatus]" slot-scope="scope">
          {{ dict.pay_status[scope.row.payStatus - 1].label }}
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','trade:edit','trade:del'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.orderStatus===2" type="primary" @click="toShipped([scope.row.id])">已发货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import crudTrade from '@/api/order/trade'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, name: null }
export default {
  name: 'Trade',
  components: { crudOperation, rrOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '订单', url: '/v1/trade/searchByPage', crudMethod: { ...crudTrade }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['order_status', 'pay_status'],
  data() {
    return {
      units: [],
      selection: [],
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'trade:add'],
        edit: ['admin', 'trade:edit'],
        del: ['admin', 'trade:del']
      }
    }
  },
  created() {
    this.crud.optShow.add = false
    this.crud.optShow.edit = false
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
    },
    toShipped(ids) {
      this.$confirm(`该订单确认已发货吗?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        crudTrade.shipped(ids)
          .then(() => {
            this.$message.success('发货成功')
          })
      }).catch(() => {
      })
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
