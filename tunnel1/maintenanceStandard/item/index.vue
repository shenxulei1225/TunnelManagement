<template>
  <ContentWrap>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>养护标准项目</span>
        </div>
      </template>
      <!-- 表格 -->
      <Table
        :columns="allColumns"
        :data="list"
        :loading="loading"
        :pagination="pagination"
        @register="register"
      >
        <!-- 操作列 -->
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            @click="handleEdit(row)"
          >
            编辑
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </Table>
    </el-card>
  </ContentWrap>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useI18n } from '@/hooks/web/useI18n'
import { Table } from '@/components/Table'
import { useTable } from '@/hooks/web/useTable'
import { useMessage } from '@/hooks/web/useMessage'
import { MaintenanceStandardItemApi } from '@/api/tunnel/maintenanceStandard/item'

const { t } = useI18n()
const message = useMessage()

// 表格的配置
const { register, list, loading, pagination } = useTable({
  fetchListApi: MaintenanceStandardItemApi.getItemPage,
  deleteApi: MaintenanceStandardItemApi.deleteItem,
  deleteConfirmMessage: '确认删除该养护标准项目？'
})

// 表格的列配置
const allColumns = ref([
  { type: 'index', label: '序号', width: 80 },
  { prop: 'name', label: '项目名称', width: 150 },
  { prop: 'categoryName', label: '分类', width: 150 },
  { prop: 'description', label: '描述', minWidth: 200 },
  { prop: 'status', label: '状态', width: 100 },
  { prop: 'createTime', label: '创建时间', width: 160 },
  { fixed: 'right', width: 120, align: 'center', slot: 'action' }
])

// 编辑
const handleEdit = (row: any) => {
  console.log('编辑:', row)
}

// 删除
const handleDelete = (row: any) => {
  message.confirm('确定要删除吗？').then(() => {
    // TODO: 实现删除逻辑
  })
}
</script>
