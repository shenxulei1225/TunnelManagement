import { ref, reactive, onMounted } from 'vue'

export function useDeviceTab() {
  // 设备相关数据
  const deviceList = ref([])
  const deviceLoading = ref(false)
  const deviceTotal = ref(0)
  const devicePage = reactive({
    pageNum: 1,
    pageSize: 10
  })
  const deviceStats = ref({
    total: 0,
    normal: 0,
    warning: 0,
    error: 0
  })

  // 设备类型映射
  const deviceTypes = {
    camera: '监控摄像头',
    sensor: '传感器',
    controller: '控制器',
    alarm: '报警器',
    light: '照明',
    fan: '风机',
    pump: '水泵',
    other: '其他'
  }

  // 设备状态映射
  const deviceStatus = {
    normal: { label: '正常', type: 'success' },
    warning: { label: '告警', type: 'warning' },
    error: { label: '故障', type: 'danger' },
    offline: { label: '离线', type: 'info' }
  }

  // 获取设备类型标签
  const getDeviceTypeLabel = (type) => {
    return deviceTypes[type] || type
  }
  
  // 获取设备状态标签
  const getDeviceStatusLabel = (status) => {
    return deviceStatus[status]?.label || status
  }
  
  // 获取设备状态类型
  const getDeviceStatusType = (status) => {
    return deviceStatus[status]?.type || ''
  }
  
  // 更新设备统计信息
  const updateDeviceStats = () => {
    const stats = {
      total: deviceList.value.length,
      normal: 0,
      warning: 0,
      error: 0
    }
    
    deviceList.value.forEach(device => {
      if (device.status === 'normal') stats.normal++
      else if (device.status === 'warning') stats.warning++
      else if (device.status === 'error') stats.error++
    })
    
    deviceStats.value = stats
  }
  
  // 加载设备列表
  const loadDeviceList = async () => {
    try {
      deviceLoading.value = true
      // 模拟数据
      deviceList.value = Array.from({ length: 15 }, (_, i) => ({
        id: `device-${i + 1}`,
        name: `设备${i + 1}`,
        code: `DEV-${1000 + i}`,
        type: Object.keys(deviceTypes)[i % Object.keys(deviceTypes).length],
        status: ['normal', 'warning', 'error', 'offline'][i % 4],
        location: `位置${i + 1}`,
        installDate: '2023-01-01'
      }))
      deviceTotal.value = 15
      
      updateDeviceStats()
    } catch (error) {
      console.error('加载设备列表失败', error)
    } finally {
      deviceLoading.value = false
    }
  }
  
  // 分页大小改变
  const handleDeviceSizeChange = (val) => {
    devicePage.pageSize = val
    loadDeviceList()
  }
  
  // 当前页改变
  const handleDevicePageChange = (val) => {
    devicePage.pageNum = val
    loadDeviceList()
  }
  
  // 查看设备详情
  const handleViewDevice = (row) => {
    console.log('查看设备详情', row)
    // 这里可以打开设备详情弹窗或跳转到详情页
  }
  
  // 编辑设备
  const handleEditDevice = (row) => {
    console.log('编辑设备', row)
    // 这里可以打开编辑设备弹窗
  }
  
  // 添加设备
  const handleAddDevice = () => {
    console.log('添加设备')
    // 这里可以打开添加设备弹窗
  }
  
  // 初始化加载设备列表
  onMounted(() => {
    loadDeviceList()
  })
  
  return {
    deviceList,
    deviceLoading,
    deviceTotal,
    devicePage,
    deviceStats,
    getDeviceTypeLabel,
    getDeviceStatusLabel,
    getDeviceStatusType,
    handleDeviceSizeChange,
    handleDevicePageChange,
    handleViewDevice,
    handleEditDevice,
    handleAddDevice,
    loadDeviceList
  }
}
