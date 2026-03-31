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
        <el-form-item label="轮播图" prop="url">
          <select-photo v-model="urls" type="image" :index="2" :num="1" :width="150" :height="150" @select="select" />
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
      <el-table-column label="排序" prop="sort" />
      <el-table-column label="轮播图片" prop="url">
        <template slot-scope="scope">
          <el-image
            style="width: 100%;height: 100px"
            :src="scope.row.url"
            fit="contain"
            :preview-src-list="[scope.row.url]"
            :z-index="999"
          />
        </template>

      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','indexCarouselImage:edit','indexCarouselImage:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudIndexCarouselImage from '@/api/mall/indexCarouselImage'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
import selectPhoto from '../product/selectPhoto'
const defaultForm = { id: null, urls: [], sort: 999 }
const sortField = ['sort,asc', 'create_time,desc']
export default {
  name: 'IndexCarouselImage',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination, selectPhoto },
  cruds() {
    return CRUD({ title: '首页轮播图', url: '/v1/indexCarouselImage/searchByPage', crudMethod: { ...crudIndexCarouselImage }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      urls: [],
      rules: {
        url: [
          { required: true, message: '请添加首页轮播图', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'indexCarouselImage:add'],
        edit: ['admin', 'indexCarouselImage:edit'],
        del: ['admin', 'indexCarouselImage:del']
      }
    }
  },
  created() {
    this.crud.optShow.edit = false
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
