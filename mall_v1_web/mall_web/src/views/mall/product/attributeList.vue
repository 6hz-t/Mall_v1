<template>
  <div>
    <el-row class="operate-button">
      <el-button
        type="primary"
        @click="onAdd()"
      >新增
      </el-button>
      <el-button
        type="primary"
        @click="onDelete()"
      >删除
      </el-button>
    </el-row>
    <el-row>
      <el-form ref="attributeListForm" :model="attributeListForm">
        <el-table
          :data="attributeEntityList"
          :border="true"
          highlight-current-row
          @selection-change="onSelectChange"
        >
          <el-table-column
            type="selection"
            width="55"
          />
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column prop="id" label="属性值ID" width="180" />
          <el-table-column prop="attributeName" label="属性" width="150" />
          <el-table-column prop="value" label="属性值" width="180" />
        </el-table>

      </el-form>

    </el-row>

  </div>
</template>

<script>
import _ from 'lodash'

export default {
  name: 'AttributeList',
  props: {
    attributeEntityList: {
      default: []
    },
    type: {
      default: 0
    }
  },
  data() {
    return {
      attributeListForm: {
        attributeValueIds: null
      },
      selection: []
    }
  },

  methods: {
    onAdd() {
      this.$emit('onClose', true, this.type)
    },
    onDelete() {
      if (!_.size(this.selection)) {
        this.$message.info('请选择要删除的属性值')
        return
      }
      this.$confirm('确定删除所选属性值吗?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() =>
        _.forEach(this.selection, entity => {
          this.attributeEntityList.splice(this.attributeEntityList.findIndex(item => item.id === entity.id), 1)
        })
      )
    },
    onSelectChange(value) {
      this.selection = value
    }
  }
}
</script>
<style lang="scss" scoped>
  .operate-button {
    margin-bottom: 10px;
  }
</style>
