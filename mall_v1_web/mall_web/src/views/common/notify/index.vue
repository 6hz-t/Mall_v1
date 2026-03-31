<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.title" clearable size="small" placeholder="输入标题搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
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
      <el-table-column label="标题" prop="title" />
      <el-table-column label="内容" prop="content" />
      <el-table-column label="跳转地址" prop="linkUrl" />
      <el-table-column label="通知类型" prop="type">
        <template slot-scope="scope">
          <span v-if="scope.row.type === 1">excel导出</span>
          <span v-else>未暂停</span>
        </template>
      </el-table-column>
      <el-table-column label="阅读状态" prop="readStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.readStatus === 1">已读</span>
          <span v-else>未读</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','notify:edit','notify:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudNotify from '@/api/common/notify'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, name: null }
export default {
  name: 'Notify',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '通知', url: '/v1/commonNotify/searchByPage', crudMethod: { ...crudNotify }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      units: [],
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'notify:add'],
        edit: ['admin', 'notify:edit'],
        del: ['admin', 'notify:del']
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
