<template>
  <div class="app-container h-full">
    <div class="h-full flex flex-col bg-gray-50">
      <div class="flex flex-1 overflow-hidden">
        <!-- 左侧管廊段信息 -->
        <div class="w-200 bg-white rounded shadow mr-4 flex flex-col flex-shrink-0">
          <div class="p-4 border-b flex items-center justify-between">
            <h3 class="text-lg font-medium flex items-center">
              <el-icon class="mr-2"><Document /></el-icon>
              管廊段信息
            </h3>
            <div class="flex items-end space-x-2">
              <span class="text-sm text-gray-600 whitespace-nowrap">管廊选择：</span>
              <div class="w-48">
                <el-select
                  v-model="searchForm.segmentId"
                  placeholder="请选择管廊段"
                  clearable
                  filterable
                  remote
                  :remote-method="handleSegmentSearch"
                  :loading="segmentLoading"
                  style="width: 100%"
                  size="small"
                  popper-class="segment-select-popper"
                  @change="handleSearch"
                  @clear="resetSearch"
                >
                  <!-- 调试信息 -->
                  <div v-if="false" style="display: none;">
                    {{ console.log('Segment Options:', segmentOptions) }}
                    {{ console.log('Segment Options Value:', segmentOptions.value) }}
                  </div>
                  
                  <template v-if="!segmentOptions || !Array.isArray(segmentOptions.value) || segmentOptions.value.length === 0">
                    <!-- 空状态 -->
                    <el-option disabled :value="''" label="无数据" />
                  </template>
                  <template v-else>
                    <el-option
                      v-for="item in segmentOptions.value"
                      :key="item?.id || 'undefined'"
                      :value="item?.id || ''"
                      :label="item?.name || '未命名'"
                      :disabled="!item?.id"
                    >
                      <div class="flex items-center justify-between">
                        <span class="text-xs">{{ item?.name || '未命名' }}</span>
                        <el-tag 
                          v-if="item?.code"
                          size="small" 
                          class="ml-1 text-xs"
                        >
                          {{ item.code }}
                        </el-tag>
                      </div>
                    </el-option>
                  </template>
                </el-select>
              </div>
            </div>
          </div>
          <div class="p-4 flex-1 overflow-auto">
            <el-tabs v-model="activeTab" class="h-full flex flex-col">
              <el-tab-pane label="基本信息" name="info" class="flex-1 flex flex-col">
                <div class="space-y-6 flex-1 overflow-y-auto pr-2 pb-6">
                  <!-- 基本信息网格 -->
                  <div class="grid grid-cols-3 gap-3">
                    <div class="p-3 bg-gray-50 rounded border">
                      <h4 class="text-xs font-medium text-gray-500 mb-1">管廊段名称</h4>
                      <p class="text-sm font-medium text-gray-900">{{ currentSegment?.name || '--' }}</p>
                    </div>
                    <div class="p-3 bg-gray-50 rounded border">
                      <h4 class="text-xs font-medium text-gray-500 mb-1">道路名称</h4>
                      <p class="text-sm font-medium text-gray-900">{{ currentSegment?.roadName || '--' }}</p>
                    </div>
                    <div class="p-3 bg-gray-50 rounded-border">
                      <h4 class="text-xs font-medium text-gray-500 mb-1">覆盖范围</h4>
                      <p class="text-sm font-medium text-gray-900">
                        {{ currentSegment?.startPoint || '--' }}
                        <span v-if="currentSegment?.startPoint && currentSegment?.endPoint"> - </span>
                        {{ currentSegment?.endPoint || '' }}
                      </p>
                    </div>
                  </div>

                  <!-- 统计信息卡片 -->
                  <div class="grid grid-cols-4 gap-4">
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                      <div class="text-sm text-gray-500 mb-1">管线数量</div>
                      <div class="text-2xl font-bold text-blue-600">{{ currentSegment?.stats?.pipelineCount ?? 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">条</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                      <div class="text-sm text-gray-500 mb-1">总长度</div>
                      <div class="text-2xl font-bold text-green-600">{{ currentSegment?.stats?.totalLength ?? 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">公里</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                      <div class="text-sm text-gray-500 mb-1">舱室类型</div>
                      <div class="text-2xl font-bold text-purple-600">{{ currentSegment?.stats?.compartmentTypes ?? 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">种</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                      <div class="text-sm text-gray-500 mb-1">风险等级</div>
                      <div class="text-2xl font-bold" :class="currentSegment?.stats ? getRiskLevelClass(currentSegment.stats.hazardLevel) : ''">
                        {{ currentSegment?.stats?.hazardLevel || '--' }}
                      </div>
                    </div>
                  </div>

                  <!-- 结构信息 -->
                  <div class="grid grid-cols-3 gap-3">
                    <div class="p-3 bg-gray-50 rounded border">
                      <h4 class="text-xs font-medium text-gray-500 mb-1">结构尺寸</h4>
                      <p class="text-sm font-medium text-gray-900">
                        {{ currentSegment.length || '--' }} × {{ currentSegment.width || '--' }} × {{ currentSegment.height || '--' }} 米
                        <span class="text-xs text-gray-400 ml-1">(长×宽×高)</span>
                      </p>
                    </div>
                    <div class="p-3 bg-gray-50 rounded border">
                      <h4 class="text-xs font-medium text-gray-500 mb-1">状态</h4>
                      <el-tag size="small" type="success" effect="plain">
                        正常
                      </el-tag>
                    </div>
                  </div>

                  <!-- 舱室信息 -->
                  <div class="space-y-3 mt-6" v-if="currentSegment?.compartments?.length">
                    <h4 class="text-sm font-medium text-gray-700 border-l-4 border-green-500 pl-2 py-1 bg-gray-50">
                      舱室信息
                    </h4>
                    <div class="grid grid-cols-2 gap-3">
                      <div 
                        v-for="comp in currentSegment.compartments" 
                        :key="comp.id" 
                        class="p-3 bg-white rounded border hover:shadow-sm transition-shadow"
                      >
                        <div class="flex justify-between items-center">
                          <h5 class="text-sm font-medium text-gray-900">{{ comp.name }}</h5>
                          <el-tag size="small" :type="getCompartmentType(comp.type)" effect="plain">
                            {{ getCompartmentTypeName(comp.type) }}
                          </el-tag>
                        </div>
                        <div class="mt-2 flex justify-between items-center text-xs">
                          <span class="text-gray-500">面积: <span class="text-gray-700 font-medium">{{ comp.area }} ㎡</span></span>
                          <span class="text-gray-500">容量: <span class="text-gray-700 font-medium">{{ comp.capacity || '--' }} m³</span></span>
                        </div>
                        <p v-if="comp.description" class="text-xs text-gray-500 mt-2 line-clamp-2" :title="comp.description">
                          {{ comp.description }}
                        </p>
                      </div>
                    </div>
                  </div>
                  <el-empty v-else-if="currentSegment" description="暂无舱室数据" :image-size="80" class="mt-6" />

                  <!-- 结构物信息 -->
                  <div class="space-y-3 mt-6">
                    <h4 class="text-sm font-medium text-gray-700 border-l-4 border-purple-500 pl-2 py-1 bg-gray-50">
                      结构物信息
                    </h4>
                    <div class="grid grid-cols-3 gap-2">
                      <div 
                        v-for="item in structureTypes" 
                        :key="item.type"
                        class="p-2 bg-white rounded border hover:border-blue-400 hover:shadow-md transition-all cursor-pointer"
                        @mouseenter="highlightStructure(item.type)"
                        @mouseleave="resetHighlight"
                      >
                        <div class="flex items-center">
                          <el-icon class="text-blue-500 mr-1">
                            <component :is="getStructureIcon(item.type)" />
                          </el-icon>
                          <span class="text-xs font-medium text-gray-700">{{ item.name }}</span>
                        </div>
                      </div>
                    </div>
                  </div>


                </div>
              </el-tab-pane>
              
              <el-tab-pane label="管线列表" name="pipelines" class="h-full flex flex-col">
                <div class="flex-1 flex flex-col h-full">
                  <!-- 搜索和筛选区域 -->
                  <div class="p-4 border-b bg-white">
                    <el-form :inline="true" :model="pipelineQuery" class="flex items-center">
                      <el-form-item label="舱室" class="mb-0 mr-4">
                        <el-select
                          v-model="pipelineQuery.compartmentId"
                          placeholder="请选择舱室"
                          clearable
                          style="width: 180px"
                          @change="handlePipelineQuery"
                        >
                          <el-option
                            v-for="item in currentSegment?.compartments || []"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="管线类型" class="mb-0 mr-4">
                        <el-select
                          v-model="pipelineQuery.type"
                          placeholder="请选择管线类型"
                          clearable
                          style="width: 180px"
                          @change="handlePipelineQuery"
                        >
                          <el-option
                            v-for="(label, key) in pipelineTypeOptions"
                            :key="key"
                            :label="label"
                            :value="key"
                          />
                        </el-select>
                      </el-form-item>
                      <el-form-item class="mb-0">
                        <el-button type="primary" :icon="Search" @click="handlePipelineQuery">
                          搜索
                        </el-button>
                        <el-button :icon="Refresh" @click="resetPipelineQuery">
                          重置
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  
                  <div class="flex-1 overflow-hidden">
                    <el-table
                      v-loading="loading"
                      :data="filteredPipelines"
                      style="width: 100%; height: 100%"
                      size="small"
                      border
                      stripe
                      class="pipeline-table w-full"
                      @sort-change="handleSortChange"
                    >
                    <el-table-column 
                      type="index"
                      label="序号" 
                      width="60"
                      align="center"
                      header-align="center"
                    >
                      <template #default="{ $index }">
                        <div class="text-sm">{{ (pageParams.pageNum - 1) * pageParams.pageSize + $index + 1 }}</div>
                      </template>
                    </el-table-column>
                    <el-table-column 
                      label="管道类型" 
                      min-width="80" 
                      align="center"
                      sortable="custom"
                      :sort-by="row => getPipelineTypeLabel(row.type)"
                      header-align="center"
                    >
                      <template #default="{ row }">
                        <el-tag :type="getPipelineType(row.type).type" size="small" class="whitespace-nowrap">
                          {{ getPipelineTypeLabel(row.type) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    
                    <el-table-column 
                      prop="code" 
                      label="编号" 
                      min-width="110" 
                      align="center"
                      class-name="whitespace-nowrap text-sm text-center"
                      sortable="custom"
                      show-overflow-tooltip
                    />
                    

                    
                    <el-table-column 
                      prop="length" 
                      label="长度(m)" 
                      min-width="100" 
                      align="center" 
                      class-name="whitespace-nowrap text-sm text-center"
                      sortable="custom"
                    >
                      <template #default="{ row }">
                        <div class="whitespace-nowrap text-sm text-center">
                          {{ safeLength(row.length) }}
                        </div>
                      </template>
                    </el-table-column>
                    
                    <el-table-column 
                      prop="diameter" 
                      label="管径/规格" 
                      min-width="120" 
                      align="center"
                      class-name="whitespace-nowrap text-sm text-center"
                      sortable="custom"
                    />
                    
                    <el-table-column 
                      prop="material" 
                      label="材质" 
                      min-width="90" 
                      align="center"
                      show-overflow-tooltip
                      sortable="custom"
                      class-name="whitespace-nowrap text-sm text-center"
                    >
                      <template #default="{ row }">
                        <div class="whitespace-nowrap text-sm text-center">{{ row.material }}</div>
                      </template>
                    </el-table-column>
                    
                    <el-table-column 
                      label="状态" 
                      min-width="80" 
                      align="center"
                      sortable="custom"
                    >
                      <template #default="{ row }">
                        <el-tag :type="row.status === '正常' ? 'success' : 'danger'" size="small" class="whitespace-nowrap">
                          {{ row.status }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    
                    <el-table-column 
                      prop="compartment" 
                      label="舱室" 
                      min-width="80" 
                      align="center"
                      class-name="whitespace-nowrap text-sm text-center"
                      sortable="custom"
                    />
                    </el-table>
                  </div>
                  
                  <!-- 分页 -->
                  <div class="border-t border-gray-200 bg-white">
                    <div class="flex justify-end py-2 pr-4">
                      <el-pagination
                        v-model:current-page="pageParams.pageNum"
                        v-model:page-size="pageParams.pageSize"
                        :page-sizes="[10, 20, 50, 100]"
                        :total="pageParams.total"
                        layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        class="pagination-compact"
                      />
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              
              <!-- 设备信息标签页 -->
              <el-tab-pane label="设备信息" name="device" class="flex-1 flex flex-col">
                <div class="space-y-4 flex-1 overflow-y-auto pr-2 pb-6">
                  <!-- 设备状态统计 -->
                  <div class="grid grid-cols-4 gap-4">
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                      <div class="text-sm text-gray-500 mb-1">设备总数</div>
                      <div class="text-2xl font-bold text-blue-600">{{ deviceStats?.total || 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">台</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-green-100">
                      <div class="text-sm text-gray-500 mb-1">运行正常</div>
                      <div class="text-2xl font-bold text-green-600">{{ deviceStats?.normal || 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">台</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-yellow-100">
                      <div class="text-sm text-gray-500 mb-1">告警</div>
                      <div class="text-2xl font-bold text-yellow-600">{{ deviceStats?.warning || 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">台</div>
                    </div>
                    <div class="bg-white rounded-lg shadow-sm p-4 border border-red-100">
                      <div class="text-sm text-gray-500 mb-1">故障</div>
                      <div class="text-2xl font-bold text-red-600">{{ deviceStats?.error || 0 }}</div>
                      <div class="text-xs text-gray-400 mt-1">台</div>
                    </div>
                  </div>
                  
                  <!-- 设备列表 -->
                  <div class="bg-white rounded-lg shadow-sm p-4 border border-gray-100">
                    <div class="flex justify-between items-center mb-4">
                      <h4 class="text-base font-medium">设备列表</h4>
                      <el-button type="primary" size="small" @click="handleAddDevice">
                        <el-icon class="mr-1"><Plus /></el-icon>添加设备
                      </el-button>
                    </div>
                    <el-table
                      :data="deviceList"
                      size="small"
                      border
                      style="width: 100%"
                      v-loading="deviceLoading"
                    >
                      <el-table-column prop="name" label="设备名称" min-width="120" show-overflow-tooltip />
                      <el-table-column prop="code" label="设备编号" width="120" show-overflow-tooltip />
                      <el-table-column prop="type" label="设备类型" width="100">
                        <template #default="{ row }">
                          <el-tag size="small" effect="plain">{{ getDeviceTypeLabel(row.type) }}</el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column prop="status" label="状态" width="80">
                        <template #default="{ row }">
                          <el-tag 
                            :type="getDeviceStatusType(row.status)"
                            size="small"
                            effect="light"
                          >
                            {{ getDeviceStatusLabel(row.status) }}
                          </el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column prop="location" label="安装位置" min-width="150" show-overflow-tooltip />
                      <el-table-column prop="installDate" label="安装日期" width="100" />
                      <el-table-column label="操作" width="120" fixed="right">
                        <template #default="{ row }">
                          <el-button type="primary" link size="small" @click="handleViewDevice(row)">详情</el-button>
                          <el-button type="primary" link size="small" @click="handleEditDevice(row)">编辑</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                      <el-pagination
                        v-model:current-page="devicePage.pageNum"
                        v-model:page-size="devicePage.pageSize"
                        :page-sizes="[10, 20, 30, 50]"
                        :small="true"
                        :background="true"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="deviceTotal"
                        @size-change="handleDeviceSizeChange"
                        @current-change="handleDevicePageChange"
                      />
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>

        <!-- 右侧主内容区 -->
        <div class="flex-1 flex flex-col bg-white rounded shadow overflow-hidden min-w-0">
          <!-- 地图区域 -->
          <div class="h-96 bg-gray-100 relative">
            <BaiduMap
              :center="mapCenter"
              :zoom="mapZoom"
              :markers="mapMarkers"
              @map-loaded="onMapLoaded"
              @marker-click="onMarkerClick"
              @map-click="onMapClick"
            >
              <template #controls>
                <el-button-group class="control-group">
                  <el-button type="primary" size="small" circle @click="zoomIn" title="放大">
                    <el-icon><ZoomIn /></el-icon>
                  </el-button>
                  <el-button type="primary" size="small" circle @click="zoomOut" title="缩小">
                    <el-icon><ZoomOut /></el-icon>
                  </el-button>
                  <el-button type="primary" size="small" circle @click="resetView" title="重置视图">
                    <el-icon><RefreshRight /></el-icon>
                  </el-button>
                </el-button-group>
              </template>
            </BaiduMap>
          </div>
          <!-- 横截面图区域 -->
          <div class="h-32 border-t border-gray-200 relative">
            <div class="absolute inset-0 flex items-center justify-center">
              <div class="text-center text-gray-400 text-sm">
                <el-icon class="text-3xl mb-1"><Picture /></el-icon>
                <p>横截面图</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
// 导入 Vue 相关
import { ref, onMounted, computed, watch, onBeforeUnmount, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { CACHE_KEY, useCache } from '@/hooks/web/useCache';
import { useDeviceTab } from './device-tab';
import { usePipelineTab } from './pipeline-tab';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  Search, 
  RefreshRight, 
  ZoomIn, 
  ZoomOut, 
  MapLocation, 
  Picture, 
  Document,
  Warning,
  Upload,
  WindPower,
  SetUp,
  Connection
} from '@element-plus/icons-vue';
import BaiduMap from '@/components/BaiduMap/index.vue';

// 初始化路由和状态
const route = useRoute();
const router = useRouter();
const { wsCache } = useCache();

// 组件状态
const activeTab = ref('info');
const loading = ref(false);
const segmentLoading = ref(false);
const deviceLoading = ref(false);

// 搜索表单
const searchForm = ref({
  segmentId: ''
});

// 当前选中的管廊段
const currentSegment = ref<any>(null);

// 设备相关状态
const deviceList = ref<Device[]>([]);
const deviceStats = ref({
  total: 0,
  online: 0,
  warning: 0,
  offline: 0
});

// 地图相关状态
const mapInstance = ref<any>(null);
const mapMarkers = ref<MapMarker[]>([]);
const zoomLevel = ref(15);

// 管线相关状态
const pipelineList = ref<any[]>([]);
const pipelineQuery = ref({
  segmentId: '',
  keyword: '',
  status: ''
});

// 过滤后的管线列表
const filteredPipelines = computed(() => {
  return pipelineList.value.filter(item => {
    const matchesSearch = !pipelineQuery.value.keyword || 
      item.name?.toLowerCase().includes(pipelineQuery.value.keyword.toLowerCase()) ||
      item.code?.toLowerCase().includes(pipelineQuery.value.keyword.toLowerCase());
    
    const matchesStatus = !pipelineQuery.value.status || 
      item.status === pipelineQuery.value.status;
    
    return matchesSearch && matchesStatus;
  });
});

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});

// 使用组合式函数
const pipelineTab = usePipelineTab();

// 组件挂载时设置事件监听
onMounted(() => {
  window.addEventListener('error', handleGlobalError as any);
  window.addEventListener('unhandledrejection', handleUnhandledRejection as any);
  
  // 初始化数据
  initializeData();
});

// 组件卸载时清理
onUnmounted(() => {
  window.removeEventListener('error', handleGlobalError as any);
  window.removeEventListener('unhandledrejection', handleUnhandledRejection as any);
  
  // 清理地图实例
  if (mapInstance.value) {
    mapInstance.value.destroy();
  }
});

// 监听路由参数变化
watch(() => route.params.id, (newVal) => {
  if (newVal) {
    searchForm.value.segmentId = Array.isArray(newVal) ? newVal[0] : newVal;
    handleSearch();
  }
});

// 初始化数据
const initializeData = async () => {
  try {
    console.log('Component mounted, initializing...');
    
    // 从路由参数中获取 segmentId
    const { segmentId } = route.params;
    console.log('Route params:', route.params);
    
    if (segmentId) {
      // 处理可能的数组情况
      const id = Array.isArray(segmentId) ? segmentId[0] : segmentId;
      console.log('Setting segmentId from route:', id);
      searchForm.value.segmentId = id;
      
      try {
        // 确保 segmentOptions 已加载
        if (segmentOptions.value.length === 0) {
          console.log('No segment options found, loading...');
          await handleSegmentSearch('');
        }
        
        console.log('Calling handleSearch...');
        await handleSearch();
      } catch (searchError) {
        console.error('Error during search initialization:', searchError);
        // 重置状态以防止UI卡在加载状态
        currentSegment.value = null;
        pipelineList.value = [];
      }
    } else {
      console.log('No segmentId in route');
    }
    
    console.log('Component initialization complete');
  } catch (error) {
    console.error('Error during component mount:', error);
    // 确保UI不会卡在加载状态
    currentSegment.value = null;
    pipelineList.value = [];
  }
};

// 处理管廊段搜索
const handleSegmentSearch = async (query: string) => {
  try {
    segmentLoading.value = true;
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // 这里应该是调用API获取数据
    // const res = await getSegmentList({ keyword: query });
    // segmentOptions.value = res.data || [];
    
    // 模拟数据
    segmentOptions.value = [
      { id: '1', name: '管廊段A', code: 'GL001' },
      { id: '2', name: '管廊段B', code: 'GL002' },
      { id: '3', name: '管廊段C', code: 'GL003' }
    ];
    
  } catch (error) {
    console.error('搜索管廊段失败:', error);
    ElMessage.error('搜索管廊段失败');
  } finally {
    segmentLoading.value = false;
  }
};

// 处理搜索
const handleSearch = async () => {
  try {
    loading.value = true;
    const { segmentId } = searchForm.value;
    
    if (!segmentId) {
      ElMessage.warning('请选择管廊段');
      return;
    }
    
    // 查找选中的管廊段
    const segment = segmentOptions.value.find(item => item.id === segmentId);
    if (!segment) {
      ElMessage.warning('未找到对应的管廊段');
      return;
    }
    
    currentSegment.value = { ...segment };
    
    // 加载管线数据
    await fetchPipelineList(segmentId);
    
    // 加载设备数据
    await fetchDeviceList(segmentId);
    
    // 更新地图标记
    updateMapMarkers(segment);
    
  } catch (error) {
    console.error('搜索失败:', error);
    ElMessage.error('搜索失败，请重试');
  } finally {
    loading.value = false;
  }
};

// 获取管线列表
const fetchPipelineList = async (segmentId: string) => {
  try {
    // 这里应该是调用API获取数据
    // const res = await getPipelineList({ segmentId });
    // pipelineList.value = res.data?.list || [];
    
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500));
    pipelineList.value = [
      { id: '1', name: '给水管线', code: 'GS001', type: 'water', status: 'normal', length: 1200 },
      { id: '2', name: '电力管线', code: 'DL001', type: 'electricity', status: 'warning', length: 800 },
      { id: '3', name: '燃气管线', code: 'RQ001', type: 'gas', status: 'normal', length: 1500 }
    ];
    
  } catch (error) {
    console.error('获取管线列表失败:', error);
    throw error;
  }
};

// 获取设备列表
const fetchDeviceList = async (segmentId: string) => {
  try {
    deviceLoading.value = true;
    // 这里应该是调用API获取数据
    // const res = await getDeviceList({ segmentId });
    // deviceList.value = res.data?.list || [];
    
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500));
    deviceList.value = [
      { 
        id: '1', 
        name: '温湿度传感器', 
        code: 'WSD001', 
        type: 'sensor', 
        status: 'normal',
        location: 'A区-1号舱',
        installDate: '2023-01-15',
        lastCheckTime: '2023-05-20 09:30:00',
        maintainer: '张三',
        phone: '13800138000'
      },
      // 更多模拟数据...
    ];
    
    // 更新设备统计
    updateDeviceStats();
    
  } catch (error) {
    console.error('获取设备列表失败:', error);
    ElMessage.error('获取设备列表失败');
  } finally {
    deviceLoading.value = false;
  }
};

// 更新设备统计
const updateDeviceStats = () => {
  const stats = {
    total: deviceList.value.length,
    online: 0,
    warning: 0,
    offline: 0
  };
  
  deviceList.value.forEach(device => {
    if (device.status === 'normal') stats.online++;
    else if (device.status === 'warning') stats.warning++;
    else if (device.status === 'offline') stats.offline++;
  });
  
  deviceStats.value = stats;
};

// 更新地图标记
const updateMapMarkers = (segment: any) => {
  // 这里应该根据管廊段数据更新地图标记
  mapMarkers.value = [
    {
      id: segment.id,
      name: segment.name,
      position: [116.404, 39.915],
      type: 'segment',
      status: 'normal'
    }
    // 可以添加更多标记点
  ];
};

// 地图加载完成回调
const onMapLoaded = (map: any) => {
  mapInstance.value = map;
  // 可以在这里进行地图初始化操作
};

// 标记点点击事件
const onMarkerClick = (marker: any) => {
  console.log('Marker clicked:', marker);
  // 可以在这里处理标记点点击事件
};

// 地图点击事件
const onMapClick = (e: any) => {
  console.log('Map clicked:', e);
  // 可以在这里处理地图点击事件
};

// 地图缩放控制
const zoomIn = () => {
  if (mapInstance.value) {
    mapInstance.value.zoomIn();
  }
};

const zoomOut = () => {
  if (mapInstance.value) {
    mapInstance.value.zoomOut();
  }
};

const resetView = () => {
  if (mapInstance.value && currentSegment.value) {
    // 这里应该设置为当前管廊段的位置
    mapInstance.value.setCenter([116.404, 39.915]);
    mapInstance.value.setZoom(15);
  }
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    segmentId: ''
  };
  currentSegment.value = null;
  pipelineList.value = [];
  deviceList.value = [];
  mapMarkers.value = [];
};

// 处理标签页切换
const handleTabChange = (tab: string) => {
  console.log('Tab changed to:', tab);
  // 可以在这里处理标签页切换逻辑
};

// 类型定义
interface Device {
  id: string;
  name: string;
  code: string;
  type: string;
  status: 'normal' | 'warning' | 'error' | 'offline';
  location: string;
  installDate: string;
  lastCheckTime?: string;
  maintainer?: string;
  phone?: string;
  description?: string;
}

interface MapMarker {
  lng: number;
  lat: number;
  title: string;
  segment?: any;
}

interface DeviceStats {
  total: number;
  normal: number;
  warning: number;
  error: number;
}

// 组件状态
// 私有引用，不直接暴露给模板
const _segmentOptions = ref<Array<{id: string, name: string, code?: string}>>([]);

// 安全获取 segmentOptions 的方法
const getSafeSegmentOptions = (): Array<{ id: string; name: string; code?: string }> => {
  try {
    // 获取当前值
    const currentValue = _segmentOptions?.value;
    
    // 确保 value 是一个数组
    if (!Array.isArray(currentValue)) {
      console.warn('segmentOptions is not an array:', currentValue);
      return [];
    }
    
    // 确保数组中的每个元素都有必要的属性
    return currentValue.map(item => ({
      id: String(item?.id || ''),
      name: String(item?.name || '未命名'),
      code: item?.code ? String(item.code) : undefined
    }));
    
  } catch (error) {
    console.error('Error in getSafeSegmentOptions:', error);
    return [];
  }
};

// 安全包装的 segmentOptions
const segmentOptions = computed({
  get() {
    try {
      return getSafeSegmentOptions();
    } catch (error) {
      console.error('Error getting segmentOptions:', error);
      return [];
    }
  },
  set(newValue) {
    try {
      if (!Array.isArray(newValue)) {
        console.warn('Attempted to set segmentOptions to non-array value');
        _segmentOptions.value = [];
      } else {
        _segmentOptions.value = newValue;
      }
    } catch (error) {
      console.error('Error setting segmentOptions:', error);
      _segmentOptions.value = [];
    }
  }
});

// 安全获取 segment 选项
const getSegmentOptions = () => {
  try {
    const options = segmentOptions.value;
    return Array.isArray(options) ? options : [];
  } catch (error) {
    console.error('Error getting segment options:', error);
    return [];
  }
};

// 添加全局错误处理
const handleGlobalError = (event: ErrorEvent) => {
  try {
    console.error('Global error caught:', {
      message: event.message,
      filename: event.filename,
      lineno: event.lineno,
      colno: event.colno,
      error: event.error,
      stack: event.error?.stack
    });
    return true; // Prevent default error handling
  } catch (e) {
    console.error('Error in global error handler:', e);
    return false;
  }
};

const handleUnhandledRejection = (event: PromiseRejectionEvent) => {
  try {
    // 确保 event.reason 存在且不是 null/undefined
    const reason = event.reason || new Error('Unknown rejection reason');
    const errorInfo = {
      message: reason?.message || 'No error message',
      stack: reason?.stack || 'No stack trace',
      name: reason?.name || 'Error',
      reason: reason,
      // 添加更多可能有用的上下文信息
      timestamp: new Date().toISOString(),
      location: window.location.href
    };
    
    console.error('Unhandled promise rejection:', errorInfo);
    
    // 可以选择显示用户友好的错误提示
    ElMessage.error('发生错误，请刷新页面重试');
    
    // 阻止默认的未处理拒绝行为
    event.preventDefault();
  } catch (e) {
    console.error('Error in unhandled rejection handler:', e);
  }
};

// 添加事件监听器
if (typeof window !== 'undefined') {
  window.removeEventListener('error', handleGlobalError as any);
  window.removeEventListener('unhandledrejection', handleUnhandledRejection as any);
  
  window.addEventListener('error', handleGlobalError as any);
  window.addEventListener('unhandledrejection', handleUnhandledRejection as any);
}

// 添加数组安全访问方法
const safeArray = (arr: any) => {
  try {
    return Array.isArray(arr) ? arr : [];
  } catch (e) {
    console.error('Error in safeArray:', e);
    return [];
  }
};

// 当前选中的管廊段
const currentSegment = ref<any>(null);

// 设备相关状态
const deviceList = ref<Device[]>([]);
const deviceTotal = ref(0);
const devicePage = ref({
  pageNum: 1,
  
  pageSize: 10
});
const deviceStats = ref<DeviceStats>({
  total: 0,
  normal: 0,
  warning: 0,
  error: 0
});

// 设备类型
const deviceTypes = {
  camera: '摄像头',
  sensor: '传感器',
  alarm: '报警器',
  controller: '控制器'
};

// 地图相关状态
const mapCenter = ref({ lng: 116.404, lat: 39.915 });
const mapZoom = ref(15);
const mapMarkers = ref<MapMarker[]>([]);
const mapInstance = ref<any>(null);

// 导入管线标签页逻辑
const {
  pipelineList, 
  filteredPipelines, 
  loading, 
  pageParams, 
  pipelineQuery, 
  pipelineTypeOptions,
  handlePipelineQuery,
  resetPipelineQuery,
  fetchPipelineData,
  handleSizeChange,
  handleCurrentChange,
  setCurrentSegmentId,
  safeLength
} = usePipelineTab();

// 导入设备标签页逻辑
const {
  loadDeviceList,
  updateDeviceStats,
  handleDeviceSizeChange,
  handleDevicePageChange,
  handleViewDevice,
  handleEditDevice,
  handleAddDevice,
  getDeviceTypeLabel,
  getDeviceStatusLabel,
  getDeviceStatusType
} = useDeviceTab({
  deviceList,
  deviceStats,
  devicePage,
  currentSegment,
  deviceTypes
});

// 处理管廊段搜索
const handleSegmentSearch = async (query: string) => {
  try {
    segmentLoading.value = true;
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // 这里应该是调用API获取数据
    // const res = await getSegmentList({ keyword: query });
    // segmentOptions.value = res.data || [];
    
    // 模拟数据
    segmentOptions.value = [
      { id: '1', name: '管廊段A', code: 'GL001' },
      { id: '2', name: '管廊段B', code: 'GL002' },
      { id: '3', name: '管廊段C', code: 'GL003' }
    ];
    
  } catch (error) {
    console.error('搜索管廊段失败:', error);
    ElMessage.error('搜索管廊段失败');
  } finally {
    segmentLoading.value = false;
  }
};

// 处理搜索
const handleSearch = async () => {
  console.log('handleSearch called with form:', { ...searchForm.value });
  
  try {
    // 验证 segmentId
    const segmentId = searchForm.value?.segmentId;
    if (!segmentId) {
      console.warn('No segmentId provided');
      ElMessage.warning('请选择管廊段');
      return;
    }
    
    console.log('Searching for segment with ID:', segmentId);
    
    // 安全获取选项
    const options = getSegmentOptions();
    console.log('Available segment options:', options);
    
    if (!options || !options.length) {
      console.warn('No segment options available');
      ElMessage.warning('没有可用的管廊段数据');
      return;
    }
    
    // 查找选中的管廊段
    const selectedSegment = options.find(opt => opt.id === segmentId);
    console.log('Selected segment:', selectedSegment);
    
    if (!selectedSegment) {
      console.warn('Selected segment not found in options');
      ElMessage.warning('未找到选中的管廊段');
      return;
    }
    
    // 更新当前选中的管廊段
    currentSegment.value = selectedSegment;
    
    // 更新地图标记
    console.log('Updating map markers for segment:', selectedSegment);
    updateMapMarkers(selectedSegment);
    
    // 加载管线列表
    if (selectedSegment.id) {
      await fetchPipelineList(selectedSegment.id);
    }
    
    // 加载设备列表
    if (selectedSegment.id) {
      await fetchDeviceList(selectedSegment.id);
    }
    
    console.log('Search completed successfully');
  } catch (error) {
    const errorMessage = error instanceof Error ? error.message : '未知错误';
    const errorStack = error instanceof Error ? error.stack : 'No stack trace';
    
    console.error('Error in handleSearch:', {
      error: errorMessage,
      stack: errorStack,
      searchForm: { ...searchForm.value }
    });
    
    ElMessage.error(`搜索过程中发生错误: ${errorMessage}`);
    
    // 重置状态以防止UI卡在加载状态
    currentSegment.value = null;
    pipelineList.value = [];
  }
};

// 重置搜索
const resetSearch = () => {
  searchForm.value.segmentId = '';
  currentSegment.value = null;
  pipelineList.value = [];
  deviceList.value = [];
  mapMarkers.value = [];
};

// 处理标签页切换
const handleTabChange = (tab: string) => {
  activeTab.value = tab;
  if (tab === 'pipelines' && currentSegment.value) {
    fetchPipelineList(currentSegment.value.id);
  } else if (tab === 'device' && currentSegment.value) {
    loadDeviceList();
  }
};

// 地图相关方法
const onMapLoaded = (map: any) => {
  mapInstance.value = map;
  // 可以在这里获取地图实例进行更多配置
};

const onMarkerClick = (marker: any) => {
  // 处理标记点点击事件
  console.log('Marker clicked:', marker);
};

const onMapClick = (e: any) => {
  // 处理地图点击事件
  console.log('Map clicked:', e);
};

// 更新地图标记点
const updateMapMarkers = (segment: any) => {
  if (!segment) return;
  
  // 清空现有标记
  mapMarkers.value = [];
  
  // 添加新的标记点
  if (segment.longitude && segment.latitude) {
    mapMarkers.value.push({
      lng: parseFloat(segment.longitude),
      lat: parseFloat(segment.latitude),
      title: segment.name || '管廊段位置',
      segment: segment
    });
  }
  
  // 更新地图中心点
  if (mapMarkers.value.length > 0) {
    mapCenter.value = {
      lng: mapMarkers.value[0].lng,
      lat: mapMarkers.value[0].lat
    };
  }
};

// 地图缩放控制
const zoomIn = () => {
  if (mapInstance.value) {
    mapInstance.value.zoomIn();
  }
};

const zoomOut = () => {
  if (mapInstance.value) {
    mapInstance.value.zoomOut();
  }
};

const resetView = () => {
  if (mapInstance.value) {
    const point = new BMapGL.Point(mapCenter.value.lng, mapCenter.value.lat);
    mapInstance.value.centerAndZoom(point, mapZoom.value);
  }
};

// 获取风险等级对应的样式类
const getRiskLevelClass = (level: string) => {
  const levelMap: Record<string, string> = {
    '低': 'text-green-600',
    '中': 'text-yellow-600',
    '高': 'text-red-600'
  };
  return levelMap[level] || '';
};

// 获取舱室类型对应的标签样式
const getCompartmentType = (type: string) => {
  const typeMap: Record<string, string> = {
    'electricity': 'bg-blue-100 text-blue-800',
    'water': 'bg-cyan-100 text-cyan-800',
    'gas': 'bg-orange-100 text-orange-800',
    'telecom': 'bg-purple-100 text-purple-800'
  };
  return typeMap[type] || 'bg-gray-100 text-gray-800';
};

// 获取舱室类型的中文名称
const getCompartmentTypeName = (type: string) => {
  const typeMap: Record<string, string> = {
    'electricity': '电力舱',
    'water': '水信舱',
    'gas': '燃气舱',
    'telecom': '通信舱'
  };
  return typeMap[type] || '未知类型';
};

// 组件卸载时清理
onUnmounted(() => {
  console.log('TunnelManagement component unmounted, cleaning up...');
  // 清理事件监听器
  if (typeof window !== 'undefined') {
    window.removeEventListener('error', handleGlobalError as any);
    window.removeEventListener('unhandledrejection', handleUnhandledRejection as any);
  }
  
  // 清理引用
  currentSegment.value = null;
  deviceList.value = [];
});

// 组件挂载后加载数据
onMounted(async () => {
  try {
    console.log('Component mounted, initializing...');
    
    // 从路由参数中获取 segmentId
    const { segmentId } = route.params;
    console.log('Route params:', route.params);
    
    if (segmentId) {
      // 处理可能的数组情况
      const id = Array.isArray(segmentId) ? segmentId[0] : segmentId;
      console.log('Setting segmentId from route:', id);
      searchForm.value.segmentId = id;
      
      try {
        // 确保 segmentOptions 已加载
        if (segmentOptions.value.length === 0) {
          console.log('No segment options found, loading...');
          await handleSegmentSearch('');
        }
        
        console.log('Calling handleSearch...');
        await handleSearch();
      } catch (searchError) {
        console.error('Error during search initialization:', searchError);
        // 重置状态以防止UI卡在加载状态
        currentSegment.value = null;
        pipelineList.value = [];
      }
    } else {
      console.log('No segmentId in route');
    }
    
    console.log('Component initialization complete');
  } catch (error) {
    console.error('Error during component mount:', error);
    // 确保UI不会卡在加载状态
    currentSegment.value = null;
    pipelineList.value = [];
  }
});

// 监听路由参数变化
watch(() => route.params.id, (newVal) => {
  if (newVal) {
    searchForm.value.segmentId = Array.isArray(newVal) ? newVal[0] : newVal;
    handleSearch();
  }
});

// 组件销毁前清理
onBeforeUnmount(() => {
  if (mapInstance.value) {
    mapInstance.value.destroy();
  }
});

// 导出组件
defineOptions({
  name: 'TunnelManagement'
});

// 导出组件
const props = defineProps({
  // 组件属性
});

// 定义事件
const emit = defineEmits(['update:modelValue']);

// 暴露方法给父组件
defineExpose({
  refresh: handleSearch
});
</script>

<style lang="scss" scoped>
.tunnel-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  
  .pipeline-table {
    :deep(.el-table__header) {
      th {
        &.is-sortable {
          cursor: pointer;
          
          .cell {
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            .el-icon {
              margin-left: 4px;
            }
          }
        }
      }
    }
  }
  padding: 16px;
  box-sizing: border-box;
  overflow: hidden;

  .search-container {
    background: #fff;
    padding: 16px;
    margin-bottom: 16px;
    border-radius: 4px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

    .el-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      align-items: center;

      .el-form-item {
        margin-bottom: 0;
      }

      .search-button {
        margin-left: auto;
      }
    }
  }

  .content-container {
    display: flex;
    flex: 1;
    gap: 16px;
    overflow: hidden;

    .left-sidebar {
      width: 380px;
      display: flex;
      flex-direction: column;
      background: #fff;
      border-radius: 4px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
      overflow: hidden;

      .segment-header {
        padding: 12px 16px;
        border-bottom: 1px solid #ebeef5;
        font-weight: 600;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #f8f9fa;
      }

      .segment-content {
        padding: 16px;
        flex: 1;
        overflow-y: auto;

        .info-item {
          display: flex;
          margin-bottom: 12px;
          line-height: 1.5;

          .label {
            color: #909399;
            width: 90px;
            flex-shrink: 0;
          }

          .value {
            flex: 1;
            color: #303133;
            word-break: break-all;
          }
        }


        .structure-list {
          .structure-item {
            display: flex;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px dashed #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .icon-wrapper {
              margin-right: 12px;
              color: #409eff;
            }

            .structure-info {
              flex: 1;
              .structure-name {
                margin-bottom: 4px;
              }
              .structure-location {
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }


        .pipeline-list {
          .el-table {
            th {
              background-color: #f8f9fa;
            }

            .el-tag {
              margin-right: 4px;
              margin-bottom: 4px;
            }
          }
        }
      }
    }


    .right-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      background: #fff;
      border-radius: 4px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
      overflow: hidden;

      .map-container {
        position: relative;
        flex: 1;
        background: #f5f7fa;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #909399;
        font-size: 16px;
        overflow: hidden;

        .map-controls {
          position: absolute;
          right: 16px;
          top: 16px;
          z-index: 10;
          display: flex;
          flex-direction: column;
          gap: 8px;

          .control-btn {
            width: 32px;
            height: 32px;
            border-radius: 4px;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
            transition: all 0.3s;

            &:hover {
              background: #f5f7fa;
              color: #409eff;
            }
          }
        }
      }


      .cross-section-container {
        height: 180px;
        border-top: 1px solid #ebeef5;
        padding: 16px;
        display: flex;
        flex-direction: column;

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;
          font-weight: 600;
        }


        .section-content {
          flex: 1;
          background: #f8f9fa;
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }


  .compartment-tag {
    margin-right: 8px;
    margin-bottom: 8px;
  }

  .compartment-item {
    margin-bottom: 16px;
    padding: 12px;
    background-color: #f8f9fa;
    border-radius: 4px;
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }

    .compartment-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      font-weight: 500;
    }
    .compartment-desc {
      font-size: 13px;
      color: #606266;
      line-height: 1.5;
    }
  }

  .stats-card {
    margin-bottom: 16px;
    .el-card__body {
      padding: 12px 16px;
    }
    .stat-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .stat-label {
        color: #909399;
      }
      
      .stat-value {
        font-weight: 600;
        color: #303133;
      }
    }
  }
}

@media (max-width: 1200px) {
  .tunnel-management .content-container {
    flex-direction: column;
    
    .left-sidebar,
    .right-content {
      width: 100%;
      height: auto;
    }
    
    .left-sidebar {
      margin-bottom: 16px;
    }
  }
}

.app-container {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  margin-bottom: 24px;
}

.header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
}

.subtitle {
  margin: 8px 0 0;
  font-size: 14px;
  color: #909399;
}

.content {
  flex: 1;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.test-content {
  padding: 20px;
  line-height: 1.6;
}

.test-content h3 {
  color: #303133;
  margin-top: 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.test-content ul {
  padding-left: 20px;
}

.test-content li {
  margin-bottom: 8px;
  color: #606266;
}

.el-divider {
  margin: 16px 0;
}
</style>
