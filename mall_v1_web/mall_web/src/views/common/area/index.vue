<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.name" clearable size="small" placeholder="输入名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.parentId" clearable size="small" placeholder="输入上级ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="上一级ID" prop="parentId">
          <el-input v-model="form.parentId" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="拼音" prop="pinyin">
          <el-input v-model="form.pinyin" />
        </el-form-item>
        <el-form-item label="名称" prop="fullName">
          <el-input v-model="form.fullName" />
        </el-form-item>
        <el-form-item label="行政编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-input v-model="form.level" />
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
      <el-table-column label="上一级ID" prop="parentId" />
      <el-table-column label="名称" prop="name" />
      <el-table-column label="拼音" prop="pinyin" />
      <el-table-column label="名称" prop="fullName" />
      <el-table-column label="行政编码" prop="code" />
      <el-table-column label="级别" prop="level" />
      <el-table-column label="创建人ID" prop="createUserId" />
      <el-table-column label="创建人名称" prop="createUserName" />
      <el-table-column label="创建日期" prop="createTime">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','commonArea:edit','commonArea:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudCommonArea from '@/api/common/area'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
const defaultForm = { id: null, parentId: null, name: null, pinyin: null, fullName: null, code: null, level: null }
const sortField = ['create_time,desc']
export default {
  name: 'CommonArea',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '地区', url: '/v1/commonArea/searchByPage', crudMethod: { ...crudCommonArea }, sortField: sortField })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        parentId: [{ required: true, message: '请输入上一级ID', trigger: 'blur' }],
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        pinyin: [{ required: true, message: '请输入拼音', trigger: 'blur' }],
        fullName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入行政编码', trigger: 'blur' }],
        level: [{ required: true, message: '请输入级别', trigger: 'blur' }]
      },
      permission: {
        add: ['admin', 'commonArea:add'],
        edit: ['admin', 'commonArea:edit'],
        del: ['admin', 'commonArea:del']
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
