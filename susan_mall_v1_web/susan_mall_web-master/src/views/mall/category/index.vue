<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--侧边分类数据-->
      <el-col :xs="9" :sm="6" :md="5" :lg="4" :xl="4">
        <div class="head-container">
          <el-input
            v-model="name"
            clearable
            size="small"
            placeholder="输入分类名称搜索"
            prefix-icon="el-icon-search"
            class="filter-item"
            @input="getCategoryDatas"
          />
        </div>
        <el-tree
          :data="categoryDatas"
          :load="getCategoryDatas"
          :props="defaultProps"
          :expand-on-click-node="false"
          lazy
          @node-click="handleNodeClick"
        />
      </el-col>
      <!--用户数据-->
      <el-col :xs="15" :sm="18" :md="19" :lg="20" :xl="20">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input v-model="query.name" clearable size="small" placeholder="输入分类名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
            <date-range-picker v-model="query.betweenTime" class="date-item" />
            <rrOperation />
          </div>
          <crudOperation :permission="permission" />
        </div>
        <!--表单组件-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
          <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
            <el-form-item label="父分类ID" prop="parentId">
              <el-input v-model="form.parentId" placeholder="一级分类填0" />
            </el-form-item>
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" />
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
          <el-table-column label="分类名称" prop="name" />
          <el-table-column label="父分类ID" prop="parentId" />
          <el-table-column label="层级" prop="level" />
          <el-table-column label="是否叶子节点" prop="isLeaf">
            <template slot-scope="scope">
              <span v-if="scope.row.isLeaf === 1">是</span>
              <span v-else>否</span>
            </template>
          </el-table-column>
          <el-table-column label="创建人ID" prop="createUserId" />
          <el-table-column label="创建人名称" prop="createUserName" />
          <el-table-column label="创建日期" prop="createTime">
            <template slot-scope="{row}">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column v-if="checkPer(['admin','category:edit','category:del'])" label="操作" width="130px" align="center" fixed="right">
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
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudCategory from '@/api/mall/category'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import { getCategoryTree } from '@/api/mall/category'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, name: null, parentId: null }
export default {
  name: 'Category',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '分类', url: '/v1/category/searchByPage', crudMethod: { ...crudCategory }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      units: [],
      name: '', categoryDatas: [],
      defaultProps: { children: 'children', label: 'name', isLeaf: 'leaf' },
      rules: {
        parentId: [
          { required: true, message: '请输入父分类ID', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'category:add'],
        edit: ['admin', 'category:edit'],
        del: ['admin', 'category:del']
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
    handleNodeClick(data) {
      if (data.pid === 0) {
        this.query.parentId = 0
      } else {
        this.query.parentId = data.id
      }
      this.form.parentId = data.id
      this.crud.toQuery()
    },
    // 获取左侧分类数据
    getCategoryDatas(node, resolve) {
      const sort = 'id,desc'
      const params = { sort: sort }
      if (typeof node !== 'object') {
        if (node) {
          params['name'] = node
        }
      } else if (node.level !== 0) {
        params['parentId'] = node.data.id
      }
      setTimeout(() => {
        getCategoryTree(params).then(res => {
          if (resolve) {
            resolve(res.data)
          } else {
            this.categoryDatas = res.data
          }
        })
      }, 200)
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
