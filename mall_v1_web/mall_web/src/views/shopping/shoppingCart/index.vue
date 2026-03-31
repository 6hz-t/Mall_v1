<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.userId" clearable size="small" placeholder="输入用户ID" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.productId" clearable size="small" placeholder="输入商品ID" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model="form.quantity" />
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
      <el-table-column label="用户ID" prop="userId" />
      <el-table-column label="用户名称" prop="userName" />
      <el-table-column label="商品ID" prop="productId" />
      <el-table-column label="商品名称" prop="productName" />
      <el-table-column label="规格" prop="model" />
      <el-table-column label="数量" prop="quantity" />
      <el-table-column label="单价" prop="price" />
      <el-table-column label="总金额" prop="totalAmount" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','shoppingCart:edit','shoppingCart:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudShoppingCart from '@/api/shopping/shoppingCart'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, productId: null, userId: null, quantity: null }
const sortField = ['create_time,desc']
export default {
  name: 'ShoppingCart',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '购物车', url: '/v1/shoppingCart/searchByPage', crudMethod: { ...crudShoppingCart }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        userId: [
          { required: true, message: '请输入用户ID', trigger: 'blur' }
        ],
        productId: [
          { required: true, message: '请输入产品ID', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: '请输入购买数量', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'shoppingCart:add'],
        edit: ['admin', 'shoppingCart:edit'],
        del: ['admin', 'shoppingCart:del']
      }
    }
  },
  created() {
    this.crud.optShow.download = false
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      if (!this.form.id) {
        this.urls = []
      }
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    select(index, urls) {
      if (urls != null && urls.length > 0) {
        this.form.url = urls[0]
      }
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
