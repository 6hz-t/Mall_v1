<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.userId" clearable size="small" placeholder="输入用户ID" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
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
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="120px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" />
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
          <el-input v-model="form.detailAddress" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="邮编" prop="postCode">
          <el-input v-model="form.postCode" />
        </el-form-item>
        <el-form-item label="是否默认地址" prop="isDefault">
          <el-radio
            v-for="item in dict.yes_or_no_status"
            :key="item.id"
            v-model="form.isDefault"
            :label="item.value === '1'"
          >
            {{ item.label }}
          </el-radio>
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
      <el-table-column label="收货人姓名" prop="receiverName" />
      <el-table-column label="收货人手机号" prop="receiverPhone" />
      <el-table-column label="省份" prop="province" />
      <el-table-column label="城市" prop="city" />
      <el-table-column label="区县" prop="district" />
      <el-table-column label="邮编" prop="postCode" />
      <el-table-column label="详细地址" prop="detailAddress" width="150" />
      <el-table-column label="是否默认地址" prop="isDefault">
        <template slot-scope="scope">
          <span v-if="scope.row.isDefault">是</span>
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
      <el-table-column
        v-if="checkPer(['admin','deliveryAddress:edit','deliveryAddress:del'])"
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
import crudDeliveryAddress from '@/api/shopping/deliveryAddress'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = {
  id: null,
  userId: null,
  receiverName: null,
  receiverPhone: null,
  province: null,
  city: null,
  district: null,
  postCode: null,
  detailAddress: null,
  isDefault: null
}
const sortField = ['create_time,desc']
export default {
  name: 'DeliveryAddress',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({
      title: '收货地址',
      url: '/v1/deliveryAddress/searchByPage',
      crudMethod: { ...crudDeliveryAddress },
      sortField: sortField
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['yes_or_no_status'],
  data() {
    return {

      rules: {
        userId: [
          { required: true, message: '请输入用户ID', trigger: 'blur' }
        ],
        receiverName: [
          { required: true, message: '请输入收货人姓名', trigger: 'blur' }
        ],
        receiverPhone: [
          { required: true, message: '请输入收货人手机号', trigger: 'blur' }
        ],
        province: [
          { required: true, message: '请输入省份', trigger: 'blur' }
        ],
        city: [
          { required: true, message: '请输入城市', trigger: 'blur' }
        ],
        district: [
          { required: true, message: '请输入区县', trigger: 'blur' }
        ],
        postCode: [
          { required: true, message: '请输入邮编', trigger: 'blur' }
        ],
        detailAddress: [
          { required: true, message: '请输入详细地址', trigger: 'blur' }
        ],
        isDefault: [
          { required: true, message: '请选择是否默认地址', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'deliveryAddress:add'],
        edit: ['admin', 'deliveryAddress:edit'],
        del: ['admin', 'deliveryAddress:del']
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
