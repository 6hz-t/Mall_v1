<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.type" class="filter-item">
          <el-option :value="1" label="热门商品" />
          <el-option :value="2" label="最新商品" />
          <el-option :value="3" label="秒杀商品" />
        </el-select>
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="商品类型" prop="type">
          <el-select v-model="form.type">
            <el-option :value="1" label="热门商品" />
            <el-option :value="2" label="最新商品" />
            <el-option :value="3" label="秒杀商品" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" />
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
      <el-table-column label="商品ID" prop="productId" />
      <el-table-column label="商品名称" prop="productName" />
      <el-table-column label="规格" prop="model" />
      <el-table-column label="价格" prop="price" />
      <el-table-column label="封面" prop="cover" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 100%;height: 100px"
            :src="scope.row.cover"
            fit="contain"
            :preview-src-list="[scope.row.cover]"
            :z-index="999"
          />
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sort" />
      <el-table-column label="商品类型" prop="type">
        <template slot-scope="scope">
          <span v-if="scope.row.type === 1">热门商品</span>
          <span v-else-if="scope.row.type === 2">最新商品</span>
          <span v-else>秒杀商品</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','indexProduct:edit','indexProduct:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudIndexProduct from '@/api/mall/indexProduct'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, productId: null, sort: 999, type: null }
export default {
  name: 'IndexProduct',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '首页商品', url: '/v1/indexProduct/searchByPage', crudMethod: { ...crudIndexProduct }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      urls: [],
      rules: {
        type: [
          { required: true, message: '请选择商品类型', trigger: 'blur' }
        ],
        productId: [
          { required: true, message: '请输入商品ID', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'indexProduct:add'],
        edit: ['admin', 'indexProduct:edit'],
        del: ['admin', 'indexProduct:del']
      }
    }
  },
  created() {
    this.crud.optShow.edit = false
    this.crud.optShow.download = false
    this.query.type = 1
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
    [CRUD.HOOK.beforeRefresh]() {
      if (!this.query.type) {
        this.query.type = 1
      }
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
