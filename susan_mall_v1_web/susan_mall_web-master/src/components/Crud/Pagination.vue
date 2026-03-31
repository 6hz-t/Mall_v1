<!--分页-->
<template>
  <el-pagination
    :page-size.sync="page.pageSize"
    :total="page.totalCount"
    :current-page.sync="page.data"
    style="margin-top: 8px;"
    layout="total, prev, pager, next, sizes"
    @size-change="crud.sizeChangeHandler($event)"
    @current-change="crud.pageChangeHandler"
  />
</template>
<script>
import { pagination } from '@crud/crud'
export default {
  mixins: [pagination()]
}
</script>
