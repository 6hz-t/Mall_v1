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
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="订单ID" prop="tradeId">
          <el-input v-model="form.tradeId" />
        </el-form-item>
        <el-form-item label="订单编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" />
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="收货人姓名" prop="receiverName">
          <el-input v-model="form.receiverName" />
        </el-form-item>
        <el-form-item label="收货人手机号" prop="receiverPhone">
          <el-input v-model="form.receiverPhone" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="form.city" />
        </el-form-item>
        <el-form-item label="区县" prop="district">
          <el-input v-model="form.district" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input v-model="form.detailAddress" />
        </el-form-item>
        <el-form-item label="邮编" prop="postCode">
          <el-input v-model="form.postCode" />
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
      <el-table-column label="订单ID" prop="tradeId" />
      <el-table-column label="订单编码" prop="code" />
      <el-table-column label="用户ID" prop="userId" />
      <el-table-column label="用户名称" prop="userName" />
      <el-table-column label="收货人姓名" prop="receiverName" />
      <el-table-column label="收货人手机号" prop="receiverPhone" width="120" />
      <el-table-column label="省份" prop="province" />
      <el-table-column label="城市" prop="city" />
      <el-table-column label="区县" prop="district" />
      <el-table-column label="详细地址" prop="detailAddress" />
      <el-table-column label="邮编" prop="postCode" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin'])" label="操作" width="130px" align="center" fixed="right">
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
import crudTradeDeliveryAddress from '@/api/order/tradeDeliveryAddress'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, tradeId: null, code: null, userId: null, userName: null, receiverName: null, receiverPhone: null, province: null, city: null, district: null, detailAddress: null, postCode: null }
const sortField = ['create_time,desc']
export default {
  name: 'TradeDeliveryAddress',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '订单收货地址', url: '/v1/tradeDeliveryAddress/searchByPage', crudMethod: { ...crudTradeDeliveryAddress }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        tradeId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }],
        code: [{ required: true, message: '请输入订单编码', trigger: 'blur' }],
        userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
        userName: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
        receiverName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
        receiverPhone: [{ required: true, message: '请输入收货人手机号', trigger: 'blur' }],
        province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
        city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
        district: [{ required: true, message: '请输入区县', trigger: 'blur' }],
        detailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        postCode: [{ required: true, message: '请输入邮编', trigger: 'blur' }]
      },
      permission: {
        add: ['admin', 'tradeDeliveryAddress:add'],
        edit: ['admin', 'tradeDeliveryAddress:edit'],
        del: ['admin', 'tradeDeliveryAddress:del']
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
