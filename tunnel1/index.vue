<template>
  <div class="app-container">
    <el-card>
      <div class="filter-container">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增舱室</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </div>

      <el-table
        :loading="loading"
        :data="list"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="舱室编号" :align="'center'" prop="code" />
        <el-table-column label="舱室名称" :align="'center'" prop="name" />
        <el-table-column label="所属管廊" :align="'center'" prop="tunnelName" />
        <el-table-column label="管廊段" :align="'center'" prop="segmentName" />
        <el-table-column label="类型" :align="'center'" prop="typeName" />
        <el-table-column label="位置" :align="'center'" prop="position" />
        <el-table-column label="长度(米)" :align="'center'" prop="length" />
        <el-table-column label="状态" :align="'center'" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" :align="'center'">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" :align="'center'" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="info" size="mini" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="listQuery.page"
        v-model:limit="listQuery.limit"
        @pagination="getList"
      />
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
      <el-form ref="dataForm" :model="temp" :rules="rules" label-position="left" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="舱室编号" prop="code">
              <el-input v-model="temp.code" placeholder="请输入舱室编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="舱室名称" prop="name">
              <el-input v-model="temp.name" placeholder="请输入舱室名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属管廊" prop="tunnelId">
              <el-select v-model="temp.tunnelId" placeholder="请选择所属管廊">
                <el-option v-for="item in tunnelOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管廊段" prop="tunnelSegmentId">
              <el-select v-model="temp.tunnelSegmentId" placeholder="请选择管廊段">
                <el-option v-for="item in segmentOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="舱室类型" prop="type">
              <el-select v-model="temp.type" placeholder="请选择舱室类型">
                <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="位置" prop="position">
              <el-input-number v-model="temp.position" :min="1" :max="10" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="长度(米)" prop="length">
              <el-input-number v-model="temp.length" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="宽度(米)" prop="width">
              <el-input-number v-model="temp.width" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="高度(米)" prop="height">
              <el-input-number v-model="temp.height" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="净高(米)" prop="netHeight">
              <el-input-number v-model="temp.netHeight" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="结构信息" prop="structures">
          <el-table :data="temp.structures" border>
            <el-table-column label="结构名称" prop="name" />
            <el-table-column label="操作" width="120">
              <template #default="{ $index }">
                <el-button type="text" @click="handleAddStructure">添加</el-button>
                <el-button type="text" @click="handleRemoveStructure($index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item label="安全设施">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="人员入出口" prop="personnelEntranceId">
                <el-select v-model="temp.personnelEntranceId" placeholder="请选择人员入出口">
                  <el-option v-for="item in entranceOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="逃生口" prop="escapeHatchId">
                <el-select v-model="temp.escapeHatchId" placeholder="请选择逃生口">
                  <el-option v-for="item in escapeOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="吊装口" prop="hoistHatchId">
                <el-select v-model="temp.hoistHatchId" placeholder="请选择吊装口">
                  <el-option v-for="item in hoistOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="进风口" prop="ventilationInletId">
                <el-select v-model="temp.ventilationInletId" placeholder="请选择进风口">
                  <el-option v-for="item in inletOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="运维设施">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="照明设施" prop="lightingFacilityId">
                <el-select v-model="temp.lightingFacilityId" placeholder="请选择照明设施">
                  <el-option v-for="item in lightingOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电气设施" prop="electricalFacilityId">
                <el-select v-model="temp.electricalFacilityId" placeholder="请选择电气设施">
                  <el-option v-for="item in electricalOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="通信设施" prop="communicationFacilityId">
                <el-select v-model="temp.communicationFacilityId" placeholder="请选择通信设施">
                  <el-option v-for="item in communicationOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="支吊架" prop="supportHangerId">
                <el-select v-model="temp.supportHangerId" placeholder="请选择支吊架">
                  <el-option v-for="item in supportOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="'舱室详情'" v-model="detailVisible" width="80%">
      <el-descriptions :column="2">
        <el-descriptions-item label="舱室编号">{{ detail.code }}</el-descriptions-item>
        <el-descriptions-item label="舱室名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="所属管廊">{{ detail.tunnelName }}</el-descriptions-item>
        <el-descriptions-item label="管廊段">{{ detail.segmentName }}</el-descriptions-item>
        <el-descriptions-item label="舱室类型">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="位置">{{ detail.position }}</el-descriptions-item>
        <el-descriptions-item label="长度(米)">{{ detail.length }}</el-descriptions-item>
        <el-descriptions-item label="宽度(米)">{{ detail.width }}</el-descriptions-item>
        <el-descriptions-item label="高度(米)">{{ detail.height }}</el-descriptions-item>
        <el-descriptions-item label="净高(米)">{{ detail.netHeight }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
      </el-descriptions>

      <div class="mt-20">
        <el-descriptions title="结构信息" :column="1">
          <el-descriptions-item v-for="structure in detail.structures" :key="structure.id">
            {{ structure.name }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="mt-20">
        <el-descriptions title="安全设施" :column="2">
          <el-descriptions-item label="人员入出口">{{ detail.personnelEntranceName }}</el-descriptions-item>
          <el-descriptions-item label="逃生口">{{ detail.escapeHatchName }}</el-descriptions-item>
          <el-descriptions-item label="吊装口">{{ detail.hoistHatchName }}</el-descriptions-item>
          <el-descriptions-item label="进风口">{{ detail.ventilationInletName }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="mt-20">
        <el-descriptions title="运维设施" :column="2">
          <el-descriptions-item label="照明设施">{{ detail.lightingFacilityName }}</el-descriptions-item>
          <el-descriptions-item label="电气设施">{{ detail.electricalFacilityName }}</el-descriptions-item>
          <el-descriptions-item label="通信设施">{{ detail.communicationFacilityName }}</el-descriptions-item>
          <el-descriptions-item label="支吊架">{{ detail.supportHangerName }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getTunnelList, createTunnel, updateTunnel, deleteTunnel, exportTunnel } from '@/api/tunnel'
import Pagination from '@/components/Pagination/index.vue'

// 时间格式化
const formatDate = (date: string | number | Date) => {
  const d = new Date(date)
  return d.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

@Component({
  name: 'TunnelChamber',
  components: {
    Pagination
  }
})
export default class extends Vue {
  private list: any[] = []
  private total = 0
  private loading = true
  private listQuery = {
    page: 1,
    limit: 20
  }
  private dialogVisible = false
  private detailVisible = false
  private dialogStatus = ''
  private temp = {
    id: undefined,
    code: '',
    name: '',
    tunnelId: undefined,
    tunnelSegmentId: undefined,
    type: undefined,
    position: 1,
    length: 0,
    width: 0,
    height: 0,
    netHeight: 0,
    structures: [],
    personnelEntranceId: undefined,
    escapeHatchId: undefined,
    hoistHatchId: undefined,
    ventilationInletId: undefined,
    ventilationOutletId: undefined,
    fireProtectionFacilityId: undefined,
    monitoringFacilityId: undefined,
    lightingFacilityId: undefined,
    electricalFacilityId: undefined,
    communicationFacilityId: undefined,
    supportHangerId: undefined,
    pipelineBranchId: undefined,
    remark: ''
  }
  private detail: any = {}
  private rules = {
    code: [{ required: true, message: '请输入舱室编号', trigger: 'blur' }],
    name: [{ required: true, message: '请输入舱室名称', trigger: 'blur' }],
    tunnelId: [{ required: true, message: '请选择所属管廊', trigger: 'change' }],
    tunnelSegmentId: [{ required: true, message: '请选择管廊段', trigger: 'change' }],
    type: [{ required: true, message: '请选择舱室类型', trigger: 'change' }],
    position: [{ required: true, message: '请输入位置', trigger: 'change' }],
    length: [{ required: true, message: '请输入长度', trigger: 'change' }],
    width: [{ required: true, message: '请输入宽度', trigger: 'change' }],
    height: [{ required: true, message: '请输入高度', trigger: 'change' }],
    netHeight: [{ required: true, message: '请输入净高', trigger: 'change' }]
  }
  private tunnelOptions: any[] = []
  private segmentOptions: any[] = []
  private typeOptions: any[] = []
  private entranceOptions: any[] = []
  private escapeOptions: any[] = []
  private hoistOptions: any[] = []
  private inletOptions: any[] = []
  private lightingOptions: any[] = []
  private electricalOptions: any[] = []
  private communicationOptions: any[] = []
  private supportOptions: any[] = []

  created() {
    this.initOptions()
    this.getList()
  }

  private async initOptions() {
    // 初始化各种下拉选项
    // 这里需要调用对应的API获取选项数据
    // 例如：getTunnelOptions(), getSegmentOptions() 等
  }

  private async getList() {
    this.loading = true
    const { data } = await getTunnelList(this.listQuery)
    this.list = data.list
    this.total = data.total
    this.loading = false
  }

  private handleAdd() {
    this.temp = {
      id: undefined,
      code: '',
      name: '',
      tunnelId: undefined,
      tunnelSegmentId: undefined,
      type: undefined,
      position: 1,
      length: 0,
      width: 0,
      height: 0,
      netHeight: 0,
      structures: [],
      personnelEntranceId: undefined,
      escapeHatchId: undefined,
      hoistHatchId: undefined,
      ventilationInletId: undefined,
      ventilationOutletId: undefined,
      fireProtectionFacilityId: undefined,
      monitoringFacilityId: undefined,
      lightingFacilityId: undefined,
      electricalFacilityId: undefined,
      communicationFacilityId: undefined,
      supportHangerId: undefined,
      pipelineBranchId: undefined,
      remark: ''
    }
    this.dialogStatus = 'create'
    this.dialogVisible = true
  }

  private handleUpdate(row: any) {
    this.temp = Object.assign({}, row)
    this.dialogStatus = 'update'
    this.dialogVisible = true
  }

  private handleView(row: any) {
    this.detail = Object.assign({}, row)
    this.detailVisible = true
  }

  private async handleDelete(row: any) {
    await deleteTunnel(row.id)
    this.$message({
      message: '删除成功',
      type: 'success'
    })
    this.getList()
  }

  private async handleExport() {
    await exportTunnel()
    this.$message({
      message: '导出成功',
      type: 'success'
    })
  }

  private handleAddStructure() {
    this.temp.structures.push({
      id: undefined,
      name: '',
      description: ''
    })
  }

  private handleRemoveStructure(index: number) {
    this.temp.structures.splice(index, 1)
  }

  private createData() {
    this.$refs['dataForm'].validate(async (valid: boolean) => {
      if (valid) {
        await createTunnel(this.temp)
        this.dialogVisible = false
        this.$message({
          message: '创建成功',
          type: 'success'
        })
        this.getList()
      }
    })
  }

  private updateData() {
    this.$refs['dataForm'].validate(async (valid: boolean) => {
      if (valid) {
        await updateTunnel(this.temp)
        this.dialogVisible = false
        this.$message({
          message: '更新成功',
          type: 'success'
        })
        this.getList()
      }
    })
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}
</style>
