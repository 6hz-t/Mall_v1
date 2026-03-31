<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.productGroupId" clearable size="small" placeholder="输入商品组ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.brandId" clearable size="small" placeholder="输入品牌ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.id" clearable size="small" placeholder="输入商品ID搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model="query.name" clearable size="small" placeholder="输入商品名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.betweenTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="800px">
      <el-form ref="form" inline :model="form" :rules="rules" size="small" label-width="100px">
        <el-row>
          <el-col :md="12">
            <el-form-item label="分类" prop="categoryId">
              <treeselect
                v-model="form.categoryId"
                :options="categorys"
                :load-options="loadCategorys"
                :normalizer="normalizer"
                style="width: 180px"
                placeholder="选择分类"
              />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="单位" prop="unitId">
              <el-select
                v-model="form.unitId"
                style="width: 180px"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in units"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="品牌" prop="brandId">
              <el-select
                v-model="form.brandId"
                style="width: 180px"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in brands"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="数量" prop="quantity">
              <el-input v-model="form.quantity" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="商品组属性" prop="spuAttributeValueIds">
              <attribute-list ref="spuAttributeListForm" :attribute-entity-list="spuAttributeEntityList" :type="1" @onClose="onClose" />
              <select-attribute-value ref="spuSelectAttributeValueForm" :visible="visibleSpu" :type="1" @addAttributeValue="addAttributeValue" @onClose="onClose" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :md="12">
            <el-form-item label="商品属性" prop="skuAttributeValueIds">
              <attribute-list ref="skuAttributeListForm" :attribute-entity-list="skuAttributeEntityList" :type="2" @onClose="onClose" />
              <select-attribute-value ref="skuSelectAttributeValueForm" :visible="visibleSku" :type="2" @addAttributeValue="addAttributeValue" @onClose="onClose" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="封面图片" prop="cover">
            <select-photo v-model="cover" type="image" :index="1" :num="1" :width="150" :height="150" @select="select" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="轮播图" prop="swiper">
            <select-photo v-model="swiper" type="image" :index="2" :num="4" :width="150" :height="150" @select="select" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="商品详情：">
            <vue-editor v-model="form.detail" />
          </el-form-item>
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
      <el-table-column label="商品组ID" prop="productGroupId" />
      <el-table-column label="商品名称" prop="name" />
      <el-table-column label="分类" prop="categoryName" />
      <el-table-column label="单位" prop="unitName" />
      <el-table-column label="品牌" prop="brandName" />
      <el-table-column label="规格" prop="model" />
      <el-table-column label="数量" prop="quantity" />
      <el-table-column label="价格" prop="price" />
      <el-table-column label="hash值" prop="hash" />
      <el-table-column prop="createUserId" label="创建人ID" />
      <el-table-column prop="createUserName" label="创建人名称" />
      <el-table-column prop="createTime" label="创建日期">
        <template slot-scope="{row}">
          {{ row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','product:edit','product:del'])" label="操作" width="130px" align="center" fixed="right">
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
import crudProduct from '@/api/mall/product'
import crudUnit from '@/api/mall/unit'
import crudBrand from '@/api/mall/brand'
import crudCategory from '@/api/mall/category'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
import { VueEditor } from 'vue2-editor'
import selectPhoto from './selectPhoto'
import attributeList from './attributeList'
import selectAttributeValue from './selectAttributeValue'
import _ from 'lodash'

const defaultForm = { id: null, name: null, categoryId: null, brandId: null, unitId: null, quantity: null, price: null, attributeValueIds: null, detail: null }
export default {
  name: 'Product',
  components: { Treeselect, crudOperation, rrOperation, udOperation, DateRangePicker, pagination, VueEditor, selectPhoto, attributeList, selectAttributeValue },
  cruds() {
    return CRUD({ title: '商品', url: '/v1/product/searchByPage', crudMethod: { ...crudProduct }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      categorys: [],
      units: [],
      brands: [],
      spuAttributeEntityList: [],
      skuAttributeEntityList: [],
      rules: {
        categoryId: [
          { required: true, message: '请输入分类ID', trigger: 'blur' }
        ],
        unitId: [
          { required: true, message: '请输入单位ID', trigger: 'blur' }
        ],
        brandId: [
          { required: true, message: '请输入品牌ID', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: '请输入数量', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ]
      },
      myConfig: {
        autoHeightEnabled: false, // 编辑器不自动被内容撑高
        initialFrameHeight: 500, // 初始容器高度
        initialFrameWidth: '100%', // 初始容器宽度
        UEDITOR_HOME_URL: '/UEditor/',
        serverUrl: ''
      },
      cover: [],
      swiper: [],
      visibleSpu: false,
      visibleSku: false,
      permission: {
        add: ['admin', 'product:add'],
        edit: ['admin', 'product:edit'],
        del: ['admin', 'product:del']
      }
    }
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getUnits()
      this.getBrands()
      if (this.form.id) {
        this.getInfo(this.form.id)
        this.getCategorys(null)
      } else {
        this.getCategorys(0)
        this.cover = []
        this.swiper = []
        this.spuAttributeEntityList = []
        this.skuAttributeEntityList = []
      }
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
      if (!_.size(this.skuAttributeEntityList)) {
        this.$message.info('请添加商品属性')
        return false
      }
      this.$set(this.form, 'cover', this.cover)
      this.$set(this.form, 'swiper', this.swiper)
      this.$set(this.form, 'skuAttributeEntityList', this.skuAttributeEntityList)
      if (_.size(this.spuAttributeEntityList)) {
        this.$set(this.form, 'spuAttributeEntityList', this.spuAttributeEntityList)
      }
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    loadCategorys({ action, parentNode, callback }) {
      const param = {
        parentId: parentNode.id
      }
      crudCategory.getCategoryTree(param).then(res => {
        parentNode.children = res.data.map(function(obj) {
          if (obj.hasChildren) {
            obj.children = null
          }
          return obj
        })
        setTimeout(() => {
          callback()
        }, 200)
      })
    },
    getCategorys(categoryId) {
      const param = {
        queryTree: categoryId === null,
        parentId: categoryId
      }
      crudCategory.getCategoryTree(param).then(res => {
        this.categorys = res.data.map(function(obj) {
          return obj
        })
      })
    },
    normalizer(node) {
      if (!node.hasChildren) {
        delete node.children
      }
    },
    getUnits() {
      const page = {
        pageSize: 1000
      }
      crudUnit.getPage(page).then(res => {
        if (res.data) {
          this.units = res.data.data
        } else {
          this.units = []
        }
      }).catch(() => { })
    },
    getBrands() {
      const page = {
        pageSize: 1000
      }
      crudBrand.getPage(page).then(res => {
        if (res.data) {
          this.brands = res.data.data
        } else {
          this.brands = []
        }
      }).catch(() => { })
    },
    select(index, urls) {
      if (index === 1) {
        this.cover = urls
      } else {
        this.swiper = urls
      }
    },
    onClose(val, type) {
      if (type === 1) {
        this.visibleSpu = val
      } else if (type === 2) {
        this.visibleSku = val
      }
    },
    addAttributeValue(val, type) {
      if (type === 1) {
        this.spuAttributeEntityList.push(val)
      } else if (type === 2) {
        this.skuAttributeEntityList.push(val)
      }
    },
    getInfo(id) {
      crudProduct.getDetail(id)
        .then(response => {
          const data = response.data
          this.cover = data.cover
          this.swiper = data.swiper
          this.form.detail = data.detail
          this.form.spuAttributeValueIds = data.spuAttributeValueIds
          this.form.skuAttributeValueIds = data.skuAttributeValueIds
          this.spuAttributeEntityList = data.spuAttributeEntityList
          this.skuAttributeEntityList = data.skuAttributeEntityList
        })
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
