<template>
  <div>
    <div v-if="photoGroupId === null">
      <div class="my-code">点击分组查看图片详情</div>
    </div>
    <div v-else>
      <!--工具栏-->
      <div class="head-container">
        <div v-if="crud.props.searchToggle">
          <!-- 搜索 -->
          <el-input v-model="query.blurry" clearable size="small" placeholder="输入图片名称查询" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
          <rrOperation />
        </div>
      </div>
      <!--表单组件-->
      <div v-loading="tableLoading">
        <el-alert
          v-if="tableData === null || tableData.length <= 0"
          title="暂无数据"
          type="info"
          :closable="false"
          center
          show-icon
        />
        <el-row :gutter="5">
          <el-checkbox-group v-model="urls" :max="num">
            <el-col v-for="(item,index) in tableData" :key="index" :span="4">
              <el-card :body-style="{ padding: '5px' }">
                <el-image
                  style="width: 100%;height: 100px"
                  :src="item.url"
                  fit="contain"
                  :preview-src-list="[item.url]"
                  :z-index="999"
                />
                <div>
                  <el-checkbox class="material-name" :label="item.url">
                    选择
                  </el-checkbox>
                  <el-row>
                    <el-col :span="24" class="col-do">
                      <el-button type="text" size="medium" @click="materialDel(item)">删除</el-button>
                    </el-col>
                  </el-row>

                </div>
              </el-card>
            </el-col>
          </el-checkbox-group>
        </el-row>
        <!--分页组件-->
        <el-pagination
          :current-page.sync="page.pageNo"
          :page-sizes="[12, 24, 36]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          class="pagination"
          @size-change="sizeChange"
          @current-change="pageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { getPage, del } from '@/api/common/photo'
import { header } from '@crud/crud'
import rrOperation from '@crud/RR.operation'

export default {
  name: 'PhotoDetail',
  components: { rrOperation },
  mixins: [
    header()],
  props: {
    photoGroupId: null
  },
  data() {
    return {
      rules: {
      },
      tableData: [],
      urls: [],
      page: {
        total: 0, // 总页数
        pageNo: 1, // 当前页数
        pageSize: 12, // 每页显示多少条
        descs: 'create_time'// 降序字段
      },
      num: 5,
      width: 150,
      height: 150,
      tableLoading: false,
      permission: {
        add: ['admin', 'photo:add'],
        edit: ['admin', 'photo:edit'],
        del: ['admin', 'photo:del']
      }
    }
  },
  watch: {
    photoGroupId(val) {
      this.getPage(this.page)
      Object.assign({ photo: { id: this.photoGroupId }})
    }
  },
  mounted() {
    this.getPage(this.page)
  },
  methods: {
    materialDel(item) {
      const that = this
      this.$confirm('是否确认删除该图片？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        del(item.id).then(function() {
          that.getPage(that.page)
        })
      })
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
      this.tableLoading = true
      getPage(Object.assign({
        pageNo: page.pageNo,
        pageSize: page.pageSize,
        sort: 'create_time,desc'
      }, {
        photoGroupId: this.photoGroupId
      })).then(response => {
        const data = response.data
        this.page.total = data.totalCount
        this.page.pageNo = data.pageNo
        this.page.pageSize = data.pageSize
        this.tableData = data.data
        this.tableLoading = false
      }).catch(() => {
        this.tableLoading = false
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
