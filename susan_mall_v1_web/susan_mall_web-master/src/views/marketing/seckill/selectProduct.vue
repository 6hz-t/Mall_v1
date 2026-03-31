<template>
  <div>
    <div v-if="value">
      <ul class="el-upload-list el-upload-list--picture-card">
        <li tabindex="0" class="el-upload-list__item is-ready">
          <div>
            <img :src="value.image" alt="" class="el-upload-list__item-thumbnail">
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="deleteGood">
                <i class="el-icon-delete" />
              </span>
            </span>
          </div>
        </li>
      </ul>
    </div>
    <div v-else tabindex="0" class="el-upload el-upload--picture-card" @click="toSelete">
      <i class="el-icon-plus" />
    </div>
    <el-dialog :visible.sync="dialog" append-to-body width="80%" title="商品列表">
      <div class="app-container">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input v-model="query.name" clearable size="small" placeholder="输入商品名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
            <date-range-picker v-model="query.betweenTime" name="betweenTime" class="date-item" />
            <rrOperation />
          </div>
        </div>
        <!--表单组件-->

        <!--表格渲染-->
        <el-table v-loading="loading" :data="tableData" size="small" style="width: 100%;">
          <el-table-column prop="id" label="商品id" />
          <el-table-column prop="categoryName" label="分类" />
          <el-table-column prop="unitName" label="单位" />
          <el-table-column prop="brandName" label="品牌" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="model" label="规格" />
          <el-table-column prop="price" label="价格" />
          <el-table-column label="操作" width="185" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="doSelect(scope.row)"
              >选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <el-pagination
          :current-page.sync="page.pageNo"
          :page-sizes="[10, 20, 50]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          class="pagination"
          @size-change="sizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import crudProduct from '@/api/mall/product'
import rrOperation from '@crud/RR.operation'
import { presenter, header, crud } from '@crud/crud'
import DateRangePicker from '@/components/DateRangePicker'

export default {
  name: 'SelectProduct',
  components: { rrOperation, DateRangePicker },
  mixins: [presenter(), header(), crud()],
  props: {
    value: {
      type: String
    }
  },
  data() {
    return {
      loading: false,
      dialog: false,
      queryTypeOptions: [
        { key: 'storeName', display_name: '商品名称' }
      ],
      tableData: [],
      isAttr: false,
      permission: {
        add: ['admin', 'product:add'],
        edit: ['admin', 'product:edit'],
        del: ['admin', 'product:del']
      },
      page: {
        total: 0, // 总页数
        pageNo: 1, // 当前页数
        pageSize: 10 // 每页显示多少条
      }
    }
  },
  mounted() {
    this.getPage(this.page)
  },
  methods: {
    beforeInit() {
    },
    deleteGood() {
      const that = this
      this.$confirm('是否确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        that.$emit('selectProduct', null)
      })
    },
    doSelect(data) {
      this.dialog = false
      this.$emit('selectProduct', data)
    },
    toSelete() {
      this.dialog = true
    },
    sizeChange(val) {
      console.log(val)
      this.page.pageNo = 1
      this.page.pageSize = val
      this.getPage(this.page)
    },
    pageChange(val) {
      console.log(val)
      this.page.pageNo = val
      this.getPage(this.page)
    },
    getPage(page, params) {
      this.loading = true
      crudProduct.getPage(Object.assign({
        pageNo: page.pageNo,
        pageSize: page.pageSize,
        sort: 'create_time,desc'
      })).then(response => {
        const data = response.data
        this.page.total = data.totalCount
        this.page.pageNo = data.pageNo
        this.page.pageSize = data.pageSize
        this.tableData = data.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
</style>
