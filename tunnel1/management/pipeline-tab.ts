import { ref, reactive, computed, onUnmounted } from 'vue'; // 添加 onUnmounted
import { ElMessage } from 'element-plus';

// 安全获取长度值
const safeLength = (value: any): number => {
  if (value === null || value === undefined) return 0;
  const num = Number(value);
  return Number.isFinite(num) ? num : 0;
};

// 声明全局类型
declare global {
  interface Window {
    currentSegmentId: string | null;
  }
}

// 类型定义
interface Pipeline {
  id: string;
  code: string;
  name: string;
  type: string;
  typeName: string;
  compartmentId: string;
  segmentId?: string;
  material: string;
  status: string;
  createTime: string;
  updateTime: string;
  pressure?: string;
  diameter?: string;
  voltage?: string;
  cableType?: string;
  temperature?: string;
}

declare global {
  interface Window {
    currentSegmentId: string | null;
  }
}

export function usePipelineTab() {
  // 管线查询参数
  interface PipelineQuery {
    compartmentId: string;
    type: string;
    keyword: string;
  }

  // 分页参数
  interface PageParams {
    pageNum: number;
    pageSize: number;
    total: number;
  }

  const pipelineQuery = reactive<PipelineQuery>({
    compartmentId: '',
    type: '',
    keyword: ''
  });
  
  // 管线分页参数
  const pageParams = reactive<PageParams>({
    pageNum: 1,
    pageSize: 10,
    total: 0
  });
  
  // 管线列表数据
  const pipelineList = ref<Pipeline[]>([]);
  const loading = ref(false);
  
  // 安全访问管线列表
  const filteredPipelines = computed(() => {
    try {
      return Array.isArray(pipelineList.value) ? pipelineList.value : [];
    } catch (error) {
      console.error('Error accessing pipeline list:', error);
      return [];
    }
  });
  
  // 处理管线查询
  const handlePipelineQuery = () => {
    try {
      console.log('Handling pipeline query:', { ...pipelineQuery });
      // 重置到第一页
      pageParams.pageNum = 1;
      // 触发数据加载
      const currentSegmentId = window.currentSegmentId || null;
      console.log('Fetching pipeline data for segment:', currentSegmentId);
      fetchPipelineData(currentSegmentId);
    } catch (error) {
      console.error('Error in handlePipelineQuery:', error);
      ElMessage.error('查询管线数据失败');
    }
  };
  
  // 重置管线查询
  const resetPipelineQuery = () => {
    try {
      console.log('Resetting pipeline query');
      pipelineQuery.compartmentId = '';
      pipelineQuery.type = '';
      pipelineQuery.keyword = '';
      handlePipelineQuery();
    } catch (error) {
      console.error('Error in resetPipelineQuery:', error);
      ElMessage.error('重置查询条件失败');
    }
  };
  
  // 管线类型配置
  interface PipeTypeConfig {
    name: string;
    pressureLevels?: string[];
    diameters?: string[];
    voltageLevels?: string[];
    cableTypes?: string[];
    tempLevels?: string[];
    types?: string[];
  }

  // 管线类型键名
  type PipeTypeKey = 'water' | 'power' | 'communication' | 'gas' | 'heating' | 'drainage';

  // 管线类型配置
  const pipeTypes: Record<PipeTypeKey, PipeTypeConfig> = {
    water: { 
      name: '给水', 
      pressureLevels: ['0.6MPa', '1.0MPa', '1.6MPa'], 
      diameters: ['DN100', 'DN150', 'DN200'] 
    },
    power: { 
      name: '电力', 
      voltageLevels: ['0.4kV', '10kV', '35kV'], 
      cableTypes: ['YJV', 'VV', 'BV'] 
    },
    communication: { 
      name: '通信', 
      types: ['电信', '移动', '联通'], 
      cableTypes: ['光缆', '电缆'] 
    },
    gas: { 
      name: '燃气', 
      pressureLevels: ['0.4MPa', '0.8MPa', '1.6MPa'], 
      diameters: ['DN50', 'DN80', 'DN100'] 
    },
    heating: { 
      name: '热力', 
      pressureLevels: ['1.0MPa', '1.6MPa'], 
      diameters: ['DN100', 'DN150', 'DN200'], 
      tempLevels: ['95/70°C', '130/70°C'] 
    },
    drainage: { 
      name: '排水', 
      types: ['雨水', '污水', '合流'], 
      diameters: ['DN200', 'DN300', 'DN400', 'DN500'] 
    }
  };

  // 生成管线模拟数据
  const generatePipelineData = (count: number): Pipeline[] => {
    const result: Pipeline[] = [];
    
    try {
      // 材质映射
      const materials: Record<PipeTypeKey, string[]> = {
        water: ['球墨铸铁', 'PE', '钢管', 'PPR'],
        power: ['铜', '铝', '铝合金'],
        communication: ['光纤', '铜缆', '同轴'],
        gas: ['PE', '钢管', '铸铁管'],
        heating: ['钢管', 'PERT', 'PPR'],
        drainage: ['HDPE', 'PVC', '混凝土', '玻璃钢']
      };
      
      // 状态
      const statuses = ['normal', 'warning', 'danger'] as const;
      type StatusType = typeof statuses[number];
      
      // 生成指定数量的管线数据
      for (let i = 1; i <= count; i++) {
        try {
          // 随机选择管道类型
          const typeKeys = Object.keys(pipeTypes) as PipeTypeKey[];
          const typeKey = typeKeys[Math.floor(Math.random() * typeKeys.length)];
          const pipeType = pipeTypes[typeKey];
          
          if (!pipeType) {
            console.warn(`Invalid pipe type key: ${typeKey}`);
            continue;
          }
          
          // 生成随机属性
          const status = statuses[Math.floor(Math.random() * statuses.length)] as StatusType;
          const material = materials[typeKey]?.[Math.floor(Math.random() * materials[typeKey].length)] || '未知';
          
          // 构建管线数据
          const pipeline: Pipeline = {
            id: `pipe-${Date.now()}-${i}`,
            name: `${pipeType.name}管线-${i}`,
            type: typeKey,
            typeName: pipeType.name,
            status,
            material,
            length: safeLength(Math.round((Math.random() * 1000 + 100) * 100) / 100), // 100-1100米，使用safeLength确保不为null/undefined
            diameter: pipeType.diameters?.[Math.floor(Math.random() * (pipeType.diameters?.length || 1))] || '',
            depth: Math.round((Math.random() * 2 + 0.5) * 100) / 100, // 0.5-2.5米
            startPoint: `起点${i}`,
            endPoint: `终点${i}`,
            owner: '市政部门',
            buildTime: new Date(2010 + Math.floor(Math.random() * 10), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1)
              .toISOString()
              .split('T')[0],
            lastCheckTime: new Date(2020 + Math.floor(Math.random() * 3), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1)
              .toISOString()
              .split('T')[0],
            description: `这是${pipeType.name}管线的描述信息`,
            segmentId: `segment-${Math.floor(Math.random() * 5) + 1}` // 1-5号管段
          };
          
          // 添加特定类型的属性
          if (typeKey === 'power' && pipeType.voltageLevels) {
            pipeline.voltage = pipeType.voltageLevels[Math.floor(Math.random() * pipeType.voltageLevels.length)];
          } else if (pipeType.pressureLevels) {
            pipeline.pressure = pipeType.pressureLevels[Math.floor(Math.random() * pipeType.pressureLevels.length)];
          }
          
          if (pipeType.tempLevels) {
            pipeline.temperature = pipeType.tempLevels[Math.floor(Math.random() * pipeType.tempLevels.length)];
          }
          
          result.push(pipeline);
        } catch (error) {
          console.error(`Error generating pipeline ${i}:`, error);
        }
      }
    } catch (error) {
      console.error('Error in generatePipelineData:', error);
      ElMessage.error('生成管线数据时出错');
    }
    
    console.log(`Generated ${result.length} pipeline records`);
    return result;
  };
  
  // 获取管线数据
  const fetchPipelineData = async (segmentId?: string | null) => {
    console.log('fetchPipelineData called with segmentId:', segmentId);
    
    // 如果没有提供 segmentId 并且没有有效的 currentSegment，则直接返回
    if (!segmentId) {
      console.warn('No segmentId provided to fetchPipelineData');
      pipelineList.value = [];
      pageParams.total = 0;
      return;
    }
    
    try {
      loading.value = true;
      console.log('Loading pipeline data for segment:', segmentId);
      
      // 模拟API调用延迟
      console.log('Simulating API call delay...');
      await new Promise(resolve => setTimeout(resolve, 100));
      
      // 生成模拟数据
      console.log('Generating mock pipeline data...');
      const allData = generatePipelineData(100);
      
      // 确保生成的数据有效
      if (!Array.isArray(allData)) {
        console.error('Generated data is not an array:', allData);
        throw new Error('Failed to generate pipeline data');
      }
      
      console.log(`Generated ${allData?.length || 0} mock pipeline records`);
      
      // 初始化过滤后的数据
      let filteredData: Pipeline[] = [];
      
      try {
        // 确保 allData 是数组且不为空
        if (Array.isArray(allData) && allData.length > 0) {
          // 过滤掉 null/undefined 项并确保所有项都有 segmentId 和安全的 length
          filteredData = allData
            .filter((item): item is Pipeline => item != null && typeof item === 'object')
            .map(item => {
              // 确保 length 字段安全
              const safeItem = {
                ...item,
                length: safeLength(item?.length),
                segmentId: item?.segmentId || segmentId // 确保每条数据都有 segmentId
              };
              return safeItem;
            });
            
          console.log(`After initial filtering, item count: ${filteredData.length}`);
          
          // 过滤当前段的数据
          filteredData = filteredData.filter(item => item?.segmentId === segmentId);
          console.log(`After filtering by segment ${segmentId}, count: ${filteredData.length}`);
          
          // 应用其他筛选条件
          if (pipelineQuery.compartmentId) {
            const countBefore = filteredData.length;
            filteredData = filteredData.filter(item => item?.compartmentId === pipelineQuery.compartmentId);
            console.log(`Filtered by compartment ${pipelineQuery.compartmentId}: ${countBefore} -> ${filteredData.length} items`);
          }
          
          if (pipelineQuery.type) {
            const countBefore = filteredData.length;
            filteredData = filteredData.filter(item => item?.type === pipelineQuery.type);
            console.log(`Filtered by type ${pipelineQuery.type}: ${countBefore} -> ${filteredData.length} items`);
          }
          
          if (pipelineQuery.keyword) {
            const countBefore = filteredData.length;
            const keyword = pipelineQuery.keyword.toLowerCase();
            filteredData = filteredData.filter(item => 
              item && (
                (item.code || '').toLowerCase().includes(keyword) || 
                (item.name || '').toLowerCase().includes(keyword)
              )
            );
            console.log(`Filtered by keyword "${pipelineQuery.keyword}": ${countBefore} -> ${filteredData.length} items`);
          }
        } else {
          console.warn('No data available after initial filtering');
          filteredData = [];
        }
      } catch (filterError) {
        console.error('Error during data filtering:', filterError);
        filteredData = [];
      }
      
      // 应用分页
      let paginatedData: Pipeline[] = [];
      let totalItems = 0;
      
      try {
        if (!Array.isArray(filteredData)) {
          console.warn('filteredData is not an array, resetting to empty array');
          filteredData = [];
        }
        
        totalItems = filteredData.length;
        const start = Math.max(0, (pageParams.pageNum - 1) * pageParams.pageSize);
        const end = Math.min(start + pageParams.pageSize, totalItems);
        
        // 确保 start 和 end 是有效值
        if (start >= 0 && end >= start) {
          paginatedData = filteredData.slice(start, end);
          console.log(`Paginated data: showing ${start + 1}-${end} of ${totalItems} items`);
        } else {
          console.warn('Invalid pagination range:', { start, end, totalItems });
          paginatedData = [];
          totalItems = 0;
        }
      } catch (paginationError) {
        console.error('Error during pagination:', paginationError);
        paginatedData = [];
        totalItems = 0;
      }
      
      // 更新响应式数据
      pipelineList.value = paginatedData || [];
      pageParams.total = totalItems;
      
      console.log('Pipeline data loaded successfully', {
        total: pageParams.total,
        currentPage: pageParams.pageNum,
        pageSize: pageParams.pageSize,
        items: paginatedData.length
      });
      
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      const errorStack = error instanceof Error ? error.stack : 'No stack trace';
      
      console.error('Failed to fetch pipeline data:', {
        error: errorMessage,
        stack: errorStack,
        segmentId,
        query: { ...pipelineQuery },
        pageParams: { ...pageParams }
      });
      
      // 显示用户友好的错误信息
      ElMessage.error(`获取管线数据失败: ${errorMessage}`);
      
      // 清空当前数据
      pipelineList.value = [];
      pageParams.total = 0;
      
    } finally {
      loading.value = false;
      console.log('Loading state set to false');
    }
  };
  
  // 处理分页大小改变
  const handleSizeChange = (val: number) => {
    try {
      if (isNaN(val) || val <= 0) {
        console.error('Invalid page size:', val);
        return;
      }
      console.log('Page size changed to:', val);
      pageParams.pageSize = val;
      pageParams.pageNum = 1; // 重置到第一页
      
      // 获取当前段ID（如果有）
      const currentSegmentId = window.currentSegmentId || null;
      console.log('Handling page size change for segment:', currentSegmentId);
      
      fetchPipelineData(currentSegmentId);
    } catch (error) {
      console.error('Error in handleSizeChange:', error);
      ElMessage.error('分页大小设置失败');
    }
  };
  
  // 处理当前页改变
  const handleCurrentChange = (val: number) => {
    try {
      if (isNaN(val) || val <= 0) {
        console.error('Invalid page number:', val);
        return;
      }
      console.log('Current page changed to:', val);
      pageParams.pageNum = val;
      
      // 获取当前段ID（如果有）
      const currentSegmentId = window.currentSegmentId || null;
      console.log('Handling page change for segment:', currentSegmentId);
      
      fetchPipelineData(currentSegmentId);
    } catch (error) {
      console.error('Error in handleCurrentChange:', error);
      ElMessage.error('页码切换失败');
    }
  };
  
  // 更新当前段ID的方法
  const setCurrentSegmentId = (segmentId: string | null) => {
    try {
      console.log('Updating current segment ID:', segmentId);
      window.currentSegmentId = segmentId;
      
      // 如果当前在管线标签页，重新加载数据
      if (segmentId) {
        fetchPipelineData(segmentId);
      }
    } catch (error) {
      console.error('Error in setCurrentSegmentId:', error);
      ElMessage.error('更新当前段ID失败');
    }
  };
  
  // 获取管线类型选项
  type PipelineType = 'water' | 'power' | 'communication' | 'gas' | 'heating' | 'drainage';
  
  const pipelineTypeOptions = computed<Record<PipelineType, string>>(() => ({
    water: '给水',
    power: '电力',
    communication: '通信',
    gas: '燃气',
    heating: '热力',
    drainage: '排水'
  }));
  
  // 初始化全局变量
  if (typeof window !== 'undefined' && window.currentSegmentId === undefined) {
    window.currentSegmentId = null;
  }

  // 确保返回的pipelineList中的length字段都经过safeLength处理
  const safePipelineList = computed(() => {
    try {
      if (!Array.isArray(pipelineList.value)) {
        console.debug('pipelineList is not an array, returning empty array');
        return [];
      }
      
      // 添加调试信息
      const result = pipelineList.value.map(item => {
        try {
          return {
            ...item,
            length: safeLength(item?.length)
          };
        } catch (itemError) {
          console.error('Error processing pipeline item:', { item, error: itemError });
          return null;
        }
      }).filter(Boolean); // 过滤掉处理失败的项
      
      console.debug(`Processed ${result.length} pipeline items`);
      return result;
      
    } catch (error) {
      console.error('Error in safePipelineList computed:', error);
      return [];
    }
  });
  
  // 组件卸载时清理
  onUnmounted(() => {
    console.log('PipelineTab component unmounted, cleaning up...');
    // 清理可能导致内存泄漏的引用
    pipelineList.value = [];
  });

  return {
    // 状态
    pipelineList: safePipelineList,
    filteredPipelines: computed(() => safePipelineList.value),
    loading,
    pageParams,
    pipelineQuery,
    pipelineTypeOptions,
    safeLength, // 导出safeLength方法，以便在模板中使用
    
    // 方法
    handlePipelineQuery,
    resetPipelineQuery,
    fetchPipelineData,
    handleSizeChange,
    handleCurrentChange,
    setCurrentSegmentId
  };
}
