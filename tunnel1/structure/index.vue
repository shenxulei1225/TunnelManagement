<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="管廊段级结构管理" name="segment">
        <router-view />
      </el-tab-pane>
      <el-tab-pane label="舱室级结构管理" name="chamber">
        <router-view />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeTab = ref('segment')

// 监听路由变化，更新激活的标签页
watch(() => route.name, (newVal) => {
  if (newVal?.includes('Segment')) {
    activeTab.value = 'segment'
  } else if (newVal?.includes('Chamber')) {
    activeTab.value = 'chamber'
  }
}, { immediate: true })

// 监听标签页变化，跳转到对应路由
watch(activeTab, (newVal) => {
  if (newVal === 'segment') {
    router.push({ name: 'TunnelSegmentStructure' })
  } else if (newVal === 'chamber') {
    router.push({ name: 'TunnelChamberStructure' })
  }
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>
