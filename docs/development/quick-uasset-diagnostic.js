/**
 * 快速UAsset诊断脚本
 * 用于检查解析结果和状态
 */

console.log('🔧 快速UAsset诊断工具已加载');

// 检查解析状态
function checkParseStatus() {
  console.log('🔍 检查UAsset解析状态...');
  
  // 检查是否有组件被创建
  const designerStore = window.$nuxt?.$store?.state?.designer;
  if (designerStore) {
    const instances = designerStore.instances || [];
    console.log(`📊 当前设计器组件数量: ${instances.length}`);
    
    if (instances.length > 0) {
      console.log('📋 组件列表:');
      instances.forEach((instance, index) => {
        console.log(`${index + 1}. ${instance.type} - ${instance.name} (${instance.id})`);
        if (instance.metadata?.originalPlatform === 'umg') {
          console.log(`   🎯 UMG组件: ${instance.metadata.umgClassName || 'unknown'}`);
        }
      });
    } else {
      console.log('⚠️ 设计器中没有组件');
    }
  } else {
    console.log('❌ 无法访问设计器状态');
  }
  
  // 检查最近的导入操作
  if (window.lastImportResult) {
    console.log('📥 最近的导入结果:', window.lastImportResult);
  } else {
    console.log('ℹ️ 没有最近的导入结果记录');
  }
}

// 模拟导入操作
function simulateImport() {
  console.log('🧪 模拟UAsset导入操作...');
  
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.uasset';
  input.onchange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    
    console.log(`📂 选择的文件: ${file.name} (${(file.size / 1024).toFixed(2)} KB)`);
    
    try {
      const buffer = await file.arrayBuffer();
      
      // 保存到全局变量
      window.testBuffer = buffer;
      window.testFileName = file.name;
      
      // 快速分析
      console.log('🔬 快速文件分析:');
      const view = new DataView(buffer);
      
      // 检查魔数
      const tag = view.getUint32(0, true);
      const isValidMagic = tag === 0x9E2A83C1;
      console.log(`魔数: 0x${tag.toString(16).toUpperCase()} ${isValidMagic ? '✅' : '❌'}`);
      
      // 检查版本
      if (buffer.byteLength >= 24) {
        const ue4Version = view.getInt32(12, true);
        const ue5Version = view.getInt32(16, true);
        console.log(`UE版本: UE4=${ue4Version}, UE5=${ue5Version}`);
      }
      
      // 手动调用UMGConverter进行解析测试
      if (window.UMGConverter) {
        console.log('🔄 手动调用UMGConverter...');
        const converter = new window.UMGConverter();
        
        try {
          const result = await converter.import(buffer);
          console.log('✅ 导入成功!', result);
          window.lastImportResult = result;
          
          if (result && result.length > 0) {
            console.log(`🎉 成功创建 ${result.length} 个组件:`);
            result.forEach((comp, i) => {
              console.log(`${i + 1}. ${comp.type} - ${comp.name}`);
            });
          } else {
            console.log('⚠️ 没有创建任何组件 - 文件可能不包含UMG控件');
          }
        } catch (error) {
          console.error('❌ 导入失败:', error);
          window.lastImportError = error;
        }
      } else {
        console.log('❌ UMGConverter不可用');
      }
      
    } catch (error) {
      console.error('❌ 文件读取失败:', error);
    }
  };
  
  input.click();
}

// 显示详细的解析日志
function enableDetailedLogging() {
  console.log('📝 启用详细解析日志...');
  
  // 重写console.log以便捕获UMGConverter的日志
  const originalLog = console.log;
  const originalWarn = console.warn;
  const originalError = console.error;
  
  window.umgLogs = [];
  
  console.log = function(...args) {
    if (args[0] && typeof args[0] === 'string' && 
        (args[0].includes('📦') || args[0].includes('📋') || args[0].includes('🔍') || 
         args[0].includes('✅') || args[0].includes('⚠️') || args[0].includes('❌'))) {
      window.umgLogs.push({ type: 'log', message: args.join(' '), timestamp: new Date() });
    }
    originalLog.apply(console, args);
  };
  
  console.warn = function(...args) {
    window.umgLogs.push({ type: 'warn', message: args.join(' '), timestamp: new Date() });
    originalWarn.apply(console, args);
  };
  
  console.error = function(...args) {
    window.umgLogs.push({ type: 'error', message: args.join(' '), timestamp: new Date() });
    originalError.apply(console, args);
  };
  
  console.log('✅ 详细日志已启用，日志将保存到 window.umgLogs');
}

// 查看保存的日志
function viewSavedLogs() {
  if (window.umgLogs && window.umgLogs.length > 0) {
    console.log('📋 UMG解析日志:');
    window.umgLogs.forEach((log, index) => {
      const time = log.timestamp.toLocaleTimeString();
      const icon = log.type === 'error' ? '❌' : log.type === 'warn' ? '⚠️' : 'ℹ️';
      console.log(`${index + 1}. [${time}] ${icon} ${log.message}`);
    });
  } else {
    console.log('📋 没有保存的UMG日志');
  }
}

// 重置调试状态
function resetDebugState() {
  console.log('🔄 重置调试状态...');
  delete window.testBuffer;
  delete window.testFileName;
  delete window.lastImportResult;
  delete window.lastImportError;
  delete window.umgLogs;
  console.log('✅ 调试状态已重置');
}

// 导出函数到全局
window.checkParseStatus = checkParseStatus;
window.simulateImport = simulateImport;
window.enableDetailedLogging = enableDetailedLogging;
window.viewSavedLogs = viewSavedLogs;
window.resetDebugState = resetDebugState;

// 显示使用说明
console.log('\n📖 快速诊断工具使用说明:');
console.log('checkParseStatus() - 检查当前解析状态');
console.log('simulateImport() - 手动测试文件导入');
console.log('enableDetailedLogging() - 启用详细日志记录');
console.log('viewSavedLogs() - 查看保存的解析日志');
console.log('resetDebugState() - 重置调试状态');
console.log('\n🎯 建议先运行 enableDetailedLogging()，然后 simulateImport() 测试您的文件'); 