<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <h3>管廊段级结构管理</h3>
          <div>
            <el-button type="primary" @click="handleCreate">
              <Icon icon="ep:plus" class="mr-5px" /> 新增
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="管廊段名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入管廊段名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="管廊段编码" prop="code">
          <el-input
            v-model="queryParams.code"
            placeholder="请输入管廊段编码"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <Icon icon="ep:search" class="mr-5px" /> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <Icon icon="ep:refresh" class="mr-5px" /> 重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="list" border>
        <el-table-column label="ID" prop="id" width="80" />
        <el-table-column label="管廊段名称" prop="name" min-width="150" />
        <el-table-column label="管廊段编码" prop="code" width="120" />
        <el-table-column label="起点位置" prop="startPoint" min-width="150" />
        <el-table-column label="终点位置" prop="endPoint" min-width="150" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="180">
          <template #default="scope">
            {{ parseTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleUpdate(scope.row)"
              v-hasPermi="['tunnel:structure:segment:update']"
            >
              <Icon icon="ep:edit" class="mr-1" /> 修改
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row)"
              v-hasPermi="['tunnel:structure:segment:delete']"
            >
              <Icon icon="ep:delete" class="mr-1" /> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="600px"
      append-to-body
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="管廊段名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入管廊段名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管廊段编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入管廊段编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="起点位置" prop="startPoint">
              <el-input v-model="form.startPoint" placeholder="请输入起点位置" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="终点位置" prop="endPoint">
              <el-input v-model="form.endPoint" placeholder="请输入终点位置" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="dialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { useDict } from '@/hooks/web/useDict'
import { parseTime } from '@/utils/ruoyi'
import { getSegmentList, getSegment, addSegment, updateSegment, delSegment } from '@/api/tunnel/segment'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = useDict('sys_normal_disable')

const list = ref([])
const loading = ref(true)
const total = ref(0)
const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()

// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: '',
  code: ''
})

// 表单参数
const form = reactive({
  id: undefined,
  name: '',
  code: '',
  startPoint: '',
  endPoint: '',
  status: 1,
  remark: ''
})

// 表单校验
const rules = reactive<FormRules>({
  name: [{ required: true, message: '管廊段名称不能为空', trigger: 'blur' }],
  code: [{ required: true, message: '管廊段编码不能为空', trigger: 'blur' }]
})

// 对话框配置
const dialog = reactive({
  visible: false,
  title: ''
})

/** 查询管廊段列表 */
function getList() {
  loading.value = true
  getSegmentList(queryParams)
    .then((response) => {
      list.value = response.data.rows
      total.value = response.data.total
      loading.value = false
    })
    .catch(() => {
      loading.value = false
    })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 分页相关 */
function handleSizeChange(val: number) {
  queryParams.pageSize = val
  getList()
}

function handleCurrentChange(val: number) {
  queryParams.pageNo = val
  getList()
}

/** 新增按钮操作 */
function handleCreate() {
  dialog.title = '添加管廊段'
  dialog.visible = true
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  dialog.title = '修改管廊段'
  dialog.visible = true
  
  // 获取详情
  getSegment(row.id).then((response) => {
    Object.assign(form, response.data)
  })
}

/** 提交表单 */
function submitForm() {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (form.id) {
        updateSegment(form)
          .then(() => {
            ElMessage.success('修改成功')
            dialog.visible = false
            getList()
          })
      } else {
        addSegment(form)
          .then(() => {
            ElMessage.success('新增成功')
            dialog.visible = false
            getList()
          })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  ElMessageBox.confirm('确认要删除该管廊段吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delSegment(row.id)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 对话框关闭事件 */
function handleDialogClosed() {
  // 重置表单
  if (formRef.value) {
    formRef.value.resetFields()
    form.id = undefined
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
