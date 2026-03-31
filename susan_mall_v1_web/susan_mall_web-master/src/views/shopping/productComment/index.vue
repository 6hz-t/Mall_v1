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
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="400px"
    >
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="父评论ID" prop="parentId">
          <el-input v-model="form.parentId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" style="width: 180px" />
        </el-form-item>
        <el-form-item label="评论内容" prop="content">
          <el-input v-model="form.content" type="textarea" />
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-input v-model="form.rating" />
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
      <el-table-column label="父评论ID" prop="parentId" />
      <el-table-column label="商品ID" prop="productId" />
      <el-table-column label="用户ID" prop="userId" />
      <el-table-column label="评论内容" prop="content" />
      <el-table-column label="评分" prop="rating" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="checkPer(['admin','productComment:edit','productComment:del'])"
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
import crudProductComment from '@/api/shopping/productComment'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = {
  id: null,
  parentId: null,
  productId: null,
  userId: null,
  content: null,
  rating: null
}
const sortField = ['create_time,desc']
export default {
  name: 'ProductComment',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({
      title: '商品评论',
      url: '/v1/productComment/searchByPage',
      crudMethod: { ...crudProductComment },
      sortField: sortField
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        parentId: [
          { required: true, message: '请输入父评论ID', trigger: 'blur' }
        ],
        productId: [
          { required: true, message: '请输入商品ID', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '请输入用户ID', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入评论内容', trigger: 'blur' }
        ],
        rating: [
          { required: true, message: '请输入评分', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'productComment:add'],
        edit: ['admin', 'productComment:edit'],
        del: ['admin', 'productComment:del']
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
