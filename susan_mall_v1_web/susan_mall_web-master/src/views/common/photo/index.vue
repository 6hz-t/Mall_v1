<template>
  <div class="app-container">
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible="crud.status.cu > 0" :title="crud.status.title" width="500px">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="分组名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <!-- 字典列表 -->
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="10" :lg="11" :xl="11" style="margin-bottom: 10px">
        <el-card class="box-card">
          <!--工具栏-->
          <div class="head-container">
            <div v-if="crud.props.searchToggle">
              <!-- 搜索 -->
              <el-input v-model="query.name" clearable size="small" placeholder="输入分组名称搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
              <rrOperation />
            </div>
            <crudOperation :permission="permission" />
          </div>
          <!--表格渲染-->
          <el-table ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="crud.selectionChangeHandler" @current-change="handleCurrentChange">
            <el-table-column type="selection" width="55" />
            <el-table-column :show-overflow-tooltip="true" prop="id" label="系统编号" />
            <el-table-column :show-overflow-tooltip="true" prop="name" label="名称" />
            <el-table-column v-if="checkPer(['admin','photoGroup:edit','photoGroup:del'])" label="操作" width="130px" align="center" fixed="right">
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
        </el-card>
      </el-col>
      <!-- 字典详情列表 -->
      <el-col :xs="24" :sm="24" :md="14" :lg="13" :xl="13">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :span="12">
                <span>图片详情</span>
              </el-col>
              <el-col :span="12" style="text-align: right;">
                <el-upload
                  :action="url"
                  :headers="headers"
                  :file-list="[]"
                  :on-progress="handleProgress"
                  :before-upload="beforeUpload"
                  :on-success="handleSuccess"
                  :data="{type: 1}"
                  multiple
                >
                  <el-button size="small" type="primary">批量上传</el-button>
                </el-upload>
              </el-col>
            </el-row>
          </div>
          <photoDetail ref="photoDetail" :permission="permission" :photo-group-id="photoGroupId" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import photoDetail from './photoDetail'
import crudPhotoGroup from '@/api/common/photoGroup'
import crudPhoto from '@/api/common/photo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import rrOperation from '@crud/RR.operation'
import udOperation from '@crud/UD.operation'
import { mapGetters } from 'vuex'

const defaultForm = { id: null, name: null }

export default {
  name: 'PhotoGroup',
  components: { crudOperation, pagination, rrOperation, udOperation, photoDetail },

  cruds() {
    return [
      CRUD({ title: '图片分组', url: '/v1/commonPhotoGroup/searchByPage', crudMethod: { ...crudPhotoGroup }})
    ]
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      url: 'v1/image/batchUpload',
      headers: {
        Authorization: 'Basic@' + getToken()
      },
      photoGroupId: null,
      resultNumber: 0,
      permission: {
        add: ['admin', 'photoGroup:add'],
        edit: ['admin', 'photoGroup:edit'],
        del: ['admin', 'photoGroup:del']
      }
    }
  },
  computed: {
    ...mapGetters([
      'uploadApi'
    ])
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      if (this.$refs.photoDetail) {
        this.$refs.photoDetail.query.name = ''
      }
      return true
    },
    handleCurrentChange(val) {
      if (val) {
        this.$refs.photoDetail.query.photoGroupId = val.id
        this.photoGroupId = val.id
        this.$refs.photoDetail.crud.toQuery()
      }
    },
    // 编辑前将字典明细临时清空，避免日志入库数据过长
    [CRUD.HOOK.beforeToEdit](crud, form) {
      // 将角色的菜单清空，避免日志入库数据过长
      form.photoDetails = null
    },
    handleProgress(event, file, fileList) {
      console.log(event)
      // let uploadProgress = file.percentage.toFixed(0)
      // this.uploadProgress = uploadProgress
    },
    handleSuccess(response, file, fileList) {
      const that = this
      this.uploadProgress = 0
      crudPhoto.add({
        photoGroupId: this.photoGroupId,
        name: file.name,
        url: response.data.downloadUrl
      }).then(() => {
        this.resultNumber++
        if (fileList.length === this.resultNumber) {
          const param = {
            pageNo: 1,
            pageSize: 12,
            photoGroupId: that.photoGroupId
          }
          that.$refs.photoDetail.getPage(param)
          that.resultNumber = 0
        }
      })
    },
    beforeUpload(file) {
      const isPic =
          file.type === 'image/jpeg' ||
          file.type === 'image/png' ||
          file.type === 'image/gif' ||
          file.type === 'image/jpg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isPic) {
        this.$message.error('上传图片只能是 JPG、JPEG、PNG、GIF 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isPic && isLt2M
    },
    sureUrls() {
      console.log('this.urls:' + this.urls)
      this.myValue = this.urls[0]
      this.$emit('input', this.urls[0])
      this.listDialogVisible = false
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    }
  }
}
</script>

<style scoped>
</style>
