<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.name" clearable size="small" placeholder="输入商品名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="120px">
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" />
        </el-form-item>
        <el-form-item label="秒杀价" prop="price">
          <el-input v-model="form.price" />
        </el-form-item>
        <el-form-item label="预扣库存" prop="withHoldQuantity">
          <el-input v-model="form.withHoldQuantity" />
        </el-form-item>
        <el-form-item label="实际剩余库存" prop="remainQuantity">
          <el-input v-model="form.remainQuantity" />
        </el-form-item>
        <el-form-item label="秒杀开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择秒杀开始时间"
          />
        </el-form-item>
        <el-form-item label="秒杀结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择秒杀结束时间"
          />
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
      <el-table-column label="商品名称" prop="name" />
      <el-table-column label="分类" prop="categoryName" />
      <el-table-column label="单位" prop="unitName" />
      <el-table-column label="品牌" prop="brandName" />
      <el-table-column label="规格" prop="model" />
      <el-table-column label="原价" prop="costPrice" />
      <el-table-column label="秒杀价" prop="price" />
      <el-table-column label="预扣库存" prop="withHoldQuantity" />
      <el-table-column label="实际剩余库存" prop="remainQuantity" />
      <el-table-column label="秒杀开始时间" prop="startTime">
        <template slot-scope="{row}">
          {{ row.startTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="秒杀结束时间" prop="endTime">
        <template slot-scope="{row}">
          {{ row.endTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','seckillProduct:edit','seckillProduct:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudSeckillProduct from '@/api/marketing/seckillProduct'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, name: null }
export default {
  name: 'SeckillProduct',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '秒杀商品', url: '/v1/seckillProduct/searchByPage', crudMethod: { ...crudSeckillProduct }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        productId: [
          { required: true, message: '请输入商品ID', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入秒杀价格', trigger: 'blur' }
        ],
        withHoldQuantity: [
          { required: true, message: '请输入预扣库存', trigger: 'blur' }
        ],
        remainQuantity: [
          { required: true, message: '请输入实际剩余库存', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请输入秒杀开始时间', trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: '请输入秒杀结束时间', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'seckillProduct:add'],
        edit: ['admin', 'seckillProduct:edit'],
        del: ['admin', 'seckillProduct:del']
      }
    }
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
    selectProduct(data) {
      this.productId = data.id
      this.productName = data.name
      this.model = data.model
      this.unitName = data.unitName
      this.brandName = data.brandName
      this.categoryName = data.categoryName
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
