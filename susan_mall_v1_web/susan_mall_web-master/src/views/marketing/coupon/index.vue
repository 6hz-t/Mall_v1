<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.type" placeholder="请选择">
          <el-option v-for="option in dict.coupon_type" :key="option.value" :label="option.label" :value="option.value" />
        </el-select>
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
      width="800px"
    >
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="120px">
        <el-row>
          <el-col :md="12">
            <el-form-item label="优惠券名称" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option v-for="option in dict.coupon_type" :key="option.value" :label="option.label" :value="option.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="图片" prop="photoUrl">
            <select-photo v-model="urls" type="image" :index="2" :num="1" :width="150" :height="150" @select="select" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="领券开始时间" prop="receiveStartTime">
              <el-date-picker v-model="form.receiveStartTime" type="datetime" placeholder="输入领券开始时间" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="领券结束时间" prop="receiveEndTime">
              <el-date-picker v-model="form.receiveEndTime" type="datetime" placeholder="输入领券结束时间" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="使用开始时间" prop="useStartTime">
              <el-date-picker v-model="form.useStartTime" type="datetime" placeholder="输入使用开始时间" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="使用结束时间" prop="useEndTime">
              <el-date-picker v-model="form.useEndTime" type="datetime" placeholder="输入使用结束时间" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="优惠券数量" prop="quantity">
              <el-input v-model="form.quantity" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="优惠金额" prop="offMoney">
              <el-input v-model="form.offMoney" placeholder="满100，减40，填40" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="折扣" prop="discount">
              <el-input v-model="form.discount" placeholder="8折，就填入80" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="最低使用金额" prop="minMoney">
              <el-input v-model="form.minMoney" placeholder="满100，减40，填100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="最少商品件数" prop="minProductCount">
              <el-input v-model="form.minProductCount" placeholder="比如：2件或者3件" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="每日限额" prop="limitCountOneDay">
              <el-input v-model="form.limitCountOneDay" placeholder="每天领取多少张优惠券" />
            </el-form-item>
          </el-col>
        </el-row>
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
      <el-table-column label="优惠券名称" prop="name" />
      <el-table-column label="类型" prop="type">
        <template v-if="dict.coupon_type[scope.row.type]" slot-scope="scope">
          {{ dict.coupon_type[scope.row.type].label }}
        </template>
      </el-table-column>
      <el-table-column label="图片" prop="photoUrl">
        <template slot-scope="scope">
          <el-image
            style="width: 100%;height: 100px"
            :src="scope.row.photoUrl"
            fit="contain"
            :preview-src-list="[scope.row.photoUrl]"
            :z-index="999"
          />
        </template>
      </el-table-column>
      <el-table-column label="领券开始时间" prop="receiveStartTime" width="120">
        <template slot-scope="{row}">
          {{ row.receiveStartTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="领券结束时间" prop="receiveEndTime" width="120">
        <template slot-scope="{row}">
          {{ row.receiveEndTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="使用开始时间" prop="useStartTime" width="120">
        <template slot-scope="{row}">
          {{ row.useStartTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="使用结束时间" prop="useEndTime" width="120">
        <template slot-scope="{row}">
          {{ row.useEndTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="优惠券数量" prop="quantity" />
      <el-table-column label="优惠金额" prop="offMoney" />
      <el-table-column label="折扣" prop="discount" />
      <el-table-column label="最低使用金额" prop="minMoney" width="120" />
      <el-table-column label="最少商品件数" prop="minProductCount" width="120" />
      <el-table-column label="每日限额" prop="limitCountOneDay" />
      <el-table-column label="有效状态" prop="validStatus">
        <template v-if="dict.valid_status[scope.row.validStatus]" slot-scope="scope">
          {{ dict.valid_status[scope.row.validStatus].label }}
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
        v-if="checkPer(['admin','coupon:edit','coupon:del'])"
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
import crudCoupon from '@/api/marketing/coupon'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
import selectPhoto from '@/views/mall/product/selectPhoto'

const defaultForm = {
  id: null,
  name: null,
  type: null,
  photoUrl: null,
  receiveStartTime: null,
  receiveEndTime: null,
  useStartTime: null,
  useEndTime: null,
  quantity: null,
  offMoney: null,
  discount: null,
  minMoney: null,
  minProductCount: null,
  limitCountOneDay: null,
  validStatus: null
}
const sortField = ['create_time,desc']
export default {
  name: 'Coupon',
  components: { crudOperation, rrOperation, udOperation, DateRangePicker, pagination, selectPhoto },
  cruds() {
    return CRUD({
      title: '优惠券',
      url: '/v1/coupon/searchByPage',
      crudMethod: { ...crudCoupon },
      sortField: sortField
    })
  },
  // 数据字典
  dicts: ['coupon_type', 'valid_status'],
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      urls: [],
      rules: {
        name: [
          { required: true, message: '请输入优惠券名称', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'blur' }
        ],
        photoUrl: [
          { required: true, message: '请上传图片', trigger: 'blur' }
        ],
        receiveStartTime: [
          { required: true, message: '请输入领券开始时间', trigger: 'blur' }
        ],
        receiveEndTime: [
          { required: true, message: '请输入领券结束时间', trigger: 'blur' }
        ],
        useStartTime: [
          { required: true, message: '请输入使用开始时间', trigger: 'blur' }
        ],
        useEndTime: [
          { required: true, message: '请输入使用结束时间', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: '请输入优惠券数量', trigger: 'blur' }
        ],
        limitCountOneDay: [
          { required: true, message: '请输入每日限额', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'coupon:add'],
        edit: ['admin', 'coupon:edit'],
        del: ['admin', 'coupon:del']
      }
    }
  },
  created() {
    this.crud.optShow.download = false
  },
  methods: {
    select(index, urls) {
      if (urls != null && urls.length > 0) {
        this.form.photoUrl = urls[0]
      }
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      if (form.id) {
        this.urls = [this.form.photoUrl]
        this.form.type = this.form.type + ''
      }
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    // 改变状态
    changeEnabled(data, val) {
      const index = val ? 1 : 0
      this.$confirm('此操作将 "' + this.dict.label.valid_status[index] + '" ' + data.name + '优惠券, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudCoupon.edit(data).then(() => {
          this.crud.notify(this.dict.label.valid_status[index] + '成功', 'success')
        })
      })
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
