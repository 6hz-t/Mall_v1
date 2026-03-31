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
        <el-form-item label="订单ID" prop="tradeId">
          <el-input v-model="form.tradeId" />
        </el-form-item>
        <el-form-item label="订单编码" prop="tradeCode">
          <el-input v-model="form.tradeCode" />
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" />
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="规格" prop="model">
          <el-input v-model="form.model" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model="form.quantity" />
        </el-form-item>
        <el-form-item label="封面图片url" prop="coverUrl">
          <el-input v-model="form.coverUrl" />
        </el-form-item>
        <el-form-item label="总金额" prop="totalAmount">
          <el-input v-model="form.totalAmount" />
        </el-form-item>
        <el-form-item label="退款金额" prop="refundAmount">
          <el-input v-model="form.refundAmount" />
        </el-form-item>
        <el-form-item label="退货类型 10：退货退款 20：换货" prop="refundType">
          <el-input v-model="form.refundType" />
        </el-form-item>
        <el-form-item label="审核状态 10：待审核 20：已同意 30：已拒绝" prop="auditStatus">
          <el-input v-model="form.auditStatus" />
        </el-form-item>
        <el-form-item label="退货状态 10：进行中 20：已拒绝 30：已完成 40：已取消" prop="refundStatus">
          <el-input v-model="form.refundStatus" />
        </el-form-item>
        <el-form-item label="拒绝原因" prop="rejectedReason">
          <el-input v-model="form.rejectedReason" />
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
      <el-table-column label="订单编码" prop="tradeCode" />
      <el-table-column label="商品ID" prop="productId" />
      <el-table-column label="商品名称" prop="name" />
      <el-table-column label="规格" prop="model" />
      <el-table-column label="数量" prop="quantity" />
      <el-table-column label="封面图片url" prop="coverUrl" />
      <el-table-column label="总金额" prop="totalAmount" />
      <el-table-column label="退款金额" prop="refundAmount" />
      <el-table-column label="退货类型 " prop="refundType" />
      <el-table-column label="审核状态" prop="auditStatus" />
      <el-table-column label="退货状态" prop="refundStatus" />
      <el-table-column label="拒绝原因" prop="rejectedReason" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','refund:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudRefund from '@/api/aftersale/refund'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, tradeId: null, tradeCode: null, productId: null, name: null, model: null, quantity: null, coverUrl: null, totalAmount: null, refundAmount: null, refundType: null, auditStatus: null, refundStatus: null, rejectedReason: null }
const sortField = ['create_time,desc']
export default {
  name: 'Refund',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '退货单', url: '/v1/refund/searchByPage', crudMethod: { ...crudRefund }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        tradeId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }],
        tradeCode: [{ required: true, message: '请输入订单编码', trigger: 'blur' }],
        productId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        model: [{ required: true, message: '请输入规格', trigger: 'blur' }],
        quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
        coverUrl: [{ required: true, message: '请输入封面图片url', trigger: 'blur' }],
        totalAmount: [{ required: true, message: '请输入总金额', trigger: 'blur' }],
        refundAmount: [{ required: true, message: '请输入退款金额', trigger: 'blur' }],
        refundType: [{ required: true, message: '请输入退货类型 10：退货退款 20：换货', trigger: 'blur' }],
        auditStatus: [{ required: true, message: '请输入审核状态 10：待审核 20：已同意 30：已拒绝', trigger: 'blur' }],
        refundStatus: [{ required: true, message: '请输入退货状态 10：进行中 20：已拒绝 30：已完成 40：已取消', trigger: 'blur' }],
        rejectedReason: [{ required: true, message: '请输入拒绝原因', trigger: 'blur' }]
      },
      permission: {
        del: ['admin', 'refund:del']
      }
    }
  },
  created() {
    this.crud.optShow.download = false
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
