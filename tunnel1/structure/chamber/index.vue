<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <h3>舱室级结构管理</h3>
          <div>
            <el-button type="primary" @click="handleCreate">
              <Icon icon="ep:plus" class="mr-5px" /> 新增
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="舱室名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入舱室名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="舱室编码" prop="code">
          <el-input
            v-model="queryParams.code"
            placeholder="请输入舱室编码"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="所属管廊段" prop="segmentId">
          <el-select
            v-model="queryParams.segmentId"
            placeholder="请选择管廊段"
            clearable
            style="width: 240px"
          >
            <el-option
              v-for="item in segmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
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
        <el-table-column label="舱室名称" prop="name" min-width="150" />
        <el-table-column label="舱室编码" prop="code" width="120" />
        <el-table-column label="所属管廊段" prop="segmentName" min-width="150" />
        <el-table-column label="舱室类型" prop="type" width="120">
          <template #default="scope">
            <dict-tag :options="chamberTypeOptions" :value="scope.row.type" />
          </template>
        </el-table-column>
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
              v-hasPermi="['tunnel:structure:chamber:update']"
            >
              <Icon icon="ep:edit" class="mr-1" /> 修改
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row)"
              v-hasPermi="['tunnel:structure:chamber:delete']"
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
      width="700px"
      append-to-body
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="舱室名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入舱室名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="舱室编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入舱室编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属管廊段" prop="segmentId">
              <el-select v-model="form.segmentId" placeholder="请选择管廊段" style="width: 100%">
                <el-option
                  v-for="item in segmentOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="舱室类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择舱室类型" style="width: 100%">
                <el-option
                  v-for="dict in chamberTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="长度(m)" prop="length">
              <el-input-number v-model="form.length" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宽度(m)" prop="width">
              <el-input-number v-model="form.width" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高度(m)" prop="height">
              <el-input-number v-model="form.height" :min="0" :precision="2" style="width: 100%" />
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
import { getChamberList, getChamber, addChamber, updateChamber, delChamber } from '@/api/tunnel/chamber'
import { getSegmentList } from '@/api/tunnel/segment'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = useDict('sys_normal_disable')

// 舱室类型选项
const chamberTypeOptions = ref([
  { value: '1', label: '综合舱' },
  { value: '2', label: '电力舱' },
  { value: '3', label: '燃气舱' },
  { value: '4', label: '给水舱' },
  { value: '5', label: '通信舱' },
  { value: '6', label: '污水舱' },
  { value: '7', label: '雨水舱' },
  { value: '8', label: '热力舱' },
  { value: '9', label: '其他' }
])

const list = ref([])
const loading = ref(true)
const total = ref(0)
const queryFormRef = ref<FormInstance>()
const formRef = ref<FormInstance>()
const segmentOptions = ref([]) // 管廊段选项

// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: '',
  code: '',
  segmentId: undefined
})

// 表单参数
const form = reactive({
  id: undefined,
  name: '',
  code: '',
  segmentId: undefined,
  segmentName: '',
  type: '',
  length: undefined,
  width: undefined,
  height: undefined,
  status: 1,
  remark: ''
})

// 表单校验
const rules = reactive<FormRules>({
  name: [{ required: true, message: '舱室名称不能为空', trigger: 'blur' }],
  code: [{ required: true, message: '舱室编码不能为空', trigger: 'blur' }],
  segmentId: [{ required: true, message: '请选择所属管廊段', trigger: 'change' }],
  type: [{ required: true, message: '请选择舱室类型', trigger: 'change' }]
})

// 对话框配置
const dialog = reactive({
  visible: false,
  title: ''
})

/** 查询舱室列表 */
function getList() {
  loading.value = true
  getChamberList(queryParams)
    .then((response) => {
      list.value = response.data.rows
      total.value = response.data.total
      loading.value = false
    })
    .catch(() => {
      loading.value = false
    })
}

/** 查询管廊段列表 */
function getSegmentOptions() {
  getSegmentList({ pageNo: 1, pageSize: 1000 }).then((response) => {
    segmentOptions.value = response.data.rows
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
  dialog.title = '添加舱室'
  dialog.visible = true
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  dialog.title = '修改舱室'
  dialog.visible = true
  
  // 获取详情
  getChamber(row.id).then((response) => {
    Object.assign(form, response.data)
  })
}

/** 提交表单 */
function submitForm() {
  formRef.value?.validate((valid) => {
    if (valid) {
      // 设置管廊段名称
      if (form.segmentId) {
        const segment = segmentOptions.value.find(item => item.id === form.segmentId)
        if (segment) {
          form.segmentName = segment.name
        }
      }
      
      if (form.id) {
        updateChamber(form)
          .then(() => {
            ElMessage.success('修改成功')
            dialog.visible = false
            getList()
          })
      } else {
        addChamber(form)
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
  ElMessageBox.confirm('确认要删除该舱室吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delChamber(row.id)
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
    form.segmentName = ''
  }
}

onMounted(() => {
  getList()
  getSegmentOptions()
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
