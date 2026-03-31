<template>
  <div>
    <el-dialog
      title="属性库"
      append-to-body
      :visible.sync="listDialogVisible"
      width="70%"
      @close="onClose"
    >
      <el-container>
        <el-col :xs="24" :sm="24" :md="10" :lg="11" :xl="11" style="margin-bottom: 10px">
          <el-card class="box-card">
            <div slot="header">
              <el-row>
                <div>
                  <!-- 搜索 -->
                  <el-input v-model="query.name" clearable size="small" placeholder="输入属性名称搜索" style="width: 150px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
                  <date-range-picker v-model="query.betweenTime" class="date-item" style="width: 230px;" />
                  <rrOperation />
                </div>
              </el-row>
              <el-row>
                <el-table ref="attributeTable" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="selectionChangeHandlerAttribute" @current-change="handleCurrentChangeAttribute">
                  <el-table-column :show-overflow-tooltip="true" prop="id" label="系统编号" />
                  <el-table-column :show-overflow-tooltip="true" prop="name" label="属性" />
                  <el-table-column label="创建日期" prop="createTime">
                    <template slot-scope="{row}">
                      {{ row.createTime | formatDate }}
                    </template>
                  </el-table-column>
                </el-table>
                <!--分页组件-->
                <pagination />
              </el-row>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="24" :md="14" :lg="13" :xl="13">
          <el-card class="box-card">
            <div slot="header">
              <el-table ref="attributeValueTable" :data="attributeValues" highlight-current-row style="width: 100%;" @selection-change="selectionChangeHandler" @current-change="handleCurrentChange">
                <el-table-column type="selection" width="55" />
                <el-table-column :show-overflow-tooltip="true" prop="id" label="系统编号" />
                <el-table-column :show-overflow-tooltip="true" prop="value" label="属性值" />
              </el-table>
            </div>
          </el-card>
        </el-col>
      </el-container>
      <span slot="footer" class="dialog-footer">
        <el-button @click="onClose">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import crudAttribute from '@/api/mall/attribute'
import crudAttributeValue from '@/api/mall/attributeValue'
import CRUD, { presenter, header, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'
import _ from 'lodash'

export default {
  name: 'SelectAttributeList',
  components: { rrOperation, DateRangePicker, pagination },
  cruds() {
    return CRUD({ title: '属性', url: '/v1/attribute/searchByPage', crudMethod: { ...crudAttribute }})
  },
  mixins: [presenter(), header(), crud()],
  props: {
    visible: {
      default: false
    },
    type: {
      default: 0
    }
  },
  data() {
    return {
      listDialogVisible: false,
      tableData: [],
      attributeValues: [],
      selection: [],
      tableLoading: false,
      permission: {
        add: ['admin', 'attribute:add'],
        edit: ['admin', 'attribute:edit'],
        del: ['admin', 'attribute:del']
      }
    }
  },
  watch: {
    visible(val) {
      this.listDialogVisible = val
    }
  },
  methods: {
    selectionChangeHandlerAttribute(val) {
      this.getAttributeDetail(val)
    },
    handleCurrentChangeAttribute(val) {
      this.getAttributeDetail(val)
    },
    getAttributeDetail(val) {
      const page = {
        pageSize: 1000,
        attributeId: val.id
      }
      crudAttributeValue.getPage(page).then(res => {
        if (res.data) {
          this.attributeValues = res.data.data
        } else {
          this.attributeValues = []
        }
      }).catch(() => { })
    },
    onSubmit() {
      if (!_.size(this.selection)) {
        this.$message.info('请选择要属性值')
        return
      }

      if (this.selection.length > 1) {
        this.$message.info('一次只能选择一个属性值')
      }
      this.$emit('addAttributeValue', this.selection[0], this.type)
      this.onClose()
    },
    selectionChangeHandler(val) {
      this.selection = val
    },
    handleCurrentChange(val) {
      this.selection = val
    },
    onClose() {
      this.listDialogVisible = false
      this.$emit('onClose', false, this.type)
      this.selection = []
    }

  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .el-icon-circle-close{
    color: red;
  }
  .material-name{
    padding: 8px 0px;
  }
  .col-do{
    text-align: center;
  }
  .button-do{
    padding: unset!important;
    font-size: 12px;
  }
</style>

