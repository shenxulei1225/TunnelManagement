<template>
  <ContentWrap>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>养护标准管理</span>
        </div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="养护标准分类" name="category">
          <MaintenanceStandardCategory />
        </el-tab-pane>
        <el-tab-pane label="养护标准项目" name="item">
          <MaintenanceStandardItem />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </ContentWrap>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import MaintenanceStandardCategory from './category/index.vue'
import MaintenanceStandardItem from './item/index.vue'

const activeTab = ref('category')
</script>
