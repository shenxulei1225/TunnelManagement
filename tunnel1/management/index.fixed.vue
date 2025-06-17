<!-- 首先保留模板部分 -->
<template>
  <!-- 模板内容保持不变 -->
  <div class="app-container h-full">
    <!-- 现有模板内容 -->
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

// 当前选中的管廊段 - 只保留这一处声明
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

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});

// 使用组合式函数
const pipelineTab = usePipelineTab();
const deviceTab = useDeviceTab();

// 安全获取 segmentOptions 的方法
const getSafeSegmentOptions = (): Array<{ id: string; name: string; code?: string }> => {
  try {
    const currentValue = _segmentOptions.value;
    
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

// 全局错误处理
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
    return true; // 阻止默认错误处理
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
      timestamp: new Date().toISOString(),
      location: window.location.href
    };
    
    console.error('Unhandled promise rejection:', errorInfo);
    
    // 显示用户友好的错误提示
    ElMessage.error('发生错误，请刷新页面重试');
    
    // 阻止默认的未处理拒绝行为
    event.preventDefault();
  } catch (e) {
    console.error('Error in unhandled rejection handler:', e);
  }
};

// 组件挂载时设置事件监听
onMounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('error', handleGlobalError as any);
    window.removeEventListener('unhandledrejection', handleUnhandledRejection as any);
    
    window.addEventListener('error', handleGlobalError as any);
    window.addEventListener('unhandledrejection', handleUnhandledRejection as any);
  }
  
  // 初始化数据
  initializeData();
});

// 组件卸载时清理
onUnmounted(() => {
  console.log('TunnelManagement component unmounted, cleaning up...');
  // 清理事件监听器
  window.removeEventListener('error', handleGlobalError as any);
  window.removeEventListener('unhandledrejection', handleUnhandledRejection as any);
});

// 类型定义
interface Device {
  id: string;
  name: string;
  code?: string;
  type: string;
  status: string;
  location?: string;
  installDate?: string;
  [key: string]: any;
}

interface MapMarker {
  position: [number, number];
  info: string;
  type: string;
  id: string;
}

interface DeviceStats {
  total: number;
  normal: number;
  warning: number;
  error: number;
}

// 私有引用，不直接暴露给模板
const _segmentOptions = ref<Array<{id: string, name: string, code?: string}>>([]);

// 这里添加其他必要的函数和逻辑...
</script>

<style scoped>
/* 保留现有的样式 */
.app-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}
/* 其他样式... */
</style>
